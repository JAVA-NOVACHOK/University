package ua.com.nikiforov.mappers_dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ua.com.nikiforov.dto.LessonDTO;
import ua.com.nikiforov.dto.TeacherDTO;
import ua.com.nikiforov.models.lesson.Lesson;

import java.util.List;

@Mapper(componentModel = "spring",uses = {SubjectMapperDTO.class,GroupMapperDTO.class,RoomMapperDTO.class, TeacherDTO.class})
public interface LessonMapperDTO {

    @Mappings({
            @Mapping(target="groupId", source = "lesson.group.groupId"),
            @Mapping(target="subjectId", source = "lesson.subject.id"),
            @Mapping(target = "roomId", source = "lesson.room.id"),
            @Mapping(target = "time", source = "lesson.lessonDate"),
            @Mapping(target = "teacherId", source = "lesson.teacher.id")
    })
    public  LessonDTO lessonToLessonDTO(Lesson lesson) ;


    @Mappings({
            @Mapping(target = "group.groupId", source = "lessonDTO.groupId"),
            @Mapping(target = "subject.id",source = "lessonDTO.subjectId"),
            @Mapping(target = "room.id", source = "lessonDTO.roomId"),
            @Mapping(target = "lessonDate", source = "lessonDTO.date",dateFormat = "yyyy-MM-dd"),
            @Mapping(target = "teacher.id",source = "lessonDTO.teacherId")
    })
    public Lesson lessonDTOToLesson(LessonDTO lessonDTO);

    public    List<LessonDTO> getLessonDTOList(List<Lesson> lessons);

}
