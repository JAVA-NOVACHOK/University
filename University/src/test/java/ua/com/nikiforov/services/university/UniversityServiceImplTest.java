package ua.com.nikiforov.services.university;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import ua.com.nikiforov.config.UniversityConfig;
import ua.com.nikiforov.dao.table_creator.TableCreator;
import ua.com.nikiforov.models.University;

@SpringJUnitConfig(UniversityConfig.class)

class UniversityServiceImplTest {

    private static final String UNIVERSITY_NAME_1 = "Foxminded";
    private static final String UNIVERSITY_NAME_1_UPDATED = "Foxminded LTD.";
    private static final String UNIVERSITY_NAME_2 = "GlobalLogic";

    private static final int UNIVERSITIES_COUNT = 2;

    @Autowired
    private UniversityService universityService;

    @Autowired
    private TableCreator tableCreator;

    @BeforeEach
    void init() {
        tableCreator.createTables();
    }

    @Test
    void whenAddUniversityIfSuccessShouldReturnTrue() {
        assertTrue(universityService.addUniversity(UNIVERSITY_NAME_1));
    }

    @Test
    void whenGetUniversityByNameReturnCorrectUniversity() {
        universityService.addUniversity(UNIVERSITY_NAME_1);
        assertEquals(UNIVERSITY_NAME_1, universityService.getUniversityByName(UNIVERSITY_NAME_1).getName());
    }

    @Test
    void whenGetUniversityByIDReturnCorrectUniversity() {
        int universityId = insertUniversity(UNIVERSITY_NAME_1);
        assertEquals(UNIVERSITY_NAME_1, universityService.getUniversityById(universityId).getName());
    }

    @Test
    void whenGetAllUniversitiesIfPresentReturnListOfAllUniversities() {
        universityService.addUniversity(UNIVERSITY_NAME_1);
        universityService.addUniversity(UNIVERSITY_NAME_2);
        assertEquals(UNIVERSITIES_COUNT, universityService.getAllUniversities().size());
    }

    @Test
    void whenUpdateUniversityByIdIfSuccessThenReturnTrue() {
        int universityId = insertUniversity(UNIVERSITY_NAME_1);
        assertTrue(universityService.updateUniversity(UNIVERSITY_NAME_1_UPDATED, universityId));
    }

    @Test
    void whenUpdateUniversityByIdThenGetUniversityByIdAfterUpdateReturnChangedName() {
        int universityId = insertUniversity(UNIVERSITY_NAME_1);
        universityService.updateUniversity(UNIVERSITY_NAME_1_UPDATED, universityId);
        University universityUpdated = universityService.getUniversityById(universityId);
        assertEquals(UNIVERSITY_NAME_1_UPDATED, universityUpdated.getName());
    }

    @Test
    void whenDeleteUniversityByIdIfSuccessThenReturnTrue() {
        int universityId = insertUniversity(UNIVERSITY_NAME_1);
        assertTrue(universityService.deleteUniversityById(universityId));
    }

    @Test
    void afterDeleteUniversityByIdIfSearchForItReturnEmptyResultDataAccessException() {
        int universityId = insertUniversity(UNIVERSITY_NAME_1);
        universityService.deleteUniversityById(universityId);
        assertThrows(EmptyResultDataAccessException.class, () -> universityService.getUniversityById(universityId));
    }

    private int insertUniversity(String UniversityName) {
        universityService.addUniversity(UNIVERSITY_NAME_1);
        return universityService.getUniversityByName(UNIVERSITY_NAME_1).getId();
    }
}
