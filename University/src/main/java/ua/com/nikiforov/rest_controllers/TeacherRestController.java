package ua.com.nikiforov.rest_controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.com.nikiforov.dto.TeacherDTO;
import ua.com.nikiforov.services.persons.TeacherService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/teachers")
public class TeacherRestController {

    private TeacherService teacherService;

    @Autowired
    public TeacherRestController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping
    public List<TeacherDTO> getTeachers() {
        return teacherService.getAllTeachers();
    }

    @GetMapping("/{teacherId}")
    public TeacherDTO getTeacher(@PathVariable long teacherId) {
        return teacherService.getTeacherById(teacherId);
    }

    @PostMapping
    public TeacherDTO addTeacher(@Valid @RequestBody TeacherDTO teacherDTO) {
        return teacherService.addTeacher(teacherDTO);
    }

    @PutMapping("/{teacherId}")
    public TeacherDTO updateTeacher(@PathVariable long teacherId,
                                    @Valid @RequestBody TeacherDTO teacherDTO) {
        teacherDTO.setId(teacherId);
        return teacherService.updateTeacher(teacherDTO);
    }

    @DeleteMapping("/{teacherId}")
    public void deleteTeacher(@PathVariable long teacherId) {
        teacherService.deleteTeacherById(teacherId);
    }

    @PostMapping("/{teacherId}/{subjectId}")
    public TeacherDTO assignSubjectToTeacher(@PathVariable long teacherId,
                                             @PathVariable int subjectId) {
        return teacherService.assignSubjectToTeacher(teacherId, subjectId);
    }

    @PostMapping("/{teacherId}/subject/{subjectId}")
    public TeacherDTO unassignSubjectFromTeacher(@PathVariable long teacherId,
                                                 @PathVariable int subjectId) {
        return teacherService.unassignSubjectFromTeacher(teacherId, subjectId);
    }
}
