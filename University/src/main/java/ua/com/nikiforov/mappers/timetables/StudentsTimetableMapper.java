package ua.com.nikiforov.mappers.timetables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import org.springframework.jdbc.core.RowMapper;
import ua.com.nikiforov.models.timetables.StudentsTimetable;
import static ua.com.nikiforov.dao.SqlKeyWords.COLUMN_STUDENTS_TIMETABLE_ID;
import static ua.com.nikiforov.dao.SqlKeyWords.COLUMN_STUDENTS_TIMETABLE_LESSON_ID;
import static ua.com.nikiforov.dao.SqlKeyWords.COLUMN_STUDENTS_TIMETABLE_STUDENT_ID;
import static ua.com.nikiforov.dao.SqlKeyWords.COLUMN_STUDENTS_TIMETABLE_TIME;

public class StudentsTimetableMapper implements RowMapper<StudentsTimetable> {

    @Override
    public StudentsTimetable mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        StudentsTimetable studentsTimetable = new StudentsTimetable();
        studentsTimetable.setId(resultSet.getLong(COLUMN_STUDENTS_TIMETABLE_ID));
        studentsTimetable.setLessonId(resultSet.getLong(COLUMN_STUDENTS_TIMETABLE_LESSON_ID));
        studentsTimetable.setStudentId(resultSet.getLong(COLUMN_STUDENTS_TIMETABLE_STUDENT_ID));
        studentsTimetable.setTime(resultSet.getObject(COLUMN_STUDENTS_TIMETABLE_TIME, Instant.class));
        return studentsTimetable;
    }

}
