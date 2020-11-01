package ua.com.nikiforov.services.subject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.nikiforov.dao.subject.SubjectDAO;
import ua.com.nikiforov.dao.teachers_subjects.TeachersSubjectsDAO;
import ua.com.nikiforov.models.Subject;
import ua.com.nikiforov.models.persons.Teacher;
import ua.com.nikiforov.services.persons.TeacherService;

@Service
public class SubjectServiceImpl implements SubjectService {

    private SubjectDAO subjectDAO;
    private TeachersSubjectsDAO techersSubjectsDAO;
    private TeacherService teacherService;

    @Autowired
    public SubjectServiceImpl(SubjectDAO subjectDAO, TeachersSubjectsDAO techersSubjectsDAO,
            TeacherService teacherService) {
        this.subjectDAO = subjectDAO;
        this.techersSubjectsDAO = techersSubjectsDAO;
        this.teacherService = teacherService;
    }

    @Override
    public boolean addSubject(String subjectName) {
        return subjectDAO.addSubject(subjectName);
    }

    @Override
    public Subject getSubjectById(int subjectId) {
        Subject subject = subjectDAO.getSubjectById(subjectId);
        return setTeachersToSubject(subject);
    }
    
    private Subject setTeachersToSubject(Subject subject) {
        List<Teacher> teachers = techersSubjectsDAO.getTeachers(subject.getId());
        subject.setTeachers(teachers);
        return subject;
    }

    @Override
    public Subject getSubjectByName(String subjectName) {
        Subject subject = subjectDAO.getSubjectByName(subjectName);
        return setTeachersToSubject(subject);
    }

    @Override
    public List<Subject> getAllSubjects() {
        List<Subject> subjects = subjectDAO.getAllSubjects();
        for (Subject subject : subjects) {
            setTeachersToSubject(subject);
        }
        return subjects;
    }

    @Override
    public boolean updateSubject(String subjectName, int subjectId) {
        return subjectDAO.updateSubject(subjectName, subjectId);
    }

    @Override
    public boolean deleteSubjectById(int subjectId) {
        return subjectDAO.deleteSubjectById(subjectId);
    }

}
