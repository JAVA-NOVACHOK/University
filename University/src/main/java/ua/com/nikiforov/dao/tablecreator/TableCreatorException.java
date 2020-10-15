package ua.com.nikiforov.dao.tablecreator;

public class TableCreatorException extends RuntimeException {

    public TableCreatorException(String massege, Exception e) {
        super(massege, e);
    }

}
