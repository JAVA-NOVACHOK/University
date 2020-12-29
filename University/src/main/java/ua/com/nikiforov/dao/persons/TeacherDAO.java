package ua.com.nikiforov.dao.persons;

import java.util.List;

import ua.com.nikiforov.dto.TeacherDTO;
import ua.com.nikiforov.models.persons.Teacher;

public interface TeacherDAO {

    public void addTeacher(TeacherDTO teacherDTO);

    public Teacher getTeacherById(long teacherId);
    
    public Teacher getTeacherByName(String firstName, String lastName);
    
    public List<Teacher> getTeacherByLikeName(String firstName, String lastName);

    public List<Teacher> getAllTeachers();
    
    public void updateTeacher(TeacherDTO teacher);

    public void deleteTeacherById(long teacherId);

    public Teacher assignSubjectToTeacher(long teacherId, int subjectId);

    public Teacher unassignSubjectFromTeacher(long teacherId, int subjectId);
}
