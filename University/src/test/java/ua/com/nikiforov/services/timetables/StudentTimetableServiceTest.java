package ua.com.nikiforov.services.timetables;

import static org.junit.jupiter.api.Assertions.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.zone.ZoneRulesProvider;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import ua.com.nikiforov.config.UniversityConfig;
import ua.com.nikiforov.dao.tablecreator.TableCreator;
import ua.com.nikiforov.models.timetable.Timetable;

@SpringJUnitConfig(UniversityConfig.class)
class StudentTimetableServiceTest {

    private static final long LESSON_ID_1 = 1;
    private static final long LESSON_ID_2 = 2;
    private static final long LESSON_ID_3 = 3;
    private static final long STUDENT_ID_1 = 1;
    private static final long STUDENT_ID_2 = 2;

    private static final int TEST_LIST_SIZE_FOR_DAY_TIMETABLE = 3;
    private static final int TEST_LIST_SIZE_FOR_MONTH_TIMETABLE = 11;
    private static final int TEST_LIST_SIZE = 3;
    private static final int LIST_INDEX_ZERO = 0;

    private static final String DATE_1 = "2020-09-15";
    private static final String DATE_1_ADD_1_DAY = "2020-09-16";
    private static final String DATE_1_ADD_3_DAYS = "2020-09-18";
    private static final String DATE_1_ADD_13_DAYS = "2020-09-28";
    private static final String DATE_1_ADD_21_DAYS = "2020-10-06";
    private static final String DATE_1_ADD_33_DAYS = "2020-11-18";

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
        assertTrue(studentTimetableService.addTimetable(LESSON_ID_1, STUDENT_ID_1, DATE_1, Period.FIRST));
    }

    @Test
    void whenGetTimetableByDateAndStudentIdShouldReturnListOfTimetables() {
        studentTimetableService.addTimetable(LESSON_ID_1, STUDENT_ID_1, DATE_1, Period.FIRST);
        studentTimetableService.addTimetable(LESSON_ID_2, STUDENT_ID_1, DATE_1, Period.SECOND);
        studentTimetableService.addTimetable(LESSON_ID_3, STUDENT_ID_1, DATE_1, Period.THIRD);
        List<Timetable> timetable = studentTimetableService.getDayTimetable(DATE_1, STUDENT_ID_1);
        long studentId = timetable.get(LIST_INDEX_ZERO).getPersonId();
        assertEquals(STUDENT_ID_1, studentId);
        assertEquals(TEST_LIST_SIZE, timetable.size());
    }

    @Test
    void whenGetTimetableByIdShouldReturnCorrespondingTimteble() {
        long timetableId = insertTimtable(LESSON_ID_1, STUDENT_ID_1, DATE_1, Period.FIRST);
        Timetable timetable = studentTimetableService.getTimetableById(timetableId);
        StringBuilder expectedTimetable = new StringBuilder();
        expectedTimetable.append(LESSON_ID_1).append(STUDENT_ID_1).append(Period.FIRST.getPeriod());
        StringBuilder actualTimetable = new StringBuilder();
        actualTimetable.append(timetable.getLessonId()).append(timetable.getPersonId()).append(timetable.getPeriod());
        assertEquals(expectedTimetable.toString(), actualTimetable.toString());
    }

    @Test
    void whenGetAllTimetablesShouldReturnListOfTimetables() {
        studentTimetableService.addTimetable(LESSON_ID_1, STUDENT_ID_1, DATE_1, Period.FIRST);
        studentTimetableService.addTimetable(LESSON_ID_2, STUDENT_ID_1, DATE_1, Period.SECOND);
        studentTimetableService.addTimetable(LESSON_ID_3, STUDENT_ID_1, DATE_1, Period.THIRD);
        List<Timetable> allTimetables = studentTimetableService.getAllTimetables();
        StringBuilder expectedTimetable = new StringBuilder();
        expectedTimetable.append(LESSON_ID_1).append(STUDENT_ID_1).append(Period.FIRST.getPeriod()).append(LESSON_ID_2)
                .append(STUDENT_ID_1).append(Period.SECOND.getPeriod()).append(LESSON_ID_3).append(STUDENT_ID_1)
                .append(Period.THIRD.getPeriod());
        StringBuilder actualTimetable = new StringBuilder();
        long count = allTimetables.stream()
                .map(t -> actualTimetable.append(t.getLessonId()).append(t.getPersonId()).append(t.getPeriod()))
                .count();
        assertEquals(TEST_LIST_SIZE, count);
        assertEquals(expectedTimetable.toString(), actualTimetable.toString());
    }

    @Test
    void whenUpdateLessonIdInTimetableShouldReturnTimetableWithUpdatedLessonId() {
        long timetableId = insertTimtable(LESSON_ID_1, STUDENT_ID_1, DATE_1, Period.FIRST);
        studentTimetableService.updateTimetable(LESSON_ID_2, STUDENT_ID_1, DATE_1, Period.FIRST, timetableId);
        Timetable changedTimetable = studentTimetableService.getTimetableById(timetableId);
        assertEquals(LESSON_ID_2, changedTimetable.getLessonId());
    }

    @Test
    void whenUpdatesStudentIdInTimetableShouldReturnTimetableWithUpdatedStudentId() {
        long timetableId = insertTimtable(LESSON_ID_1, STUDENT_ID_1, DATE_1, Period.FIRST);
        studentTimetableService.updateTimetable(LESSON_ID_1, STUDENT_ID_2, DATE_1, Period.FIRST, timetableId);
        Timetable changedTimetable = studentTimetableService.getTimetableById(timetableId);
        assertEquals(STUDENT_ID_2, changedTimetable.getPersonId());
    }

    @Test
    void whenUpdatesPeriodInTimetableShouldReturnTimetableWithUpdatedPeriod() {
        long timetableId = insertTimtable(LESSON_ID_1, STUDENT_ID_1, DATE_1, Period.FIRST);
        studentTimetableService.updateTimetable(LESSON_ID_1, STUDENT_ID_1, DATE_1, Period.SECOND, timetableId);
        Timetable changedTimetable = studentTimetableService.getTimetableById(timetableId);
        assertEquals(Period.SECOND.getPeriod(), changedTimetable.getPeriod());
    }

    @Test
    void whenDeleteTimetableByIdIfAfterGetTimetableByIdShouldThrowEmptyResultDataAccessException() {
        long timetableId = insertTimtable(LESSON_ID_1, STUDENT_ID_1, DATE_1, Period.FIRST);
        studentTimetableService.deleteTimetableById(timetableId);
        assertThrows(EmptyResultDataAccessException.class, () -> studentTimetableService.getTimetableById(timetableId));
    }

    @Test
    void whenGetTimtableForDayShouldReturnListOfTimetableOfSetDate() {
        studentTimetableService.addTimetable(LESSON_ID_1, STUDENT_ID_1, DATE_1, Period.FIRST);
        studentTimetableService.addTimetable(LESSON_ID_2, STUDENT_ID_1, DATE_1, Period.SECOND);
        studentTimetableService.addTimetable(LESSON_ID_3, STUDENT_ID_1, DATE_1, Period.THIRD);

        studentTimetableService.addTimetable(LESSON_ID_1, STUDENT_ID_1, DATE_1_ADD_1_DAY, Period.FIRST);
        studentTimetableService.addTimetable(LESSON_ID_3, STUDENT_ID_1, DATE_1_ADD_1_DAY, Period.SECOND);
        assertEquals(TEST_LIST_SIZE_FOR_DAY_TIMETABLE,
                studentTimetableService.getDayTimetable(DATE_1, STUDENT_ID_1).size());
    }

    @Test
    void whenGetTimtableForMonthShouldReturnListOfTimetableFromSetDateTillMonth() {
        studentTimetableService.addTimetable(LESSON_ID_1, STUDENT_ID_1, DATE_1, Period.FIRST);
        studentTimetableService.addTimetable(LESSON_ID_2, STUDENT_ID_1, DATE_1, Period.SECOND);
        studentTimetableService.addTimetable(LESSON_ID_3, STUDENT_ID_1, DATE_1, Period.THIRD);

        studentTimetableService.addTimetable(LESSON_ID_1, STUDENT_ID_1, DATE_1_ADD_1_DAY, Period.FIRST);
        studentTimetableService.addTimetable(LESSON_ID_3, STUDENT_ID_1, DATE_1_ADD_1_DAY, Period.SECOND);

        studentTimetableService.addTimetable(LESSON_ID_1, STUDENT_ID_1, DATE_1_ADD_3_DAYS, Period.FIRST);
        studentTimetableService.addTimetable(LESSON_ID_2, STUDENT_ID_1, DATE_1_ADD_3_DAYS, Period.SECOND);

        studentTimetableService.addTimetable(LESSON_ID_1, STUDENT_ID_1, DATE_1_ADD_13_DAYS, Period.FIRST);
        studentTimetableService.addTimetable(LESSON_ID_2, STUDENT_ID_1, DATE_1_ADD_13_DAYS, Period.SECOND);

        studentTimetableService.addTimetable(LESSON_ID_1, STUDENT_ID_1, DATE_1_ADD_21_DAYS, Period.FIRST);
        studentTimetableService.addTimetable(LESSON_ID_2, STUDENT_ID_1, DATE_1_ADD_21_DAYS, Period.SECOND);

        studentTimetableService.addTimetable(LESSON_ID_1, STUDENT_ID_1, DATE_1_ADD_33_DAYS, Period.FIRST);
        studentTimetableService.addTimetable(LESSON_ID_2, STUDENT_ID_1, DATE_1_ADD_33_DAYS, Period.SECOND);
        assertEquals(TEST_LIST_SIZE_FOR_MONTH_TIMETABLE,
                studentTimetableService.getMonthTimetable(DATE_1, STUDENT_ID_1).size());
    }

    private long insertTimtable(long lessonId, long studentId, String date, Period period) {
        studentTimetableService.addTimetable(LESSON_ID_1, STUDENT_ID_1, DATE_1, Period.FIRST);
        List<Timetable> timetables = studentTimetableService.getDayTimetable(DATE_1, STUDENT_ID_1);
        return timetables.get(LIST_INDEX_ZERO).getId();
    }

}
