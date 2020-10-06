package ua.com.nikiforov.models.timetables;

import java.time.Instant;

public class StudentsTimtable extends Timetable {

    private long studentId;

    public StudentsTimtable(long id, long lessonId, Instant time, long studentId) {
        super(id, lessonId, time);
        this.studentId = studentId;
    }

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

}
