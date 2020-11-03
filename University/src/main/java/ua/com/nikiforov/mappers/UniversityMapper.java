package ua.com.nikiforov.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ua.com.nikiforov.models.University;

@Component
public class UniversityMapper implements RowMapper<University> {
    
    private static final int UNIVERSITY_ID_INDEX = 1;
    private static final int UNIVERSITY_NAME_INDEX = 2;

    @Override
    public University mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        University university = new University();
        university.setId(resultSet.getInt(UNIVERSITY_ID_INDEX));
        university.setName(resultSet.getString(UNIVERSITY_NAME_INDEX));
        return university;
    }

}
