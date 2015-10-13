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
import java.sql.Statement;
import java.sql.SQLException;

import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.basics.settings.Settings;
import ch.ehi.sqlgen.generator.SqlConfiguration;
import ch.ehi.sqlgen.repository.*;

/**
 * @author ce
 * @version $Revision: 1.1 $ $Date: 2007/01/27 09:26:12 $
 */
public class GeneratorPostgresql extends GeneratorJdbc {

	private boolean createGeomIdx=false;
	public void visitSchemaBegin(Settings config, DbSchema schema)
			throws IOException {
		super.visitSchemaBegin(config, schema);
		if("True".equals(config.getValue(SqlConfiguration.CREATE_GEOM_INDEX))){
			createGeomIdx=true;
		}
	}

	public void visitColumn(DbColumn column) throws IOException {
		String type="";
		String size="";
		String notSupported=null;
		boolean createColNow=true;
		if(column instanceof DbColBoolean){
			type="boolean";
		}else if(column instanceof DbColDateTime){
			type="timestamp";
		}else if(column instanceof DbColDate){
			type="date";
		}else if(column instanceof DbColTime){
			type="time";
		}else if(column instanceof DbColDecimal){
			DbColDecimal col=(DbColDecimal)column;
			type="decimal("+Integer.toString(col.getSize())+","+Integer.toString(col.getPrecision())+")";
		}else if(column instanceof DbColGeometry){
			// just collect it; process it later
			geomColumns.add((DbColGeometry) column);
			createColNow=false;
		}else if(column instanceof DbColId){
			type="integer";
		}else if(column instanceof DbColUuid){
			type="uuid";
		}else if(column instanceof DbColNumber){
			DbColNumber col=(DbColNumber)column;
			type="integer";
		}else if(column instanceof DbColVarchar){
			int colsize=((DbColVarchar)column).getSize();
			if(colsize==-1){
				type="text";
			}else{
				type="varchar("+Integer.toString(colsize)+")";
			}
		}else{
			type="text";
		}
		String isNull=column.isNotNull()?"NOT NULL":"NULL";
		if(column.isPrimaryKey()){
			isNull="PRIMARY KEY";
		}
		String sep=" ";
		String defaultValue="";
		if(column.getDefaultValue()!=null){
			defaultValue=sep+"DEFAULT "+column.getDefaultValue();
			sep=" ";
		}
		if(column.isIndex() || (createGeomIdx && column instanceof DbColGeometry)){
			// just collect it; process it later
			indexColumns.add(column);
		}
		if(createColNow){
			String name=column.getName();
			out.write(getIndent()+colSep+name+" "+type+" "+isNull+defaultValue+newline());
			colSep=",";
		}
	}
	private ArrayList<DbColumn> indexColumns=null;
	private ArrayList<DbColGeometry> geomColumns=null;
	public void visit1TableBegin(DbTable tab) throws IOException {
		super.visit1TableBegin(tab);
		geomColumns=new ArrayList<DbColGeometry>();
		indexColumns=new ArrayList<DbColumn>();
	}

