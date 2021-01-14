package ua.com.nikiforov.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "universities", uniqueConstraints = @UniqueConstraint(columnNames ={"university_name"}))
public class University {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotBlank(message = "University name cannot be empty!")
    @Column(name = "university_name")
    private String universityName;

    public University() {
    }

    public University(String universityName) {
        this.universityName = universityName;
    }

    public University(int id, String universityName) {
        this.id = id;
        this.universityName = universityName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return universityName;
    }

    public void setName(String name) {
        this.universityName = name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + ((universityName == null) ? 0 : universityName.hashCode());
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
        University other = (University) obj;
        if (id != other.id)
            return false;
        if (universityName == null) {
            if (other.universityName != null)
                return false;
        } else if (!universityName.equals(other.universityName))
            return false;
        return true;
    }

}
