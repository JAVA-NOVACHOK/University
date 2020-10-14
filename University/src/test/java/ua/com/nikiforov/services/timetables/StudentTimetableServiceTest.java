package ua.com.nikiforov.services.timetables;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static final int TEST_LIST_SIZE = 3;
    private static final int TIMETABLE_LIST_INDEX = 0;

    private static final String DATE = "2020-10-15";

    @Autowired
    private StudentTimetableService studentTimtableService;

    @Autowired
    private TableCreator tableCreator;

    @BeforeEach
    void init() {
        tableCreator.createTables();
    }

    @Test
    void whenAddTimetableIfSuccessReturnTrue() {
        assertTrue(studentTimtableService.addTimetable(LESSON_ID_1, STUDENT_ID_1, DATE, Period.FIRST));
    }

    @Test
    void whenGetTimetableByDateAndStudentIdShouldReturnListOfTimetables() {
        studentTimtableService.addTimetable(LESSON_ID_1, STUDENT_ID_1, DATE, Period.FIRST);
        studentTimtableService.addTimetable(LESSON_ID_2, STUDENT_ID_1, DATE, Period.SECOND);
        studentTimtableService.addTimetable(LESSON_ID_3, STUDENT_ID_1, DATE, Period.THIRD);
        List<Timetable> timetable = studentTimtableService.getDayTimetable(DATE, STUDENT_ID_1);
        long studentId = timetable.get(TIMETABLE_LIST_INDEX).getPersonId();
        assertEquals(STUDENT_ID_1, studentId);
        assertEquals(TEST_LIST_SIZE, timetable.size());
    }

    @Test
    void whenGetTimetableByIdShouldReturnCorrespondingTimteble() {
        studentTimtableService.addTimetable(LESSON_ID_1, STUDENT_ID_1, DATE, Period.FIRST);
        List<Timetable> timetables = studentTimtableService.getDayTimetable(DATE, STUDENT_ID_1);
        long timetableId = timetables.get(TIMETABLE_LIST_INDEX).getId();
        Timetable timetable = studentTimtableService.getTimetableById(timetableId);
        StringBuilder expectedTimetable = new StringBuilder();
        expectedTimetable.append(LESSON_ID_1).append(STUDENT_ID_1).append(Period.FIRST.getPeriod());
        StringBuilder actualTimetable = new StringBuilder();
        actualTimetable.append(timetable.getLessonId()).append(timetable.getPersonId()).append(timetable.getPeriod());
        assertEquals(expectedTimetable.toString(), actualTimetable.toString());
    }

    @Test
    void whenGetAllTimetablesShouldReturnListOfTimetables() {
        studentTimtableService.addTimetable(LESSON_ID_1, STUDENT_ID_1, DATE, Period.FIRST);
        studentTimtableService.addTimetable(LESSON_ID_2, STUDENT_ID_1, DATE, Period.SECOND);
        studentTimtableService.addTimetable(LESSON_ID_3, STUDENT_ID_1, DATE, Period.THIRD);
        List<Timetable> allTimetables = studentTimtableService.getAllTimetables();
        StringBuilder expectedTimetable = new StringBuilder();
        expectedTimetable.append(LESSON_ID_1).append(STUDENT_ID_1).append(Period.FIRST.getPeriod()).append(LESSON_ID_2)
                .append(STUDENT_ID_1).append(Period.SECOND.getPeriod()).append(LESSON_ID_3).append(STUDENT_ID_1)
                .append(Period.THIRD.getPeriod());
        StringBuilder actualTimetable = new StringBuilder();
        long count = allTimetables.stream()
                .map(t -> actualTimetable.append(t.getLessonId()).append(t.getPersonId()).append(t.getPeriod()))
                .count();
        assertEquals(TEST_LIST_SIZE, count);
        assertEquals(expectedTimetable.toString(),actualTimetable.toString());
    }
    
    @Test
    void 

}
