package ua.com.nikiforov.dao.persons;

import java.util.List;

import org.springframework.stereotype.Component;

import ua.com.nikiforov.dto.StudentDTO;
import ua.com.nikiforov.models.persons.Student;

@Component
public interface StudentDAO {

    public void addStudent(StudentDTO student);

    public Student getStudentById(long studentId);
    
    public Student getStudentByNameGroupId(String firstName, String lastName, long groupId);
    
    public Student getStudentByName(String firstName, String lastName);

    public List<Student> getAllStudents();

    public void updateStudent(StudentDTO student);

    public void deleteStudentById(long studentId);
    
}
