package ua.com.nikiforov.mappers.persons;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import ua.com.nikiforov.models.persons.Teacher;
import static ua.com.nikiforov.dao.SqlConstants.TeachersTable.*;


public class TeacherMapper implements RowMapper<Teacher> {

    @Override
    public Teacher mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Teacher teacher = new Teacher();
        teacher.setId(resultSet.getLong(ID));
        teacher.setFirstName(resultSet.getString(FIRST_NAME));
        teacher.setLastName(resultSet.getString(LAST_NAME));
        return teacher;
    }

}
