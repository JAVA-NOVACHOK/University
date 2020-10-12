package ua.com.nikiforov.services.university;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.nikiforov.dao.university.UniversityDAO;
import ua.com.nikiforov.models.University;

@Service
public class UniversityServiceImpl implements UniversityService {

    @Autowired
    private UniversityDAO universityDAO;

    @Override
    public boolean addUniversity(String name) {
        return universityDAO.addUniversity(name);
    }

    @Override
    public University findUniversityById(int id) {
        return universityDAO.findUniversityById(id);
    }

    @Override
    public List<University> getAllUniversities() {
        return universityDAO.getAllUniversities();
    }

    @Override
    public boolean updateUniversity(String name, int id) {
        return universityDAO.updateUniversity(name, id);
    }

    @Override
    public boolean deleteUniversityById(int id) {
        return universityDAO.deleteUniversityById(id);
    }

}
