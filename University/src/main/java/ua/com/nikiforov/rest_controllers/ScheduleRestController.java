package ua.com.nikiforov.rest_controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

    private static final String DATE_PARAM_VALUE = "Date when to look up for schedule";
    private static final String TEACHER_PARAM_VALUE = "Teacher ID who to look up for schedule";
    private static final String GROUP_PARAM_VALUE = "Group ID where to look up for schedule";

    private TeachersTimetableService teachersTimetableService;
    private StudentTimetableService studentTimetableService;

    @Autowired
    public ScheduleRestController(TeachersTimetableService teachersTimetableService, StudentTimetableService studentTimetableService) {
        this.teachersTimetableService = teachersTimetableService;
        this.studentTimetableService = studentTimetableService;
    }

    @GetMapping("/teacher/{teacherId}/day/{date}")
    @ApiOperation(
            value = "Retrieves teacher's timetable for one day for indicated date",
            notes = "Provide date and teacher ID for teacher's one day schedule",
            response = DayTimetable.class
    )
    public List<DayTimetable> getTeachersDayTimetable(
            @ApiParam(value = DATE_PARAM_VALUE, required = true) @PathVariable String date,
            @ApiParam(value = TEACHER_PARAM_VALUE, required = true) @PathVariable long teacherId) {
        return teachersTimetableService.getDayTimetable(date, teacherId);
    }

    @GetMapping("/teacher/{teacherId}/month/{date}")
    @ApiOperation(
            value = "Retrieves teacher's schedule for one month from indicated date",
            notes = "Provide date and teacher ID to look up for teacher's month schedule",
            response = DayTimetable.class
    )
    public List<DayTimetable> getTeachersMonthTimetable(
            @ApiParam(value = DATE_PARAM_VALUE, required = true) @PathVariable String date,
            @ApiParam(value = TEACHER_PARAM_VALUE, required = true) @PathVariable long teacherId) {
        return teachersTimetableService.getMonthTimetable(date, teacherId);
    }

    @GetMapping("/student/{groupId}/day/{date}")
    @ApiOperation(
            value = "Retrieves students schedule for one day from indicated date",
            notes = "Provide date and group ID to look up for students's one day schedule",
            response = DayTimetable.class
    )
    public List<DayTimetable> getStudentsDayTimetable(
            @ApiParam(value = DATE_PARAM_VALUE, required = true) @PathVariable String date,
            @ApiParam(value = GROUP_PARAM_VALUE, required = true) @PathVariable long groupId) {
        return studentTimetableService.getDayTimetable(date, groupId);
    }

    @GetMapping("/student/{groupId}/month/{date}")
    @ApiOperation(
            value = "Retrieves students schedule for month from indicated date",
            notes = "Provide date and group ID to look up for students's month schedule",
            response = DayTimetable.class
    )
    public List<DayTimetable> getStudentsMonthTimetable(
            @ApiParam(value = DATE_PARAM_VALUE, required = true) @PathVariable String date,
            @ApiParam(value = GROUP_PARAM_VALUE, required = true) @PathVariable long groupId) {
        return studentTimetableService.getMonthTimetable(date, groupId);
    }
}
