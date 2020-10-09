package ua.com.nikiforov.dao.persons;

import java.util.List;

import ua.com.nikiforov.models.persons.Teacher;

public interface TeacherDAO {

    public boolean addTeacher(String firstName, String lastName, long groupId);

    public Teacher getTeacherById(long id);

    public List<Teacher> getAllTeachers();

    public boolean updateTeacher(String firstName, String lastName, long groupId);

    public boolean deleteTeacherById(long id);
}
