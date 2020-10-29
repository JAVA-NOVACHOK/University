<<<<<<< HEAD
package ua.com.nikiforov.services.persons;

import java.util.List;

import ua.com.nikiforov.models.persons.Student;

public interface StudentsService {
    
    public boolean addStudent(String firstName, String lastName, long groupId);

    public Student getStudentById(long studentId);
    
    public List<Student> getStudentsByGroupId(long groupId);
    
    public Student getStudentByNameGroupId(String firstName, String lastName, long groupId);

    public List<Student> getAllStudents();

    public boolean updateStudent(String firstName, String lastName, long groupId, long studentId);

    public boolean deleteStudentById(long studentId);
    
    public boolean transferStudent(long studentId, long groupIdTo);
}
=======
package ua.com.nikiforov.services.persons;

import java.util.List;

import ua.com.nikiforov.models.persons.Student;

public interface StudentsService {
    
    public boolean addStudent(String firstName, String lastName, long groupId);

    public Student getStudentById(long studentId);
    
    public Student getStudentByNameGroupId(String firstName, String lastName, long groupId);

    public List<Student> getAllStudents();

    public boolean updateStudent(String firstName, String lastName, long groupId, long studentId);

    public boolean deleteStudentById(long studentId);
}
>>>>>>> refs/remotes/origin/master
