package ua.com.nikiforov.dao.university;

import ua.com.nikiforov.dto.UniversityDTO;
import ua.com.nikiforov.models.University;

public interface UniversityDAO {
    
    public void addUniversity(String name);

    public University findUniversityById(int id);
    
    public University getUniversityByName(String universityName);

    public void updateUniversity(UniversityDTO universityDTO);

    public void deleteUniversityById(int id);
}
