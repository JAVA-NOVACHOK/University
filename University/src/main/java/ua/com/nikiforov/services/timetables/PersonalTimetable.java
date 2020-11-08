package ua.com.nikiforov.services.timetables;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ua.com.nikiforov.models.timetable.Timetable;

public abstract class PersonalTimetable {
    
    public static final String ZONE = "Europe/Simferopol";
        
    public abstract List<Timetable> getDayTimetable(String date, long personId);
    
    public abstract List<DayTimetable>  getMonthTimetable(String stringDate, long studentId);
    
    public List<DayTimetable> createMonthTimetable(List<Timetable> allTimetablesList){
        List<DayTimetable> monthTimetable = new ArrayList<>();
        if (!allTimetablesList.isEmpty()) {
            for (int i = 1; i <= allTimetablesList.size(); i++) {
                DayTimetable dayTimetable = new DayTimetable();
                Timetable previousTimetable = allTimetablesList.get(i - 1);
                dayTimetable.addTimetable(previousTimetable);
                dayTimetable.setDateInfo(parseInstantToDateInfo(previousTimetable));
                monthTimetable.add(dayTimetable);
                while (i < allTimetablesList.size()) {
                    Timetable currentTimetable = allTimetablesList.get(i);
                    if (previousTimetable.getTime().equals(currentTimetable.getTime())) {
                        dayTimetable.addTimetable(currentTimetable);
                        i++;
                    } else {
                        Collections.sort(dayTimetable.getTimetables(), new CompareByPeriod());
                        break;
                    }
                }
            }
        }
        return monthTimetable;
    }
    
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
