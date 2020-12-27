package ua.com.nikiforov.mappers_dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import ua.com.nikiforov.dto.*;
import ua.com.nikiforov.models.lesson.Lesson;

import java.time.LocalDate;
import java.util.List;

@Mapper(componentModel = "spring", uses = {SubjectMapperDTO.class, RoomMapperDTO.class, GroupMapperDTO.class, TeacherMapperDTO.class})
public interface TimetableMapperDTO {

    public abstract List<TimetableDTO> getTimetableDTOs(List<Lesson> lessons);

    @Mappings({
            @Mapping(target = "lessonId", source = "lesson.id")
    })
    public TimetableDTO getTimetableDTO(Lesson lesson);

}
