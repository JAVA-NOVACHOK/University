package ua.com.nikiforov.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import ua.com.nikiforov.models.Lesson;
import static ua.com.nikiforov.dao.SqlKeyWords.COLUMN_LESSON_ID;
import static ua.com.nikiforov.dao.SqlKeyWords.COLUMN_LESSON_SUBJECT_ID;
import static ua.com.nikiforov.dao.SqlKeyWords.COLUMN_LESSON_GROUP_ID;
import static ua.com.nikiforov.dao.SqlKeyWords.COLUMN_LESSON_ROOM_ID;

public class LessonMapper implements RowMapper<Lesson> {

    @Override
    public Lesson mapRow(ResultSet resultSet, int rowNum)  throws SQLException{
        Lesson lesson = new Lesson();
        lesson.setId(resultSet.getLong(COLUMN_LESSON_ID));
        lesson.setSubjectId(resultSet.getInt(COLUMN_LESSON_SUBJECT_ID));
        lesson.setRoomId(resultSet.getInt(COLUMN_LESSON_ROOM_ID));
        lesson.setGroupId(resultSet.getLong(COLUMN_LESSON_GROUP_ID));
        return lesson;
    }

}
