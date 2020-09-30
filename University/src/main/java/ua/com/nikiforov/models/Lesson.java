package ua.com.nikiforov.models;

import java.sql.Date;

public class Lesson {

    private Date starTime;
    private Date endTime;
    private Room lessonRoom;
    private Subject subjectOfLesson;
    private Group group;

    public Lesson(Date starTime, Date endTime, Room lessonRoom, Subject subjectOfLesson, Group group) {
        this.starTime = starTime;
        this.endTime = endTime;
        this.lessonRoom = lessonRoom;
        this.subjectOfLesson = subjectOfLesson;
        this.group = group;
    }

    public Date getStarTime() {
        return starTime;
    }

    public void setStarTime(Date starTime) {
        this.starTime = starTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Room getLessonRoom() {
        return lessonRoom;
    }

    public void setLessonRoom(Room lessonRoom) {
        this.lessonRoom = lessonRoom;
    }

    public Subject getSubjectOfLesson() {
        return subjectOfLesson;
    }

    public void setSubjectOfLesson(Subject subjectOfLesson) {
        this.subjectOfLesson = subjectOfLesson;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

}
