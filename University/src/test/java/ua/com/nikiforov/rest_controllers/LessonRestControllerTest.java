package ua.com.nikiforov.rest_controllers;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ua.com.nikiforov.dto.*;
import ua.com.nikiforov.helper.SetupTestHelper;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class LessonRestControllerTest extends SetupTestHelper {

    private static final String TEST_GROUP_NAME_1 = "AA-12";
    private static final String TEST_GROUP_NAME_2 = "AA-13";
    private static final String TEST_GROUP_NAME_3 = "AA-14";

    private static final int TEST_ROOM_NUMBER_1 = 12;
    private static final int TEST_ROOM_NUMBER_2 = 13;
    private static final int TEST_ROOM_NUMBER_3 = 14;

    private static final int TEST_SEAT_NUMBER_1 = 20;
    private static final int TEST_SEAT_NUMBER_2 = 25;
    private static final int TEST_SEAT_NUMBER_3 = 30;

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
    private static final String DATE = "2021-10-13";


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


        lesson_1 = insertLesson(PERIOD_1, subject_1.getId(), room_1.getId(), group_1.getGroupId(), DATE,
                teacher.getId());
        lesson_2 = insertLesson(PERIOD_2, subject_2.getId(), room_2.getId(), group_2.getGroupId(), DATE,
                teacher.getId());
        lesson_3 = insertLesson(PERIOD_3, subject_1.getId(), room_3.getId(), group_2.getGroupId(), DATE,
                teacher.getId());

        lesson_4 = insertLesson(PERIOD_1, subject_1.getId(), room_1.getId(), group_3.getGroupId(), DATE_1,
                teacher.getId());
        lesson_5 = insertLesson(PERIOD_2, subject_2.getId(), room_2.getId(), group_3.getGroupId(), DATE_1_ADD_3_DAYS,
                teacher.getId());
        lesson_6 = insertLesson(PERIOD_1, subject_3.getId(), room_3.getId(), group_3.getGroupId(), DATE_1_ADD_13_DAYS,
                teacher.getId());

    }

    @Test
    void whenGetAllLessons_Status200_LessonList() throws Exception {
        this.mockMvc.perform(get("/teacher/{teacherId}/day/{date}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_ROOT, hasSize(6)))
                .andExpect(jsonPath("$[0].id", is(lesson_1.getId()), Long.class))
                .andExpect(jsonPath("$[0].teacherId", is(lesson_1.getTeacherId()), Long.class))
                .andExpect(jsonPath("$[0].subjectId", is(lesson_1.getSubjectId())))
                .andExpect(jsonPath("$[0].groupId", is(lesson_1.getGroupId()), Long.class))
                .andExpect(jsonPath("$[0].roomId", is(lesson_1.getRoomId())))
                .andExpect(jsonPath("$[0].date", is(lesson_1.getDate())))
                .andExpect(jsonPath("$[0].time", is(lesson_1.getTime().toString())))
//
                .andExpect(jsonPath("$[1].id", is(lesson_2.getId()), Long.class))
                .andExpect(jsonPath("$[1].teacherId", is(lesson_2.getTeacherId()), Long.class))
                .andExpect(jsonPath("$[1].subjectId", is(lesson_2.getSubjectId())))
                .andExpect(jsonPath("$[1].groupId", is(lesson_2.getGroupId()), Long.class))
                .andExpect(jsonPath("$[1].roomId", is(lesson_2.getRoomId())))
                .andExpect(jsonPath("$[1].date", is(lesson_2.getDate())))
//
                .andExpect(jsonPath("$[2].id", is(lesson_3.getId()), Long.class))
                .andExpect(jsonPath("$[2].teacherId", is(lesson_3.getTeacherId()), Long.class))
                .andExpect(jsonPath("$[2].subjectId", is(lesson_3.getSubjectId())))
                .andExpect(jsonPath("$[2].groupId", is(lesson_3.getGroupId()), Long.class))
                .andExpect(jsonPath("$[2].roomId", is(lesson_3.getRoomId())))
                .andExpect(jsonPath("$[2].date", is(lesson_3.getDate())))

                .andExpect(jsonPath("$[3].id", is(lesson_4.getId()), Long.class))
                .andExpect(jsonPath("$[3].teacherId", is(lesson_4.getTeacherId()), Long.class))
                .andExpect(jsonPath("$[3].subjectId", is(lesson_4.getSubjectId())))
                .andExpect(jsonPath("$[3].groupId", is(lesson_4.getGroupId()), Long.class))
                .andExpect(jsonPath("$[3].roomId", is(lesson_4.getRoomId())))

                .andExpect(jsonPath("$[4].id", is(lesson_5.getId()), Long.class))
                .andExpect(jsonPath("$[4].teacherId", is(lesson_5.getTeacherId()), Long.class))
                .andExpect(jsonPath("$[4].subjectId", is(lesson_5.getSubjectId())))
                .andExpect(jsonPath("$[4].groupId", is(lesson_5.getGroupId()), Long.class))
                .andExpect(jsonPath("$[4].roomId", is(lesson_5.getRoomId())))

                .andExpect(jsonPath("$[5].id", is(lesson_6.getId()), Long.class))
                .andExpect(jsonPath("$[5].teacherId", is(lesson_6.getTeacherId()), Long.class))
                .andExpect(jsonPath("$[5].subjectId", is(lesson_6.getSubjectId())))
                .andExpect(jsonPath("$[5].groupId", is(lesson_6.getGroupId()), Long.class))
                .andExpect(jsonPath("$[5].roomId", is(lesson_6.getRoomId())));
    }

    @Test
    void whenGetLessonByValidId_Status200() throws Exception {
        this.mockMvc.perform(get("/api/lessons/{lessonId}", lesson_1.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(lesson_1.getId()), Long.class))
                .andExpect(jsonPath("$.teacherId", is(lesson_1.getTeacherId()), Long.class))
                .andExpect(jsonPath("$.subjectId", is(lesson_1.getSubjectId())))
                .andExpect(jsonPath("$.groupId", is(lesson_1.getGroupId()), Long.class))
                .andExpect(jsonPath("$.roomId", is(lesson_1.getRoomId())))
                .andExpect(jsonPath("$.date", is(lesson_1.getDate())))
                .andExpect(jsonPath("$.time", is(lesson_1.getTime().toString())));
    }

    @Test
    void whenAddLessonWithDuplicateData_Status400() throws Exception {
        LessonDTO newLesson = new LessonDTO(PERIOD_1, group_1.getGroupId(), subject_1.getId(), room_1.getId(), DATE,
                teacher.getId());
        String errorMessage = createDuplicateMessage(newLesson);
        this.mockMvc.perform(post("/api/lessons/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(newLesson)))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.errors", is(errorMessage)));
    }

    @Test
    void whenAddLessonWithValidData_Status200() throws Exception {
        LessonDTO newLesson = new LessonDTO(PERIOD_1, group_1.getGroupId(), subject_1.getId(), room_1.getId(), DATE_1_ADD_21_DAYS, teacher.getId());
        this.mockMvc.perform(post("/api/lessons/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(newLesson)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.teacherId", is(newLesson.getTeacherId()), Long.class))
                .andExpect(jsonPath("$.subjectId", is(newLesson.getSubjectId())))
                .andExpect(jsonPath("$.groupId", is(newLesson.getGroupId()), Long.class))
                .andExpect(jsonPath("$.roomId", is(newLesson.getRoomId())))
                .andExpect(jsonPath("$.date", is(newLesson.getDate())));
    }

    @Test
    void whenUpdateLessonWithValidData_Status200() throws Exception {
        LessonDTO newLesson = new LessonDTO(lesson_1.getId(),PERIOD_1, group_1.getGroupId(), subject_2.getId(), room_3.getId(), DATE_1_ADD_21_DAYS, teacher.getId());
        this.mockMvc.perform(put("/api/lessons/{lessonId}", lesson_1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(newLesson)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.teacherId", is(newLesson.getTeacherId()), Long.class))
                .andExpect(jsonPath("$.subjectId", is(newLesson.getSubjectId())))
                .andExpect(jsonPath("$.groupId", is(newLesson.getGroupId()), Long.class))
                .andExpect(jsonPath("$.roomId", is(newLesson.getRoomId())))
                .andExpect(jsonPath("$.date", is(newLesson.getDate())));
    }

    @Test
    void whenUpdateLessonWithDuplicateData_Status400() throws Exception {
        LessonDTO newLesson = new LessonDTO(lesson_2.getId(),PERIOD_1, group_1.getGroupId(), subject_1.getId(), room_1.getId(), DATE,
                teacher.getId());
        String errorMessage = createDuplicateMessage(newLesson);
        this.mockMvc.perform(put("/api/lessons/{lessonId}",lesson_2.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(newLesson)))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.errors", is(errorMessage)));
    }

    @Test
    void whenDeleteLessonByValidId_Status200_LessonDelete() throws Exception {
        this.mockMvc.perform(delete("/api/lessons/{lessonId}", lesson_1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    void whenDeleteLessonByInValidId_Status404_LessonDelete() throws Exception {
        this.mockMvc.perform(delete("/api/lessons/{lessonId}", INVALID_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.errors", is(String.format("Failed to delete Lesson by id %d", INVALID_ID))));
    }

    private String createDuplicateMessage(LessonDTO lessonDTO) {
        return String.format(
                "Error! Already exists Lesson {id=%d, period=%d, groupId=%d, subjectId=%d, roomId=%d, time=%s, teacherId=%d, date=%s}",
                lessonDTO.getId(), lessonDTO.getPeriod(), lessonDTO.getGroupId(), lessonDTO.getSubjectId(),
                lessonDTO.getRoomId(), lessonDTO.getTime(), lessonDTO.getTeacherId(), lessonDTO.getDate());
    }
}