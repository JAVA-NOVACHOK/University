package ua.com.nikiforov.dao;

public class SqlKeyWords {
    
    public static final String INSERT = "INSERT INTO ";
    public static final String SELECT = "SELECT ";
    public static final String DELETE = "DELETE ";
    public static final String UPDATE = "UPDATE ";
    public static final String SET = " SET ";
    public static final String FROM = " FROM ";
    public static final String AND = " AND ";
    public static final String WHERE = " WHERE ";
    public static final String VALUES_1_QMARK = ") VALUES(?)";
    public static final String VALUES_2_QMARK = ") VALUES(?,?)";
    public static final String VALUES_3_QMARK = ") VALUES(?,?,?)";
    public static final String VALUES_6_QMARK = ") VALUES(?,?,?,?,?,?)";
    public static final String COUNT = " COUNT(*) ";
    public static final String GROUP_BY = " GROUP BY ";
    public static final String HAVING_COUNT_LESS_EQUALS = " HAVING COUNT(*) <= ";
    
    public static final String ASTERISK = " * ";
    public static final String Q_MARK = " ? ";
    public static final String EMPTY_LINE = "";
    public static final String COMA = ",";
    public static final String L_BRACKET = " (";
    public static final String EQUALS_M = " = ";
    public static final String MONO_QUOTE = "\'";
    public static final String DOT = ".";
    
    
    public static final String TABLE_UNIVERSITIES = "universities";
    public static final String COLUMN_UNIVERSITY_ID = "id";
    public static final String COLUMN_UNIVERSITY_NAME = "university_name";
    
    public static final String TABLE_ROOMS = "rooms";
    public static final String COLUMN_ROOM_ID = "room_id";
    public static final String COLUMN_ROOM_NUMBER = "room_number";
    
    public static final String TABLE_GROUPS = "groups";
    public static final String COLUMN_GROUP_ID = "group_id";
    public static final String COLUMN_GROUP_NAME = "group_name";
    
    public static final String TABLE_LESSONS = "lessons";
    public static final String COLUMN_LESSON_ID = "lesson_id";
    public static final String COLUMN_LESSON_GROUP_ID = "group_id";
    public static final String COLUMN_LESSON_SUBJECT_ID = "subject_id";
    public static final String COLUMN_LESSON_ROOM_ID = "room_id";
    
    public static final String TABLE_SUBJECTS = "subject";
    public static final String COLUMN_SUBJECT_ID = "subject_id";
    public static final String COLUMN_SUBJECT_NAME = "subject_name";
    
    public static final String TABLE_TEACHERS = "teachers";
    public static final String COLUMN_TEACHER_ID = "id";
    public static final String COLUMN_TEACHER_FIRST_NAME = "first_name";
    public static final String COLUMN_TEACHER_LAST_NAME = "last_name";
    public static final String COLUMN_TEACHER_SUBJECT_ID = "subject_id";
    
    public static final String TABLE_STUDENTS = "students";
    public static final String COLUMN_STUDENT_ID = "id";
    public static final String COLUMN_STUDENT_FIRST_NAME = "first_name";
    public static final String COLUMN_STUDENT_LAST_NAME = "last_name";
    public static final String COLUMN_STUDENT_GROUP_ID = "teachers";
    
    public static final String TABLE_STUDENTS_TIMETABLE = "students_timetable";
    public static final String COLUMN_STUDENT_TIMETABLE_ID = "id";
    public static final String COLUMN_STUDENT_TIMETABLE_LESSON_ID = "lesson_id";
    public static final String COLUMN_STUDENT_TIMETABLE_TIME = "time"; 
    public static final String COLUMN_STUDENT_TIMETABLE_STUDENT_ID = "student_id";
    
    public static final String TABLE_TEACHER_TIMETABLE = "teachers_timetable";
    public static final String COLUMN_TEACHER_TIMETABLE_ID = "id";
    public static final String COLUMN_TEACHER_TIMETABLE_LESSON_ID = "lesson_id";
    public static final String COLUMN_TEACHER_TIMETABLE_TIME = "time"; 
    public static final String COLUMN_TEACHER_TIMETABLE_TEACHER_ID = "teacher_id";
    

}
