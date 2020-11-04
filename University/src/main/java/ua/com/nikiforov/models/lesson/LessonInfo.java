package ua.com.nikiforov.models.lesson;

import ua.com.nikiforov.models.Group;
import ua.com.nikiforov.models.Room;
import ua.com.nikiforov.models.Subject;

public class LessonInfo {

    private Subject subject;
    private Room room;
    private Group group;

    public LessonInfo() {
    }

    public LessonInfo( Subject subject, Room room, Group group) {
        this.subject = subject;
        this.room = room;
        this.group = group;
    }


    public Subject getSubject() {
        return subject;
    }
    
    public String getSubjectName() {
        return subject.getName();
    }
    

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Room getRoom() {
        return room;
    }

    public int getRoomNumber() {
        return room.getRoomNumber();
    }
    public void setRoom(Room room) {
        this.room = room;
    }

    public Group getGroup() {
        return group;
    }

    public String getGroupName() {
        return group.getGroupName();
    }
    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((group == null) ? 0 : group.hashCode());
        result = prime * result + ((room == null) ? 0 : room.hashCode());
        result = prime * result + ((subject == null) ? 0 : subject.hashCode());
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
        if (group == null) {
            if (other.group != null)
                return false;
        } else if (!group.equals(other.group))
            return false;
        if (room == null) {
            if (other.room != null)
                return false;
        } else if (!room.equals(other.room))
            return false;
        if (subject == null) {
            if (other.subject != null)
                return false;
        } else if (!subject.equals(other.subject))
            return false;
        return true;
    }

}
