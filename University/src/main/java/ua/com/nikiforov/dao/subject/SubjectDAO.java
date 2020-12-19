package ua.com.nikiforov.dao.subject;

import java.util.List;

import ua.com.nikiforov.dto.SubjectDTO;
import ua.com.nikiforov.models.Subject;

public interface SubjectDAO {

    public void addSubject(String subjectName);

    public Subject getSubjectById(int subjectId);
    
    public Subject getSubjectByName(String subjectName);

    public List<Subject> getAllSubjects();

    public void updateSubject(SubjectDTO subject);

    public void deleteSubjectById(int subjectId);
}
