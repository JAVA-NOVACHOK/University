package ua.com.nikiforov.services.university;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;

import ua.com.nikiforov.dto.UniversityDTO;
import ua.com.nikiforov.exceptions.EntityNotFoundException;
import ua.com.nikiforov.models.University;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
class UniversityServiceImplTest {

    private static final String UNIVERSITY_NAME_1 = "Foxminded";
    private static final String UNIVERSITY_NAME_1_UPDATED = "Foxminded LTD.";
    private static final String UNIVERSITY_NAME_2 = "GlobalLogic";

    private static final int UNIVERSITIES_COUNT = 2;


    @Autowired
    private UniversityService universityService;

    @Test
    void whenAddUniversityIfSuccessShouldReturnTrue() {
        assertDoesNotThrow(() -> universityService.addUniversity(UNIVERSITY_NAME_1));
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
    void whenUpdateUniversityByIdIfSuccessThenReturnTrue() {
        int universityId = insertUniversity(UNIVERSITY_NAME_1);
        assertDoesNotThrow(() -> universityService.updateUniversity(new UniversityDTO(universityId,UNIVERSITY_NAME_1_UPDATED)));
    }

    @Test
    void whenUpdateUniversityByIdThenGetUniversityByIdAfterUpdateReturnChangedName() {
        int universityId = insertUniversity(UNIVERSITY_NAME_1);
        universityService.updateUniversity(new UniversityDTO(universityId,UNIVERSITY_NAME_1_UPDATED));
        University universityUpdated = universityService.getUniversityById(universityId);
        assertEquals(UNIVERSITY_NAME_1_UPDATED, universityUpdated.getName());
    }

    @Test
    void whenDeleteUniversityByIdIfSuccessThenReturnTrue() {
        int universityId = insertUniversity(UNIVERSITY_NAME_1);
        assertDoesNotThrow(() -> universityService.deleteUniversityById(universityId));
    }

    @Test
    void afterDeleteUniversityByIdIfSearchForItReturnEmptyResultDataAccessException() {
        int universityId = insertUniversity(UNIVERSITY_NAME_1);
        universityService.deleteUniversityById(universityId);
        assertThrows(EntityNotFoundException.class, () -> universityService.getUniversityById(universityId));
    }

    private int insertUniversity(String UniversityName) {
        universityService.addUniversity(UNIVERSITY_NAME_1);
        return universityService.getUniversityByName(UNIVERSITY_NAME_1).getId();
    }
}
