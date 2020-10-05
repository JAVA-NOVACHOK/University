package ua.com.nikiforov.models;

import java.sql.Date;

public class Lesson {

    private int id;
    private Date lessonTime;
    private int teacherId;
    private int groupId;
    private int roomId;

    
    public Lesson(int id, Date lessonTime, int teacherId, int groupId, int roomId) {
        this.id = id;
        this.lessonTime = lessonTime;
        this.teacherId = teacherId;
        this.groupId = groupId;
        this.roomId = roomId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getLessonTime() {
        return lessonTime;
    }

    public void setLessonTime(Date lessonTime) {
        this.lessonTime = lessonTime;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setSubjectId(int teacherId) {
        this.teacherId = teacherId;
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

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

}
