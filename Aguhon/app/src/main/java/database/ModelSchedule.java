package database;

public class ModelSchedule {
    public String getId() {
        return id;
    }

    public String getGradeLevel() {
        return gradeLevel;
    }

    public String getScheduleStart() {
        return scheduleStart;
    }

    public String getScheduleEnd() {
        return scheduleEnd;
    }

    public String getDaysOfWeek() {
        return daysOfWeek;
    }

    public String getSubject() {
        return subject;
    }
    public String getSection(){return section;}

    public String getTeacher() {
        return teacher;
    }

    String id;
    String subject;
    String gradeLevel;
    String scheduleStart;
    String scheduleEnd;
    String daysOfWeek;
    String teacher;
    String section;

    public ModelSchedule(String subject,
                         String gradeLevel,
                         String scheduleStart,
                         String scheduleEnd,
                         String daysOfWeek,
                         String teacher,
                         String section) {
        this.subject = subject;
        this.gradeLevel = gradeLevel;
        this.scheduleStart = scheduleStart;
        this.scheduleEnd = scheduleEnd;
        this.daysOfWeek = daysOfWeek;
        this.teacher = teacher;
        this.section = section;
    }
}