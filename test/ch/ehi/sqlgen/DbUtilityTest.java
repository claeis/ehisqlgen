package ch.ehi.sqlgen;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Test;

import ch.ehi.sqlgen.repository.DbTableName;

public class DbUtilityTest {
	public static final String DBSCHEMA = "DbUtilityTest";
	String dburl=System.getProperty("dburl"); 
	String dbuser=System.getProperty("dbusr");
	String dbpwd=System.getProperty("dbpwd"); 

	@Test
	public void tableExists() throws SQLException, ClassNotFoundException {
	    Class driverClass = Class.forName("org.postgresql.Driver");
        Connection conn = DriverManager.getConnection(dburl, dbuser, dbpwd);
        Statement stmt = conn.createStatement();

        final String TABLE1="DbUtility1";
        final String TABLE2="DbUtility2";
        final String TABLE_XXX="TableXXXX234012";
		stmt.execute("DROP SCHEMA IF EXISTS "+DBSCHEMA+" CASCADE;");
		stmt.execute("CREATE SCHEMA "+DBSCHEMA+";");
		stmt.execute("CREATE TABLE "+DBSCHEMA+"."+TABLE1+"()");
		stmt.execute("CREATE TABLE IF NOT EXISTS "+TABLE2+"()");
		stmt.close();
		assertTrue(DbUtility.tableExists(conn, new DbTableName(DBSCHEMA, TABLE1)));
		assertFalse(DbUtility.tableExists(conn, new DbTableName(DBSCHEMA, TABLE_XXX)));
		assertTrue(DbUtility.tableExists(conn, new DbTableName(TABLE2)));
		assertFalse(DbUtility.tableExists(conn, new DbTableName(TABLE_XXX)));
		conn.close();
	}
	@Test
	public void sequenceExists() throws SQLException, ClassNotFoundException {
	    Class driverClass = Class.forName("org.postgresql.Driver");
        Connection conn = DriverManager.getConnection(dburl, dbuser, dbpwd);
        Statement stmt = conn.createStatement();

        final String SEQ1="t_ili2db_seq";
        final String SEQ_XXX="SeqXXXX234012";
		stmt.execute("DROP SCHEMA IF EXISTS "+DBSCHEMA+" CASCADE;");
		stmt.execute("CREATE SCHEMA "+DBSCHEMA+";");
		stmt.execute("CREATE SEQUENCE "+DBSCHEMA+"."+SEQ1);
		stmt.close();
		assertTrue(DbUtility.sequenceExists(conn, new DbTableName(DBSCHEMA, SEQ1)));
		assertFalse(DbUtility.sequenceExists(conn, new DbTableName(DBSCHEMA, SEQ_XXX)));
		conn.close();
	}
	@Test
	public void schemaExists() throws SQLException, ClassNotFoundException {
	    Class driverClass = Class.forName("org.postgresql.Driver");
        Connection conn = DriverManager.getConnection(dburl, dbuser, dbpwd);
        Statement stmt = conn.createStatement();

        final String SCHEMA_XXX="SeqXXXX234012";
		stmt.execute("DROP SCHEMA IF EXISTS "+DBSCHEMA+" CASCADE;");
		stmt.execute("CREATE SCHEMA "+DBSCHEMA+";");
		stmt.close();
		assertTrue(DbUtility.schemaExists(conn, DBSCHEMA));
		assertFalse(DbUtility.schemaExists(conn, SCHEMA_XXX));
		conn.close();
	}
	@Test
	public void createSchema() throws SQLException, ClassNotFoundException {
	    Class driverClass = Class.forName("org.postgresql.Driver");
        Connection conn = DriverManager.getConnection(dburl, dbuser, dbpwd);
        Statement stmt = conn.createStatement();

		stmt.execute("DROP SCHEMA IF EXISTS "+DBSCHEMA+" CASCADE;");
		stmt.close();
		DbUtility.createSchema(conn, DBSCHEMA);
		assertTrue(DbUtility.schemaExists(conn, DBSCHEMA));
		conn.close();
	}

}
