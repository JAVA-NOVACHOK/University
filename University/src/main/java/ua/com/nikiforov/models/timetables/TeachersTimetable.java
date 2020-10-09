package ua.com.nikiforov.models.timetables;

import java.time.Instant;

public class TeachersTimetable extends Timetable {

    private long teacherId;

    public TeachersTimetable() {
    }

    public TeachersTimetable(long id, long lessonId, Instant time, long teacherId) {
        super(id, lessonId, time);
        this.teacherId = teacherId;
    }

    public long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(long teacherId) {
        this.teacherId = teacherId;
    }

}
