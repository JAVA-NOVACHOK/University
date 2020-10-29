<<<<<<< HEAD
package ua.com.nikiforov.models;

import java.util.ArrayList;
import java.util.List;

import ua.com.nikiforov.models.persons.Teacher;

public class Subject {

    private int id;
    private String name;
    private List<Teacher> teachers;

    public Subject() {
        teachers = new ArrayList<>();
    }

    public Subject(int id, String name) {
        this.id = id;
        this.name = name;
        teachers = new ArrayList<>();
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

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        Subject other = (Subject) obj;
        if (id != other.id)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

}
=======
package ua.com.nikiforov.models;

import java.util.ArrayList;
import java.util.List;

public class Subject {

    private int id;
    private String name;
    private List<Long> teachersIds;

    public Subject() {
    }

    public Subject(int id, String name) {
        this.id = id;
        this.name = name;
        teachersIds = new ArrayList<>();
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

    public List<Long> getSubjectTeachersIds() {
        return teachersIds;
    }

    public void setSubjectTeacherIds(List<Long> subjectTeacherIds) {
        this.teachersIds = subjectTeacherIds;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((teachersIds == null) ? 0 : teachersIds.hashCode());
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
        Subject other = (Subject) obj;
        if (id != other.id)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (teachersIds == null) {
            if (other.teachersIds != null)
                return false;
        } else if (!teachersIds.equals(other.teachersIds))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "id=" + id + ", name=" + name + ", teachersIds=" + teachersIds;
    }

}
>>>>>>> refs/remotes/origin/master
