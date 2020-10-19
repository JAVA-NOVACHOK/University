package ua.com.nikiforov.dao.university;

import java.util.List;

import ua.com.nikiforov.models.University;

public interface UniversityDAO {
    
    public boolean addUniversity(String name);

    public University findUniversityById(int id);
    
    public University getUniversityByName(String universityName);

    public List<University> getAllUniversities();

    public boolean updateUniversity(String name, int id);

    public boolean deleteUniversityById(int id);
}
