package ua.com.nikiforov.models;

import java.sql.Date;

public class Lesson {

    private int id;
    private Date lessonTime;
    private Subject subjectOfLesson;
    private Group group;

    public Lesson(int id, Date lessonTime, Subject subjectOfLesson, Group group) {
        this.id = id;
        this.lessonTime = lessonTime;
        this.subjectOfLesson = subjectOfLesson;
        this.group = group;
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
