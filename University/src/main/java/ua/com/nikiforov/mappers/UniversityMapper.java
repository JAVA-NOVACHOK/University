<<<<<<< HEAD
package ua.com.nikiforov.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ua.com.nikiforov.models.University;
import static ua.com.nikiforov.dao.SqlConstants.UniversityTable.*;

@Component
public class UniversityMapper implements RowMapper<University> {

    @Override
    public University mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        University university = new University();
        university.setId(resultSet.getInt(ID));
        university.setName(resultSet.getString(NAME));
        return university;
    }

}
=======
package ua.com.nikiforov.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ua.com.nikiforov.models.University;
import static ua.com.nikiforov.dao.SqlConstants.UniversityTable.*;

@Component
public class UniversityMapper implements RowMapper<University> {

    @Override
    public University mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        University university = new University();
        university.setId(resultSet.getInt(ID));
        university.setName(resultSet.getString(NAME));
        return university;
    }

}
>>>>>>> refs/remotes/origin/master
