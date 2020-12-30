package ua.com.nikiforov.dto;

import java.util.Set;
import java.util.TreeSet;

public class SubjectDTO implements Comparable<SubjectDTO>{

    private int id;
    private String name;
    private Set<TeacherDTO> teachers = new TreeSet<>();

    public SubjectDTO() {

    }

    public SubjectDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public SubjectDTO(int id, String name, Set<TeacherDTO> teachers) {
        this.id = id;
        this.name = name;
        this.teachers = teachers;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addTeacher(TeacherDTO teacher) {
        teachers.add(teacher);
    }

    public Set<TeacherDTO> getTeachers() {
        return teachers;
    }

    public void setTeachers(Set<TeacherDTO> teachers) {
        this.teachers = teachers;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((teachers == null || teachers.isEmpty()) ? 0 : teachers.hashCode());
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
        SubjectDTO other = (SubjectDTO) obj;
        if (id != other.id)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (teachers == null) {
            return other.teachers == null;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SubjectDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", teachers=" + teachers +
                '}';
    }

    @Override
    public int compareTo(SubjectDTO subjectDTO) {
        return this.name.compareTo(subjectDTO.getName());
    }
}
