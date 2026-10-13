-- liquibase formatted sql
-- changeset reportlauncher:create_db_reportlauncher.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM reportlauncher_report WHERE 1 = 0

--
-- Structure for table reportlauncher_
--

DROP TABLE IF EXISTS reportlauncher_report;
CREATE TABLE reportlauncher_report (
id_report int NOT NULL,
name varchar(50) default '' NOT NULL,
page varchar(255) default '' NOT NULL,
PRIMARY KEY (id_report)
);

--
-- Structure for table reportlauncher_
--

DROP TABLE IF EXISTS reportlauncher_parameter;
CREATE TABLE reportlauncher_parameter (
id_parameter int NOT NULL,
id_report int NOT NULL,
name varchar(50) default '' NOT NULL,
value varchar(50) default '' NOT NULL,
PRIMARY KEY (id_parameter),
CONSTRAINT fk_reportlauncher_parameter_parameter FOREIGN KEY(id_report) references reportlauncher_report(id_report)
);
