<<<<<<< HEAD
package ua.com.nikiforov.services.timetables;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import ua.com.nikiforov.config.UniversityConfig;
import ua.com.nikiforov.dao.table_creator.TableCreator;
import ua.com.nikiforov.exceptions.EntityNotFoundException;
import ua.com.nikiforov.models.timetable.Timetable;

@SpringJUnitConfig(UniversityConfig.class)
class StudentTimetableServiceTest {

    private static final long LESSON_ID_1 = 1;
    private static final long LESSON_ID_2 = 2;
    private static final long LESSON_ID_3 = 3;
    private static final long STUDENT_ID_1 = 1;
    private static final long STUDENT_ID_2 = 2;

    private static final String DATE_1 = "2020-09-15";
    private static final String DATE_1_ADD_1_DAY = "2020-09-16";
    private static final String DATE_1_ADD_3_DAYS = "2020-09-18";
    private static final String DATE_1_ADD_13_DAYS = "2020-09-28";
    private static final String DATE_1_ADD_21_DAYS = "2020-10-06";
    private static final String DATE_1_ADD_33_DAYS = "2020-11-18";
    private static final String DATE = "2020-10-15";

    @Autowired
    private StudentTimetableService studentTimetableService;

    @Autowired
    private TableCreator tableCreator;

    @BeforeEach
    void init() {
        tableCreator.createTables();
    }

    @Test
    void whenAddTimetableIfSuccessReturnTrue() {
        assertTrue(studentTimetableService.addTimetable(LESSON_ID_1, STUDENT_ID_1, DATE, Period.FIRST));
    }

    private Timetable insertTimetable(long lessonId, long teacherId, String stringDate, Period period) {
        studentTimetableService.addTimetable(lessonId, teacherId, stringDate, period);
        return studentTimetableService.getTimetableByLessonPersonTimePeriod(lessonId, teacherId, stringDate, period);
    }

    @Test
    void whenGetTimetableByDateAndStudentIdShouldReturnListOfTimetables() {
        List<Timetable> expectedTimetables = new ArrayList<>();
        expectedTimetables.add(insertTimetable(LESSON_ID_1, STUDENT_ID_1, DATE_1, Period.FIRST));
        expectedTimetables.add(insertTimetable(LESSON_ID_2, STUDENT_ID_1, DATE_1, Period.SECOND));
        expectedTimetables.add(insertTimetable(LESSON_ID_3, STUDENT_ID_1, DATE_1, Period.THIRD));

        studentTimetableService.addTimetable(LESSON_ID_3, STUDENT_ID_1, DATE_1_ADD_1_DAY, Period.SECOND);
        studentTimetableService.addTimetable(LESSON_ID_3, STUDENT_ID_2, DATE_1_ADD_13_DAYS, Period.THIRD);
        studentTimetableService.addTimetable(LESSON_ID_2, STUDENT_ID_1, DATE_1_ADD_3_DAYS, Period.THIRD);

        List<Timetable> actualTimetables = studentTimetableService.getDayTimetable(DATE_1, STUDENT_ID_1);
        assertEquals(expectedTimetables, actualTimetables);
    }

    @Test
    void whenGetTimetableByIdShouldReturnCorrespondingTimteble() {
        Timetable expectedTimetable = insertTimetable(LESSON_ID_1, STUDENT_ID_1, DATE, Period.FIRST);
        assertEquals(expectedTimetable, studentTimetableService.getTimetableById(expectedTimetable.getId()));
    }

    @Test
    void whenGetAllTimetablesShouldReturnListOfTimetables() {
        List<Timetable> expectedTimetables = new ArrayList<>();
        expectedTimetables.add(insertTimetable(LESSON_ID_1, STUDENT_ID_1, DATE_1, Period.FIRST));
        expectedTimetables.add(insertTimetable(LESSON_ID_2, STUDENT_ID_1, DATE_1_ADD_13_DAYS, Period.SECOND));
        expectedTimetables.add(insertTimetable(LESSON_ID_3, STUDENT_ID_1, DATE_1_ADD_21_DAYS, Period.THIRD));
        expectedTimetables.add(insertTimetable(LESSON_ID_1, STUDENT_ID_2, DATE_1_ADD_1_DAY, Period.FIRST));
        expectedTimetables.add(insertTimetable(LESSON_ID_2, STUDENT_ID_2, DATE_1_ADD_33_DAYS, Period.SECOND));
        expectedTimetables.add(insertTimetable(LESSON_ID_3, STUDENT_ID_2, DATE_1_ADD_33_DAYS, Period.THIRD));
        List<Timetable> actualTimetables = studentTimetableService.getAllTimetables();
        assertIterableEquals(expectedTimetables, actualTimetables);
    }

