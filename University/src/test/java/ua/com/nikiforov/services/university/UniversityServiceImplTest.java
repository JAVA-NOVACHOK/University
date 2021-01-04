package ua.com.nikiforov.services.university;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import ua.com.nikiforov.dto.UniversityDTO;

import javax.persistence.EntityNotFoundException;

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

    private UniversityDTO university_1;

    @BeforeAll
    void startup(){
        university_1 = insertUniversity(UNIVERSITY_NAME_1);
    }

    @Test
    @Order(1)
    void whenAddUniversityIfSuccessShouldReturnTrue() {
        assertDoesNotThrow(() -> universityService.addUniversity(UNIVERSITY_NAME_2));
    }

    @Test
    @Order(2)
    void whenGetUniversityByNameReturnCorrectUniversity() {
        assertEquals(university_1, universityService.getUniversityByName(UNIVERSITY_NAME_1));
    }

    @Test
    @Order(3)
    void whenGetUniversityByIDReturnCorrectUniversity() {
        assertEquals(university_1, universityService.getUniversityById(university_1.getId()));
    }


    @Test
    @Order(4)
    void whenUpdateUniversityByIdIfSuccessThenReturnTrue() {
        assertDoesNotThrow(() -> universityService.updateUniversity(new UniversityDTO(university_1.getId(),UNIVERSITY_NAME_1_UPDATED)));
    }

    @Test
    @Order(5)
    void whenUpdateUniversityByIdThenGetUniversityByIdAfterUpdateReturnChangedName() {
        assertEquals(new UniversityDTO(university_1.getId(),UNIVERSITY_NAME_1_UPDATED), universityService.getUniversityById(university_1.getId()));
    }

    @Test
    @Order(6)
    void whenDeleteUniversityByIdIfSuccessThenReturnTrue() {
        assertDoesNotThrow(() -> universityService.deleteUniversityById(university_1.getId()));
    }

    @Test
    @Order(7)
    void afterDeleteUniversityByIdIfSearchForItReturnEmptyResultDataAccessException() {
        assertThrows(EntityNotFoundException.class, () -> universityService.getUniversityById(university_1.getId()));
    }

    private UniversityDTO insertUniversity(String UniversityName) {
        return universityService.addUniversity(UNIVERSITY_NAME_1);
    }
}
