package ua.com.nikiforov.controllers.dto;

import java.time.LocalDate;

public class LessonDTO {

    private long id;
    private int period;
    private long groupId;
    protected int subjectId;
    private int roomId;
    private LocalDate time;
    private long teacherId;
    private String date;

    public LessonDTO() {
    }

    public LessonDTO(long id, int period,long groupId, int subjectId, int roomId,String date,  long teacherId) {
        this.id = id;
        this.groupId = groupId;
        this.subjectId = subjectId;
        this.roomId = roomId;
        this.date = date;
        this.period = period;
        this.teacherId = teacherId;
        this.date = date;
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

    public LocalDate getTime() {
        return time;
    }

    public void setTime(LocalDate time) {
        this.time = time;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(long teacherId) {
        this.teacherId = teacherId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
