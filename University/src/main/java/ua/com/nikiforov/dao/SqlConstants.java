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
    public static final String INNER = " INNER ";
    public static final String JOIN = " JOIN ";
    public static final String ON = " ON ";
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

        public static final String TABLE_UNIVERSITIES = "universities ";
        public static final String ID = "id";
        public static final String NAME = "university_name";
    }

    public static class RoomsTable {
        private RoomsTable() {
        }

        public static final String TABLE_ROOMS = "rooms ";
        public static final String ID = "room_id";
        public static final String ROOM_NUMBER = "room_number";
        public static final String SEAT_NUMBER = "seat_number";
    }

    public static class GroupsTable {
        private GroupsTable() {
        }

        public static final String TABLE_GROUPS = "groups ";
        public static final String GROUPS_GROUP_ID = "groups.group_id";
        public static final String GROUPS_GROUP_NAME = "groups.group_name";
    }

    public static class LessonsTable {
        private LessonsTable() {

        }

        public static final String TABLE_LESSONS = "lessons ";
        public static final String LESSONS_LESSON_ID = "lessons.lesson_id";
        public static final String LESSONS_GROUP_ID = "lessons.group_id";
        public static final String LESSONS_SUBJECT_ID = "lessons.subject_id";
        public static final String LESSONS_ROOM_ID = "lessons.room_id";
    }

    public static class SubjectTable {
        private SubjectTable() {
        }

        public static final String TABLE_SUBJECTS = "subjects ";
        public static final String SUBJECTS_SUBJECT_ID = "subjects.subject_id";
        public static final String SUBJECTS_SUBJECT_NAME = "subjects.subject_name";
    }

    public static class TeachersTable {
        private TeachersTable() {
        }

        public static final String TABLE_TEACHERS = "teachers";
        public static final String TEACHERS_TEACHER_ID = "teachers.teacher_id";
        public static final String TEACHERS_FIRST_NAME = "teachers.first_name";
        public static final String TEACHERS_LAST_NAME = "teachers.last_name";
        public static final String TEACHERS_SUBJECT_ID = "teachers.subject_id";
    }

    public static class StudentsTable {
        private StudentsTable() {
        }

        public static final String TABLE_STUDENTS = "students";
        public static final String STUDENTS_STUDENT_ID = "students.student_id";
        public static final String STUDENTS_STUDENT_FIRST_NAME = "students.first_name";
        public static final String STUDENTS_STUDENT_LAST_NAME = "students.last_name";
        public static final String STUDENTS_STUDENT_GROUP_ID = "students.group_id";
    }

    public static class StudentsTimetableTable {
        private StudentsTimetableTable() {
        }

        public static final String TABLE_STUDENTS_TIMETABLE = "students_timetable";
        public static final String ID = "students_timetable.id";
        public static final String LESSON_ID = "students_timetable.lesson_id";
        public static final String DATE = "students_timetable.date";
        public static final String PERSON_ID = "students_timetable.person_id";
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
        public static final String TEACHERS_SUBJECTS_TEACHER_ID = "teachers_subjects.teacher_id";
        public static final String TEACHERS_SUBJECTS_SUBJECT_ID = "teachers_subjects.subject_id";
    }
    
    

}
