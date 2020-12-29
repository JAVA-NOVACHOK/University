package ua.com.nikiforov.services.persons;

import java.util.List;

import ua.com.nikiforov.dto.TeacherDTO;
import ua.com.nikiforov.models.persons.Teacher;

public interface TeacherService {

    public void addTeacher(TeacherDTO teacher);

    public TeacherDTO getTeacherById(long id);
    
    public TeacherDTO getTeacherByName(String firstName, String lastName);

    public List<TeacherDTO> getTeacherByLikeName(String firstName, String lastName);
    
    public List<TeacherDTO> getAllTeachers();
    
    public void updateTeacher(TeacherDTO teacher);

    public void deleteTeacherById(long teacherId);
    
    public Teacher assignSubjectToTeacher(long teacherId, int subjectId);

    public Teacher unassignSubjectFromTeacher( long teacherId,int subjectId);
    
   

}
