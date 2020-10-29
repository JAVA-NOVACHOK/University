<<<<<<< HEAD
package ua.com.nikiforov.services.timetables;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;

import ua.com.nikiforov.config.UniversityConfig;
import ua.com.nikiforov.dao.table_creator.TableCreator;
import ua.com.nikiforov.exceptions.EntityNotFoundException;
import ua.com.nikiforov.models.timetable.Timetable;

@SpringJUnitConfig(UniversityConfig.class)
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
class TeachersTimetableServiceTest {

    private static final long LESSON_ID_1 = 1;
    private static final long LESSON_ID_2 = 2;
    private static final long LESSON_ID_3 = 3;
    private static final long TEACHER_ID_1 = 1;
    private static final long TEACHER_ID_2 = 2;

    private static final String DATE_1 = "2020-09-15";
    private static final String DATE_1_ADD_1_DAY = "2020-09-16";
    private static final String DATE_1_ADD_3_DAYS = "2020-09-18";
    private static final String DATE_1_ADD_13_DAYS = "2020-09-28";
    private static final String DATE_1_ADD_21_DAYS = "2020-10-06";
    private static final String DATE_1_ADD_33_DAYS = "2020-11-18";
    private static final String DATE = "2020-10-15";

    @Autowired
    private TeachersTimetableService teachersTimtableService;

    @Autowired
    private TableCreator tableCreator;

    @BeforeEach
    void init() {
        tableCreator.createTables();
    }

    @Test
    void whenAddTimetableIfSuccessReturnTrue() {
        assertTrue(teachersTimtableService.addTimetable(LESSON_ID_1, TEACHER_ID_1, DATE, Period.FIRST));
    }

    private Timetable insertTimetable(long lessonId, long teacherId, String stringDate, Period period) {
        teachersTimtableService.addTimetable(lessonId, teacherId, stringDate, period);
        return teachersTimtableService.getTimetableByLessonPersonTimePeriod(lessonId, teacherId, stringDate, period);
    }

    @Test
    void whenGetTimetableByDateAndTeacherIdShouldReturnListOfTimetables() {
        List<Timetable> expectedTimetables = new ArrayList<>();
        expectedTimetables.add(insertTimetable(LESSON_ID_1, TEACHER_ID_1, DATE_1, Period.FIRST));
        expectedTimetables.add(insertTimetable(LESSON_ID_2, TEACHER_ID_1, DATE_1, Period.SECOND));
        expectedTimetables.add(insertTimetable(LESSON_ID_3, TEACHER_ID_1, DATE_1, Period.THIRD));

        teachersTimtableService.addTimetable(LESSON_ID_3, TEACHER_ID_1, DATE_1_ADD_1_DAY, Period.SECOND);
        teachersTimtableService.addTimetable(LESSON_ID_3, TEACHER_ID_2, DATE_1_ADD_13_DAYS, Period.THIRD);
        teachersTimtableService.addTimetable(LESSON_ID_2, TEACHER_ID_1, DATE_1_ADD_3_DAYS, Period.THIRD);

        List<Timetable> actualTimetables = teachersTimtableService.getDayTimetable(DATE_1, TEACHER_ID_1);
        assertEquals(expectedTimetables, actualTimetables);
    }

    @Test
    void whenGetTimetableByIdShouldReturnCorrespondingTimteble() {
        Timetable expectedTimetable = insertTimetable(LESSON_ID_1, TEACHER_ID_1, DATE, Period.FIRST);
        assertEquals(expectedTimetable, teachersTimtableService.getTimetableById(expectedTimetable.getId()));
    }

