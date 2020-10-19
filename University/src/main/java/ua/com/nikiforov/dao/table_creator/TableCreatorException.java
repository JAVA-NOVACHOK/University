package ua.com.nikiforov.dao.table_creator;

public class TableCreatorException extends RuntimeException {

    public TableCreatorException(String massege, Exception e) {
        super(massege, e);
    }

}
