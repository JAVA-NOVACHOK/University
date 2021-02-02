package ua.com.nikiforov.rest_controllers;

import com.github.database.rider.core.api.dataset.DataSet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ua.com.nikiforov.helper.SetupTestHelper;
import ua.com.nikiforov.rest_controllers.lesson_test.data_set_builder.ScheduleDataset;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class ScheduleRestControllerTest extends SetupTestHelper {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeAll
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DataSet(provider = ScheduleDataset.class, disableConstraints = true,
            executeScriptsBefore = {
                    RESET_LESSON_ID, RESET_GROUP_ID,
                    RESET_SUBJECT_ID, RESET_TEACHER_ID, RESET_ROOM_ID},
            cleanAfter = true)
    void whenGetTeacherScheduleForDay_Status200() throws Exception {
        this.mockMvc.perform(
                get("/api/schedule/teacher/{teacherId}/day/{date}",
                        1L, DATE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].timetables", hasSize(3)))
                .andExpect(jsonPath("$[0].timetables.[0].lessonId", is(1L), Long.class))
                .andExpect(jsonPath("$[0].timetables.[0].period", is(1)))
                .andExpect(jsonPath("$[0].timetables.[0].subject.id", is(1)))
                .andExpect(jsonPath("$[0].timetables.[0].room.id", is(1)))
                .andExpect(jsonPath("$[0].timetables.[0].teacher.id", is(1L), Long.class))
                .andExpect(jsonPath("$[0].timetables.[0].group.groupId", is(1L), Long.class))
                .andExpect(jsonPath("$[0].timetables.[0].lessonDate", is(DATE)))

                .andExpect(jsonPath("$[0].timetables.[1].lessonId", is(2L), Long.class))
                .andExpect(jsonPath("$[0].timetables.[1].period", is(2)))
                .andExpect(jsonPath("$[0].timetables.[1].subject.id", is(2)))
                .andExpect(jsonPath("$[0].timetables.[1].room.id", is(2)))
                .andExpect(jsonPath("$[0].timetables.[1].teacher.id", is(1L), Long.class))
                .andExpect(jsonPath("$[0].timetables.[1].group.groupId", is(1L), Long.class))
                .andExpect(jsonPath("$[0].timetables.[1].lessonDate", is(DATE)))

                .andExpect(jsonPath("$[0].timetables.[2].lessonId", is(3L), Long.class))
                .andExpect(jsonPath("$[0].timetables.[2].period", is(3)))
                .andExpect(jsonPath("$[0].timetables.[2].subject.id", is(1)))
                .andExpect(jsonPath("$[0].timetables.[2].room.id", is(3)))
                .andExpect(jsonPath("$[0].timetables.[2].teacher.id", is(1L), Long.class))
                .andExpect(jsonPath("$[0].timetables.[2].group.groupId", is(1L), Long.class))
                .andExpect(jsonPath("$[0].timetables.[2].lessonDate", is(DATE)));
    }

    @Test
    @DataSet(provider = ScheduleDataset.class, disableConstraints = true,
            executeScriptsBefore = {
                    RESET_LESSON_ID, RESET_GROUP_ID,
                    RESET_SUBJECT_ID, RESET_TEACHER_ID, RESET_ROOM_ID},
            cleanAfter = true)
    void whenGetTeacherScheduleForMonth_Status200() throws Exception {
        this.mockMvc.perform(
                get("/api/schedule/teacher/{teacherId}/month/{date}",
                        1L, DATE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].timetables", hasSize(3)))
                .andExpect(jsonPath("$[0].timetables.[0].lessonId", is(1L), Long.class))
                .andExpect(jsonPath("$[0].timetables.[0].period", is(1)))
                .andExpect(jsonPath("$[0].timetables.[0].subject.id", is(1)))
                .andExpect(jsonPath("$[0].timetables.[0].room.id", is(1)))
                .andExpect(jsonPath("$[0].timetables.[0].teacher.id", is(1L), Long.class))
                .andExpect(jsonPath("$[0].timetables.[0].group.groupId", is(1L), Long.class))
                .andExpect(jsonPath("$[0].timetables.[0].lessonDate", is(DATE)))

                .andExpect(jsonPath("$[0].timetables.[1].lessonId", is(2L), Long.class))
                .andExpect(jsonPath("$[0].timetables.[1].period", is(2)))
                .andExpect(jsonPath("$[0].timetables.[1].subject.id", is(2)))
                .andExpect(jsonPath("$[0].timetables.[1].room.id", is(2)))
                .andExpect(jsonPath("$[0].timetables.[1].teacher.id", is(1L), Long.class))
                .andExpect(jsonPath("$[0].timetables.[1].group.groupId", is(1L), Long.class))
                .andExpect(jsonPath("$[0].timetables.[1].lessonDate", is(DATE)))

                .andExpect(jsonPath("$[0].timetables.[2].lessonId", is(3L), Long.class))
                .andExpect(jsonPath("$[0].timetables.[2].period", is(3)))
                .andExpect(jsonPath("$[0].timetables.[2].subject.id", is(1)))
                .andExpect(jsonPath("$[0].timetables.[2].room.id", is(3)))
                .andExpect(jsonPath("$[0].timetables.[2].teacher.id", is(1L), Long.class))
                .andExpect(jsonPath("$[0].timetables.[2].group.groupId", is(1L), Long.class))
                .andExpect(jsonPath("$[0].timetables.[2].lessonDate", is(DATE)))

                .andExpect(jsonPath("$[1].timetables", hasSize(1)))
                .andExpect(jsonPath("$[1].timetables.[0].lessonId", is(4L), Long.class))
                .andExpect(jsonPath("$[1].timetables.[0].period", is(1)))
                .andExpect(jsonPath("$[1].timetables.[0].subject.id", is(1)))
                .andExpect(jsonPath("$[1].timetables.[0].room.id", is(1)))
                .andExpect(jsonPath("$[1].timetables.[0].teacher.id", is(1L), Long.class))
                .andExpect(jsonPath("$[1].timetables.[0].group.groupId", is(1L), Long.class))
                .andExpect(jsonPath("$[1].timetables.[0].lessonDate", is(DATE_1)))

                .andExpect(jsonPath("$[2].timetables", hasSize(1)))
                .andExpect(jsonPath("$[2].timetables.[0].lessonId", is(5L), Long.class))
                .andExpect(jsonPath("$[2].timetables.[0].period", is(2)))
                .andExpect(jsonPath("$[2].timetables.[0].subject.id", is(2)))
                .andExpect(jsonPath("$[2].timetables.[0].room.id", is(2)))
                .andExpect(jsonPath("$[2].timetables.[0].teacher.id", is(1L), Long.class))
                .andExpect(jsonPath("$[2].timetables.[0].group.groupId", is(1L), Long.class))
                .andExpect(jsonPath("$[2].timetables.[0].lessonDate", is(DATE_1_ADD_3_DAYS)))

                .andExpect(jsonPath("$[3].timetables", hasSize(1)))
                .andExpect(jsonPath("$[3].timetables.[0].lessonId", is(6L), Long.class))
                .andExpect(jsonPath("$[3].timetables.[0].period", is(1)))
                .andExpect(jsonPath("$[3].timetables.[0].subject.id", is(3)))
                .andExpect(jsonPath("$[3].timetables.[0].room.id", is(3)))
                .andExpect(jsonPath("$[3].timetables.[0].teacher.id", is(1L), Long.class))
                .andExpect(jsonPath("$[3].timetables.[0].group.groupId", is(1L), Long.class))
                .andExpect(jsonPath("$[3].timetables.[0].lessonDate", is(DATE_1_ADD_13_DAYS)));
    }

    @Test
    @DataSet(provider = ScheduleDataset.class, disableConstraints = true,
            executeScriptsBefore = {
                    RESET_LESSON_ID, RESET_GROUP_ID,
                    RESET_SUBJECT_ID, RESET_TEACHER_ID, RESET_ROOM_ID},
            cleanAfter = true)
    void whenGetStudentScheduleForDay_Status200() throws Exception {
        this.mockMvc.perform(
                get("/api/schedule/student/{groupId}/day/{date}",
                        1L,DATE)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].timetables", hasSize(3)))
                .andExpect(jsonPath("$[0].timetables.[0].lessonId", is(1L), Long.class))
                .andExpect(jsonPath("$[0].timetables.[0].period", is(1)))
                .andExpect(jsonPath("$[0].timetables.[0].subject.id", is(1)))
                .andExpect(jsonPath("$[0].timetables.[0].room.id", is(1)))
                .andExpect(jsonPath("$[0].timetables.[0].teacher.id", is(1L), Long.class))
                .andExpect(jsonPath("$[0].timetables.[0].group.groupId", is(1L), Long.class))
                .andExpect(jsonPath("$[0].timetables.[0].lessonDate", is(DATE)))

                .andExpect(jsonPath("$[0].timetables.[1].lessonId", is(2L), Long.class))
                .andExpect(jsonPath("$[0].timetables.[1].period", is(2)))
                .andExpect(jsonPath("$[0].timetables.[1].subject.id", is(2)))
                .andExpect(jsonPath("$[0].timetables.[1].room.id", is(2)))
                .andExpect(jsonPath("$[0].timetables.[1].teacher.id", is(1L), Long.class))
                .andExpect(jsonPath("$[0].timetables.[1].group.groupId", is(1L), Long.class))
                .andExpect(jsonPath("$[0].timetables.[1].lessonDate", is(DATE)))

                .andExpect(jsonPath("$[0].timetables.[2].lessonId", is(3L), Long.class))
                .andExpect(jsonPath("$[0].timetables.[2].period", is(3)))
                .andExpect(jsonPath("$[0].timetables.[2].subject.id", is(1)))
                .andExpect(jsonPath("$[0].timetables.[2].room.id", is(3)))
                .andExpect(jsonPath("$[0].timetables.[2].teacher.id", is(1L), Long.class))
                .andExpect(jsonPath("$[0].timetables.[2].group.groupId", is(1L), Long.class))
                .andExpect(jsonPath("$[0].timetables.[2].lessonDate", is(DATE)));
    }

    @Test
    @DataSet(provider = ScheduleDataset.class, disableConstraints = true,
            executeScriptsBefore = {
                    RESET_LESSON_ID, RESET_GROUP_ID,
                    RESET_SUBJECT_ID, RESET_TEACHER_ID, RESET_ROOM_ID},
            cleanAfter = true)
    void whenGetStudentScheduleForMonth_Status200() throws Exception {
        this.mockMvc.perform(
                get("/api/schedule/student/{groupId}/month/{date}",
                        1L,DATE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].timetables", hasSize(3)))
                .andExpect(jsonPath("$[0].timetables.[0].lessonId", is(1L), Long.class))
                .andExpect(jsonPath("$[0].timetables.[0].period", is(1)))
                .andExpect(jsonPath("$[0].timetables.[0].subject.id", is(1)))
                .andExpect(jsonPath("$[0].timetables.[0].room.id", is(1)))
                .andExpect(jsonPath("$[0].timetables.[0].teacher.id", is(1L), Long.class))
                .andExpect(jsonPath("$[0].timetables.[0].group.groupId", is(1L), Long.class))
                .andExpect(jsonPath("$[0].timetables.[0].lessonDate", is(DATE)))

                .andExpect(jsonPath("$[0].timetables.[1].lessonId", is(2L), Long.class))
                .andExpect(jsonPath("$[0].timetables.[1].period", is(2)))
                .andExpect(jsonPath("$[0].timetables.[1].subject.id", is(2)))
                .andExpect(jsonPath("$[0].timetables.[1].room.id", is(2)))
                .andExpect(jsonPath("$[0].timetables.[1].teacher.id", is(1L), Long.class))
                .andExpect(jsonPath("$[0].timetables.[1].group.groupId", is(1L), Long.class))
                .andExpect(jsonPath("$[0].timetables.[1].lessonDate", is(DATE)))

                .andExpect(jsonPath("$[0].timetables.[2].lessonId", is(3L), Long.class))
                .andExpect(jsonPath("$[0].timetables.[2].period", is(3)))
                .andExpect(jsonPath("$[0].timetables.[2].subject.id", is(1)))
                .andExpect(jsonPath("$[0].timetables.[2].room.id", is(3)))
                .andExpect(jsonPath("$[0].timetables.[2].teacher.id", is(1L), Long.class))
                .andExpect(jsonPath("$[0].timetables.[2].group.groupId", is(1L), Long.class))
                .andExpect(jsonPath("$[0].timetables.[2].lessonDate", is(DATE)))

                .andExpect(jsonPath("$[1].timetables", hasSize(1)))
                .andExpect(jsonPath("$[1].timetables.[0].lessonId", is(4L), Long.class))
                .andExpect(jsonPath("$[1].timetables.[0].period", is(1)))
                .andExpect(jsonPath("$[1].timetables.[0].subject.id", is(1)))
                .andExpect(jsonPath("$[1].timetables.[0].room.id", is(1)))
                .andExpect(jsonPath("$[1].timetables.[0].teacher.id", is(1L), Long.class))
                .andExpect(jsonPath("$[1].timetables.[0].group.groupId", is(1L), Long.class))
                .andExpect(jsonPath("$[1].timetables.[0].lessonDate", is(DATE_1)))

                .andExpect(jsonPath("$[2].timetables", hasSize(1)))
                .andExpect(jsonPath("$[2].timetables.[0].lessonId", is(5L), Long.class))
                .andExpect(jsonPath("$[2].timetables.[0].period", is(2)))
                .andExpect(jsonPath("$[2].timetables.[0].subject.id", is(2)))
                .andExpect(jsonPath("$[2].timetables.[0].room.id", is(2)))
                .andExpect(jsonPath("$[2].timetables.[0].teacher.id", is(1L), Long.class))
                .andExpect(jsonPath("$[2].timetables.[0].group.groupId", is(1L), Long.class))
                .andExpect(jsonPath("$[2].timetables.[0].lessonDate", is(DATE_1_ADD_3_DAYS)))

                .andExpect(jsonPath("$[3].timetables", hasSize(1)))
                .andExpect(jsonPath("$[3].timetables.[0].lessonId", is(6L), Long.class))
                .andExpect(jsonPath("$[3].timetables.[0].period", is(1)))
                .andExpect(jsonPath("$[3].timetables.[0].subject.id", is(3)))
                .andExpect(jsonPath("$[3].timetables.[0].room.id", is(3)))
                .andExpect(jsonPath("$[3].timetables.[0].teacher.id", is(1L), Long.class))
                .andExpect(jsonPath("$[3].timetables.[0].group.groupId", is(1L), Long.class))
                .andExpect(jsonPath("$[3].timetables.[0].lessonDate", is(DATE_1_ADD_13_DAYS)));
    }

}
