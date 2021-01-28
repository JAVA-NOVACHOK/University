package ua.com.nikiforov.rest_controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.com.nikiforov.dto.SubjectDTO;
import ua.com.nikiforov.services.subject.SubjectService;

import javax.validation.Valid;
import java.util.List;

import static ua.com.nikiforov.error_holder.ErrorMessage.*;

@RestController
@RequestMapping("/api/subjects")
public class SubjectsRestController {

    private SubjectService subjectService;

    @Autowired
    public SubjectsRestController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping
    @ApiOperation(
            value = "Retrieves all existing subjects in university",
            responseContainer = "List",
            response = SubjectDTO.class
    )
    @ApiResponses({
            @ApiResponse(code = CODE_200, message = "Successfully retrieved all subjects"),
            @ApiResponse(code = CODE_400, message = ERROR_400),
            @ApiResponse(code = CODE_401, message = ERROR_401),
            @ApiResponse(code = CODE_404, message = ERROR_404)
    })
    public List<SubjectDTO> getSubjects() {
        return subjectService.getAllSubjects();
    }

    @GetMapping("/{subjectId}")
    @ApiOperation(
            value = "Retrieves subject by ID",
            notes = "Provide subject ID to get subject",
            response = SubjectDTO.class
    )
    @ApiResponses({
            @ApiResponse(code = CODE_200, message = "Successfully retrieved subject by ID"),
            @ApiResponse(code = CODE_400, message = ERROR_400),
            @ApiResponse(code = CODE_401, message = ERROR_401),
            @ApiResponse(code = CODE_404, message = ERROR_404)
    })
    public SubjectDTO getSubject(@ApiParam(value = "Subject ID to get subject", required = true) @PathVariable int subjectId) {
        return subjectService.getSubjectById(subjectId);
    }

    @PostMapping
    @ApiOperation(
            value = "Uploads new subject to university",
            notes = "Provide SubjectDTO request body of new subject",
            response = SubjectDTO.class
    )
    @ApiResponses({
            @ApiResponse(code = CODE_200, message = "Successfully added new subject"),
            @ApiResponse(code = CODE_400, message = ERROR_400),
            @ApiResponse(code = CODE_401, message = ERROR_401),
            @ApiResponse(code = CODE_404, message = ERROR_404)
    })
    public SubjectDTO addSubject(@ApiParam(value = "New SubjectDTO request body", required = true) @Valid @RequestBody SubjectDTO subjectDTO) {
        return subjectService.addSubject(subjectDTO);
    }

    @PutMapping("/{subjectId}")
    @ApiOperation(
            value = "Updates existing subject in university",
            notes = "Provide updated SubjectDTO to edit existing one",
            response = SubjectDTO.class
    )
    @ApiResponses({
            @ApiResponse(code = CODE_200, message = "Successfully updated existing subject"),
            @ApiResponse(code = CODE_400, message = ERROR_400),
            @ApiResponse(code = CODE_401, message = ERROR_401),
            @ApiResponse(code = CODE_404, message = ERROR_404)
    })
    public SubjectDTO updateSubject(@ApiParam(value = "Subject ID to update", required = true) @PathVariable int subjectId,
                                    @ApiParam(value = "SubjectDTO updated request body", required = true) @Valid @RequestBody SubjectDTO subjectDTO) {
        subjectDTO.setId(subjectId);
        return subjectService.updateSubject(subjectDTO);
    }

    @DeleteMapping("/{subjectId}")
    @ApiOperation(
            value = "Deletes existing subject from university",
            notes = "Provide subject ID to delete subject"
    )
    @ApiResponses({
            @ApiResponse(code = CODE_200, message = "Successfully deleted subject"),
            @ApiResponse(code = CODE_400, message = ERROR_400),
            @ApiResponse(code = CODE_401, message = ERROR_401),
            @ApiResponse(code = CODE_404, message = ERROR_404)
    })
    public void deleteSubject(@ApiParam(value = "Subject ID to delete existing subject", required = true) @PathVariable int subjectId) {
        subjectService.deleteSubjectById(subjectId);
    }
}
