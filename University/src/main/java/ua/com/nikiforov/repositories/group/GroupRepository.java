package ua.com.nikiforov.repositories.group;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.nikiforov.models.Group;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {

    public Optional<Group> getGroupByGroupName(String groupName);

}
