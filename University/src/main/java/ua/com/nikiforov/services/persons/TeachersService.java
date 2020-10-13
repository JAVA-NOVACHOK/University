package ua.com.nikiforov.services.persons;

import java.util.List;

import ua.com.nikiforov.models.persons.Teacher;

public interface TeachersService {

    public boolean addTeacher(String firstName, String lastName);

    public Teacher getTeacherById(long id);
    
    public Teacher getTeacherByName(String firstName, String lastName);

    public List<Teacher> getAllTeachers();

    public boolean updateTeacher(String firstName, String lastName, long teacherId);

    public boolean deleteTeacherById(long teacherId);
    
    public boolean assignSubjectToTeacher(int subjectId, long teacherId);

}
