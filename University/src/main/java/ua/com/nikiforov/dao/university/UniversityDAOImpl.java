package ua.com.nikiforov.dao.university;

import static ua.com.nikiforov.dao.SqlConstants.*;
import static ua.com.nikiforov.dao.SqlConstants.UniversityTable.*;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ua.com.nikiforov.mappers.UniversityMapper;
import ua.com.nikiforov.models.University;

@Repository
public class UniversityDAOImpl implements UniversityDAO {

    private static final String ADD_UNIVERSITY = INSERT + TABLE_UNIVERSITIES + L_BRACKET + NAME + VALUES_1_QMARK;
    private static final String FIND_UNIVERSITY_BY_ID = SELECT + ASTERISK + FROM + TABLE_UNIVERSITIES + WHERE + ID
            + EQUALS_M + Q_MARK;
    private static final String FIND_UNIVERSITY_BY_NAME = SELECT + ASTERISK + FROM + TABLE_UNIVERSITIES + WHERE + NAME
            + EQUALS_M + Q_MARK;
    private static final String GET_ALL_UNIVERSITIES = SELECT + ASTERISK + FROM + TABLE_UNIVERSITIES;
    private static final String UPDATE_UNIVERSITY = UPDATE + TABLE_UNIVERSITIES + SET + NAME + EQUALS_M + Q_MARK + WHERE
            + ID + EQUALS_M + Q_MARK;
    private static final String DELETE_UNIVERSITY_BY_ID = DELETE + FROM + TABLE_UNIVERSITIES + WHERE + ID + EQUALS_M
            + Q_MARK;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public UniversityDAOImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public boolean addUniversity(String name) {
        return jdbcTemplate.update(ADD_UNIVERSITY, name) > 0;
    }

    @Override
    public University findUniversityById(int id) {
        return jdbcTemplate.queryForObject(FIND_UNIVERSITY_BY_ID, new Object[] { id }, new UniversityMapper());
    }
    @Override
    public University getUniversityByName(String universityName) {
        return jdbcTemplate.queryForObject(FIND_UNIVERSITY_BY_NAME, new Object[] { universityName }, new UniversityMapper());
    }

    public List<University> getAllUniversities() {
        return jdbcTemplate.query(GET_ALL_UNIVERSITIES, new UniversityMapper());
    }

    @Override
    public boolean updateUniversity(String name, int id) {
        return jdbcTemplate.update(UPDATE_UNIVERSITY, name, id) > 0;
    }

    @Override
    public boolean deleteUniversityById(int id) {
        return jdbcTemplate.update(DELETE_UNIVERSITY_BY_ID, id) > 0;
    }
}
