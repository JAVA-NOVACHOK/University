package ua.com.nikiforov.controllers;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import ua.com.nikiforov.dto.*;
import ua.com.nikiforov.services.group.GroupService;
import ua.com.nikiforov.services.lesson.LessonService;
import ua.com.nikiforov.services.persons.StudentsService;
import ua.com.nikiforov.services.persons.TeacherService;
import ua.com.nikiforov.services.room.RoomService;
import ua.com.nikiforov.services.subject.SubjectService;
import ua.com.nikiforov.services.timetables.DateInfo;
import ua.com.nikiforov.services.timetables.DayTimetable;
import ua.com.nikiforov.services.timetables.StudentTimetableService;
import ua.com.nikiforov.services.timetables.TeachersTimetableService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
class ScheduleControllerTest {

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

    private static final String DATE_1 = "2020-09-15";
    private static final String DATE_1_ADD_1_DAY = "2020-09-16";
    private static final String DATE_1_ADD_3_DAYS = "2020-09-18";
    private static final String DATE_1_ADD_13_DAYS = "2020-09-28";
    private static final String DATE_1_ADD_21_DAYS = "2020-10-06";
    private static final String DATE_1_ADD_33_DAYS = "2020-11-18";
    private static final String DATE = "2020-10-13";

    private static final String WEEK_DAY_FOR_DATE_2020_10_13 = "Tuesday";
    private static final String MONTH_NAME_FOR_DATE_2020_10_13 = "OCTOBER";

    private static final int MONTH_DAY_FOR_DATE_2020_10_13 = 13;
    private static final int YEAR_2020 = 2020;

    private static final String PERIOD_ATTR = "period";
    private static final String ID = "id";
    private static final String LESSON_ID_ATTR = "lessonId";
    private static final String SUBJECT_ID_ATTR = "subjectId";
    private static final String SUBJECT_NAME_ATTR = "name";
    private static final String SUBJECTS_ATTR = "subjects";
    private static final String ROOM_ID_ATTR = "roomId";
    private static final String ROOM_NUMBER_ATTR = "roomNumber";
    private static final String ROOMS_ATTR = "rooms";
    private static final String GROUP_ID_ATTR = "groupId";
    private static final String GROUPS_ATTR = "groups";
    private static final String DATE_ATTR = "date";
    private static final String TEACHER_ID_ATTR = "teacherId";
    private static final String TEACHERS_ATTR = "teachers";
    private static final String TEACHER_ATTR = "teacher";
    private static final String SCHEDULE_FIND_ATTR = "scheduleFindAttr";
    private static final String STUDENT_ATTR = "student";
    private static final String TIMETABLES_ATTR = "timetables";
    private static final String TIMETABLE_ATTR = "timetable";
    private static final String LESSON_ATTR = "lesson";
    private static final String DATE_STRING_ATTR = "dateString";

    private static final String FIRST_NAME_PARAM = "firstName";
    private static final String LAST_NAME_PARAM = "lastName";
    private static final String TIME_PARAM = "time";

    private static final String ONE_TEACHER_VIEW = "teachers/one_teacher";
    private static final String TEACHER_SCHEDULE_VIEW = "timetable/teacher_schedule";
    private static final String STUDENT_SCHEDULE_VIEW = "timetable/student_schedule";
    private static final String TEACHER_TIMETABLE_FORM_VIEW = "timetable/teacher_timetable_form";
    private static final String SCHEDULE_VIEW = "timetable/schedule";
    private static final String EDIT_SCHEDULE_VIEW = "timetable/edit_schedule";

    private static final String SUCCESS_MSG = "success";
    private static final String FAIL_MSG = "failMessage";

    private static final String STR = "";

    @Autowired
    private RoomService roomService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private StudentsService studentsService;

    @Autowired
    private StudentTimetableService studentTimetableService;

    @Autowired
    private TeachersTimetableService teacherTimetableService;

    @Autowired
    private LessonService lessonService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private TeacherDTO teacher;
    private TeacherDTO teacher_2;
    private TeacherDTO teacher_3;

    private StudentDTO student;


    private  RoomDTO room_1;
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


    @BeforeAll
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @BeforeAll
    public void init() {

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

        lessonService.addLesson(new LessonDTO(PERIOD_1, group_3.getGroupId(), subject_1.getId(), room_1.getId(), DATE_1,
                teacher.getId()));
        lessonService.addLesson(new LessonDTO(PERIOD_2, group_3.getGroupId(), subject_2.getId(), room_2.getId(), DATE_1_ADD_3_DAYS,
                teacher.getId()));
        lessonService.addLesson(new LessonDTO(PERIOD_1, group_3.getGroupId(), subject_3.getId(), room_3.getId(), DATE_1_ADD_13_DAYS,
                teacher.getId()));

    }

