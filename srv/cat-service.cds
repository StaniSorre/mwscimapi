using com.enel.ipscan as my from '../db/data-model';

service CatalogServiceApiSCIM @(requires: ['authenticated-user', 'system-user']) {

	entity Users 
		@(Capabilities: {
			InsertRestrictions: {Insertable: true},
			UpdateRestrictions: {Updatable: true},
			DeleteRestrictions: {Deletable: true}
		})
		@(restrict:[
			{grant:['READ','WRITE'], to:'ApiScimCreate_SC'},
			{grant:['READ','WRITE'], to:'ApiScimExternal_SC'},
			{grant:['READ'], to:'ApiScimRead_SC'}
		])
	as projection on my.Users;
	
	
	entity UsersRolesAndAttributes 
		@(Capabilities: {
			InsertRestrictions: {Insertable: true},
			UpdateRestrictions: {Updatable: true},
			DeleteRestrictions: {Deletable: true}
		})
		@(restrict:[
			{grant:['READ','WRITE'], to:'ApiScimCreate_SC'},
			{grant:['READ','WRITE'], to:'ApiScimExternal_SC'},
			{grant:['READ'], to:'ApiScimRead_SC'}
		])
	as projection on my.UsersRolesAndAttributes;

}