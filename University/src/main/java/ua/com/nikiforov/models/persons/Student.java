package ua.com.nikiforov.models.persons;

import ua.com.nikiforov.models.Group;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "students", uniqueConstraints = @UniqueConstraint(columnNames = {"first_name", "last_name", "group_id"}))
public class Student implements Comparable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "student_id")
    private long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id")
    private Group group;

    public Student() {
    }

    public Student(long id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Student(String firstName, String lastName, Group group) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.group = group;
    }

    public Student(long id, String firstName, String lastName, Group group) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.group = group;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getGroupId() {
        return group.getGroupId();
    }

    public void setGroup(long groupId) {
        group.setGroupId(groupId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return id == student.id &&
                firstName.equals(student.firstName) &&
                lastName.equals(student.lastName) &&
                group.equals(student.group);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, group);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", group=" + group +
                '}';
    }


    @Override
    public int compareTo(Object o) {
        Student student = (Student) o;
        return this.lastName.compareTo(student.getLastName());
    }
}
