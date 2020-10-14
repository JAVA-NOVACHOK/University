package ua.com.nikiforov;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ua.com.nikiforov.config.UniversityConfig;
import ua.com.nikiforov.dao.persons.StudentDAO;
import ua.com.nikiforov.dao.tablecreator.TableCreator;
import ua.com.nikiforov.services.group.GroupService;
import ua.com.nikiforov.services.persons.StudentsService;
import ua.com.nikiforov.services.persons.TeacherServiceImpl;
import ua.com.nikiforov.services.persons.TeachersService;
import ua.com.nikiforov.services.room.RoomService;
import ua.com.nikiforov.services.subject.SubjectService;
import ua.com.nikiforov.services.timetables.Period;
import ua.com.nikiforov.services.timetables.StudentTimetableService;
import ua.com.nikiforov.services.timetables.TeachersTimetableService;

public class Main {

    public static void main(String[] args) {

        
        
        
        ApplicationContext context = new AnnotationConfigApplicationContext(UniversityConfig.class);
        StudentTimetableService sts = context.getBean(StudentTimetableService.class);
        String date = "2020-08-12";
        sts.addTimetable(1, 1, date, Period.FIFTH);
        System.out.println(sts.getTimetableById(1).getTime());
//        Time t = Time.valueOf("12:00:00");
//        System.out.println(t);
//        GroupService groupService = context.getBean(GroupService.class);
//        groupService.createTable();
//        groupService.addGroup("BB-11");
//        System.out.println(groupService.getGroupById(1).getGroupName());
        TableCreator tableCreator = context.getBean(TableCreator.class);
        tableCreator.createTables();
//        RoomService roomService = context.getBean(RoomService.class);
//        roomService.addRoom(221);
//        GroupService groupService = context.getBean(GroupService.class);
//        groupService.addGroup("AA-10");
//        System.out.println(groupService.getGroupByName("AA-10").getId());
//        SubjectService subjectService = context.getBean(SubjectService.class);
//        subjectService.addSubject("math");
//        subjectService.addSubject("math");
//        StudentsService studentDAO = context.getBean(StudentsService.class);
//        studentDAO.addStudent("Olena", "Nikiforova", 1);
//        TeachersService teachersService = context.getBean(TeachersService.class);
//        teachersService.addTeacher("Artem", "Nikiforov");
//        TeachersTimetableService teachersTimetableService = context.getBean(TeachersTimetableService.class);
//        Date date = new  Date();
//        Instant time = date.toInstant();
//        teachersTimetableService.addTimetable(lessonId, teacherId, time)e(1, 2, time);
        
        ((AnnotationConfigApplicationContext) context).close();

    }

}
