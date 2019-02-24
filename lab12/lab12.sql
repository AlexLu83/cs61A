.read fa18data.sql

-- Q2
CREATE TABLE obedience AS
  SELECT seven, denero from students;

-- Q3
CREATE TABLE smallest_int AS
  SELECT time, smallest FROM students WHERE smallest > 13 ORDER BY smallest LIMIT 20;

-- Q4
CREATE TABLE matchmaker AS
  SELECT S1.pet, S1.song, S1.color, S2.color FROM students AS S1, students AS S2 WHERE S1.pet = S2.pet AND S1.song = S2.song AND S1.time < S2.time;
