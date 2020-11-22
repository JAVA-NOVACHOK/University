package ua.com.nikiforov.services.lesson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;

import ua.com.nikiforov.config.DatabaseConfig;
import ua.com.nikiforov.dao.table_creator.TableCreator;
import ua.com.nikiforov.exceptions.EntityNotFoundException;
import ua.com.nikiforov.models.lesson.Lesson;


@SpringJUnitConfig(DatabaseConfig.class)
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
class LessonServiceImplTest {

    private static final long TEST_GROUP_ID_1 = 1l;
    private static final long TEST_GROUP_ID_2 = 2l;
    private static final long TEST_GROUP_ID_3 = 3l;

    private static final int TEST_SUBJECT_ID_1 = 1;
    private static final int TEST_SUBJECT_ID_2 = 2;
    private static final int TEST_SUBJECT_ID_3 = 3;

    private static final int TEST_ROOM_ID_1 = 1;
    private static final int TEST_ROOM_ID_2 = 2;
    private static final int TEST_ROOM_ID_3 = 3;
    
    private static final int PERIOD_1 = 1;
    private static final int PERIOD_2 = 2;
    private static final int PERIOD_3 = 3;
    
    private static final long TEACHER_ID_1 = 1;
    
    private static final String DATE = "2020-10-15";

    @Autowired
    private LessonService lessonService;

    @Autowired
    private TableCreator tableCreator;

    @BeforeEach
    void init() {
        tableCreator.createTables();
    }

    @Test
    void whenAddLessonIfSuccessThenReturnTrue() {
        assertTrue(lessonService.addLesson(PERIOD_1, TEST_SUBJECT_ID_1, TEST_ROOM_ID_1,TEST_GROUP_ID_1,DATE,TEACHER_ID_1));
    }

    @Test
    void whenGetLessonByIdReturnCorrectLesson() {
        Lesson expectedlesson = insertLesson(PERIOD_1, TEST_SUBJECT_ID_1, TEST_ROOM_ID_1,TEST_GROUP_ID_1,DATE,TEACHER_ID_1);
        assertEquals(expectedlesson, lessonService.getLessonById(expectedlesson.getId()));
    }

    @Test
    void whenGetAllLessonsIfPresentReturnListOfAllLessons() {
        List<Lesson> expectedLessons = new ArrayList<>();
        expectedLessons.add(insertLesson(PERIOD_1, TEST_SUBJECT_ID_1, TEST_ROOM_ID_1,TEST_GROUP_ID_1,DATE,TEACHER_ID_1));
        expectedLessons.add(insertLesson(PERIOD_2, TEST_SUBJECT_ID_2, TEST_ROOM_ID_2,TEST_GROUP_ID_2,DATE,TEACHER_ID_1));
        expectedLessons.add(insertLesson(PERIOD_3, TEST_SUBJECT_ID_3, TEST_ROOM_ID_3,TEST_GROUP_ID_3,DATE,TEACHER_ID_1));
        List<Lesson> actualLessons = lessonService.getAllLessons();
        assertIterableEquals(expectedLessons, actualLessons);
    }

    @Test
    void whenUpdateLessonIfSuccessThenReturnTrue() {
        long lessonId = insertLesson(PERIOD_1, TEST_SUBJECT_ID_1, TEST_ROOM_ID_1,TEST_GROUP_ID_1,DATE,TEACHER_ID_1).getId();
        assertTrue(lessonService.updateLesson(PERIOD_2, TEST_SUBJECT_ID_2, TEST_ROOM_ID_2,TEST_GROUP_ID_2,DATE,TEACHER_ID_1, lessonId));
    }

    @Test
    void whenUpdateLessonThenLessonIsChanged() {
        long lessonId = insertLesson(PERIOD_1, TEST_SUBJECT_ID_1, TEST_ROOM_ID_1,TEST_GROUP_ID_1,DATE,TEACHER_ID_1).getId();
        lessonService.updateLesson(PERIOD_2, TEST_SUBJECT_ID_2, TEST_ROOM_ID_2,TEST_GROUP_ID_2,DATE,TEACHER_ID_1, lessonId);
        Lesson expectedUpdatedLesson = lessonService.getLessonByGroupRoomSubjectIds(TEST_SUBJECT_ID_2,TEST_ROOM_ID_2,TEST_GROUP_ID_2
                );
        Lesson actualUpdatedLesson = lessonService.getLessonById(lessonId);
        assertEquals(expectedUpdatedLesson, actualUpdatedLesson);
    }

    @Test
    void whenDeleteLessonByIdIfSuccessThenReturnTrue() {
        long lessonId = insertLesson(PERIOD_1, TEST_SUBJECT_ID_1, TEST_ROOM_ID_1,TEST_GROUP_ID_1,DATE,TEACHER_ID_1).getId();
        assertTrue(lessonService.deleteLessonById(lessonId));
    }

    @Test
    void afterDeleteLessonIfSearchReturnEntityNotFoundException() {
        long lessonId = insertLesson(PERIOD_1, TEST_SUBJECT_ID_1, TEST_ROOM_ID_1,TEST_GROUP_ID_1,DATE,TEACHER_ID_1).getId();
        lessonService.deleteLessonById(lessonId);
        assertThrows(EntityNotFoundException.class, () -> lessonService.getLessonById(lessonId));
    }

    private Lesson insertLesson(int period, int subjectId, int roomId, long groupId,String date, long teacherId) {
        lessonService.addLesson(period, subjectId, roomId, groupId, date, teacherId);
        return lessonService.getLessonByGroupRoomSubjectIds(subjectId, roomId, groupId);
    }

}
