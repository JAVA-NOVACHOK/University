DROP TABLE IF EXISTS rooms;
CREATE TABLE rooms(
	room_id serial PRIMARY KEY,
	room_number int UNIQUE
);
DROP TABLE IF EXISTS groups;
CREATE TABLE groups(
	group_id serial PRIMARY KEY,
	group_name varchar(255) UNIQUE
);
DROP TABLE IF EXISTS lessons;
CREATE TABLE lessons(
	lesson_id serial PRIMARY KEY,
	group_id BIGINT,
	subject_id int,
	room_id int
);
DROP TABLE IF EXISTS subjects CASCADE;
CREATE TABLE subjects(
	subject_id serial PRIMARY KEY,
	subject_name varchar(255) UNIQUE
);
DROP TABLE IF EXISTS students;
CREATE TABLE students(
	student_id serial PRIMARY KEY,
	first_name varchar(255),
	last_name varchar(255),
	group_id BIGINT,
	UNIQUE(first_name,last_name,group_id)
);
DROP TABLE IF EXISTS teachers CASCADE;
CREATE TABLE teachers(
	teacher_id serial PRIMARY KEY,
	first_name varchar(255),
	last_name varchar(255),
	UNIQUE(first_name,last_name)
);
DROP TABLE IF EXISTS teachers_timetable;
CREATE TABLE teachers_timetable(
	id serial PRIMARY KEY,
	lesson_id BIGINT,
	date TIMESTAMP WITH TIME ZONE NOT NULL,
	period int NOT NULL,
	person_id BIGINT
);
DROP TABLE IF EXISTS students_timetable;
CREATE TABLE students_timetable(
	id serial PRIMARY KEY,
	lesson_id BIGINT,
	date TIMESTAMP WITH TIME ZONE NOT NULL,
	period int NOT NULL,
	person_id BIGINT
);
DROP TABLE IF EXISTS universities;
CREATE TABLE universities(
	id serial PRIMARY KEY,
	university_name VARCHAR(255)
);
DROP TABLE IF EXISTS teachers_subjects;
CREATE TABLE teachers_subjects(
	teacher_id BIGINT NOT NULL REFERENCES teachers (teacher_id) ON UPDATE CASCADE ON DELETE CASCADE,
	subject_id int NOT NULL REFERENCES subjects (subject_id) ON UPDATE CASCADE ON DELETE CASCADE,
	PRIMARY KEY(teacher_id,subject_id),
	UNIQUE(teacher_id,subject_id)
);
	
