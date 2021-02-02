package ua.com.nikiforov.rest_controllers.lesson_test.data_set_builder;

import com.github.database.rider.core.api.dataset.DataSetProvider;
import com.github.database.rider.core.dataset.builder.DataSetBuilder;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import ua.com.nikiforov.helper.SetupTestHelper;

public class LessonDatasetExpected extends SetupTestHelper implements DataSetProvider {

        @Override
    public IDataSet provide() throws DataSetException {
        return new DataSetBuilder()
                .table("lessons")
                .columns("period","subject_id","room_id","group_id","lesson_date","teacher_id")
                .values(1,1,1,1,DATE,1)
                .values(2,2,2,2,DATE,1)
                .values(3,1,3,2,DATE,1)
                .values(1,1,1,3,DATE_1,1)
                .values(2,2,2,3,DATE_1_ADD_3_DAYS,1)
                .values(1,3,3,3,DATE_1_ADD_13_DAYS,1)
                .build();
    }
}
