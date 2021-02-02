package ua.com.nikiforov.rest_controllers.lesson_test.data_set_builder;

import com.github.database.rider.core.api.dataset.DataSetProvider;
import com.github.database.rider.core.dataset.builder.DataSetBuilder;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import ua.com.nikiforov.helper.SetupTestHelper;

public class PrepareLessonDataset extends SetupTestHelper implements DataSetProvider {

        @Override
    public IDataSet provide() throws DataSetException {
        return new DataSetBuilder()
                .table("subjects")
                .columns("subject_name")
                .values(SUBJECT_NAME_1)

                .table("rooms")
                .columns("room_number","seat_number")
                .values(TEST_ROOM_NUMBER_1,TEST_SEAT_NUMBER_1)

                .table("groups")
                .columns("group_name")
                .values(TEST_GROUP_NAME_1)

                .table("teachers")
                .columns("first_name","last_name")
                .values(TEACHERS_FIRST_NAME_1,TEACHERS_LAST_NAME_1)

                .build();
    }
}
