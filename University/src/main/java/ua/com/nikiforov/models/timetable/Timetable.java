<<<<<<< HEAD
package ua.com.nikiforov.models.timetable;

import java.time.Instant;

public class Timetable {

    private long id;
    private long personId;
    private long lessonId;
    private Instant time;
    private int period;

    public Timetable() {
    }

    public Timetable(long id, long personId, long lessonId, Instant time, int period) {
        this.id = id;
        this.personId = personId;
        this.lessonId = lessonId;
        this.time = time;
        this.period = period;
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

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + (int) (lessonId ^ (lessonId >>> 32));
        result = prime * result + period;
        result = prime * result + (int) (personId ^ (personId >>> 32));
        if (time != null) {
            long timeMillis = time.toEpochMilli();
            result = prime * result + (int) (timeMillis ^ (timeMillis >>> 32));
        } else {
            result = prime * result;
        }
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
        if (id != other.id)
            return false;
        if (lessonId != other.lessonId)
            return false;
        if (period != other.period)
            return false;
        if (personId != other.personId)
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
        return "id=" + id + ", personId=" + personId + ", lessonId=" + lessonId + ", time=" + time + ", period="
                + period;
    }

}
=======
package ua.com.nikiforov.models.timetable;

import java.time.Instant;

public class Timetable {

    private long id;
    private long personId;
    private long lessonId;
    private Instant time;
    private int period;

    public Timetable() {
    }

    public Timetable(long id, long personId, long lessonId, Instant time, int period) {
        this.id = id;
        this.personId = personId;
        this.lessonId = lessonId;
        this.time = time;
        this.period = period;
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

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + (int) (lessonId ^ (lessonId >>> 32));
        result = prime * result + period;
        result = prime * result + (int) (personId ^ (personId >>> 32));
        if (time != null) {
            long timeMillis = time.toEpochMilli();
            result = prime * result + (int) (timeMillis ^ (timeMillis >>> 32));
        } else {
            result = prime * result;
        }
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
        if (id != other.id)
            return false;
        if (lessonId != other.lessonId)
            return false;
        if (period != other.period)
            return false;
        if (personId != other.personId)
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
        return "id=" + id + ", personId=" + personId + ", lessonId=" + lessonId + ", time=" + time + ", period="
                + period;
    }

}
>>>>>>> refs/remotes/origin/master
