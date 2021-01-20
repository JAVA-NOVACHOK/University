package ua.com.nikiforov.services.group;

import java.util.List;

import ua.com.nikiforov.dto.GroupDTO;

public interface GroupService {
    
    public GroupDTO addGroup(GroupDTO groupDTO);
    
    public GroupDTO getGroupById(long id);
    
    public GroupDTO getGroupByName(String groupName);
    
    public List<GroupDTO> getAllGroups();
    
    public GroupDTO updateGroup(GroupDTO group);
    
    public void deleteGroup(long id);
    
}

