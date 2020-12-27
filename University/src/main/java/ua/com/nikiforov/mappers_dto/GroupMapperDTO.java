package ua.com.nikiforov.mappers_dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import ua.com.nikiforov.dto.GroupDTO;
import ua.com.nikiforov.models.Group;

import java.util.List;

@Mapper(componentModel = "spring", uses = {StudentMapperDTO.class})
public interface GroupMapperDTO {


    @Mappings({
            @Mapping(target = "groupStudents", source = "students")
    })
    public Group groupDTOToGroup(GroupDTO groupDTO);

    public List<GroupDTO> getGroupDTOList(List<Group> groups);

    @Mappings({
            @Mapping(target = "students", source = "groupStudents")
    })
    public GroupDTO groupToGroupDTO(Group group);
}
