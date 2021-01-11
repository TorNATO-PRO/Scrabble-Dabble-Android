package tornato.dev.ScrabbleDabble;

public class ListItem {

    private String score;
    private String title;

    public ListItem(String score, String title) {
        this.score = score;
        this.title = title;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
