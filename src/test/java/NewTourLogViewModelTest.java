import at.jp.tourplanner.event.EventManager;
import at.jp.tourplanner.model.TourLog;
import at.jp.tourplanner.repository.StateRepository;
import at.jp.tourplanner.service.TourLogService;
import at.jp.tourplanner.view.tourlog.NewTourLogView;
import at.jp.tourplanner.viewmodel.tourlog.NewTourLogViewModel;
import at.jp.tourplanner.window.WindowManager;
import org.junit.jupiter.api.Test;
import static jdk.internal.org.objectweb.asm.util.CheckClassAdapter.verify;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NewTourLogViewModelTest {

    @Test
    void addTourLog_ShouldThrowExceptionForEmptyTourLog() throws IllegalAccessException {
        TourLogService tls = new TourLogService(new EventManager(), StateRepository.getInstance());
        TourLog tl = new TourLog();
        tl.tourLogCommentProperty().setValue("");
        tl.tourLogRatingProperty().setValue(2);
        assertThrows(IllegalAccessException.class, () -> {
            tls.add(tl);
        });
    }
    @Test
    void addTourLog_ShouldAddNewTourlog() throws IllegalAccessException {
        TourLogService tls = new TourLogService(new EventManager(),StateRepository.getInstance());
        TourLog tl = new TourLog();
        tl.tourLogCommentProperty().setValue("TestComment");
        tl.tourLogRatingProperty().setValue(4);

        assertDoesNotThrow(() -> tls.add(tl));
    }
}
