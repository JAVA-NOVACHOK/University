package ua.com.nikiforov.services.university;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import ua.com.nikiforov.dto.UniversityDTO;
import ua.com.nikiforov.helper.SetupTestHelper;

import javax.persistence.EntityNotFoundException;


class UniversityServiceImplTest extends SetupTestHelper {

    private static final String UNIVERSITY_NAME_1 = "Foxminded";
    private static final String UNIVERSITY_NAME_1_UPDATED = "Foxminded LTD.";
    private static final String UNIVERSITY_NAME_2 = "GlobalLogic";

    private static final int UNIVERSITIES_COUNT = 2;


    @Autowired
    private UniversityService universityService;

    private UniversityDTO university_1;

    @BeforeEach
    void startup(){
        university_1 = insertUniversity(UNIVERSITY_NAME_1);
    }

    @Test
    void whenAddUniversityIfSuccessShouldReturnTrue() {
        assertDoesNotThrow(() -> universityService.addUniversity(UNIVERSITY_NAME_2));
    }

    @Test
    void whenGetUniversityByNameReturnCorrectUniversity() {
        assertEquals(university_1, universityService.getUniversityByName(UNIVERSITY_NAME_1));
    }

    @Test
    void whenGetUniversityByIDReturnCorrectUniversity() {
        assertEquals(university_1, universityService.getUniversityById(university_1.getId()));
    }


    @Test
    void whenUpdateUniversityIfSuccessThenDoesNotThrow() {
        assertDoesNotThrow(() -> universityService.updateUniversity(new UniversityDTO(university_1.getId(),UNIVERSITY_NAME_1_UPDATED)));
    }

    @Test
    void whenUpdateUniversityByIdThenGetUniversityByIdAfterUpdateReturnChangedName() {
        universityService.updateUniversity(new UniversityDTO(university_1.getId(),UNIVERSITY_NAME_1_UPDATED));
        assertEquals(new UniversityDTO(university_1.getId(),UNIVERSITY_NAME_1_UPDATED), universityService.getUniversityById(university_1.getId()));
    }

    @Test
    void whenDeleteUniversityByIdIfSuccessThenReturnTrue() {
        assertDoesNotThrow(() -> universityService.deleteUniversityById(university_1.getId()));
    }

    @Test
    void afterDeleteUniversityByIdIfSearchForItReturnEmptyResultDataAccessException() {
        universityService.deleteUniversityById(university_1.getId());
        assertThrows(EntityNotFoundException.class, () -> universityService.getUniversityById(university_1.getId()));
    }

    private UniversityDTO insertUniversity(String universityName) {
        return universityService.addUniversity(universityName);
    }
}
