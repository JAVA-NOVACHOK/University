package ua.com.nikiforov.dao.persons;

import java.util.List;

import ua.com.nikiforov.models.persons.Teacher;

public interface TeacherDAO {

    public boolean addTeacher(String firstName, String lastName);

    public Teacher getTeacherById(long teacherId);

    public List<Teacher> getAllTeachers();

    public boolean updateTeacher(String firstName, String lastName,long teacherId);

    public boolean deleteTeacherById(long teacherId);
}
