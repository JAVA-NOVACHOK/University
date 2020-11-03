package ua.com.nikiforov.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ua.com.nikiforov.models.lesson.LessonInfo;


@Component
public class LessonInfoMapper implements RowMapper<LessonInfo> {
    
    private static final int SUBJECT_NAME_INDEX = 1;
    private static final int ROOM_NUMBER_INDEX = 2;
    private static final int GROUP_NAME_INDEX = 3;

    @Override
    public LessonInfo mapRow(ResultSet resultSet, int rowNum)  throws SQLException{
        LessonInfo lesson = new LessonInfo();
        lesson.setSubjectName(resultSet.getString(SUBJECT_NAME_INDEX));
        lesson.setRoomNumber(resultSet.getInt(ROOM_NUMBER_INDEX));
        lesson.setGroupName(resultSet.getString(GROUP_NAME_INDEX));
        return lesson;
    }

}
