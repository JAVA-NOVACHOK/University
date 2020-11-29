package ua.com.nikiforov.services.subject;

import java.util.List;

import ua.com.nikiforov.controllers.dto.SubjectDTO;
import ua.com.nikiforov.models.Subject;

public interface SubjectService {

    public boolean addSubject(String subjectName);

    public Subject getSubjectById(int subjectId);

    public Subject getSubjectByName(String subjectName);

    public List<Subject> getAllSubjects();
    
    public List<Subject> getAllSubjectsWithoutTeachers();

    public boolean updateSubject(SubjectDTO subject);

    public boolean deleteSubjectById(int subjectId);
}
