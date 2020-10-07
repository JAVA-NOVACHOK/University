package ua.com.nikiforov.mappers.timetables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import org.springframework.jdbc.core.RowMapper;
import ua.com.nikiforov.models.timetables.TeachersTimetable;
import static ua.com.nikiforov.dao.SqlKeyWords.COLUMN_TEACHER_TIMETABLE_ID;
import static ua.com.nikiforov.dao.SqlKeyWords.COLUMN_TEACHER_TIMETABLE_LESSON_ID;
import static ua.com.nikiforov.dao.SqlKeyWords.COLUMN_TEACHER_TIMETABLE_TEACHER_ID;
import static ua.com.nikiforov.dao.SqlKeyWords.COLUMN_TEACHER_TIMETABLE_TIME;

public class TeachersTimetableMapper implements RowMapper<TeachersTimetable> {

    @Override
    public TeachersTimetable mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        TeachersTimetable teacherTimetable = new TeachersTimetable();
        teacherTimetable.setId(resultSet.getLong(COLUMN_TEACHER_TIMETABLE_ID));
        teacherTimetable.setLessonId(resultSet.getLong(COLUMN_TEACHER_TIMETABLE_LESSON_ID));
        teacherTimetable.setTeacherId(resultSet.getLong(COLUMN_TEACHER_TIMETABLE_TEACHER_ID));
        teacherTimetable.setTime(resultSet.getObject(COLUMN_TEACHER_TIMETABLE_TIME, Instant.class));
        return teacherTimetable;
    }

}
