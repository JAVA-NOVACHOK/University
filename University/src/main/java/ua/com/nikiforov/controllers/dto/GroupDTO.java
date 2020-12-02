package ua.com.nikiforov.controllers.dto;

import java.util.ArrayList;
import java.util.List;


public class GroupDTO {
    
    private long groupId;
    private String groupName;
    private List<StudentDTO> students;

    public GroupDTO() {
    }

    public GroupDTO(long groupId, String groupName) {
        this.groupId = groupId;
        this.groupName = groupName;
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
        return students;
    }
    
    public void addStudent(StudentDTO student) {
        students.add(student);
    }

    public void setStudents(List<StudentDTO> students) {
        this.students.addAll(students);
    }

}
