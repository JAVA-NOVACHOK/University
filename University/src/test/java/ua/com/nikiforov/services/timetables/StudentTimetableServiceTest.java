package ua.com.nikiforov.services.timetables;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ua.com.nikiforov.dto.*;
import ua.com.nikiforov.helper.SetupTestHelper;
import ua.com.nikiforov.services.group.GroupService;
import ua.com.nikiforov.services.lesson.LessonService;
import ua.com.nikiforov.services.persons.StudentsService;
import ua.com.nikiforov.services.persons.TeacherService;
import ua.com.nikiforov.services.room.RoomService;
import ua.com.nikiforov.services.subject.SubjectService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static ua.com.nikiforov.services.timetables.PersonalTimetable.getLocalDate;


class StudentTimetableServiceTest extends SetupTestHelper {

    private static final String TEST_GROUP_NAME_1 = "AA-12";
    private static final String TEST_GROUP_NAME_2 = "AA-13";

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
    private StudentTimetableService studentTimetableService;

    @Autowired
    private LessonService lessonService;

    private TeacherDTO teacher_1;
    private TeacherDTO teacher_2;
    private TeacherDTO teacher_3;

    private StudentDTO student;

    private LessonDTO lesson_1_date;
    private LessonDTO lesson_2_date;
    private LessonDTO lesson_3_date;


    RoomDTO room_1;
    RoomDTO room_2;
    RoomDTO room_3;

    GroupDTO group_1;
    GroupDTO group_2;

    SubjectDTO subject_1;
    SubjectDTO subject_2;
    SubjectDTO subject_3;

    private LessonDTO lesson_1_date_day_1;
    private LessonDTO lesson_2_date_day_1;
    private LessonDTO lesson_3_date_day_1;

    private LessonDTO lesson_1_date_day_3;
    private LessonDTO lesson_2_date_day_3;
    private LessonDTO lesson_3_date_day_3;

    private LessonDTO lesson_1_date_day_13;
    private LessonDTO lesson_2_date_day_13;
    private LessonDTO lesson_3_date_day_13;

    private LessonDTO lesson_1_date_day_21;
    private LessonDTO lesson_2_date_day_21;
    private LessonDTO lesson_3_date_day_21;

