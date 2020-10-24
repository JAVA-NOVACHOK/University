package ua.com.nikiforov.services.lesson;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;

import ua.com.nikiforov.config.UniversityConfig;
import ua.com.nikiforov.dao.table_creator.TableCreator;
import ua.com.nikiforov.exceptions.DataOperationException;
import ua.com.nikiforov.exceptions.EntityNotFoundException;
import ua.com.nikiforov.models.Lesson;


@SpringJUnitConfig(UniversityConfig.class)
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
        assertTrue(lessonService.addLesson(TEST_GROUP_ID_1, TEST_SUBJECT_ID_1, TEST_ROOM_ID_1));
    }

    @Test
    void whenGetLessonByIdReturnCorrectLesson() {
        Lesson expectedlesson = insertLesson(TEST_GROUP_ID_1, TEST_SUBJECT_ID_1, TEST_ROOM_ID_1);
        assertEquals(expectedlesson, lessonService.getLessonById(expectedlesson.getId()));
    }

    @Test
    void whenGetAllLessonsIfPresentReturnListOfAllLessons() {
        List<Lesson> expectedLessons = new ArrayList<>();
        expectedLessons.add(insertLesson(TEST_GROUP_ID_1, TEST_SUBJECT_ID_1, TEST_ROOM_ID_1));
        expectedLessons.add(insertLesson(TEST_GROUP_ID_2, TEST_SUBJECT_ID_2, TEST_ROOM_ID_2));
        expectedLessons.add(insertLesson(TEST_GROUP_ID_3, TEST_SUBJECT_ID_3, TEST_ROOM_ID_3));
        List<Lesson> actualLessons = lessonService.getAllLessons();
        assertIterableEquals(expectedLessons, actualLessons);
    }

    @Test
    void whenUpdateLessonIfSuccessThenReturnTrue() {
        long lessonId = insertLesson(TEST_GROUP_ID_1, TEST_SUBJECT_ID_1, TEST_ROOM_ID_1).getId();
        assertTrue(lessonService.updateLesson(TEST_GROUP_ID_2, TEST_SUBJECT_ID_1, TEST_ROOM_ID_1, lessonId));
    }

    @Test
    void whenUpdateLessonIfSuccessThenGetLessonByIdAfterUpdateReturnChangedLesson() {
        long lessonId = insertLesson(TEST_GROUP_ID_1, TEST_SUBJECT_ID_1, TEST_ROOM_ID_1).getId();
        lessonService.updateLesson(TEST_GROUP_ID_2, TEST_SUBJECT_ID_2, TEST_ROOM_ID_2, lessonId);
        Lesson expectedUpdatedLesson = lessonService.getLessonByGroupRoomSubjectIds(TEST_GROUP_ID_2, TEST_ROOM_ID_2,
                TEST_SUBJECT_ID_2);
        Lesson actualUpdatedLesson = lessonService.getLessonById(lessonId);
        assertEquals(expectedUpdatedLesson, actualUpdatedLesson);
    }

    @Test
    void whenDeleteLessonByIdIfSuccessThenReturnTrue() {
        long lessonId = insertLesson(TEST_GROUP_ID_1, TEST_SUBJECT_ID_1, TEST_ROOM_ID_1).getId();
        assertTrue(lessonService.deleteLessonById(lessonId));
    }

    @Test
    void afterDeleteLessonByIdIfSearchForItReturnEntityNotFoundException() {
        long lessonId = insertLesson(TEST_GROUP_ID_1, TEST_SUBJECT_ID_1, TEST_ROOM_ID_1).getId();
        lessonService.deleteLessonById(lessonId);
        assertThrows(EntityNotFoundException.class, () -> lessonService.getLessonById(lessonId));
    }

    private Lesson insertLesson(long groupId, int roomId, int subjectId) {
        lessonService.addLesson(groupId, roomId, subjectId);
        return lessonService.getLessonByGroupRoomSubjectIds(groupId, roomId, subjectId);
    }

}
