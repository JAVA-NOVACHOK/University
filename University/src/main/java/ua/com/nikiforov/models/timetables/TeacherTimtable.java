package ua.com.nikiforov.models.timetables;

import java.time.Instant;

public class TeacherTimtable extends Timetable {

    private long teacherId;

    public TeacherTimtable(long id, long lessonId, Instant time, long teacherId) {
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
