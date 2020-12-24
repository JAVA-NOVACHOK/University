package ua.com.nikiforov.mappers_dto;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import ua.com.nikiforov.dto.*;
import ua.com.nikiforov.models.lesson.Lesson;

import java.time.LocalDate;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class TimetableMapperDTO {

    @Autowired
    private SubjectMapperDTO subjectMapper;

    @Autowired
    private RoomMapperDTO roomMapper;

    @Autowired
    private GroupMapperDTO groupMapper;

    @Autowired
    private TeacherMapperDTO teacherMapper;

    public abstract List<TimetableDTO> getTimetableDTOs(List<Lesson> lessons);

    public TimetableDTO getTimetableDTO(Lesson lesson){
        long lessonId = lesson.getId();
        SubjectDTO subject = subjectMapper.subjectToSubjectDTO(lesson.getSubject());
        GroupDTO group = groupMapper.groupToGroupDTO(lesson.getGroup());
        RoomDTO room = roomMapper.roomToRoomDTO(lesson.getRoom());
        LocalDate time = lesson.getLessonDate();
        TeacherDTO teacher = teacherMapper.getTeacherDTO(lesson.getTeacher());
        int period = lesson.getPeriod();
        return new TimetableDTO(lessonId,period,subject,group,room,teacher,time);
    }

}
