DROP TABLE IF EXISTS reservation CASCADE;
DROP TABLE IF EXISTS pret		 CASCADE;
DROP TABLE IF EXISTS livre       CASCADE;
DROP TABLE IF EXISTS membre      CASCADE;

CREATE TABLE membre (idMembre   INTEGER AUTO_INCREMENT  CHECK (idMembre > 0),
                     nom        VARCHAR(10)  NOT NULL,
                     telephone  BIGINT(10),
					 limitePret INTEGER(2)   CHECK (limitePret > 0 AND limitePret <= 10),
					 CONSTRAINT cleMembre    PRIMARY KEY (idMembre),
					 CONSTRAINT limiteNbPret CHECK (nbPret <= limitePret));

CREATE TABLE livre (idLivre         INTEGER  AUTO_INCREMENT  CHECK (idLivre > 0),
                    titre           VARCHAR(50)   NOT NULL,
					auteur          VARCHAR(50)   NOT NULL,
					dateAcquisition TIMESTAMP(3)  NOT NULL,
					idMembre        INTEGER(3),
					datePret        TIMESTAMP(3)  NULL,
					CONSTRAINT      cleLivre      PRIMARY KEY (idLivre),
					CONSTRAINT      refMembre 	  FOREIGN KEY (idMembre) REFERENCES membre (idMembre));

CREATE TABLE pret(	idPret INTEGER AUTO_INCREMENT CHECK(idPret > 0),
					idMembre INTEGER not null CHECK(idMembre > 0),
					idLivre INTEGER NOT NULL CHECK(idLivre > 0),
					datePret TIMESTAMP(3),
					dateRetour TIMESTAMP(3) NULL,
					CONSTRAINT clePrimairePret PRIMARY KEY (idPret),
					CONSTRAINT refPretMembre FOREIGN KEY (idMembre) REFERENCES membre(idMembre),
					CONSTRAINT refPretLivre FOREIGN KEY (idLivre) REFERENCES livre (idLivre));
					
CREATE TABLE reservation (idReservation   INTEGER AUTO_INCREMENT CHECK(idReservation > 0),
                          idMembre        INTEGER,
						  idLivre         INTEGER,
						  dateReservation TIMESTAMP(3),
						  CONSTRAINT      clePrimaireReservation  PRIMARY KEY (idReservation),
						  CONSTRAINT      cleEtrangereReservation UNIQUE (idMembre, idLivre),
						  CONSTRAINT      refReservationMembre    FOREIGN KEY (idMembre) REFERENCES membre (idMembre) ON DELETE CASCADE,
			              CONSTRAINT      refReservationLivre     FOREIGN KEY (idLivre)  REFERENCES livre (idLivre)   ON DELETE CASCADE);