    @Test
    void whenGetAllTimetablesShouldReturnListOfTimetables() {
        List<Timetable> expectedTimetables = new ArrayList<>();
        expectedTimetables.add(insertTimetable(LESSON_ID_1, TEACHER_ID_1, DATE_1, Period.FIRST));
        expectedTimetables.add(insertTimetable(LESSON_ID_2, TEACHER_ID_1, DATE_1_ADD_13_DAYS, Period.SECOND));
        expectedTimetables.add(insertTimetable(LESSON_ID_3, TEACHER_ID_1, DATE_1_ADD_21_DAYS, Period.THIRD));
        expectedTimetables.add(insertTimetable(LESSON_ID_1, TEACHER_ID_2, DATE_1_ADD_1_DAY, Period.FIRST));
        expectedTimetables.add(insertTimetable(LESSON_ID_2, TEACHER_ID_2, DATE_1_ADD_33_DAYS, Period.SECOND));
        expectedTimetables.add(insertTimetable(LESSON_ID_3, TEACHER_ID_2, DATE_1_ADD_33_DAYS, Period.THIRD));
        List<Timetable> actualTimetables = teachersTimtableService.getAllTimetables();
        assertIterableEquals(expectedTimetables, actualTimetables);
    }

    @Test
    void whenUpdateTimetableShouldReturnUpdatedTimetable() {
        Timetable timetable = insertTimetable(LESSON_ID_1, TEACHER_ID_1, DATE_1, Period.FIRST);
        long timetableId = timetable.getId();
        teachersTimtableService.updateTimetable(LESSON_ID_2, TEACHER_ID_2, DATE_1_ADD_1_DAY, Period.FIFTH, timetableId);
        Timetable expectedTimeTable = teachersTimtableService.getTimetableByLessonPersonTimePeriod(LESSON_ID_2,
                TEACHER_ID_2, DATE_1_ADD_1_DAY, Period.FIFTH);
        Timetable actualTimetable = teachersTimtableService.getTimetableById(timetableId);
        assertEquals(expectedTimeTable, actualTimetable);
    }

    @Test
    void whenDeleteTimetableByIdIfAfterGetTimetableByIdShouldThrowEmptyResultDataAccessException() {
        long timetableId = insertTimetable(LESSON_ID_1, TEACHER_ID_1, DATE, Period.FIRST).getId();
        teachersTimtableService.deleteTimetableById(timetableId);
        assertThrows(EntityNotFoundException.class, () -> teachersTimtableService.getTimetableById(timetableId));
    }

    @Test
    void whenGetTimtableForDayShouldReturnListOfTimetableOfSetDate() {
        List<Timetable> expectedTimetables = new ArrayList<>();
        expectedTimetables.add(insertTimetable(LESSON_ID_1, TEACHER_ID_1, DATE_1, Period.FIRST));
        expectedTimetables.add(insertTimetable(LESSON_ID_2, TEACHER_ID_1, DATE_1, Period.SECOND));
        expectedTimetables.add(insertTimetable(LESSON_ID_3, TEACHER_ID_1, DATE_1, Period.THIRD));

        teachersTimtableService.addTimetable(LESSON_ID_2, TEACHER_ID_2, DATE_1, Period.FIRST);
        teachersTimtableService.addTimetable(LESSON_ID_1, TEACHER_ID_1, DATE_1_ADD_1_DAY, Period.FIRST);
        teachersTimtableService.addTimetable(LESSON_ID_3, TEACHER_ID_2, DATE_1_ADD_21_DAYS, Period.SECOND);
        teachersTimtableService.addTimetable(LESSON_ID_1, TEACHER_ID_2, DATE_1_ADD_13_DAYS, Period.FIRST);
        teachersTimtableService.addTimetable(LESSON_ID_3, TEACHER_ID_2, DATE_1_ADD_33_DAYS, Period.SECOND);
        List<Timetable> actualTimetable = teachersTimtableService.getDayTimetable(DATE_1, TEACHER_ID_1);
        assertIterableEquals(expectedTimetables, actualTimetable);
    }

