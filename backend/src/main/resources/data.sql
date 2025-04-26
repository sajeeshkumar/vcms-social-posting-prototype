DROP ALL OBJECTS;

CREATE TABLE platforms (id INT NOT NULL, name varchar(100) NOT NULL);

CREATE TABLE posts (
    id VARCHAR(36) PRIMARY KEY,
    message VARCHAR(255) NOT NULL,
    scheduled_time TIMESTAMP,
    posted BOOLEAN NOT NULL
);

CREATE TABLE post_media_urls (
    post_id VARCHAR(36) NOT NULL,
    media_url VARCHAR(255) NOT NULL,
    FOREIGN KEY (post_id) REFERENCES posts(id)
);

CREATE TABLE  post_platforms (
    post_id VARCHAR(36) NOT NULL,
    platform VARCHAR(255) NOT NULL,
    FOREIGN KEY (post_id) REFERENCES posts(id)
);


INSERT INTO platforms (id, name) VALUES (1, 'X');
INSERT INTO platforms (id, name) VALUES (2, 'Instagram');
INSERT INTO platforms (id, name) VALUES (3, 'Bluesky');

commit;