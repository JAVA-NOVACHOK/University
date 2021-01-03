package ua.com.nikiforov.mappers_dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ua.com.nikiforov.dto.StudentDTO;
import ua.com.nikiforov.models.persons.Student;

import java.util.List;

@Mapper(componentModel = "spring",uses = {GroupMapperDTO.class})
public interface StudentMapperDTO {

    public StudentDTO studentToStudentDTO(Student student);

    public List<StudentDTO> getStudentDTOList(List<Student> students);

    public List<Student> getStudentList(List<StudentDTO> students);

    @Mappings({
            @Mapping(target = "group.groupId",source = "groupId")
    })
    public Student studentDTOToStudent(StudentDTO studentDTO);

}
