package database;

public class ModelAnnouncement {
    public String getId() {
        return id;
    }

    public String gettitle() {
        return title;
    }

    public String getdetail() {
        return detail;
    }

    public String getTargetGrade() {
        return targetGrade;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    String id;
    String title;
    String detail;
    String targetGrade;
    String date;
    String time;

    public ModelAnnouncement(String title, String detail, String date, String time, String targetGrade) {
        this.title = title;
        this.detail = detail;
        this.date = date;
        this.time = time;
        this.targetGrade = targetGrade;
    }
}