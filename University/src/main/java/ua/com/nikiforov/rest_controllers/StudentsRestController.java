package ua.com.nikiforov.rest_controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.com.nikiforov.dto.StudentDTO;
import ua.com.nikiforov.models.persons.Student;
import ua.com.nikiforov.services.persons.StudentsService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentsRestController {

    private StudentsService studentsService;

    @Autowired
    public StudentsRestController(StudentsService studentsService) {
        this.studentsService = studentsService;
    }

    @GetMapping
    public List<StudentDTO> getStudents(){
        return studentsService.getAllStudents();
    }

    @GetMapping("/{studentId}")
    public StudentDTO getStudent(@PathVariable long studentId){
        return studentsService.getStudentById(studentId);
    }

    @PostMapping
    public StudentDTO addStudent(@Valid @RequestBody StudentDTO studentDTO){
        return studentsService.addStudent(studentDTO);
    }

    @PutMapping("/{studentId}")
    public StudentDTO updateStudent(@PathVariable long studentId, @Valid @RequestBody StudentDTO studentDTO){
        studentDTO.setId(studentId);
        return studentsService.updateStudent(studentDTO);
    }

    @DeleteMapping("/{studentId}")
    public void deleteStudent(@PathVariable long studentId){
        studentsService.deleteStudentById(studentId);
    }

    @PutMapping("/{studentId}/{groupToId}")
    public StudentDTO transferStudent(@PathVariable long studentId,
                                      @PathVariable long groupToId){
       return studentsService.transferStudent(studentId,groupToId);
    }
}
