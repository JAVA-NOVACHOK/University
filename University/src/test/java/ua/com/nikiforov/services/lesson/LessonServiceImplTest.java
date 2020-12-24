package ua.com.nikiforov.services.lesson;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;

import ua.com.nikiforov.dto.GroupDTO;
import ua.com.nikiforov.dto.LessonDTO;
import ua.com.nikiforov.dto.RoomDTO;
import ua.com.nikiforov.dto.SubjectDTO;
import ua.com.nikiforov.dto.TeacherDTO;
import ua.com.nikiforov.dao.table_creator.TableCreator;
import ua.com.nikiforov.datasource.TestDataSource;
import ua.com.nikiforov.exceptions.EntityNotFoundException;
import ua.com.nikiforov.services.group.GroupService;
import ua.com.nikiforov.services.persons.TeacherService;
import ua.com.nikiforov.services.room.RoomService;
import ua.com.nikiforov.services.subject.SubjectService;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(TestDataSource.class)
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@Transactional
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

    @Autowired
    private TableCreator tableCreator;

    private TeacherDTO teacher_1;
    private TeacherDTO teacher_2;
    private TeacherDTO teacher_3;

    RoomDTO room_1;
    RoomDTO room_2;
    RoomDTO room_3;

    GroupDTO group_1;
    GroupDTO group_2;
    GroupDTO group_3;

    SubjectDTO subject_1;
    SubjectDTO subject_2;
    SubjectDTO subject_3;

    @BeforeEach
    void init() {
        tableCreator.createTables();
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
    }

    @Test
    void whenAddLessonIfSuccessThenReturnTrue() {

        assertDoesNotThrow(() -> lessonService.addLesson(new LessonDTO(PERIOD_3, group_1.getGroupId(), subject_1.getId(), room_1.getId(), DATE,
                teacher_1.getId())));
    }

    @Test
    void whenGetLessonByIdReturnCorrectLesson() {
        LessonDTO expectedlesson = insertLesson(PERIOD_1, subject_1.getId(), room_1.getId(), group_1.getGroupId(), DATE,
                teacher_1.getId());
        assertEquals(expectedlesson, lessonService.getLessonById(expectedlesson.getId()));
    }

    @Test
    void whenGetAllLessonsIfPresentReturnListOfAllLessons() {
        List<LessonDTO> expectedLessons = new ArrayList<>();
        expectedLessons
                .add(insertLesson(PERIOD_1, subject_1.getId(), room_1.getId(), group_1.getGroupId(), DATE, teacher_1.getId()));
        expectedLessons
                .add(insertLesson(PERIOD_2, subject_2.getId(), room_2.getId(), group_2.getGroupId(), DATE, teacher_1.getId()));
        expectedLessons
                .add(insertLesson(PERIOD_3, subject_3.getId(), room_3.getId(), group_3.getGroupId(), DATE, teacher_1.getId()));
        List<LessonDTO> actualLessons = lessonService.getAllLessons();
        assertIterableEquals(expectedLessons, actualLessons);
    }

    @Test
    void whenUpdateLessonIfSuccessThenReturnTrue() {
        long lessonId = insertLesson(PERIOD_1, subject_1.getId(), room_1.getId(), group_1.getGroupId(), DATE, teacher_1.getId())
                .getId();
        assertDoesNotThrow(() -> lessonService.updateLesson(new LessonDTO(lessonId, PERIOD_2, group_2.getGroupId(), subject_2.getId(), room_2.getId(), DATE,
                teacher_1.getId())));
    }

    @Test
    void whenUpdateLessonThenLessonIsChanged() {
        long lessonId = insertLesson(PERIOD_1, subject_1.getId(), room_1.getId(), group_1.getGroupId(), DATE, teacher_1.getId())
                .getId();
        lessonService.updateLesson(new LessonDTO(lessonId, PERIOD_2, group_2.getGroupId(), subject_2.getId(), room_2.getId(), DATE, teacher_1.getId()));
        LessonDTO expectedUpdatedLesson = lessonService.getLessonByAllArgs(new LessonDTO(PERIOD_2, group_2.getGroupId(), subject_2.getId(), room_2.getId(), DATE, teacher_1.getId()));
        LessonDTO actualUpdatedLesson = lessonService.getLessonById(lessonId);
        assertEquals(expectedUpdatedLesson, actualUpdatedLesson);
    }

    @Test
    void whenDeleteLessonByIdIfSuccessThenReturnTrue() {
        long lessonId = insertLesson(PERIOD_1, subject_1.getId(), room_1.getId(), group_1.getGroupId(), DATE, teacher_1.getId())
                .getId();
        assertDoesNotThrow(() -> lessonService.deleteLessonById(lessonId));
    }

    @Test
    void afterDeleteLessonIfSearchReturnEntityNotFoundException() {
        long lessonId = insertLesson(PERIOD_1, subject_1.getId(), room_1.getId(), group_1.getGroupId(), DATE, teacher_1.getId())
                .getId();
        lessonService.deleteLessonById(lessonId);
        assertThrows(EntityNotFoundException.class, () -> lessonService.getLessonById(lessonId));
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
