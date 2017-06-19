package ch.ehi.sqlgen;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Test;

import ch.ehi.sqlgen.repository.DbTableName;

public class DbUtilityTest {
	private static final String DBSCHEMA = "DbUtilityTest";
	String dburl=System.getProperty("dburl"); 
	String dbuser=System.getProperty("dbusr");
	String dbpwd=System.getProperty("dbpwd"); 

	@Test
	public void test() throws SQLException, ClassNotFoundException {
	    Class driverClass = Class.forName("org.postgresql.Driver");
        Connection conn = DriverManager.getConnection(dburl, dbuser, dbpwd);
        Statement stmt = conn.createStatement();

        final String TABLE1="DbUtility1";
        final String TABLE2="DbUtility2";
        final String TABLE_XXX="TableXXXX234012";
        final String SEQ1="t_ili2db_seq";
		stmt.execute("DROP SCHEMA IF EXISTS "+DBSCHEMA+" CASCADE;");
		stmt.execute("CREATE SCHEMA "+DBSCHEMA+";");
		stmt.execute("CREATE SEQUENCE "+DBSCHEMA+"."+SEQ1);
		stmt.execute("CREATE TABLE "+DBSCHEMA+"."+TABLE1+"()");
		stmt.execute("CREATE TABLE IF NOT EXISTS "+TABLE2+"()");
		stmt.close();
		assertTrue(DbUtility.tableExists(conn, new DbTableName(DBSCHEMA, TABLE1)));
		assertFalse(DbUtility.tableExists(conn, new DbTableName(DBSCHEMA, TABLE_XXX)));
		assertTrue(DbUtility.sequenceExists(conn, new DbTableName(DBSCHEMA, SEQ1)));
		assertTrue(DbUtility.tableExists(conn, new DbTableName(TABLE2)));
		assertFalse(DbUtility.tableExists(conn, new DbTableName(TABLE_XXX)));
		conn.close();
	}

}
