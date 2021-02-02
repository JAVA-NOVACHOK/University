package ua.com.nikiforov.rest_controllers.lesson_test;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ua.com.nikiforov.dto.*;
import ua.com.nikiforov.helper.SetupTestHelper;
import ua.com.nikiforov.rest_controllers.lesson_test.data_set_builder.LessonDataset;
import ua.com.nikiforov.rest_controllers.lesson_test.data_set_builder.LessonDatasetExpected;
import ua.com.nikiforov.rest_controllers.lesson_test.data_set_builder.OneLessonDataset;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LessonRestControllerTest extends SetupTestHelper {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeAll

    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DataSet(provider = LessonDataset.class, executeScriptsBefore = RESET_LESSON_ID,
            disableConstraints = true, cleanAfter = true)
    @ExpectedDataSet(provider = LessonDatasetExpected.class)
    void whenGetAllLessons_Status200_LessonList() throws Exception {
        this.mockMvc.perform(get("/api/lessons/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_ROOT, hasSize(6)))
                .andExpect(jsonPath("$[0].id", is(1L), Long.class))
                .andExpect(jsonPath("$[0].period", is(1)))
                .andExpect(jsonPath("$[0].subjectId", is(1)))
                .andExpect(jsonPath("$[0].roomId", is(1)))
                .andExpect(jsonPath("$[0].groupId", is(1L), Long.class))
                .andExpect(jsonPath("$[0].date", is(DATE)))
                .andExpect(jsonPath("$[0].teacherId", is(1L), Long.class))

                .andExpect(jsonPath("$[1].id", is(2L), Long.class))
                .andExpect(jsonPath("$[1].period", is(2)))
                .andExpect(jsonPath("$[1].subjectId", is(2)))
                .andExpect(jsonPath("$[1].roomId", is(2)))
                .andExpect(jsonPath("$[1].groupId", is(2L), Long.class))
                .andExpect(jsonPath("$[1].teacherId", is(1L), Long.class))
                .andExpect(jsonPath("$[1].date", is(DATE)))

                .andExpect(jsonPath("$[2].id", is(3L), Long.class))
                .andExpect(jsonPath("$[2].period", is(3)))
                .andExpect(jsonPath("$[2].subjectId", is(1)))
                .andExpect(jsonPath("$[2].roomId", is(3)))
                .andExpect(jsonPath("$[2].groupId", is(2L), Long.class))
                .andExpect(jsonPath("$[2].date", is(DATE)))
                .andExpect(jsonPath("$[2].teacherId", is(1L), Long.class))

                .andExpect(jsonPath("$[3].id", is(4L), Long.class))
                .andExpect(jsonPath("$[3].period", is(1)))
                .andExpect(jsonPath("$[3].subjectId", is(1)))
                .andExpect(jsonPath("$[3].roomId", is(1)))
                .andExpect(jsonPath("$[3].groupId", is(3L), Long.class))
                .andExpect(jsonPath("$[3].date", is(DATE_1)))
                .andExpect(jsonPath("$[3].teacherId", is(1L), Long.class))

                .andExpect(jsonPath("$[4].id", is(5L), Long.class))
                .andExpect(jsonPath("$[4].period", is(2)))
                .andExpect(jsonPath("$[4].subjectId", is(2)))
                .andExpect(jsonPath("$[4].roomId", is(2)))
                .andExpect(jsonPath("$[4].groupId", is(3L), Long.class))
                .andExpect(jsonPath("$[4].date", is(DATE_1_ADD_3_DAYS)))
                .andExpect(jsonPath("$[4].teacherId", is(1L), Long.class))

                .andExpect(jsonPath("$[5].id", is(6L), Long.class))
                .andExpect(jsonPath("$[5].period", is(1)))
                .andExpect(jsonPath("$[5].subjectId", is(3)))
                .andExpect(jsonPath("$[5].roomId", is(3)))
                .andExpect(jsonPath("$[5].groupId", is(3L), Long.class))
                .andExpect(jsonPath("$[5].date", is(DATE_1_ADD_13_DAYS)))
                .andExpect(jsonPath("$[5].teacherId", is(1L), Long.class));
    }

    @Test
    @DataSet(provider = LessonDataset.class, executeScriptsBefore = RESET_LESSON_ID,
            disableConstraints = true, cleanAfter = true)
    void whenGetLessonByValidId_Status200() throws Exception {
        this.mockMvc.perform(get("/api/lessons/{lessonId}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.teacherId", is(1L), Long.class))
                .andExpect(jsonPath("$.subjectId", is(1)))
                .andExpect(jsonPath("$.groupId", is(1L), Long.class))
                .andExpect(jsonPath("$.roomId", is(1)))
                .andExpect(jsonPath("$.date", is(DATE)))
                .andExpect(jsonPath("$.time", is(DATE)));
    }

    @Test
    @DataSet(executeScriptsBefore = RESET_LESSON_ID, disableConstraints = true, cleanAfter = true)
    @ExpectedDataSet(provider = OneLessonDataset.class)
    void whenAddLessonWithValidData_Status200() throws Exception {
        LessonDTO newLesson = new LessonDTO(PERIOD_1, 1, 1, 1, DATE, 1);
        this.mockMvc.perform(post("/api/lessons/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(newLesson)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.teacherId", is(newLesson.getTeacherId()), Long.class))
                .andExpect(jsonPath("$.subjectId", is(newLesson.getSubjectId())))
                .andExpect(jsonPath("$.groupId", is(newLesson.getGroupId()), Long.class))
                .andExpect(jsonPath("$.roomId", is(newLesson.getRoomId())))
                .andExpect(jsonPath("$.date", is(newLesson.getDate())));
    }

    @Test
    @DataSet(provider = LessonDataset.class, executeScriptsBefore = RESET_LESSON_ID,
            disableConstraints = true, cleanAfter = true)
    void whenAddLessonWithDuplicateData_Status400() throws Exception {
        LessonDTO newLesson = new LessonDTO(PERIOD_2, 2L, 2, 2, DATE,
                1L);
        String errorMessage = createDuplicateMessage(newLesson);
        this.mockMvc.perform(post("/api/lessons/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(newLesson)))
                .andExpect(jsonPath(STATUS, is(400)))
                .andExpect(jsonPath(ERRORS, is(errorMessage)));
    }

    @Test
    @DataSet(provider = LessonDataset.class, executeScriptsBefore = RESET_LESSON_ID,
            disableConstraints = true, cleanAfter = true)
    void whenUpdateLessonWithValidData_Status200() throws Exception {
        LessonDTO newLesson = new LessonDTO(1L, PERIOD_1, 2L, 3, 1, DATE_1_ADD_21_DAYS, 1);
        this.mockMvc.perform(put("/api/lessons/{lessonId}", newLesson.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(newLesson)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.teacherId", is(newLesson.getTeacherId()), Long.class))
                .andExpect(jsonPath("$.subjectId", is(newLesson.getSubjectId())))
                .andExpect(jsonPath("$.groupId", is(newLesson.getGroupId()), Long.class))
                .andExpect(jsonPath("$.roomId", is(newLesson.getRoomId())))
                .andExpect(jsonPath("$.date", is(newLesson.getDate())));
    }

    @Test
    @DataSet(provider = LessonDataset.class, executeScriptsBefore = RESET_LESSON_ID,
            disableConstraints = true, cleanAfter = true)
    void whenUpdateLessonWithDuplicateData_Status400() throws Exception {
        LessonDTO newLesson = new LessonDTO(1L, PERIOD_2, 2L, 2, 2, DATE,
                1L);
        String errorMessage = createDuplicateMessage(newLesson);
        this.mockMvc.perform(put("/api/lessons/{lessonId}", newLesson.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(newLesson)))
                .andExpect(jsonPath(STATUS, is(400)))
                .andExpect(jsonPath(ERRORS, is(errorMessage)));
    }

    @Test
    @DataSet(provider = OneLessonDataset.class, executeScriptsBefore = RESET_LESSON_ID,
            disableConstraints = true, cleanAfter = true)
    void whenDeleteLessonByValidId_Status200_LessonDelete() throws Exception {
        this.mockMvc.perform(delete("/api/lessons/{lessonId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/api/lessons/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_ROOT, hasSize(0)));
    }

    @Test
    @DataSet(provider = OneLessonDataset.class, executeScriptsBefore = RESET_LESSON_ID,
            disableConstraints = true, cleanAfter = true)
    void whenDeleteLessonByInValidId_Status404_LessonDelete() throws Exception {
        this.mockMvc.perform(delete("/api/lessons/{lessonId}", INVALID_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath(STATUS).value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath(ERRORS, is(String.format("Failed to delete Lesson by id %d", INVALID_ID))));
    }

    private String createDuplicateMessage(LessonDTO lessonDTO) {
        return String.format(
                "Error! Already exists Lesson {id=%d, period=%d, groupId=%d, subjectId=%d, roomId=%d, time=%s, teacherId=%d, date=%s}",
                lessonDTO.getId(), lessonDTO.getPeriod(), lessonDTO.getGroupId(), lessonDTO.getSubjectId(),
                lessonDTO.getRoomId(), lessonDTO.getTime(), lessonDTO.getTeacherId(), lessonDTO.getDate());
    }
}
