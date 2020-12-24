package ua.com.nikiforov.services.subject;

import java.util.List;

import ua.com.nikiforov.dto.SubjectDTO;

public interface SubjectService {

    public void addSubject(String subjectName);

    public SubjectDTO getSubjectById(int subjectId);

    public SubjectDTO getSubjectByName(String subjectName);

    public List<SubjectDTO> getAllSubjects();

    public void updateSubject(SubjectDTO subject);

    public void deleteSubjectById(int subjectId);

}
