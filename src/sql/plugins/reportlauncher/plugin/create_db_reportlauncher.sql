
--
-- Structure for table reportlauncher_
--

DROP TABLE IF EXISTS reportlauncher_report;
CREATE TABLE reportlauncher_report (
id_report int(6) NOT NULL,
name varchar(50) NOT NULL default '',
page varchar(255) NOT NULL default '',
PRIMARY KEY (id_report)
);

--
-- Structure for table reportlauncher_
--

DROP TABLE IF EXISTS reportlauncher_parameter;
CREATE TABLE reportlauncher_parameter (
id_parameter int(6) NOT NULL,
id_report int(6) NOT NULL,
name varchar(50) NOT NULL default '',
value varchar(50) NOT NULL default '',
PRIMARY KEY (id_parameter),
CONSTRAINT fk_reportlauncher_parameter_parameter FOREIGN KEY(id_report) references reportlauncher_report(id_report)
);
