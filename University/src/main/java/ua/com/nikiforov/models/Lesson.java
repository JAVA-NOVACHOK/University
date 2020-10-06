package ua.com.nikiforov.models;

public class Lesson {

    private long id;
    private long groupId;
    protected int subjectId;
    private int roomId;

    public Lesson(long id, long groupId, int subjectId, int roomId) {
        this.id = id;
        this.groupId = groupId;
        this.subjectId = subjectId;
        this.roomId = roomId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

}
