package ua.com.nikiforov.dao.subject;

import java.util.List;

import org.springframework.stereotype.Component;

import ua.com.nikiforov.models.Subject;

public interface SubjectDAO {

    public boolean addSubject(String subjectName);

    public Subject getSubjectById(int subjectId);

    public List<Subject> getAllSubjects();

    public boolean updateSubject(String subjectName, int subjectId);

    public boolean deleteSubjectById(int subjectId);
}
