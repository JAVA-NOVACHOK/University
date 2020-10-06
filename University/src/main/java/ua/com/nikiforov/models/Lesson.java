package ua.com.nikiforov.models;

public class Lesson {

    private int id;
    private int groupId;
    protected long subjectId;
    private int roomId;

    public Lesson(int id, int groupId, long subjectId, int roomId) {
        this.id = id;
        this.groupId = groupId;
        this.subjectId = subjectId;
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

    public long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(long subjectId) {
        this.subjectId = subjectId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

}
