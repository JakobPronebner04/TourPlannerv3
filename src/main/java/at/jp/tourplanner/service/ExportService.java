package at.jp.tourplanner.service;

import at.jp.tourplanner.dataaccess.StateDataAccess;
import at.jp.tourplanner.exception.TourNotFoundException;
import at.jp.tourplanner.service.importexport.TourTransferModel;
import at.jp.tourplanner.entity.TourEntity;
import at.jp.tourplanner.entity.TourLogEntity;
import at.jp.tourplanner.inputmodel.Tour;
import at.jp.tourplanner.inputmodel.TourLog;
import at.jp.tourplanner.repository.TourRepositoryORM;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ExportService {
    private final TourRepositoryORM tourRepository;
    private final StateDataAccess stateDataAccess;
    private final ObjectMapper objectMapper;
    public ExportService(TourRepositoryORM tourRepositoryORM, StateDataAccess stateDataAccess) {
        this.tourRepository = tourRepositoryORM;
        this.stateDataAccess = stateDataAccess;
        this.objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .enable(SerializationFeature.INDENT_OUTPUT)
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public void exportSingleTourAsPDF(BufferedImage image) throws IOException {
        Optional<TourEntity> tourOpt = tourRepository
                .findByName(stateDataAccess.getSelectedTour().getTourName());

        if(tourOpt.isEmpty()) throw new TourNotFoundException("Tour not found!");

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDImageXObject pdImage = LosslessFactory.createFromImage(document, image);

            try (PDPageContentStream content = new PDPageContentStream(document, page)) {
                float margin    = 50;
                float startX    = margin;
                float startY    = page.getMediaBox().getHeight() - margin;
                float padding   = 10;

                if (tourOpt.isPresent()) {
                    TourEntity tour = tourOpt.get();

                    PDType1Font titleFont     = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
                    float titleFontSize = 18;
                    float titleLeading  = titleFontSize * 1.2f;
                    content.beginText();
                    try {
                        content.setFont(titleFont, titleFontSize);
                        content.newLineAtOffset(startX, startY);
                        content.showText("Tour of " + tour.getName().replaceAll("\\p{Cntrl}", " "));
                    } finally {
                        content.endText();
                    }

                    float attrStartY = startY - titleLeading - padding;
                    float currentY = writeDynamicAttributes(content,
                            getTourAttributes(tour), startX, attrStartY, margin);

                    float imageHeight = pdImage.getHeight();
                    float imageX      = startX;
                    float imageY      = currentY - padding - imageHeight;
                    content.drawImage(pdImage, imageX, imageY);

                    float logsStartY = imageY - padding - 20;
                    for (TourLogEntity log : tour.getTourLogs()) {
                        currentY   = writeDynamicAttributes(content,
                                getTourLogAttributes(log), startX, logsStartY, margin);
                        logsStartY = currentY - padding;
                    }
                }
            }
            Path imagesDir = Paths.get("singletours");
            if (Files.notExists(imagesDir)) {
                Files.createDirectories(imagesDir);
            }
            String fileName =  tourOpt.get().getName() + "_" + UUID.randomUUID() + ".pdf";
            Path filePath = imagesDir.resolve(fileName);
            document.save(filePath.toFile());
        }
    }

    public void exportStatisticalSummaryReport() throws IOException {
        List<TourEntity> allTours = tourRepository.findAll();

        if (allTours.isEmpty()) {
            throw new TourNotFoundException("No tours available for summary export.");
        }

        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        PDPageContentStream content = new PDPageContentStream(document, page);
        try {
            float margin = 50;
            float startX = margin;
            float startY = page.getMediaBox().getHeight() - margin;
            float padding = 10;
            float currentY = startY;

            PDType1Font titleFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
            PDType1Font bodyFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
            float titleFontSize = 16;
            float bodyFontSize = 12;
            float lineHeight = bodyFontSize * 1.5f;

            content.setFont(bodyFont, bodyFontSize);

            content.beginText();
            content.setFont(titleFont, titleFontSize + 2);
            content.newLineAtOffset(startX, currentY);
            content.showText("Tour Summary Statistics Report");
            content.endText();
            currentY -= (titleFontSize + 10);

            for (TourEntity tour : allTours) {
                if (currentY < margin + 100) {
                    content.close();
                    page = new PDPage(PDRectangle.A4);
                    document.addPage(page);
                    content = new PDPageContentStream(document, page);
                    currentY = page.getMediaBox().getHeight() - margin;
                }

                content.beginText();
                content.setFont(titleFont, titleFontSize);
                content.newLineAtOffset(startX, currentY);
                content.showText("Tour: " + tour.getName());
                content.endText();
                currentY -= lineHeight;

                List<TourLogEntity> logs = tour.getTourLogs();

                if (logs.isEmpty()) {
                    content.beginText();
                    content.setFont(bodyFont, bodyFontSize);
                    content.newLineAtOffset(startX, currentY);
                    content.showText("  No logs available for this tour.");
                    content.endText();
                    currentY -= lineHeight * 2;
                    continue;
                }

                double avgTime = logs.stream().mapToDouble(TourLogEntity::getActualTime).average().orElse(0.0);
                double avgDistance = logs.stream().mapToDouble(TourLogEntity::getActualDistance).average().orElse(0.0);
                double avgRating = logs.stream().mapToDouble(TourLogEntity::getRating).average().orElse(0.0);

                Map<String, String> stats = new LinkedHashMap<>();
                stats.put("Popularity [Amount of tourlogs]",String.format("%d",tour.getPopularity()));
                stats.put("  Average Duration [h]", String.format("%.2f", avgTime));
                stats.put("  Average Distance [km]", String.format("%.2f", avgDistance));
                stats.put("  Average Rating", String.format("%.2f", avgRating));

                for (Map.Entry<String, String> entry : stats.entrySet()) {
                    content.beginText();
                    content.setFont(bodyFont, bodyFontSize);
                    content.newLineAtOffset(startX, currentY);
                    content.showText(entry.getKey() + ": " + entry.getValue());
                    content.endText();
                    currentY -= lineHeight;
                }

                currentY -= lineHeight;
            }
        } finally {
            content.close();
            Path outputDir = Paths.get("summaryReports");
            if (Files.notExists(outputDir)) {
                Files.createDirectories(outputDir);
            }

            String fileName = "TourSummaryReport_" + UUID.randomUUID() + ".pdf";
            Path filePath = outputDir.resolve(fileName);
            document.save(filePath.toFile());
            document.close();
        }
    }


    private Map<String, String> getTourAttributes(TourEntity tour) {
        Map<String, String> attributes = new LinkedHashMap<>();
        attributes.put("Description", tour.getDescription());
        attributes.put("Start", tour.getStart());
        attributes.put("Destination", tour.getDestination());
        attributes.put("Transport type", tour.getTransportType());
        attributes.put("Distance", ((int) tour.getDistance() / 1000) + " km");
        attributes.put("Duration", ((int) tour.getDuration() / 3600) + " hours " +
                ((int) ((tour.getDuration() % 3600) / 60)) + " minutes");
        return attributes;
    }

    private Map<String, String> getTourLogAttributes(TourLogEntity tourlog) {
        Map<String, String> attributes = new LinkedHashMap<>();
        attributes.put("Comment", tourlog.getComment());
        attributes.put("Actual Distance needed [km]", String.valueOf(tourlog.getActualDistance()));
        attributes.put("Actual Duration needed[h]", String.valueOf(tourlog.getActualTime()));
        attributes.put("Rating", String.valueOf(tourlog.getRating()));
        attributes.put("Difficulty",String.valueOf(tourlog.getDifficulty()));
        return attributes;
    }

    private float writeDynamicAttributes(PDPageContentStream content,
                                         Map<String, String> attributes,
                                         float startX,
                                         float startY,
                                         float margin) throws IOException {
        PDType1Font keyFont   = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
        PDType1Font valueFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

        float fontSize  = 12;
        float leading   = fontSize * 1.2f;
        float maxKeyWidth = 0;

        for (String key : attributes.keySet()) {
            float w = keyFont.getStringWidth(key + ":") / 1000 * fontSize;
            if (w > maxKeyWidth) maxKeyWidth = w;
        }
        float valueX = startX + maxKeyWidth + 10;
        float pageWidth = PDRectangle.A4.getWidth();
        float maxWidth  = pageWidth - margin - valueX;

        float y = startY;
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            String safeKey   = entry.getKey().replaceAll("\\p{Cntrl}", " ");
            String safeValue = entry.getValue().replaceAll("\\p{Cntrl}", " ");

            content.beginText();
            try {
                content.setFont(keyFont, fontSize);
                content.newLineAtOffset(startX, y);
                content.showText(safeKey + ":");
            } finally {
                content.endText();
            }

            if (entry.getKey().equals("Description") || entry.getKey().equals("Comment")) {
                y = writeWrappedText(
                        content,
                        safeValue,
                        valueFont,
                        fontSize,
                        valueX,
                        y,
                        maxWidth,
                        leading
                );
            }
            else{
                content.beginText();
                try {
                    content.setFont(valueFont, fontSize);
                    content.newLineAtOffset(valueX, y);
                    content.showText(safeValue);
                } finally {
                    content.endText();
                }
                y -= leading;
            }
        }
        return y;
    }

    private float writeWrappedText(PDPageContentStream content,
                                   String text,
                                   PDType1Font font,
                                   float fontSize,
                                   float startX,
                                   float startY,
                                   float maxWidth,
                                   float leading) throws IOException {
        String[] words = text.split("\\s+");
        StringBuilder line = new StringBuilder();
        float y = startY;

        for (String word : words) {
            String candidate = line.length() == 0 ? word : line + " " + word;
            float width = font.getStringWidth(candidate) / 1000 * fontSize;
            if (width > maxWidth && line.length() > 0) {

                content.beginText();
                content.setFont(font, fontSize);
                content.newLineAtOffset(startX, y);
                content.showText(line.toString());
                content.endText();
                y -= leading;
                line = new StringBuilder(word);
            } else {
                line = new StringBuilder(candidate);
            }
        }
        if (line.length() > 0) {
            content.beginText();
            content.setFont(font, fontSize);
            content.newLineAtOffset(startX, y);
            content.showText(line.toString());
            content.endText();
            y -= leading;
        }
        return y;
    }

    public void exportSingleTourAsJSON() throws IOException {
        Optional<TourEntity> tourOpt = tourRepository
                .findByName(stateDataAccess.getSelectedTour().getTourName());
        if (tourOpt.isEmpty()) throw new TourNotFoundException("Tour not found!");

        TourEntity tourEntity = tourOpt.get();

        Tour tour = new Tour();
        tour.setTourName(tourEntity.getName());
        tour.setTourDescription(tourEntity.getDescription());
        tour.setTourStart(tourEntity.getStart());
        tour.setTourDestination(tourEntity.getDestination());
        tour.setTourTransportType(tourEntity.getTransportType());

        TourTransferModel exportData = getTourTransferModel(tourEntity, tour);

        Path imagesDir = Paths.get("jsonExportedTours");
        if (Files.notExists(imagesDir)) {
            Files.createDirectories(imagesDir);
        }
        String fileName = UUID.randomUUID() + ".json";
        Path filePath = imagesDir.resolve(fileName);

        objectMapper.writerWithDefaultPrettyPrinter().writeValue(filePath.toFile(), exportData);
    }

    private static TourTransferModel getTourTransferModel(TourEntity tourEntity, Tour tour) {
        List<TourLog> tourLogs = new ArrayList<>();
        for (TourLogEntity logEntity : tourEntity.getTourLogs()) {
            TourLog log = new TourLog();
            log.setComment(logEntity.getComment());
            log.setRating(logEntity.getRating());
            log.setDifficulty(logEntity.getDifficulty());
            log.setActualDistance(logEntity.getActualDistance());
            log.setActualTime(logEntity.getActualTime());
            tourLogs.add(log);
        }

        return new TourTransferModel(tour, tourLogs);
    }
}