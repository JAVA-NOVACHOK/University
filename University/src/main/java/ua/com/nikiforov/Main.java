package ua.com.nikiforov;

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
import ua.com.nikiforov.services.timetables.TeachersTimetableService;

public class Main {

    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(UniversityConfig.class);
//        GroupService groupService = context.getBean(GroupService.class);
//        groupService.createTable();
//        groupService.addGroup("BB-11");
//        System.out.println(groupService.getGroupById(1).getGroupName());
//        TableCreator tableCreator = context.getBean(TableCreator.class);
//        tableCreator.createTables();
        RoomService roomService = context.getBean(RoomService.class);
        roomService.addRoom(221);
        GroupService groupService = context.getBean(GroupService.class);
        groupService.addGroup("AA-10");
        SubjectService subjectService = context.getBean(SubjectService.class);
        subjectService.addSubject("math");
        StudentsService studentDAO = context.getBean(StudentsService.class);
        studentDAO.addStudent("Olena", "Nikiforova", 1);
        TeachersService teachersService = context.getBean(TeachersService.class);
        teachersService.addTeacher("Artem", "Nikiforov");
//        TeachersTimetableService teachersTimetableService = context.getBean(TeachersTimetableService.class);
//        Date date = new  Date();
//        Instant time = date.toInstant();
//        teachersTimetableService.addTimetable(lessonId, teacherId, time)e(1, 2, time);
        
        ((AnnotationConfigApplicationContext) context).close();

    }

}
