package ua.com.nikiforov.mappers.timetables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ua.com.nikiforov.models.timetable.Timetable;


@Component
public class TimetableMapper implements RowMapper<Timetable> {

    private static final int TIMETABLE_ID_INDEX = 1;
    private static final int TIMETABLE_LESSON_ID_INDEX = 2;
    private static final int TIMETABLE_DATE_INDEX = 3;
    private static final int TIMETABLE_PERIOD_INDEX = 4;
    private static final int TIMETABLE_PERSON_ID_INDEX = 5;

    @Override
    public Timetable mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Timetable timetable = new Timetable();
        timetable.setId(resultSet.getLong(TIMETABLE_ID_INDEX));
        timetable.setLessonId(resultSet.getLong(TIMETABLE_LESSON_ID_INDEX));
        Timestamp timestamp = resultSet.getTimestamp(TIMETABLE_DATE_INDEX);
        timetable.setTime(timestamp.toInstant());
        timetable.setPeriod(resultSet.getInt(TIMETABLE_PERIOD_INDEX));
        timetable.setPersonId(resultSet.getLong(TIMETABLE_PERSON_ID_INDEX));
        return timetable;
    }

}
