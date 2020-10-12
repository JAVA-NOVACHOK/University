package ua.com.nikiforov.services.persons;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.nikiforov.dao.persons.TeacherDAO;
import ua.com.nikiforov.dao.teachers_subjects.TeachersSubjectsDAO;
import ua.com.nikiforov.models.persons.Teacher;

@Service
public class TeacherServiceImpl implements TeachersService {

    private TeacherDAO teacherDAO;
    private TeachersSubjectsDAO techersSubjectsDAO;

    @Autowired
    public TeacherServiceImpl(TeacherDAO teacherDAOImpl, TeachersSubjectsDAO techersSubjectsDAOImpl) {
        this.teacherDAO = teacherDAOImpl;
        this.techersSubjectsDAO = techersSubjectsDAOImpl;
    }

    @Override
    public boolean addTeacher(String firstName, String lastName) {
        return teacherDAO.addTeacher(firstName, lastName);
    }

    @Override
    public Teacher getTeacherById(long teacherId) {
        Teacher teacher = teacherDAO.getTeacherById(teacherId);
        teacher.setSubjectIds(techersSubjectsDAO.getSubjectsIds(teacherId));
        return teacher;
    }

    @Override
    public List<Teacher> getAllTeachers() {
        return teacherDAO.getAllTeachers();
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
        return techersSubjectsDAO.addSubjectForTeacher(teacherId, subjectId);
    }

}
