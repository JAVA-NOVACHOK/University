package ua.com.nikiforov.models.timetables;

public class TeacherTimtable extends Timetable {

    private long teacherId;

    public TeacherTimtable(long id, long subjectId, long teacherId) {
        super(id, subjectId);
        this.teacherId = teacherId;
    }

    public long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(long teacherId) {
        this.teacherId = teacherId;
    }

}
