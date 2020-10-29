<<<<<<< HEAD
package ua.com.nikiforov.models;

public class TeachersSubjects {

    private long teachersId;
    private int subjectId;

    public TeachersSubjects() {
    }

    public TeachersSubjects(long teachersId, int subjectId) {
        this.teachersId = teachersId;
        this.subjectId = subjectId;
    }

    public long getTeachersId() {
        return teachersId;
    }

    public void setTeachersId(long teachersId) {
        this.teachersId = teachersId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + subjectId;
        result = prime * result + (int) (teachersId ^ (teachersId >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TeachersSubjects other = (TeachersSubjects) obj;
        if (subjectId != other.subjectId)
            return false;
        if (teachersId != other.teachersId)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "teachersId=" + teachersId + ", subjectId=" + subjectId;
    }

}
=======
package ua.com.nikiforov.models;

public class TeachersSubjects {

    private long teachersId;
    private int subjectId;

    public TeachersSubjects() {
    }

    public TeachersSubjects(long teachersId, int subjectId) {
        this.teachersId = teachersId;
        this.subjectId = subjectId;
    }

    public long getTeachersId() {
        return teachersId;
    }

    public void setTeachersId(long teachersId) {
        this.teachersId = teachersId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + subjectId;
        result = prime * result + (int) (teachersId ^ (teachersId >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TeachersSubjects other = (TeachersSubjects) obj;
        if (subjectId != other.subjectId)
            return false;
        if (teachersId != other.teachersId)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "teachersId=" + teachersId + ", subjectId=" + subjectId;
    }

}
>>>>>>> refs/remotes/origin/master
