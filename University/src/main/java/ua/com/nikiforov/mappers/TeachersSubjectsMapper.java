package ua.com.nikiforov.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ua.com.nikiforov.models.TeachersSubjects;
import static ua.com.nikiforov.dao.SqlConstants.TeachersSubjectsTable.*;

@Component
public class TeachersSubjectsMapper implements RowMapper<TeachersSubjects> {

    @Override
    public TeachersSubjects mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        TeachersSubjects teachersSubjects = new TeachersSubjects();
        teachersSubjects.setTeachersId(resultSet.getLong(TEACHER_ID));
        teachersSubjects.setSubjectId(resultSet.getInt(SUBJECT_ID));
        return teachersSubjects;
    }

}
