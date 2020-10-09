package ua.com.nikiforov.models;

public class University {

    private int id;
    private String universityName;

    public University() {
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

}
