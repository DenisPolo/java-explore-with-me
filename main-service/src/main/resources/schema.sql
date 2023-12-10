CREATE TABLE IF NOT EXISTS users (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  email VARCHAR(254) NOT NULL,
  name VARCHAR(250) NOT NULL,
  registration_date TIMESTAMP NOT NULL,
  CONSTRAINT pk_user PRIMARY KEY (id),
  CONSTRAINT UQ_USER_EMAIL UNIQUE (email),
  UNIQUE(id)
);

CREATE TABLE IF NOT EXISTS categories (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(50) NOT NULL,
  registration_date TIMESTAMP NOT NULL,
  CONSTRAINT pk_category PRIMARY KEY (id),
  CONSTRAINT UQ_CATEGORY_NAME UNIQUE (name),
  UNIQUE(id)
);

CREATE TABLE IF NOT EXISTS locations (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  lat FLOAT NOT NULL,
  lon FLOAT NOT NULL,
  CONSTRAINT pk_location PRIMARY KEY (id),
  UNIQUE(id)
);

CREATE TABLE IF NOT EXISTS events (
  annotation VARCHAR(2000) NOT NULL,
  category_id BIGINT NOT NULL,
  confirmed_requests INTEGER,
  created_on TIMESTAMP,
  description VARCHAR(7000),
  event_date TIMESTAMP NOT NULL,
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  initiator_id BIGINT NOT NULL,
  location_id BIGINT NOT NULL,
  paid BOOLEAN NOT NULL,
  participant_limit INTEGER,
  published_on TIMESTAMP,
  request_moderation BOOLEAN,
  state VARCHAR(20),
  title VARCHAR(120) NOT NULL,
  views BIGINT,
  CONSTRAINT pk_event PRIMARY KEY (id),
  CONSTRAINT fk_events_to_categories FOREIGN KEY(category_id) REFERENCES categories(id),
  CONSTRAINT fk_events_to_users FOREIGN KEY(initiator_id) REFERENCES users(id),
  CONSTRAINT fk_events_to_locations FOREIGN KEY(location_id) REFERENCES locations(id),
  UNIQUE(id)
);

CREATE TABLE IF NOT EXISTS requests (
  created TIMESTAMP NOT NULL,
  event_id BIGINT NOT NULL,
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  requester_id BIGINT NOT NULL,
  status VARCHAR(20),
  CONSTRAINT pk_request PRIMARY KEY (id),
  CONSTRAINT fk_requests_to_events FOREIGN KEY(event_id) REFERENCES events(id),
  CONSTRAINT fk_requests_to_users FOREIGN KEY(requester_id) REFERENCES users(id),
  UNIQUE(id)
);

CREATE TABLE IF NOT EXISTS compilations (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  pinned BOOLEAN NOT NULL,
  title VARCHAR(50),
  created TIMESTAMP NOT NULL,
  CONSTRAINT pk_compilation PRIMARY KEY (id),
  UNIQUE(id),
  UNIQUE(title)
);

CREATE TABLE IF NOT EXISTS events_compilations (
  event_id BIGINT NOT NULL,
  compilation_id BIGINT NOT NULL,
  CONSTRAINT fk_events_compilations_to_events FOREIGN KEY(event_id) REFERENCES events(id),
  CONSTRAINT fk_events_compilations_to_compilations FOREIGN KEY(compilation_id) REFERENCES compilations(id)
);

CREATE TABLE IF NOT EXISTS main_locations (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(50) NOT NULL,
  description VARCHAR(7000),
  lat FLOAT NOT NULL,
  lon FLOAT NOT NULL,
  rad FLOAT NOT NULL,
  created_on TIMESTAMP,
  CONSTRAINT pk_main_location PRIMARY KEY (id),
  UNIQUE(id),
  UNIQUE(name)
);

CREATE OR REPLACE FUNCTION distance(lat1 float, lon1 float, lat2 float, lon2 float)
    RETURNS float
AS
'
declare
    dist float = 0;
    rad_lat1 float;
    rad_lat2 float;
    theta float;
    rad_theta float;
BEGIN
    IF lat1 = lat2 AND lon1 = lon2
    THEN
        RETURN dist;
    ELSE
        -- переводим градусы широты в радианы
        rad_lat1 = pi() * lat1 / 180;
        -- переводим градусы долготы в радианы
        rad_lat2 = pi() * lat2 / 180;
        -- находим разность долгот
        theta = lon1 - lon2;
        -- переводим градусы в радианы
        rad_theta = pi() * theta / 180;
        -- находим длину ортодромии
        dist = sin(rad_lat1) * sin(rad_lat2) + cos(rad_lat1) * cos(rad_lat2) * cos(rad_theta);

        IF dist > 1
            THEN dist = 1;
        END IF;

        dist = acos(dist);
        -- переводим радианы в градусы
        dist = dist * 180 / pi();
        -- переводим градусы в метры
        dist = dist * 60 * 1.8524 * 1000;

        RETURN dist;
    END IF;
END;
'
LANGUAGE PLPGSQL;