	public void visit1TableEnd(DbTable tab) throws IOException {
		String sqlTabName=tab.getName().getName();
		if(tab.getName().getSchema()!=null){
			sqlTabName=tab.getName().getSchema()+"."+sqlTabName;
		}
		boolean tableExists=tableExists(tab.getName());
		super.visit1TableEnd(tab);
		
		
		
		for(DbColGeometry geo:geomColumns){
			String stmt=null;
			if(tab.getName().getSchema()!=null){
				stmt="SELECT AddGeometryColumn(\'"+tab.getName().getSchema().toLowerCase()+"\',\'"+tab.getName().getName().toLowerCase()+"\',\'"
				+geo.getName().toLowerCase()+"\'"
				+ ",(SELECT srid FROM SPATIAL_REF_SYS WHERE AUTH_NAME=\'"+geo.getSrsAuth()+"\' AND AUTH_SRID="+geo.getSrsId()+"),\'"
				+getPostgisType(geo.getType())+"\',"
				+ geo.getDimension()
				+")";
				
			}else{
				stmt="SELECT AddGeometryColumn(\'"+tab.getName().getName().toLowerCase()+"\',\'"
				+geo.getName().toLowerCase()+"\'"
				+ ",(SELECT srid FROM SPATIAL_REF_SYS WHERE AUTH_NAME=\'"+geo.getSrsAuth()+"\' AND AUTH_SRID="+geo.getSrsId()+"),\'"
				+getPostgisType(geo.getType())+"\',"
				+ geo.getDimension()
				+")";
				
			}
			addCreateLine(new Stmt(stmt));
						
			String dropstmt=null;
			if(tab.getName().getSchema()!=null){
				dropstmt="SELECT DropGeometryColumn(\'"+tab.getName().getSchema().toLowerCase()+"\',\'"+tab.getName().getName().toLowerCase()+"\',\'"
				+geo.getName().toLowerCase()+"\'"
				+")";
			}else{
				dropstmt="SELECT DropGeometryColumn(\'"+tab.getName().getName().toLowerCase()+"\',\'"
				+geo.getName().toLowerCase()+"\'"
				+")";
			}
			addDropLine(new Stmt(dropstmt));
			if(!tableExists){
				Statement dbstmt = null;
				try{
					try{
						dbstmt = conn.createStatement();
						EhiLogger.traceBackendCmd(stmt);
						dbstmt.execute(stmt);
					}finally{
						dbstmt.close();
					}
				}catch(SQLException ex){
					IOException iox=new IOException("failed to add geometry column "+geo.getName()+" to table "+tab.getName());
					iox.initCause(ex);
					throw iox;
				}
			}
		}
		geomColumns=null;

		for(DbColumn idxcol:indexColumns){
			
			String idxstmt=null;
			if(idxcol instanceof DbColGeometry){
				idxstmt="CREATE INDEX "+tab.getName().getName().toLowerCase()+"_"+idxcol.getName().toLowerCase()+"_idx ON "+sqlTabName.toLowerCase()+" USING GIST ( "+idxcol.getName().toLowerCase()+" )";
			}else{
				idxstmt="CREATE INDEX "+tab.getName().getName().toLowerCase()+"_"+idxcol.getName().toLowerCase()+"_idx ON "+sqlTabName.toLowerCase()+" ( "+idxcol.getName().toLowerCase()+" )";
			}
			addCreateLine(new Stmt(idxstmt));
			
			if(!tableExists){
				Statement dbstmt = null;
				try{
					try{
						dbstmt = conn.createStatement();
						EhiLogger.traceBackendCmd(idxstmt);
						dbstmt.execute(idxstmt);
					}finally{
						dbstmt.close();
					}
				}catch(SQLException ex){
					IOException iox=new IOException("failed to add index on column "+tab.getName()+"."+idxcol.getName());
					iox.initCause(ex);
					throw iox;
				}
			}
		}
		indexColumns=null;
		
		String cmt=tab.getComment();
		if(cmt!=null){
			cmt="COMMENT ON TABLE "+sqlTabName+" IS '"+escapeString(cmt)+"'";
			addCreateLine(new Stmt(cmt));
			if(!tableExists){
				Statement dbstmt = null;
				try{
					try{
						dbstmt = conn.createStatement();
						EhiLogger.traceBackendCmd(cmt);
						dbstmt.execute(cmt);
					}finally{
						dbstmt.close();
					}
				}catch(SQLException ex){
					IOException iox=new IOException("failed to add comment to table "+tab.getName());
					iox.initCause(ex);
					throw iox;
				}
			}
		}
		
		java.util.Iterator coli=tab.iteratorColumn();
		while(coli.hasNext()){
			DbColumn col=(DbColumn)coli.next();
			cmt=col.getComment();
			if(cmt!=null){
				cmt="COMMENT ON COLUMN "+sqlTabName+"."+col.getName()+" IS '"+escapeString(cmt)+"'";
				addCreateLine(new Stmt(cmt));
				if(!tableExists){
					Statement dbstmt = null;
					try{
						try{
							dbstmt = conn.createStatement();
							EhiLogger.traceBackendCmd(cmt);
							dbstmt.execute(cmt);
						}finally{
							dbstmt.close();
						}
					}catch(SQLException ex){
						IOException iox=new IOException("failed to add comment to table "+tab.getName());
						iox.initCause(ex);
						throw iox;
					}
				}
			}
		}
		
		// ALTER TABLE <tablename> ADD CONSTRAINT fid_pkey PRIMARY KEY  (<primary key column>);
	}
	static public String escapeString(String cmt)
	{
		StringBuilder ret=new StringBuilder((int)cmt.length());
		for(int i=0;i<cmt.length();i++){
			char c=cmt.charAt(i);
			ret.append(c);
			if(c=='\''){
				ret.append(c);
			}
		}
		return ret.toString();
	}
	static public String getPostgisType(int type)
	{
		 switch(type){
			case DbColGeometry.POINT:
				return "POINT";
			case DbColGeometry.LINESTRING:
				return "LINESTRING";
			case DbColGeometry.POLYGON:
				return "POLYGON";
			case DbColGeometry.MULTIPOINT:
				return "MULTIPOINT";
			case DbColGeometry.MULTILINESTRING:
				return "MULTILINESTRING";
			case DbColGeometry.MULTIPOLYGON:
				return "MULTIPOLYGON";
			case DbColGeometry.GEOMETRYCOLLECTION:
				return "GEOMETRYCOLLECTION";
			case DbColGeometry.CIRCULARSTRING:
				return "CIRCULARSTRING";
			case DbColGeometry.COMPOUNDCURVE:
				return "COMPOUNDCURVE";
			case DbColGeometry.CURVEPOLYGON:
				return "CURVEPOLYGON";
			case DbColGeometry.MULTICURVE:
				return "MULTICURVE";
			case DbColGeometry.POLYHEDRALSURFACE:
				return "POLYHEDRALSURFACE";
			case DbColGeometry.TIN:
				return "TIN";
			case DbColGeometry.TRIANGLE:
				return "TRIANGLE";
			default:
				throw new IllegalArgumentException();
		 }
	}

