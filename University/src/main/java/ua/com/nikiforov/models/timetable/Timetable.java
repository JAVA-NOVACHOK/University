package ua.com.nikiforov.models.timetable;

import java.time.Instant;

public class Timetable implements Comparable<Timetable> {

    private int period;
    private String subjectName;
    private int roomNumber;
    private String groupName;
    private Instant time;
    private String teachersName;

    public Timetable() {
    }

    public Timetable(int period, String subjectName, int roomNumber, String groupName, Instant time,
            String teachersName) {
        this.period = period;
        this.subjectName = subjectName;
        this.roomNumber = roomNumber;
        this.groupName = groupName;
        this.time = time;
        this.teachersName = teachersName;
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

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public String getTeachersName() {
        return teachersName;
    }

    public void setTeachersName(String teachersName) {
        this.teachersName = teachersName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((groupName == null) ? 0 : groupName.hashCode());
        result = prime * result + period;
        result = prime * result + roomNumber;
        result = prime * result + ((subjectName == null) ? 0 : subjectName.hashCode());
        result = prime * result + ((teachersName == null) ? 0 : teachersName.hashCode());
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
        Timetable other = (Timetable) obj;
        if (groupName == null) {
            if (other.groupName != null)
                return false;
        } else if (!groupName.equals(other.groupName))
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
        if (teachersName == null) {
            if (other.teachersName != null)
                return false;
        } else if (!teachersName.equals(other.teachersName))
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
        return "Timetable [period=" + period + ", subjectName=" + subjectName + ", roomNumber=" + roomNumber
                + ", groupName=" + groupName + ", time=" + time + ", teachersName=" + teachersName + "]";
    }

    @Override
    public int compareTo(Timetable o) {
        if (period > o.getPeriod()) {
            return 1;
        } else
            return 0;
    }

}