    @Test
    void whenUpdateTimetableShouldReturnUpdatedTimetable() {
        Timetable timetable = insertTimetable(LESSON_ID_1, STUDENT_ID_1, DATE_1, Period.FIRST);
        long timetableId = timetable.getId();
        studentTimetableService.updateTimetable(LESSON_ID_2, STUDENT_ID_2, DATE_1_ADD_1_DAY, Period.FIFTH, timetableId);
        Timetable expectedTimeTable = studentTimetableService.getTimetableByLessonPersonTimePeriod(LESSON_ID_2,
                STUDENT_ID_2, DATE_1_ADD_1_DAY, Period.FIFTH);
        Timetable actualTimetable = studentTimetableService.getTimetableById(timetableId);
        assertEquals(expectedTimeTable, actualTimetable);
    }

    @Test
    void whenDeleteTimetableByIdIfAfterGetTimetableByIdShouldThrowEmptyResultDataAccessException() {
        long timetableId = insertTimetable(LESSON_ID_1, STUDENT_ID_1, DATE, Period.FIRST).getId();
        studentTimetableService.deleteTimetableById(timetableId);
        assertThrows(EntityNotFoundException.class, () -> studentTimetableService.getTimetableById(timetableId));
    }

    @Test
    void whenGetTimtableForDayShouldReturnListOfTimetableOfSetDate() {
        List<Timetable> expectedTimetables = new ArrayList<>();
        expectedTimetables.add(insertTimetable(LESSON_ID_1, STUDENT_ID_1, DATE_1, Period.FIRST));
        expectedTimetables.add(insertTimetable(LESSON_ID_2, STUDENT_ID_1, DATE_1, Period.SECOND));
        expectedTimetables.add(insertTimetable(LESSON_ID_3, STUDENT_ID_1, DATE_1, Period.THIRD));

        studentTimetableService.addTimetable(LESSON_ID_2, STUDENT_ID_2, DATE_1, Period.FIRST);
        studentTimetableService.addTimetable(LESSON_ID_1, STUDENT_ID_1, DATE_1_ADD_1_DAY, Period.FIRST);
        studentTimetableService.addTimetable(LESSON_ID_3, STUDENT_ID_2, DATE_1_ADD_21_DAYS, Period.SECOND);
        studentTimetableService.addTimetable(LESSON_ID_1, STUDENT_ID_2, DATE_1_ADD_13_DAYS, Period.FIRST);
        studentTimetableService.addTimetable(LESSON_ID_3, STUDENT_ID_2, DATE_1_ADD_33_DAYS, Period.SECOND);
        List<Timetable> actualTimetable = studentTimetableService.getDayTimetable(DATE_1, STUDENT_ID_1);
        assertIterableEquals(expectedTimetables, actualTimetable);
    }

