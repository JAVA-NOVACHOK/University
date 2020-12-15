package ua.com.nikiforov.models.persons;

import ua.com.nikiforov.models.Group;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="students")
public class Student implements Comparable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "student_id")
    private long id;
    @Column(name="first_name")
    private String firstName;
    @Column(name="last_name")
    private String lastName;


    @ManyToOne(fetch = FetchType.LAZY)
    private Group group;



    public Student() {
    }

    public Student(long id, String firstName, String lastName, long groupId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

//    @Column(name = "first_name")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

//    @Column(name = "last_name")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

//    @Column(name = "group_id")
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
        Student student = (Student)o;
        return this.lastName.compareTo(student.getLastName());
    }
}
