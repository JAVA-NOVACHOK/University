package ua.com.nikiforov.services.persons;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.nikiforov.dao.persons.StudentDAO;
import ua.com.nikiforov.models.persons.Student;

@Service
public class StudentsServiseImpl implements StudentsService {

    private StudentDAO studentDAO;

    @Autowired
    public StudentsServiseImpl(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }

    @Override
    public boolean addStudent(String firstName, String lastName, long groupId) {
        return studentDAO.addStudent(firstName, lastName, groupId);
    }

    @Override
    public Student getStudentById(long studentId) {
        return studentDAO.getStudentById(studentId);
    }
    
    public Student getStudentByName(String firstName, String lastName) {
        return studentDAO.getStudentByName(firstName, lastName);
    }

    public Student getStudentByNameGroupId(String firstName, String lastName, long groupId) {
        return studentDAO.getStudentByNameGroupId(firstName, lastName, groupId);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentDAO.getAllStudents();
    }

    @Override
    public boolean updateStudent(Student student) {
        return studentDAO.updateStudent(student);
    }

    @Override
    public boolean deleteStudentById(long studentId) {
        return studentDAO.deleteStudentById(studentId);
    }

    @Override
    public List<Student> getStudentsByGroupId(long groupId) {
        return studentDAO.getStudentsByGroupId(groupId);
    }

    @Override
    public boolean transferStudent(long studentId, long groupIdTo) {
        Student student = studentDAO.getStudentById(studentId);
        Student newStudent = new Student(studentId,student.getFirstName(), student.getLastName(), groupIdTo);
        return studentDAO.updateStudent(newStudent);
    }

}
