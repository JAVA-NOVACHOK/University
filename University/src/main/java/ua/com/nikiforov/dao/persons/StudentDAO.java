package ua.com.nikiforov.dao.persons;

import java.util.List;

import org.springframework.stereotype.Component;

import ua.com.nikiforov.models.persons.Student;

@Component
public interface StudentDAO {

    public boolean addStudent(String firstName, String lastName, long groupId);

    public Student getStudentById(long studentId);
    
    public List<Student> getStudentsByGroupId(long groupId);

    public Student getStudentByNameGroupId(String firstName, String lastName, long groupId);
    
    public Student getStudentByName(String firstName, String lastName);

    public List<Student> getAllStudents();

    public boolean updateStudent(Student student);

    public boolean deleteStudentById(long studentId);
    
}
