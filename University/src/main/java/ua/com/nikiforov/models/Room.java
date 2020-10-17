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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + roomNumber;
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
        Room other = (Room) obj;
        if (id != other.id)
            return false;
        if (roomNumber != other.roomNumber)
            return false;
        return true;
    }

}
