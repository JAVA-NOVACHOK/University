package ua.com.nikiforov.services.group;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.nikiforov.dao.group.GroupDAO;
import ua.com.nikiforov.models.Group;

@Service
public class GroupServiceImpl implements GroupService {
    
    @Autowired
    private GroupDAO groupDAO;
    
    @Override
    public boolean addGroup(String groupName) {
        return groupDAO.addGroup(groupName);
    }

    @Override
    public Group getGroupById(long id) {
        return groupDAO.getGroupById(id);
    }

    @Override
    public List<Group> getAllGroups() {
        return groupDAO.getAllGroups();
    }

    @Override
    public boolean updateGroup(long id, String groupName) {
        return groupDAO.updateGroup(groupName, id);
    }

    @Override
    public boolean deleteGroup(long id) {
        return groupDAO.deleteGroupById(id);
    }

    @Override
    public void createTable() {
        groupDAO.createTable();
        
    }

}
