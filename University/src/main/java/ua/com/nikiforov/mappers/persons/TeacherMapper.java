package ua.com.nikiforov.mappers.persons;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import ua.com.nikiforov.models.persons.Teacher;
import static ua.com.nikiforov.dao.SqlKeyWords.COLUMN_TEACHER_ID;
import static ua.com.nikiforov.dao.SqlKeyWords.COLUMN_TEACHER_FIRST_NAME;
import static ua.com.nikiforov.dao.SqlKeyWords.COLUMN_TEACHER_LAST_NAME;
import static ua.com.nikiforov.dao.SqlKeyWords.COLUMN_TEACHER_SUBJECT_ID;

public class TeacherMapper implements RowMapper<Teacher> {

    @Override
    public Teacher mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Teacher teacher = new Teacher();
        teacher.setId(resultSet.getLong(COLUMN_TEACHER_ID));
        teacher.setFirstName(resultSet.getString(COLUMN_TEACHER_FIRST_NAME));
        teacher.setLastName(resultSet.getString(COLUMN_TEACHER_LAST_NAME));
        teacher.setSubjectId(resultSet.getInt(COLUMN_TEACHER_SUBJECT_ID));
        return teacher;
    }

}
