package ua.com.nikiforov.services.timetables;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

import ua.com.nikiforov.dto.GroupDTO;
import ua.com.nikiforov.dto.LessonDTO;
import ua.com.nikiforov.dto.RoomDTO;
import ua.com.nikiforov.dto.SubjectDTO;
import ua.com.nikiforov.dto.TeacherDTO;
import ua.com.nikiforov.dto.TimetableDTO;
import ua.com.nikiforov.dao.table_creator.TableCreator;
import ua.com.nikiforov.datasource.TestDataSource;
import ua.com.nikiforov.services.group.GroupService;
import ua.com.nikiforov.services.lesson.LessonService;
import ua.com.nikiforov.services.persons.TeacherService;
import ua.com.nikiforov.services.room.RoomService;
import ua.com.nikiforov.services.subject.SubjectService;

@TestInstance(Lifecycle.PER_CLASS)
@SpringJUnitConfig(TestDataSource.class)
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
class TeachersTimetableServiceTest {

    private static final String TEST_GROUP_NAME_1 = "AA-12";
    private static final String TEST_GROUP_NAME_2 = "AA-13";
    private static final String TEST_GROUP_NAME_3 = "AA-14";

    private static final int TEST_ROOM_NUMBER_1 = 12;
    private static final int TEST_ROOM_NUMBER_2 = 13;
    private static final int TEST_ROOM_NUMBER_3 = 14;

    private static final int TEST_SEAT_NUMBER_1 = 20;

    private static final int PERIOD_1 = 1;
    private static final int PERIOD_2 = 2;
    private static final int PERIOD_3 = 3;

    private static final String TEACHERS_FIRST_NAME_1 = "Bob";
    private static final String TEACHERS_FIRST_NAME_2 = "Mike";
    private static final String TEACHERS_FIRST_NAME_3 = "Will";
    private static final String TEACHERS_LAST_NAME_1 = "Harris";
    private static final String TEACHERS_LAST_NAME_2 = "Smith";
    private static final String TEACHERS_LAST_NAME_3 = "Spike";

    private static final String SUBJECT_NAME_1 = "Math";
    private static final String SUBJECT_NAME_2 = "Programming";
    private static final String SUBJECT_NAME_3 = "Cybersecurity";

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
    private TableCreator tableCreator;

    @Autowired
    private TeachersTimetableService teacherTimetableService;

    @Autowired
    private LessonService lessonService;

    private TeacherDTO teacher_1;
    private TeacherDTO teacher_2;
    private TeacherDTO teacher_3;

    private LessonDTO lesson_1_date;
    private LessonDTO lesson_2_date;
    private LessonDTO lesson_3_date;

    private LessonDTO lesson_1_date_day_1;
    private LessonDTO lesson_2_date_day_1;

    private LessonDTO lesson_1_date_day_3;
    private LessonDTO lesson_2_date_day_3;

    private LessonDTO lesson_1_date_day_13;
    private LessonDTO lesson_2_date_day_13;

    private LessonDTO lesson_1_date_day_21;

    RoomDTO room_1;
    RoomDTO room_2;
    RoomDTO room_3;

    GroupDTO group_1;
    GroupDTO group_2;
    GroupDTO group_3;

    SubjectDTO subject_1;
    SubjectDTO subject_2;
    SubjectDTO subject_3;

