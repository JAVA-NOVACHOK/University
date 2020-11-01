package ua.com.nikiforov.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ua.com.nikiforov.models.lesson.Lesson;
import static ua.com.nikiforov.dao.SqlConstants.LessonsTable.*;

@Component
public class LessonMapper implements RowMapper<Lesson> {
    
    private static final int LESSON_ID_INDEX = 1;
    private static final int GROUP_ID_INDEX = 2;
    private static final int SUBJECT_ID_INDEX = 3;
    private static final int ROOM_ID_INDEX = 4;

    @Override
    public Lesson mapRow(ResultSet resultSet, int rowNum)  throws SQLException{
        Lesson lesson = new Lesson();
        lesson.setId(resultSet.getLong(LESSON_ID_INDEX));
        lesson.setGroupId(resultSet.getLong(GROUP_ID_INDEX));
        lesson.setSubjectId(resultSet.getInt(SUBJECT_ID_INDEX));
        lesson.setRoomId(resultSet.getInt(ROOM_ID_INDEX));
        return lesson;
    }

}
