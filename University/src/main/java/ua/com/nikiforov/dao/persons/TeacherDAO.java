package ua.com.nikiforov.dao.persons;

import java.util.List;

import ua.com.nikiforov.controllers.dto.TeacherDTO;
import ua.com.nikiforov.models.persons.Teacher;

public interface TeacherDAO {

    public boolean addTeacher(String firstName, String lastName);

    public Teacher getTeacherById(long teacherId);
    
    public Teacher getTeacherByName(String firstName, String lastName);
    
    public List<Teacher> getTeacherByLikeName(String firstName, String lastName);

    public List<Teacher> getAllTeachers();
    
    public boolean updateTeacher(TeacherDTO teacher);

    public boolean deleteTeacherById(long teacherId);
}
