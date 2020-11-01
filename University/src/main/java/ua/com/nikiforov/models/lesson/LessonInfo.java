package ua.com.nikiforov.models.lesson;

public class LessonInfo {

    private long lessonId;
    protected String subjectName;
    private int roomNamber;
    private String groupName;

    public LessonInfo() {
    }

    public LessonInfo(long lessonId, String subjectName, int roomNamber, String groupName) {
        this.lessonId = lessonId;
        this.subjectName = subjectName;
        this.roomNamber = roomNamber;
        this.groupName = groupName;
    }

    public long getLessonId() {
        return lessonId;
    }

    public void setLessonId(long lessonId) {
        this.lessonId = lessonId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getRoomNamber() {
        return roomNamber;
    }

    public void setRoomNamber(int roomNamber) {
        this.roomNamber = roomNamber;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((groupName == null) ? 0 : groupName.hashCode());
        result = prime * result + (int) (lessonId ^ (lessonId >>> 32));
        result = prime * result + roomNamber;
        result = prime * result + ((subjectName == null) ? 0 : subjectName.hashCode());
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
        LessonInfo other = (LessonInfo) obj;
        if (groupName == null) {
            if (other.groupName != null)
                return false;
        } else if (!groupName.equals(other.groupName))
            return false;
        if (lessonId != other.lessonId)
            return false;
        if (roomNamber != other.roomNamber)
            return false;
        if (subjectName == null) {
            if (other.subjectName != null)
                return false;
        } else if (!subjectName.equals(other.subjectName))
            return false;
        return true;
    }

   

}