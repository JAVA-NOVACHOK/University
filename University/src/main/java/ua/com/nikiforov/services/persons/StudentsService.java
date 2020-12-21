package ua.com.nikiforov.services.persons;

import java.util.List;

import ua.com.nikiforov.dto.StudentDTO;

public interface StudentsService {

    public void addStudent(StudentDTO student);

    public StudentDTO getStudentById(long studentId);

    public StudentDTO getStudentByName(String firstName, String lastName);

    public StudentDTO getStudentByNameGroupId(String firstName, String lastName, long groupId);

    public List<StudentDTO> getAllStudents();

    public void updateStudent(StudentDTO student);

    public void deleteStudentById(long studentId);

    public void transferStudent(long studentId, long groupIdTo);

}
