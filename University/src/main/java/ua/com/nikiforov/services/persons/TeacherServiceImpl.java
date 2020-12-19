package ua.com.nikiforov.services.persons;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import ua.com.nikiforov.dto.SubjectDTO;
import ua.com.nikiforov.dto.TeacherDTO;
import ua.com.nikiforov.dao.persons.TeacherDAO;
import ua.com.nikiforov.models.Subject;
import ua.com.nikiforov.models.persons.Teacher;

@Service
public class TeacherServiceImpl implements TeacherService {

    private TeacherDAO teacherDAO;

    @Autowired
    public TeacherServiceImpl(TeacherDAO teacherDAO) {
        this.teacherDAO = teacherDAO;
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
    public TeacherDTO getTeacherById(long teacherId) {
        return getTeacherDTO(teacherDAO.getTeacherById(teacherId));
    }

    public TeacherDTO getTeacherByName(String firstName, String lastName) {
        return getTeacherDTO(teacherDAO.getTeacherByName(firstName, lastName));
    }

    @Override
    public List<TeacherDTO> getAllTeachers() {
        return getTeachersDTOList(teacherDAO.getAllTeachers());
    }

    @Override
    public void updateTeacher(TeacherDTO teacher) {
        try {
            teacherDAO.updateTeacher(teacher);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateKeyException("Error! Duplicate teacher while editing!", e);
        }
    }

    @Override
    public void deleteTeacherById(long teacherId) {
        teacherDAO.deleteTeacherById(teacherId);
    }

    @Override
    public void assignSubjectToTeacher(long teacherId, int subjectId) {
        teacherDAO.assignSubjectToTeacher(teacherId, subjectId);
    }

    @Override
    public void unassignSubjectFromTeacher(long teacherId, int subjectId) {
        teacherDAO.unassignSubjectFromTeacher(teacherId, subjectId);
    }

    @Override
    public List<TeacherDTO> getTeacherByLikeName(String firstName, String lastName) {
        return getTeachersDTOList(teacherDAO.getTeacherByLikeName(firstName, lastName));
    }

    @Override
    public List<TeacherDTO> getAllTeachersWithoutSubjects() {
        return getTeachersDTOList(teacherDAO.getAllTeachers());
    }

    public List<TeacherDTO> getTeachersDTOList(List<Teacher> teachers) {
        List<TeacherDTO> teachersDTO = new ArrayList<>();
        for (Teacher teacher : teachers) {
            teachersDTO.add(getTeacherDTO(teacher));
        }
        return teachersDTO;
    }

    private TeacherDTO getTeacherDTO(Teacher teacher) {
        TeacherDTO teacherDTO = new TeacherDTO();
        teacherDTO.setId(teacher.getId());
        teacherDTO.setFirstName(teacher.getFirstName());
        teacherDTO.setLastName(teacher.getLastName());
        teacherDTO.setSubjects(getSubjectDTOList(teacher.getSubjects()));
        return teacherDTO;
    }

    private List<SubjectDTO> getSubjectDTOList(List<Subject> subjects) {
        List<SubjectDTO> subjectsDTO = new ArrayList<>();
        for (Subject subject : subjects) {
            subjectsDTO.add(new SubjectDTO(subject.getId(), subject.getName()));
        }
        return subjectsDTO;
    }


}
