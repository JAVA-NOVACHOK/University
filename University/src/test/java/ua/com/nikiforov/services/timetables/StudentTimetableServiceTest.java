package ua.com.nikiforov.services.timetables;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;

import ua.com.nikiforov.config.DatabaseConfig;
import ua.com.nikiforov.dao.table_creator.TableCreator;
import ua.com.nikiforov.models.Group;
import ua.com.nikiforov.models.Room;
import ua.com.nikiforov.models.Subject;
import ua.com.nikiforov.models.persons.Student;
import ua.com.nikiforov.models.persons.Teacher;
import ua.com.nikiforov.models.timetable.Timetable;
import ua.com.nikiforov.services.group.GroupService;
import ua.com.nikiforov.services.lesson.LessonService;
import ua.com.nikiforov.services.persons.StudentsService;
import ua.com.nikiforov.services.persons.TeacherService;
import ua.com.nikiforov.services.room.RoomService;
import ua.com.nikiforov.services.subject.SubjectService;

@TestInstance(Lifecycle.PER_CLASS)
@SpringJUnitConfig(DatabaseConfig.class)
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
class StudentTimetableServiceTest {

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
    private static final String DATE = "2020-10-13";

    public static final String ZONE = "Europe/Simferopol";

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
    private LessonService lessonService;

    private Teacher teacher_1;
    private Teacher teacher_2;
    private Teacher teacher_3;

    private Student student;

