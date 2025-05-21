package at.jp.tourplanner.viewmodel;

import at.jp.tourplanner.service.ExceptionService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import net.bytebuddy.build.Plugin;

public class ExceptionViewModel {
    private final ExceptionService exceptionService;
    private StringProperty messageProperty = new SimpleStringProperty("");
    public ExceptionViewModel(ExceptionService exceptionService) {
        this.exceptionService = exceptionService;
        messageProperty.setValue(exceptionService.getCurrentExceptionMessage());
    }

    public StringProperty getMessageProperty()
    {
        return messageProperty;
    }
}
