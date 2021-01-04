package ua.com.nikiforov.repositories.university;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.nikiforov.models.University;

public interface UniversityRepository extends JpaRepository<University,Integer> {

    public University getUniversityByUniversityName(String universityName);

}
