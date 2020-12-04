package ua.com.nikiforov.dao.table_creator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.com.nikiforov.config.DatabaseConfig;

@Repository
public class TableCreator {
    
    private static final String PATH = "src/test/resources/script_create_tables.sql";
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public TableCreator(@Qualifier("test") DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void createTables() {
        try (BufferedReader readerFromScriptFile = new BufferedReader(new FileReader(PATH))) {
            StringBuilder queryCreateTables = new StringBuilder();
            String line = "";
            while ((line = readerFromScriptFile.readLine()) != null) {
                queryCreateTables.append(line);
            }
            executeScript(queryCreateTables.toString());
        } catch (FileNotFoundException e) {
            throw new TableCreatorException("Couldn't find file " + PATH, e);
        } catch (IOException ex) {
            throw new TableCreatorException("Error heppend when reading file!!!", ex);
        }
    }

    private void executeScript(String script) {
        
        jdbcTemplate.execute(script);
    }
}