    @Test
    void givenScheduleURI_whenMockMVC_thenReturnScheduleView() throws Exception {
        this.mockMvc.perform(get("/schedules/")).andDo(print()).andExpect(view().name(SCHEDULE_VIEW));
    }

    @Test
    void givenTeacherURI_whenMockMVC_thenReturnsTeacherTimetableFormView() throws Exception {
        this.mockMvc.perform(get("/schedules/teacher")).andDo(print())
                .andExpect(view().name(TEACHER_TIMETABLE_FORM_VIEW));
    }

    @Test
    void TeachersDayURI_ThenReturnsTeacherSchedule_WithTimetable_Models()
            throws Exception {
        List<DayTimetable> timetables = new ArrayList<>();
        List<TimetableDTO> timetablesList = new ArrayList<>();

        timetablesList.add(new TimetableDTO(lesson_1.getId(), PERIOD_1, subject_1,
                group_1, room_1, teacher, getLocalDateFromString(DATE)));
        timetablesList.add(new TimetableDTO(lesson_2.getId(), PERIOD_2, subject_2,
                group_2, room_2, teacher, getLocalDateFromString(DATE)));
        timetablesList.add(new TimetableDTO(lesson_3.getId(), PERIOD_3, subject_1,
                group_2, room_3, teacher, getLocalDateFromString(DATE)));

        DateInfo dateInfo = new DateInfo(WEEK_DAY_FOR_DATE_2020_10_13, MONTH_DAY_FOR_DATE_2020_10_13, MONTH_NAME_FOR_DATE_2020_10_13, YEAR_2020);
        timetables.add(new DayTimetable(timetablesList, dateInfo));
        this.mockMvc
                .perform(post("/schedules/teachers_day")
                        .param(FIRST_NAME_PARAM, TEACHERS_FIRST_NAME_1)
                        .param(LAST_NAME_PARAM, TEACHERS_LAST_NAME_1)
                        .param(TIME_PARAM, DATE)
                        .sessionAttr(SCHEDULE_FIND_ATTR, new ScheduleFindAttr()))
                .andExpect(model().attribute(TEACHER_ATTR, teacher))
                .andExpect(model().attribute(TIMETABLES_ATTR, timetables))
                .andExpect(model().attributeDoesNotExist(FAIL_MSG))
                .andExpect(model().size(5))
                .andExpect(status().isOk())
                .andExpect(view().name(TEACHER_SCHEDULE_VIEW));
    }

    @Test
    void ScheduleTeachersMonthURI_ReturnsTeacherTimetable_WithDateInfo_DayTimetable_Models()
            throws Exception {

        this.mockMvc
                .perform(post("/schedules/teachers_month")
                        .param(FIRST_NAME_PARAM, TEACHERS_FIRST_NAME_1)
                        .param(LAST_NAME_PARAM, TEACHERS_LAST_NAME_1)
                        .param(TIME_PARAM, DATE)
                        .sessionAttr(SCHEDULE_FIND_ATTR, new ScheduleFindAttr()))
                .andExpect(model().attribute(TEACHER_ATTR, teacher))
                .andExpect(model().attributeExists(TIMETABLES_ATTR))
                .andExpect(model().attributeDoesNotExist(FAIL_MSG))
                .andExpect(model().size(5))
                .andExpect(status().isOk())
                .andExpect(view().name(TEACHER_SCHEDULE_VIEW));
    }

    @Test
    void StudentsDayURI_ReturnsStudentTimetable_WithStudent_DateInfo_DayTimetable_Models()
            throws Exception {
        this.mockMvc
                .perform(post("/schedules/students_day")
                        .param(FIRST_NAME_PARAM, STUDENTS_FIRST_NAME_1)
                        .param(LAST_NAME_PARAM, STUDENTS_LAST_NAME_1)
                        .param(TIME_PARAM, DATE)
                        .sessionAttr(SCHEDULE_FIND_ATTR, new ScheduleFindAttr()))
                .andExpect(status().isOk())
                .andExpect(model().size(5))
                .andExpect(model().attribute(STUDENT_ATTR, student))
                .andExpect(model().attributeExists(TIMETABLES_ATTR))
                .andExpect(model().attributeExists(SCHEDULE_FIND_ATTR))
                .andExpect(view().name(STUDENT_SCHEDULE_VIEW));
    }