    @BeforeAll
    void setup() {
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

        lesson_1_date = insertLesson(PERIOD_1, subject_1.getId(), room_1.getId(), group_1.getGroupId(), DATE,
                teacher_1.getId());
        lesson_2_date = insertLesson(PERIOD_2, subject_2.getId(), room_2.getId(), group_2.getGroupId(), DATE,
                teacher_1.getId());
        lesson_3_date = insertLesson(PERIOD_3, subject_1.getId(), room_2.getId(), group_3.getGroupId(), DATE,
                teacher_1.getId());

        lessonService.addLesson(new LessonDTO(PERIOD_1, group_2.getGroupId(), subject_1.getId(), room_2.getId(), DATE,
                teacher_2.getId()));
        lessonService.addLesson(new LessonDTO(PERIOD_2, group_3.getGroupId(), subject_2.getId(), room_3.getId(), DATE,
                teacher_3.getId()));
        lessonService.addLesson(new LessonDTO(PERIOD_3, group_1.getGroupId(), subject_3.getId(), room_2.getId(), DATE,
                teacher_2.getId()));

        lesson_1_date_day_1 = insertLesson(PERIOD_1, subject_3.getId(), room_2.getId(), group_1.getGroupId(),
                DATE_1_ADD_1_DAY, teacher_1.getId());
        lesson_2_date_day_1 = insertLesson(PERIOD_3, subject_3.getId(), room_3.getId(), group_2.getGroupId(),
                DATE_1_ADD_1_DAY, teacher_1.getId());

        lessonService.addLesson(new LessonDTO(PERIOD_2, group_3.getGroupId(), subject_2.getId(), room_2.getId(), DATE_1_ADD_1_DAY,
                teacher_2.getId()));

        lesson_1_date_day_3 = insertLesson(PERIOD_1, subject_1.getId(), room_1.getId(), group_1.getGroupId(),
                DATE_1_ADD_3_DAYS, teacher_1.getId());
        lesson_2_date_day_3 = insertLesson(PERIOD_2, subject_1.getId(), room_1.getId(), group_1.getGroupId(),
                DATE_1_ADD_3_DAYS, teacher_1.getId());

        lessonService.addLesson(new LessonDTO(PERIOD_3, group_1.getGroupId(), subject_2.getId(), room_3.getId(), DATE_1_ADD_3_DAYS,
                teacher_3.getId()));

        lesson_1_date_day_13 = insertLesson(PERIOD_1, subject_1.getId(), room_3.getId(), group_1.getGroupId(),
                DATE_1_ADD_13_DAYS, teacher_1.getId());
        lesson_2_date_day_13 = insertLesson(PERIOD_2, subject_2.getId(), room_2.getId(), group_2.getGroupId(),
                DATE_1_ADD_13_DAYS, teacher_1.getId());

        lessonService.addLesson(new LessonDTO(PERIOD_2, group_1.getGroupId(), subject_1.getId(), room_2.getId(), DATE_1_ADD_13_DAYS,
                teacher_3.getId()));
        lessonService.addLesson(new LessonDTO(PERIOD_3, group_1.getGroupId(), subject_2.getId(), room_2.getId(), DATE_1_ADD_13_DAYS,
                teacher_2.getId()));
        lessonService.addLesson(new LessonDTO(PERIOD_3, group_2.getGroupId(), subject_3.getId(), room_1.getId(), DATE_1_ADD_13_DAYS,
                teacher_3.getId()));

        lesson_1_date_day_21 = insertLesson(PERIOD_2, subject_2.getId(), room_2.getId(), group_1.getGroupId(),
                DATE_1_ADD_21_DAYS, teacher_1.getId());

        lessonService.addLesson(new LessonDTO(PERIOD_1, group_1.getGroupId(), subject_1.getId(), room_1.getId(), DATE_1_ADD_21_DAYS,
                teacher_2.getId()));
        lessonService.addLesson(new LessonDTO(PERIOD_3, group_1.getGroupId(), subject_3.getId(), room_3.getId(), DATE_1_ADD_21_DAYS,
                teacher_3.getId()));

        lessonService.addLesson(new LessonDTO(PERIOD_3, group_1.getGroupId(), subject_3.getId(), room_3.getId(), DATE_1_ADD_33_DAYS,
                teacher_2.getId()));
        lessonService.addLesson(new LessonDTO(PERIOD_1, group_1.getGroupId(), subject_1.getId(), room_1.getId(), DATE_1_ADD_33_DAYS,
                teacher_1.getId()));
        lessonService.addLesson(new LessonDTO(PERIOD_2, group_3.getGroupId(), subject_2.getId(), room_2.getId(), DATE_1_ADD_33_DAYS,
                teacher_1.getId()));

    }

    @Test
    void whenGetDayTimetableByDateAndTeacherIdShouldReturnListOfTimetables() {
        List<TimetableDTO> expectedTimetables = new ArrayList<>();
        expectedTimetables.add(new TimetableDTO(lesson_1_date.getId(), PERIOD_1, subject_1, group_1, room_1, teacher_1,
                getLocalDateFromString(DATE)));
        expectedTimetables.add(new TimetableDTO(lesson_2_date.getId(), PERIOD_2, subject_2, group_2, room_2,
                teacher_2, getLocalDateFromString(DATE)));
        expectedTimetables.add(new TimetableDTO(lesson_3_date.getId(), PERIOD_3, subject_3, group_3, room_2, teacher_1,
                getLocalDateFromString(DATE)));

        List<DayTimetable> dayTimetables = teacherTimetableService.getDayTimetable(DATE, teacher_1.getId());
        List<TimetableDTO> actualTimetables = dayTimetables.get(0).getTimetables();
        assertIterableEquals(expectedTimetables, actualTimetables);
    }

