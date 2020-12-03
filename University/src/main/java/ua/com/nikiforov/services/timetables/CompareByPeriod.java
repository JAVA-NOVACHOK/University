package ua.com.nikiforov.services.timetables;

import java.util.Comparator;

import ua.com.nikiforov.controllers.dto.TimetableDTO;

public class CompareByPeriod implements Comparator<TimetableDTO> {

    @Override
    public int compare(TimetableDTO t1, TimetableDTO t2) {
        if (t1.getPeriod() > t2.getPeriod()) {
            return 1;
        } else {
            return 0;
        }
    }

}
