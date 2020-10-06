package ua.com.nikiforov.models.persons;

public class Student extends Person {

    private long groupId;

    public Student(long id, String firstName, String lastName, int groupId) {
        super(id, firstName, lastName);
        this.groupId = groupId;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroup(int groupId) {
        this.groupId = groupId;
    }

}
