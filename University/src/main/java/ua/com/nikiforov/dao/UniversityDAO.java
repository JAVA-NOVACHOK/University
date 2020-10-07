package ua.com.nikiforov.dao;

import static ua.com.nikiforov.dao.SqlKeyWords.*;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import ua.com.nikiforov.mappers.UniversityMapper;
import ua.com.nikiforov.models.University;

public class UniversityDAO {

    private static final String ADD_UNIVERSITY = INSERT + TABLE_UNIVERSITIES + L_BRACKET + COLUMN_UNIVERSITY_NAME
            + VALUES_1_QMARK;
    private static final String FIND_UNIVERSITY_BY_ID = SELECT + ASTERISK + FROM + TABLE_UNIVERSITIES + WHERE
            + COLUMN_UNIVERSITY_ID + EQUALS_M + Q_MARK;
    private static final String GET_ALL_UNIVERSITIES = SELECT + ASTERISK + FROM + TABLE_UNIVERSITIES;
    private static final String UPDATE_UNIVERSITY = UPDATE + TABLE_UNIVERSITIES + SET + COLUMN_UNIVERSITY_NAME
            + EQUALS_M + Q_MARK + WHERE + COLUMN_UNIVERSITY_ID + EQUALS_M + Q_MARK;
    private static final String DELETE_UNIVERSITY_BY_ID = DELETE + FROM + TABLE_UNIVERSITIES + WHERE
            + COLUMN_UNIVERSITY_ID + EQUALS_M + Q_MARK;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public UniversityDAO(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public boolean addUniversity(String name) {
        return jdbcTemplate.update(ADD_UNIVERSITY, name) > 0;
    }

    public University findUniversityById(int id) {
        return jdbcTemplate.queryForObject(FIND_UNIVERSITY_BY_ID, new Object[] { id }, new UniversityMapper());
    }

    public List<University> getAllUniversities() {
        return jdbcTemplate.query(GET_ALL_UNIVERSITIES, new UniversityMapper());
    }

    public boolean updateUniversity(String name, int id) {
        return jdbcTemplate.update(UPDATE_UNIVERSITY, name, id) > 0;
    }

    public boolean deleteUniversityById(int id) {
        return jdbcTemplate.update(DELETE_UNIVERSITY_BY_ID, id) > 0;
    }
}
