package ua.com.nikiforov.rest_controllers;

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
    public List<LessonDTO> allLessons() {
        return lessonService.getAllLessons();
    }

    @GetMapping("/{lessonId}")
    public LessonDTO getLesson(@PathVariable long lessonId) {
        return lessonService.getLessonById(lessonId);
    }

    @PostMapping
    public LessonDTO addLesson(@Valid @RequestBody LessonDTO lessonDTO) {
        return lessonService.addLesson(lessonDTO);
    }

    @PutMapping("/{lessonId}")
    public LessonDTO updateLesson(@PathVariable long lessonId, @RequestBody LessonDTO lessonDTO) {
        lessonDTO.setId(lessonId);
        return lessonService.updateLesson(lessonDTO);
    }

    @DeleteMapping("/{lessonId}")
    public void deleteLesson(@PathVariable long lessonId) {
        lessonService.deleteLessonById(lessonId);
    }

}