package ua.com.nikiforov.dto;

import javax.validation.constraints.NotBlank;

public class ScheduleFindAttr {

    @NotBlank(message = "First name cannot be empty!")
    private String firstName;

    @NotBlank(message = "Last name cannot be empty!")
    private String lastName;

    private String time;

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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
