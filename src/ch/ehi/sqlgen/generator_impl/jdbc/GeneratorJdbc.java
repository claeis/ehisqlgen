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
import ch.ehi.sqlgen.DbUtility;
import ch.ehi.sqlgen.generator.Generator;
import ch.ehi.sqlgen.generator.TextFileUtility;
import ch.ehi.sqlgen.generator.SqlConfiguration;
import ch.ehi.sqlgen.repository.*;

/**
 * @author ce
 * @version $Revision: 1.1 $ $Date: 2006/05/30 07:40:33 $
 */
public class GeneratorJdbc implements Generator {
	public static int DEFAULT_NAME_LENGTH=60;
	private int _maxSqlNameLength=DEFAULT_NAME_LENGTH;
	protected java.io.StringWriter out=null;
	protected Connection conn=null;
	private ArrayList dropLines=null; // list<AbstractStmt>
	private ArrayList createLines=null;  // list<AbstractStmt>
	protected java.util.HashSet<DbTableName> createdTables=new java.util.HashSet<DbTableName>();
	private static final String MAX_SQLNAME_LENGTH="ch.ehi.ili2db.maxSqlNameLength";
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
		dropLines=new ArrayList();
		createLines=new ArrayList();
	}
	@Override
	public void visitSchemaBegin(ch.ehi.basics.settings.Settings config, DbSchema schema)
		throws IOException {
		conn=(Connection)config.getTransientObject(SqlConfiguration.JDBC_CONNECTION);
		if(config.getValue(MAX_SQLNAME_LENGTH)!=null){
			_maxSqlNameLength=Integer.parseInt(config.getValue(MAX_SQLNAME_LENGTH));
		}
	}

	@Override
	public void visitSchemaEnd(DbSchema schema) throws IOException {
	}

	@Override
	public void visit1Begin() throws IOException {
	}

	@Override
	public void visit1End() throws IOException {
	}

	@Override
	public void visit2Begin() throws IOException {
	}

	@Override
	public void visit2End() throws IOException {
	}
	
	protected String colSep=null;
	@Override
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

	@Override
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
        String cmt=getTableEndOptions(tab);
        if(cmt!=null) {
            out.write(getIndent()+") "+cmt+newline());
        }else {
            out.write(getIndent()+")"+newline());
        }
		// execute stmt
		String stmt=out.toString();
		addCreateLine(new Stmt(stmt));
		addDropLine(new Stmt("DROP TABLE "+tab.getName()));
		out=null;
		if(conn!=null) {
	        if(DbUtility.tableExists(conn,tab.getName())){
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
	                    createdTables.add(tab.getName());
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
		
	}

	protected String getTableEndOptions(DbTable dbTab) {
        return null;
    }
    @Override
	public void visit2TableBegin(DbTable arg0) throws IOException {
	}
	@Override
	public void visit2TableEnd(DbTable arg0) throws IOException {
	}
	@Override
	public void visitColumn(DbTable dbTab,DbColumn column) throws IOException {
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
			type="BLOB";
		}else if(column instanceof DbColId){
			type="NUMBER(9)";
		}else if(column instanceof DbColUuid){
			type="VARCHAR2(36)";
		}else if(column instanceof DbColNumber){
			DbColNumber col=(DbColNumber)column;
			type="NUMBER("+Integer.toString(col.getSize())+")";
		}else if(column instanceof DbColVarchar){
			int colsize=((DbColVarchar)column).getSize();
			type="VARCHAR("+Integer.toString(colsize)+")";
		}else{
			type="VARCHAR(20)";
		}
		String isNull=column.isNotNull()?"NOT NULL":"NULL";
		String name=column.getName();
		out.write(getIndent()+colSep+name+" "+type+" "+isNull+newline());
		colSep=",";
	}
	@Override
	public void visitTableBeginColumn(DbTable arg0) throws IOException {
	}
	@Override
	public void visitTableEndColumn(DbTable arg0) throws IOException {
	}
	@Override
	public void visitIndex(DbIndex idx) throws IOException {
		if(idx.isUnique()){
			java.io.StringWriter out = new java.io.StringWriter();
			DbTable tab=idx.getTable();
			String tableName=tab.getName().getQName();
			String constraintName=idx.getName();
			if(constraintName==null){
				String colNames[]=new String[idx.sizeAttr()];
				int i=0;
				for(Iterator attri=idx.iteratorAttr();attri.hasNext();){
					DbColumn attr=(DbColumn)attri.next();
					colNames[i++]=attr.getName();
				}
				constraintName=createConstraintName(tab,"key", colNames);
			}
			out.write(getIndent()+"ALTER TABLE "+tableName+" ADD CONSTRAINT "+constraintName+" UNIQUE (");
			String sep="";
			for(Iterator attri=idx.iteratorAttr();attri.hasNext();){
				DbColumn attr=(DbColumn)attri.next();
				out.write(sep+attr.getName());
				sep=",";
			}
			out.write(")"+newline());
			String stmt=out.toString();
			addCreateLine(new Stmt(stmt));
			out=null;
			if(conn!=null) {
	            if(createdTables.contains(tab.getName())){
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
	                    IOException iox=new IOException("failed to add UNIQUE to table "+tab.getName());
	                    iox.initCause(ex);
	                    throw iox;
	                }
	            }
			}
		}
	}
	@Override
	public void visitTableBeginIndex(DbTable arg0) throws IOException {
	}
	@Override
	public void visitTableEndIndex(DbTable arg0) throws IOException {
	}
	@Override
	public void visitConstraint(DbConstraint arg0) throws IOException {
		
	}
	@Override
	public void visitTableBeginConstraint(DbTable arg0) throws IOException {
	}
	@Override
	public void visitTableEndConstraint(DbTable arg0) throws IOException {
	}
	@Override
	public void visitEnumEle(DbEnumEle arg0) throws IOException {
	}
	@Override
	public void visitTableBeginEnumEle(DbTable arg0) throws IOException {
	}
	@Override
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
	public void addDropLine(AbstractStmt dropstmt){
		dropLines.add(dropstmt);
	}
	public Iterator iteratorDropLines(){
		return dropLines.iterator();
	}
	public void addCreateLine(AbstractStmt createstmt){
		createLines.add(createstmt);
	}
	public Iterator iteratorCreateLines(){
		return createLines.iterator();
	}
	/** tests if a table with the given name exists
	 * @deprecated
	 */
	public boolean tableExists(DbTableName tableName)
	throws IOException
	{
		return conn!=null && DbUtility.tableExists(conn, tableName);
	}
	/** tests if a table with the given name was created in phase1
	 */
	public boolean tableCreated(DbTableName tableName)
	{
		return createdTables.contains(tableName);
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
			System.out.println(tab.toString()+" "+DbUtility.tableExists(tst.conn,tab));
			tab=new DbTableName("test1b");
			System.out.println(tab.toString()+" "+DbUtility.tableExists(tst.conn,tab));
			tab=new DbTableName("schema2","test2");
			System.out.println(tab.toString()+" "+DbUtility.tableExists(tst.conn,tab));
			tab=new DbTableName("schema2","test2b");
			System.out.println(tab.toString()+" "+DbUtility.tableExists(tst.conn,tab));
			tab=new DbTableName("schema3","test3");
			System.out.println(tab.toString()+" "+DbUtility.tableExists(tst.conn,tab));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	protected void addConstraint(DbTable dbTab, String constraintName, String createstmt, String dropstmt)
			throws IOException {
		addCreateLine(new Stmt(createstmt));
		addDropLine(new Stmt(dropstmt));
		
		if(conn!=null && tableCreated(dbTab.getName())){
			Statement dbstmt = null;
			try{
				try{
					dbstmt = conn.createStatement();
					EhiLogger.traceBackendCmd(createstmt);
					dbstmt.execute(createstmt);
				}finally{
					dbstmt.close();
				}
			}catch(SQLException ex){
				IOException iox=new IOException("failed to add constraint "+constraintName+" to table "+dbTab.getName());
				iox.initCause(ex);
				throw iox;
			}
		}
	}
	protected String createConstraintName(DbTable table,String suffix, String... colNames) {
		int elec=colNames.length+1;
		int suffixLen=0;
		if(suffix!=null){
			suffixLen=suffix.length()+1;
		}
		int maxLength=getMaxNameLength()-suffixLen-1;
		int eleLength=maxLength/elec-1;
		StringBuffer name=new StringBuffer();
		name.append(shortcutName(table.getName().getName(),eleLength));
		for(String colName:colNames){
			name.append("_");
			name.append(shortcutName(colName,eleLength));
		}
		if(suffix!=null){
			name.append("_");
			name.append(suffix);
		}
		String sqlname=name.toString();
		String base=sqlname;
		int c=1;
		while(table.getSchema().containsConstraintName(sqlname)){
			sqlname=base+Integer.toString(c++);
		}
		table.getSchema().addConstraintName(sqlname);
		return sqlname;
	}

	public static String shortcutName(String aname, int maxlen) {
		StringBuffer name = new StringBuffer(aname);
		// number of charcters to remove
		int stripc = name.length() - maxlen;
		if (stripc <= 0)
			return aname;
		// remove vocals
		for (int i = name.length() - 4; i >= 3; i--) {
			char c = name.charAt(i);
			if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u'
					|| c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U') {
				name.deleteCharAt(i);
				stripc--;
				if (stripc == 0)
					return name.toString();
			}
		}
		// still to long
		// remove from the middle of the name
		int start = (name.length() - stripc) / 2;
		name.delete(start, start + stripc);
		// ASSERT(!name.IsEmpty());
		return name.toString();
	}

	private int getMaxNameLength() {
		return _maxSqlNameLength;
	}
}
