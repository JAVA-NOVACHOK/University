package ua.com.nikiforov.rest_controllers;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ua.com.nikiforov.dto.*;
import ua.com.nikiforov.helper.SetupTestHelper;
import ua.com.nikiforov.services.timetables.PersonalTimetable;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ScheduleRestControllerTest extends SetupTestHelper {

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

    private static final String DATE_1 = "2021-09-15";
    private static final String DATE_1_ADD_1_DAY = "2021-09-16";
    private static final String DATE_1_ADD_3_DAYS = "2021-09-18";
    private static final String DATE_1_ADD_13_DAYS = "2021-09-28";
    private static final String DATE_1_ADD_21_DAYS = "2021-10-06";
    private static final String DATE_1_ADD_33_DAYS = "2021-11-18";
    private static final String DATE = "2021-09-13";


    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private TeacherDTO teacher;
    private TeacherDTO teacher_2;
    private TeacherDTO teacher_3;

    private StudentDTO student;


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

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        room_1 = insertRoom(TEST_ROOM_NUMBER_1, TEST_SEAT_NUMBER_1);
        room_2 = insertRoom(TEST_ROOM_NUMBER_2, TEST_SEAT_NUMBER_1);
        room_3 = insertRoom(TEST_ROOM_NUMBER_3, TEST_SEAT_NUMBER_1);

        group_1 = insertGroup(TEST_GROUP_NAME_1);
        group_2 = insertGroup(TEST_GROUP_NAME_2);
        group_3 = insertGroup(TEST_GROUP_NAME_3);

        subject_1 = insertSubject(SUBJECT_NAME_1);
        subject_2 = insertSubject(SUBJECT_NAME_2);
        subject_3 = insertSubject(SUBJECT_NAME_3);

        teacher = insertTeacher(TEACHERS_FIRST_NAME_1, TEACHERS_LAST_NAME_1);
        teacher_2 = insertTeacher(TEACHERS_FIRST_NAME_2, TEACHERS_LAST_NAME_2);
        teacher_3 = insertTeacher(TEACHERS_FIRST_NAME_3, TEACHERS_LAST_NAME_3);

        student = insertStudent(STUDENTS_FIRST_NAME_1, STUDENTS_LAST_NAME_1, group_1.getGroupId());
        insertStudent(STUDENTS_FIRST_NAME_2, STUDENTS_LAST_NAME_2, group_1.getGroupId());
        insertStudent(STUDENTS_FIRST_NAME_3, STUDENTS_LAST_NAME_3, group_1.getGroupId());

        group_1 = groupService.getGroupById(group_1.getGroupId());

        lesson_1 = insertLesson(PERIOD_1, subject_1.getId(), room_1.getId(), group_1.getGroupId(), DATE,
                teacher.getId());
        lesson_2 = insertLesson(PERIOD_2, subject_2.getId(), room_2.getId(), group_1.getGroupId(), DATE,
                teacher.getId());
        lesson_3 = insertLesson(PERIOD_3, subject_1.getId(), room_3.getId(), group_1.getGroupId(), DATE,
                teacher.getId());

        lesson_4 = insertLesson(PERIOD_1, subject_1.getId(), room_1.getId(), group_1.getGroupId(), DATE_1,
                teacher.getId());
        lesson_5 = insertLesson(PERIOD_2, subject_2.getId(), room_2.getId(), group_1.getGroupId(), DATE_1_ADD_3_DAYS,
                teacher.getId());
        lesson_6 = insertLesson(PERIOD_1, subject_3.getId(), room_3.getId(), group_1.getGroupId(), DATE_1_ADD_13_DAYS,
                teacher.getId());

        insertLesson(PERIOD_1, subject_3.getId(), room_3.getId(), group_1.getGroupId(), DATE_1_ADD_33_DAYS,
                teacher.getId());
    }

    @Test
    void whenGetTeacherScheduleForDay_Status200() throws Exception {
        this.mockMvc.perform(
                get("/api/schedule/teacher/{teacherId}/day/{date}",
                        teacher.getId(),DATE)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].timetables", hasSize(3)))
                .andExpect(jsonPath("$[0].timetables.[0].lessonId", is(lesson_1.getId()),Long.class))
                .andExpect(jsonPath("$[0].timetables.[0].period", is(lesson_1.getPeriod())))
                .andExpect(jsonPath("$[0].timetables.[0].subject.id", is(lesson_1.getSubjectId())))
                .andExpect(jsonPath("$[0].timetables.[0].room.id", is(lesson_1.getRoomId())))
                .andExpect(jsonPath("$[0].timetables.[0].teacher.id", is(lesson_1.getTeacherId()),Long.class))
                .andExpect(jsonPath("$[0].timetables.[0].group.groupId", is(lesson_1.getGroupId()),Long.class))
                .andExpect(jsonPath("$[0].timetables.[0].lessonDate", is(lesson_1.getDate())))

                .andExpect(jsonPath("$[0].timetables.[1].lessonId", is(lesson_2.getId()),Long.class))
                .andExpect(jsonPath("$[0].timetables.[1].period", is(lesson_2.getPeriod())))
                .andExpect(jsonPath("$[0].timetables.[1].subject.id", is(lesson_2.getSubjectId())))
                .andExpect(jsonPath("$[0].timetables.[1].room.id", is(lesson_2.getRoomId())))
                .andExpect(jsonPath("$[0].timetables.[1].teacher.id", is(lesson_2.getTeacherId()),Long.class))
                .andExpect(jsonPath("$[0].timetables.[1].group.groupId", is(lesson_2.getGroupId()),Long.class))
                .andExpect(jsonPath("$[0].timetables.[1].lessonDate", is(lesson_2.getDate())))

                .andExpect(jsonPath("$[0].timetables.[2].lessonId", is(lesson_3.getId()),Long.class))
                .andExpect(jsonPath("$[0].timetables.[2].period", is(lesson_3.getPeriod())))
                .andExpect(jsonPath("$[0].timetables.[2].subject.id", is(lesson_3.getSubjectId())))
                .andExpect(jsonPath("$[0].timetables.[2].room.id", is(lesson_3.getRoomId())))
                .andExpect(jsonPath("$[0].timetables.[2].teacher.id", is(lesson_3.getTeacherId()),Long.class))
                .andExpect(jsonPath("$[0].timetables.[2].group.groupId", is(lesson_3.getGroupId()),Long.class))
                .andExpect(jsonPath("$[0].timetables.[2].lessonDate", is(lesson_3.getDate())));

    }

    @Test
    void whenGetTeacherScheduleForMonth_Status200() throws Exception {
        this.mockMvc.perform(
                get("/api/schedule/teacher/{teacherId}/month/{date}",
                        teacher.getId(),DATE)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].timetables", hasSize(3)))
                .andExpect(jsonPath("$[0].timetables.[0].lessonId", is(lesson_1.getId()),Long.class))
                .andExpect(jsonPath("$[0].timetables.[0].period", is(lesson_1.getPeriod())))
                .andExpect(jsonPath("$[0].timetables.[0].subject.id", is(lesson_1.getSubjectId())))
                .andExpect(jsonPath("$[0].timetables.[0].room.id", is(lesson_1.getRoomId())))
                .andExpect(jsonPath("$[0].timetables.[0].teacher.id", is(lesson_1.getTeacherId()),Long.class))
                .andExpect(jsonPath("$[0].timetables.[0].group.groupId", is(lesson_1.getGroupId()),Long.class))
                .andExpect(jsonPath("$[0].timetables.[0].lessonDate", is(lesson_1.getDate())))

                .andExpect(jsonPath("$[0].timetables.[1].lessonId", is(lesson_2.getId()),Long.class))
                .andExpect(jsonPath("$[0].timetables.[1].period", is(lesson_2.getPeriod())))
                .andExpect(jsonPath("$[0].timetables.[1].subject.id", is(lesson_2.getSubjectId())))
                .andExpect(jsonPath("$[0].timetables.[1].room.id", is(lesson_2.getRoomId())))
                .andExpect(jsonPath("$[0].timetables.[1].teacher.id", is(lesson_2.getTeacherId()),Long.class))
                .andExpect(jsonPath("$[0].timetables.[1].group.groupId", is(lesson_2.getGroupId()),Long.class))
                .andExpect(jsonPath("$[0].timetables.[1].lessonDate", is(lesson_2.getDate())))

                .andExpect(jsonPath("$[0].timetables.[2].lessonId", is(lesson_3.getId()),Long.class))
                .andExpect(jsonPath("$[0].timetables.[2].period", is(lesson_3.getPeriod())))
                .andExpect(jsonPath("$[0].timetables.[2].subject.id", is(lesson_3.getSubjectId())))
                .andExpect(jsonPath("$[0].timetables.[2].room.id", is(lesson_3.getRoomId())))
                .andExpect(jsonPath("$[0].timetables.[2].teacher.id", is(lesson_3.getTeacherId()),Long.class))
                .andExpect(jsonPath("$[0].timetables.[2].group.groupId", is(lesson_3.getGroupId()),Long.class))
                .andExpect(jsonPath("$[0].timetables.[2].lessonDate", is(lesson_3.getDate())))

                .andExpect(jsonPath("$[1].timetables", hasSize(1)))
                .andExpect(jsonPath("$[1].timetables.[0].lessonId", is(lesson_4.getId()),Long.class))
                .andExpect(jsonPath("$[1].timetables.[0].period", is(lesson_4.getPeriod())))
                .andExpect(jsonPath("$[1].timetables.[0].subject.id", is(lesson_4.getSubjectId())))
                .andExpect(jsonPath("$[1].timetables.[0].room.id", is(lesson_4.getRoomId())))
                .andExpect(jsonPath("$[1].timetables.[0].teacher.id", is(lesson_4.getTeacherId()),Long.class))
                .andExpect(jsonPath("$[1].timetables.[0].group.groupId", is(lesson_4.getGroupId()),Long.class))
                .andExpect(jsonPath("$[1].timetables.[0].lessonDate", is(lesson_4.getDate())))

                .andExpect(jsonPath("$[2].timetables", hasSize(1)))
                .andExpect(jsonPath("$[2].timetables.[0].lessonId", is(lesson_5.getId()),Long.class))
                .andExpect(jsonPath("$[2].timetables.[0].period", is(lesson_5.getPeriod())))
                .andExpect(jsonPath("$[2].timetables.[0].subject.id", is(lesson_5.getSubjectId())))
                .andExpect(jsonPath("$[2].timetables.[0].room.id", is(lesson_5.getRoomId())))
                .andExpect(jsonPath("$[2].timetables.[0].teacher.id", is(lesson_5.getTeacherId()),Long.class))
                .andExpect(jsonPath("$[2].timetables.[0].group.groupId", is(lesson_5.getGroupId()),Long.class))
                .andExpect(jsonPath("$[2].timetables.[0].lessonDate", is(lesson_5.getDate())));

    }

    @Test
    void whenGetStudentScheduleForDay_Status200() throws Exception {
        this.mockMvc.perform(
                get("/api/schedule/student/{groupId}/day/{date}",
                        group_1.getGroupId(),DATE)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].timetables", hasSize(3)))
                .andExpect(jsonPath("$[0].timetables.[0].lessonId", is(lesson_1.getId()),Long.class))
                .andExpect(jsonPath("$[0].timetables.[0].period", is(lesson_1.getPeriod())))
                .andExpect(jsonPath("$[0].timetables.[0].subject.id", is(lesson_1.getSubjectId())))
                .andExpect(jsonPath("$[0].timetables.[0].room.id", is(lesson_1.getRoomId())))
                .andExpect(jsonPath("$[0].timetables.[0].teacher.id", is(lesson_1.getTeacherId()),Long.class))
                .andExpect(jsonPath("$[0].timetables.[0].group.groupId", is(lesson_1.getGroupId()),Long.class))
                .andExpect(jsonPath("$[0].timetables.[0].lessonDate", is(lesson_1.getDate())))

                .andExpect(jsonPath("$[0].timetables.[1].lessonId", is(lesson_2.getId()),Long.class))
                .andExpect(jsonPath("$[0].timetables.[1].period", is(lesson_2.getPeriod())))
                .andExpect(jsonPath("$[0].timetables.[1].subject.id", is(lesson_2.getSubjectId())))
                .andExpect(jsonPath("$[0].timetables.[1].room.id", is(lesson_2.getRoomId())))
                .andExpect(jsonPath("$[0].timetables.[1].teacher.id", is(lesson_2.getTeacherId()),Long.class))
                .andExpect(jsonPath("$[0].timetables.[1].group.groupId", is(lesson_2.getGroupId()),Long.class))
                .andExpect(jsonPath("$[0].timetables.[1].lessonDate", is(lesson_2.getDate())))

                .andExpect(jsonPath("$[0].timetables.[2].lessonId", is(lesson_3.getId()),Long.class))
                .andExpect(jsonPath("$[0].timetables.[2].period", is(lesson_3.getPeriod())))
                .andExpect(jsonPath("$[0].timetables.[2].subject.id", is(lesson_3.getSubjectId())))
                .andExpect(jsonPath("$[0].timetables.[2].room.id", is(lesson_3.getRoomId())))
                .andExpect(jsonPath("$[0].timetables.[2].teacher.id", is(lesson_3.getTeacherId()),Long.class))
                .andExpect(jsonPath("$[0].timetables.[2].group.groupId", is(lesson_3.getGroupId()),Long.class))
                .andExpect(jsonPath("$[0].timetables.[2].lessonDate", is(lesson_3.getDate())));

    }

    @Test
    void whenGetStudentScheduleForMonth_Status200() throws Exception {
        this.mockMvc.perform(
                get("/api/schedule/student/{groupId}/month/{date}",
                        group_1.getGroupId(),DATE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].timetables", hasSize(3)))
                .andExpect(jsonPath("$[0].timetables.[0].lessonId", is(lesson_1.getId()),Long.class))
                .andExpect(jsonPath("$[0].timetables.[0].period", is(lesson_1.getPeriod())))
                .andExpect(jsonPath("$[0].timetables.[0].subject.id", is(lesson_1.getSubjectId())))
                .andExpect(jsonPath("$[0].timetables.[0].room.id", is(lesson_1.getRoomId())))
                .andExpect(jsonPath("$[0].timetables.[0].teacher.id", is(lesson_1.getTeacherId()),Long.class))
                .andExpect(jsonPath("$[0].timetables.[0].group.groupId", is(lesson_1.getGroupId()),Long.class))
                .andExpect(jsonPath("$[0].timetables.[0].lessonDate", is(lesson_1.getDate())))

                .andExpect(jsonPath("$[0].timetables.[1].lessonId", is(lesson_2.getId()),Long.class))
                .andExpect(jsonPath("$[0].timetables.[1].period", is(lesson_2.getPeriod())))
                .andExpect(jsonPath("$[0].timetables.[1].subject.id", is(lesson_2.getSubjectId())))
                .andExpect(jsonPath("$[0].timetables.[1].room.id", is(lesson_2.getRoomId())))
                .andExpect(jsonPath("$[0].timetables.[1].teacher.id", is(lesson_2.getTeacherId()),Long.class))
                .andExpect(jsonPath("$[0].timetables.[1].group.groupId", is(lesson_2.getGroupId()),Long.class))
                .andExpect(jsonPath("$[0].timetables.[1].lessonDate", is(lesson_2.getDate())))

                .andExpect(jsonPath("$[0].timetables.[2].lessonId", is(lesson_3.getId()),Long.class))
                .andExpect(jsonPath("$[0].timetables.[2].period", is(lesson_3.getPeriod())))
                .andExpect(jsonPath("$[0].timetables.[2].subject.id", is(lesson_3.getSubjectId())))
                .andExpect(jsonPath("$[0].timetables.[2].room.id", is(lesson_3.getRoomId())))
                .andExpect(jsonPath("$[0].timetables.[2].teacher.id", is(lesson_3.getTeacherId()),Long.class))
                .andExpect(jsonPath("$[0].timetables.[2].group.groupId", is(lesson_3.getGroupId()),Long.class))
                .andExpect(jsonPath("$[0].timetables.[2].lessonDate", is(lesson_3.getDate())))

                .andExpect(jsonPath("$[1].timetables", hasSize(1)))
                .andExpect(jsonPath("$[1].timetables.[0].lessonId", is(lesson_4.getId()),Long.class))
                .andExpect(jsonPath("$[1].timetables.[0].period", is(lesson_4.getPeriod())))
                .andExpect(jsonPath("$[1].timetables.[0].subject.id", is(lesson_4.getSubjectId())))
                .andExpect(jsonPath("$[1].timetables.[0].room.id", is(lesson_4.getRoomId())))
                .andExpect(jsonPath("$[1].timetables.[0].teacher.id", is(lesson_4.getTeacherId()),Long.class))
                .andExpect(jsonPath("$[1].timetables.[0].group.groupId", is(lesson_4.getGroupId()),Long.class))
                .andExpect(jsonPath("$[1].timetables.[0].lessonDate", is(lesson_4.getDate())))

                .andExpect(jsonPath("$[2].timetables", hasSize(1)))
                .andExpect(jsonPath("$[2].timetables.[0].lessonId", is(lesson_5.getId()),Long.class))
                .andExpect(jsonPath("$[2].timetables.[0].period", is(lesson_5.getPeriod())))
                .andExpect(jsonPath("$[2].timetables.[0].subject.id", is(lesson_5.getSubjectId())))
                .andExpect(jsonPath("$[2].timetables.[0].room.id", is(lesson_5.getRoomId())))
                .andExpect(jsonPath("$[2].timetables.[0].teacher.id", is(lesson_5.getTeacherId()),Long.class))
                .andExpect(jsonPath("$[2].timetables.[0].group.groupId", is(lesson_5.getGroupId()),Long.class))
                .andExpect(jsonPath("$[2].timetables.[0].lessonDate", is(lesson_5.getDate())));

    }

}