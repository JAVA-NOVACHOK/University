package ua.com.nikiforov.services.timetables;

import java.util.Comparator;

import ua.com.nikiforov.models.timetable.Timetable;

public class CompareByPeriod implements Comparator<Timetable> {

    @Override
    public int compare(Timetable t1, Timetable t2) {
        if (t1.getPeriod() > t2.getPeriod()) {
            return 1;
        } else {
            return 0;
        }
    }

    

}
