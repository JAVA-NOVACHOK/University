package ua.com.nikiforov.services.timetables;

import static org.junit.jupiter.api.Assertions.*;

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
class TeachersTimetableServiceTest {

    private static final long LESSON_ID_1 = 1;
    private static final long LESSON_ID_2 = 2;
    private static final long LESSON_ID_3 = 3;
    private static final long TEACHER_ID_1 = 1;
    private static final long TEACHER_ID_2 = 2;

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

    @Test
    void whenGetTimetableByDateAndTeacherIdShouldReturnListOfTimetables() {
        teachersTimtableService.addTimetable(LESSON_ID_1, TEACHER_ID_1, DATE, Period.FIRST);
        teachersTimtableService.addTimetable(LESSON_ID_2, TEACHER_ID_1, DATE, Period.SECOND);
        teachersTimtableService.addTimetable(LESSON_ID_3, TEACHER_ID_1, DATE, Period.THIRD);
        List<Timetable> timetable = teachersTimtableService.getDayTimetable(DATE, TEACHER_ID_1);
        long teacherId = timetable.get(LIST_INDEX_ZERO).getPersonId();
        assertEquals(TEACHER_ID_1, teacherId);
        assertEquals(TEST_LIST_SIZE, timetable.size());
    }

    @Test
    void whenGetTimetableByIdShouldReturnCorrespondingTimteble() {
        long timetableId = insertTimtable(LESSON_ID_1, TEACHER_ID_1, DATE, Period.FIRST);
        Timetable timetable = teachersTimtableService.getTimetableById(timetableId);
        StringBuilder expectedTimetable = new StringBuilder();
        expectedTimetable.append(LESSON_ID_1).append(TEACHER_ID_1).append(Period.FIRST.getPeriod());
        StringBuilder actualTimetable = new StringBuilder();
        actualTimetable.append(timetable.getLessonId()).append(timetable.getPersonId()).append(timetable.getPeriod());
        assertEquals(expectedTimetable.toString(), actualTimetable.toString());
    }

    @Test
    void whenGetAllTimetablesShouldReturnListOfTimetables() {
        teachersTimtableService.addTimetable(LESSON_ID_1, TEACHER_ID_1, DATE, Period.FIRST);
        teachersTimtableService.addTimetable(LESSON_ID_2, TEACHER_ID_1, DATE, Period.SECOND);
        teachersTimtableService.addTimetable(LESSON_ID_3, TEACHER_ID_1, DATE, Period.THIRD);
        List<Timetable> allTimetables = teachersTimtableService.getAllTimetables();
        StringBuilder expectedTimetable = new StringBuilder();
        expectedTimetable.append(LESSON_ID_1).append(TEACHER_ID_1).append(Period.FIRST.getPeriod()).append(LESSON_ID_2)
                .append(TEACHER_ID_1).append(Period.SECOND.getPeriod()).append(LESSON_ID_3).append(TEACHER_ID_1)
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
        long timetableId = insertTimtable(LESSON_ID_1, TEACHER_ID_1, DATE, Period.FIRST);
        teachersTimtableService.updateTimetable(LESSON_ID_2, TEACHER_ID_1, DATE, Period.FIRST, timetableId);
        Timetable changedTimetable = teachersTimtableService.getTimetableById(timetableId);
        assertEquals(LESSON_ID_2, changedTimetable.getLessonId());
    }

    @Test
    void whenUpdatesTeacherIdInTimetableShouldReturnTimetableWithUpdatedTeacherId() {
        long timetableId = insertTimtable(LESSON_ID_1, TEACHER_ID_1, DATE, Period.FIRST);
        teachersTimtableService.updateTimetable(LESSON_ID_1, TEACHER_ID_2, DATE, Period.FIRST, timetableId);
        Timetable changedTimetable = teachersTimtableService.getTimetableById(timetableId);
        assertEquals(TEACHER_ID_2, changedTimetable.getPersonId());
    }

    @Test
    void whenUpdatesPeriodInTimetableShouldReturnTimetableWithUpdatedPeriod() {
        long timetableId = insertTimtable(LESSON_ID_1, TEACHER_ID_1, DATE, Period.FIRST);
        teachersTimtableService.updateTimetable(LESSON_ID_1, TEACHER_ID_1, DATE, Period.SECOND, timetableId);
        Timetable changedTimetable = teachersTimtableService.getTimetableById(timetableId);
        assertEquals(Period.SECOND.getPeriod(), changedTimetable.getPeriod());
    }

