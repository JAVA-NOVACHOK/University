package ua.com.nikiforov.dao;

public class SqlConstants {

    private SqlConstants() {
    }

    public static final String INSERT = "INSERT INTO ";
    public static final String SELECT = "SELECT ";
    public static final String DELETE = "DELETE ";
    public static final String UPDATE = "UPDATE ";
    public static final String SET = " SET ";
    public static final String FROM = " FROM ";
    public static final String AND = " AND ";
    public static final String WHERE = " WHERE ";
    public static final String BETWEEN = " BETWEEN ";
    public static final String VALUES_1_QMARK = ") VALUES(?)";
    public static final String VALUES_2_QMARK = ") VALUES(?,?)";
    public static final String VALUES_3_QMARK = ") VALUES(?,?,?)";
    public static final String VALUES_4_QMARK = ") VALUES(?,?,?,?)";
    public static final String VALUES_6_QMARK = ") VALUES(?,?,?,?,?,?)";
    public static final String COUNT = " COUNT(*) ";
    public static final String GROUP_BY = " GROUP BY ";
    public static final String HAVING_COUNT_LESS_EQUALS = " HAVING COUNT(*) <= ";

    public static final String ASTERISK = " * ";
    public static final String Q_MARK = " ? ";
    public static final String COMA = ",";
    public static final String L_BRACKET = " (";
    public static final String EQUALS_M = " = ";
    public static final String MONO_QUOTE = "\'";
    public static final String DOT = ".";

    public static class UniversityTable {
        private UniversityTable() {
        }

        public static final String TABLE_UNIVERSITIES = "universities";
        public static final String ID = "id";
        public static final String NAME = "university_name";
    }

    public static class RoomsTable {
        private RoomsTable() {
        }

        public static final String TABLE_ROOMS = "rooms";
        public static final String ID = "room_id";
        public static final String ROOM_NUMBER = "room_number";
    }

    public static class GroupsTable {
        private GroupsTable() {
        }

        public static final String TABLE_GROUPS = "groups";
        public static final String ID = "group_id";
        public static final String NAME = "group_name";
    }

    public static class LessonsTable {
        private LessonsTable() {

        }

        public static final String TABLE_LESSONS = "lessons";
        public static final String ID = "lesson_id";
        public static final String GROUP_ID = "group_id";
        public static final String SUBJECT_ID = "subject_id";
        public static final String ROOM_ID = "room_id";
    }

    public static class SubjectTable {
        private SubjectTable() {
        }

        public static final String TABLE_SUBJECTS = "subjects";
        public static final String SUBJECT_ID = "subject_id";
        public static final String SUBJECT_NAME = "subject_name";
    }

    public static class TeachersTable {
        private TeachersTable() {
        }

        public static final String TABLE_TEACHERS = "teachers";
        public static final String ID = "teacher_id";
        public static final String FIRST_NAME = "first_name";
        public static final String LAST_NAME = "last_name";
        public static final String SUBJECT_ID = "subject_id";
    }

    public static class StudentsTable {
        private StudentsTable() {
        }

        public static final String TABLE_STUDENTS = "students";
        public static final String ID = "student_id";
        public static final String FIRST_NAME = "first_name";
        public static final String LAST_NAME = "last_name";
        public static final String GROUP_ID = "group_id";
    }

    public static class StudentsTimetableTable {
        private StudentsTimetableTable() {
        }

        public static final String TABLE_STUDENTS_TIMETABLE = "students_timetable";
        public static final String ID = "id";
        public static final String LESSON_ID = "lesson_id";
        public static final String DATE = "date";
        public static final String PERSON_ID = "person_id";
        public static final String PERIOD = "period";
    }

    public static class TeachersTimetableTable {
        private TeachersTimetableTable() {
        }

        public static final String TABLE_TEACHERS_TIMETABLE = "teachers_timetable";
        public static final String ID = "id";
        public static final String LESSON_ID = "lesson_id";
        public static final String DATE = "date";
        public static final String PERSON_ID = "person_id";
        public static final String PERIOD = "period";
    }

    public static class TeachersSubjectsTable {
        private TeachersSubjectsTable() {
        }

        public static final String TEACHERS_SUBJECTS_TABLE = "teachers_subjects";
        public static final String TEACHER_ID = "teacher_id";
        public static final String SUBJECT_ID = "subject_id";
    }

}
