package ua.com.nikiforov.dao.persons;

import java.util.List;

import ua.com.nikiforov.models.persons.Student;

public interface StudentDAO {
    
    public boolean addStudent(String firstName, String lastName, long groupId);

    public Student getStudentById(long id);

    public List<Student> getAllStudents();

    public boolean updateStudent(String firstName, String lastName, long groupId);

    public boolean deleteStudentById(long id);
}
