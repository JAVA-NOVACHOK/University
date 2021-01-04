package ua.com.nikiforov.repositories.group;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.nikiforov.models.Group;

public interface GroupRepository extends JpaRepository<Group, Long> {

    public Group getGroupByGroupName(String groupName);

}
