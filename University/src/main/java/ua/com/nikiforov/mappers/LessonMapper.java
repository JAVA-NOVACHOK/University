<<<<<<< HEAD
package ua.com.nikiforov.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ua.com.nikiforov.models.Lesson;
import static ua.com.nikiforov.dao.SqlConstants.LessonsTable.*;

@Component
public class LessonMapper implements RowMapper<Lesson> {

    @Override
    public Lesson mapRow(ResultSet resultSet, int rowNum)  throws SQLException{
        Lesson lesson = new Lesson();
        lesson.setId(resultSet.getLong(ID));
        lesson.setSubjectId(resultSet.getInt(SUBJECT_ID));
        lesson.setRoomId(resultSet.getInt(ROOM_ID));
        lesson.setGroupId(resultSet.getLong(GROUP_ID));
        return lesson;
    }

}
=======
package ua.com.nikiforov.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ua.com.nikiforov.models.Lesson;
import static ua.com.nikiforov.dao.SqlConstants.LessonsTable.*;

@Component
public class LessonMapper implements RowMapper<Lesson> {

    @Override
    public Lesson mapRow(ResultSet resultSet, int rowNum)  throws SQLException{
        Lesson lesson = new Lesson();
        lesson.setId(resultSet.getLong(ID));
        lesson.setSubjectId(resultSet.getInt(SUBJECT_ID));
        lesson.setRoomId(resultSet.getInt(ROOM_ID));
        lesson.setGroupId(resultSet.getLong(GROUP_ID));
        return lesson;
    }

}
>>>>>>> refs/remotes/origin/master
