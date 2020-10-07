package ua.com.nikiforov.dao;

import static ua.com.nikiforov.dao.SqlKeyWords.*;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import ua.com.nikiforov.mappers.SubjectMapper;
import ua.com.nikiforov.models.Subject;

public class SubjectDAO {

    private static final String ADD_SUBJECT = INSERT + TABLE_SUBJECTS + L_BRACKET + COLUMN_SUBJECT_NAME
            + VALUES_1_QMARK;
    private static final String GET_SUBJECT_BY_ID = SELECT + ASTERISK + FROM + TABLE_SUBJECTS + WHERE
            + COLUMN_SUBJECT_ID + EQUALS_M + Q_MARK;
    private static final String GET_ALL_SUBJECTS = SELECT + ASTERISK + FROM + TABLE_SUBJECTS;
    private static final String UPDATE_SUBJECT = UPDATE + TABLE_SUBJECTS + SET + COLUMN_SUBJECT_NAME + EQUALS_M + Q_MARK
            + WHERE + COLUMN_SUBJECT_ID + EQUALS_M + Q_MARK;
    private static final String DELETE_SUBJECT_BY_ID = DELETE + ASTERISK + FROM + TABLE_SUBJECTS + WHERE
            + COLUMN_SUBJECT_ID + EQUALS_M + Q_MARK;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public SubjectDAO(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public boolean addSubject(String subjectName) {
        return jdbcTemplate.update(ADD_SUBJECT, subjectName) > 0;
    }

    public Subject getSubjectById(int id) {
        return jdbcTemplate.queryForObject(GET_SUBJECT_BY_ID, new Object[] { id }, new SubjectMapper());
    }

    public List<Subject> getAllSubjects() {
        return jdbcTemplate.query(GET_ALL_SUBJECTS, new SubjectMapper());
    }

    public boolean updateSubject(String subjectName, int id) {
        return jdbcTemplate.update(UPDATE_SUBJECT, subjectName, id) > 0;
    }

    public boolean deleteSubjectById(int id) {
        return jdbcTemplate.update(DELETE_SUBJECT_BY_ID, id) > 0;
    }

}
