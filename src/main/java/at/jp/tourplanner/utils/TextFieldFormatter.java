package at.jp.tourplanner.utils;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class TextFieldFormatter {

    public static void setTextFieldFormatFloat(TextField textField)
    {
        textField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            int dotCount = 0;

            for (char c : newText.toCharArray()) {
                if (c == '.') {
                    dotCount++;
                    if (dotCount > 1) {
                        return null;
                    }
                } else if (!Character.isDigit(c)) {
                    return null;
                }
            }
            return change;
        }));
    }
}
