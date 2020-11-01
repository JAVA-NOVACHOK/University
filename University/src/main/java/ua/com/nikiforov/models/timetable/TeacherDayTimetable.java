package ua.com.nikiforov.models.timetable;

import java.time.Instant;

import ua.com.nikiforov.models.lesson.LessonInfo;

public class TeacherDayTimetable extends DayTimetable{
    
    private String groupName;

    public TeacherDayTimetable() {
        super();
    }

    public TeacherDayTimetable(LessonInfo lesson, Instant date, String personName,String groupName) {
        super(lesson, date, personName);
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((groupName == null) ? 0 : groupName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        TeacherDayTimetable other = (TeacherDayTimetable) obj;
        if (groupName == null) {
            if (other.groupName != null)
                return false;
        } else if (!groupName.equals(other.groupName))
            return false;
        return true;
    }

}
