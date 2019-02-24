CREATE TABLE parents AS
  SELECT "abraham" AS parent, "barack" AS child UNION
  SELECT "abraham"          , "clinton"         UNION
  SELECT "delano"           , "herbert"         UNION
  SELECT "fillmore"         , "abraham"         UNION
  SELECT "fillmore"         , "delano"          UNION
  SELECT "fillmore"         , "grover"          UNION
  SELECT "eisenhower"       , "fillmore";

CREATE TABLE dogs AS
  SELECT "abraham" AS name, "long" AS fur, 26 AS height UNION
  SELECT "barack"         , "short"      , 52           UNION
  SELECT "clinton"        , "long"       , 47           UNION
  SELECT "delano"         , "long"       , 46           UNION
  SELECT "eisenhower"     , "short"      , 35           UNION
  SELECT "fillmore"       , "curly"      , 32           UNION
  SELECT "grover"         , "short"      , 28           UNION
  SELECT "herbert"        , "curly"      , 31;

CREATE TABLE sizes AS
  SELECT "toy" AS size, 24 AS min, 28 AS max UNION
  SELECT "mini"       , 28       , 35        UNION
  SELECT "medium"     , 35       , 45        UNION
  SELECT "standard"   , 45       , 60;

-------------------------------------------------------------
-- PLEASE DO NOT CHANGE ANY SQL STATEMENTS ABOVE THIS LINE --
-------------------------------------------------------------

-- The size of each dog
CREATE TABLE size_of_dogs AS
  SELECT name, size FROM dogs, sizes WHERE dogs.height > sizes.min AND dogs.height <= sizes.max;

-- All dogs with parents ordered by decreasing height of their parent
CREATE TABLE by_parent_height AS
  SELECT p.child FROM parents AS p, dogs WHERE dogs.name = p.parent ORDER BY dogs.height DESC;

-- Filling out this helper table is optional
CREATE TABLE siblings AS
  SELECT p.parent AS parent, p.child AS name, s.size AS size FROM parents AS p, size_of_dogs AS s WHERE p.child = s.name ORDER BY s.name ASC;

-- Sentences about siblings that are the same size
CREATE TABLE sentences AS
  SELECT a.name || " and " || b.name || " are " || a.size || " siblings" FROM siblings AS a, siblings AS b WHERE a.parent = b.parent AND a.size = b.size AND a.name < b.name;

-- Ways to stack 4 dogs to a height of at least 170, ordered by total height
CREATE TABLE stacks_helper(dogs, stack_height, last_height);
  INSERT INTO stacks_helper(dogs, stack_height, last_height) SELECT one.name, one.height, one.height FROM dogs AS one;
  INSERT INTO stacks_helper(dogs, stack_height, last_height) SELECT sub.dogs || ", " || two.name, sub.stack_height + two.height, two.height FROM dogs AS two, stacks_helper AS sub WHERE two.height > sub.last_height;
  INSERT INTO stacks_helper(dogs, stack_height, last_height) SELECT sub.dogs || ", " || three.name, sub.stack_height + three.height, three.height FROM dogs AS three, stacks_helper AS sub WHERE three.height > sub.last_height;
  INSERT INTO stacks_helper(dogs, stack_height, last_height) SELECT sub.dogs || ", " || four.name, sub.stack_height + four.height, four.height FROM dogs AS four, stacks_helper AS sub WHERE four.height > sub.last_height;

CREATE TABLE stacks AS
  SELECT dogs, stack_height FROM stacks_helper WHERE stack_height >+ 170 ORDER BY stack_height ASC;
