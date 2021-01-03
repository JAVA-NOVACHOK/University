package ua.com.nikiforov.services.university;


import ua.com.nikiforov.dto.UniversityDTO;

public interface UniversityService {
    
    public UniversityDTO addUniversity(String name);

    public UniversityDTO getUniversityById(int id);
    
    public UniversityDTO getUniversityByName(String universityName);

    public void updateUniversity(UniversityDTO universityDTO);

    public void deleteUniversityById(int id);

}
