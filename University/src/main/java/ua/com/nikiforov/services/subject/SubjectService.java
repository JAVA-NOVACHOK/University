package ua.com.nikiforov.services.subject;

import java.util.ArrayList;
import java.util.List;

import ua.com.nikiforov.controllers.dto.SubjectDTO;
import ua.com.nikiforov.models.Subject;

public interface SubjectService {

    public boolean addSubject(String subjectName);

    public SubjectDTO getSubjectById(int subjectId);

    public SubjectDTO getSubjectByName(String subjectName);

    public List<SubjectDTO> getAllSubjects();

    public List<SubjectDTO> getAllSubjectsWithoutTeachers();

    public boolean updateSubject(SubjectDTO subject);

    public boolean deleteSubjectById(int subjectId);

   
}
