package ua.com.nikiforov.dao.persons;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.com.nikiforov.models.persons.Teacher;

import javax.transaction.Transactional;

public interface TeacherDAO extends JpaRepository<Teacher, Long> {

    @Transactional
    public Teacher save(Teacher teacher);

    public Teacher getTeacherById(long teacherId);

    @Query("SELECT t FROM Teacher t WHERE t.firstName = ?1 AND t.lastName = ?2")
    public Teacher getTeacherByName(String firstName, String lastName);

    @Query("SELECT t FROM Teacher t WHERE UPPER(t.firstName)" +
            " LIKE UPPER( CONCAT(?1,'%')) OR UPPER(t.lastName) LIKE UPPER( CONCAT(?2,'%')) ORDER BY t.lastName")
    public List<Teacher> getTeacherByLikeName(String firstName, String lastName);

    @Query("SELECT t FROM Teacher t ORDER BY t.lastName")
    public List<Teacher> getAllTeachers();

    @Transactional
    public void deleteTeacherById(long teacherId);

}
