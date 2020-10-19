package ua.com.nikiforov.dao.persons;

import java.util.List;

import org.springframework.stereotype.Component;

import ua.com.nikiforov.models.persons.Student;

@Component
public interface StudentDAO {

    public boolean addStudent(String firstName, String lastName, long groupId);

    public Student getStudentById(long studentId);

    public Student getStudentByNameGroupId(String firstName, String lastName, long groupId);

    public List<Student> getAllStudents();

    public boolean updateStudent(String firstName, String lastName, long groupId, long studentId);

    public boolean deleteStudentById(long studentId);
}
