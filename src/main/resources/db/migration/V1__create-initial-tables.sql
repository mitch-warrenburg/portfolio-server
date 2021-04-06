CREATE TYPE principal_role AS ENUM ('ROLE_USER', 'ROLE_ANONYMOUS');
CREATE TABLE authenticated_user
(
    id           SERIAL PRIMARY KEY,
    uid          TEXT      NOT NULL UNIQUE,
    username     TEXT,
    email        TEXT,
    company      TEXT,
    phone_number TEXT,
    email_count  SMALLINT           DEFAULT 0,
    "role"       principal_role     DEFAULT 'ROLE_ANONYMOUS',
    created_by   TEXT      NOT NULL DEFAULT 'SYSTEM',
    modified_by  TEXT,
    created_ts   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_ts  TIMESTAMP
);

CREATE UNIQUE INDEX user_uid_index ON authenticated_user (uid);

CREATE TABLE client_metadata
(
    user_id              INTEGER PRIMARY KEY REFERENCES authenticated_user,
    mobile               BOOLEAN            DEFAULT FALSE,
    fingerprint          TEXT,
    os                   TEXT,
    os_version           TEXT,
    ipad                 BOOLEAN            DEFAULT FALSE,
    iphone               BOOLEAN            DEFAULT FALSE,
    ios                  BOOLEAN            DEFAULT FALSE,
    browser              TEXT,
    browser_version      TEXT,
    browser_full_version TEXT,
    device               TEXT,
    device_cpu           TEXT,
    device_type          TEXT,
    device_vendor        TEXT,
    timezone             TEXT,
    language             TEXT,
    system_language      TEXT,
    cookies              BOOLEAN            DEFAULT FALSE,
    local_storage        BOOLEAN            DEFAULT FALSE,
    session_storage      BOOLEAN            DEFAULT FALSE,
    resolution           TEXT,
    available_resolution TEXT,
    screen_info          TEXT,
    created_by           TEXT      NOT NULL DEFAULT 'SYSTEM',
    modified_by          TEXT,
    created_ts           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_ts          TIMESTAMP
);

CREATE TABLE schedule_event
(
    id          SERIAL PRIMARY KEY,
    user_id     INTEGER REFERENCES authenticated_user,
    start       TIMESTAMP NOT NULL,
    "end"       TIMESTAMP NOT NULL,
    description TEXT      NOT NULL,
    created_by  TEXT      NOT NULL DEFAULT 'SYSTEM',
    modified_by TEXT,
    created_ts  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_ts TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES authenticated_user (id)
);

CREATE UNIQUE INDEX schedule_user_id_index ON schedule_event (user_id, id);
