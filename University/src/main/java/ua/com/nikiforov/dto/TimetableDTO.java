package ua.com.nikiforov.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.util.Objects;

public class TimetableDTO implements Comparable<TimetableDTO> {


    private long lessonId;
    @Max(value = 6, message = "Period must be equals or less then 6!")
    @Min(value = 1, message = "Period must be equals or greater then 1!")
    private int period;
    private SubjectDTO subject;
    private GroupDTO group;
    private RoomDTO room;
    private TeacherDTO teacher;
    private LocalDate lessonDate;

    public TimetableDTO() {
    }

    public TimetableDTO(long lessonId, int period, SubjectDTO subject,
                        GroupDTO group, RoomDTO room, TeacherDTO teacher, LocalDate lessonDate) {
        this.lessonId = lessonId;
        this.period = period;
        this.subject = subject;
        this.group = group;
        this.room = room;
        this.teacher = teacher;
        this.lessonDate = lessonDate;
    }

    public long getLessonId() {
        return lessonId;
    }

    public void setLessonId(long lessonId) {
        this.lessonId = lessonId;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public SubjectDTO getSubject() {
        return subject;
    }

    public String getDate(){
        String[] dateArr = lessonDate.toString().split("T");
        return dateArr[0];
    }

    public void setSubject(SubjectDTO subject) {
        this.subject = subject;
    }

    public GroupDTO getGroup() {
        return group;
    }

    public void setGroup(GroupDTO group) {
        this.group = group;
    }

    public RoomDTO getRoom() {
        return room;
    }

    public void setRoom(RoomDTO room) {
        this.room = room;
    }

    public TeacherDTO getTeacher() {
        return teacher;
    }

    public void setTeacher(TeacherDTO teacher) {
        this.teacher = teacher;
    }

    public LocalDate getLessonDate() {
        return lessonDate;
    }

    public void setLessonDate(LocalDate lessonDate) {
        this.lessonDate = lessonDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimetableDTO that = (TimetableDTO) o;
        return lessonId == that.lessonId &&
                period == that.period &&
                subject.equals(that.subject) &&
                group.equals(that.group) &&
                room.equals(that.room) &&
                teacher.equals(that.teacher) &&
                lessonDate.equals(that.lessonDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lessonId, period, subject, group, room, teacher, lessonDate);
    }

    @Override
    public String toString() {
        return "TimetableDTO{" +
                "lessonId=" + lessonId +
                ", period=" + period +
                ", subject=" + subject +
                ", group=" + group +
                ", room=" + room +
                ", teacher=" + teacher +
                ", time_=" + lessonDate +
                '}';
    }

    @Override
    public int compareTo(TimetableDTO o) {
        if (period > o.getPeriod()) {
            return 1;
        } else
            return 0;
    }

}
