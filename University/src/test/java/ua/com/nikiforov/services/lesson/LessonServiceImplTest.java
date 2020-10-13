package ua.com.nikiforov.services.lesson;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import ua.com.nikiforov.config.UniversityConfig;
import ua.com.nikiforov.dao.tablecreator.TableCreator;
import ua.com.nikiforov.models.Lesson;

@SpringJUnitConfig(UniversityConfig.class)
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

    private static final int TEST_LESSON_COUNT = 3;

    private static final String SPACE = " ";

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
    void whenGetLessonByGroupSubjectRoomIdsReturnCorrectLesson() {
        Lesson lesson = insertLesson();
        assertEquals(TEST_GROUP_ID_1, lesson.getGroupId());
        assertEquals(TEST_SUBJECT_ID_1, lesson.getSubjectId());
        assertEquals(TEST_ROOM_ID_1, lesson.getRoomId());
    }

    @Test
    void whenGetLessonByIdReturnCorrectLesson() {
        long lessonId = insertLesson().getId();
        assertEquals(TEST_GROUP_ID_1, lessonService.getLessonById(lessonId).getGroupId());
        assertEquals(TEST_SUBJECT_ID_1, lessonService.getLessonById(lessonId).getSubjectId());
        assertEquals(TEST_ROOM_ID_1, lessonService.getLessonById(lessonId).getRoomId());
    }

    @Test
    void whenGetAllLessonsIfPresentReturnListOfAllLessons() {
        lessonService.addLesson(TEST_GROUP_ID_1, TEST_SUBJECT_ID_1, TEST_ROOM_ID_1);
        lessonService.addLesson(TEST_GROUP_ID_2, TEST_SUBJECT_ID_2, TEST_ROOM_ID_2);
        lessonService.addLesson(TEST_GROUP_ID_3, TEST_SUBJECT_ID_3, TEST_ROOM_ID_3);
        StringBuilder expected = new StringBuilder();
        expected.append(TEST_GROUP_ID_1).append(SPACE).append(TEST_SUBJECT_ID_1).append(SPACE).append(TEST_ROOM_ID_1)
                .append(SPACE);
        expected.append(TEST_GROUP_ID_2).append(SPACE).append(TEST_SUBJECT_ID_2).append(SPACE).append(TEST_ROOM_ID_2)
                .append(SPACE);
        expected.append(TEST_GROUP_ID_3).append(SPACE).append(TEST_SUBJECT_ID_3).append(SPACE).append(TEST_ROOM_ID_3)
                .append(SPACE);
        StringBuilder actual = new StringBuilder();
        List<Lesson> lessons = lessonService.getAllLessons();
        long count = lessons.stream().map(l -> actual.append(l.getGroupId()).append(SPACE).append(l.getSubjectId())
                .append(SPACE).append(l.getRoomId()).append(SPACE)).count();
        assertEquals(TEST_LESSON_COUNT, count);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    void whenUpdateLessonIfSuccessThenReturnTrue() {
        long lessonId = insertLesson().getId();
        assertTrue(lessonService.updateLesson(TEST_GROUP_ID_2, TEST_SUBJECT_ID_1, TEST_ROOM_ID_1, lessonId));
    }

    @Test
    void whenUpdateLessonIfSuccessThenGetLessonByIdAfterUpdateReturnChangedLesson() {
        long lessonId = insertLesson().getId();
        lessonService.updateLesson(TEST_GROUP_ID_2, TEST_SUBJECT_ID_1, TEST_ROOM_ID_1, lessonId);
        assertEquals(TEST_GROUP_ID_2, lessonService.getLessonById(lessonId).getGroupId());
    }

    @Test
    void whenDeleteLessonByIdIfSuccessThenReturnTrue() {
        long lessonId = insertLesson().getId();
        assertTrue(lessonService.deleteLessonById(lessonId));
    }

    @Test
    void afterDeleteLessonByIdIfSearchForItReturnEmptyResultDataAccessException() {
        long lessonId = insertLesson().getId();
        lessonService.deleteLessonById(lessonId);
        assertThrows(EmptyResultDataAccessException.class, () -> lessonService.getLessonById(lessonId));
    }

    private Lesson insertLesson() {
        lessonService.addLesson(TEST_GROUP_ID_1, TEST_SUBJECT_ID_1, TEST_ROOM_ID_1);
        return lessonService.getLessonByGroupRoomSubjectIds(TEST_GROUP_ID_1, TEST_SUBJECT_ID_1, TEST_ROOM_ID_1);
    }

}
