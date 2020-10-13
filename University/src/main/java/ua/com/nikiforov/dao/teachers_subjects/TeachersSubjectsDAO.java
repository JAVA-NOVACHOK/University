package ua.com.nikiforov.dao.teachers_subjects;

import java.util.List;

public interface TeachersSubjectsDAO {

    public List<Long> getTeachersIds(int subjectId);
    
    public List<Integer> getSubjectsIds(long teacherId);
    
    public boolean assignSubjectToTeacher(long teacherId, int subjectId);

    public boolean unassignSubjectFromTeacher(long teacherId, int subjectId);    
    
    
}
