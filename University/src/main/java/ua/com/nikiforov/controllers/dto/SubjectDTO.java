package ua.com.nikiforov.controllers.dto;

import java.util.List;

public class SubjectDTO {
    
    private int id;
    private String name;
    private List<TeacherDTO> teachers;
    
    public SubjectDTO() {
    }

    public SubjectDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public SubjectDTO(int id, String name, List<TeacherDTO> teachers) {
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

    public List<TeacherDTO> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<TeacherDTO> teachers) {
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
            if (other.teachers != null)
                return false;
        } 
        return true;
    }

}
