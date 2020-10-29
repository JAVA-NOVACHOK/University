<<<<<<< HEAD
package ua.com.nikiforov.services.university;

import java.util.List;

import ua.com.nikiforov.models.University;

public interface UniversityService {
    
    public boolean addUniversity(String name);

    public University getUniversityById(int id);
    
    public University getUniversityByName(String universityName);

    public List<University> getAllUniversities();

    public boolean updateUniversity(String name, int id);

    public boolean deleteUniversityById(int id);

}
=======
package ua.com.nikiforov.services.university;

import java.util.List;

import ua.com.nikiforov.models.University;

public interface UniversityService {
    
    public boolean addUniversity(String name);

    public University getUniversityById(int id);
    
    public University getUniversityByName(String universityName);

    public List<University> getAllUniversities();

    public boolean updateUniversity(String name, int id);

    public boolean deleteUniversityById(int id);

}
>>>>>>> refs/remotes/origin/master
