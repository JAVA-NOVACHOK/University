package ua.com.nikiforov.dao.timetables;

import java.util.List;

import ua.com.nikiforov.dto.TimetableDTO;

public interface TimetableDAO {
    
    public List<TimetableDTO> getDayTimetable(String date, long id);

    public List<TimetableDTO> getMonthTimetable(String date, long id);
}
