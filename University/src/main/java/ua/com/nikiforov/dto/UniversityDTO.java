package ua.com.nikiforov.dto;


import javax.validation.constraints.NotBlank;
import java.util.Objects;

public class UniversityDTO {

    private int id;

    @NotBlank(message = "University name cannot be empty!")
    private String universityName;

    public UniversityDTO() {
    }

    public UniversityDTO(String universityName) {
        this.universityName = universityName;
    }

    public UniversityDTO(int id, String universityName) {
        this.id = id;
        this.universityName = universityName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UniversityDTO other = (UniversityDTO) obj;
        if (id != other.getId())
            return false;
        if (universityName == null) {
            if (other.universityName != null)
                return false;
        } else if (!universityName.equals(other.universityName))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, universityName);
    }

    @Override
    public String toString() {
        return "UniversityDTO{" +
                "id=" + id +
                ", universityName='" + universityName + '\'' +
                '}';
    }
}
