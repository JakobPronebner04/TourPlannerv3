package at.jp.tourplanner.service;

import at.jp.tourplanner.dataaccess.StateDataAccess;
import at.jp.tourplanner.entity.TourEntity;
import at.jp.tourplanner.entity.TourLogEntity;
import at.jp.tourplanner.repository.TourRepositoryORM;
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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class ExportService {
    private final TourRepositoryORM tourRepository;
    private final StateDataAccess stateDataAccess;

    public ExportService(TourRepositoryORM tourRepositoryORM, StateDataAccess stateDataAccess) {
        this.tourRepository = tourRepositoryORM;
        this.stateDataAccess = stateDataAccess;
    }

    public void exportSingleTourAsPDF(BufferedImage image, String outputPdf) throws IOException {
        Optional<TourEntity> tourOpt = tourRepository
                .findByName(stateDataAccess.getSelectedTour().getTourName());

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

            document.save(outputPdf);
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
}