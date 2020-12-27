package ua.com.nikiforov.mappers_dto;

import org.mapstruct.Mapper;
import ua.com.nikiforov.dto.TeacherDTO;
import ua.com.nikiforov.models.persons.Teacher;

import java.util.List;

@Mapper(componentModel = "spring", uses = {SubjectMapperDTO.class})
public interface TeacherMapperDTO {

    public Teacher teacherDTOToTeacher(TeacherDTO teacherDTO);

    public List<TeacherDTO> getTeacherDTOList(List<Teacher> teachers);

    public TeacherDTO getTeacherDTO(Teacher teacher);
}
