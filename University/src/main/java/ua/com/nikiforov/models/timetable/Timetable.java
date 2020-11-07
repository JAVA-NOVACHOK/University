package ua.com.nikiforov.models.timetable;

import java.time.Instant;


public class Timetable implements Comparable<Timetable>{
    
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
    public int compareTo(Timetable o) {
        if(period > o.getPeriod()) {
            return 1;
        }else
         return 0;
    }

}
