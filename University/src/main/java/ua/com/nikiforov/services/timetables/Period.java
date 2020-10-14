package ua.com.nikiforov.services.timetables;

public enum Period {
    
    FIRST(1),
    SECOND(2),
    THIRD(3),
    FOURTH(4),
    FIFTH(5);
    
    private final int period;
    
    private Period(int period) {
        this.period = period;
    }
    
    public int getPeriod() {
        return period;
    }

}