    @Test
    void whenGetTimtableForMonthShouldReturnListOfTimetablesFromSetDateWithinMonth() {
        List<Timetable> expectedTimetables = new ArrayList<>();
        expectedTimetables.add(insertTimetable(LESSON_ID_1, STUDENT_ID_1, DATE_1, Period.FIRST));
        expectedTimetables.add(insertTimetable(LESSON_ID_2, STUDENT_ID_1, DATE_1, Period.SECOND));
        expectedTimetables.add(insertTimetable(LESSON_ID_3, STUDENT_ID_1, DATE_1, Period.THIRD));

        studentTimetableService.addTimetable(LESSON_ID_2, STUDENT_ID_2, DATE_1, Period.SECOND);
        studentTimetableService.addTimetable(LESSON_ID_3, STUDENT_ID_2, DATE_1, Period.THIRD);

        expectedTimetables.add(insertTimetable(LESSON_ID_1, STUDENT_ID_1, DATE_1_ADD_1_DAY, Period.FIRST));
        expectedTimetables.add(insertTimetable(LESSON_ID_3, STUDENT_ID_1, DATE_1_ADD_1_DAY, Period.SECOND));

        expectedTimetables.add(insertTimetable(LESSON_ID_1, STUDENT_ID_1, DATE_1_ADD_3_DAYS, Period.FIRST));
        expectedTimetables.add(insertTimetable(LESSON_ID_2, STUDENT_ID_1, DATE_1_ADD_3_DAYS, Period.SECOND));

        expectedTimetables.add(insertTimetable(LESSON_ID_1, STUDENT_ID_1, DATE_1_ADD_13_DAYS, Period.FIRST));
        expectedTimetables.add(insertTimetable(LESSON_ID_2, STUDENT_ID_1, DATE_1_ADD_13_DAYS, Period.SECOND));

        studentTimetableService.addTimetable(LESSON_ID_1, STUDENT_ID_2, DATE_1_ADD_13_DAYS, Period.FIRST);
        studentTimetableService.addTimetable(LESSON_ID_2, STUDENT_ID_2, DATE_1_ADD_13_DAYS, Period.SECOND);

        expectedTimetables.add(insertTimetable(LESSON_ID_1, STUDENT_ID_1, DATE_1_ADD_21_DAYS, Period.FIRST));
        expectedTimetables.add(insertTimetable(LESSON_ID_2, STUDENT_ID_1, DATE_1_ADD_21_DAYS, Period.SECOND));

        studentTimetableService.addTimetable(LESSON_ID_1, STUDENT_ID_1, DATE_1_ADD_33_DAYS, Period.FIRST);
        studentTimetableService.addTimetable(LESSON_ID_2, STUDENT_ID_1, DATE_1_ADD_33_DAYS, Period.SECOND);
        List<Timetable> actualTimetables = studentTimetableService.getMonthTimetable(DATE_1, STUDENT_ID_1);
        assertIterableEquals(expectedTimetables, actualTimetables);
    }


}
=======
package ua.com.nikiforov.services.timetables;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.zone.ZoneRulesProvider;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import ua.com.nikiforov.config.UniversityConfig;
import ua.com.nikiforov.dao.table_creator.TableCreator;
import ua.com.nikiforov.exceptions.EntityNotFoundException;
import ua.com.nikiforov.models.timetable.Timetable;

@SpringJUnitConfig(UniversityConfig.class)
class StudentTimetableServiceTest {

    private static final long LESSON_ID_1 = 1;
    private static final long LESSON_ID_2 = 2;
    private static final long LESSON_ID_3 = 3;
    private static final long STUDENT_ID_1 = 1;
    private static final long STUDENT_ID_2 = 2;

    private static final String DATE_1 = "2020-09-15";
    private static final String DATE_1_ADD_1_DAY = "2020-09-16";
    private static final String DATE_1_ADD_3_DAYS = "2020-09-18";
    private static final String DATE_1_ADD_13_DAYS = "2020-09-28";
    private static final String DATE_1_ADD_21_DAYS = "2020-10-06";
    private static final String DATE_1_ADD_33_DAYS = "2020-11-18";
    private static final String DATE = "2020-10-15";

    @Autowired
    private StudentTimetableService studentTimetableService;

    @Autowired
    private TableCreator tableCreator;

    @BeforeEach
    void init() {
        tableCreator.createTables();
    }

    @Test
    void whenAddTimetableIfSuccessReturnTrue() {
        assertTrue(studentTimetableService.addTimetable(LESSON_ID_1, STUDENT_ID_1, DATE, Period.FIRST));
    }

    private Timetable insertTimetable(long lessonId, long teacherId, String stringDate, Period period) {
        studentTimetableService.addTimetable(lessonId, teacherId, stringDate, period);
        return studentTimetableService.getTimetableByLessonPersonTimePeriod(lessonId, teacherId, stringDate, period);
    }

