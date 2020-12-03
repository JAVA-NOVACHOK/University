package ua.com.nikiforov.services.persons;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.nikiforov.controllers.dto.SubjectDTO;
import ua.com.nikiforov.controllers.dto.TeacherDTO;
import ua.com.nikiforov.dao.persons.TeacherDAO;
import ua.com.nikiforov.dao.teachers_subjects.TeachersSubjectsDAO;
import ua.com.nikiforov.models.Subject;
import ua.com.nikiforov.models.persons.Teacher;
import ua.com.nikiforov.services.subject.SubjectService;

@Service
public class TeacherServiceImpl implements TeacherService {

    private TeacherDAO teacherDAO;
    private TeachersSubjectsDAO techersSubjectsDAO;

    @Autowired
    public TeacherServiceImpl(TeacherDAO teacherDAO, TeachersSubjectsDAO techersSubjectsDAO) {
        this.teacherDAO = teacherDAO;
        this.techersSubjectsDAO = techersSubjectsDAO;

    }

    @Override
    public boolean addTeacher(TeacherDTO teacher) {
        String firstName = teacher.getFirstName();
        String lastName = teacher.getLastName();
        return teacherDAO.addTeacher(firstName, lastName);
    }

    @Override
    public TeacherDTO getTeacherById(long teacherId) {
        TeacherDTO teacher = getTeacherDTO(teacherDAO.getTeacherById(teacherId));
        return addSubjectsToTeacher(teacher);
    }

    public TeacherDTO getTeacherByName(String firstName, String lastName) {
        return getTeacherDTO(teacherDAO.getTeacherByName(firstName, lastName));
    }

    private TeacherDTO addSubjectsToTeacher(TeacherDTO teacher) {
        List<SubjectDTO> subjects = getSubjectDTOList(techersSubjectsDAO.getSubjects(teacher.getId()));
        teacher.setSubjects(subjects);
        return teacher;
    }

    @Override
    public List<TeacherDTO> getAllTeachers() {
       return getTeachersDTOList(teacherDAO.getAllTeachers());
    }

    @Override
    public boolean updateTeacher(TeacherDTO teacher) {
        return teacherDAO.updateTeacher(teacher);
    }

    @Override
    public boolean deleteTeacherById(long teacherId) {
        return teacherDAO.deleteTeacherById(teacherId);
    }

    @Override
    public boolean assignSubjectToTeacher(int subjectId, long teacherId) {
        return techersSubjectsDAO.assignSubjectToTeacher(teacherId, subjectId);
    }

    @Override
    public boolean unassignSubjectFromTeacher(int subjectId, long teacherId) {
        return techersSubjectsDAO.unassignSubjectFromTeacher(teacherId, subjectId);
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
    
    private  List<SubjectDTO> getSubjectDTOList(List<Subject> subjects) {
        List<SubjectDTO> subjectsDTO = new ArrayList<>();
        for (Subject subject : subjects) {
            subjectsDTO.add(new SubjectDTO(subject.getId(), subject.getName()));
        }
        return subjectsDTO;
    }
    

}
