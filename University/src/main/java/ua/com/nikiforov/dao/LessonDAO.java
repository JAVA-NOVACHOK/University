package ua.com.nikiforov.dao;

import static ua.com.nikiforov.dao.SqlKeyWords.*;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import ua.com.nikiforov.mappers.LessonMapper;
import ua.com.nikiforov.models.Lesson;

public class LessonDAO {

    private static final String ADD_LESSON = INSERT + TABLE_LESSONS + L_BRACKET + COLUMN_LESSON_GROUP_ID + COMA
            + COLUMN_LESSON_ROOM_ID + COMA + COLUMN_LESSON_SUBJECT_ID + VALUES_3_QMARK;
    private static final String GET_ALL_LESSONS = SELECT + ASTERISK + FROM + TABLE_LESSONS;
    private static final String FIND_LESSON_BY_ID = SELECT + ASTERISK + FROM + TABLE_LESSONS + WHERE + COLUMN_LESSON_ID
            + EQUALS_M + Q_MARK;
    private static final String UPDATE_LESSON = UPDATE + TABLE_LESSONS + SET + COLUMN_LESSON_GROUP_ID + EQUALS_M
            + Q_MARK + COMA + COLUMN_LESSON_ROOM_ID + EQUALS_M + Q_MARK + COMA + COLUMN_LESSON_SUBJECT_ID + EQUALS_M
            + Q_MARK + WHERE + COLUMN_LESSON_ID + EQUALS_M + Q_MARK;
    private static final String DELETE_LESSON_BY_ID = DELETE + ASTERISK + FROM + TABLE_LESSONS + WHERE
            + COLUMN_LESSON_ID + EQUALS_M + Q_MARK;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public LessonDAO(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public boolean addLesson(long groupId, int roomId, int subjectId) {
        return jdbcTemplate.update(ADD_LESSON, groupId, roomId, subjectId) > 0;
    }

    public Lesson findLessonById(long id) {
        return jdbcTemplate.queryForObject(FIND_LESSON_BY_ID, new Object[] { id }, new LessonMapper());
    }

    public List<Lesson> getAllLessons() {
        return jdbcTemplate.query(GET_ALL_LESSONS, new LessonMapper());
    }

   public boolean updateLesson(long groupId, int roomId, int subjectId, long lessonId) {
       return jdbcTemplate.update(UPDATE_LESSON, groupId,roomId,subjectId, lessonId) > 0;
   }
   
   public boolean deleteLessonById(long id) {
       return jdbcTemplate.update(DELETE_LESSON_BY_ID,id) > 0;
   }

}
