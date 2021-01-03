package ua.com.nikiforov.mappers_dto;

import org.mapstruct.Mapper;
import ua.com.nikiforov.dto.UniversityDTO;
import ua.com.nikiforov.models.University;

@Mapper(componentModel = "spring")
public interface UniversityMapperDTO {

    public UniversityDTO universityToUniversityDTO(University university);

    public University universityDTOToUniversity(UniversityDTO universityDTO);

}
