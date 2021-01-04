package ua.com.nikiforov.repositories.persons;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.com.nikiforov.models.persons.Teacher;

import java.util.List;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    public Teacher getTeacherByFirstNameAndLastName(String firstName, String lastName);

    @Query("SELECT t FROM Teacher t WHERE UPPER(t.firstName)" +
            " LIKE UPPER( CONCAT(:firstName,'%')) OR UPPER(t.lastName) LIKE UPPER( CONCAT(:lastName,'%')) ORDER BY t.lastName")
    public List<Teacher> getTeacherByLikeName(
            @Param("firstName") String firstName,
            @Param("lastName") String lastName);

}
