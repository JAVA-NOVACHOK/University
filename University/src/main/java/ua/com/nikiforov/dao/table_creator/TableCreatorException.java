package ua.com.nikiforov.dao.table_creator;

public class TableCreatorException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public TableCreatorException(String massege, Exception e) {
        super(massege, e);
    }

}
