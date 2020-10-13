package ua.com.nikiforov.dao.teachers_subjects;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import javax.sql.DataSource;
import static ua.com.nikiforov.dao.SqlConstants.*;
import static ua.com.nikiforov.dao.SqlConstants.TeachersSubjectsTable.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

import ua.com.nikiforov.mappers.TeachersSubjectsMapper;
import ua.com.nikiforov.models.TeachersSubjects;

@Repository
public class TeachersSubjectsDAOImpl implements TeachersSubjectsDAO {

    private static final String GET_TEACHERS_IDS = SELECT + ASTERISK + FROM + TEACHERS_SUBJECTS_TABLE + WHERE
            + SUBJECT_ID + EQUALS_M + Q_MARK;
    private static final String GET_SUBJECTS_IDS = SELECT + ASTERISK + FROM + TEACHERS_SUBJECTS_TABLE + WHERE
            + TEACHER_ID + EQUALS_M + Q_MARK;
    private static final String ADD_SUBJECT_FOR_TEACHER = INSERT + TEACHERS_SUBJECTS_TABLE + SET + TEACHER_ID + EQUALS_M
            + Q_MARK + COMA + SUBJECT_ID + EQUALS_M + Q_MARK;

    private static final int PREPARE_STATEMENT_FIRST_INDEX = 1;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public TeachersSubjectsDAOImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Long> getTeachersIds(int subjectId) {
        List<TeachersSubjects> teachersSubjects = jdbcTemplate.query(GET_TEACHERS_IDS, new Object[] { subjectId },
                new TeachersSubjectsMapper());
        return teachersSubjects.stream().map(t -> t.getTeachersId()).collect(Collectors.toList());
    }

    @Override
    public List<Integer> getSubjectsIds(long teacherId) {
        List<TeachersSubjects> teachersSubjects = jdbcTemplate.query(GET_SUBJECTS_IDS,
                ps -> ps.setLong(PREPARE_STATEMENT_FIRST_INDEX, teacherId), new TeachersSubjectsMapper());
        return teachersSubjects.stream().map(t -> t.getSubjectId()).collect(Collectors.toList());
    }

    @Override
    public boolean addSubjectForTeacher(long teacherId, int subjectId) {
        return jdbcTemplate.update(ADD_SUBJECT_FOR_TEACHER, teacherId, subjectId) > 0;
    }

}
