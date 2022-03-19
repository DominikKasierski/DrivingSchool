package pl.lodz.p.it.dk.auth.identitystores;

import pl.lodz.p.it.dk.security.PasswordUtils;

import javax.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;

@DatabaseIdentityStoreDefinition(
        dataSourceLookup = "java:app/jdbc/authDS",
        callerQuery = "SELECT DISTINCT password FROM auth_view WHERE login = ?",
        groupsQuery = "SELECT access FROM auth_view WHERE login = ?",
        hashAlgorithm = PasswordUtils.class
)
public class DatabaseIdentityStore {

}
