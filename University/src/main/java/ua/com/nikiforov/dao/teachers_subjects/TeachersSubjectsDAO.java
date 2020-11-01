package ua.com.nikiforov.dao.teachers_subjects;

import java.util.List;

import ua.com.nikiforov.models.Subject;
import ua.com.nikiforov.models.persons.Teacher;

public interface TeachersSubjectsDAO {

    public List<Teacher> getTeachers(int subjectId);
    
    public List<Subject> getSubjects(long teacherId);
    
    public boolean assignSubjectToTeacher(long teacherId, int subjectId);

    public boolean unassignSubjectFromTeacher(long teacherId, int subjectId);    
    
    
}