	@Override
	public void visitTableBeginConstraint(DbTable dbTab) throws IOException {
		super.visitTableBeginConstraint(dbTab);
		
		String sqlTabName=dbTab.getName().getQName();
		for(Iterator dbColi=dbTab.iteratorColumn();dbColi.hasNext();){
			DbColumn dbCol=(DbColumn) dbColi.next();
			if(dbCol.getReferencedTable()!=null){
				String createstmt=null;
				String action="";
				if(dbCol.getOnUpdateAction()!=null){
					action=action+" ON UPDATE "+dbCol.getOnUpdateAction();
				}
				if(dbCol.getOnDeleteAction()!=null){
					action=action+" ON DELETE "+dbCol.getOnDeleteAction();
				}
				String constraintName=dbTab.getName().getName()+"_"+dbCol.getName()+"_fkey";
				//  ALTER TABLE ce.classb1 ADD CONSTRAINT classb1_t_id_fkey FOREIGN KEY ( t_id ) REFERENCES ce.classa1;
				createstmt="ALTER TABLE "+sqlTabName+" ADD CONSTRAINT "+constraintName+" FOREIGN KEY ( "+dbCol.getName()+" ) REFERENCES "+dbCol.getReferencedTable().getQName()+action+" DEFERRABLE INITIALLY DEFERRED";
				addCreateLine(new Stmt(createstmt));
				
				//  ALTER TABLE ce.classb1 DROP CONSTRAINT classb1_t_id_fkey;
				String dropstmt=null;
				dropstmt="ALTER TABLE "+sqlTabName+" DROP CONSTRAINT "+constraintName;
				addDropLine(new Stmt(dropstmt));
				
				if(tableCreated(dbTab.getName())){
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
						IOException iox=new IOException("failed to add fk constraint to table "+dbTab.getName());
						iox.initCause(ex);
						throw iox;
					}
				}
				
			}
		}
	}
}
