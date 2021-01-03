package ua.com.nikiforov.dao.subject;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.com.nikiforov.models.Subject;

import javax.transaction.Transactional;
import java.util.List;

public interface SubjectDAO extends JpaRepository<Subject, Integer> {

    @Transactional
    public Subject save(Subject subject);

    public Subject getSubjectById(int id);
    
    public Subject getSubjectByName(String name);

    @Query("SELECT s FROM Subject s ORDER BY s.name")
    public List<Subject> getAllSubjects();

    @Transactional
    public void deleteSubjectById(int id);
}
