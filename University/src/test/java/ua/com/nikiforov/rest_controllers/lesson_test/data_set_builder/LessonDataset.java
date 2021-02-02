package ua.com.nikiforov.rest_controllers.lesson_test.data_set_builder;

import com.github.database.rider.core.api.dataset.DataSetProvider;
import com.github.database.rider.core.dataset.builder.DataSetBuilder;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import ua.com.nikiforov.helper.SetupTestHelper;

public class LessonDataset extends SetupTestHelper implements DataSetProvider {

        @Override
    public IDataSet provide() throws DataSetException {
        return new DataSetBuilder()
                .table("subjects")
                .columns("subject_name")
                .values(SUBJECT_NAME_1)
                .values(SUBJECT_NAME_2)
                .values(SUBJECT_NAME_3)

                .table("rooms")
                .columns("room_number","seat_number")
                .values(TEST_ROOM_NUMBER_1,TEST_SEAT_NUMBER_1)
                .values(TEST_ROOM_NUMBER_2,TEST_SEAT_NUMBER_2)
                .values(TEST_ROOM_NUMBER_3,TEST_SEAT_NUMBER_3)

                .table("groups")
                .columns("group_name")
                .values(TEST_GROUP_NAME_1)
                .values(TEST_GROUP_NAME_2)
                .values(TEST_GROUP_NAME_3)

                .table("teachers")
                .columns("first_name","last_name")
                .values(TEACHERS_FIRST_NAME_1,TEACHERS_LAST_NAME_1)
                .values(TEACHERS_FIRST_NAME_2,TEACHERS_LAST_NAME_2)
                .values(TEACHERS_FIRST_NAME_3,TEACHERS_LAST_NAME_3)

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
