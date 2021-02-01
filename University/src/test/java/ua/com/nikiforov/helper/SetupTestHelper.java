package ua.com.nikiforov.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import ua.com.nikiforov.dto.*;
import ua.com.nikiforov.services.group.GroupService;
import ua.com.nikiforov.services.lesson.LessonService;
import ua.com.nikiforov.services.persons.StudentsService;
import ua.com.nikiforov.services.persons.TeacherService;
import ua.com.nikiforov.services.room.RoomService;
import ua.com.nikiforov.services.subject.SubjectService;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@RunWith(SpringRunner.class)
@DBRider
@Transactional
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class SetupTestHelper {

    protected static final String FIRST_NAME_1 = "Tom";
    protected static final String FIRST_NAME_2 = "Bill";
    protected static final String FIRST_NAME_3 = "Jack";
    protected static final String FIRST_NAME_4 = "Jay";

    protected static final String LAST_NAME_1 = "Hanks";
    protected static final String LAST_NAME_2 = "Clinton";
    protected static final String LAST_NAME_3 = "Sparrow";
    protected static final String LAST_NAME_4 = "Prichet";

    protected static final String TEST_GROUP_NAME_1 = "AA-12";
    protected static final String TEST_GROUP_NAME_2 = "AA-13";
    protected static final String TEST_GROUP_NAME_3 = "AA-14";
    protected static final String TEST_GROUP_NAME_4 = "AA-15";
    protected static final String TEST_WRONG_PATTERN_NAME = "AA-111";

    protected static final String ADD_ONE_ROOM_XML = "rooms/one_room.xml";
    protected static final String ADD_ONE_ROOM_ID_XML = "rooms/one_room_id.xml";
    protected static final String ADD_THREE_ROOMS_XML = "rooms/three_rooms.xml";
    protected static final String RESET_ROOM_ID = "datasets/rooms/scripts/reset.sql";

    protected static final String ADD_ONE_GROUP_XML = "groups/one_group.xml";
    protected static final String ADD_THREE_GROUPS_XML = "groups/three_groups.xml";
    protected static final String RESET_GROUP_ID = "datasets/groups/scripts/reset.sql";

    protected static final String ADD_ONE_STUDENT_XML = "students/one_student.xml";
    protected static final String ADD_THREE_STUDENTS_XML = "students/three_students.xml";
    protected static final String RESET_STUDENT_ID = "datasets/students/scripts/reset.sql";



    protected static final int ID_1 = 1;

    protected static final String STATUS = "$.status";
    protected static final String ERRORS = "$.errors";
    protected static final String JSON_ROOT = "$";

    protected static final long INVALID_ID = 100500l;

    @Autowired
    protected RoomService roomService;

    @Autowired
    protected GroupService groupService;

    @Autowired
    protected SubjectService subjectService;

    @Autowired
    protected TeacherService teacherService;

    @Autowired
    protected StudentsService studentsService;

    @Autowired
    protected LessonService lessonService;

    public GroupDTO insertGroup(String groupName) {
        return groupService.addGroup(new GroupDTO(groupName));
    }

    public StudentDTO insertStudent(String firstName, String lastName, long groupName) {
        return studentsService.addStudent(new StudentDTO(firstName, lastName, groupName));
    }

    public SubjectDTO insertSubject(String subjectName) {
        return subjectService.addSubject(new SubjectDTO(subjectName));
    }

    public RoomDTO insertRoom(int roomNumber, int seatNumber) {
        return roomService.addRoom(new RoomDTO(roomNumber, seatNumber));
    }

    public TeacherDTO insertTeacher(String firstName, String lastName) {
        return teacherService.addTeacher(new TeacherDTO(firstName, lastName));
    }

    public LessonDTO insertLesson(int period, int subjectId, int roomId, long groupId, String date, long teacherId) {
        return lessonService.addLesson(new LessonDTO(period, groupId, subjectId, roomId, date, teacherId));
    }

    public LocalDate getLocalDateFromString(String date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, dateTimeFormatter);
    }

    public String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
