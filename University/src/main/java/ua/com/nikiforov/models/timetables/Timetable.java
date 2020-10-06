package ua.com.nikiforov.models.timetables;

import java.time.Instant;

public abstract class Timetable {

    protected long id;
    protected long lessonId;
    protected Instant time;

    public Timetable(long id, long lessonId, Instant time) {
        this.id = id;
        this.lessonId = lessonId;
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
