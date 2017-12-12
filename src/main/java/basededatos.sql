/* 
 * Sistema creado por bfhsoftware para musica ambiental
 * Bernardo harreguy, Derechos reservados
 * and open the template in the editor.
 */
BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS `usuarios` (
	`id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	`usuario`	TEXT,
	`contra`	TEXT
);
CREATE TABLE IF NOT EXISTS `musica` (
	`id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
	`nombreyruta`	TEXT,
	`anulado`	NUMERIC DEFAULT 0,
	`album`	INTEGER
);
CREATE TABLE IF NOT EXISTS `albums` (
	`id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	`nombre`	TEXT
);
COMMIT;