    @Test
    void whenDeleteTimetableByIdIfAfterGetTimetableByIdShouldThrowEmptyResultDataAccessException() {
        long timetableId = insertTimtable(LESSON_ID_1, TEACHER_ID_1, DATE, Period.FIRST);
        teachersTimtableService.deleteTimetableById(timetableId);
        assertThrows(EmptyResultDataAccessException.class, () -> teachersTimtableService.getTimetableById(timetableId));
    }

    private long insertTimtable(long lessonId, long teacherId, String date, Period period) {
        teachersTimtableService.addTimetable(LESSON_ID_1, TEACHER_ID_1, DATE, Period.FIRST);
        List<Timetable> timetables = teachersTimtableService.getDayTimetable(DATE, TEACHER_ID_1);
        return timetables.get(LIST_INDEX_ZERO).getId();
    }

    @Test
    void whenGetTimtableForDayShouldReturnListOfTimetableOfSetDate() {
        teachersTimtableService.addTimetable(LESSON_ID_1, TEACHER_ID_1, DATE_1, Period.FIRST);
        teachersTimtableService.addTimetable(LESSON_ID_2, TEACHER_ID_1, DATE_1, Period.SECOND);
        teachersTimtableService.addTimetable(LESSON_ID_3, TEACHER_ID_1, DATE_1, Period.THIRD);

        teachersTimtableService.addTimetable(LESSON_ID_1, TEACHER_ID_1, DATE_1_ADD_1_DAY, Period.FIRST);
        teachersTimtableService.addTimetable(LESSON_ID_3, TEACHER_ID_1, DATE_1_ADD_1_DAY, Period.SECOND);
        assertEquals(TEST_LIST_SIZE_FOR_DAY_TIMETABLE,
                teachersTimtableService.getDayTimetable(DATE_1, TEACHER_ID_1).size());
    }

    @Test
    void whenGetTimtableForMonthShouldReturnListOfTimetableFromSetDateTillMonth() {
        teachersTimtableService.addTimetable(LESSON_ID_1, TEACHER_ID_1, DATE_1, Period.FIRST);
        teachersTimtableService.addTimetable(LESSON_ID_2, TEACHER_ID_1, DATE_1, Period.SECOND);
        teachersTimtableService.addTimetable(LESSON_ID_3, TEACHER_ID_1, DATE_1, Period.THIRD);

        teachersTimtableService.addTimetable(LESSON_ID_1, TEACHER_ID_1, DATE_1_ADD_1_DAY, Period.FIRST);
        teachersTimtableService.addTimetable(LESSON_ID_3, TEACHER_ID_1, DATE_1_ADD_1_DAY, Period.SECOND);
        
        teachersTimtableService.addTimetable(LESSON_ID_1, TEACHER_ID_1, DATE_1_ADD_3_DAYS, Period.FIRST);
        teachersTimtableService.addTimetable(LESSON_ID_2, TEACHER_ID_1, DATE_1_ADD_3_DAYS, Period.SECOND);
        
        teachersTimtableService.addTimetable(LESSON_ID_1, TEACHER_ID_1, DATE_1_ADD_13_DAYS, Period.FIRST);
        teachersTimtableService.addTimetable(LESSON_ID_2, TEACHER_ID_1, DATE_1_ADD_13_DAYS, Period.SECOND);
        
        teachersTimtableService.addTimetable(LESSON_ID_1, TEACHER_ID_1, DATE_1_ADD_21_DAYS, Period.FIRST);
        teachersTimtableService.addTimetable(LESSON_ID_2, TEACHER_ID_1, DATE_1_ADD_21_DAYS, Period.SECOND);
        
        teachersTimtableService.addTimetable(LESSON_ID_1, TEACHER_ID_1, DATE_1_ADD_33_DAYS, Period.FIRST);
        teachersTimtableService.addTimetable(LESSON_ID_2, TEACHER_ID_1, DATE_1_ADD_33_DAYS, Period.SECOND);
        assertEquals(TEST_LIST_SIZE_FOR_MONTH_TIMETABLE,
                teachersTimtableService.getMonthTimetable(DATE_1, TEACHER_ID_1).size());
    }
    
}
