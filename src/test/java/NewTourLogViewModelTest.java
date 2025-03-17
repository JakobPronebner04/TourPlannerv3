import at.jp.tourplanner.event.EventManager;
import at.jp.tourplanner.model.TourLog;
import at.jp.tourplanner.repository.StateRepository;
import at.jp.tourplanner.service.TourLogService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NewTourLogViewModelTest {

    @Test
    void addTourLog_ShouldThrowExceptionForEmptyTourLog() throws IllegalAccessException {
        TourLogService tls = new TourLogService(new EventManager(), StateRepository.getInstance());
        TourLog tl = new TourLog();
        tl.setTourLogComment("");
        tl.setTourLogRating(2);
        assertThrows(IllegalAccessException.class, () -> {
            tls.add(tl);
        });
    }
    @Test
    void addTourLog_ShouldAddNewTourlog() throws IllegalAccessException {
        TourLogService tls = new TourLogService(new EventManager(),StateRepository.getInstance());
        TourLog tl = new TourLog();
        tl.setTourLogComment("TestComment");
        tl.setTourLogRating(2);

        assertDoesNotThrow(() -> tls.add(tl));
    }
}