    @Test
    void whenGetTimtableForMonthShouldReturnListOfTimetablesFromSetDateWithinMonth() {
        List<Timetable> expectedTimetables = new ArrayList<>();
        expectedTimetables.add(insertTimetable(LESSON_ID_1, TEACHER_ID_1, DATE_1, Period.FIRST));
        expectedTimetables.add(insertTimetable(LESSON_ID_2, TEACHER_ID_1, DATE_1, Period.SECOND));
        expectedTimetables.add(insertTimetable(LESSON_ID_3, TEACHER_ID_1, DATE_1, Period.THIRD));

        teachersTimtableService.addTimetable(LESSON_ID_2, TEACHER_ID_2, DATE_1, Period.SECOND);
        teachersTimtableService.addTimetable(LESSON_ID_3, TEACHER_ID_2, DATE_1, Period.THIRD);

        expectedTimetables.add(insertTimetable(LESSON_ID_1, TEACHER_ID_1, DATE_1_ADD_1_DAY, Period.FIRST));
        expectedTimetables.add(insertTimetable(LESSON_ID_3, TEACHER_ID_1, DATE_1_ADD_1_DAY, Period.SECOND));

        expectedTimetables.add(insertTimetable(LESSON_ID_1, TEACHER_ID_1, DATE_1_ADD_3_DAYS, Period.FIRST));
        expectedTimetables.add(insertTimetable(LESSON_ID_2, TEACHER_ID_1, DATE_1_ADD_3_DAYS, Period.SECOND));

        expectedTimetables.add(insertTimetable(LESSON_ID_1, TEACHER_ID_1, DATE_1_ADD_13_DAYS, Period.FIRST));
        expectedTimetables.add(insertTimetable(LESSON_ID_2, TEACHER_ID_1, DATE_1_ADD_13_DAYS, Period.SECOND));

        teachersTimtableService.addTimetable(LESSON_ID_1, TEACHER_ID_2, DATE_1_ADD_13_DAYS, Period.FIRST);
        teachersTimtableService.addTimetable(LESSON_ID_2, TEACHER_ID_2, DATE_1_ADD_13_DAYS, Period.SECOND);

        expectedTimetables.add(insertTimetable(LESSON_ID_1, TEACHER_ID_1, DATE_1_ADD_21_DAYS, Period.FIRST));
        expectedTimetables.add(insertTimetable(LESSON_ID_2, TEACHER_ID_1, DATE_1_ADD_21_DAYS, Period.SECOND));

        teachersTimtableService.addTimetable(LESSON_ID_1, TEACHER_ID_1, DATE_1_ADD_33_DAYS, Period.FIRST);
        teachersTimtableService.addTimetable(LESSON_ID_2, TEACHER_ID_1, DATE_1_ADD_33_DAYS, Period.SECOND);
        List<Timetable> actualTimetables = teachersTimtableService.getMonthTimetable(DATE_1, TEACHER_ID_1);
        assertIterableEquals(expectedTimetables, actualTimetables);
    }

}
=======
package ua.com.nikiforov.services.timetables;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
class TeachersTimetableServiceTest {

    private static final long LESSON_ID_1 = 1;
    private static final long LESSON_ID_2 = 2;
    private static final long LESSON_ID_3 = 3;
    private static final long TEACHER_ID_1 = 1;
    private static final long TEACHER_ID_2 = 2;

    private static final String DATE_1 = "2020-09-15";
    private static final String DATE_1_ADD_1_DAY = "2020-09-16";
    private static final String DATE_1_ADD_3_DAYS = "2020-09-18";
    private static final String DATE_1_ADD_13_DAYS = "2020-09-28";
    private static final String DATE_1_ADD_21_DAYS = "2020-10-06";
    private static final String DATE_1_ADD_33_DAYS = "2020-11-18";
    private static final String DATE = "2020-10-15";

    @Autowired
    private TeachersTimetableService teachersTimtableService;

    @Autowired
    private TableCreator tableCreator;

    @BeforeEach
    void init() {
        tableCreator.createTables();
    }

    @Test
    void whenAddTimetableIfSuccessReturnTrue() {
        assertTrue(teachersTimtableService.addTimetable(LESSON_ID_1, TEACHER_ID_1, DATE, Period.FIRST));
    }

    private Timetable insertTimetable(long lessonId, long teacherId, String stringDate, Period period) {
        teachersTimtableService.addTimetable(lessonId, teacherId, stringDate, period);
        return teachersTimtableService.getTimetableByLessonPersonTimePeriod(lessonId, teacherId, stringDate, period);
    }

