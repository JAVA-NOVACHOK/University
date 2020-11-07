package ua.com.nikiforov.dao.timetables;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.com.nikiforov.exceptions.EntityNotFoundException;
import ua.com.nikiforov.mappers.timetables.TimetableMapper;
import ua.com.nikiforov.models.timetable.Timetable;

@Repository
public class TeachersTimetableDAOImpl implements TeachersTimetableDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeachersTimetableDAOImpl.class);

    private static final String GET_TEACHER_DAY_TIMETABLE = "SELECT period, subject_name, room_number, group_name, time, first_name, last_name "
            + "FROM lessons " + "INNER JOIN groups ON lessons.group_id = groups.group_id "
            + "INNER JOIN subjects ON lessons.subject_id = subjects.subject_id "
            + "INNER JOIN rooms ON lessons.room_id = rooms.room_id "
            + "INNER JOIN teachers ON lessons.teacher_id = teachers.teacher_id "
            + "WHERE lessons.teacher_id = ? AND time = ?";

    private static final String GET_TEACHER_MONTH_TIMETABLE = "SELECT period, subject_name, room_number, group_name, time, first_name, last_name "
            + "FROM lessons " + "INNER JOIN groups ON lessons.group_id = groups.group_id "
            + "INNER JOIN subjects ON lessons.subject_id = subjects.subject_id "
            + "INNER JOIN rooms ON lessons.room_id = rooms.room_id "
            + "INNER JOIN teachers ON lessons.teacher_id = teachers.teacher_id "
            + "WHERE lessons.teacher_id = ? AND time BETWEEN ? AND ?";

    private JdbcTemplate jdbcTemplate;
    private TimetableMapper timetableMapper;

    @Autowired
    public TeachersTimetableDAOImpl(DataSource dataSource, TimetableMapper timetableMapper) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        this.timetableMapper = timetableMapper;
    }

    @Override
    public List<Timetable> getDayTeacherTimetable(String date, long teacherId) {
        String timetableInfoMSG = String
                .format("Teacher's timetable for day by date with such data: %s and teacherId %d", date, teacherId);
        LOGGER.debug("Getting '{}'", timetableInfoMSG);
        List<Timetable> dayTimetable = new ArrayList<>();
        Timestamp time = getTimestampFromString(date);
        try {
            dayTimetable.addAll(
                    jdbcTemplate.query(GET_TEACHER_DAY_TIMETABLE, new Object[] { teacherId, time }, timetableMapper));
            LOGGER.info("Successfully retrived {}", timetableInfoMSG);
        } catch (EmptyResultDataAccessException e) {
            String failGetByIdMessage = "Couldn't get " + timetableInfoMSG;
            LOGGER.error(failGetByIdMessage);
            throw new EntityNotFoundException(failGetByIdMessage, e);
        }
        return dayTimetable;
    }

    @Override
    public List<Timetable> getMonthTeacherTimetable(String date, long teacherId) {
        Timestamp timeFrom = getTimestampFromString(date);
        LocalDate localDate = timeFrom.toLocalDateTime().toLocalDate();
        Timestamp timeTo = Timestamp.valueOf(localDate.plusMonths(1).atTime(LocalTime.MIDNIGHT));
        String timetableInfoMSG = String.format(
                "Teacher's timetable for month by date with  teacherId %d and date between %s and %s", teacherId,
                timeFrom.toString(), timeTo.toString());
        LOGGER.debug("Getting '{}'", timetableInfoMSG);
        List<Timetable> unsortedDayTimetable = new ArrayList<>();
        try {
            unsortedDayTimetable.addAll(jdbcTemplate.query(GET_TEACHER_MONTH_TIMETABLE,
                    new Object[] { teacherId, timeFrom, timeTo }, timetableMapper));
            LOGGER.info("Successfully retrived {}", timetableInfoMSG);
        } catch (EmptyResultDataAccessException e) {
            String failGetByIdMessage = "Couldn't get " + timetableInfoMSG;
            LOGGER.error(failGetByIdMessage);
            throw new EntityNotFoundException(failGetByIdMessage, e);
        }
        
        List<Timetable> sortedDayTimetable = unsortedDayTimetable.stream().sorted(Comparator.comparing(Timetable::getTime)).collect(Collectors.toList());
//        Collections.sort(dayTimetable, (t1, t2) -> 
//            (t1.getTime()).compareTo(t1.getTime())
//        );
        unsortedDayTimetable.stream().forEach(s -> LOGGER.debug("timetable date: {}", s.getTime()));
        LOGGER.debug("DAY_TIMETABLE = {}", unsortedDayTimetable.size());
        return sortedDayTimetable;
    }

    private Timestamp getTimestampFromString(String stringDate) {
        return Timestamp.valueOf(stringDate + " 00:00:00");
    }

}
