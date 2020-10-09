DROP TABLE IF EXISTS rooms;
CREATE TABLE rooms(
	room_id serial PRIMARY KEY,
	room_number int
);
DROP TABLE IF EXISTS groups;
CREATE TABLE groups(
	group_id serial PRIMARY KEY,
	group_number long
);

	
