/* This file is part of the uml2db project.
 * For more information, please see <http://www.umleditor.org>.
 *
 * Copyright (c) 2006 Eisenhut Informatik AG
 * Permission is hereby granted, free of charge, to any person obtaining a 
 * copy of this software and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including without limitation 
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the 
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included 
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL 
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING 
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
 * DEALINGS IN THE SOFTWARE.
 */
package ch.ehi.sqlgen.generator_impl.jdbc;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;

import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;

import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.sqlgen.generator.Generator;
import ch.ehi.sqlgen.generator.TextFileUtility;
import ch.ehi.sqlgen.generator.SqlConfiguration;
import ch.ehi.sqlgen.repository.*;

/**
 * @author ce
 * @version $Revision: 1.1 $ $Date: 2006/05/30 07:40:33 $
 */
public class GeneratorJdbc implements Generator {
	protected java.io.StringWriter out=null;
	protected Connection conn=null;
	private ArrayList dropLines=null; // list<AbstractStmt>
	private ArrayList createLines=null;  // list<AbstractStmt>
	public abstract class AbstractStmt {
		private String line=null;
		public AbstractStmt(String line1){
			line=line1;
		}
		public String getLine(){
			return line;
		}
	}
	public class Stmt extends AbstractStmt {
		public Stmt(String line1){
			super(line1);
		}
	}
	public class Cmt extends AbstractStmt{
		public Cmt(String line1){
			super(line1);
		}
	}
	public GeneratorJdbc(){
	}
	public void visitSchemaBegin(ch.ehi.basics.settings.Settings config, DbSchema schema)
		throws IOException {
		conn=(Connection)config.getTransientObject(SqlConfiguration.JDBC_CONNECTION);
		if(conn==null){
			throw new IllegalArgumentException("config.getConnection()==null");
		}
		dropLines=new ArrayList();
		createLines=new ArrayList();
	}

	public void visitSchemaEnd(DbSchema schema) throws IOException {
	}

	public void visit1Begin() throws IOException {
	}

	public void visit1End() throws IOException {
	}

	public void visit2Begin() throws IOException {
	}

	public void visit2End() throws IOException {
	}
	
	protected String colSep=null;
	public void visit1TableBegin(DbTable tab) throws IOException {
		String iliname=tab.getIliName();
		if(iliname!=null){
			addCreateLine(new Cmt(tab.getIliName()));
		}
		out = new java.io.StringWriter();
		String tabName=tab.getName().getName();
		if(tab.getName().getSchema()!=null){
			tabName=tab.getName().getSchema()+"."+tabName;
		}
		out.write(getIndent()+"CREATE TABLE "+tabName+" ("+newline());
		inc_ind();
		if(iliname!=null){
			addDropLine(new Cmt(tab.getIliName()));
		}
		colSep="";
	}

	public void visit1TableEnd(DbTable tab) throws IOException {
		// write primary key
		for(Iterator idxi=tab.iteratorIndex();idxi.hasNext();){
			DbIndex idx=(DbIndex)idxi.next();
			if(idx.isPrimary()){
				out.write(getIndent()+colSep+"PRIMARY KEY (");
				String sep="";
				for(Iterator attri=idx.iteratorAttr();attri.hasNext();){
					DbColumn attr=(DbColumn)attri.next();
					out.write(sep+attr.getName());
					sep=",";
				}
				out.write(")"+newline());
				colSep=",";
			}
		}
		dec_ind();
		out.write(getIndent()+")"+newline());
		// execute stmt
		String stmt=out.toString();
		addCreateLine(new Stmt(stmt));
		addDropLine(new Stmt("DROP TABLE "+tab.getName()));
		out=null;
		if(tableExists(tab.getName())){
			if(tab.isDeleteDataIfTableExists()){
				Statement dbstmt = null;
				try{
					try{
						dbstmt = conn.createStatement();
						String delStmt="DELETE FROM "+tab.getName();
						EhiLogger.traceBackendCmd(delStmt);
						dbstmt.executeUpdate(delStmt);
					}finally{
						dbstmt.close();
					}
				}catch(SQLException ex){
					IOException iox=new IOException("failed to delete data from table "+tab.getName());
					iox.initCause(ex);
					throw iox;
				}
			}
		}else{
			Statement dbstmt = null;
			try{
				try{
					dbstmt = conn.createStatement();
					EhiLogger.traceBackendCmd(stmt);
					dbstmt.executeUpdate(stmt);
				}finally{
					dbstmt.close();
				}
			}catch(SQLException ex){
				IOException iox=new IOException("failed to create table "+tab.getName());
				iox.initCause(ex);
				throw iox;
			}
		}
		
	}