    @BeforeAll
    void setup() {
        tableCreator.createTables();
        Room room_1 = insertRoom(TEST_ROOM_NUMBER_1, TEST_SEAT_NUMBER_1);
        Room room_2 = insertRoom(TEST_ROOM_NUMBER_2, TEST_SEAT_NUMBER_1);
        Room room_3 = insertRoom(TEST_ROOM_NUMBER_3, TEST_SEAT_NUMBER_1);

        Group group_1 = insertGroup(TEST_GROUP_NAME_1);
        Group group_2 = insertGroup(TEST_GROUP_NAME_2);

        Subject subject_1 = insertSubject(SUBJECT_NAME_1);
        Subject subject_2 = insertSubject(SUBJECT_NAME_2);
        Subject subject_3 = insertSubject(SUBJECT_NAME_3);
        
        teacher_1 = insertTeacher(TEACHERS_FIRST_NAME_1, TEACHERS_LAST_NAME_1);
        teacher_2 = insertTeacher(TEACHERS_FIRST_NAME_2, TEACHERS_LAST_NAME_2);
        teacher_3 = insertTeacher(TEACHERS_FIRST_NAME_3, TEACHERS_LAST_NAME_3);

        student = insertStudent(STUDENTS_FIRST_NAME_1, STUDENTS_LAST_NAME_1, group_1.getId());
        insertStudent(STUDENTS_FIRST_NAME_2, STUDENTS_LAST_NAME_2, group_1.getId());
        insertStudent(STUDENTS_FIRST_NAME_3, STUDENTS_LAST_NAME_3, group_1.getId());

        lessonService.addLesson(PERIOD_1, subject_1.getId(), room_1.getId(), group_1.getId(), DATE,
                teacher_1.getId());
        lessonService.addLesson(PERIOD_2, subject_2.getId(), room_2.getId(), group_1.getId(), DATE,
                teacher_2.getId());
        lessonService.addLesson(PERIOD_3, subject_3.getId(), room_3.getId(), group_1.getId(), DATE,
                teacher_3.getId());

        lessonService.addLesson(PERIOD_1, subject_1.getId(), room_2.getId(), group_2.getId(), DATE,
                teacher_1.getId());
        lessonService.addLesson(PERIOD_2, subject_2.getId(), room_3.getId(), group_2.getId(), DATE,
                teacher_2.getId());
        lessonService.addLesson(PERIOD_3, subject_3.getId(), room_2.getId(), group_2.getId(), DATE,
                teacher_3.getId());

        lessonService.addLesson(PERIOD_1, subject_3.getId(), room_2.getId(), group_1.getId(), DATE_1_ADD_1_DAY,
                teacher_1.getId());
        lessonService.addLesson(PERIOD_2, subject_2.getId(), room_2.getId(), group_1.getId(), DATE_1_ADD_1_DAY,
                teacher_2.getId());
        lessonService.addLesson(PERIOD_3, subject_3.getId(), room_3.getId(), group_1.getId(), DATE_1_ADD_1_DAY,
                teacher_3.getId());

        lessonService.addLesson(PERIOD_1, subject_1.getId(), room_1.getId(), group_1.getId(), DATE_1_ADD_3_DAYS,
                teacher_1.getId());
        lessonService.addLesson(PERIOD_2, subject_1.getId(), room_1.getId(), group_1.getId(), DATE_1_ADD_3_DAYS,
                teacher_1.getId());
        lessonService.addLesson(PERIOD_3, subject_2.getId(), room_3.getId(), group_1.getId(), DATE_1_ADD_3_DAYS,
                teacher_3.getId());

        lessonService.addLesson(PERIOD_1, subject_3.getId(), room_3.getId(), group_1.getId(), DATE_1_ADD_13_DAYS,
                teacher_3.getId());
        lessonService.addLesson(PERIOD_2, subject_1.getId(), room_2.getId(), group_1.getId(), DATE_1_ADD_13_DAYS,
                teacher_2.getId());
        lessonService.addLesson(PERIOD_3, subject_2.getId(), room_2.getId(), group_1.getId(), DATE_1_ADD_13_DAYS,
                teacher_2.getId());

        lessonService.addLesson(PERIOD_1, subject_1.getId(), room_2.getId(), group_2.getId(), DATE_1_ADD_13_DAYS,
                teacher_1.getId());
        lessonService.addLesson(PERIOD_2, subject_2.getId(), room_3.getId(), group_2.getId(), DATE_1_ADD_13_DAYS,
                teacher_2.getId());
        lessonService.addLesson(PERIOD_3, subject_3.getId(), room_1.getId(), group_2.getId(), DATE_1_ADD_13_DAYS,
                teacher_3.getId());

        lessonService.addLesson(PERIOD_1, subject_1.getId(), room_1.getId(), group_1.getId(), DATE_1_ADD_21_DAYS,
                teacher_1.getId());
        lessonService.addLesson(PERIOD_2, subject_2.getId(), room_2.getId(), group_1.getId(), DATE_1_ADD_21_DAYS,
                teacher_2.getId());
        lessonService.addLesson(PERIOD_3, subject_3.getId(), room_3.getId(), group_1.getId(), DATE_1_ADD_21_DAYS,
                teacher_3.getId());
        
        lessonService.addLesson(PERIOD_1, subject_1.getId(), room_1.getId(), group_1.getId(), DATE_1_ADD_33_DAYS,
                teacher_1.getId());
        lessonService.addLesson(PERIOD_2, subject_2.getId(), room_2.getId(), group_1.getId(), DATE_1_ADD_33_DAYS,
                teacher_2.getId());
        lessonService.addLesson(PERIOD_3, subject_3.getId(), room_3.getId(), group_1.getId(), DATE_1_ADD_33_DAYS,
                teacher_3.getId());

    }

    @Test
    void whenGetDayTimetableByDateAndGroupIdShouldReturnListOfTimetables() {
        List<Timetable> expectedTimetables = new ArrayList<>();
        expectedTimetables.add(new Timetable(PERIOD_1, SUBJECT_NAME_1, TEST_ROOM_NUMBER_1,
                TEST_GROUP_NAME_1, getInstantFromString(DATE), TEACHERS_FIRST_NAME_1 + " " + TEACHERS_LAST_NAME_1));
        expectedTimetables.add(new Timetable(PERIOD_2, SUBJECT_NAME_2, TEST_ROOM_NUMBER_2,
                TEST_GROUP_NAME_1, getInstantFromString(DATE), TEACHERS_FIRST_NAME_2 + " " + TEACHERS_LAST_NAME_2));
        expectedTimetables.add(new Timetable(PERIOD_3, SUBJECT_NAME_3, TEST_ROOM_NUMBER_3,
                TEST_GROUP_NAME_1, getInstantFromString(DATE), TEACHERS_FIRST_NAME_3 + " " + TEACHERS_LAST_NAME_3));

        List<Timetable> actualTimetables = studentTimetableService.getDayTimetable(DATE, student.getGroupId());
        assertIterableEquals(expectedTimetables, actualTimetables);
    }

