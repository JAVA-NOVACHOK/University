package ua.com.nikiforov.dto;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class GroupDTO {

    private long groupId;

    @Pattern(regexp = "^[A-Z]{2}-\\d{2}$",
            message = "Group name must have first two capital letters, dash and two numbers!")
    @Column(name = "group_name")
    private String groupName;
    private List<StudentDTO> students;

    public GroupDTO() {
        students = new ArrayList<>();
    }

    public GroupDTO(@NotBlank String groupName) {
        this.groupName = groupName;
    }

    public GroupDTO(long groupId, @NotBlank String groupName) {
        this.groupId = groupId;
        this.groupName = groupName;
        students = new ArrayList<>();
    }

    public GroupDTO(long groupId, @NotBlank String groupName, List<StudentDTO> students) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.students = students;
        students = new ArrayList<>();
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<StudentDTO> getStudents() {
        Collections.sort(students);
        return students;
    }
    
    public void addStudent(StudentDTO student) {
        students.add(student);
    }

    public void setStudents(List<StudentDTO> students) {
        this.students = students;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (groupId ^ (groupId >>> 32));
        result = prime * result + ((groupName == null) ? 0 : groupName.hashCode());
        result = prime * result + ((students == null || students.isEmpty()) ? 0 : students.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GroupDTO other = (GroupDTO) obj;
        if (groupId != other.groupId)
            return false;
        if (groupName == null) {
            if (other.groupName != null)
                return false;
        } else if (!groupName.equals(other.groupName))
            return false;
        if (students == null) {
            return other.students == null;
        } 
        return true;
    }

    @Override
    public String toString() {
        return "GroupDTO{" +
                "groupId=" + groupId +
                ", groupName='" + groupName + '\'' +
                ", students=" + students +
                '}';
    }
}
