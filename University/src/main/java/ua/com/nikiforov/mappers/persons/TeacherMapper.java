package ua.com.nikiforov.mappers.persons;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ua.com.nikiforov.models.persons.Teacher;

@Component
public class TeacherMapper implements RowMapper<Teacher> {
    
    private static final int TEACHER_ID_INDEX = 1;
    private static final int TEACHER_FIRST_NAME_INDEX = 2;
    private static final int TEACHER_LAST_NAME_INDEX = 3;

    @Override
    public Teacher mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Teacher teacher = new Teacher();
        teacher.setId(resultSet.getLong(TEACHER_ID_INDEX));
        teacher.setFirstName(resultSet.getString(TEACHER_FIRST_NAME_INDEX));
        teacher.setLastName(resultSet.getString(TEACHER_LAST_NAME_INDEX));
        return teacher;
    }

}