    @Test
    void whenGetMonthTimetableByDateAndStudentGroupIdShouldReturnListOfTimetables() {

        List<DayTimetable> expectedMonthTimetable = new ArrayList<>();

        List<Timetable> expectedTimetables_DATE = new ArrayList<>();
        expectedTimetables_DATE.add(new Timetable(PERIOD_1, SUBJECT_NAME_1, TEST_ROOM_NUMBER_1,
                TEST_GROUP_NAME_1, getInstantFromString(DATE), TEACHERS_FIRST_NAME_1 + " " + TEACHERS_LAST_NAME_1));
        expectedTimetables_DATE.add(new Timetable(PERIOD_2, SUBJECT_NAME_2, TEST_ROOM_NUMBER_2,
                TEST_GROUP_NAME_1, getInstantFromString(DATE), TEACHERS_FIRST_NAME_2 + " " + TEACHERS_LAST_NAME_2));
        expectedTimetables_DATE.add(new Timetable(PERIOD_3, SUBJECT_NAME_3, TEST_ROOM_NUMBER_3,
                TEST_GROUP_NAME_1, getInstantFromString(DATE), TEACHERS_FIRST_NAME_3 + " " + TEACHERS_LAST_NAME_3));
        DateInfo dateInfo_DATE = PersonalTimetable.parseInstantToDateInfo(expectedTimetables_DATE.get(0));
        expectedMonthTimetable.add(new DayTimetable(expectedTimetables_DATE, dateInfo_DATE));

        List<Timetable> expectedTimetables_DATE_ADD_1_DAY = new ArrayList<>();
        expectedTimetables_DATE_ADD_1_DAY
                .add(new Timetable(PERIOD_1, SUBJECT_NAME_3, TEST_ROOM_NUMBER_2, TEST_GROUP_NAME_1,
                        getInstantFromString(DATE_1_ADD_1_DAY), TEACHERS_FIRST_NAME_1 + " " + TEACHERS_LAST_NAME_1));
        expectedTimetables_DATE_ADD_1_DAY
                .add(new Timetable(PERIOD_2, SUBJECT_NAME_2, TEST_ROOM_NUMBER_2, TEST_GROUP_NAME_1,
                        getInstantFromString(DATE_1_ADD_1_DAY), TEACHERS_FIRST_NAME_2 + " " + TEACHERS_LAST_NAME_2));
        expectedTimetables_DATE_ADD_1_DAY
                .add(new Timetable(PERIOD_3, SUBJECT_NAME_3, TEST_ROOM_NUMBER_3, TEST_GROUP_NAME_1,
                        getInstantFromString(DATE_1_ADD_1_DAY), TEACHERS_FIRST_NAME_3 + " " + TEACHERS_LAST_NAME_3));
        DateInfo dateInfo_DATE_ADD_1_DAY = PersonalTimetable.parseInstantToDateInfo(expectedTimetables_DATE.get(0));
        expectedMonthTimetable.add(new DayTimetable(expectedTimetables_DATE_ADD_1_DAY, dateInfo_DATE_ADD_1_DAY));

        List<Timetable> expectedTimetables_DATE_ADD_3_DAYS = new ArrayList<>();
        expectedTimetables_DATE_ADD_3_DAYS
                .add(new Timetable(PERIOD_1, SUBJECT_NAME_1, TEST_ROOM_NUMBER_1, TEST_GROUP_NAME_1,
                        getInstantFromString(DATE_1_ADD_3_DAYS), TEACHERS_FIRST_NAME_1 + " " + TEACHERS_LAST_NAME_1));
        expectedTimetables_DATE_ADD_3_DAYS
                .add(new Timetable(PERIOD_2, SUBJECT_NAME_1, TEST_ROOM_NUMBER_1, TEST_GROUP_NAME_1,
                        getInstantFromString(DATE_1_ADD_3_DAYS), TEACHERS_FIRST_NAME_1 + " " + TEACHERS_LAST_NAME_1));
        expectedTimetables_DATE_ADD_3_DAYS
                .add(new Timetable(PERIOD_3, SUBJECT_NAME_2, TEST_ROOM_NUMBER_3, TEST_GROUP_NAME_1,
                        getInstantFromString(DATE_1_ADD_3_DAYS), TEACHERS_FIRST_NAME_3 + " " + TEACHERS_LAST_NAME_3));
        DateInfo dateInfo_DATE_ADD_3_DAY = PersonalTimetable.parseInstantToDateInfo(expectedTimetables_DATE.get(0));
        expectedMonthTimetable.add(new DayTimetable(expectedTimetables_DATE_ADD_3_DAYS, dateInfo_DATE_ADD_3_DAY));

        List<Timetable> expectedTimetables_DATE_ADD_13_DAYS = new ArrayList<>();
        expectedTimetables_DATE_ADD_13_DAYS
                .add(new Timetable(PERIOD_1, SUBJECT_NAME_3, TEST_ROOM_NUMBER_3, TEST_GROUP_NAME_1,
                        getInstantFromString(DATE_1_ADD_13_DAYS), TEACHERS_FIRST_NAME_3 + " " + TEACHERS_LAST_NAME_3));
        expectedTimetables_DATE_ADD_13_DAYS
                .add(new Timetable(PERIOD_2, SUBJECT_NAME_1, TEST_ROOM_NUMBER_2, TEST_GROUP_NAME_1,
                        getInstantFromString(DATE_1_ADD_13_DAYS), TEACHERS_FIRST_NAME_2 + " " + TEACHERS_LAST_NAME_2));
        expectedTimetables_DATE_ADD_13_DAYS
                .add(new Timetable(PERIOD_3, SUBJECT_NAME_2, TEST_ROOM_NUMBER_2, TEST_GROUP_NAME_1,
                        getInstantFromString(DATE_1_ADD_13_DAYS), TEACHERS_FIRST_NAME_2 + " " + TEACHERS_LAST_NAME_2));
        DateInfo dateInfo_DATE_ADD_13_DAY = PersonalTimetable.parseInstantToDateInfo(expectedTimetables_DATE.get(0));
        expectedMonthTimetable.add(new DayTimetable(expectedTimetables_DATE_ADD_13_DAYS, dateInfo_DATE_ADD_13_DAY));

        List<Timetable> expectedTimetables_DATE_ADD_21_DAYS = new ArrayList<>();
        expectedTimetables_DATE_ADD_21_DAYS.add(new Timetable(PERIOD_1, SUBJECT_NAME_1, TEST_ROOM_NUMBER_1,
                TEST_GROUP_NAME_1, getInstantFromString(DATE_1_ADD_21_DAYS), TEACHERS_FIRST_NAME_1 + " " + TEACHERS_LAST_NAME_1));
        expectedTimetables_DATE_ADD_21_DAYS.add(new Timetable(PERIOD_2, SUBJECT_NAME_2, TEST_ROOM_NUMBER_2,
                TEST_GROUP_NAME_1, getInstantFromString(DATE_1_ADD_21_DAYS), TEACHERS_FIRST_NAME_2 + " " + TEACHERS_LAST_NAME_2));
        expectedTimetables_DATE_ADD_21_DAYS.add(new Timetable(PERIOD_3, SUBJECT_NAME_3, TEST_ROOM_NUMBER_3,
                TEST_GROUP_NAME_1, getInstantFromString(DATE_1_ADD_21_DAYS), TEACHERS_FIRST_NAME_3 + " " + TEACHERS_LAST_NAME_3));
        DateInfo dateInfo_DATE_ADD_21_DAY = PersonalTimetable.parseInstantToDateInfo(expectedTimetables_DATE.get(0));
        expectedMonthTimetable.add(new DayTimetable(expectedTimetables_DATE_ADD_21_DAYS, dateInfo_DATE_ADD_21_DAY));

        List<DayTimetable> actualMonthTimetables = studentTimetableService.getMonthTimetable(DATE,
                student.getGroupId());
        assertEquals(expectedMonthTimetable.size(), actualMonthTimetables.size());

        for (int i = 0; i < expectedMonthTimetable.size(); i++) {
            assertIterableEquals(expectedMonthTimetable.get(i).getTimetables(),
                    actualMonthTimetables.get(i).getTimetables());
        }
    }

    private Instant getInstantFromString(String date) {
        Date time = null;
        try {
            time = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time.toInstant();
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