    @Test
    void whenGetTimetableByDateAndStudentIdShouldReturnListOfTimetables() {
        List<Timetable> expectedTimetables = new ArrayList<>();
        expectedTimetables.add(insertTimetable(LESSON_ID_1, STUDENT_ID_1, DATE_1, Period.FIRST));
        expectedTimetables.add(insertTimetable(LESSON_ID_2, STUDENT_ID_1, DATE_1, Period.SECOND));
        expectedTimetables.add(insertTimetable(LESSON_ID_3, STUDENT_ID_1, DATE_1, Period.THIRD));

        studentTimetableService.addTimetable(LESSON_ID_3, STUDENT_ID_1, DATE_1_ADD_1_DAY, Period.SECOND);
        studentTimetableService.addTimetable(LESSON_ID_3, STUDENT_ID_2, DATE_1_ADD_13_DAYS, Period.THIRD);
        studentTimetableService.addTimetable(LESSON_ID_2, STUDENT_ID_1, DATE_1_ADD_3_DAYS, Period.THIRD);

        List<Timetable> actualTimetables = studentTimetableService.getDayTimetable(DATE_1, STUDENT_ID_1);
        assertEquals(expectedTimetables, actualTimetables);
    }

    @Test
    void whenGetTimetableByIdShouldReturnCorrespondingTimteble() {
        Timetable expectedTimetable = insertTimetable(LESSON_ID_1, STUDENT_ID_1, DATE, Period.FIRST);
        assertEquals(expectedTimetable, studentTimetableService.getTimetableById(expectedTimetable.getId()));
    }

    @Test
    void whenGetAllTimetablesShouldReturnListOfTimetables() {
        List<Timetable> expectedTimetables = new ArrayList<>();
        expectedTimetables.add(insertTimetable(LESSON_ID_1, STUDENT_ID_1, DATE_1, Period.FIRST));
        expectedTimetables.add(insertTimetable(LESSON_ID_2, STUDENT_ID_1, DATE_1_ADD_13_DAYS, Period.SECOND));
        expectedTimetables.add(insertTimetable(LESSON_ID_3, STUDENT_ID_1, DATE_1_ADD_21_DAYS, Period.THIRD));
        expectedTimetables.add(insertTimetable(LESSON_ID_1, STUDENT_ID_2, DATE_1_ADD_1_DAY, Period.FIRST));
        expectedTimetables.add(insertTimetable(LESSON_ID_2, STUDENT_ID_2, DATE_1_ADD_33_DAYS, Period.SECOND));
        expectedTimetables.add(insertTimetable(LESSON_ID_3, STUDENT_ID_2, DATE_1_ADD_33_DAYS, Period.THIRD));
        List<Timetable> actualTimetables = studentTimetableService.getAllTimetables();
        assertIterableEquals(expectedTimetables, actualTimetables);
    }

    @Test
    void whenUpdateTimetableShouldReturnUpdatedTimetable() {
        Timetable timetable = insertTimetable(LESSON_ID_1, STUDENT_ID_1, DATE_1, Period.FIRST);
        long timetableId = timetable.getId();
        studentTimetableService.updateTimetable(LESSON_ID_2, STUDENT_ID_2, DATE_1_ADD_1_DAY, Period.FIFTH, timetableId);
        Timetable expectedTimeTable = studentTimetableService.getTimetableByLessonPersonTimePeriod(LESSON_ID_2,
                STUDENT_ID_2, DATE_1_ADD_1_DAY, Period.FIFTH);
        Timetable actualTimetable = studentTimetableService.getTimetableById(timetableId);
        assertEquals(expectedTimeTable, actualTimetable);
    }

    @Test
    void whenDeleteTimetableByIdIfAfterGetTimetableByIdShouldThrowEmptyResultDataAccessException() {
        long timetableId = insertTimetable(LESSON_ID_1, STUDENT_ID_1, DATE, Period.FIRST).getId();
        studentTimetableService.deleteTimetableById(timetableId);
        assertThrows(EntityNotFoundException.class, () -> studentTimetableService.getTimetableById(timetableId));
    }

