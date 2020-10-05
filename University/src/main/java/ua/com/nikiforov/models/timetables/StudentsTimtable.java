package ua.com.nikiforov.models.timetables;

public class StudentsTimtable extends Timetable {

    private long studentId;

    public StudentsTimtable(long id, long subjectId, long studentId) {
        super(id, subjectId);
        this.studentId = studentId;
    }

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

}
