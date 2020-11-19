package ua.com.nikiforov.dao.persons;

import java.util.List;

import ua.com.nikiforov.models.persons.Teacher;

public interface TeacherDAO {

    public boolean addTeacher(String firstName, String lastName);

    public Teacher getTeacherById(long teacherId);
    
    public Teacher getTeacherByName(String firstName, String lastName);

    public List<Teacher> getAllTeachers();

    public boolean updateTeacher(Teacher teacher);

    public boolean deleteTeacherById(long teacherId);
}
