package ua.com.nikiforov.services.persons;

import java.util.ArrayList;
import java.util.List;

import ua.com.nikiforov.controllers.dto.TeacherDTO;
import ua.com.nikiforov.models.persons.Teacher;

public interface TeacherService {

    public boolean addTeacher(TeacherDTO teacher);

    public TeacherDTO getTeacherById(long id);
    
    public TeacherDTO getTeacherByName(String firstName, String lastName);

    public List<TeacherDTO> getTeacherByLikeName(String firstName, String lastName);
    
    public List<TeacherDTO> getAllTeachers();
    
    public List<TeacherDTO> getAllTeachersWithoutSubjects();

    public boolean updateTeacher(TeacherDTO teacher);

    public boolean deleteTeacherById(long teacherId);
    
    public boolean assignSubjectToTeacher(int subjectId, long teacherId);

    public boolean unassignSubjectFromTeacher(int subjectId, long teacherId);
    
   

}
