package ua.com.nikiforov.dao.subject;

import java.util.List;

import ua.com.nikiforov.models.Subject;

public interface SubjectDAO {

    public boolean addSubject(String subjectName);

    public Subject getSubjectById(int subjectId);
    
    public Subject getSubjectByName(String subjectName);

    public List<Subject> getAllSubjects();

    public boolean updateSubject(Subject subject);

    public boolean deleteSubjectById(int subjectId);
}
