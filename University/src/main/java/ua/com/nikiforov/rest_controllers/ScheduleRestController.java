package ua.com.nikiforov.rest_controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.com.nikiforov.services.timetables.DayTimetable;
import ua.com.nikiforov.services.timetables.StudentTimetableService;
import ua.com.nikiforov.services.timetables.TeachersTimetableService;

import java.util.List;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleRestController {

    private TeachersTimetableService teachersTimetableService;
    private StudentTimetableService studentTimetableService;

    @Autowired
    public ScheduleRestController(TeachersTimetableService teachersTimetableService, StudentTimetableService studentTimetableService) {
        this.teachersTimetableService = teachersTimetableService;
        this.studentTimetableService = studentTimetableService;
    }

    @GetMapping("/teacher/{teacherId}/day/{date}")
    public List<DayTimetable> getTeachersDayTimetable(@PathVariable("date") String date,
                                                      @PathVariable("teacherId") long teacherId) {
        return teachersTimetableService.getDayTimetable(date, teacherId);
    }

    @GetMapping("/teacher/{teacherId}/month/{date}")
    public List<DayTimetable> getTeachersMonthTimetable(@PathVariable("date") String date,
                                                        @PathVariable("teacherId") long teacherId) {
        return teachersTimetableService.getMonthTimetable(date, teacherId);
    }

    @GetMapping("/student/{groupId}/day/{date}")
    public List<DayTimetable> getStudentsDayTimetable(@PathVariable("date") String date,
                                                      @PathVariable("groupId") long groupId) {
        return studentTimetableService.getDayTimetable(date, groupId);
    }

    @GetMapping("/student/{groupId}/month/{date}")
    public List<DayTimetable> getStudentsMonthTimetable(@PathVariable("date") String date,
                                                        @PathVariable("groupId") long groupId) {
        return studentTimetableService.getMonthTimetable(date, groupId);
    }
}
