package ua.com.nikiforov.services.subject;

import java.util.List;

import ua.com.nikiforov.dto.SubjectDTO;

public interface SubjectService {

    public SubjectDTO addSubject(SubjectDTO subjectDTO);

    public SubjectDTO getSubjectById(int subjectId);

    public SubjectDTO getSubjectByName(String subjectName);

    public List<SubjectDTO> getAllSubjects();

    public SubjectDTO updateSubject(SubjectDTO subject);

    public void deleteSubjectById(int subjectId);

}
