package ua.com.nikiforov.models;

public class TeachersSubjects {

    private long teachersId;
    private int subjectId;

    public TeachersSubjects() {
    }

    public TeachersSubjects(long teachersId, int subjectId) {
        this.teachersId = teachersId;
        this.subjectId = subjectId;
    }

    public long getTeachersId() {
        return teachersId;
    }

    public void setTeachersId(long teachersId) {
        this.teachersId = teachersId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

}
