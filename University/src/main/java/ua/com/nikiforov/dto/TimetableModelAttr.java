package ua.com.nikiforov.dto;

import java.util.Objects;

public class TimetableModelAttr {

    private long lessonId;
    private int period;
    private String subjectName;
    private String groupName;
    private int roomNumber;
    private long teacherId;
    private String date;

    public TimetableModelAttr( ) {
    }

    public TimetableModelAttr(long lessonId, int period, String subjectName, String groupName, int roomNumber, long teacherId, String date) {
        this.lessonId = lessonId;
        this.period = period;
        this.subjectName = subjectName;
        this.groupName = groupName;
        this.roomNumber = roomNumber;
        this.teacherId = teacherId;
        this.date = date;
    }

    public long getLessonId() {
        return lessonId;
    }

    public void setLessonId(long lessonId) {
        this.lessonId = lessonId;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimetableModelAttr that = (TimetableModelAttr) o;
        return lessonId == that.lessonId &&
                period == that.period &&
                roomNumber == that.roomNumber &&
                teacherId == that.teacherId &&
                subjectName.equals(that.subjectName) &&
                groupName.equals(that.groupName) &&
                date.equals(that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lessonId, period, subjectName, groupName, roomNumber, teacherId, date);
    }

    @Override
    public String toString() {
        return "TimetableModelAttr{" +
                "lessonId=" + lessonId +
                ", period=" + period +
                ", subjectName=" + subjectName +
                ", groupName='" + groupName + '\'' +
                ", roomNumber='" + roomNumber + '\'' +
                ", teacherId=" + teacherId +
                ", date='" + date + '\'' +
                '}';
    }
}
