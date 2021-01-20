package ua.com.nikiforov.services.persons;

import ua.com.nikiforov.dto.TeacherDTO;

import java.util.List;

public interface TeacherService {

    public TeacherDTO addTeacher(TeacherDTO teacher);

    public TeacherDTO getTeacherById(long id);
    
    public TeacherDTO getTeacherByName(String firstName, String lastName);

    public List<TeacherDTO> getTeacherByLikeName(String firstName, String lastName);


    public List<TeacherDTO> getAllTeachers();
    
    public TeacherDTO updateTeacher(TeacherDTO teacher);

    public void deleteTeacherById(long teacherId);
    
    public TeacherDTO assignSubjectToTeacher(long teacherId, int subjectId);

    public TeacherDTO unassignSubjectFromTeacher( long teacherId,int subjectId);
    
   

}
