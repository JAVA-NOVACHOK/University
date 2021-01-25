package ua.com.nikiforov.models;

import javax.persistence.*;

@Entity
@Table(name = "rooms", uniqueConstraints = @UniqueConstraint(columnNames = {"room_number"}))
public class Room implements Comparable<Room> {

    @Id
    @Column(name = "room_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false,name = "room_number")
    private int roomNumber;

    @Column(nullable = false,name = "seat_number")
    private int seatNumber;

    public Room() {
    }

    public Room(int id) {
        this.id = id;
    }

    public Room(int id, int roomNumber, int seatNumber) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.seatNumber = seatNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
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
        return roomNumber == other.roomNumber;
    }

    @Override
    public String toString() {
        return "id=" + id + ", roomNumber=" + roomNumber + ", seatNumber=" + seatNumber;
    }

    @Override
    public int compareTo(Room r) {
        if (this.getRoomNumber() > r.getRoomNumber()) {
            return 1;
        } else {
            return 0;
        }
    }

}
