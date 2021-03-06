package ua.com.nikiforov.repositories.persons;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import ua.com.nikiforov.models.persons.Student;

import java.util.Optional;

@Component
public interface StudentRepository extends JpaRepository<Student,Long> {

    @Query("SELECT s FROM Student s WHERE s.group.groupId = :groupId AND s.firstName = :firstName AND s.lastName = :lastName")
    public Optional<Student> getStudentByNameGroupId(
            @Param("groupId") long groupId,
            @Param("firstName")String firstName,
            @Param("lastName")String lastName);

    public Optional<Student> getStudentByFirstNameAndLastName(String firstName,String lastName);

}
