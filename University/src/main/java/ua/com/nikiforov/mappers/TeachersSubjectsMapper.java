package ua.com.nikiforov.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ua.com.nikiforov.models.TeachersSubjects;

@Component
public class TeachersSubjectsMapper implements RowMapper<TeachersSubjects> {
    
    private static final int TEACHERS_SUBJECTS_ID_INDEX = 1;
    private static final int TEACHERS_SUBJECTS_NAME_INDEX = 2;

    @Override
    public TeachersSubjects mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        TeachersSubjects teachersSubjects = new TeachersSubjects();
        teachersSubjects.setTeachersId(resultSet.getLong(TEACHERS_SUBJECTS_ID_INDEX));
        teachersSubjects.setSubjectId(resultSet.getInt(TEACHERS_SUBJECTS_NAME_INDEX));
        return teachersSubjects;
    }

}
