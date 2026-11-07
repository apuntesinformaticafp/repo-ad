CREATE TABLE IF NOT EXISTS empresa (
    id TEXT PRIMARY KEY NOT NULL,
    nombre TEXT NOT NULL CHECK (length(trim(nombre)) > 0)
);
CREATE TABLE IF NOT EXISTS estudiante (
    id TEXT PRIMARY KEY NOT NULL,
    nombre TEXT NOT NULL CHECK (length(trim(nombre)) > 0),
    ciclo TEXT NOT NULL CHECK (length(trim(ciclo)) > 0)
);
CREATE TABLE IF NOT EXISTS tutor (
    id TEXT PRIMARY KEY NOT NULL,
    nombre TEXT NOT NULL CHECK (length(trim(nombre)) > 0),
    empresa_id TEXT NOT NULL REFERENCES empresa(id),
    UNIQUE (id, empresa_id)
);
CREATE TABLE IF NOT EXISTS asignacion (
    id TEXT PRIMARY KEY NOT NULL,
    estudiante_id TEXT NOT NULL REFERENCES estudiante(id),
    empresa_id TEXT NOT NULL REFERENCES empresa(id),
    tutor_id TEXT NOT NULL,
    inicio DATE NOT NULL,
    fin DATE NOT NULL,
    CHECK (inicio <= fin),
    FOREIGN KEY (tutor_id, empresa_id) REFERENCES tutor(id, empresa_id)
);