    @Test
    void whenGetMonthTimetableByDateAndTeacherIdShouldReturnListOfDayTimetables() {

        List<DayTimetable> expectedMonthTimetable = new ArrayList<>();

        List<TimetableDTO> expectedTimetables_DATE = new ArrayList<>();
        expectedTimetables_DATE.add(new TimetableDTO(lesson_1_date.getId(), PERIOD_1, subject_1, group_1, room_1, teacher_1,
                getLocalDateFromString(DATE)));
        expectedTimetables_DATE.add(new TimetableDTO(lesson_2_date.getId(), PERIOD_2, subject_2, group_2, room_2,teacher_1,
                getLocalDateFromString(DATE)));
        expectedTimetables_DATE.add(new TimetableDTO(lesson_3_date.getId(), PERIOD_3, subject_1, group_3,room_2,teacher_1,
                getLocalDateFromString(DATE)));
        DateInfo dateInfo_DATE = PersonalTimetable.parseInstantToDateInfo(expectedTimetables_DATE.get(0));
        expectedMonthTimetable.add(new DayTimetable(expectedTimetables_DATE, dateInfo_DATE));

        List<TimetableDTO> expectedTimetables_DATE_ADD_1_DAY = new ArrayList<>();
        expectedTimetables_DATE_ADD_1_DAY.add(new TimetableDTO(lesson_1_date_day_1.getId(), PERIOD_1, subject_3,group_1,room_2,
                teacher_1, getLocalDateFromString(DATE_1_ADD_1_DAY)));
        expectedTimetables_DATE_ADD_1_DAY.add(new TimetableDTO(lesson_2_date_day_1.getId(), PERIOD_3, subject_3,group_2,room_3,
                teacher_1, getLocalDateFromString(DATE_1_ADD_1_DAY)));
        DateInfo dateInfo_DATE_ADD_1_DAY = PersonalTimetable.parseInstantToDateInfo(expectedTimetables_DATE.get(0));
        expectedMonthTimetable.add(new DayTimetable(expectedTimetables_DATE_ADD_1_DAY, dateInfo_DATE_ADD_1_DAY));

        List<TimetableDTO> expectedTimetables_DATE_ADD_3_DAYS = new ArrayList<>();
        expectedTimetables_DATE_ADD_3_DAYS.add(new TimetableDTO(lesson_1_date_day_3.getId(), PERIOD_1, subject_1,group_1,room_1,
                teacher_1, getLocalDateFromString(DATE_1_ADD_3_DAYS)));
        expectedTimetables_DATE_ADD_3_DAYS.add(new TimetableDTO(lesson_2_date_day_3.getId(), PERIOD_2, subject_1,group_1,room_1,
                teacher_1, getLocalDateFromString(DATE_1_ADD_3_DAYS)));
        DateInfo dateInfo_DATE_ADD_3_DAY = PersonalTimetable.parseInstantToDateInfo(expectedTimetables_DATE.get(0));
        expectedMonthTimetable.add(new DayTimetable(expectedTimetables_DATE_ADD_3_DAYS, dateInfo_DATE_ADD_3_DAY));

        List<TimetableDTO> expectedTimetables_DATE_ADD_13_DAYS = new ArrayList<>();
        expectedTimetables_DATE_ADD_13_DAYS.add(new TimetableDTO(lesson_1_date_day_13.getId(), PERIOD_1, subject_1,group_1,room_3,
                teacher_1, getLocalDateFromString(DATE_1_ADD_13_DAYS)));
        expectedTimetables_DATE_ADD_13_DAYS.add(new TimetableDTO(lesson_2_date_day_13.getId(), PERIOD_2, subject_2,group_2,room_2,
                teacher_1, getLocalDateFromString(DATE_1_ADD_13_DAYS)));
        DateInfo dateInfo_DATE_ADD_13_DAY = PersonalTimetable.parseInstantToDateInfo(expectedTimetables_DATE.get(0));
        expectedMonthTimetable.add(new DayTimetable(expectedTimetables_DATE_ADD_13_DAYS, dateInfo_DATE_ADD_13_DAY));

        List<TimetableDTO> expectedTimetables_DATE_ADD_21_DAYS = new ArrayList<>();
        expectedTimetables_DATE_ADD_21_DAYS.add(new TimetableDTO(lesson_1_date_day_21.getId(), PERIOD_2, subject_2,group_1,room_2,
                teacher_1, getLocalDateFromString(DATE_1_ADD_21_DAYS)));
        DateInfo dateInfo_DATE_ADD_21_DAY = PersonalTimetable.parseInstantToDateInfo(expectedTimetables_DATE.get(0));
        expectedMonthTimetable.add(new DayTimetable(expectedTimetables_DATE_ADD_21_DAYS, dateInfo_DATE_ADD_21_DAY));

        List<DayTimetable> actualMonthTimetables = teacherTimetableService.getMonthTimetable(DATE, teacher_1.getId());
        assertEquals(expectedMonthTimetable.size(), actualMonthTimetables.size());

        for (int i = 0; i < expectedMonthTimetable.size(); i++) {
            assertIterableEquals(expectedMonthTimetable.get(i).getTimetables(),
                    actualMonthTimetables.get(i).getTimetables());
        }
    }

    private LocalDate getLocalDateFromString(String date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, dateTimeFormatter);
    }

    private LessonDTO insertLesson(int period, int subjectId, int roomId, long groupId, String date, long teacherId) {
        lessonService.addLesson(new LessonDTO(period, groupId, subjectId, roomId, date, teacherId));
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
        roomService.addRoom(roomNumber, seatNumber);
        return roomService.getRoomByRoomNumber(roomNumber);
    }

    private TeacherDTO insertTeacher(String firstName, String lastName) {
        teacherService.addTeacher(new TeacherDTO(firstName, lastName));
        return teacherService.getTeacherByName(firstName, lastName);
    }

}
