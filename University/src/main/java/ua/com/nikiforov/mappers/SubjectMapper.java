package ua.com.nikiforov.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ua.com.nikiforov.models.Subject;

@Component
public class SubjectMapper implements RowMapper<Subject> {
    
    private static final int SUBJECT_ID_INDEX = 1;
    private static final int SUBJECT_NAME_INDEX = 2;

    @Override
    public Subject mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Subject subject = new Subject();
        subject.setId(resultSet.getInt(SUBJECT_ID_INDEX));
        subject.setName(resultSet.getString(SUBJECT_NAME_INDEX));
        return subject;
    }

}
