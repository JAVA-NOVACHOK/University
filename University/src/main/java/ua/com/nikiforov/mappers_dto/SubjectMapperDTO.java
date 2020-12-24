package ua.com.nikiforov.mappers_dto;

import org.mapstruct.Mapper;
import ua.com.nikiforov.dto.SubjectDTO;
import ua.com.nikiforov.dto.TeacherDTO;
import ua.com.nikiforov.models.Subject;
import ua.com.nikiforov.models.persons.Teacher;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Mapper(componentModel = "spring")
public abstract class SubjectMapperDTO {

    public abstract Subject subjectDTOToSubject(SubjectDTO subjectDTO);

    public abstract List<SubjectDTO> getSubjectDTOList(List<Subject> subjects);

    public  SubjectDTO subjectToSubjectDTO(Subject subject) {
        Set<TeacherDTO> teachersDTO = getTeachersDTOList(subject.getTeachers());
        return new SubjectDTO(subject.getId(), subject.getName(), teachersDTO);
    }

    private Set<TeacherDTO> getTeachersDTOList(Set<Teacher> teachers) {
        Set<TeacherDTO> teachersDTO = new TreeSet<>();
        for (Teacher teacher : teachers) {
            teachersDTO.add(new TeacherDTO(teacher.getId(), teacher.getFirstName(), teacher.getLastName()));
        }
        return teachersDTO;
    }

}
