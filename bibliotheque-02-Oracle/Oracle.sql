DROP TABLE reservation; 
DROP TABLE livre;
DROP TABLE membre;

CREATE TABLE membre (idMembre   number(3)   CHECK (idMembre > 0),
                     nom        VARCHAR(10)  NOT NULL,
                     telephone  number(10),
					 limitePret number(2)   CHECK (limitePret > 0 AND limitePret <= 10),
					 nbPret     number(2)   DEFAULT 0 CHECK (nbpret >= 0),
					 CONSTRAINT cleMembre    PRIMARY KEY (idMembre),
					 CONSTRAINT limiteNbPret CHECK (nbPret <= limitePret));

CREATE TABLE livre (idLivre         number(3)    CHECK (idLivre > 0),
                    titre           VARCHAR(10)   NOT NULL,
					auteur          VARCHAR(10)   NOT NULL,
					dateAcquisition TIMESTAMP(3)  NOT NULL,
					idMembre        number(3),
					datePret        TIMESTAMP(3)  NULL,
					CONSTRAINT      cleLivre      PRIMARY KEY (idLivre),
					CONSTRAINT      refPretMembre FOREIGN KEY (idMembre) REFERENCES membre (idMembre));

CREATE TABLE reservation (idReservation   number(3),
                          idMembre        number(3),
						  idLivre         number(3),
						  dateReservation TIMESTAMP(3),
						  CONSTRAINT      clePrimaireReservation  PRIMARY KEY (idReservation),
						  CONSTRAINT      cleEtrangereReservation UNIQUE (idMembre, idLivre),
						  CONSTRAINT      refReservationMembre    FOREIGN KEY (idMembre) REFERENCES membre (idMembre) ON DELETE CASCADE,
			              CONSTRAINT      refReservationLivre     FOREIGN KEY (idLivre)  REFERENCES livre (idLivre)   ON DELETE CASCADE);