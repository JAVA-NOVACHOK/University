package ua.com.nikiforov.rest_controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.com.nikiforov.dto.LessonDTO;
import ua.com.nikiforov.services.lesson.LessonService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/lessons")
public class LessonRestController {

    private LessonService lessonService;

    @Autowired
    public LessonRestController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @GetMapping
    @ApiOperation(
            value = "Retrieves all existing lessons in university",
            responseContainer = "List",
            response = LessonDTO.class
    )
    public List<LessonDTO> allLessons() {
        return lessonService.getAllLessons();
    }

    @GetMapping("/{lessonId}")
    @ApiOperation(
            value = "Retrieves lesson by ID",
            notes = "Provide lesson ID to get it",
            response = LessonDTO.class
    )
    public LessonDTO getLesson(@ApiParam(value = "ID value for Lesson to retrieve", required = true) @PathVariable long lessonId) {
        return lessonService.getLessonById(lessonId);
    }

    @PostMapping
    @ApiOperation(
            value = "Upload new lesson to university",
            notes = "Provide LessonDTO response body to record lesson to university",
            response = LessonDTO.class
    )
    public LessonDTO addLesson(@ApiParam(value = "LessonDTO response body with id=0", required = true) @Valid @RequestBody LessonDTO lessonDTO) {
        return lessonService.addLesson(lessonDTO);
    }

    @PutMapping("/{lessonId}")
    @ApiOperation(
            value = "Updates existing lesson in university",
            notes = "Provide LessonDTO response body for updating",
            response = LessonDTO.class
    )
    public LessonDTO updateLesson(@ApiParam(value = "Lesson ID to update", required = true) @PathVariable long lessonId,
                                  @ApiParam(value = "Updated LessonDTO request body", required = true) @RequestBody LessonDTO lessonDTO) {
        lessonDTO.setId(lessonId);
        return lessonService.updateLesson(lessonDTO);
    }

    @DeleteMapping("/{lessonId}")
    @ApiOperation(
            value = "Deletes existing lesson from university",
            notes = "Provide lesson ID to delete it from university"
    )
    public void deleteLesson(@ApiParam(value = "ID of the lesson to delete", required = true) @PathVariable long lessonId) {
        lessonService.deleteLessonById(lessonId);
    }

}