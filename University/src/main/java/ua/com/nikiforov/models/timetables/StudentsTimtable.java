package ua.com.nikiforov.models.timetables;

import java.time.Instant;

public class StudentsTimtable extends Timetable {

    private long studentId;

    public StudentsTimtable(long id, long subjectId, Instant time, long studentId) {
        super(id, subjectId, time);
        this.studentId = studentId;
    }

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

}
