package ua.com.nikiforov.mappers.timetables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import org.springframework.jdbc.core.RowMapper;
import ua.com.nikiforov.models.timetables.StudentsTimetable;
import static ua.com.nikiforov.dao.SqlConstants.StudentsTimetableTable.*;


public class StudentsTimetableMapper implements RowMapper<StudentsTimetable> {

    @Override
    public StudentsTimetable mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        StudentsTimetable studentsTimetable = new StudentsTimetable();
        studentsTimetable.setId(resultSet.getLong(ID));
        studentsTimetable.setLessonId(resultSet.getLong(LESSON_ID));
        studentsTimetable.setStudentId(resultSet.getLong(STUDENT_ID));
        studentsTimetable.setTime(resultSet.getObject(TIME, Instant.class));
        return studentsTimetable;
    }

}
