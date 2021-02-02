package ua.com.nikiforov.services.subject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ua.com.nikiforov.dto.SubjectDTO;
import ua.com.nikiforov.dto.TeacherDTO;
import ua.com.nikiforov.helper.SetupTestHelper;
import ua.com.nikiforov.services.persons.TeacherService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

class SubjectServiceImplTest extends SetupTestHelper {

    private static final String SUBJECT_NAME_1 = "Math";
    private static final String SUBJECT_NAME_2 = "Programming";
    private static final String SUBJECT_NAME_3 = "Cybersecurity";
    private static final String SUBJECT_NAME_4 = "Java";
    private static final String SUBJECT_NAME_5 = "WordPress";

    private static final String FIRST_NAME_1 = "Tom";
    private static final String FIRST_NAME_2 = "Bill";
    private static final String FIRST_NAME_3 = "Jack";

    private static final String LAST_NAME_1 = "Hanks";
    private static final String LAST_NAME_2 = "Clinton";
    private static final String LAST_NAME_3 = "Sparrow";

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private SubjectService subjectService;

    private TeacherDTO teacher_1;
    private TeacherDTO teacher_2;
    private TeacherDTO teacher_3;

    private SubjectDTO subject_1;
    private SubjectDTO subject_2;
    private SubjectDTO subject_3;

    @BeforeEach
    void startup() {

        teacher_1 = insertTeacher(FIRST_NAME_1, LAST_NAME_1);
        teacher_2 = insertTeacher(FIRST_NAME_2, LAST_NAME_2);
        teacher_3 = insertTeacher(FIRST_NAME_3, LAST_NAME_3);

        subject_1 = insertSubject(SUBJECT_NAME_1);
        subject_2 = insertSubject(SUBJECT_NAME_2);
        subject_3 = insertSubject(SUBJECT_NAME_3);

    }

    @Test
    void whenGetAllSubjectsIfPresentReturnListOfAllSubjects() {

        List<SubjectDTO> expectedSubjects = new ArrayList<>();
        expectedSubjects.add(subject_3);
        expectedSubjects.add(subject_1);
        expectedSubjects.add(subject_2);

        List<SubjectDTO> actualSubjects = subjectService.getAllSubjects();
        assertIterableEquals(expectedSubjects, actualSubjects);
    }

    @Test
    void whenAddSubjectIfSuccessReturnTrue() {
        assertDoesNotThrow(() -> subjectService.addSubject(new SubjectDTO(SUBJECT_NAME_4)));
    }

    @Test
    void afetrAddSubjectGetSubjectByIdReturnCorrectSubject() {
        assertEquals(subject_1, subjectService.getSubjectById(subject_1.getId()));
    }

    @Test
    void whenUpdateSubjectByIdIfSuccessThenReturnTrue() {
        assertDoesNotThrow(() -> subjectService.updateSubject(new SubjectDTO(subject_2.getId(), SUBJECT_NAME_5)));
    }

    @Test
    void whenUpdateSubjectThenSubjectHasUpdatedName() {
        SubjectDTO updatedSubject = new SubjectDTO(subject_2.getId(), SUBJECT_NAME_2);
        subjectService.updateSubject(updatedSubject);
        SubjectDTO actualSubject = subjectService.getSubjectById(subject_2.getId());
        assertEquals(updatedSubject, actualSubject);
    }

    @Test
    void whenDeleteSubjectByIdIfSuccessThenReturnTrue() {
        assertDoesNotThrow(() -> subjectService.deleteSubjectById(subject_2.getId()));
    }

    @Test
    void afterDeleteSubjectByIdNoSubjectInAllSubjects() {
        List<SubjectDTO> expectedSubjects = new ArrayList<>();
        expectedSubjects.add(subject_1);
        expectedSubjects.add(subject_2);

        subjectService.deleteSubjectById(subject_3.getId());
        List<SubjectDTO> actualSubjects = subjectService.getAllSubjects();
        assertIterableEquals(expectedSubjects, actualSubjects);
    }

    @Test
    void afterAssignSubjectsToTeachers_SubjectHasListOfTeachers() {

        Set<TeacherDTO> expectedTeachers = new TreeSet<>();
        expectedTeachers.add(teacher_2);
        expectedTeachers.add(teacher_1);
        expectedTeachers.add(teacher_3);

        teacherService.assignSubjectToTeacher(teacher_1.getId(), subject_1.getId());
        teacherService.assignSubjectToTeacher(teacher_2.getId(), subject_1.getId());
        teacherService.assignSubjectToTeacher(teacher_3.getId(), subject_1.getId());

        subject_1 = subjectService.getSubjectById(subject_1.getId());
        Set<TeacherDTO> actualTeachers = subject_1.getTeachers();
        assertIterableEquals(expectedTeachers, actualTeachers);
    }

}
