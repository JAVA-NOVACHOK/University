package ua.com.nikiforov.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.*;

@ApiModel(description = "Details about RoomDTO")
public class RoomDTO {

    private int id;

    @ApiModelProperty(notes = "Number of the room")
    @Positive(message = "Room Number must be positive!")
    @Max(value = 1000,message = "Room number cannot be greater then 1000!")
    private int roomNumber;

    @ApiModelProperty(notes = "Amount of seats in the room available")
    @Positive(message = "Seats Number must be positive!")
    @Max(value = 300, message = "Seats number cannot be greater then 300!")
    private int seatNumber;

    public RoomDTO() {
    }

    public RoomDTO(int roomNumber, int seatNumber) {
        this.roomNumber = roomNumber;
        this.seatNumber = seatNumber;
    }

    public RoomDTO(int id, int roomNumber, int seatNumber) {
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
        result = prime * result + id;
        result = prime * result + roomNumber;
        result = prime * result + seatNumber;
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
        RoomDTO other = (RoomDTO) obj;
        if (id != other.id)
            return false;
        if (roomNumber != other.roomNumber)
            return false;
        return seatNumber == other.seatNumber;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", roomNumber=" + roomNumber +
                ", seatNumber=" + seatNumber +
                '}';
    }
}
