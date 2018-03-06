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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.basics.settings.Settings;
import ch.ehi.basics.tools.TopoSort;
import ch.ehi.sqlgen.DbUtility;
import ch.ehi.sqlgen.generator.SqlConfiguration;
import ch.ehi.sqlgen.repository.*;

/**
 * @author ce
 * @version $Revision: 1.1 $ $Date: 2007/01/27 09:26:12 $
 */
public class GeneratorGeoPackage extends GeneratorJdbc {

	private boolean createGeomIdx=false;
	private String today="";
	@Override
	public void visitSchemaBegin(Settings config, DbSchema schema)
			throws IOException {
		super.visitSchemaBegin(config, schema);
		today=new SimpleDateFormat("yyy-MM-dd'T'HH:mm:ss.S'Z'").format(new Date());
		if("True".equals(config.getValue(SqlConfiguration.CREATE_GEOM_INDEX))){
			createGeomIdx=true;
		}
		// sort tables 
		TopoSort topoSort=new TopoSort();
		for(DbTable table:schema.getTables()){
			topoSort.add(table);
			for(Iterator<DbColumn> dbColi=table.iteratorColumn();dbColi.hasNext();){
				DbColumn dbCol=dbColi.next();
				if(dbCol.getReferencedTable()!=null){
					DbTable refTable=schema.findTable(dbCol.getReferencedTable());
					if(refTable!=null){
						topoSort.addcond(refTable, table);
					}
				}
			}
		}
		if(!topoSort.sort()) {
		      StringBuffer loopele=new StringBuffer();
		      Iterator resi=topoSort.getResult().iterator();
		      String sep="";
		      while(resi.hasNext()){
		          DbTable res=(DbTable)resi.next();
		        loopele.append(sep);
		        loopele.append(res.getName().getName());
		        sep="->";
		      }
		      throw new IOException("loop in create table statements: "+loopele.toString());
		}
		schema.setTables(topoSort.getResult());
	}

