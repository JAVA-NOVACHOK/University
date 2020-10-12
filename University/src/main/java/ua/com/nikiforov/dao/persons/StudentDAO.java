package ua.com.nikiforov.dao.persons;

import java.util.List;

import ua.com.nikiforov.models.persons.Student;

public interface StudentDAO {
    
    public boolean addStudent(String firstName, String lastName, long groupId);

    public Student getStudentById(long studentId);

    public List<Student> getAllStudents();

    public boolean updateStudent(String firstName, String lastName, long groupId,long studentId);

    public boolean deleteStudentById(long studentId);
}
