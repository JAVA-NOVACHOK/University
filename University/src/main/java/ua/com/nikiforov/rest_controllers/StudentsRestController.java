package ua.com.nikiforov.rest_controllers;


import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.com.nikiforov.dto.StudentDTO;
import ua.com.nikiforov.services.persons.StudentsService;

import javax.validation.Valid;
import java.util.List;

import static ua.com.nikiforov.error_holder.ErrorMessage.*;

@RestController
@RequestMapping("/api/students")
public class StudentsRestController {

    private StudentsService studentsService;

    @Autowired
    public StudentsRestController(StudentsService studentsService) {
        this.studentsService = studentsService;
    }

    @GetMapping
    @ApiOperation(
            value = "Retrieves all existing students",
            responseContainer = "List",
            response = StudentDTO.class
    )
    @ApiResponses({
            @ApiResponse(code = CODE_200, message = "Successfully retrieved all students"),
            @ApiResponse(code = CODE_400, message = ERROR_400),
            @ApiResponse(code = CODE_401, message = ERROR_401),
            @ApiResponse(code = CODE_404, message = ERROR_404)
    })
    public List<StudentDTO> getStudents() {
        return studentsService.getAllStudents();
    }

    @GetMapping("/{studentId}")
    @ApiOperation(
            value = "Retrieves student by ID",
            notes = "Provide ID to get specific student",
            response = StudentDTO.class
    )
    @ApiResponses({
            @ApiResponse(code = CODE_200, message = "Successfully retrieved student by ID"),
            @ApiResponse(code = CODE_400, message = ERROR_400),
            @ApiResponse(code = CODE_401, message = ERROR_401),
            @ApiResponse(code = CODE_404, message = ERROR_404)
    })
    public StudentDTO getStudent(@ApiParam(value = "ID of the student", required = true) @PathVariable long studentId) {
        return studentsService.getStudentById(studentId);
    }

    @PostMapping
    @ApiOperation(
            value = "Adds new student to university",
            notes = "Provide StudentDTO request body to add room to university",
            response = StudentDTO.class
    )
    @ApiResponses({
            @ApiResponse(code = CODE_200, message = "Successfully added student"),
            @ApiResponse(code = CODE_400, message = ERROR_400),
            @ApiResponse(code = CODE_401, message = ERROR_401),
            @ApiResponse(code = CODE_404, message = ERROR_404)
    })
    public StudentDTO addStudent(@ApiParam(value = "StudentDTO request body with id=0", required = true)
                                 @Valid @RequestBody StudentDTO studentDTO) {
        return studentsService.addStudent(studentDTO);
    }

    @PutMapping("/{studentId}")
    @ApiOperation(
            value = "Updates existing student in university",
            notes = "Provide updated StudentDTO request body for update",
            response = StudentDTO.class
    )
    @ApiResponses({
            @ApiResponse(code = CODE_200, message = "Successfully updated student"),
            @ApiResponse(code = CODE_400, message = ERROR_400),
            @ApiResponse(code = CODE_401, message = ERROR_401),
            @ApiResponse(code = CODE_404, message = ERROR_404)
    })
    public StudentDTO updateStudent(@ApiParam(value = "ID of the student to update", required = true) @PathVariable long studentId,
                                    @ApiParam(value = "Updated StudentDTO request body to change existing one", required = true)
                                    @Valid @RequestBody StudentDTO studentDTO) {
        studentDTO.setId(studentId);
        return studentsService.updateStudent(studentDTO);
    }

    @DeleteMapping("/{studentId}")
    @ApiOperation(
            value = "Deletes student from university",
            notes = "Provide ID to delete student"
    )
    @ApiResponses({
            @ApiResponse(code = CODE_200, message = "Successfully deleted student"),
            @ApiResponse(code = CODE_400, message = ERROR_400),
            @ApiResponse(code = CODE_401, message = ERROR_401),
            @ApiResponse(code = CODE_404, message = ERROR_404)
    })
    public void deleteStudent(@ApiParam(value = "ID of the student to delete", required = true) @PathVariable long studentId) {
        studentsService.deleteStudentById(studentId);
    }

    @PutMapping("/{studentId}/{groupToId}")
    @ApiOperation(
            value = "Transfers student from one existing group to another by changing students group ID",
            notes = "Provide ID of the student which is going to be transferred and group ID where to transfer",
            response = StudentDTO.class
    )
    @ApiResponses({
            @ApiResponse(code = CODE_200, message = "Successfully transferred student to another group"),
            @ApiResponse(code = CODE_400, message = ERROR_400),
            @ApiResponse(code = CODE_401, message = ERROR_401),
            @ApiResponse(code = CODE_404, message = ERROR_404)
    })
    public StudentDTO transferStudent(@ApiParam(value = "Student ID which is going to be transferred", required = true) @PathVariable long studentId,
                                      @ApiParam(value = "Group ID where to transfer", required = true) @PathVariable long groupToId) {
        return studentsService.transferStudent(studentId, groupToId);
    }
}
