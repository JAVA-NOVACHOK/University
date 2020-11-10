package ua.com.nikiforov.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.plaf.multi.MultiViewportUI;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import ua.com.nikiforov.config.WebConfig;
import ua.com.nikiforov.controllers.model_atributes.ScheduleFindAttr;
import ua.com.nikiforov.dao.table_creator.TableCreator;
import ua.com.nikiforov.models.Group;
import ua.com.nikiforov.models.Room;
import ua.com.nikiforov.models.Subject;
import ua.com.nikiforov.models.lesson.Lesson;
import ua.com.nikiforov.models.persons.Student;
import ua.com.nikiforov.models.persons.Teacher;
import ua.com.nikiforov.models.timetable.Timetable;
import ua.com.nikiforov.services.group.GroupService;
import ua.com.nikiforov.services.lesson.LessonService;
import ua.com.nikiforov.services.persons.StudentsService;
import ua.com.nikiforov.services.persons.TeacherService;
import ua.com.nikiforov.services.room.RoomService;
import ua.com.nikiforov.services.subject.SubjectService;
import ua.com.nikiforov.services.timetables.DateInfo;
import ua.com.nikiforov.services.timetables.Period;
import ua.com.nikiforov.services.timetables.StudentTimetableService;
import ua.com.nikiforov.services.timetables.TeachersTimetableService;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { WebConfig.class })
@WebAppConfiguration
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
    private TableCreator tableCreator;

    @Autowired
    private StudentTimetableService studentTimetableService;

    @Autowired
    private TeachersTimetableService teacherTimetableService;

    @Autowired
    private LessonService lessonService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    
    private Teacher teacher;
    
    private Student student;
    
    private DateInfo dateInfo;

    @BeforeAll
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        tableCreator.createTables();
        
        Room room_1 = insertRoom(TEST_ROOM_NUMBER_1, TEST_SEAT_NUMBER_1);
        Room room_2 = insertRoom(TEST_ROOM_NUMBER_2, TEST_SEAT_NUMBER_1);
        Room room_3 = insertRoom(TEST_ROOM_NUMBER_3, TEST_SEAT_NUMBER_1);

        Group group_1 = insertGroup(TEST_GROUP_NAME_1);
        Group group_2 = insertGroup(TEST_GROUP_NAME_2);
        Group group_3 = insertGroup(TEST_GROUP_NAME_3);

        Subject subject_1 = insertSubject(SUBJECT_NAME_1);
        Subject subject_2 = insertSubject(SUBJECT_NAME_2);
        Subject subject_3 = insertSubject(SUBJECT_NAME_3);

        teacher = insertTeacher(TEACHERS_FIRST_NAME_1, TEACHERS_LAST_NAME_1);

        student = insertStudent(STUDENTS_FIRST_NAME_1, STUDENTS_LAST_NAME_1, group_1.getId());
        insertStudent(STUDENTS_FIRST_NAME_2, STUDENTS_LAST_NAME_2, group_1.getId());
        insertStudent(STUDENTS_FIRST_NAME_3, STUDENTS_LAST_NAME_3, group_1.getId());
        
        dateInfo = new DateInfo(WEEK_DAY_FOR_DATE_2020_10_13, MONTH_DAY_FOR_DATE_2020_10_13,
                MONTH_NAME_FOR_DATE_2020_10_13, YEAR_2020);

        lessonService.addLesson(Period.FIRST, subject_1.getId(), room_1.getId(), group_1.getId(), DATE,
                teacher.getId());
        lessonService.addLesson(Period.SECOND, subject_2.getId(), room_2.getId(), group_2.getId(), DATE,
                teacher.getId());
        lessonService.addLesson(Period.THIRD, subject_1.getId(), room_3.getId(), group_2.getId(), DATE,
                teacher.getId());
        
        lessonService.addLesson(Period.FIRST, subject_1.getId(), room_1.getId(), group_3.getId(), DATE_1,
                teacher.getId());
        lessonService.addLesson(Period.SECOND, subject_2.getId(), room_2.getId(), group_3.getId(), DATE_1_ADD_3_DAYS,
                teacher.getId());
        lessonService.addLesson(Period.FIRST, subject_3.getId(), room_3.getId(), group_3.getId(), DATE_1_ADD_13_DAYS,
                teacher.getId());

    }

    @Test
    void givenSchedulePageURI_whenMockMVC_thenReturnsScheduleViewName() throws Exception {
        this.mockMvc.perform(get("/schedules/")).andDo(print()).andExpect(view().name("timetable/schedule"));
    }

    @Test
    void givenScheduleTeacherPageURI_whenMockMVC_thenReturnsTeacherTimetableFormViewName() throws Exception {
        this.mockMvc.perform(get("/schedules/teacher")).andDo(print())
                .andExpect(view().name("timetable/teacher_timetable_form"));
    }

    @Test
    void givenScheduleTeachersDayPageURI_ThenReturnsTimetableTeacherScheduleViewName_WithDateInfo_DayTimetable_Models()
            throws Exception {
       

        this.mockMvc
                .perform(post("/schedules/teachers_day")
                .param("firstName", TEACHERS_FIRST_NAME_1)
                .param("lastName", TEACHERS_LAST_NAME_1)
                .param("time", DATE)
                .sessionAttr("scheduleFindAttr", new ScheduleFindAttr()))
                .andExpect(model().attribute("dateInfo", dateInfo))
                .andExpect(model().attribute("teacher", teacher))
                .andExpect(model().attributeExists("dayTimetable"))
                .andExpect(model().size(4))
                .andExpect(status().isOk())
                .andExpect(view().name("timetable/teacher_schedule"));
    }
    
    @Test
    void givenScheduleTeachersMonthPageURI_ThenReturnsTimetableTeacherScheduleViewName_WithDateInfo_DayTimetable_Models()
            throws Exception {
       
        this.mockMvc
                .perform(post("/schedules/teachers_month")
                .param("firstName", TEACHERS_FIRST_NAME_1)
                .param("lastName", TEACHERS_LAST_NAME_1)
                .param("time", DATE)
                .sessionAttr("scheduleFindAttr", new ScheduleFindAttr()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("monthTimetable"))
                .andExpect(model().attribute("teacher",teacher))
                .andExpect(model().size(3))
                .andExpect(view().name("timetable/teacher_schedule_month"));
    }
    
    @Test
    void givenScheduleStudentsDayPageURI_ThenReturnsTimetableStudentScheduleViewName_WithStudent_DateInfo_DayTimetable_Models()
            throws Exception {
       
        DateInfo dateInfo = new DateInfo("Tuesday", 13, "OCTOBER", 2020);

        this.mockMvc
                .perform(post("/schedules/students_day")
                .param("firstName", STUDENTS_FIRST_NAME_1)
                .param("lastName", STUDENTS_LAST_NAME_1)
                .param("time", DATE)
                .sessionAttr("scheduleFindAttr", new ScheduleFindAttr()))
                .andExpect(status().isOk())
                .andExpect(model().size(4))
                .andExpect(model().attribute("student", student))
                .andExpect(model().attributeExists("dayTimetable"))
                .andExpect(model().attributeExists("scheduleFindAttr"))
                .andExpect(model().attribute("dateInfo", dateInfo))
                .andExpect(view().name("timetable/student_schedule"));
    }
    
    @Test
    void givenScheduleStudentsMonthPageURI_ThenReturnsTimetableStudentScheduleViewName_WithDateInfo_DayTimetable_Models()
            throws Exception {
       
        this.mockMvc
                .perform(post("/schedules/students_month")
                .param("firstName", STUDENTS_FIRST_NAME_1)
                .param("lastName", STUDENTS_LAST_NAME_1)
                .param("time", DATE)
                .sessionAttr("scheduleFindAttr", new ScheduleFindAttr()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("monthTimetable"))
                .andExpect(model().attribute("student",student))
                .andExpect(model().size(3))
                .andExpect(view().name("timetable/student_schedule_month"));
    }

    private Group insertGroup(String groupName) {
        groupService.addGroup(groupName);
        return groupService.getGroupByName(groupName);
    }

    public Student insertStudent(String firstName, String lastaName, long groupName) {
        studentsService.addStudent(firstName, lastaName, groupName);
        return studentsService.getStudentByNameGroupId(firstName, lastaName, groupName);
    }

    private Subject insertSubject(String subjectName) {
        subjectService.addSubject(subjectName);
        return subjectService.getSubjectByName(subjectName);
    }

    private Room insertRoom(int roomNumber, int seatNumber) {
        roomService.addRoom(roomNumber, seatNumber);
        return roomService.getRoomByRoomNumber(roomNumber);
    }

    private Teacher insertTeacher(String firstName, String lastName) {
        teacherService.addTeacher(firstName, lastName);
        return teacherService.getTeacherByName(firstName, lastName);
    }

}
