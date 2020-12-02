package ua.com.nikiforov.services.persons;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.nikiforov.controllers.dto.StudentDTO;
import ua.com.nikiforov.dao.persons.StudentDAO;
import ua.com.nikiforov.models.persons.Student;
import ua.com.nikiforov.services.group.GroupService;

@Service
public class StudentsServiseImpl implements StudentsService {

    private StudentDAO studentDAO;

    @Autowired
    public StudentsServiseImpl(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }

    @Override
    public boolean addStudent(StudentDTO student) {
        return studentDAO.addStudent(student);
    }

    @Override
    public StudentDTO getStudentById(long studentId) {
        return getStudentDTO(studentDAO.getStudentById(studentId));
    }

    public StudentDTO getStudentByName(String firstName, String lastName) {
        return getStudentDTO(studentDAO.getStudentByName(firstName, lastName));
    }

    public StudentDTO getStudentByNameGroupId(String firstName, String lastName, long groupId) {
        return getStudentDTO(studentDAO.getStudentByNameGroupId(firstName, lastName, groupId));
    }

    @Override
    public List<StudentDTO> getAllStudents() {
        List<Student> students = studentDAO.getAllStudents();
        return getListStudentDTO(students);
    }

    @Override
    public boolean updateStudent(StudentDTO student) {
        return studentDAO.updateStudent(student);
    }

    @Override
    public boolean deleteStudentById(long studentId) {
        return studentDAO.deleteStudentById(studentId);
    }

    @Override
    public List<StudentDTO> getStudentsByGroupId(long groupId) {
        List<Student> students = studentDAO.getStudentsByGroupId(groupId);
        return getListStudentDTO(students);
    }

    @Override
    public boolean transferStudent(long studentId, long groupIdTo) {
        Student student = studentDAO.getStudentById(studentId);
        StudentDTO newStudent = new StudentDTO(studentId, student.getFirstName(), student.getLastName(), groupIdTo);
        return studentDAO.updateStudent(newStudent);
    }

    public StudentDTO getStudentDTO(Student student) {
        long id = student.getId();
        String firstName = student.getFirstName();
        String lastName = student.getLastName();
        long groupId = student.getGroupId();
        return new StudentDTO(id, firstName, lastName, groupId);
    }

    private List<StudentDTO> getListStudentDTO(List<Student> students) {
        List<StudentDTO> studensDTO = new ArrayList<>();
        for (Student student : students) {
            studensDTO.add(getStudentDTO(student));
        }
        return studensDTO;
    }

}
