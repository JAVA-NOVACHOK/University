package ua.com.nikiforov.services.lesson;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ua.com.nikiforov.dto.*;
import ua.com.nikiforov.exceptions.EntityNotFoundException;
import ua.com.nikiforov.services.group.GroupService;
import ua.com.nikiforov.services.persons.TeacherService;
import ua.com.nikiforov.services.room.RoomService;
import ua.com.nikiforov.services.subject.SubjectService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
class LessonServiceImplTest {

    private static final String TEST_GROUP_NAME_1 = "AA-12";
    private static final String TEST_GROUP_NAME_2 = "AA-13";
    private static final String TEST_GROUP_NAME_3 = "AA-14";

    private static final int TEST_ROOM_NUMBER_1 = 12;
    private static final int TEST_ROOM_NUMBER_2 = 13;
    private static final int TEST_ROOM_NUMBER_3 = 14;

    private static final int TEST_SEAT_NUMBER_1 = 20;

    private static final String STUDENTS_FIRST_NAME_1 = "Tom";
    private static final String STUDENTS_FIRST_NAME_2 = "Bill";
    private static final String STUDENTS_FIRST_NAME_3 = "Jack";
    private static final String STUDENTS_LAST_NAME_1 = "Hanks";
    private static final String STUDENTS_LAST_NAME_2 = "Clinton";
    private static final String STUDENTS_LAST_NAME_3 = "Sparrow";

    private static final String TEACHERS_FIRST_NAME_1 = "Bob";
    private static final String TEACHERS_FIRST_NAME_2 = "Mike";
    private static final String TEACHERS_FIRST_NAME_3 = "Will";
    private static final String TEACHERS_LAST_NAME_1 = "Harris";
    private static final String TEACHERS_LAST_NAME_2 = "Smith";
    private static final String TEACHERS_LAST_NAME_3 = "Spike";

    private static final String SUBJECT_NAME_1 = "Math";
    private static final String SUBJECT_NAME_2 = "Programming";
    private static final String SUBJECT_NAME_3 = "Cybersecurity";

    private static final int PERIOD_1 = 1;
    private static final int PERIOD_2 = 2;
    private static final int PERIOD_3 = 3;

    private static final String DATE_1_ADD_1_DAY = "2020-10-14";
    private static final String DATE_1_ADD_3_DAYS = "2020-10-16";
    private static final String DATE_1_ADD_13_DAYS = "2020-10-26";
    private static final String DATE_1_ADD_21_DAYS = "2020-11-03";
    private static final String DATE_1_ADD_33_DAYS = "2020-11-15";

    private static final long TEACHER_ID_1 = 1;

    private static final String DATE = "2020-10-15";

    @Autowired
    private RoomService roomService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private LessonService lessonService;

    private TeacherDTO teacher_1;
    private TeacherDTO teacher_2;
    private TeacherDTO teacher_3;

    private RoomDTO room_1;
    private RoomDTO room_2;
    private RoomDTO room_3;

    private GroupDTO group_1;
    private GroupDTO group_2;
    private GroupDTO group_3;

    private SubjectDTO subject_1;
    private SubjectDTO subject_2;
    private SubjectDTO subject_3;

    private LessonDTO lesson_1;
    private LessonDTO lesson_2;
    private LessonDTO lesson_3;
    private LessonDTO lesson_4;
    private LessonDTO lesson_5;
    private LessonDTO lesson_6;
    private LessonDTO lesson_7;
    private LessonDTO lesson_8;

    @BeforeAll
    void init() {
        room_1 = insertRoom(TEST_ROOM_NUMBER_1, TEST_SEAT_NUMBER_1);
        room_2 = insertRoom(TEST_ROOM_NUMBER_2, TEST_SEAT_NUMBER_1);
        room_3 = insertRoom(TEST_ROOM_NUMBER_3, TEST_SEAT_NUMBER_1);

        group_1 = insertGroup(TEST_GROUP_NAME_1);
        group_2 = insertGroup(TEST_GROUP_NAME_2);
        group_3 = insertGroup(TEST_GROUP_NAME_3);

        subject_1 = insertSubject(SUBJECT_NAME_1);
        subject_2 = insertSubject(SUBJECT_NAME_2);
        subject_3 = insertSubject(SUBJECT_NAME_3);

        teacher_1 = insertTeacher(TEACHERS_FIRST_NAME_1, TEACHERS_LAST_NAME_1);
        teacher_2 = insertTeacher(TEACHERS_FIRST_NAME_2, TEACHERS_LAST_NAME_2);
        teacher_3 = insertTeacher(TEACHERS_FIRST_NAME_3, TEACHERS_LAST_NAME_3);

        lesson_1 = insertLesson(PERIOD_1, subject_1.getId(), room_1.getId(), group_1.getGroupId(), DATE,
                teacher_1.getId());
        lesson_2 = insertLesson(PERIOD_2, subject_2.getId(), room_2.getId(), group_2.getGroupId(), DATE, teacher_1.getId());
        lesson_3 = insertLesson(PERIOD_3, subject_3.getId(), room_3.getId(), group_3.getGroupId(), DATE, teacher_1.getId());
        lesson_4 = insertLesson(PERIOD_1, subject_1.getId(), room_1.getId(), group_1.getGroupId(), DATE_1_ADD_3_DAYS,
                teacher_1.getId());

    }

