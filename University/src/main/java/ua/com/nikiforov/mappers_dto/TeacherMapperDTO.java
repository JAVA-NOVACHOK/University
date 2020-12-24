package ua.com.nikiforov.mappers_dto;

import org.mapstruct.Mapper;
import ua.com.nikiforov.dto.SubjectDTO;
import ua.com.nikiforov.dto.TeacherDTO;
import ua.com.nikiforov.models.Subject;
import ua.com.nikiforov.models.persons.Teacher;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class TeacherMapperDTO {

    public abstract Teacher teacherDTOToTeacher(TeacherDTO teacherDTO);

    public abstract List<TeacherDTO> getTeacherDTOList(List<Teacher> teachers);

    public TeacherDTO getTeacherDTO(Teacher teacher) {
        TeacherDTO teacherDTO = new TeacherDTO(teacher.getId(),teacher.getFirstName(),teacher.getLastName());
        teacherDTO.setSubjects(getSubjectDTOList(teacher.getSubjects()));
        return teacherDTO;
    }

    private List<SubjectDTO> getSubjectDTOList(List<Subject> subjects) {
        List<SubjectDTO> subjectsDTO = new ArrayList<>();
        for (Subject subject : subjects) {
            subjectsDTO.add(new SubjectDTO(subject.getId(), subject.getName()));
        }
        return subjectsDTO;
    }
}
