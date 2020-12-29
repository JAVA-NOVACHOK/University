package ua.com.nikiforov.services.subject;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import ua.com.nikiforov.dto.SubjectDTO;
import ua.com.nikiforov.dao.subject.SubjectDAO;
import ua.com.nikiforov.mappers_dto.SubjectMapperDTO;

import javax.transaction.Transactional;

@Service
public class SubjectServiceImpl implements SubjectService {

    private SubjectDAO subjectDAO;
    private SubjectMapperDTO subjectMapper;

    @Autowired
    public SubjectServiceImpl(SubjectDAO subjectDAO, SubjectMapperDTO subjectMapper) {
        this.subjectDAO = subjectDAO;
        this.subjectMapper = subjectMapper;
    }

    @Override
    @Transactional
    public void addSubject(String subjectName) {
        try {
            subjectDAO.addSubject(subjectName);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateKeyException("Error! Duplicate Subject while adding", e);
        }
    }

    @Override
    @Transactional
    public SubjectDTO getSubjectById(int subjectId) {
        return subjectMapper.subjectToSubjectDTO(subjectDAO.getSubjectById(subjectId));

    }

    @Override
    @Transactional
    public SubjectDTO getSubjectByName(String subjectName) {
        return subjectMapper.subjectToSubjectDTO(subjectDAO.getSubjectByName(subjectName));

    }

    @Override
    @Transactional
    public List<SubjectDTO> getAllSubjects() {
        return subjectMapper.getSubjectDTOList(subjectDAO.getAllSubjects());
    }


    @Override
    @Transactional
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


}