	public void visit2TableBegin(DbTable arg0) throws IOException {
	}
	public void visit2TableEnd(DbTable arg0) throws IOException {
	}
	public void visitColumn(DbColumn column) throws IOException {
		String type="";
		String size="";
		String notSupported=null;
		if(column instanceof DbColBoolean){
			type="NUMBER(1)";
		}else if(column instanceof DbColDateTime){
			type="TIMESTAMP";
		}else if(column instanceof DbColDate){
			type="DATE";
		}else if(column instanceof DbColTime){
			type="TIME";
		}else if(column instanceof DbColDecimal){
			DbColDecimal col=(DbColDecimal)column;
			type="DECIMAL("+Integer.toString(col.getSize())+","+Integer.toString(col.getPrecision())+")";
		}else if(column instanceof DbColGeometry){
			type="MDSYS.SDO_GEOMETRY";
		}else if(column instanceof DbColId){
			type="NUMBER(9)";
		}else if(column instanceof DbColUuid){
			type="VARCHAR2(36)";
		}else if(column instanceof DbColNumber){
			DbColNumber col=(DbColNumber)column;
			type="NUMBER("+Integer.toString(col.getSize())+")";
		}else if(column instanceof DbColVarchar){
			int colsize=((DbColVarchar)column).getSize();
			type="VARCHAR2("+Integer.toString(colsize)+")";
		}else{
			type="VARCHAR2(20)";
		}
		String isNull=column.isNotNull()?"NOT NULL":"NULL";
		String name=column.getName();
		out.write(getIndent()+colSep+name+" "+type+" "+isNull+newline());
		colSep=",";
	}
	public void visitTableBeginColumn(DbTable arg0) throws IOException {
	}
	public void visitTableEndColumn(DbTable arg0) throws IOException {
	}
	public void visitIndex(DbIndex arg0) throws IOException {
	}
	public void visitTableBeginIndex(DbTable arg0) throws IOException {
	}
	public void visitTableEndIndex(DbTable arg0) throws IOException {
	}
	public void visitConstraint(DbConstraint arg0) throws IOException {
	}
	public void visitTableBeginConstraint(DbTable arg0) throws IOException {
	}
	public void visitTableEndConstraint(DbTable arg0) throws IOException {
	}
	public void visitEnumEle(DbEnumEle arg0) throws IOException {
	}
	public void visitTableBeginEnumEle(DbTable arg0) throws IOException {
	}
	public void visitTableEndEnumEle(DbTable arg0) throws IOException {
	}
	// utilities
	private TextFileUtility txtOut=new TextFileUtility();
	protected String getIndent(){
		return txtOut.getIndent();
	}
	protected void inc_ind()
	{
		txtOut.inc_ind();
	}
	protected void dec_ind()
	{
		txtOut.dec_ind();
	}
	protected String newline(){
		return txtOut.newline();
	}
	protected void addDropLine(AbstractStmt dropstmt){
		dropLines.add(dropstmt);
	}
	public Iterator iteratorDropLines(){
		return dropLines.iterator();
	}
	protected void addCreateLine(AbstractStmt createstmt){
		createLines.add(createstmt);
	}
	public Iterator iteratorCreateLines(){
		return createLines.iterator();
	}
	/** tests if a table with the given name exists
	 */
	public boolean tableExists(DbTableName tableName)
	throws IOException
	{
		try{
			boolean supportsMixedCase=conn.getMetaData().supportsMixedCaseIdentifiers();
			String catalog=conn.getCatalog();
			java.sql.DatabaseMetaData meta=conn.getMetaData();
			// on oracle getUserName() returns schemaname
			// on PostgreSQL "public" is the defualt schema
			String schema=tableName.getSchema();
			if(schema==null){
				if(conn.getMetaData().getURL().startsWith("jdbc:postgresql:")){
					schema="public";
				}else{
					schema=meta.getUserName();
				}
			}
			java.sql.ResultSet rs=null;
			try{
				//rs=meta.getTables(catalog,schema,tableName.toUpperCase(),null);
				rs=meta.getTables(null,null,null,null);
				while(rs.next()){
					String db_catalogName=rs.getString("TABLE_CAT");
					String db_schemaName=rs.getString("TABLE_SCHEM");
					String db_tableName=rs.getString("TABLE_NAME");
					//EhiLogger.debug(db_catalogName+"."+db_schemaName+"."+db_tableName);
					if((db_schemaName==null || db_schemaName.equalsIgnoreCase(schema)) && db_tableName.equalsIgnoreCase(tableName.getName())){
						//EhiLogger.debug(db_catalogName+"."+db_schemaName+"."+db_tableName);
						// table exists
						return true;
					}
				}
			}finally{
				if(rs!=null)rs.close();
			}
		}catch(java.sql.SQLException ex){
			IOException iox=new IOException("failed to check if table "+tableName+" exists");
			iox.initCause(ex);
			throw iox;
		}
		return false;
	}
	public static void main(String args[]){
		//EhiLogger.getInstance().setTraceFilter(false);
		GeneratorJdbc tst=new GeneratorJdbc();
		String dburl="jdbc:postgresql:ili2pg";
		String dbusr="postgres";
		String dbpwd="ola2011";
		try {
			tst.conn=DriverManager.getConnection(dburl, dbusr, dbpwd);
			DbTableName tab=new DbTableName("t_ili2db_imports");
			System.out.println(tab.toString()+" "+tst.tableExists(tab));
			tab=new DbTableName("test1b");
			System.out.println(tab.toString()+" "+tst.tableExists(tab));
			tab=new DbTableName("schema2","test2");
			System.out.println(tab.toString()+" "+tst.tableExists(tab));
			tab=new DbTableName("schema2","test2b");
			System.out.println(tab.toString()+" "+tst.tableExists(tab));
			tab=new DbTableName("schema3","test3");
			System.out.println(tab.toString()+" "+tst.tableExists(tab));
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
