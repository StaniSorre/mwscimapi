namespace com.enel.ipscan;
using { managed } from '@sap/cds/common';
 
// Tabella di anagrafica utente
entity Users: managed {
	key	externalId	: String(50);	// Matricola utente
		id			: UUID;			// Serve a Compaq
		familyName	: String(50);	// Cognome
		givenName	: String(50);	// Nome
		mail		: String(50);
}

// Tabella di trascodifica codice ruolo / descrizione (es. 01 - Responsabile)
// entity Roles: managed {
// 	key roleId			: String(2);	// ID Ruolo
// 	key roleDescription	: String(50);	// Descrizone ruolo
// }

// Tabella di atteribuzione ruoli e attributi
// es. MarioRossi responsabile su area X dal - al
// se. MarioRossi responsabile su zona y dal - al
entity UsersRolesAndAttributes: managed {
	key id				: UUID;			// Identificativo utente
	// key roleId			: String(2);	// ID Ruolo (responsabile, controllore, ecc..)
	key attributeId		: String(3);	// Area, Zona, Unità operativa, Area ALL (001 - 002 - 003 - 004)
	key attributeValue	: String(10);	// Codice dell'area, della zona o della unità operativa
	key dateFrom		: String(8);
	key dateTo			: String(8);
	project				: String(50);
	role				: String(50);
}

// ----- Associazioni

extend Users with {
	UsersRolesAndAttributes: Association to many UsersRolesAndAttributes on UsersRolesAndAttributes.id = $self.id;
}