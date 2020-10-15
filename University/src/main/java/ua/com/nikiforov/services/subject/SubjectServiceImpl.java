package ua.com.nikiforov.services.subject;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.nikiforov.dao.subject.SubjectDAO;
import ua.com.nikiforov.dao.teachers_subjects.TeachersSubjectsDAO;
import ua.com.nikiforov.models.Subject;

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
        subject.setSubjectTeacherIds(techersSubjectsDAO.getTeachersIds(subjectId));
        return subject;
    }
    @Override
    public Subject getSubjectByName(String subjectName){
        Subject subject = subjectDAO.getSubjectByName(subjectName);
        subject.setSubjectTeacherIds(techersSubjectsDAO.getTeachersIds(subject.getId()));
        return subject;
    }
    
    @Override
    public List<Subject> getAllSubjects() {
        List<Subject> subjects = subjectDAO.getAllSubjects();
        for (Subject subject : subjects) {
            List<Long> subjectTeachersIds = techersSubjectsDAO.getTeachersIds(subject.getId());
            subject.setSubjectTeacherIds(subjectTeachersIds);
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
