package ua.com.nikiforov.services.subject;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.nikiforov.controllers.dto.SubjectDTO;
import ua.com.nikiforov.controllers.dto.TeacherDTO;
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
    public List<SubjectDTO> getAllSubjectsWithoutTeachers(){
        return getSubjectDTOList(subjectDAO.getAllSubjects());
    }


    @Override
    public boolean updateSubject(SubjectDTO subject) {
        return subjectDAO.updateSubject(subject);
    }

    @Override
    public boolean deleteSubjectById(int subjectId) {
        return subjectDAO.deleteSubjectById(subjectId);
    }
    
    private List<SubjectDTO> getSubjectDTOList(List<Subject> subjects){
        List<SubjectDTO> subjectsDTO = new ArrayList<>();
        for(Subject subject : subjects) {
            subjectsDTO.add(getSubjectDTO(subject));
        }
        return subjectsDTO;
    }
    
    private SubjectDTO getSubjectDTO(Subject subject) {
        int subjectId = subject.getId();
        String name = subject.getName();
        List<Teacher> teachers = techersSubjectsDAO.getTeachers(subject.getId());
        List<TeacherDTO> teachersDTO = getTeachersDTOList(teachers);
        return new SubjectDTO(subjectId,name,teachersDTO);
    }
    
    private List<TeacherDTO> getTeachersDTOList(List<Teacher> teachers){
        List<TeacherDTO> teachersDTO = new ArrayList<>();
        for(Teacher teacher : teachers) {
            teachersDTO.add(new TeacherDTO(teacher.getId(),teacher.getFirstName(),teacher.getLastName()));
        }
        return teachersDTO;
    }
   

}
