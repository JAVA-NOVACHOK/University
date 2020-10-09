package ua.com.nikiforov.dao.subject;

import java.util.List;

import ua.com.nikiforov.models.Subject;

public interface SublectDAO {

    public boolean addSubject(String subjectName);

    public Subject getSubjectById(int id);

    public List<Subject> getAllSubjects();

    public boolean updateSubject(String subjectName, int id);

    public boolean deleteSubjectById(int id);
}
