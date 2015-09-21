CREATE TABLE 'categories' (
    _id INTEGER PRIMARY KEY AUTOINCREMENT,
    code INTEGER,
    title TEXT,
    image TEXT,
    UNIQUE (code) ON CONFLICT REPLACE);

CREATE TABLE 'places' (
    _id INTEGER PRIMARY KEY AUTOINCREMENT,
    code TEXT,
    title TEXT,
    address TEXT,
    cp TEXT,
    telephone TEXT,
    email TEXT,
    web TEXT,
    latitude REAL,
    longitude REAL,
    accessibility TEXT,
    UNIQUE (code) ON CONFLICT REPLACE);

CREATE TABLE 'events' (
    _id INTEGER PRIMARY KEY AUTOINCREMENT,
    code INTEGER,
    title TEXT,
    description TEXT,
    place_code TEXT,
    start_date TEXT,
    end_date TEXT,
    start_hour TEXT,
    category_code INTEGER,
    price TEXT,
    web TEXT,
    image TEXT,
    UNIQUE (code) ON CONFLICT REPLACE);