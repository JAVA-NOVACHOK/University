package ua.com.nikiforov.dao.university;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.nikiforov.models.University;

import javax.transaction.Transactional;

public interface UniversityDAO extends JpaRepository<University,Integer> {

    @Transactional
    public University save(University university);

    public University findUniversityById(int id);
    
    public University getUniversityByUniversityName(String universityName);

    @Transactional
    public void deleteUniversityById(int id);
}