    @BeforeAll
    void setup() {
        room_1 = insertRoom(TEST_ROOM_NUMBER_1, TEST_SEAT_NUMBER_1);
        room_2 = insertRoom(TEST_ROOM_NUMBER_2, TEST_SEAT_NUMBER_1);
        room_3 = insertRoom(TEST_ROOM_NUMBER_3, TEST_SEAT_NUMBER_1);

        group_1 = insertGroup(TEST_GROUP_NAME_1);
        group_2 = insertGroup(TEST_GROUP_NAME_2);

        subject_1 = insertSubject(SUBJECT_NAME_1);
        subject_2 = insertSubject(SUBJECT_NAME_2);
        subject_3 = insertSubject(SUBJECT_NAME_3);

        teacher_1 = insertTeacher(TEACHERS_FIRST_NAME_1, TEACHERS_LAST_NAME_1);
        teacher_2 = insertTeacher(TEACHERS_FIRST_NAME_2, TEACHERS_LAST_NAME_2);
        teacher_3 = insertTeacher(TEACHERS_FIRST_NAME_3, TEACHERS_LAST_NAME_3);

        student = insertStudent(STUDENTS_FIRST_NAME_1, STUDENTS_LAST_NAME_1, group_1.getGroupId());
        insertStudent(STUDENTS_FIRST_NAME_2, STUDENTS_LAST_NAME_2, group_1.getGroupId());
        insertStudent(STUDENTS_FIRST_NAME_3, STUDENTS_LAST_NAME_3, group_1.getGroupId());

        lesson_1_date = insertLesson(PERIOD_1, subject_1.getId(), room_1.getId(), group_1.getGroupId(), DATE,
                teacher_1.getId());
        lesson_2_date = insertLesson(PERIOD_2, subject_2.getId(), room_2.getId(), group_1.getGroupId(), DATE,
                teacher_2.getId());
        lesson_3_date = insertLesson(PERIOD_3, subject_3.getId(), room_3.getId(), group_1.getGroupId(), DATE,
                teacher_3.getId());

        lessonService.addLesson(new LessonDTO(PERIOD_1, group_2.getGroupId(), subject_1.getId(), room_2.getId(), DATE,
                teacher_1.getId()));
        lessonService.addLesson(new LessonDTO(PERIOD_2, group_2.getGroupId(), subject_2.getId(), room_3.getId(), DATE,
                teacher_2.getId()));
        lessonService.addLesson(new LessonDTO(PERIOD_3, group_2.getGroupId(), subject_3.getId(), room_2.getId(), DATE,
                teacher_3.getId()));

        lesson_1_date_day_1 = insertLesson(PERIOD_1, subject_3.getId(), room_2.getId(), group_1.getGroupId(),
                DATE_1_ADD_1_DAY, teacher_1.getId());
        lesson_2_date_day_1 = insertLesson(PERIOD_2, subject_2.getId(), room_2.getId(), group_1.getGroupId(),
                DATE_1_ADD_1_DAY, teacher_2.getId());
        lesson_3_date_day_1 = insertLesson(PERIOD_3, subject_3.getId(), room_3.getId(), group_1.getGroupId(),
                DATE_1_ADD_1_DAY, teacher_3.getId());

        lesson_1_date_day_3 = insertLesson(PERIOD_1, subject_1.getId(), room_1.getId(), group_1.getGroupId(),
                DATE_1_ADD_3_DAYS, teacher_1.getId());
        lesson_2_date_day_3 = insertLesson(PERIOD_2, subject_1.getId(), room_1.getId(), group_1.getGroupId(),
                DATE_1_ADD_3_DAYS, teacher_1.getId());
        lesson_3_date_day_3 = insertLesson(PERIOD_3, subject_2.getId(), room_3.getId(), group_1.getGroupId(),
                DATE_1_ADD_3_DAYS, teacher_3.getId());

        lesson_1_date_day_13 = insertLesson(PERIOD_1, subject_3.getId(), room_3.getId(), group_1.getGroupId(),
                DATE_1_ADD_13_DAYS, teacher_3.getId());
        lesson_2_date_day_13 = insertLesson(PERIOD_2, subject_1.getId(), room_2.getId(), group_1.getGroupId(),
                DATE_1_ADD_13_DAYS, teacher_2.getId());
        lesson_3_date_day_13 = insertLesson(PERIOD_3, subject_2.getId(), room_2.getId(), group_1.getGroupId(),
                DATE_1_ADD_13_DAYS, teacher_2.getId());

        lessonService.addLesson(new LessonDTO(PERIOD_1, group_2.getGroupId(), subject_1.getId(), room_2.getId(), DATE_1_ADD_13_DAYS,
                teacher_1.getId()));
        lessonService.addLesson(new LessonDTO(PERIOD_2, group_2.getGroupId(), subject_2.getId(), room_3.getId(), DATE_1_ADD_13_DAYS,
                teacher_2.getId()));
        lessonService.addLesson(new LessonDTO(PERIOD_3, group_2.getGroupId(), subject_3.getId(), room_1.getId(), DATE_1_ADD_13_DAYS,
                teacher_3.getId()));

        lesson_1_date_day_21 = insertLesson(PERIOD_1, subject_1.getId(), room_1.getId(), group_1.getGroupId(),
                DATE_1_ADD_21_DAYS, teacher_1.getId());
        lesson_2_date_day_21 = insertLesson(PERIOD_2, subject_2.getId(), room_2.getId(), group_1.getGroupId(),
                DATE_1_ADD_21_DAYS, teacher_2.getId());
        lesson_3_date_day_21 = insertLesson(PERIOD_3, subject_3.getId(), room_3.getId(), group_1.getGroupId(),
                DATE_1_ADD_21_DAYS, teacher_3.getId());

        lessonService.addLesson(new LessonDTO(PERIOD_1, group_1.getGroupId(), subject_1.getId(), room_1.getId(), DATE_1_ADD_33_DAYS,
                teacher_1.getId()));
        lessonService.addLesson(new LessonDTO(PERIOD_2, group_1.getGroupId(), subject_2.getId(), room_2.getId(), DATE_1_ADD_33_DAYS,
                teacher_2.getId()));
        lessonService.addLesson(new LessonDTO(PERIOD_3, group_1.getGroupId(), subject_3.getId(), room_3.getId(), DATE_1_ADD_33_DAYS,
                teacher_3.getId()));
    }