    @Test
    void givenScheduleStudentsMonthPageURI_ThenReturnsTimetableStudentScheduleViewName_WithDateInfo_DayTimetable_Models()
            throws Exception {

        this.mockMvc
                .perform(post("/schedules/students_month")
                        .param(FIRST_NAME_PARAM, STUDENTS_FIRST_NAME_1)
                        .param(LAST_NAME_PARAM, STUDENTS_LAST_NAME_1)
                        .param(TIME_PARAM, DATE)
                        .sessionAttr(SCHEDULE_FIND_ATTR, new ScheduleFindAttr()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists(TIMETABLES_ATTR))
                .andExpect(model().attribute(STUDENT_ATTR, student))
                .andExpect(model().size(5))
                .andExpect(view().name(STUDENT_SCHEDULE_VIEW));
    }


    @Test
    void addTimetableSchedule_Success() throws Exception {
//        TeacherDTO teacher_2 = insertTeacher(TEACHERS_FIRST_NAME_2, TEACHERS_LAST_NAME_2);
//        TeacherDTO teacher_3 = insertTeacher(TEACHERS_FIRST_NAME_3, TEACHERS_LAST_NAME_3);

        teacherService.assignSubjectToTeacher(teacher.getId(), subject_1.getId());
        teacherService.assignSubjectToTeacher(teacher.getId(), subject_2.getId());

        teacher.addSubject(subject_1);
        teacher.addSubject(subject_2);
        this.mockMvc
                .perform(post("/schedules/add")
                        .param(PERIOD_ATTR, PERIOD_1 + STR)
                        .param(SUBJECT_ID_ATTR, subject_1.getId() + STR)
                        .param(GROUP_ID_ATTR, group_1.getGroupId() + STR)
                        .param(ROOM_ID_ATTR, room_1.getId() + STR)
                        .param(DATE_ATTR, DATE_1_ADD_33_DAYS)
                        .param(TEACHER_ID_ATTR, teacher.getId() + STR))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ROOMS_ATTR, hasItems(room_1, room_2, room_3)))
                .andExpect(model().attribute(TEACHERS_ATTR, hasItems(teacher, teacher_2, teacher_3)))
                .andExpect(model().attribute(SUBJECTS_ATTR, hasItems(subject_1, subject_2, subject_3)))
                .andExpect(model().attribute(GROUPS_ATTR, hasItems(group_1, group_2, group_3)))
                .andExpect(model().attributeExists(SUCCESS_MSG))
                .andExpect(model().size(9))
                .andExpect(view().name(ONE_TEACHER_VIEW));
    }

    @Test
    void addTimetableSchedule_WithDuplicateData_FailAdding() throws Exception {
//        TeacherDTO teacher_2 = insertTeacher(TEACHERS_FIRST_NAME_2, TEACHERS_LAST_NAME_2);
//        TeacherDTO teacher_3 = insertTeacher(TEACHERS_FIRST_NAME_3, TEACHERS_LAST_NAME_3);

//        insertLesson(PERIOD_1, subject_1.getId(), room_1.getId(), group_1.getGroupId(),
//                DATE_1_ADD_33_DAYS, teacher.getId());

        teacherService.assignSubjectToTeacher(teacher.getId(), subject_1.getId());
        teacherService.assignSubjectToTeacher(teacher.getId(), subject_2.getId());

        teacher.addSubject(subject_1);
        teacher.addSubject(subject_2);
        this.mockMvc
                .perform(post("/schedules/add")
                        .param(PERIOD_ATTR, PERIOD_1 + STR)
                        .param(SUBJECT_ID_ATTR, subject_1.getId() + STR)
                        .param(ROOM_ID_ATTR, room_1.getId() + STR)
                        .param(GROUP_ID_ATTR, group_1.getGroupId() + STR)
                        .param(DATE_ATTR, DATE_1_ADD_33_DAYS)
                        .param(TEACHER_ID_ATTR, teacher.getId() + STR))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ROOMS_ATTR, hasItems(room_1, room_2, room_3)))
                .andExpect(model().attribute(TEACHERS_ATTR, hasItems(teacher, teacher_2, teacher_3)))
                .andExpect(model().attribute(SUBJECTS_ATTR, hasItems(subject_1, subject_2, subject_3)))
                .andExpect(model().attribute(GROUPS_ATTR, hasItems(group_1, group_2, group_3)))
                .andExpect(model().attributeExists(FAIL_MSG))
                .andExpect(model().size(9))
                .andExpect(view().name(ONE_TEACHER_VIEW));
    }

    @Test
    void editTimetableSchedule_ReturnEditForm() throws Exception {
//        TeacherDTO teacher_2 = insertTeacher(TEACHERS_FIRST_NAME_2, TEACHERS_LAST_NAME_2);
//        TeacherDTO teacher_3 = insertTeacher(TEACHERS_FIRST_NAME_3, TEACHERS_LAST_NAME_3);

        teacherService.assignSubjectToTeacher(teacher.getId(), subject_1.getId());
        teacherService.assignSubjectToTeacher(teacher.getId(), subject_2.getId());

        teacher.addSubject(subject_1);
        teacher.addSubject(subject_2);
        this.mockMvc
                .perform(get("/schedules/edit")
                        .param(LESSON_ID_ATTR, lesson_1.getId() + STR)
                        .param(PERIOD_ATTR, PERIOD_1 + STR)
                        .param(SUBJECT_ID_ATTR, subject_1.getName())
                        .param(ROOM_ID_ATTR, room_1.getRoomNumber() + STR)
                        .param(GROUP_ID_ATTR, group_1.getGroupName())
                        .requestAttr(DATE_ATTR, getLocalDateFromString(DATE))
                        .param(TEACHER_ID_ATTR, teacher.getId() + STR)
                        .sessionAttr(TIMETABLES_ATTR, new TimetableDTO())
                        .param(DATE_STRING_ATTR, DATE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ROOMS_ATTR, hasItems(room_1, room_2, room_3)))
                .andExpect(model().attribute(TEACHERS_ATTR, hasItems(teacher, teacher_2, teacher_3)))
                .andExpect(model().attribute(SUBJECTS_ATTR, hasItems(subject_1, subject_2, subject_3)))
                .andExpect(model().attribute(GROUPS_ATTR, hasItems(group_1, group_2, group_3)))
                .andExpect(model().size(8))
                .andExpect(view().name(EDIT_SCHEDULE_VIEW));
    }

    @Test
    void editLesson_SuccessEditing() throws Exception {
        teacherService.assignSubjectToTeacher(teacher.getId(), subject_1.getId());
        teacherService.assignSubjectToTeacher(teacher.getId(), subject_2.getId());

        teacher.addSubject(subject_1);
        teacher.addSubject(subject_2);
        this.mockMvc
                .perform(post("/schedules/edit")
                        .param(ID, lesson_1.getId() + STR)
                        .param(PERIOD_ATTR, PERIOD_1 + STR)
                        .param(SUBJECT_ID_ATTR, subject_1.getId() + STR)
                        .param(ROOM_ID_ATTR, room_1.getId() + STR)
                        .param(GROUP_ID_ATTR, group_1.getGroupId() + STR)
                        .param(DATE_ATTR, DATE)
                        .param(TEACHER_ID_ATTR, teacher.getId() + STR)
                        .sessionAttr(LESSON_ATTR, new LessonDTO()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists(TIMETABLES_ATTR))
                .andExpect(model().attributeExists(SUCCESS_MSG))
                .andExpect(model().size(6))
                .andExpect(view().name(TEACHER_SCHEDULE_VIEW));
    }

    @Test
    void deleteTimetable_SuccessDelete() throws Exception {
        List<DayTimetable> timetables = new ArrayList<>();
        List<TimetableDTO> timetablesList = new ArrayList<>();

        timetablesList.add(new TimetableDTO(lesson_2.getId(), PERIOD_2, subject_2,
                group_2, room_2, teacher, getLocalDateFromString(DATE)));
        timetablesList.add(new TimetableDTO(lesson_3.getId(), PERIOD_3, subject_1,
                group_2, room_3, teacher, getLocalDateFromString(DATE)));

        DateInfo dateInfo = new DateInfo(WEEK_DAY_FOR_DATE_2020_10_13, MONTH_DAY_FOR_DATE_2020_10_13, MONTH_NAME_FOR_DATE_2020_10_13, YEAR_2020);
        timetables.add(new DayTimetable(timetablesList, dateInfo));
        this.mockMvc
                .perform(get("/schedules/delete")
                        .param(LESSON_ID_ATTR, lesson_1.getId() + STR))
                .andExpect(status().isOk())
                .andExpect(model().attribute(TEACHER_ATTR, teacher))
                .andExpect(model().attribute(TIMETABLES_ATTR, timetables))
                .andExpect(model().attributeExists(SUCCESS_MSG))
                .andExpect(view().name(TEACHER_SCHEDULE_VIEW));
    }

    private GroupDTO insertGroup(String groupName) {
        groupService.addGroup(new GroupDTO(groupName));
        return groupService.getGroupByName(groupName);
    }

    public StudentDTO insertStudent(String firstName, String lastaName, long groupName) {
        studentsService.addStudent(new StudentDTO(firstName, lastaName, groupName));
        return studentsService.getStudentByNameGroupId(firstName, lastaName, groupName);
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

    private LessonDTO insertLesson(int period, int subjectId, int roomId, long groupId, String date, long teacherId) {
        lessonService.addLesson(new LessonDTO(period, groupId, subjectId, roomId, date, teacherId));
        return lessonService.getLessonByAllArgs(new LessonDTO(period, groupId, subjectId, roomId, date, teacherId));
    }

    private LocalDate getLocalDateFromString(String date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, dateTimeFormatter);
    }

}