    @Test
    void whenGetTimetableByDateAndTeacherIdShouldReturnListOfTimetables() {
        List<Timetable> expectedTimetables = new ArrayList<>();
        expectedTimetables.add(insertTimetable(LESSON_ID_1, TEACHER_ID_1, DATE_1, Period.FIRST));
        expectedTimetables.add(insertTimetable(LESSON_ID_2, TEACHER_ID_1, DATE_1, Period.SECOND));
        expectedTimetables.add(insertTimetable(LESSON_ID_3, TEACHER_ID_1, DATE_1, Period.THIRD));

        teachersTimtableService.addTimetable(LESSON_ID_3, TEACHER_ID_1, DATE_1_ADD_1_DAY, Period.SECOND);
        teachersTimtableService.addTimetable(LESSON_ID_3, TEACHER_ID_2, DATE_1_ADD_13_DAYS, Period.THIRD);
        teachersTimtableService.addTimetable(LESSON_ID_2, TEACHER_ID_1, DATE_1_ADD_3_DAYS, Period.THIRD);

        List<Timetable> actualTimetables = teachersTimtableService.getDayTimetable(DATE_1, TEACHER_ID_1);
        assertEquals(expectedTimetables, actualTimetables);
    }

    @Test
    void whenGetTimetableByIdShouldReturnCorrespondingTimteble() {
        Timetable expectedTimetable = insertTimetable(LESSON_ID_1, TEACHER_ID_1, DATE, Period.FIRST);
        assertEquals(expectedTimetable, teachersTimtableService.getTimetableById(expectedTimetable.getId()));
    }

    @Test
    void whenGetAllTimetablesShouldReturnListOfTimetables() {
        List<Timetable> expectedTimetables = new ArrayList<>();
        expectedTimetables.add(insertTimetable(LESSON_ID_1, TEACHER_ID_1, DATE_1, Period.FIRST));
        expectedTimetables.add(insertTimetable(LESSON_ID_2, TEACHER_ID_1, DATE_1_ADD_13_DAYS, Period.SECOND));
        expectedTimetables.add(insertTimetable(LESSON_ID_3, TEACHER_ID_1, DATE_1_ADD_21_DAYS, Period.THIRD));
        expectedTimetables.add(insertTimetable(LESSON_ID_1, TEACHER_ID_2, DATE_1_ADD_1_DAY, Period.FIRST));
        expectedTimetables.add(insertTimetable(LESSON_ID_2, TEACHER_ID_2, DATE_1_ADD_33_DAYS, Period.SECOND));
        expectedTimetables.add(insertTimetable(LESSON_ID_3, TEACHER_ID_2, DATE_1_ADD_33_DAYS, Period.THIRD));
        List<Timetable> actualTimetables = teachersTimtableService.getAllTimetables();
        assertIterableEquals(expectedTimetables, actualTimetables);
    }

    @Test
    void whenUpdateTimetableShouldReturnUpdatedTimetable() {
        Timetable timetable = insertTimetable(LESSON_ID_1, TEACHER_ID_1, DATE_1, Period.FIRST);
        long timetableId = timetable.getId();
        teachersTimtableService.updateTimetable(LESSON_ID_2, TEACHER_ID_2, DATE_1_ADD_1_DAY, Period.FIFTH, timetableId);
        Timetable expectedTimeTable = teachersTimtableService.getTimetableByLessonPersonTimePeriod(LESSON_ID_2,
                TEACHER_ID_2, DATE_1_ADD_1_DAY, Period.FIFTH);
        Timetable actualTimetable = teachersTimtableService.getTimetableById(timetableId);
        assertEquals(expectedTimeTable, actualTimetable);
    }

    @Test
    void whenDeleteTimetableByIdIfAfterGetTimetableByIdShouldThrowEmptyResultDataAccessException() {
        long timetableId = insertTimetable(LESSON_ID_1, TEACHER_ID_1, DATE, Period.FIRST).getId();
        teachersTimtableService.deleteTimetableById(timetableId);
        assertThrows(EntityNotFoundException.class, () -> teachersTimtableService.getTimetableById(timetableId));
    }

