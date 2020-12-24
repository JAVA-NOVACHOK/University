package ua.com.nikiforov.mappers_dto;

import org.mapstruct.Mapper;
import ua.com.nikiforov.dto.LessonDTO;
import ua.com.nikiforov.models.Group;
import ua.com.nikiforov.models.Room;
import ua.com.nikiforov.models.Subject;
import ua.com.nikiforov.models.lesson.Lesson;
import ua.com.nikiforov.models.persons.Teacher;
import ua.com.nikiforov.services.timetables.PersonalTimetable;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class LessonMapperDTO {

    public LessonDTO lessonToLessonDTO(Lesson lesson) {
        LessonDTO lessonDTO = new LessonDTO();
        lessonDTO.setId(lesson.getId());
        lessonDTO.setGroupId(lesson.getGroup().getGroupId());
        lessonDTO.setSubjectId(lesson.getSubject().getId());
        lessonDTO.setRoomId(lesson.getRoom().getId());
        lessonDTO.setTime(lesson.getLessonDate());
        lessonDTO.setPeriod(lesson.getPeriod());
        lessonDTO.setTeacherId(lesson.getTeacher().getId());
        lessonDTO.setDate(lessonDTO.getDateFromLocalDate());
        return lessonDTO;
    }

    public Lesson lessonDTOToLesson(LessonDTO lessonDTO){
        Lesson lesson = new Lesson();
        lesson.setId(lessonDTO.getId());
        lesson.setGroup(new Group(lessonDTO.getGroupId()));
        lesson.setSubject(new Subject(lessonDTO.getSubjectId()));
        lesson.setRoom(new Room(lessonDTO.getRoomId()));
        lesson.setLessonDate(PersonalTimetable.getLocalDate(lessonDTO.getDate()));
        lesson.setTeacher(new Teacher(lessonDTO.getTeacherId()));
        lesson.setPeriod(lessonDTO.getPeriod());
        return lesson;
    }

    public abstract List<LessonDTO> getLessonDTOList(List<Lesson> lessons);

}
