package ua.com.nikiforov.models;

import java.util.ArrayList;
import java.util.List;

public class Timetable {
    
    private List<Lesson> lessons;
    
    public Timetable() {
        lessons = new ArrayList<>();
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }
    
}
