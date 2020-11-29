package ua.com.nikiforov.services.persons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;

import ua.com.nikiforov.config.DatabaseConfig;
import ua.com.nikiforov.controllers.dto.TeacherDTO;
import ua.com.nikiforov.dao.table_creator.TableCreator;
import ua.com.nikiforov.exceptions.EntityNotFoundException;
import ua.com.nikiforov.models.Subject;
import ua.com.nikiforov.models.persons.Teacher;
import ua.com.nikiforov.services.subject.SubjectService;

@SpringJUnitConfig(DatabaseConfig.class)
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
class TeacherServiceImplTest {

    private static final String FIRST_NAME_1 = "Tom";
    private static final String FIRST_NAME_2 = "Bill";
    private static final String FIRST_NAME_3 = "Jack";

    private static final String LAST_NAME_1 = "Hanks";
    private static final String LAST_NAME_2 = "Clinton";
    private static final String LAST_NAME_3 = "Sparrow";

    private static final String SUBJECT_NAME_1 = "Math";
    private static final String SUBJECT_NAME_2 = "Programming";
    private static final String SUBJECT_NAME_3 = "Cybersecurity";

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private TableCreator tableCreator;

    @BeforeEach
    void init() {
        tableCreator.createTables();
    }

    @Test
    void whenAddTEacherIfSuccessReturnTrue() {
        assertTrue(teacherService.addTeacher(FIRST_NAME_1, LAST_NAME_1));
    }

    @Test
    void afterAddTeacherReturnCorrectTeacherObject() {
        Teacher expectedTeacher = insertTeacher(FIRST_NAME_1, LAST_NAME_1);
        assertEquals(expectedTeacher, teacherService.getTeacherById(expectedTeacher.getId()));
    }

    @Test
    void whenGetAllTeachersReturnListOfAllTeachers() {
        List<Teacher> expectedTeachers = new ArrayList<>();
        expectedTeachers.add(insertTeacher(FIRST_NAME_1, LAST_NAME_1));
        expectedTeachers.add(insertTeacher(FIRST_NAME_2, LAST_NAME_2));
        expectedTeachers.add(insertTeacher(FIRST_NAME_3, LAST_NAME_3));
        Collections.sort(expectedTeachers);
        assertIterableEquals(expectedTeachers, teacherService.getAllTeachers());
    }

    @Test
    void whenUpdateTeacherIfSuccessThenReturnTrue() {
        long teacherId = insertTeacher(FIRST_NAME_1, LAST_NAME_1).getId();
        assertTrue(teacherService.updateTeacher(new TeacherDTO(teacherId, FIRST_NAME_2, LAST_NAME_2)));
    }

    @Test
    void afterUpdateTeacherThenTeacherHasUpdatedTeacher() {
        long teacherId = insertTeacher(FIRST_NAME_1, LAST_NAME_1).getId();
        teacherService.updateTeacher(new TeacherDTO(teacherId,FIRST_NAME_2, LAST_NAME_2));
        Teacher expectedTeacher = teacherService.getTeacherByName(FIRST_NAME_2, LAST_NAME_2);
        Teacher actualTeacher = teacherService.getTeacherById(teacherId);
        assertEquals(expectedTeacher, actualTeacher);
    }

    @Test
    void whenDeleteTeacherByIdIfSuccessThenReturnTrue() {
        Teacher teacher = insertTeacher(FIRST_NAME_1, LAST_NAME_1);
        assertTrue(teacherService.deleteTeacherById(teacher.getId()));
    }

    @Test
    void afterDeleteTeacherByIdIfSearchReturnEntityNotFoundException() {
        Teacher teacher = insertTeacher(FIRST_NAME_1, LAST_NAME_1);
        long teacherId = teacher.getId();
        teacherService.deleteTeacherById(teacherId);
        assertThrows(EntityNotFoundException.class, () -> teacherService.getTeacherById(teacherId));
    }

    @Test
    void whenAssignSubjectToTeacherIfSuccessReturnTrue() {
        teacherService.addTeacher(FIRST_NAME_1, LAST_NAME_1);
        subjectService.addSubject(SUBJECT_NAME_1);
        assertTrue(teacherService.assignSubjectToTeacher(1, 1));
    }

    @Test
    void afterAssignSubjectsToTeacher_ItHasListOfSubjectIds() {
        Teacher teacher = insertTeacher(FIRST_NAME_1, LAST_NAME_1);
        long teacherId = teacher.getId();

        Subject subjectOne = insertSubject(SUBJECT_NAME_1);
        int subjectOneId = subjectOne.getId();
        Subject subjectTwo = insertSubject(SUBJECT_NAME_2);
        int subjectTwoId = subjectTwo.getId();
        Subject subjectThree = insertSubject(SUBJECT_NAME_3);
        int subjectThreeId = subjectThree.getId();

        List<Subject> expectedSubjects = new ArrayList<>();
        expectedSubjects.add(subjectOne);
        expectedSubjects.add(subjectTwo);
        expectedSubjects.add(subjectThree);

        teacherService.assignSubjectToTeacher(subjectOneId, teacherId);
        teacherService.assignSubjectToTeacher(subjectTwoId, teacherId);
        teacherService.assignSubjectToTeacher(subjectThreeId, teacherId);

        Teacher teacherAfterAssignSubjects = teacherService.getTeacherById(teacherId);
        List<Subject> actualSubjects = teacherAfterAssignSubjects.getSubjects();
        assertIterableEquals(expectedSubjects, actualSubjects);
    }

    @Test
    void afterUnAssignSubjectsFromTeacher_HeHasNoSubjectInListOfSubjects() {
        Teacher teacher = insertTeacher(FIRST_NAME_1, LAST_NAME_1);
        long teacherId = teacher.getId();

        Subject subjectOne = insertSubject(SUBJECT_NAME_1);
        int subjectOneId = subjectOne.getId();
        Subject subjectTwo = insertSubject(SUBJECT_NAME_2);
        int subjectTwoId = subjectTwo.getId();
        Subject subjectThree = insertSubject(SUBJECT_NAME_3);
        int subjectThreeId = subjectThree.getId();

        List<Subject> expectedSubjects = new ArrayList<>();
        expectedSubjects.add(subjectOne);
        expectedSubjects.add(subjectTwo);
       
        teacherService.assignSubjectToTeacher(subjectOneId, teacherId);
        teacherService.assignSubjectToTeacher(subjectTwoId, teacherId);
        teacherService.assignSubjectToTeacher(subjectThreeId, teacherId);

        teacherService.unassignSubjectFromTeacher(subjectThreeId, teacherId);

        Teacher teacherAfterAssignSubjects = teacherService.getTeacherById(teacherId);
        List<Subject> actualSubjects = teacherAfterAssignSubjects.getSubjects();
        assertIterableEquals(expectedSubjects, actualSubjects);
    }

    private Subject insertSubject(String subjectName) {
        subjectService.addSubject(subjectName);
        return subjectService.getSubjectByName(subjectName);
    }

    private Teacher insertTeacher(String firstName, String lastName) {
        teacherService.addTeacher(firstName, lastName);
        return teacherService.getTeacherByName(firstName, lastName);
    }

}
