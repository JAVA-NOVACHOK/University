package ua.com.nikiforov.models.timetables;

public abstract class Timetable {

    protected long id;
    protected long subjectId;
    
    public Timetable(long id, long subjectId) {
        this.id = id;
        this.subjectId = subjectId;
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
    
}
