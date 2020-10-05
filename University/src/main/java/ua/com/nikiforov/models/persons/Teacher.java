package ua.com.nikiforov.models.persons;

public class Teacher extends Person {

    private long subjectId;

    public Teacher(long id, String firstName, String lastName, int subjectId) {
        super(id, firstName, lastName);
        this.subjectId = subjectId;
    }

    public long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

}
