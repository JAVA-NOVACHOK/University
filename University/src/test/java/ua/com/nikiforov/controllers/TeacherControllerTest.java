package ua.com.nikiforov.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ua.com.nikiforov.dto.GroupDTO;
import ua.com.nikiforov.dto.RoomDTO;
import ua.com.nikiforov.dto.SubjectDTO;
import ua.com.nikiforov.dto.TeacherDTO;
import ua.com.nikiforov.helper.SetupTestHelper;
import ua.com.nikiforov.services.group.GroupService;
import ua.com.nikiforov.services.persons.StudentsService;
import ua.com.nikiforov.services.persons.TeacherService;
import ua.com.nikiforov.services.room.RoomService;
import ua.com.nikiforov.services.subject.SubjectService;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class TeacherControllerTest extends SetupTestHelper {

    private static final String FIRST_NAME_1 = "Tom";
    private static final String FIRST_NAME_2 = "Bill";
    private static final String FIRST_NAME_3 = "Jack";
    private static final String FIRST_NAME_4 = "Bob";
    private static final String MICHEL_FIRST_NAME = "Michel";

    private static final String LAST_NAME_1 = "Hanks";
    private static final String LAST_NAME_2 = "Clinton";
    private static final String LAST_NAME_3 = "Sparrow";
    private static final String LAST_NAME_4 = "Marley";
    private static final String JACKSON_LAST_NAME = "Jackson";

    private static final String TEST_GROUP_NAME_1 = "AA-12";
    private static final String TEST_GROUP_NAME_2 = "AA-13";
    private static final String TEST_GROUP_NAME_3 = "AA-14";

    private static final int TEST_ROOM_NUMBER_1 = 12;
    private static final int TEST_ROOM_NUMBER_2 = 13;
    private static final int TEST_ROOM_NUMBER_3 = 14;

    private static final int TEST_SEAT_NUMBER_1 = 20;

    private static final long INVALID_ID = 100500l;

    private static final String SUBJECT_NAME_1 = "Math";
    private static final String SUBJECT_NAME_2 = "Programming";
    private static final String SUBJECT_NAME_3 = "Cybersecurity";

    private static final String FIRST_NAME_PARAM = "firstName";
    private static final String LAST_NAME_PARAM = "lastName";

    private static final String TEACHER_ATTR = "teacher";
    private static final String TEACHERS_ATTR = "teachers";
    private static final String SUBJECTS_ATTR = "subjects";
    private static final String ROOMS_ATTR = "rooms";
    private static final String GROUPS_ATTR = "groups";
    private static final String SUBJECT_ID_ATTR = "subjectId";
    private static final String TEACHER_ID_ATTR = "teacherId";
    private static final String ID = "id";
    private static final String SUCCESS_MSG = "success";
    private static final String FAIL_MSG = "failMessage";
    private static final String STR = "";

    private static final String TEACHERS_VIEW = "teachers/teachers";
    private static final String ONE_TEACHER = "teachers/one_teacher";

    private TeacherDTO teacher_1;
    private TeacherDTO teacher_2;
    private TeacherDTO teacher_3;

    private RoomDTO room_1;
    private RoomDTO room_2;
    private RoomDTO room_3;

    private GroupDTO group_1;
    private GroupDTO group_2;
    private GroupDTO group_3;

    private SubjectDTO subject_1;
    private SubjectDTO subject_2;
    private SubjectDTO subject_3;

    @Autowired
    private RoomService roomService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private StudentsService studentsService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        teacher_1 = insertTeacher(FIRST_NAME_1, LAST_NAME_1);
        teacher_2 = insertTeacher(FIRST_NAME_2, LAST_NAME_2);
        teacher_3 = insertTeacher(FIRST_NAME_3, LAST_NAME_3);

        room_1 = insertRoom(TEST_ROOM_NUMBER_1, TEST_SEAT_NUMBER_1);
        room_2 = insertRoom(TEST_ROOM_NUMBER_2, TEST_SEAT_NUMBER_1);
        room_3 = insertRoom(TEST_ROOM_NUMBER_3, TEST_SEAT_NUMBER_1);

        group_1 = insertGroup(TEST_GROUP_NAME_1);
        group_2 = insertGroup(TEST_GROUP_NAME_2);
        group_3 = insertGroup(TEST_GROUP_NAME_3);

        subject_1 = insertSubject(SUBJECT_NAME_1);
        subject_2 = insertSubject(SUBJECT_NAME_2);
        subject_3 = insertSubject(SUBJECT_NAME_3);
    }

    @Test
    void getTeachersURI_ReturnTeachersView() throws Exception {
        this.mockMvc.perform(get("/teachers")).andExpect(view().name(TEACHERS_VIEW));
    }

    @Test
    void deleteTeacher_ifGiveValidId_ReturnTeachersView_DeleteTeacher() throws Exception {

        this.mockMvc.perform(get("/teachers/delete").param(ID, teacher_2.getId() + STR)).andExpect(status().isOk())
                .andExpect(model().attributeExists(SUCCESS_MSG))
                .andExpect(model().attribute(TEACHERS_ATTR, hasItems(teacher_1, teacher_3)))
                .andExpect(model().attribute(SUBJECTS_ATTR, hasItems(subject_1, subject_2, subject_3)))
                .andExpect(view().name(TEACHERS_VIEW));
    }

    @Test
    void getTeacherURI_ReturnOneTeacherView_WithAttrs() throws Exception {
        this.mockMvc.perform(get("/teachers/teacher").param(ID, teacher_1.getId() + STR)).andDo(print())
                .andExpect(status().isOk()).andExpect(model().attribute(TEACHER_ATTR, teacher_1))
                .andExpect(model().attribute(ROOMS_ATTR, hasItems(room_1, room_2, room_3)))
                .andExpect(model().attribute(GROUPS_ATTR, hasItems(group_1, group_2, group_3)))
                .andExpect(model().attribute(SUBJECTS_ATTR, hasItems(subject_3, subject_1, subject_2)))
                .andExpect(view().name(ONE_TEACHER));
    }

    @Test
    void addTeacherURI_ReturnTeachersView_Teachers() throws Exception {
        this.mockMvc
                .perform(
                        post("/teachers/add")
                                .param(FIRST_NAME_PARAM, FIRST_NAME_4)
                                .param(LAST_NAME_PARAM, LAST_NAME_4))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(model().attributeExists(SUCCESS_MSG))
                .andExpect(model().attribute(SUBJECTS_ATTR, hasItems(subject_1, subject_2, subject_3)))
                .andExpect(model().attribute(TEACHERS_ATTR,
                        hasItems(teacherService.getTeacherById(teacher_1.getId()), teacherService.getTeacherByName(FIRST_NAME_4, LAST_NAME_4), teacher_3)))
                .andExpect(view().name(TEACHERS_VIEW));
    }

    @Test
    void editTeacherURI_ReturnTeachersView_EditedTeacher() throws Exception {
        TeacherDTO updatedTeacher = new TeacherDTO(teacher_1.getId(), FIRST_NAME_2, LAST_NAME_1);
        this.mockMvc
                .perform(
                        post("/teachers/edit")
                                .param(ID, teacher_1.getId() + STR)
                                .param(FIRST_NAME_PARAM, FIRST_NAME_2)
                                .param(LAST_NAME_PARAM, LAST_NAME_1)
                                .sessionAttr(TEACHER_ATTR, teacher_1))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists(SUCCESS_MSG))
                .andExpect(model().attribute(TEACHER_ATTR, updatedTeacher))
                .andExpect(view().name(ONE_TEACHER));
    }


    @Test
    void assignSubjectToTeacher_thenReturnSuccessAssignedSubject() throws Exception {
        teacher_1.addSubject(subject_1);

        this.mockMvc
                .perform(post("/teachers/assign/").param(SUBJECT_ID_ATTR, subject_1.getId() + STR).param(TEACHER_ID_ATTR,
                        teacher_1.getId() + STR))
                .andExpect(status().isOk()).andExpect(model().attributeExists(SUCCESS_MSG))
                .andExpect(model().attribute(TEACHER_ATTR, teacherService.getTeacherById(teacher_1.getId())))
                .andExpect(view().name(ONE_TEACHER));
    }

    @Test
    void unassignSubjectFromTeacher() throws Exception {
        teacherService.assignSubjectToTeacher(teacher_1.getId(), subject_1.getId());
        teacherService.assignSubjectToTeacher(teacher_1.getId(), subject_2.getId());

        this.mockMvc
                .perform(get("/teachers/unassign/").param(SUBJECT_ID_ATTR, subject_2.getId() + STR)
                        .param(TEACHER_ID_ATTR, teacher_1.getId() + STR))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists(SUCCESS_MSG))
                .andExpect(model().attribute(TEACHER_ATTR, teacherService.getTeacherById(teacher_1.getId()))).andExpect(view().name(ONE_TEACHER));
    }

    @Test
    void findTeacherByLikeNames() throws Exception {
        TeacherDTO searchingTeacher = insertTeacher(MICHEL_FIRST_NAME, JACKSON_LAST_NAME);

        this.mockMvc.perform(post("/teachers/find/").param(FIRST_NAME_PARAM, "Mi").param(LAST_NAME_PARAM, "ja"))
                .andExpect(status().isOk()).andExpect(model().attribute(TEACHERS_ATTR, hasItems(searchingTeacher)))
                .andExpect(view().name(TEACHERS_VIEW));
    }

}
