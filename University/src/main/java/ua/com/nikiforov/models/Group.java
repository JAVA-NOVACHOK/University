package ua.com.nikiforov.models;

import java.util.List;

import ua.com.nikiforov.models.persons.Student;

import javax.persistence.*;


@Entity
@Table(name = "groups", uniqueConstraints = @UniqueConstraint(columnNames = {"group_name"}))
public class Group implements Comparable<Group> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "group_id")
    private long groupId;
    @Column(name = "group_name")
    private String groupName;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id")
    private List<Student> groupStudents;

    public Group() {
    }

    public Group(long groupId) {
        this.groupId = groupId;
    }

    public Group(String groupName) {
        this.groupName = groupName;
    }

    public Group(long id, String groupName) {
        this.groupId = id;
        this.groupName = groupName;
    }

    public Group(long groupId, String groupName, List<Student> groupStudents) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupStudents = groupStudents;

    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
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

    public void addStudent(Student student) {
        groupStudents.add(student);
    }

    public void setGroupStudents(List<Student> groupStudents) {
        this.groupStudents = groupStudents;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((groupName == null) ? 0 : groupName.hashCode());
        result = prime * result + (int) (groupId ^ (groupId >>> 32));
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
        return groupId == other.groupId;
    }

    @Override
    public String toString() {
        return "id=" + groupId + ", groupName=" + groupName + "]";
    }

    @Override
    public int compareTo(Group g) {
        return groupName.compareTo(g.groupName);
    }

}

