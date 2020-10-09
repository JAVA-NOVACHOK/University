package ua.com.nikiforov.mappers.timetables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import org.springframework.jdbc.core.RowMapper;
import ua.com.nikiforov.models.timetables.TeachersTimetable;
import static ua.com.nikiforov.dao.SqlConstants.TeachersTimetableTable.*;

public class TeachersTimetableMapper implements RowMapper<TeachersTimetable> {

    @Override
    public TeachersTimetable mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        TeachersTimetable teacherTimetable = new TeachersTimetable();
        teacherTimetable.setId(resultSet.getLong(ID));
        teacherTimetable.setLessonId(resultSet.getLong(LESSON_ID));
        teacherTimetable.setTeacherId(resultSet.getLong(TEACHER_ID));
        teacherTimetable.setTime(resultSet.getObject(TIME, Instant.class));
        return teacherTimetable;
    }

}
