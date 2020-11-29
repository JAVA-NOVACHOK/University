package ua.com.nikiforov.controllers.dto;

public class StudentDTO {

    
    private long id;
    private String firstName;
    private String lastName;
    private String groupName;
    private long groupId;
    
    
    public StudentDTO() {
    }

    public StudentDTO(long id, String firstName, String lastName,long groupId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.groupId = groupId;
        this.groupName = "";
    }

    public StudentDTO(long id, String firstName, String lastName, String groupName, long groupId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.groupName = groupName;
        this.groupId = groupId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }
    
    

}
