package ua.com.nikiforov.models.timetable;

import java.time.Instant;

public class Timetable {

    private long id;
    private long personId;
    private long lessonId;
    private Instant time;

    public Timetable() {
    }

    public Timetable(long id, long personId, long lessonId, Instant time) {
        this.id = id;
        this.personId = personId;
        this.lessonId = lessonId;
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPersonId() {
        return personId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }

    public long getLessonId() {
        return lessonId;
    }

    public void setLessonId(long lessonId) {
        this.lessonId = lessonId;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

}
