package ua.com.nikiforov.dto;

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

    public LessonDTO(long id, int period, long groupId, int subjectId, int roomId, String date, long teacherId) {
        this.id = id;
        this.groupId = groupId;
        this.subjectId = subjectId;
        this.roomId = roomId;
        this.date = date;
        this.period = period;
        this.teacherId = teacherId;
    }

    public LessonDTO(int period, long groupId, int subjectId, int roomId, String date, long teacherId) {
        this.groupId = groupId;
        this.subjectId = subjectId;
        this.roomId = roomId;
        this.date = date;
        this.period = period;
        this.teacherId = teacherId;
    }

    public LessonDTO(int period, long groupId, int subjectId, int roomId, LocalDate time, long teacherId) {
        this.period = period;
        this.groupId = groupId;
        this.subjectId = subjectId;
        this.roomId = roomId;
        this.teacherId = teacherId;
        this.time = time;
        this.date = getDate();
    }

    public LessonDTO(long id, int period, long groupId, int subjectId, int roomId, LocalDate time, long teacherId) {
        this.id = id;
        this.period = period;
        this.groupId = groupId;
        this.subjectId = subjectId;
        this.roomId = roomId;
        this.time = time;
        this.teacherId = teacherId;

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

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        if(date == null) {
            String[] dateArr = time.toString().split("T");
            date = dateArr[0];
        }
        return date;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + (int) (groupId ^ (groupId >>> 32));
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + period;
        result = prime * result + roomId;
        result = prime * result + subjectId;
        result = prime * result + (int) (teacherId ^ (teacherId >>> 32));
        result = prime * result + ((time == null) ? 0 : time.hashCode());
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
        LessonDTO other = (LessonDTO) obj;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;
        if (groupId != other.groupId)
            return false;
        if (id != other.id)
            return false;
        if (period != other.period)
            return false;
        if (roomId != other.roomId)
            return false;
        if (subjectId != other.subjectId)
            return false;
        if (teacherId != other.teacherId)
            return false;
        if (time == null) {
            if (other.time != null)
                return false;
        } else if (!time.equals(other.time))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", period=" + period +
                ", groupId=" + groupId +
                ", subjectId=" + subjectId +
                ", roomId=" + roomId +
                ", time=" + time +
                ", teacherId=" + teacherId +
                ", date=" + getDate() + "}";
    }
}
