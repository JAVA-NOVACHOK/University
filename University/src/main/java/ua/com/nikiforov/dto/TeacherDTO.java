package ua.com.nikiforov.dto;

import java.util.ArrayList;
import java.util.List;

public class TeacherDTO implements  Comparable<TeacherDTO>{

    private long id;
    private String firstName;
    private String lastName;
    private List<SubjectDTO> subjects = new ArrayList<>();

    public TeacherDTO(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        subjects = new ArrayList<>();
    }

    public TeacherDTO(long id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        subjects = new ArrayList<>();
    }

    public TeacherDTO() {
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
    
    public List<SubjectDTO> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<SubjectDTO> subjects) {
        this.subjects = subjects;
    }

    public void addSubject(SubjectDTO subject) {
        subjects.add(subject);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
        result = prime * result + ((subjects == null || subjects.isEmpty()) ? 0 : subjects.hashCode());
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
        TeacherDTO other = (TeacherDTO) obj;
        if (firstName == null) {
            if (other.firstName != null)
                return false;
        } else if (!firstName.equals(other.firstName))
            return false;
        if (id != other.id)
            return false;
        if (lastName == null) {
            if (other.lastName != null)
                return false;
        } else if (!lastName.equals(other.lastName))
            return false;
        if (subjects == null) {
            return other.subjects == null;
        } 
        return true;
    }

    @Override
    public String toString() {
        return "TeacherDTO{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", subjects=" + subjects +
                '}';
    }

    @Override
    public int compareTo(TeacherDTO teacherDTO) {
        return this.lastName.compareTo(teacherDTO.getLastName());
    }
}