    @Test
    void whenGetTimtableForDayShouldReturnListOfTimetableOfSetDate() {
        List<Timetable> expectedTimetables = new ArrayList<>();
        expectedTimetables.add(insertTimetable(LESSON_ID_1, STUDENT_ID_1, DATE_1, Period.FIRST));
        expectedTimetables.add(insertTimetable(LESSON_ID_2, STUDENT_ID_1, DATE_1, Period.SECOND));
        expectedTimetables.add(insertTimetable(LESSON_ID_3, STUDENT_ID_1, DATE_1, Period.THIRD));

        studentTimetableService.addTimetable(LESSON_ID_2, STUDENT_ID_2, DATE_1, Period.FIRST);
        studentTimetableService.addTimetable(LESSON_ID_1, STUDENT_ID_1, DATE_1_ADD_1_DAY, Period.FIRST);
        studentTimetableService.addTimetable(LESSON_ID_3, STUDENT_ID_2, DATE_1_ADD_21_DAYS, Period.SECOND);
        studentTimetableService.addTimetable(LESSON_ID_1, STUDENT_ID_2, DATE_1_ADD_13_DAYS, Period.FIRST);
        studentTimetableService.addTimetable(LESSON_ID_3, STUDENT_ID_2, DATE_1_ADD_33_DAYS, Period.SECOND);
        List<Timetable> actualTimetable = studentTimetableService.getDayTimetable(DATE_1, STUDENT_ID_1);
        assertIterableEquals(expectedTimetables, actualTimetable);
    }

    @Test
    void whenGetTimtableForMonthShouldReturnListOfTimetablesFromSetDateWithinMonth() {
        List<Timetable> expectedTimetables = new ArrayList<>();
        expectedTimetables.add(insertTimetable(LESSON_ID_1, STUDENT_ID_1, DATE_1, Period.FIRST));
        expectedTimetables.add(insertTimetable(LESSON_ID_2, STUDENT_ID_1, DATE_1, Period.SECOND));
        expectedTimetables.add(insertTimetable(LESSON_ID_3, STUDENT_ID_1, DATE_1, Period.THIRD));

        studentTimetableService.addTimetable(LESSON_ID_2, STUDENT_ID_2, DATE_1, Period.SECOND);
        studentTimetableService.addTimetable(LESSON_ID_3, STUDENT_ID_2, DATE_1, Period.THIRD);

        expectedTimetables.add(insertTimetable(LESSON_ID_1, STUDENT_ID_1, DATE_1_ADD_1_DAY, Period.FIRST));
        expectedTimetables.add(insertTimetable(LESSON_ID_3, STUDENT_ID_1, DATE_1_ADD_1_DAY, Period.SECOND));

        expectedTimetables.add(insertTimetable(LESSON_ID_1, STUDENT_ID_1, DATE_1_ADD_3_DAYS, Period.FIRST));
        expectedTimetables.add(insertTimetable(LESSON_ID_2, STUDENT_ID_1, DATE_1_ADD_3_DAYS, Period.SECOND));

        expectedTimetables.add(insertTimetable(LESSON_ID_1, STUDENT_ID_1, DATE_1_ADD_13_DAYS, Period.FIRST));
        expectedTimetables.add(insertTimetable(LESSON_ID_2, STUDENT_ID_1, DATE_1_ADD_13_DAYS, Period.SECOND));

        studentTimetableService.addTimetable(LESSON_ID_1, STUDENT_ID_2, DATE_1_ADD_13_DAYS, Period.FIRST);
        studentTimetableService.addTimetable(LESSON_ID_2, STUDENT_ID_2, DATE_1_ADD_13_DAYS, Period.SECOND);

        expectedTimetables.add(insertTimetable(LESSON_ID_1, STUDENT_ID_1, DATE_1_ADD_21_DAYS, Period.FIRST));
        expectedTimetables.add(insertTimetable(LESSON_ID_2, STUDENT_ID_1, DATE_1_ADD_21_DAYS, Period.SECOND));

        studentTimetableService.addTimetable(LESSON_ID_1, STUDENT_ID_1, DATE_1_ADD_33_DAYS, Period.FIRST);
        studentTimetableService.addTimetable(LESSON_ID_2, STUDENT_ID_1, DATE_1_ADD_33_DAYS, Period.SECOND);
        List<Timetable> actualTimetables = studentTimetableService.getMonthTimetable(DATE_1, STUDENT_ID_1);
        assertIterableEquals(expectedTimetables, actualTimetables);
    }


}
>>>>>>> refs/remotes/origin/master
