BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS `usuarios` (
	`id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	`usuario`	TEXT,
	`contra`	TEXT
);
CREATE TABLE IF NOT EXISTS `opciones` (
	`id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
	`nombre`	TEXT,
	`tipo`	INTEGER NOT NULL,
	`numero`	INTEGER,
	`texto`	TEXT
);
CREATE TABLE IF NOT EXISTS `musica` (
	`id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
	`nombreyruta`	TEXT NOT NULL,
	`anulado`	NUMERIC DEFAULT 0,
	`album`	INTEGER,
	`reproducido`	INTEGER NOT NULL
);
CREATE TABLE IF NOT EXISTS `albums` (
	`id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	`nombre`	TEXT
);
COMMIT;
