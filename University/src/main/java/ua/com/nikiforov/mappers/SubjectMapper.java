package ua.com.nikiforov.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ua.com.nikiforov.models.Subject;
import static ua.com.nikiforov.dao.SqlConstants.SubjectTable.*;

@Component
public class SubjectMapper implements RowMapper<Subject> {

    @Override
    public Subject mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Subject subject = new Subject();
        subject.setId(resultSet.getInt(SUBJECT_ID));
        subject.setName(resultSet.getString(SUBJECT_NAME));
        return subject;
    }

}
