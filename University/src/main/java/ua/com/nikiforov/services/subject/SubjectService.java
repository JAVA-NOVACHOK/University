package ua.com.nikiforov.services.subject;

import java.util.List;

import ua.com.nikiforov.models.Subject;

public interface SubjectService {

    public boolean addSubject(String subjectName);

    public Subject getSubjectById(int subjectId);

    public Subject getSubjectByName(String subjectName);

    public List<Subject> getAllSubjects();

    public boolean updateSubject(String subjectName, int subjectId);

    public boolean deleteSubjectById(int subjectId);
}
