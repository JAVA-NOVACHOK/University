<<<<<<< HEAD
package ua.com.nikiforov.dao.subject;

import java.util.List;

import ua.com.nikiforov.models.Subject;

public interface SubjectDAO {

    public boolean addSubject(String subjectName);

    public Subject getSubjectById(int subjectId);
    
    public Subject getSubjectByName(String subjectName);

    public List<Subject> getAllSubjects();

    public boolean updateSubject(String subjectName, int subjectId);

    public boolean deleteSubjectById(int subjectId);
}
=======
package ua.com.nikiforov.dao.subject;

import java.util.List;

import ua.com.nikiforov.models.Subject;

public interface SubjectDAO {

    public boolean addSubject(String subjectName);

    public Subject getSubjectById(int subjectId);
    
    public Subject getSubjectByName(String subjectName);

    public List<Subject> getAllSubjects();

    public boolean updateSubject(String subjectName, int subjectId);

    public boolean deleteSubjectById(int subjectId);
}
>>>>>>> refs/remotes/origin/master
