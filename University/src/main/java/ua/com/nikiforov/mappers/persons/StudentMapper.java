package ua.com.nikiforov.mappers.persons;

import java.sql.ResultSet;

import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ua.com.nikiforov.models.persons.Student;
import static ua.com.nikiforov.dao.SqlConstants.StudentsTable.*;


@Component
public class StudentMapper implements RowMapper<Student> {

    @Override
    public Student mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Student student = new Student();
        student.setId(resultSet.getLong(STUDENTS_STUDENT_ID));
        student.setFirstName(resultSet.getString(STUDENTS_STUDENT_FIRST_NAME));
        student.setLastName(resultSet.getString(STUDENTS_STUDENT_LAST_NAME));
        student.setGroup(resultSet.getLong(STUDENTS_STUDENT_GROUP_ID));
        return student;
    }

}
