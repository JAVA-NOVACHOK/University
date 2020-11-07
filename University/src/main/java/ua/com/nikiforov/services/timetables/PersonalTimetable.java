package ua.com.nikiforov.services.timetables;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import ua.com.nikiforov.models.timetable.Timetable;

public abstract class PersonalTimetable {
    
    public static final String ZONE = "Europe/Simferopol";
        
    public abstract List<Timetable> getDayTimetable(String date, long personId);
    
    public abstract List<DayTimetable>  getMonthTimetable(String stringDate, long studentId);
    
    public DateInfo parseInstantToDateInfo(Timetable timetable) {
        Instant instant = timetable.getTime();
        ZonedDateTime zonedDateTime = getZonedDateTime(instant, ZONE);
        String weekDay = zonedDateTime.getDayOfWeek().name();
        int monthDay = zonedDateTime.getDayOfMonth();
        String month = zonedDateTime.getMonth().toString();
        int year = zonedDateTime.getYear();
        return new DateInfo(weekDay, monthDay, month, year);
    }

    private ZonedDateTime getZonedDateTime(Instant instant, String zone) {
        ZoneId zoneId = ZoneId.of(zone);
        return ZonedDateTime.ofInstant(instant, zoneId);
    }

        
}
