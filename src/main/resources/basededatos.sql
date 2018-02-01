/* 
 * Sistema creado por bfhsoftware para musica ambiental
 * Bernardo harreguy, Derechos reservados
 * and open the template in the editor.
 */
BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "albums" (`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `nombre` TEXT);
CREATE TABLE IF NOT EXISTS "musica" (`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, `nombreyruta` TEXT NOT NULL, `anulado` NUMERIC NOT NULL DEFAULT 0, `album`	INTEGER, `reproducido` INTEGER NOT NULL DEFAULT 0, `ultimareproduccion`	TEXT);
CREATE TABLE IF NOT EXISTS "opciones" (`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, `nombre`	TEXT, `numero` INTEGER, `texto`	TEXT, `binario` INTEGER);
CREATE TABLE IF NOT EXISTS "reproduccion" (`Id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, `idalbum` INTEGER NOT NULL DEFAULT 0, `habilitado` INTEGER NOT NULL DEFAULT 1);
CREATE TABLE IF NOT EXISTS "usuarios" ( `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `usuario` TEXT, `sha1`	TEXT);
COMMIT;

