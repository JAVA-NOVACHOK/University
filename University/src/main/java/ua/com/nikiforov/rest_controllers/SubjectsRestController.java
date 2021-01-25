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
    public SubjectDTO getSubject(@PathVariable int subjectId){
        return subjectService.getSubjectById(subjectId);
    }

    @PostMapping
    public SubjectDTO addSubject(@Valid @RequestBody SubjectDTO subjectDTO){
        return subjectService.addSubject(subjectDTO);
    }

    @PutMapping("/{subjectId}")
    public SubjectDTO updateSubject(@PathVariable int subjectId,@Valid @RequestBody SubjectDTO subjectDTO){
        subjectDTO.setId(subjectId);
        return subjectService.updateSubject(subjectDTO);
    }

    @DeleteMapping("/{subjectId}")
    public void deleteSubject(@PathVariable int subjectId){
        subjectService.deleteSubjectById(subjectId);
    }
}