    @Test
    void whenGetDayTimetableByDateAndGroupIdShouldReturnListOfTimetables() {
        List<TimetableDTO> expectedTimetables = new ArrayList<>();
        expectedTimetables.add(new TimetableDTO(lesson_1_date.getId(), PERIOD_1, subject_1, group_1, room_1, teacher_1,
                getLocalDate(DATE)));
        expectedTimetables.add(new TimetableDTO(lesson_2_date.getId(), PERIOD_2, subject_2, group_1, room_2, teacher_2,
                getLocalDate(DATE)));
        expectedTimetables.add(new TimetableDTO(lesson_3_date.getId(), PERIOD_3, subject_3, group_1, room_3, teacher_3,
                getLocalDate(DATE)));

        List<DayTimetable> dayTimetables = studentTimetableService.getDayTimetable(DATE, student.getGroupId());
        List<TimetableDTO> actualTimetable = dayTimetables.get(0).getTimetables();
        assertIterableEquals(expectedTimetables, actualTimetable);
    }

    @Test
    void whenGetMonthTimetableByDateAndStudentGroupIdShouldReturnListOfTimetables() {

        List<DayTimetable> expectedMonthTimetable = new ArrayList<>();

        List<TimetableDTO> expectedTimetables_DATE = new ArrayList<>();
        expectedTimetables_DATE.add(new TimetableDTO(lesson_1_date.getId(), PERIOD_1, subject_1, group_1, room_1, teacher_1,
                getLocalDate(DATE)));
        expectedTimetables_DATE.add(new TimetableDTO(lesson_2_date.getId(), PERIOD_2, subject_2, group_1, room_2, teacher_2,
                getLocalDate(DATE)));
        expectedTimetables_DATE.add(new TimetableDTO(lesson_3_date.getId(), PERIOD_3, subject_3, group_1, room_3, teacher_3,
                getLocalDate(DATE)));
        DateInfo dateInfo_DATE = PersonalTimetable.parseInstantToDateInfo(getLocalDate(DATE));
        expectedMonthTimetable.add(new DayTimetable(expectedTimetables_DATE, dateInfo_DATE));

        List<TimetableDTO> expectedTimetables_DATE_ADD_1_DAY = new ArrayList<>();
        expectedTimetables_DATE_ADD_1_DAY.add(new TimetableDTO(lesson_1_date_day_1.getId(), PERIOD_1, subject_3, group_1, room_2,
                teacher_1, getLocalDate(DATE_1_ADD_1_DAY)));
        expectedTimetables_DATE_ADD_1_DAY.add(new TimetableDTO(lesson_2_date_day_1.getId(), PERIOD_2, subject_2, group_1, room_2,
                teacher_2, getLocalDate(DATE_1_ADD_1_DAY)));
        expectedTimetables_DATE_ADD_1_DAY.add(new TimetableDTO(lesson_3_date_day_1.getId(), PERIOD_3, subject_3, group_1, room_3,
                teacher_3, getLocalDate(DATE_1_ADD_1_DAY)));
        DateInfo dateInfo_DATE_ADD_1_DAY = PersonalTimetable.parseInstantToDateInfo(getLocalDate(DATE_1_ADD_1_DAY));
        expectedMonthTimetable.add(new DayTimetable(expectedTimetables_DATE_ADD_1_DAY, dateInfo_DATE_ADD_1_DAY));

        List<TimetableDTO> expectedTimetables_DATE_ADD_3_DAYS = new ArrayList<>();
        expectedTimetables_DATE_ADD_3_DAYS.add(new TimetableDTO(lesson_1_date_day_3.getId(), PERIOD_1, subject_1, group_1, room_1,
                teacher_1, getLocalDate(DATE_1_ADD_3_DAYS)));
        expectedTimetables_DATE_ADD_3_DAYS.add(new TimetableDTO(lesson_2_date_day_3.getId(), PERIOD_2, subject_1,group_1,room_1,
                teacher_1, getLocalDate(DATE_1_ADD_3_DAYS)));
        expectedTimetables_DATE_ADD_3_DAYS.add(new TimetableDTO(lesson_3_date_day_3.getId(), PERIOD_3, subject_2,group_1,room_3,
                teacher_3, getLocalDate(DATE_1_ADD_3_DAYS)));
        DateInfo dateInfo_DATE_ADD_3_DAY = PersonalTimetable.parseInstantToDateInfo(getLocalDate(DATE_1_ADD_3_DAYS));
        expectedMonthTimetable.add(new DayTimetable(expectedTimetables_DATE_ADD_3_DAYS, dateInfo_DATE_ADD_3_DAY));

        List<TimetableDTO> expectedTimetables_DATE_ADD_13_DAYS = new ArrayList<>();
        expectedTimetables_DATE_ADD_13_DAYS.add(new TimetableDTO(lesson_1_date_day_13.getId(), PERIOD_1, subject_3,group_1,room_3,
                teacher_3, getLocalDate(DATE_1_ADD_13_DAYS)));
        expectedTimetables_DATE_ADD_13_DAYS.add(new TimetableDTO(lesson_2_date_day_13.getId(), PERIOD_2, subject_1,group_1,room_2,
                teacher_2, getLocalDate(DATE_1_ADD_13_DAYS)));
        expectedTimetables_DATE_ADD_13_DAYS.add(new TimetableDTO(lesson_3_date_day_13.getId(), PERIOD_3, subject_2,group_1,room_2,
                teacher_2, getLocalDate(DATE_1_ADD_13_DAYS)));
        DateInfo dateInfo_DATE_ADD_13_DAY = PersonalTimetable.parseInstantToDateInfo(getLocalDate(DATE_1_ADD_13_DAYS));
        expectedMonthTimetable.add(new DayTimetable(expectedTimetables_DATE_ADD_13_DAYS, dateInfo_DATE_ADD_13_DAY));

        List<TimetableDTO> expectedTimetables_DATE_ADD_21_DAYS = new ArrayList<>();
        expectedTimetables_DATE_ADD_21_DAYS.add(new TimetableDTO(lesson_1_date_day_21.getId(), PERIOD_1, subject_1,group_1,room_1,
                teacher_1, getLocalDate(DATE_1_ADD_21_DAYS)));
        expectedTimetables_DATE_ADD_21_DAYS.add(new TimetableDTO(lesson_2_date_day_21.getId(), PERIOD_2, subject_2,group_1,room_2,
               teacher_2, getLocalDate(DATE_1_ADD_21_DAYS)));
        expectedTimetables_DATE_ADD_21_DAYS.add(new TimetableDTO(lesson_3_date_day_21.getId(), PERIOD_3, subject_3,group_1,room_3,
                teacher_3, getLocalDate(DATE_1_ADD_21_DAYS)));
        DateInfo dateInfo_DATE_ADD_21_DAY = PersonalTimetable.parseInstantToDateInfo(getLocalDate(DATE_1_ADD_21_DAYS));
        expectedMonthTimetable.add(new DayTimetable(expectedTimetables_DATE_ADD_21_DAYS, dateInfo_DATE_ADD_21_DAY));

        List<DayTimetable> actualMonthTimetables = studentTimetableService.getMonthTimetable(DATE,
                student.getGroupId());
        assertEquals(expectedMonthTimetable.size(), actualMonthTimetables.size());

        for (int i = 0; i < expectedMonthTimetable.size(); i++) {
            assertIterableEquals(expectedMonthTimetable.get(i).getTimetables(),
                    actualMonthTimetables.get(i).getTimetables());
        }
    }

}
