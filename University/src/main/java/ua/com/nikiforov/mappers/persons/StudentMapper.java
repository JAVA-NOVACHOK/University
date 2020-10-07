package ua.com.nikiforov.mappers.persons;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import ua.com.nikiforov.models.persons.Student;
import static ua.com.nikiforov.dao.SqlKeyWords.COLUMN_STUDENT_ID;
import static ua.com.nikiforov.dao.SqlKeyWords.COLUMN_STUDENT_FIRST_NAME;
import static ua.com.nikiforov.dao.SqlKeyWords.COLUMN_STUDENT_LAST_NAME;
import static ua.com.nikiforov.dao.SqlKeyWords.COLUMN_STUDENT_GROUP_ID;

public class StudentMapper implements RowMapper<Student> {

    @Override
    public Student mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Student student = new Student();
        student.setId(resultSet.getLong(COLUMN_STUDENT_ID));
        student.setFirstName(resultSet.getString(COLUMN_STUDENT_FIRST_NAME));
        student.setLastName(resultSet.getString(COLUMN_STUDENT_LAST_NAME));
        student.setGroup(resultSet.getLong(COLUMN_STUDENT_GROUP_ID));
        return student;
    }

}
