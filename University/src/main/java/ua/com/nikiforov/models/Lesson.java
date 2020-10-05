package ua.com.nikiforov.models;

import java.sql.Date;
import java.time.Instant;

public class Lesson {

    private int id;
    private Instant lessonTime;
    private int teacherId;
    private int groupId;
    private int roomId;

    
    public Lesson(int id, Instant lessonTime, int teacherId, int groupId, int roomId) {
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

    public Instant getLessonTime() {
        return lessonTime;
    }

    public void setLessonTime(Instant lessonTime) {
        this.lessonTime = lessonTime;
    }

    public int getTeacherId() {
        return teacherId;
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
