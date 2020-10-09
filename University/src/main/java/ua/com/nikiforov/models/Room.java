package ua.com.nikiforov.models;

public class Room {

    private int id;
    private int roomNumber;

    public Room() {
    }

    public Room(int id, int number) {
        this.id = id;
        this.roomNumber = number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber() {
        return roomNumber;
    }

    public void setNumber(int number) {
        this.roomNumber = number;
    }

}
