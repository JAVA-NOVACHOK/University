package ua.com.nikiforov.models.persons;

public class Student extends Person {

    private int groupId;

    public Student(int id, String firstName, String lastName,int groupId) {
        super(id, firstName, lastName);
        this.groupId = groupId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroup(int groupId) {
        this.groupId = groupId;
    }

}
