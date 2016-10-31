DROP TABLE reservation CASCADE CONSTRAINT;
DROP TABLE pret		 CASCADE CONSTRAINT;
DROP TABLE livre       CASCADE CONSTRAINT;
DROP TABLE membre      CASCADE CONSTRAINT;
DROP SEQUENCE membre_sequence;
DROP SEQUENCE livre_sequence;
DROP SEQUENCE pret_sequence;
DROP SEQUENCE reservation_sequence;

CREATE SEQUENCE membre_sequence
        START WITH 1
        INCREMENT BY 1
        NOCACHE
        NOCYCLE;

CREATE SEQUENCE livre_sequence
START WITH 1
INCREMENT BY 1
NOCACHE
NOCYCLE;

CREATE SEQUENCE pret_sequence
START WITH 1
INCREMENT BY 1
NOCACHE
NOCYCLE;

CREATE SEQUENCE reservation_sequence
START WITH 1
INCREMENT BY 1
NOCACHE
NOCYCLE;

CREATE TABLE membre (idMembre   NUMBER,
                     nom        VARCHAR(10)  NOT NULL,
                     telephone  NUMBER,
					 limitePret NUMBER(2)   CHECK (limitePret > 0 AND limitePret <= 10),
					 CONSTRAINT cleMembre    PRIMARY KEY (idMembre);

CREATE TABLE livre (idLivre         NUMBER,
                    titre           VARCHAR(50)   NOT NULL,
					auteur          VARCHAR(50)   NOT NULL,
					dateAcquisition TIMESTAMP(3)  NOT NULL,
					idMembre        NUMBER,
					datePret        TIMESTAMP(3)  NULL,
					CONSTRAINT      cleLivre      PRIMARY KEY (idLivre),
					CONSTRAINT      refMembre 	  FOREIGN KEY (idMembre) REFERENCES membre (idMembre));

CREATE TABLE pret(	idPret NUMBER,
					idMembre NUMBER NOT null CHECK(idMembre > 0),
					idLivre NUMBER NOT NULL CHECK(idLivre > 0),
					datePret TIMESTAMP(3),
					dateRetour TIMESTAMP(3) NULL,
					CONSTRAINT clePrimairePret PRIMARY KEY (idPret),
					CONSTRAINT refPretMembre FOREIGN KEY (idMembre) REFERENCES membre(idMembre),
					CONSTRAINT refPretLivre FOREIGN KEY (idLivre) REFERENCES livre (idLivre));
					
CREATE TABLE reservation (idReservation   NUMBER,
                          idMembre        NUMBER,
						  idLivre         NUMBER,
						  dateReservation TIMESTAMP(3),
						  CONSTRAINT      clePrimaireReservation  PRIMARY KEY (idReservation),
						  CONSTRAINT      cleEtrangereReservation UNIQUE (idMembre, idLivre),
						  CONSTRAINT      refReservationMembre    FOREIGN KEY (idMembre) REFERENCES membre (idMembre) ON DELETE CASCADE,
			              CONSTRAINT      refReservationLivre     FOREIGN KEY (idLivre)  REFERENCES livre (idLivre)   ON DELETE CASCADE);
			              
CREATE OR REPLACE TRIGGER membre_trigger 
BEFORE INSERT ON membre 
FOR EACH ROW

BEGIN
  SELECT membre_sequence.NEXTVAL
  INTO   :new.idMembre
  FROM   dual;
END;

CREATE OR REPLACE TRIGGER livre_trigger 
BEFORE INSERT ON livre 
FOR EACH ROW

BEGIN
  SELECT livre_sequence.NEXTVAL
  INTO   :new.idLivre
  FROM   dual;
END;

CREATE OR REPLACE TRIGGER pret_trigger 
BEFORE INSERT ON pret 
FOR EACH ROW

BEGIN
  SELECT pret_sequence.NEXTVAL
  INTO   :new.idPret
  FROM   dual;
END;

CREATE OR REPLACE TRIGGER reservation_trigger 
BEFORE INSERT ON reservation 
FOR EACH ROW
BEGIN
  SELECT reservation_sequence.NEXTVAL
  INTO   :new.idReservation
  FROM   dual;
END;
/	              
			              
			              