package ua.com.nikiforov.models.timetable;

import java.time.Instant;

import ua.com.nikiforov.models.lesson.LessonInfo;

public abstract class DayTimetable {

    private LessonInfo lesson;
    private Instant date;
    private String personName;

    public DayTimetable() {
    }

    public DayTimetable(LessonInfo lesson, Instant date, String personName) {
        this.lesson = lesson;
        this.date = date;
        this.personName = personName;
    }

    public LessonInfo getLesson() {
        return lesson;
    }

    public void setLesson(LessonInfo lesson) {
        this.lesson = lesson;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + ((lesson == null) ? 0 : lesson.hashCode());
        result = prime * result + ((personName == null) ? 0 : personName.hashCode());
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
        DayTimetable other = (DayTimetable) obj;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;
        if (lesson == null) {
            if (other.lesson != null)
                return false;
        } else if (!lesson.equals(other.lesson))
            return false;
        if (personName == null) {
            if (other.personName != null)
                return false;
        } else if (!personName.equals(other.personName))
            return false;
        return true;
    }

}
