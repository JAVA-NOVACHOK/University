package ua.com.nikiforov.services.group;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import ua.com.nikiforov.dao.group.GroupDAO;
import ua.com.nikiforov.dto.GroupDTO;
import ua.com.nikiforov.mappers_dto.GroupMapperDTO;

import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

    private GroupDAO groupDAO;
    private GroupMapperDTO groupMapper;

    @Autowired
    public GroupServiceImpl(GroupDAO groupDAO, GroupMapperDTO groupMapper) {
        this.groupDAO = groupDAO;
        this.groupMapper = groupMapper;
    }

    @Override
    public void addGroup(String groupName) {
        try {
            groupDAO.addGroup(groupName);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateKeyException("Error! Duplicate group while adding", e);
        }
    }

    @Override
    public GroupDTO getGroupById(long groupId) {
        return groupMapper.groupToGroupDTO(groupDAO.getGroupById(groupId));
    }

    @Override
    public GroupDTO getGroupByName(String groupName) {
        return groupMapper.groupToGroupDTO(groupDAO.getGroupByName(groupName));
    }

    @Override
    public List<GroupDTO> getAllGroups() {
        return groupMapper.getGroupDTOList(groupDAO.getAllGroups());
    }

    @Override
    public void updateGroup(GroupDTO group) {
        try {
            groupDAO.updateGroup(group);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateKeyException("Error! Duplicate group while editing!", e);
        }
    }

    @Override
    public void deleteGroup(long id) {
        groupDAO.deleteGroupById(id);
    }

}
