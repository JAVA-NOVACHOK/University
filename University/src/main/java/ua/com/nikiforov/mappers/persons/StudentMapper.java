package ua.com.nikiforov.mappers.persons;

import java.sql.ResultSet;

import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ua.com.nikiforov.models.persons.Student;


@Component
public class StudentMapper implements RowMapper<Student> {
    
    private static final int STUDENT_ID_INDEX = 1;
    private static final int STUDENT_FIRST_NAME_INDEX = 2;
    private static final int STUDENT_LAST_NAME_INDEX = 3;
    private static final int STUDENT_GROUP_INDEX = 4;

    @Override
    public Student mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Student student = new Student();
        student.setId(resultSet.getLong(STUDENT_ID_INDEX));
        student.setFirstName(resultSet.getString(STUDENT_FIRST_NAME_INDEX));
        student.setLastName(resultSet.getString(STUDENT_LAST_NAME_INDEX));
        student.setGroup(resultSet.getLong(STUDENT_GROUP_INDEX));
        return student;
    }

}
