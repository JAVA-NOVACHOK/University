package ua.com.nikiforov.services.university;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import ua.com.nikiforov.dao.university.UniversityDAO;
import ua.com.nikiforov.dto.UniversityDTO;
import ua.com.nikiforov.models.University;

@Service
public class UniversityServiceImpl implements UniversityService {

    @Autowired
    private UniversityDAO universityDAO;

    @Override
    public void addUniversity(String name) {
        try {
            universityDAO.addUniversity(name);
        }catch (DataIntegrityViolationException e){
            throw new DuplicateKeyException("University is already exists",e);
        }
    }

    @Override
    public University getUniversityById(int id) {
        return universityDAO.findUniversityById(id);
    }

    public University getUniversityByName(String universityName) {
        return universityDAO.getUniversityByName(universityName);
    }



    @Override
    public void updateUniversity(UniversityDTO universityDTO) {
        universityDAO.updateUniversity(universityDTO);
    }

    @Override
    public void deleteUniversityById(int id) {
        universityDAO.deleteUniversityById(id);
    }

}
