package ua.com.nikiforov.services.persons;

import java.util.List;

import ua.com.nikiforov.dto.TeacherDTO;

public interface TeacherService {

    public void addTeacher(TeacherDTO teacher);

    public TeacherDTO getTeacherById(long id);
    
    public TeacherDTO getTeacherByName(String firstName, String lastName);

    public List<TeacherDTO> getTeacherByLikeName(String firstName, String lastName);
    
    public List<TeacherDTO> getAllTeachers();
    
    public List<TeacherDTO> getAllTeachersWithoutSubjects();

    public void updateTeacher(TeacherDTO teacher);

    public void deleteTeacherById(long teacherId);
    
    public void assignSubjectToTeacher( long teacherId,int subjectId);

    public void unassignSubjectFromTeacher( long teacherId,int subjectId);
    
   

}
