package ua.com.nikiforov.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import ua.com.nikiforov.models.University;
import static ua.com.nikiforov.dao.SqlKeyWords.COLUMN_UNIVERSITY_ID;
import static ua.com.nikiforov.dao.SqlKeyWords.COLUMN_UNIVERSITY_NAME;

public class UniversityMapper implements RowMapper<University> {

    @Override
    public University mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        University university = new University();
        university.setId(resultSet.getInt(COLUMN_UNIVERSITY_ID));
        university.setName(resultSet.getString(COLUMN_UNIVERSITY_NAME));
        return university;
    }

}
