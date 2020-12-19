package ua.com.nikiforov.mappers.timetables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ua.com.nikiforov.dto.TimetableDTO;

@Component
public class TimetableMapper implements RowMapper<TimetableDTO> {

    private static final int TIMETABLE_PERIOD_INDEX = 1;
    private static final int TIMETABLE_SUBJECT_INDEX = 2;
    private static final int TIMETABLE_ROOM_INDEX = 3;
    private static final int TIMETABLE_GROUP_INDEX = 4;
    private static final int TIMETABLE_DATE_INDEX = 5;
    private static final int TIMETABLE_TEACHER_FIRST_NAME_INDEX = 6;
    private static final int TIMETABLE_TEACHER_LAST_NAME_INDEX = 7;
    private static final int TIMETABLE_LESSON_ID_INDEX = 8;
    private static final int TIMETABLE_TEACHER_ID_INDEX = 9;

    @Override
    public TimetableDTO mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        TimetableDTO timetable = new TimetableDTO();
        timetable.setPeriod(resultSet.getInt(TIMETABLE_PERIOD_INDEX));
        timetable.setSubjectName(resultSet.getString(TIMETABLE_SUBJECT_INDEX));
        timetable.setRoomNumber(resultSet.getInt(TIMETABLE_ROOM_INDEX));
        timetable.setGroupName(resultSet.getString(TIMETABLE_GROUP_INDEX));
        Timestamp timestamp = resultSet.getTimestamp(TIMETABLE_DATE_INDEX);
        timetable.setDate(timestamp.toLocalDateTime().toLocalDate());
        String firstName = resultSet.getString(TIMETABLE_TEACHER_FIRST_NAME_INDEX);
        String lastName = resultSet.getString(TIMETABLE_TEACHER_LAST_NAME_INDEX);
        timetable.setTeachersName(String.format("%s %s", firstName, lastName));
        timetable.setLessonId(resultSet.getLong(TIMETABLE_LESSON_ID_INDEX));
        timetable.setTeacherId(resultSet.getLong(TIMETABLE_TEACHER_ID_INDEX));
        return timetable;
    }

}
