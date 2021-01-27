package ua.com.nikiforov.rest_controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
    @ApiOperation(
            value = "Retrieves all existing teachers in university",
            responseContainer = "List",
            response = TeacherDTO.class
    )
    public List<TeacherDTO> getTeachers() {
        return teacherService.getAllTeachers();
    }

    @GetMapping("/{teacherId}")
    @ApiOperation(
            value = "Retrieves teacher in university by ID",
            notes = "Provide teacher ID to get it",
            response = TeacherDTO.class
    )
    public TeacherDTO getTeacher(
            @ApiParam(value = "Teacher ID to get in university", required = true) @PathVariable long teacherId) {
        return teacherService.getTeacherById(teacherId);
    }

    @PostMapping
    @ApiOperation(
            value = "Uploads new teacher to university",
            notes = "Provide new teacher response body to add to university",
            response = TeacherDTO.class
    )
    public TeacherDTO addTeacher(
            @ApiParam(value = "TeacherDTO response body with id=0") @Valid @RequestBody TeacherDTO teacherDTO) {
        return teacherService.addTeacher(teacherDTO);
    }

    @PutMapping("/{teacherId}")
    @ApiOperation(
            value = "Updates existing teacher in university",
            notes = "Provide teacher ID and updated teacher response body",
            response = TeacherDTO.class
    )
    public TeacherDTO updateTeacher(
            @ApiParam(value = "ID of existing teacher to update", required = true) @PathVariable long teacherId,
            @ApiParam(value = "Updated TeacherDTO request body", required = true) @Valid @RequestBody TeacherDTO teacherDTO) {
        teacherDTO.setId(teacherId);
        return teacherService.updateTeacher(teacherDTO);
    }

    @DeleteMapping("/{teacherId}")
    @ApiOperation(
            value = "Deletes existing teacher from university by ID",
            notes = "Provide ID of teacher to delete"
    )
    public void deleteTeacher(
            @ApiParam(value = "Teacher ID to delete from university") @PathVariable long teacherId) {
        teacherService.deleteTeacherById(teacherId);
    }

    @PostMapping("/{teacherId}/{subjectId}")
    @ApiOperation(
            value = "Assigns teacher to specific subject",
            notes = "Provide ID's of existing teacher and subject for assigning",
            response = TeacherDTO.class
    )
    public TeacherDTO assignSubjectToTeacher(
            @ApiParam(value = "Teacher ID who to assign to") @PathVariable long teacherId,
            @ApiParam(value = "Subject ID what to assign") @PathVariable int subjectId) {
        return teacherService.assignSubjectToTeacher(teacherId, subjectId);
    }

    @PostMapping("/{teacherId}/subject/{subjectId}")
    @ApiOperation(
            value = "Unassigns teacher from specific subject",
            notes = "Provide ID's of existing teacher and subject for unassigning",
            response = TeacherDTO.class
    )
    public TeacherDTO unassignSubjectFromTeacher(
            @ApiParam(value = "Teacher ID who to unassign from") @PathVariable long teacherId,
            @ApiParam(value = "Subject ID what to unassign") @PathVariable int subjectId) {
        return teacherService.unassignSubjectFromTeacher(teacherId, subjectId);
    }
}
