package ua.com.nikiforov.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ua.com.nikiforov.models.lesson.LessonInfo;


@Component
public class LessonInfoMapper implements RowMapper<LessonInfo> {
    
    private static final int LESSON_ID_INDEX = 1;
    private static final int SUBJECT_NAME_INDEX = 2;
    private static final int ROOM_NUMBER_INDEX = 3;
    private static final int GROUP_NAME_INDEX = 4;

    @Override
    public LessonInfo mapRow(ResultSet resultSet, int rowNum)  throws SQLException{
        LessonInfo lesson = new LessonInfo();
        lesson.setLessonId(resultSet.getLong(LESSON_ID_INDEX));
        lesson.setSubjectName(resultSet.getString(SUBJECT_NAME_INDEX));
        lesson.setRoomNamber(resultSet.getInt(ROOM_NUMBER_INDEX));
        lesson.setGroupName(resultSet.getString(GROUP_NAME_INDEX));
        return lesson;
    }

}
