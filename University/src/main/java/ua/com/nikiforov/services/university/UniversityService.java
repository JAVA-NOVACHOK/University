package ua.com.nikiforov.services.university;

import java.util.List;

import org.springframework.stereotype.Service;

import ua.com.nikiforov.models.University;

public interface UniversityService {
    
    public boolean addUniversity(String name);

    public University findUniversityById(int id);

    public List<University> getAllUniversities();

    public boolean updateUniversity(String name, int id);

    public boolean deleteUniversityById(int id);

}
