<<<<<<< HEAD
package ua.com.nikiforov.models;

public class Lesson {

    private long id;
    private long groupId;
    protected int subjectId;
    private int roomId;

    public Lesson() {
    }

    public Lesson(long id, long groupId, int subjectId, int roomId) {
        this.id = id;
        this.groupId = groupId;
        this.subjectId = subjectId;
        this.roomId = roomId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (groupId ^ (groupId >>> 32));
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + roomId;
        result = prime * result + subjectId;
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
        Lesson other = (Lesson) obj;
        if (groupId != other.groupId)
            return false;
        if (id != other.id)
            return false;
        if (roomId != other.roomId)
            return false;
        if (subjectId != other.subjectId)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "id=" + id + ", groupId=" + groupId + ", subjectId=" + subjectId + ", roomId=" + roomId;
    }
    
    

}
=======
package ua.com.nikiforov.models;

public class Lesson {

    private long id;
    private long groupId;
    protected int subjectId;
    private int roomId;

    public Lesson() {
    }

    public Lesson(long id, long groupId, int subjectId, int roomId) {
        this.id = id;
        this.groupId = groupId;
        this.subjectId = subjectId;
        this.roomId = roomId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (groupId ^ (groupId >>> 32));
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + roomId;
        result = prime * result + subjectId;
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
        Lesson other = (Lesson) obj;
        if (groupId != other.groupId)
            return false;
        if (id != other.id)
            return false;
        if (roomId != other.roomId)
            return false;
        if (subjectId != other.subjectId)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "id=" + id + ", groupId=" + groupId + ", subjectId=" + subjectId + ", roomId=" + roomId;
    }
    
    

}
>>>>>>> refs/remotes/origin/master
