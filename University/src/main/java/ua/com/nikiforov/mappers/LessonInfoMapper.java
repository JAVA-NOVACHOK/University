package ua.com.nikiforov.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ua.com.nikiforov.models.Group;
import ua.com.nikiforov.models.Room;
import ua.com.nikiforov.models.Subject;
import ua.com.nikiforov.models.lesson.LessonInfo;

@Component
public class LessonInfoMapper implements RowMapper<LessonInfo> {

    private static final int SUBJECT_ID_INDEX = 1;
    private static final int SUBJECT_NAME_INDEX = 2;
    private static final int ROOM_ID_INDEX = 3;
    private static final int ROOM_NUMBER_INDEX = 4;
    private static final int ROOM_SEAT_NUMBER = 5;
    private static final int GROUP_ID_INDEX = 6;
    private static final int GROUP_NAME_INDEX = 7;

    @Override
    public LessonInfo mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        LessonInfo lessonInfo = new LessonInfo();
        lessonInfo.setSubject(new Subject(resultSet.getInt(SUBJECT_ID_INDEX), resultSet.getString(SUBJECT_NAME_INDEX)));
        lessonInfo.setRoom(new Room(resultSet.getInt(ROOM_ID_INDEX),resultSet.getInt(ROOM_NUMBER_INDEX),resultSet.getInt(ROOM_SEAT_NUMBER)));
        lessonInfo.setGroup(new Group(resultSet.getLong(GROUP_ID_INDEX),resultSet.getString(GROUP_NAME_INDEX)));
        return lessonInfo;
    }

}
