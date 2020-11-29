package ua.com.nikiforov.services.subject;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.nikiforov.controllers.dto.SubjectDTO;
import ua.com.nikiforov.dao.subject.SubjectDAO;
import ua.com.nikiforov.dao.teachers_subjects.TeachersSubjectsDAO;
import ua.com.nikiforov.models.Subject;
import ua.com.nikiforov.models.persons.Teacher;

@Service
public class SubjectServiceImpl implements SubjectService {

    private SubjectDAO subjectDAO;
    private TeachersSubjectsDAO techersSubjectsDAO;

    @Autowired
    public SubjectServiceImpl(SubjectDAO subjectDAO, TeachersSubjectsDAO techersSubjectsDAO) {
        this.subjectDAO = subjectDAO;
        this.techersSubjectsDAO = techersSubjectsDAO;
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
    public List<Subject> getAllSubjectsWithoutTeachers(){
        return subjectDAO.getAllSubjects();
    }


    @Override
    public boolean updateSubject(SubjectDTO subject) {
        return subjectDAO.updateSubject(subject);
    }

    @Override
    public boolean deleteSubjectById(int subjectId) {
        return subjectDAO.deleteSubjectById(subjectId);
    }

}
