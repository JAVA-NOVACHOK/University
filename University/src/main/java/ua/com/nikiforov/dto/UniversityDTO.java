package ua.com.nikiforov.dto;

import java.util.Objects;

public class UniversityDTO {

    private int id;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UniversityDTO that = (UniversityDTO) o;
        return id == that.id &&
                universityName.equals(that.universityName);
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
