package ua.com.nikiforov.services.university;


import ua.com.nikiforov.dto.UniversityDTO;
import ua.com.nikiforov.models.University;

public interface UniversityService {
    
    public void addUniversity(String name);

    public University getUniversityById(int id);
    
    public University getUniversityByName(String universityName);

    public void updateUniversity(UniversityDTO universityDTO);

    public void deleteUniversityById(int id);

}