	@Override
	public void visitColumn(DbTable dbTab,DbColumn column) throws IOException {
		String type="";
		String size="";
		String notSupported=null;
		boolean createColNow=true;
		String createConstraintStmt="";
		if(column instanceof DbColBoolean){
			type="BOOLEAN";
		}else if(column instanceof DbColDateTime){
			type="DATETIME";
		}else if(column instanceof DbColDate){
			type="DATE";
		}else if(column instanceof DbColTime){
			type="TIME";
		}else if(column instanceof DbColDecimal){
			DbColDecimal col=(DbColDecimal)column;
			type="DOUBLE";
			String constraintAction=null;
			if(col.getMaxValue()!=null || col.getMinValue()!=null){
				if(col.getMaxValue()==null){
					constraintAction=">="+col.getMinValue();
				}else if(col.getMinValue()==null){
					constraintAction="<="+col.getMaxValue();
				}else{
					constraintAction="BETWEEN "+col.getMinValue()+" AND "+col.getMaxValue();
				}
			}
			if(constraintAction!=null){
				String constraintName=createConstraintName(dbTab,"check",col.getName());
				createConstraintStmt=" CONSTRAINT "+constraintName+" CHECK( "+col.getName()+" "+constraintAction+")";
			}
		}else if(column instanceof DbColGeometry){
			geomColumns.add((DbColGeometry) column);
			type=getGpkgGeometryTypename(((DbColGeometry) column).getType());
		}else if(column instanceof DbColId){
			type="INTEGER";
		}else if(column instanceof DbColUuid){
			type="TEXT(36)"; // 550e8400-e29b-11d4-a716-446655440000
		}else if(column instanceof DbColNumber){
			DbColNumber col=(DbColNumber)column;
			type="INTEGER";
			String constraintAction=null;
			if(col.getMaxValue()!=null || col.getMinValue()!=null){
				if(col.getMaxValue()==null){
					constraintAction=">="+col.getMinValue();
				}else if(col.getMinValue()==null){
					constraintAction="<="+col.getMaxValue();
				}else{
					constraintAction="BETWEEN "+col.getMinValue()+" AND "+col.getMaxValue();
				}
			}
			if(constraintAction!=null){
				String constraintName=createConstraintName(dbTab,"check",col.getName());
				createConstraintStmt=" CONSTRAINT "+constraintName+" CHECK( "+col.getName()+" "+constraintAction+")";
			}
		}else if(column instanceof DbColVarchar){
			int colsize=((DbColVarchar)column).getSize();
			if(colsize==DbColVarchar.UNLIMITED){
				type="TEXT";
			}else{
				type="TEXT("+Integer.toString(colsize)+")";
			}
		}else if(column instanceof DbColBlob){
			type="BLOB";
		}else if(column instanceof DbColXml){
			type="TEXT";
		}else{
			type="TEXT";
		}
		String isNull=column.isNotNull()?"NOT NULL":"NULL";
		if(column.isPrimaryKey()){
			isNull="NOT NULL PRIMARY KEY";
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
		String fkConstraint="";
		if(column.getReferencedTable()!=null){
			String action="";
			if(column.getOnUpdateAction()!=null){
				action=action+" ON UPDATE "+column.getOnUpdateAction();
			}
			if(column.getOnDeleteAction()!=null){
				action=action+" ON DELETE "+column.getOnDeleteAction();
			}
			String constraintName=createConstraintName(dbTab,"fkey",column.getName());
			fkConstraint=" CONSTRAINT "+constraintName+" REFERENCES "+column.getReferencedTable().getQName()+action+" DEFERRABLE INITIALLY DEFERRED";
		}
		if(createColNow){
			String name=column.getName();
			out.write(getIndent()+colSep+name+" "+type+" "+isNull+defaultValue+fkConstraint+createConstraintStmt+newline());
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

	@Override
	public void visitTableEndColumn(DbTable tab) throws IOException {
		for(Iterator idxi=tab.iteratorIndex();idxi.hasNext();){
			DbIndex idx=(DbIndex)idxi.next();
			if(idx.isUnique()){
				
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
				
				
				out.write(getIndent()+colSep+"CONSTRAINT "+constraintName+" UNIQUE (");
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
	}
	@Override
	public void visit1TableEnd(DbTable tab) throws IOException {
		String sqlTabName=tab.getName().getName();
		if(tab.getName().getSchema()!=null){
			sqlTabName=tab.getName().getSchema()+"."+sqlTabName;
		}
		boolean tableExists=DbUtility.tableExists(conn,tab.getName());
		super.visit1TableEnd(tab);
		
		
		//INSERT INTO gpkg_contents (table_name,data_type,identifier,description,last_change,min_x,min_y,max_x,max_y,srs_id) 
		//VALUES ('BoFlaeche','features','DM01.BoFlaeche','Beschreibung BoFlaeche','2016-01-01T08:19:32.000Z',580000.000,205000.000,660000.000,270000.000,21781)

		//INSERT INTO gpkg_geometry_columns (table_name,column_name,geometry_type_name,srs_id,z,m) 
		//VALUES ('BoFlaeche' ,'Geometrie' ,'CURVEPOLYGON',21781,0,0);
		
		if(geomColumns.size()>0){
			DbColGeometry geo=geomColumns.get(0);
			String srsId="(SELECT srs_id FROM gpkg_spatial_ref_sys WHERE organization=\'"+geo.getSrsAuth()+"\' AND organization_coordsys_id="+geo.getSrsId()+")";
			String cmt=tab.getComment()==null?"null":"\'"+tab.getComment()+"\'";
			String stmt1="INSERT INTO gpkg_contents (table_name,data_type,identifier,description,last_change,min_x,min_y,max_x,max_y,srs_id)" 
			+"VALUES (\'"+tab.getName().getName()+"\','features',\'"+tab.getName().getName()+"\',"+cmt+",\'"+today+"\',"+geo.getMin1()+","+geo.getMin2()+","+geo.getMax1()+","+geo.getMax2()+","+srsId+")";
			String stmt2="INSERT INTO gpkg_geometry_columns (table_name,column_name,geometry_type_name,srs_id,z,m)" 
			+"VALUES (\'"+tab.getName().getName()+"\',\'"+geo.getName()+"\' ,\'"+getGpkgGeometryTypename(geo.getType())+"\',"+srsId+","+(geo.getDimension()==2?"0":"1")+",0)";
			
			addCreateLine(new Stmt(stmt1));
			addCreateLine(new Stmt(stmt2));
						
			String dropstmt1="DELETE FROM gpkg_contents WHERE table_name=\'"+tab.getName().getName()+"\'";
			addDropLine(new Stmt(dropstmt1));
			String dropstmt2="DELETE FROM gpkg_geometry_columns WHERE table_name=\'"+tab.getName().getName()+"\' AND column_name=\'"+geo.getName()+"\'";
			addDropLine(new Stmt(dropstmt2));
			if(!tableExists){
				Statement dbstmt = null;
				try{
					try{
						dbstmt = conn.createStatement();
						EhiLogger.traceBackendCmd(stmt1);
						dbstmt.execute(stmt1);
					}finally{
						dbstmt.close();
					}
					try{
						dbstmt = conn.createStatement();
						EhiLogger.traceBackendCmd(stmt2);
						dbstmt.execute(stmt2);
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
			String idxName=createConstraintName(tab,"idx",idxcol.getName().toLowerCase());
			if(idxcol instanceof DbColGeometry){
				//idxstmt="CREATE INDEX "+tab.getName().getName().toLowerCase()+"_"+idxcol.getName().toLowerCase()+"_idx ON "+sqlTabName.toLowerCase()+" USING GIST ( "+idxcol.getName().toLowerCase()+" )";
			}else{
				idxstmt="CREATE INDEX "+idxName+" ON "+sqlTabName.toLowerCase()+" ( "+idxcol.getName().toLowerCase()+" )";
			}
			if(idxstmt!=null){
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
		}
		indexColumns=null;
		
	}
	@Override
	public void visitIndex(DbIndex idx) throws IOException {
		// don't add UNQIUE constraint via  ALTER TABLE
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
	static public String getGpkgGeometryTypename(int type)
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
				return "GEOMCOLLECTION";
			case DbColGeometry.CIRCULARSTRING:
				return "CIRCULARSTRING";
			case DbColGeometry.COMPOUNDCURVE:
				return "COMPOUNDCURVE";
			case DbColGeometry.CURVEPOLYGON:
				return "CURVEPOLYGON";
			case DbColGeometry.MULTICURVE:
				return "MULTICURVE";
			case DbColGeometry.MULTISURFACE:
				return "MULTISURFACE";
			default:
				throw new IllegalArgumentException();
		 }
	}
}
