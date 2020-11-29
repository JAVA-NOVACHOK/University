package ua.com.nikiforov.services.persons;

import java.util.List;

import ua.com.nikiforov.controllers.dto.TeacherDTO;
import ua.com.nikiforov.models.persons.Teacher;

public interface TeacherService {

    public boolean addTeacher(String firstName, String lastName);

    public Teacher getTeacherById(long id);
    
    public Teacher getTeacherByName(String firstName, String lastName);

    public List<Teacher> getTeacherByLikeName(String firstName, String lastName);
    
    public List<Teacher> getAllTeachers();
    
    public List<Teacher> getAllTeachersWithoutSubjects();

    public boolean updateTeacher(TeacherDTO teacher);

    public boolean deleteTeacherById(long teacherId);
    
    public boolean assignSubjectToTeacher(int subjectId, long teacherId);

    public boolean unassignSubjectFromTeacher(int subjectId, long teacherId);

}
