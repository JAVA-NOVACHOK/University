package ua.com.nikiforov.models.lesson;

import ua.com.nikiforov.models.Group;
import ua.com.nikiforov.models.Room;
import ua.com.nikiforov.models.Subject;
import ua.com.nikiforov.models.persons.Teacher;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "lessons",uniqueConstraints = @UniqueConstraint(columnNames = {"group_id","subject_id","room_id","lesson_date","period","teacher_id"}))
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "lesson_id")
    private long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;
    @Column(name = "lesson_date")
    private LocalDate lessonDate;
    private int period;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    public Lesson() {
    }

    public Lesson(Group group, Subject subject, Room room, LocalDate time, int period, Teacher teacher) {
        this.group = group;
        this.subject = subject;
        this.room = room;
        this.lessonDate = time;
        this.period = period;
        this.teacher = teacher;
    }

    public Lesson(long id, Group group, Subject subject, Room room, LocalDate time, int period, Teacher teacher) {
        this.id = id;
        this.group = group;
        this.subject = subject;
        this.room = room;
        this.lessonDate = time;
        this.period = period;
        this.teacher = teacher;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public LocalDate getLessonDate() {
        return lessonDate;
    }

    public void setLessonDate(LocalDate time) {
        this.lessonDate = time;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lesson lesson = (Lesson) o;
        return id == lesson.id &&
                period == lesson.period &&
                group.equals(lesson.group) &&
                subject.equals(lesson.subject) &&
                room.equals(lesson.room) &&
                lessonDate.equals(lesson.lessonDate) &&
                teacher.equals(lesson.teacher);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, group, subject, room, lessonDate, period, teacher);
    }
}
