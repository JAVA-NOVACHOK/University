package ua.com.nikiforov.dto;

import java.time.LocalDate;

public class TimetableDTO implements Comparable<TimetableDTO> {

    private long lessonId;
    private int period;
    private String subjectName;
    private int roomNumber;
    private String groupName;
    private String teachersName;
    private long teacherId;
    private LocalDate date;

    public TimetableDTO() {
    }

    public TimetableDTO(long lessonId, int period, String subjectName, int roomNumber, String groupName,
            String teachersName, long teacherId, LocalDate date) {
        this.lessonId = lessonId;
        this.period = period;
        this.subjectName = subjectName;
        this.roomNumber = roomNumber;
        this.groupName = groupName;
        this.teachersName = teachersName;
        this.teacherId = teacherId;
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(long teacherId) {
        this.teacherId = teacherId;
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

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getTeachersName() {
        return teachersName;
    }

    public void setTeachersName(String teachersName) {
        this.teachersName = teachersName;
    }
    

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + ((groupName == null) ? 0 : groupName.hashCode());
        result = prime * result + (int) (lessonId ^ (lessonId >>> 32));
        result = prime * result + period;
        result = prime * result + roomNumber;
        result = prime * result + ((subjectName == null) ? 0 : subjectName.hashCode());
        result = prime * result + (int) (teacherId ^ (teacherId >>> 32));
        result = prime * result + ((teachersName == null) ? 0 : teachersName.hashCode());
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
        TimetableDTO other = (TimetableDTO) obj;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;
        if (groupName == null) {
            if (other.groupName != null)
                return false;
        } else if (!groupName.equals(other.groupName))
            return false;
        if (lessonId != other.lessonId)
            return false;
        if (period != other.period)
            return false;
        if (roomNumber != other.roomNumber)
            return false;
        if (subjectName == null) {
            if (other.subjectName != null)
                return false;
        } else if (!subjectName.equals(other.subjectName))
            return false;
        if (teacherId != other.teacherId)
            return false;
        if (teachersName == null) {
            if (other.teachersName != null)
                return false;
        } else if (!teachersName.equals(other.teachersName))
            return false;
        return true;
    }

    

    @Override
    public String toString() {
        return "Timetable [lessonId=" + lessonId + ", period=" + period + ", subjectName=" + subjectName
                + ", roomNumber=" + roomNumber + ", groupName=" + groupName + ", teachersName=" + teachersName
                + ", teacherId=" + teacherId + ", date=" + date + "]";
    }

    @Override
    public int compareTo(TimetableDTO o) {
        if (period > o.getPeriod()) {
            return 1;
        } else
            return 0;
    }

}
