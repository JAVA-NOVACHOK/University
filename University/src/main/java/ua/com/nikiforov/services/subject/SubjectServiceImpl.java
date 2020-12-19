package ua.com.nikiforov.services.subject;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import ua.com.nikiforov.dao.persons.TeacherDAO;
import ua.com.nikiforov.dto.SubjectDTO;
import ua.com.nikiforov.dto.TeacherDTO;
import ua.com.nikiforov.dao.subject.SubjectDAO;
import ua.com.nikiforov.models.Subject;
import ua.com.nikiforov.models.persons.Teacher;

@Service
public class SubjectServiceImpl implements SubjectService {

    private SubjectDAO subjectDAO;

    @Autowired
    public SubjectServiceImpl(SubjectDAO subjectDAO) {
        this.subjectDAO = subjectDAO;
    }

    @Override
    public void addSubject(String subjectName) {
        try {
            subjectDAO.addSubject(subjectName);
        }catch (DataIntegrityViolationException e){
            throw new DuplicateKeyException("Error! Duplicate Subject while adding",e);
        }
    }

    @Override
    public SubjectDTO getSubjectById(int subjectId) {
        return getSubjectDTO(subjectDAO.getSubjectById(subjectId));

    }

    @Override
    public SubjectDTO getSubjectByName(String subjectName) {
        return getSubjectDTO(subjectDAO.getSubjectByName(subjectName));

    }

    @Override
    public List<SubjectDTO> getAllSubjects() {
        return getSubjectDTOList(subjectDAO.getAllSubjects());
    }

    @Override
    public List<SubjectDTO> getAllSubjectsWithoutTeachers() {
        return getSubjectDTOList(subjectDAO.getAllSubjects());
    }

    @Override
    public void updateSubject(SubjectDTO subject) {
        try {
            subjectDAO.updateSubject(subject);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateKeyException("Error! Duplicate group while editing!", e);
        }
    }

    @Override
    public void deleteSubjectById(int subjectId) {
        subjectDAO.deleteSubjectById(subjectId);
    }

    private List<SubjectDTO> getSubjectDTOList(List<Subject> subjects) {
        List<SubjectDTO> subjectsDTO = new ArrayList<>();
        for (Subject subject : subjects) {
            subjectsDTO.add(getSubjectDTO(subject));
        }
        return subjectsDTO;
    }

    private SubjectDTO getSubjectDTO(Subject subject) {
        int subjectId = subject.getId();
        String name = subject.getName();
        List<Teacher> teachers = subject.getTeachers();
        List<TeacherDTO> teachersDTO = getTeachersDTOList(teachers);
        return new SubjectDTO(subjectId, name, teachersDTO);
    }

    private List<TeacherDTO> getTeachersDTOList(List<Teacher> teachers) {
        List<TeacherDTO> teachersDTO = new ArrayList<>();
        for (Teacher teacher : teachers) {
            teachersDTO.add(new TeacherDTO(teacher.getId(), teacher.getFirstName(), teacher.getLastName()));
        }
        return teachersDTO;
    }


}
