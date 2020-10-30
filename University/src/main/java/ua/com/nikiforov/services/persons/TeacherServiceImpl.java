package ua.com.nikiforov.services.persons;

import java.util.List;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.nikiforov.dao.persons.TeacherDAO;
import ua.com.nikiforov.dao.subject.SubjectDAO;
import ua.com.nikiforov.dao.teachers_subjects.TeachersSubjectsDAO;
import ua.com.nikiforov.models.persons.Teacher;

@Service
public class TeacherServiceImpl implements TeacherService {

    private TeacherDAO teacherDAO;
    private TeachersSubjectsDAO techersSubjectsDAO;
    private SubjectDAO subjectDAO;

    @Autowired
    public TeacherServiceImpl(TeacherDAO teacherDAO, TeachersSubjectsDAO techersSubjectsDAO, SubjectDAO subjectDAO) {
        this.teacherDAO = teacherDAO;
        this.techersSubjectsDAO = techersSubjectsDAO;
        this.subjectDAO = subjectDAO;
    }

    @Override
    public boolean addTeacher(String firstName, String lastName) {
        return teacherDAO.addTeacher(firstName, lastName);
    }

    @Override
    public Teacher getTeacherById(long teacherId) {
        Teacher teacher = teacherDAO.getTeacherById(teacherId);
        return addSubjectsToTeacher(teacher);
    }

    public Teacher getTeacherByName(String firstName, String lastName) {
        return teacherDAO.getTeacherByName(firstName, lastName);
    }

    private Teacher addSubjectsToTeacher(Teacher teacher) {
        List<Integer> subjectsId = techersSubjectsDAO.getSubjectsIds(teacher.getId());
        List<ua.com.nikiforov.models.Subject> subjects = subjectsId.stream().map(s -> subjectDAO.getSubjectById(s))
                .collect(Collectors.toList());
        teacher.setSubjects(subjects);
        return teacher;
    }

    @Override
    public List<Teacher> getAllTeachers() {
        List<Teacher> teachers = teacherDAO.getAllTeachers();
        for (Teacher teacher : teachers) {
            addSubjectsToTeacher(teacher);
        }
        return teachers;
    }

    @Override
    public boolean updateTeacher(String firstName, String lastName, long teacherId) {
        return teacherDAO.updateTeacher(firstName, lastName, teacherId);
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

}
