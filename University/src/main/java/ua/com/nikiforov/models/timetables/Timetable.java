package ua.com.nikiforov.models.timetables;

import java.time.Instant;

public abstract class Timetable {

    protected long id;
    protected long subjectId;
    protected Instant time;

    public Timetable(long id, long subjectId, Instant time) {
        this.id = id;
        this.subjectId = subjectId;
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(long subjectId) {
        this.subjectId = subjectId;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

}
