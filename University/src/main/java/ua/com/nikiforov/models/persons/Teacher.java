package ua.com.nikiforov.models.persons;

public class Teacher extends Person {

    private int subjectId;

    public Teacher(long id, String firstName, String lastName, int subjectId) {
        super(id, firstName, lastName);
        this.subjectId = subjectId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

}
