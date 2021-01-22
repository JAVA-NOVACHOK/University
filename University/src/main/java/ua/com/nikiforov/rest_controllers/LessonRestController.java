package ua.com.nikiforov.rest_controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.com.nikiforov.dto.LessonDTO;
import ua.com.nikiforov.models.lesson.Lesson;
import ua.com.nikiforov.services.lesson.LessonService;
import ua.com.nikiforov.services.timetables.DayTimetable;
import ua.com.nikiforov.services.timetables.StudentTimetableService;
import ua.com.nikiforov.services.timetables.TeachersTimetableService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/lessons")
public class LessonRestController {

    private LessonService lessonService;
    private TeachersTimetableService teachersTimetableService;
    private StudentTimetableService studentTimetableService;

    @Autowired
    public LessonRestController(LessonService lessonService,
                                TeachersTimetableService teachersTimetableService,
                                StudentTimetableService studentTimetableService) {
        this.lessonService = lessonService;
        this.teachersTimetableService = teachersTimetableService;
        this.studentTimetableService = studentTimetableService;
    }

    @GetMapping
    public List<LessonDTO> allLessons() {
        return lessonService.getAllLessons();
    }

    @GetMapping("/{lessonId}")
    public LessonDTO getLesson(@PathVariable("lessonId") long lessonId) {
        return lessonService.getLessonById(lessonId);
    }

    @PostMapping
    public LessonDTO addLesson(@Valid @RequestBody LessonDTO lessonDTO) {
        return lessonService.addLesson(lessonDTO);
    }

    @PutMapping("/{lessonId}")
    public LessonDTO updateLesson(@PathVariable("lessonId") long lessonId, @RequestBody LessonDTO lessonDTO) {
        lessonDTO.setId(lessonId);
        return lessonService.updateLesson(lessonDTO);
    }

    @DeleteMapping("/{lessonId}")
    public void deleteLesson(@PathVariable("lessonId") long lessonId) {
        lessonService.deleteLessonById(lessonId);
    }

    @GetMapping("/{date}/teacher_day/{teacherId}")
    public List<DayTimetable> getTeachersDayTimetable(@PathVariable("date") String date,
                                                      @PathVariable("teacherId") long teacherId) {
        return teachersTimetableService.getDayTimetable(date, teacherId);
    }

    @GetMapping("/{date}/teacher_month/{teacherId}")
    public List<DayTimetable> getTeachersMonthTimetable(@PathVariable("date") String date,
                                                        @PathVariable("teacherId") long teacherId) {
        return teachersTimetableService.getMonthTimetable(date, teacherId);
    }

    @GetMapping("/{date}/student_day/{groupId}")
    public List<DayTimetable> getStudentsDayTimetable(@PathVariable("date") String date,
                                                      @PathVariable("groupId") long groupId) {
        return studentTimetableService.getDayTimetable(date, groupId);

    }

    @GetMapping("/{date}/student_month/{groupId}")
    public List<DayTimetable> getStudentsMonthTimetable(@PathVariable("date") String date,
                                                        @PathVariable("groupId") long groupId) {
        return studentTimetableService.getMonthTimetable(date, groupId);
    }
}