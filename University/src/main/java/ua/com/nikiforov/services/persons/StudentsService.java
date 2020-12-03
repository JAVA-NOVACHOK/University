package ua.com.nikiforov.services.persons;

import java.util.List;

import ua.com.nikiforov.controllers.dto.StudentDTO;
import ua.com.nikiforov.models.persons.Student;

public interface StudentsService {

    public boolean addStudent(StudentDTO student);

    public StudentDTO getStudentById(long studentId);

    public List<StudentDTO> getStudentsByGroupId(long groupId);

    public StudentDTO getStudentByName(String firstName, String lastName);

    public StudentDTO getStudentByNameGroupId(String firstName, String lastName, long groupId);

    public List<StudentDTO> getAllStudents();

    public boolean updateStudent(StudentDTO student);

    public boolean deleteStudentById(long studentId);

    public boolean transferStudent(long studentId, long groupIdTo);

}