    @Test
    void whenGetTimtableForDayShouldReturnListOfTimetableOfSetDate() {
        List<Timetable> expectedTimetables = new ArrayList<>();
        expectedTimetables.add(insertTimetable(LESSON_ID_1, TEACHER_ID_1, DATE_1, Period.FIRST));
        expectedTimetables.add(insertTimetable(LESSON_ID_2, TEACHER_ID_1, DATE_1, Period.SECOND));
        expectedTimetables.add(insertTimetable(LESSON_ID_3, TEACHER_ID_1, DATE_1, Period.THIRD));

        teachersTimtableService.addTimetable(LESSON_ID_2, TEACHER_ID_2, DATE_1, Period.FIRST);
        teachersTimtableService.addTimetable(LESSON_ID_1, TEACHER_ID_1, DATE_1_ADD_1_DAY, Period.FIRST);
        teachersTimtableService.addTimetable(LESSON_ID_3, TEACHER_ID_2, DATE_1_ADD_21_DAYS, Period.SECOND);
        teachersTimtableService.addTimetable(LESSON_ID_1, TEACHER_ID_2, DATE_1_ADD_13_DAYS, Period.FIRST);
        teachersTimtableService.addTimetable(LESSON_ID_3, TEACHER_ID_2, DATE_1_ADD_33_DAYS, Period.SECOND);
        List<Timetable> actualTimetable = teachersTimtableService.getDayTimetable(DATE_1, TEACHER_ID_1);
        assertIterableEquals(expectedTimetables, actualTimetable);
    }

    @Test
    void whenGetTimtableForMonthShouldReturnListOfTimetablesFromSetDateWithinMonth() {
        List<Timetable> expectedTimetables = new ArrayList<>();
        expectedTimetables.add(insertTimetable(LESSON_ID_1, TEACHER_ID_1, DATE_1, Period.FIRST));
        expectedTimetables.add(insertTimetable(LESSON_ID_2, TEACHER_ID_1, DATE_1, Period.SECOND));
        expectedTimetables.add(insertTimetable(LESSON_ID_3, TEACHER_ID_1, DATE_1, Period.THIRD));

        teachersTimtableService.addTimetable(LESSON_ID_2, TEACHER_ID_2, DATE_1, Period.SECOND);
        teachersTimtableService.addTimetable(LESSON_ID_3, TEACHER_ID_2, DATE_1, Period.THIRD);

        expectedTimetables.add(insertTimetable(LESSON_ID_1, TEACHER_ID_1, DATE_1_ADD_1_DAY, Period.FIRST));
        expectedTimetables.add(insertTimetable(LESSON_ID_3, TEACHER_ID_1, DATE_1_ADD_1_DAY, Period.SECOND));

        expectedTimetables.add(insertTimetable(LESSON_ID_1, TEACHER_ID_1, DATE_1_ADD_3_DAYS, Period.FIRST));
        expectedTimetables.add(insertTimetable(LESSON_ID_2, TEACHER_ID_1, DATE_1_ADD_3_DAYS, Period.SECOND));

        expectedTimetables.add(insertTimetable(LESSON_ID_1, TEACHER_ID_1, DATE_1_ADD_13_DAYS, Period.FIRST));
        expectedTimetables.add(insertTimetable(LESSON_ID_2, TEACHER_ID_1, DATE_1_ADD_13_DAYS, Period.SECOND));

        teachersTimtableService.addTimetable(LESSON_ID_1, TEACHER_ID_2, DATE_1_ADD_13_DAYS, Period.FIRST);
        teachersTimtableService.addTimetable(LESSON_ID_2, TEACHER_ID_2, DATE_1_ADD_13_DAYS, Period.SECOND);

        expectedTimetables.add(insertTimetable(LESSON_ID_1, TEACHER_ID_1, DATE_1_ADD_21_DAYS, Period.FIRST));
        expectedTimetables.add(insertTimetable(LESSON_ID_2, TEACHER_ID_1, DATE_1_ADD_21_DAYS, Period.SECOND));

        teachersTimtableService.addTimetable(LESSON_ID_1, TEACHER_ID_1, DATE_1_ADD_33_DAYS, Period.FIRST);
        teachersTimtableService.addTimetable(LESSON_ID_2, TEACHER_ID_1, DATE_1_ADD_33_DAYS, Period.SECOND);
        List<Timetable> actualTimetables = teachersTimtableService.getMonthTimetable(DATE_1, TEACHER_ID_1);
        assertIterableEquals(expectedTimetables, actualTimetables);
    }

}
>>>>>>> refs/remotes/origin/master
