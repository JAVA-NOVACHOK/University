package ua.com.nikiforov.models;

import java.util.ArrayList;
import java.util.List;

import ua.com.nikiforov.models.persons.Student;

public class Group implements Comparable<Group>{

    private long id;
    private String groupName;
    private List<Student> groupStudents;

    public Group() {
    }

    public Group(long id, String groupName) {
        this.id = id;
        this.groupName = groupName;
        groupStudents = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    
    public List<Student> getGroupStudents() {
        return groupStudents;
    }

    public void setGroupStudents(List<Student> groupStudents) {
        this.groupStudents = groupStudents;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((groupName == null) ? 0 : groupName.hashCode());
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Group other = (Group) obj;
        if (groupName == null) {
            if (other.groupName != null)
                return false;
        } else if (!groupName.equals(other.groupName))
            return false;
        if (id != other.id)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "id=" + id + ", groupName=" + groupName + "]";
    }

    @Override
    public int compareTo(Group g) {
        return groupName.compareTo(g.groupName);
    }

}
