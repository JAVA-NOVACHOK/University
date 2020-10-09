package ua.com.nikiforov.dao.lesson;

import static ua.com.nikiforov.dao.SqlConstants.*;
import static ua.com.nikiforov.dao.SqlConstants.LessonsTable.*;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.com.nikiforov.mappers.LessonMapper;
import ua.com.nikiforov.models.Lesson;

@Repository
public class LessonDAOImpl implements LessonDAO {

    private static final String ADD_LESSON = INSERT + TABLE_LESSONS + L_BRACKET + GROUP_ID + COMA
            + ROOM_ID + COMA + SUBJECT_ID + VALUES_3_QMARK;
    private static final String GET_ALL_LESSONS = SELECT + ASTERISK + FROM + TABLE_LESSONS;
    private static final String FIND_LESSON_BY_ID = SELECT + ASTERISK + FROM + TABLE_LESSONS + WHERE + ID
            + EQUALS_M + Q_MARK;
    private static final String UPDATE_LESSON = UPDATE + TABLE_LESSONS + SET + GROUP_ID + EQUALS_M
            + Q_MARK + COMA + ROOM_ID + EQUALS_M + Q_MARK + COMA + SUBJECT_ID + EQUALS_M
            + Q_MARK + WHERE + ID + EQUALS_M + Q_MARK;
    private static final String DELETE_LESSON_BY_ID = DELETE + ASTERISK + FROM + TABLE_LESSONS + WHERE
            + ID + EQUALS_M + Q_MARK;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public LessonDAOImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public boolean addLesson(long groupId, int roomId, int subjectId) {
        return jdbcTemplate.update(ADD_LESSON, groupId, roomId, subjectId) > 0;
    }

    @Override
    public Lesson getLessonById(long id) {
        return jdbcTemplate.queryForObject(FIND_LESSON_BY_ID, new Object[] { id }, new LessonMapper());
    }

    @Override
    public List<Lesson> getAllLessons() {
        return jdbcTemplate.query(GET_ALL_LESSONS, new LessonMapper());
    }

    @Override
    public boolean updateLesson(long groupId, int roomId, int subjectId, long lessonId) {
        return jdbcTemplate.update(UPDATE_LESSON, groupId, roomId, subjectId, lessonId) > 0;
    }

    @Override
    public boolean deleteLessonById(long id) {
        return jdbcTemplate.update(DELETE_LESSON_BY_ID, id) > 0;
    }

}
