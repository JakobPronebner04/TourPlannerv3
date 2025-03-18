package at.jp.tourplanner.utils;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class ControlsFormatter {

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
    public static <T> void setTableColumnCutOff(TableColumn<T, String> tableColumn) {
        tableColumn.setCellFactory(column -> new TableCell<T, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.split("\n")[0]);
                }
            }
        });
    }

}
