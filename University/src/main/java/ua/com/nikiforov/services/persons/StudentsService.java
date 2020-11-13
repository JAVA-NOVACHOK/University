package ua.com.nikiforov.services.persons;

import java.util.List;

import ua.com.nikiforov.models.persons.Student;

public interface StudentsService {
    
    public boolean addStudent(String firstName, String lastName, long groupId);

    public Student getStudentById(long studentId);
    
    public List<Student> getStudentsByGroupId(long groupId);
    
    public Student getStudentByName(String firstName, String lastName);
    
    public Student getStudentByNameGroupId(String firstName, String lastName, long groupId);

    public List<Student> getAllStudents();

    public boolean updateStudent(Student student);
    public boolean deleteStudentById(long studentId);
    
    public boolean transferStudent(long studentId, long groupIdTo);
}
