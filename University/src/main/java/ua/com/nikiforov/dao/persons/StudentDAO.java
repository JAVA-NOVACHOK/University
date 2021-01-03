package ua.com.nikiforov.dao.persons;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import ua.com.nikiforov.models.persons.Student;

import javax.transaction.Transactional;

@Component
public interface StudentDAO extends JpaRepository<Student,Long> {

    @Transactional
    public Student save(Student student);

    public Student getStudentById(long studentId);

    @Query("SELECT s FROM Student s WHERE s.group.groupId = ?1 AND s.firstName = ?2 AND s.lastName = ?3")
    public Student getStudentByNameGroupId(long groupId,String firstName, String lastName);


    @Query("SELECT s FROM Student s WHERE s.firstName = ?1 AND s.lastName = ?2")
    public Student getStudentByName(String firstName, String lastName);


    @Query("SELECT s FROM Student s ORDER BY s.lastName")
    public List<Student> getAllStudents();

    @Transactional
    public void deleteStudentById(long studentId);
    
}
