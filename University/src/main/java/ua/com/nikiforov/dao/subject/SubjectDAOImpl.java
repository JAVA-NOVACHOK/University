package ua.com.nikiforov.dao.subject;

import static ua.com.nikiforov.dao.SqlConstants.*;
import static ua.com.nikiforov.dao.SqlConstants.SubjectTable.*;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.com.nikiforov.mappers.SubjectMapper;
import ua.com.nikiforov.models.Subject;

@Repository
public class SubjectDAOImpl implements SubjectDAO {

    private static final String ADD_SUBJECT = INSERT + TABLE_SUBJECTS + L_BRACKET + SUBJECT_NAME + VALUES_1_QMARK;
    private static final String GET_SUBJECT_BY_ID = SELECT + ASTERISK + FROM + TABLE_SUBJECTS + WHERE + SUBJECT_ID
            + EQUALS_M + Q_MARK;
    private static final String GET_SUBJECT_BY_NAME = SELECT + ASTERISK + FROM + TABLE_SUBJECTS + WHERE + SUBJECT_NAME
            + EQUALS_M + Q_MARK;
    private static final String GET_ALL_SUBJECTS = SELECT + ASTERISK + FROM + TABLE_SUBJECTS;
    private static final String UPDATE_SUBJECT = UPDATE + TABLE_SUBJECTS + SET + SUBJECT_NAME + EQUALS_M + Q_MARK
            + WHERE + SUBJECT_ID + EQUALS_M + Q_MARK;
    private static final String DELETE_SUBJECT_BY_ID = DELETE + FROM + TABLE_SUBJECTS + WHERE + SUBJECT_ID
            + EQUALS_M + Q_MARK;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public SubjectDAOImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public boolean addSubject(String subjectName) {
        return jdbcTemplate.update(ADD_SUBJECT, subjectName) > 0;
    }

    @Override
    public Subject getSubjectById(int subjectId) {
        return jdbcTemplate.queryForObject(GET_SUBJECT_BY_ID, new Object[] { subjectId }, new SubjectMapper());
    }

    @Override
    public Subject getSubjectByName(String subjectName) {
        return jdbcTemplate.queryForObject(GET_SUBJECT_BY_NAME, new Object[] { subjectName }, new SubjectMapper());
    }

    @Override
    public List<Subject> getAllSubjects() {
        return jdbcTemplate.query(GET_ALL_SUBJECTS, new SubjectMapper());
    }

    @Override
    public boolean updateSubject(String subjectName, int subjectId) {
        return jdbcTemplate.update(UPDATE_SUBJECT, subjectName, subjectId) > 0;
    }

    @Override
    public boolean deleteSubjectById(int subjectId) {
        return jdbcTemplate.update(DELETE_SUBJECT_BY_ID, subjectId) > 0;
    }

}
