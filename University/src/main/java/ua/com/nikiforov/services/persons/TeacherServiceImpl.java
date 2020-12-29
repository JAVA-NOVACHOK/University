package ua.com.nikiforov.services.persons;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import ua.com.nikiforov.dto.TeacherDTO;
import ua.com.nikiforov.dao.persons.TeacherDAO;
import ua.com.nikiforov.mappers_dto.TeacherMapperDTO;
import ua.com.nikiforov.models.persons.Teacher;

import javax.transaction.Transactional;

@Service
public class TeacherServiceImpl implements TeacherService {

    private TeacherDAO teacherDAO;
    private TeacherMapperDTO teacherMapper;

    @Autowired
    public TeacherServiceImpl(TeacherDAO teacherDAO, TeacherMapperDTO teacherMapper) {
        this.teacherDAO = teacherDAO;
        this.teacherMapper = teacherMapper;
    }

    @Override
    public void addTeacher(TeacherDTO teacher) {
        try {
            teacherDAO.addTeacher(teacher);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateKeyException("Error! Duplicate Teacher while adding",e);
        }
    }

    @Override
    @Transactional
    public TeacherDTO getTeacherById(long teacherId) {
        return teacherMapper.getTeacherDTO(teacherDAO.getTeacherById(teacherId));
    }

    @Override
    @Transactional
    public TeacherDTO getTeacherByName(String firstName, String lastName) {
        return teacherMapper.getTeacherDTO(teacherDAO.getTeacherByName(firstName, lastName));
    }

    @Override
    @Transactional
    public List<TeacherDTO> getAllTeachers() {
        return teacherMapper.getTeacherDTOList(teacherDAO.getAllTeachers());
    }

    @Override
    @Transactional
    public void updateTeacher(TeacherDTO teacher) {
        try {
            teacherDAO.updateTeacher(teacher);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateKeyException("Error! Duplicate teacher while editing!", e);
        }
    }

    @Override
    @Transactional
    public void deleteTeacherById(long teacherId) {
        teacherDAO.deleteTeacherById(teacherId);
    }

    @Override
    public Teacher assignSubjectToTeacher(long teacherId, int subjectId) {
        return teacherDAO.assignSubjectToTeacher(teacherId, subjectId);
    }

    @Override
    public Teacher unassignSubjectFromTeacher(long teacherId, int subjectId) {
        return teacherDAO.unassignSubjectFromTeacher(teacherId, subjectId);
    }

    @Override
    @Transactional
    public List<TeacherDTO> getTeacherByLikeName(String firstName, String lastName) {
        return teacherMapper.getTeacherDTOList(teacherDAO.getTeacherByLikeName(firstName, lastName));
    }





}
