package ua.com.nikiforov.repositories.subject;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.nikiforov.models.Subject;

import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Integer> {

    public Optional<Subject> getSubjectByName(String name);

}
