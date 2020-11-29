package ua.com.nikiforov.services.timetables;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ua.com.nikiforov.controllers.dto.TimetableDTO;

public abstract class PersonalTimetable {
    
    public static final String ZONE = "UTC";
        
    public abstract List<DayTimetable>  getDayTimetable(String date, long personId);
    
    public abstract List<DayTimetable>  getMonthTimetable(String stringDate, long studentId);
    
    public List<DayTimetable> createMonthTimetable(List<TimetableDTO> allTimetablesList){
        List<DayTimetable> monthTimetable = new ArrayList<>();
        if (!allTimetablesList.isEmpty()) {
            for (int i = 1; i <= allTimetablesList.size(); i++) {
                DayTimetable dayTimetable = new DayTimetable();
                TimetableDTO previousTimetable = allTimetablesList.get(i - 1);
                dayTimetable.addTimetable(previousTimetable);
                dayTimetable.setDateInfo(parseInstantToDateInfo(previousTimetable));
                monthTimetable.add(dayTimetable);
                while (i < allTimetablesList.size()) {
                    TimetableDTO currentTimetable = allTimetablesList.get(i);
                    if (previousTimetable.getDate().equals(currentTimetable.getDate())) {
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
    
    public static DateInfo parseInstantToDateInfo( TimetableDTO timetable) {
        LocalDate date = timetable.getDate();
        ZonedDateTime zonedDateTime = date.atStartOfDay(ZoneId.systemDefault());
        String weekDay = zonedDateTime.getDayOfWeek().name();
        int monthDay = zonedDateTime.getDayOfMonth();
        String month = zonedDateTime.getMonth().toString();
        int year = zonedDateTime.getYear();
        return new DateInfo(weekDay, monthDay, month, year);
    }
    
    public static LocalDate getLocalDate(String date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, dateTimeFormatter);
    }
    
    public static Timestamp getTimestampFromString(String stringDate) {
        return Timestamp.valueOf(stringDate + " 00:00:00");
    }


        
}
