CREATE TYPE player_position_enum AS ENUM (
  'GK',
  'DEF',
  'MIDF',
  'STR'
);

CREATE TYPE continent_enum AS ENUM (
  'AFRICA',
  'EUROPA',
  'ASIA',
  'AMERICA'
);

CREATE TABLE Team(
  id serial primary key,
  name varchar(255) not null,
  continent continent_enum not null
);
CREATE TABLE Player(
  id serial primary key,
  name varchar(255) not null,
  age int not null,
  position player_position_enum not null,
  id_team int references Team(id)
);