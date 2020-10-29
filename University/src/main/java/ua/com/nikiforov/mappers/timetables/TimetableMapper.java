<<<<<<< HEAD
package ua.com.nikiforov.mappers.timetables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ua.com.nikiforov.models.timetable.Timetable;

import static ua.com.nikiforov.dao.SqlConstants.TeachersTimetableTable.*;

@Component
public class TimetableMapper implements RowMapper<Timetable> {

    @Override
    public Timetable mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Timetable timetable = new Timetable();
        timetable.setId(resultSet.getLong(ID));
        timetable.setLessonId(resultSet.getLong(LESSON_ID));
        timetable.setPersonId(resultSet.getLong(PERSON_ID));
        Timestamp timestamp = resultSet.getTimestamp(DATE);
        timetable.setTime(timestamp.toInstant());
        timetable.setPeriod(resultSet.getInt(PERIOD));
        return timetable;
    }

}
=======
package ua.com.nikiforov.mappers.timetables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ua.com.nikiforov.models.timetable.Timetable;

import static ua.com.nikiforov.dao.SqlConstants.TeachersTimetableTable.*;

@Component
public class TimetableMapper implements RowMapper<Timetable> {

    @Override
    public Timetable mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Timetable timetable = new Timetable();
        timetable.setId(resultSet.getLong(ID));
        timetable.setLessonId(resultSet.getLong(LESSON_ID));
        timetable.setPersonId(resultSet.getLong(PERSON_ID));
        Timestamp timestamp = resultSet.getTimestamp(DATE);
        timetable.setTime(timestamp.toInstant());
        timetable.setPeriod(resultSet.getInt(PERIOD));
        return timetable;
    }

}
>>>>>>> refs/remotes/origin/master