    @Test
    @Order(1)
    void whenGetAllLessonsIfPresentReturnListOfAllLessons() {
        List<LessonDTO> expectedLessons = new ArrayList<>();
        expectedLessons.add(lesson_1);
        expectedLessons.add(lesson_2);
        expectedLessons.add(lesson_3);
        expectedLessons.add(lesson_4);
        List<LessonDTO> actualLessons = lessonService.getAllLessons();
        assertIterableEquals(expectedLessons, actualLessons);
    }

    @Test
    @Order(2)
    void whenAddLessonIfSuccessThenReturnTrue() {
        assertDoesNotThrow(() -> lessonService.addLesson(new LessonDTO(PERIOD_3, group_1.getGroupId(), subject_1.getId(), room_1.getId(), DATE_1_ADD_33_DAYS,
                teacher_1.getId())));
    }

    @Test
    @Order(3)
    void whenGetLessonByIdReturnCorrectLesson() {
        assertEquals(lesson_1, lessonService.getLessonById(lesson_1.getId()));
    }



    @Test
    @Order(4)
    void whenUpdateLessonIfSuccessThenReturnTrue() {
        assertDoesNotThrow(() -> lessonService.updateLesson(new LessonDTO(lesson_1.getId(), PERIOD_2, group_1.getGroupId(), subject_2.getId(), room_1.getId(), DATE_1_ADD_3_DAYS, teacher_1.getId())));
    }

    @Test
    @Order(5)
    void whenUpdateLessonThenLessonIsChanged() {
        LessonDTO updatedLesson = new LessonDTO(lesson_1.getId(), PERIOD_1, group_1.getGroupId(), subject_1.getId(), room_1.getId(), DATE, teacher_1.getId());
        lessonService.updateLesson(updatedLesson);
        LessonDTO actualUpdatedLesson = lessonService.getLessonById(lesson_1.getId());
        assertEquals(updatedLesson, actualUpdatedLesson);
    }

    @Test
    @Order(6)
    void whenDeleteLessonByIdIfSuccessThenReturnTrue() {
        assertDoesNotThrow(() -> lessonService.deleteLessonById(lesson_4.getId()));
    }

    @Test
    @Order(7)
    void afterDeleteLessonIfSearchReturnEntityNotFoundException() {
        lessonService.deleteLessonById(lesson_4.getId());
        assertThrows(EntityNotFoundException.class, () -> lessonService.getLessonById(lesson_4.getId()));
    }


    @Test
    @Order(8)
    void whenDeleteLessons_NoLessonInLessonsList() {
        lessonService.deleteLessonById(lesson_1.getId());

        List<LessonDTO> expectedLessons = new ArrayList<>();
        expectedLessons.add(lesson_2);
        expectedLessons.add(lesson_3);
        expectedLessons.add(lessonService.getLessonByAllArgs(new LessonDTO(PERIOD_3, group_1.getGroupId(), subject_1.getId(), room_1.getId(), DATE_1_ADD_33_DAYS,
                teacher_1.getId())));

        List<LessonDTO> actualLessons = lessonService.getAllLessons();
        assertIterableEquals(expectedLessons, actualLessons);
    }

    private LessonDTO insertLesson(int period, int subjectId, int roomId, long groupId, String date, long teacherId) {
        lessonService.addLesson(new LessonDTO(0, period, groupId, subjectId, roomId, date, teacherId));
        return lessonService.getLessonByAllArgs(new LessonDTO(period, groupId, subjectId, roomId, date, teacherId));
    }

    private GroupDTO insertGroup(String groupName) {
        groupService.addGroup(groupName);
        return groupService.getGroupByName(groupName);
    }

    private SubjectDTO insertSubject(String subjectName) {
        subjectService.addSubject(subjectName);
        return subjectService.getSubjectByName(subjectName);
    }

    private RoomDTO insertRoom(int roomNumber, int seatNumber) {
        roomService.addRoom(new RoomDTO(roomNumber, seatNumber));
        return roomService.getRoomByRoomNumber(roomNumber);
    }

    private TeacherDTO insertTeacher(String firstName, String lastName) {
        teacherService.addTeacher(new TeacherDTO(firstName, lastName));
        return teacherService.getTeacherByName(firstName, lastName);
    }

}
