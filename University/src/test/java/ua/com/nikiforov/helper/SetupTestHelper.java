package ua.com.nikiforov.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import ua.com.nikiforov.dto.*;
import ua.com.nikiforov.services.group.GroupService;
import ua.com.nikiforov.services.lesson.LessonService;
import ua.com.nikiforov.services.persons.StudentsService;
import ua.com.nikiforov.services.persons.TeacherService;
import ua.com.nikiforov.services.room.RoomService;
import ua.com.nikiforov.services.subject.SubjectService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SetupTestHelper {

    public static final String STATUS = "$.status";
    public static final String ERRORS = "$.errors";
    public static final String JSON_ROOT = "$";

    public static final long INVALID_ID = 100500l;

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
