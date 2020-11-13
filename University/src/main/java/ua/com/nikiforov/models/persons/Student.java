package ua.com.nikiforov.models.persons;

public class Student extends Person {

    private long groupId;

    public Student() {
    }

    public Student(long id, String firstName, String lastName, long groupId) {
        super(id, firstName, lastName);
        this.groupId = groupId;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroup(long groupId) {
        this.groupId = groupId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (int) (groupId ^ (groupId >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Student other = (Student) obj;
        if (groupId != other.groupId)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return String.format("Student with ID = %d and firstName = %s, lastname = %s, groupId = %d",
                this.getId(), this.getFirstName(), this.getLastName(), groupId);
    }

}
