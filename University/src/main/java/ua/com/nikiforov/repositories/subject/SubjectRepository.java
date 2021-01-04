package ua.com.nikiforov.repositories.subject;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.nikiforov.models.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Integer> {

    public Subject getSubjectByName(String name);

}
