package ua.com.nikiforov.rest_controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.com.nikiforov.dto.SubjectDTO;
import ua.com.nikiforov.services.subject.SubjectService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/subjects")
public class SubjectsRestController {

    private SubjectService subjectService;

    @Autowired
    public SubjectsRestController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping
    public List<SubjectDTO> getSubjects(){
        return subjectService.getAllSubjects();
    }

    @GetMapping("/{subjectId}")
    public SubjectDTO getSubject(@PathVariable("subjectId") int subjectId){
        return subjectService.getSubjectById(subjectId);
    }

    @PostMapping
    public SubjectDTO addSubject(@Valid @RequestBody SubjectDTO subjectDTO){
        return subjectService.addSubject(subjectDTO);
    }

    @PutMapping
    public SubjectDTO updateSubject(@Valid @RequestBody SubjectDTO subjectDTO){
        return subjectService.updateSubject(subjectDTO);
    }

    @DeleteMapping("/{subjectId}")
    public void deleteSubject(@PathVariable("subjectId") int subjectId){
        subjectService.deleteSubjectById(subjectId);
    }
}
