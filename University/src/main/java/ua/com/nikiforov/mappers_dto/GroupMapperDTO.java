package ua.com.nikiforov.mappers_dto;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import ua.com.nikiforov.dto.GroupDTO;
import ua.com.nikiforov.models.Group;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class GroupMapperDTO {

    @Autowired
    private StudentMapperDTO studentMapperDTO;

    public abstract Group groupDTOToGroup(GroupDTO groupDTO);

    public abstract List<GroupDTO> getGroupDTOList(List<Group> groups);

    public GroupDTO groupToGroupDTO(Group group) {
        GroupDTO groupDTO = new GroupDTO(group.getGroupId(),group.getGroupName());
        groupDTO.setStudents(studentMapperDTO.getStudentDTOList(group.getGroupStudents()));
        return groupDTO;
    }
}
