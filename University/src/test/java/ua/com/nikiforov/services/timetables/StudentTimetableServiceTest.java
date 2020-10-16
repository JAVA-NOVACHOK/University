package ua.com.nikiforov.services.timetables;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
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
import ua.com.nikiforov.dao.table_creator.TableCreator;
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
    private static final int TEST_LIST_SIZE_FOR_GET_ALL = 6;
    private static final int LIST_INDEX_ZERO = 0;

    private static final String LOCAL_ZONE_ID = "Europe/Simferopol";
    private static final String DATE_1 = "2020-09-15";
    private static final String DATE_1_ADD_1_DAY = "2020-09-16";
    private static final String DATE_1_ADD_3_DAYS = "2020-09-18";
    private static final String DATE_1_ADD_13_DAYS = "2020-09-28";
    private static final String DATE_1_ADD_21_DAYS = "2020-10-06";
    private static final String DATE_1_ADD_33_DAYS = "2020-11-18";
    private static final String DATE_FOR_INSTANT = "2020-10-15T00:00:00Z";
    private static final String INSTANT_PARSED_DATE = "2020-10-15";

    private static final String DASH = "-";

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
    void whenGetStringDateFromInstantShouldParseToPattern_yyyy_MM_dd() {
        Instant instant = Instant.parse(DATE_FOR_INSTANT);
        String parsedInstant = getStringDateFromInstant(instant, LOCAL_ZONE_ID);
        assertEquals(INSTANT_PARSED_DATE, parsedInstant);
    }

    @Test
    void whenGetTimetableByDateAndStudentIdShouldReturnListOfTimetables() {
        studentTimetableService.addTimetable(LESSON_ID_1, STUDENT_ID_1, DATE_1, Period.FIRST);
        studentTimetableService.addTimetable(LESSON_ID_2, STUDENT_ID_1, DATE_1, Period.SECOND);
        studentTimetableService.addTimetable(LESSON_ID_3, STUDENT_ID_1, DATE_1, Period.THIRD);
        studentTimetableService.addTimetable(LESSON_ID_3, STUDENT_ID_1, DATE_1_ADD_1_DAY, Period.SECOND);
        studentTimetableService.addTimetable(LESSON_ID_3, STUDENT_ID_2, DATE_1_ADD_13_DAYS, Period.THIRD);
        studentTimetableService.addTimetable(LESSON_ID_2, STUDENT_ID_1, DATE_1_ADD_3_DAYS, Period.THIRD);
        StringBuilder expectedTimetable = new StringBuilder();
        expectedTimetable.append(LESSON_ID_1).append(STUDENT_ID_1).append(DATE_1).append(Period.FIRST.getPeriod())
                .append(LESSON_ID_2).append(STUDENT_ID_1).append(DATE_1).append(Period.SECOND.getPeriod())
                .append(LESSON_ID_3).append(STUDENT_ID_1).append(DATE_1).append(Period.THIRD.getPeriod());
        List<Timetable> timetable = studentTimetableService.getDayTimetable(DATE_1, STUDENT_ID_1);
        StringBuilder actualTimetable = new StringBuilder();
        long count = timetable.stream().map(t -> actualTimetable.append(t.getLessonId()).append(t.getPersonId())
                .append(getStringDateFromInstant(t.getTime(), LOCAL_ZONE_ID)).append(t.getPeriod())).count();
        assertEquals(TEST_LIST_SIZE, count);
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
        studentTimetableService.addTimetable(LESSON_ID_1, STUDENT_ID_2, DATE_1, Period.FIRST);
        studentTimetableService.addTimetable(LESSON_ID_2, STUDENT_ID_2, DATE_1, Period.SECOND);
        studentTimetableService.addTimetable(LESSON_ID_3, STUDENT_ID_2, DATE_1, Period.THIRD);
        List<Timetable> allTimetables = studentTimetableService.getAllTimetables();
        StringBuilder expectedTimetable = new StringBuilder();
        expectedTimetable.append(LESSON_ID_1).append(STUDENT_ID_1).append(DATE_1).append(Period.FIRST.getPeriod())
                .append(LESSON_ID_2).append(STUDENT_ID_1).append(DATE_1).append(Period.SECOND.getPeriod())
                .append(LESSON_ID_3).append(STUDENT_ID_1).append(DATE_1).append(Period.THIRD.getPeriod())
                .append(LESSON_ID_1).append(STUDENT_ID_2).append(DATE_1).append(Period.FIRST.getPeriod())
                .append(LESSON_ID_2).append(STUDENT_ID_2).append(DATE_1).append(Period.SECOND.getPeriod())
                .append(LESSON_ID_3).append(STUDENT_ID_2).append(DATE_1).append(Period.THIRD.getPeriod());
        StringBuilder actualTimetable = new StringBuilder();
        long count = allTimetables.stream().map(t -> actualTimetable.append(t.getLessonId()).append(t.getPersonId())
                .append(getStringDateFromInstant(t.getTime(), LOCAL_ZONE_ID)).append(t.getPeriod())).count();
        assertEquals(TEST_LIST_SIZE_FOR_GET_ALL, count);
        assertEquals(expectedTimetable.toString(), actualTimetable.toString());
    }

    @Test
    void whenUpdateLessonIdInTimetableShouldReturnTimetableWithUpdatedLessonId() {
        long timetableId = insertTimtable(LESSON_ID_1, STUDENT_ID_1, DATE_1, Period.FIRST);
        studentTimetableService.updateTimetable(LESSON_ID_2, STUDENT_ID_1, DATE_1, Period.FIRST, timetableId);
        StringBuilder expectedTimetable = new StringBuilder();
        StringBuilder actualTimetable = new StringBuilder();
        Timetable updatedTimetable = studentTimetableService.getTimetableById(timetableId);
        expectedTimetable.append(LESSON_ID_2).append(STUDENT_ID_1).append(DATE_1).append(Period.FIRST.getPeriod());
        actualTimetable.append(updatedTimetable.getLessonId()).append(updatedTimetable.getPersonId())
                .append(getStringDateFromInstant(updatedTimetable.getTime(), LOCAL_ZONE_ID))
                .append(updatedTimetable.getPeriod());
        assertEquals(expectedTimetable.toString(), actualTimetable.toString());
    }

    @Test
    void whenUpdatesStudentIdInTimetableShouldReturnTimetableWithUpdatedStudentId() {
        long timetableId = insertTimtable(LESSON_ID_1, STUDENT_ID_1, DATE_1, Period.FIRST);
        studentTimetableService.updateTimetable(LESSON_ID_1, STUDENT_ID_2, DATE_1, Period.FIRST, timetableId);
        StringBuilder expectedTimetable = new StringBuilder();
        StringBuilder actualTimetable = new StringBuilder();
        Timetable updatedTimetable = studentTimetableService.getTimetableById(timetableId);
        expectedTimetable.append(LESSON_ID_1).append(STUDENT_ID_2).append(DATE_1).append(Period.FIRST.getPeriod());
        actualTimetable.append(updatedTimetable.getLessonId()).append(updatedTimetable.getPersonId())
                .append(getStringDateFromInstant(updatedTimetable.getTime(), LOCAL_ZONE_ID))
                .append(updatedTimetable.getPeriod());
        assertEquals(expectedTimetable.toString(), actualTimetable.toString());
    }
    
    @Test
    void whenUpdatesDateInTimetableShouldReturnTimetableWithUpdatedDate() {
        long timetableId = insertTimtable(LESSON_ID_1, STUDENT_ID_1, DATE_1, Period.FIRST);
        studentTimetableService.updateTimetable(LESSON_ID_1, STUDENT_ID_1, DATE_1_ADD_1_DAY, Period.FIRST, timetableId);
        StringBuilder expectedTimetable = new StringBuilder();
        StringBuilder actualTimetable = new StringBuilder();
        Timetable updatedTimetable = studentTimetableService.getTimetableById(timetableId);
        expectedTimetable.append(LESSON_ID_1).append(STUDENT_ID_1).append(DATE_1_ADD_1_DAY).append(Period.FIRST.getPeriod());
        actualTimetable.append(updatedTimetable.getLessonId()).append(updatedTimetable.getPersonId())
                .append(getStringDateFromInstant(updatedTimetable.getTime(), LOCAL_ZONE_ID))
                .append(updatedTimetable.getPeriod());
        assertEquals(expectedTimetable.toString(), actualTimetable.toString());
    }
    
    @Test
    void whenUpdatesAllDataInTimetableShouldReturnTimetableWithUpdatedAllData() {
        long timetableId = insertTimtable(LESSON_ID_1, STUDENT_ID_1, DATE_1, Period.FIRST);
        studentTimetableService.updateTimetable(LESSON_ID_2, STUDENT_ID_2, DATE_1_ADD_1_DAY, Period.SECOND, timetableId);
        StringBuilder expectedTimetable = new StringBuilder();
        StringBuilder actualTimetable = new StringBuilder();
        Timetable updatedTimetable = studentTimetableService.getTimetableById(timetableId);
        expectedTimetable.append(LESSON_ID_2).append(STUDENT_ID_2).append(DATE_1_ADD_1_DAY).append(Period.SECOND.getPeriod());
        actualTimetable.append(updatedTimetable.getLessonId()).append(updatedTimetable.getPersonId())
                .append(getStringDateFromInstant(updatedTimetable.getTime(), LOCAL_ZONE_ID))
                .append(updatedTimetable.getPeriod());
        assertEquals(expectedTimetable.toString(), actualTimetable.toString());
    }

    @Test
    void whenUpdatesPeriodInTimetableShouldReturnTimetableWithUpdatedPeriod() {
        long timetableId = insertTimtable(LESSON_ID_1, STUDENT_ID_1, DATE_1, Period.FIRST);
        studentTimetableService.updateTimetable(LESSON_ID_1, STUDENT_ID_1, DATE_1, Period.SECOND, timetableId);
        StringBuilder expectedTimetable = new StringBuilder();
        StringBuilder actualTimetable = new StringBuilder();
        Timetable updatedTimetable = studentTimetableService.getTimetableById(timetableId);
        expectedTimetable.append(LESSON_ID_1).append(STUDENT_ID_1).append(DATE_1).append(Period.SECOND.getPeriod());
        actualTimetable.append(updatedTimetable.getLessonId()).append(updatedTimetable.getPersonId())
                .append(getStringDateFromInstant(updatedTimetable.getTime(), LOCAL_ZONE_ID))
                .append(updatedTimetable.getPeriod());
        assertEquals(expectedTimetable.toString(), actualTimetable.toString());
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
        studentTimetableService.addTimetable(LESSON_ID_3, STUDENT_ID_2, DATE_1_ADD_1_DAY, Period.SECOND)
        ;
        studentTimetableService.addTimetable(LESSON_ID_1, STUDENT_ID_2, DATE_1_ADD_1_DAY, Period.FIRST);
        studentTimetableService.addTimetable(LESSON_ID_3, STUDENT_ID_2, DATE_1_ADD_1_DAY, Period.SECOND);
        List<Timetable> dayTimetable = studentTimetableService.getDayTimetable(DATE_1, STUDENT_ID_1);
        StringBuilder expectedTimetable = new StringBuilder();
        expectedTimetable.append(STUDENT_ID_1).append(DATE_1).append(STUDENT_ID_1).append(DATE_1).append(STUDENT_ID_1)
                .append(DATE_1);
        StringBuilder actualTimetable = new StringBuilder();
        long count = dayTimetable.stream().map(t -> actualTimetable.append(t.getPersonId())
                .append(getStringDateFromInstant(t.getTime(), LOCAL_ZONE_ID))).count();
        assertEquals(expectedTimetable.toString(), actualTimetable.toString());
        assertEquals(TEST_LIST_SIZE_FOR_DAY_TIMETABLE, count);
    }

    @Test
    void whenGetTimtableForMonthShouldReturnListOfTimetableFromSetDateWithinMonth() {
        studentTimetableService.addTimetable(LESSON_ID_1, STUDENT_ID_1, DATE_1, Period.FIRST);
        studentTimetableService.addTimetable(LESSON_ID_2, STUDENT_ID_1, DATE_1, Period.SECOND);
        studentTimetableService.addTimetable(LESSON_ID_3, STUDENT_ID_1, DATE_1, Period.THIRD);

        studentTimetableService.addTimetable(LESSON_ID_2, STUDENT_ID_2, DATE_1, Period.SECOND);
        studentTimetableService.addTimetable(LESSON_ID_3, STUDENT_ID_2, DATE_1, Period.THIRD);

        studentTimetableService.addTimetable(LESSON_ID_1, STUDENT_ID_1, DATE_1_ADD_1_DAY, Period.FIRST);
        studentTimetableService.addTimetable(LESSON_ID_3, STUDENT_ID_1, DATE_1_ADD_1_DAY, Period.SECOND);

        studentTimetableService.addTimetable(LESSON_ID_1, STUDENT_ID_1, DATE_1_ADD_3_DAYS, Period.FIRST);
        studentTimetableService.addTimetable(LESSON_ID_2, STUDENT_ID_1, DATE_1_ADD_3_DAYS, Period.SECOND);

        studentTimetableService.addTimetable(LESSON_ID_1, STUDENT_ID_1, DATE_1_ADD_13_DAYS, Period.FIRST);
        studentTimetableService.addTimetable(LESSON_ID_2, STUDENT_ID_1, DATE_1_ADD_13_DAYS, Period.SECOND);

        studentTimetableService.addTimetable(LESSON_ID_1, STUDENT_ID_2, DATE_1_ADD_13_DAYS, Period.FIRST);
        studentTimetableService.addTimetable(LESSON_ID_2, STUDENT_ID_2, DATE_1_ADD_13_DAYS, Period.SECOND);

        studentTimetableService.addTimetable(LESSON_ID_1, STUDENT_ID_1, DATE_1_ADD_21_DAYS, Period.FIRST);
        studentTimetableService.addTimetable(LESSON_ID_2, STUDENT_ID_1, DATE_1_ADD_21_DAYS, Period.SECOND);

        studentTimetableService.addTimetable(LESSON_ID_1, STUDENT_ID_1, DATE_1_ADD_33_DAYS, Period.FIRST);
        studentTimetableService.addTimetable(LESSON_ID_2, STUDENT_ID_1, DATE_1_ADD_33_DAYS, Period.SECOND);
        List<Timetable> allTimetables = studentTimetableService.getMonthTimetable(DATE_1, STUDENT_ID_1);
        StringBuilder expectedTimetable = new StringBuilder();
        expectedTimetable.append(STUDENT_ID_1).append(DATE_1).append(STUDENT_ID_1).append(DATE_1).append(STUDENT_ID_1)
                .append(DATE_1).append(STUDENT_ID_1).append(DATE_1_ADD_1_DAY).append(STUDENT_ID_1)
                .append(DATE_1_ADD_1_DAY).append(STUDENT_ID_1).append(DATE_1_ADD_3_DAYS).append(STUDENT_ID_1)
                .append(DATE_1_ADD_3_DAYS).append(STUDENT_ID_1).append(DATE_1_ADD_13_DAYS).append(STUDENT_ID_1)
                .append(DATE_1_ADD_13_DAYS).append(STUDENT_ID_1).append(DATE_1_ADD_21_DAYS).append(STUDENT_ID_1)
                .append(DATE_1_ADD_21_DAYS);
        StringBuilder actualTimetable = new StringBuilder();
        long count = allTimetables.stream().map(t -> actualTimetable.append(t.getPersonId())
                .append(getStringDateFromInstant(t.getTime(), LOCAL_ZONE_ID))).count();
        assertEquals(expectedTimetable.toString(), actualTimetable.toString());
        assertEquals(TEST_LIST_SIZE_FOR_MONTH_TIMETABLE, count);
    }

    private long insertTimtable(long lessonId, long studentId, String date, Period period) {
        studentTimetableService.addTimetable(LESSON_ID_1, STUDENT_ID_1, DATE_1, Period.FIRST);
        List<Timetable> timetables = studentTimetableService.getDayTimetable(DATE_1, STUDENT_ID_1);
        return timetables.get(LIST_INDEX_ZERO).getId();
    }

    private String getStringDateFromInstant(Instant time, String zoneIdText) {
        ZoneId zoneId = ZoneId.of(zoneIdText);
        ZonedDateTime zonedDateTime = time.atZone(zoneId);
        int year = zonedDateTime.getYear();
        int monthValue = zonedDateTime.getMonthValue();
        int dayValue = zonedDateTime.getDayOfMonth();
        String month = zeroBeforeOneGigitNumber(monthValue);
        String day = zeroBeforeOneGigitNumber(dayValue);
        return String.format("%d%s%s%s%s", year, DASH, month, DASH, day);
    }

    private String zeroBeforeOneGigitNumber(int number) {
        return (number > 9) ? number + "" : "0" + number;
    }

}
