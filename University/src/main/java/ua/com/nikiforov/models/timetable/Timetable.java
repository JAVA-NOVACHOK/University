package ua.com.nikiforov.models.timetable;

import java.time.Instant;

import ua.com.nikiforov.models.lesson.LessonInfo;

public class Timetable implements Comparable<Timetable>{

    private long id;
    private long personId;
    private long lessonId;
    private LessonInfo lessonInfo;
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
        lessonInfo = new LessonInfo();
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

    public LessonInfo getLessonInfo() {
        return lessonInfo;
    }

    public void setLessonInfo(LessonInfo lessonInfo) {
        this.lessonInfo = lessonInfo;
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
        result = prime * result + ((lessonInfo == null) ? 0 : lessonInfo.hashCode());
        result = prime * result + period;
        result = prime * result + (int) (personId ^ (personId >>> 32));
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
        if (id != other.id)
            return false;
        if (lessonId != other.lessonId)
            return false;
        if (lessonInfo == null) {
            if (other.lessonInfo != null)
                return false;
        } else if (!lessonInfo.equals(other.lessonInfo))
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

    @Override
    public int compareTo(Timetable o) {
        if(period > o.getPeriod()) {
            return 1;
        }else
         return 0;
    }

}
