package pl.lodz.p.it.dk.common.configs;

import javax.annotation.sql.DataSourceDefinition;
import javax.ejb.Stateless;
import java.sql.Connection;

@DataSourceDefinition(
        name = "java:app/jdbc/authDS",
        className = "org.postgresql.ds.PGSimpleDataSource",
        user = "root",
        password = "root",
        portNumber = 5432,
        databaseName = "database",
        isolationLevel = Connection.TRANSACTION_READ_COMMITTED,
        maxPoolSize = 32,
        minPoolSize = 8)

@DataSourceDefinition(
        name = "java:app/jdbc/mokDS",
        className = "org.postgresql.ds.PGSimpleDataSource",
        user = "root",
        password = "root",
        portNumber = 5432,
        databaseName = "database",
        isolationLevel = Connection.TRANSACTION_READ_COMMITTED,
        maxPoolSize = 32,
        minPoolSize = 8)

@DataSourceDefinition(
        name = "java:app/jdbc/mosDS",
        className = "org.postgresql.ds.PGSimpleDataSource",
        user = "root",
        password = "root",
        portNumber = 5432,
        databaseName = "database",
        isolationLevel = Connection.TRANSACTION_READ_COMMITTED,
        maxPoolSize = 32,
        minPoolSize = 8)

@Stateless
public class JDBCConfig {

}
