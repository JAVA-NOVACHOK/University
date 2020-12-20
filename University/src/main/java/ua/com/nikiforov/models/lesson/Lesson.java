package ua.com.nikiforov.models.lesson;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "lessons",uniqueConstraints = @UniqueConstraint(columnNames = {"group_id","subject_id","room_id","time","period","teacher_id"}))
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "lesson_id")
    private long id;
    @Column(name = "group_id")
    private long groupId;
    @Column(name = "subject_id")
    protected int subjectId;
    @Column(name = "room_id")
    private int roomId;
    private LocalDate time;
    private int period;
    @Column(name = "teacher_id")
    private long teacherId;

    public Lesson() {
    }

    public Lesson(int period,long groupId, int subjectId, int roomId, LocalDate time, long teacherId) {
        this.groupId = groupId;
        this.subjectId = subjectId;
        this.roomId = roomId;
        this.time = time;
        this.period = period;
        this.teacherId = teacherId;
    }

    public Lesson(long id, int period, long groupId, int subjectId, int roomId, LocalDate time, long teacherId) {
        this.id = id;
        this.groupId = groupId;
        this.subjectId = subjectId;
        this.roomId = roomId;
        this.time = time;
        this.period = period;
        this.teacherId = teacherId;

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


    public LocalDate getTime() {
        return time;
    }

    public void setTime(LocalDate time) {
        this.time = time;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(long teacherId) {
        this.teacherId = teacherId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (groupId ^ (groupId >>> 32));
        result = prime * result + (int) (teacherId ^ (teacherId >>> 32));
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + period;
        result = prime * result + roomId;
        result = prime * result + subjectId;
        result = prime * result + ((time == null) ? 0 : time.hashCode());
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
        if (teacherId != other.teacherId)
            return false;
        if (id != other.id)
            return false;
        if (period != other.period)
            return false;
        if (roomId != other.roomId)
            return false;
        if (subjectId != other.subjectId)
            return false;
        if (time == null) {
            if (other.time != null)
                return false;
        } else if (!time.equals(other.time))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "id=" + id +
                ", groupId=" + groupId +
                ", subjectId=" + subjectId +
                ", roomId=" + roomId +
                ", time=" + time +
                ", period=" + period +
                ", teacherId=" + teacherId +
                '}';
    }
}
