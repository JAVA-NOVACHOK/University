package ua.com.nikiforov.models;

public class Lesson {

    private int id;
    private int groupId;
    private int roomId;

    public Lesson(int id, int groupId, int roomId) {
        this.id = id;
        this.groupId = groupId;
        this.roomId = roomId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

}
