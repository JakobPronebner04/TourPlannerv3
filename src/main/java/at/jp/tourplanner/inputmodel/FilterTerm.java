package at.jp.tourplanner.inputmodel;

public class FilterTerm {
    private String text;
    private String type;

    public FilterTerm() {
        text = "";
        type = "";
    }
    public FilterTerm(String text, String type) {
        this.text = text;
        this.type = type;
    }

    public void setText(String text) {
        this.text = text;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getText() {
        return text;
    }
    public String getType() {
        return type;
    }
}
