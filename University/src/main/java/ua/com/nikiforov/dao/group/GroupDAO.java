package ua.com.nikiforov.dao.group;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.com.nikiforov.models.Group;

import javax.transaction.Transactional;

public interface GroupDAO extends JpaRepository<Group, Long> {

    @Transactional
    public Group save(Group group);
    
    public Group getGroupByGroupId(Long groupId);
    
    public Group getGroupByGroupName(String groupName);

    @Transactional
    public void deleteGroupByGroupId(Long groupId);

    @Query("SELECT g FROM Group g ORDER BY g.groupName")
    public List<Group> getAllGroups();

}
