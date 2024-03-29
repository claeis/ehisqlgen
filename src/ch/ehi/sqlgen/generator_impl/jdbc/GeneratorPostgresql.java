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

import java.util.ArrayList;
import java.util.Iterator;
import java.io.IOException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.sql.SQLException;

import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.basics.settings.Settings;
import ch.ehi.sqlgen.DbUtility;
import ch.ehi.sqlgen.generator.SqlConfiguration;
import ch.ehi.sqlgen.repository.*;

/**
 * @author ce
 * @version $Revision: 1.1 $ $Date: 2007/01/27 09:26:12 $
 */
public class GeneratorPostgresql extends GeneratorJdbc {
	/*
	The standard names for indexes in PostgreSQL are:

	{tablename}_{columnname(s)}_{suffix}

	where the suffix is one of the following:

	    pkey for a Primary Key constraint
	    key for a Unique constraint
	    excl for an Exclusion constraint
	    idx for any other kind of index
	    fkey for a Foreign key
	    check for a Check constraint

	Standard suffix for sequences is

	    seq for all sequences
	*/

	private boolean createGeomIdx=false;
	private ArrayList<DbColumn> indexColumns=null;
    private DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    private DateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private DateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss.SSS");
	
	@Override
	public void visitSchemaBegin(Settings config, DbSchema schema)
			throws IOException {
		super.visitSchemaBegin(config, schema);
		if("True".equals(config.getValue(SqlConfiguration.CREATE_GEOM_INDEX))){
			createGeomIdx=true;
		}
	}

	@Override
	public void visitColumn(DbTable dbTab,DbColumn column) throws IOException {
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
		    DbColGeometry geo=(DbColGeometry)column;
		    if(!geo.getSrsAuth().equals("EPSG")) {
		        throw new IllegalArgumentException("unexpected SrsAuth <"+geo.getSrsAuth()+">");
		    }
            type="geometry("+getPostgisType(geo.getType())+(geo.getDimension()==3?"Z":"")+","+geo.getSrsId()+")";
		}else if(column instanceof DbColId){
			type="bigint";
		}else if(column instanceof DbColUuid){
			type="uuid";
		}else if(column instanceof DbColNumber){
			DbColNumber col=(DbColNumber)column;
			if(col.getSize()>10 
			        || (col.getMaxValue()!=null && col.getMaxValue()>(long)Integer.MAX_VALUE)
			        || (col.getMinValue()!=null && col.getMinValue()<(long)Integer.MIN_VALUE)) {
	            type="bigint";
			}else {
	            type="integer";
			}
		}else if(column instanceof DbColVarchar){
			int colsize=((DbColVarchar)column).getSize();
			if(colsize==DbColVarchar.UNLIMITED){
				type="text";
			}else{
				type="varchar("+Integer.toString(colsize)+")";
			}
		}else if(column instanceof DbColBlob){
			type="bytea";
		}else if(column instanceof DbColXml){
			type="xml";
        }else if(column instanceof DbColJson){
            type="jsonb";
		}else{
			type="text";
		}
		String array="";
		if(column.getArraySize()!=DbColumn.NOT_AN_ARRAY) {
			if(column.getArraySize()==DbColumn.UNLIMITED_ARRAY) {
				array="ARRAY ";
			}else {
				array="ARRAY["+column.getArraySize()+"] ";
			}
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
			out.write(getIndent()+colSep+name+" "+type+" "+array+isNull+defaultValue+newline());
			colSep=",";
		}
	}
	
	@Override
	public void visit1TableBegin(DbTable tab) throws IOException {
		super.visit1TableBegin(tab);
		indexColumns=new ArrayList<DbColumn>();
	}
	
	@Override
	public void visitTableEndColumn(DbTable tab) throws IOException {
		boolean tableExists=DbUtility.tableExists(conn,tab.getName());
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
			out.write(getIndent()+"CREATE UNIQUE INDEX "+constraintName+" ON "+tableName+" (");
			String sep="";
			for(Iterator attri=idx.iteratorAttr();attri.hasNext();){
				DbColumn attr=(DbColumn)attri.next();
				
				if (attr instanceof DbColGeometry) {
					if (((DbColGeometry) attr).getType() == DbColGeometry.POINT) {
						// only for points?
					}
					out.write(sep+"ST_AsBinary("+attr.getName()+")");
				} else {
					out.write(sep+attr.getName());
				}				
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
	public void visit1TableEnd(DbTable tab) throws IOException {
		String sqlTabName=tab.getName().getName();
		if(tab.getName().getSchema()!=null){
			sqlTabName=tab.getName().getSchema()+"."+sqlTabName;
		}
		boolean tableExists=DbUtility.tableExists(conn,tab.getName());
		super.visit1TableEnd(tab);

		for(DbColumn idxcol:indexColumns){
			
			String idxstmt=null;
			String idxName=createConstraintName(tab,"idx",idxcol.getName().toLowerCase());
			if(idxcol instanceof DbColGeometry){
				idxstmt="CREATE INDEX "+idxName+" ON "+sqlTabName.toLowerCase()+" USING GIST ( "+idxcol.getName().toLowerCase()+" )";
			}else{
				idxstmt="CREATE INDEX "+idxName+" ON "+sqlTabName.toLowerCase()+" ( "+idxcol.getName().toLowerCase()+" )";
			}
			addCreateLine(new Stmt(idxstmt));
			
            if(conn!=null) {
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
		
		String cmt=tab.getComment();
		if(cmt!=null){
			cmt="COMMENT ON TABLE "+sqlTabName+" IS '"+escapeString(cmt)+"'";
			addCreateLine(new Stmt(cmt));
            if(conn!=null) {
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
		
		java.util.Iterator coli=tab.iteratorColumn();
		while(coli.hasNext()){
			DbColumn col=(DbColumn)coli.next();
			cmt=col.getComment();
			if(cmt!=null){
				cmt="COMMENT ON COLUMN "+sqlTabName+"."+col.getName()+" IS '"+escapeString(cmt)+"'";
				addCreateLine(new Stmt(cmt));
	            if(conn!=null) {
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
			case DbColGeometry.MULTISURFACE:
				return "MULTISURFACE";
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
	            if(dbCol.getArraySize()==DbColumn.NOT_AN_ARRAY) {
	                String createstmt=null;
	                String action="";
	                if(dbCol.getOnUpdateAction()!=null){
	                    action=action+" ON UPDATE "+dbCol.getOnUpdateAction();
	                }
	                if(dbCol.getOnDeleteAction()!=null){
	                    action=action+" ON DELETE "+dbCol.getOnDeleteAction();
	                }
	                String constraintName=createConstraintName(dbTab,"fkey",dbCol.getName());
	                //  ALTER TABLE ce.classb1 ADD CONSTRAINT classb1_t_id_fkey FOREIGN KEY ( t_id ) REFERENCES ce.classa1;
	                createstmt="ALTER TABLE "+sqlTabName+" ADD CONSTRAINT "+constraintName+" FOREIGN KEY ( "+dbCol.getName()+" ) REFERENCES "+dbCol.getReferencedTable().getQName()+action+" DEFERRABLE INITIALLY DEFERRED";
	                
	                //  ALTER TABLE ce.classb1 DROP CONSTRAINT classb1_t_id_fkey;
	                String dropstmt=null;
	                dropstmt="ALTER TABLE "+sqlTabName+" DROP CONSTRAINT "+constraintName;

	                addConstraint(dbTab, constraintName,createstmt, dropstmt);
	            }
			}
			if(dbCol instanceof DbColNumber && (((DbColNumber)dbCol).getMinValue()!=null || ((DbColNumber)dbCol).getMaxValue()!=null)){
	            if(dbCol.getArraySize()==DbColumn.NOT_AN_ARRAY) {
	                DbColNumber dbColNum=(DbColNumber)dbCol;
	                String createstmt=null;
	                String action="";
	                if(dbColNum.getMinValue()!=null || dbColNum.getMaxValue()!=null){
	                    if(dbColNum.getMaxValue()==null){
	                        action=">="+dbColNum.getMinValue();
	                    }else if(dbColNum.getMinValue()==null){
	                        action="<="+dbColNum.getMaxValue();
	                    }else{
	                        action="BETWEEN "+dbColNum.getMinValue()+" AND "+dbColNum.getMaxValue();
	                    }
	                }
	                String constraintName=createConstraintName(dbTab,"check",dbCol.getName());
	                //  ALTER TABLE ce.classb1 ADD CONSTRAINT classb1_attr_check CHECK (attr BETWEEN 0 AND 50);
	                createstmt="ALTER TABLE "+sqlTabName+" ADD CONSTRAINT "+constraintName+" CHECK( "+dbCol.getName()+" "+action+")";
	                
	                //  ALTER TABLE ce.classb1 DROP CONSTRAINT classb1_t_id_fkey;
	                String dropstmt=null;
	                dropstmt="ALTER TABLE "+sqlTabName+" DROP CONSTRAINT "+constraintName;

	                addConstraint(dbTab, constraintName,createstmt, dropstmt);
	            }
			}else if(dbCol instanceof DbColDecimal && (((DbColDecimal)dbCol).getMinValue()!=null || ((DbColDecimal)dbCol).getMaxValue()!=null)){
	            if(dbCol.getArraySize()==DbColumn.NOT_AN_ARRAY) {
	                DbColDecimal dbColNum=(DbColDecimal)dbCol;
	                String createstmt=null;
	                String action="";
	                if(dbColNum.getMinValue()!=null || dbColNum.getMaxValue()!=null){
	                    if(dbColNum.getMaxValue()==null){
	                        action=">="+dbColNum.getMinValue();
	                    }else if(dbColNum.getMinValue()==null){
	                        action="<="+dbColNum.getMaxValue();
	                    }else{
	                        action="BETWEEN "+dbColNum.getMinValue()+" AND "+dbColNum.getMaxValue();
	                    }
	                }
	                String constraintName=createConstraintName(dbTab,"check",dbCol.getName());
	                //  ALTER TABLE ce.classb1 ADD CONSTRAINT classb1_attr_check CHECK (attr BETWEEN 0 AND 50);
	                createstmt="ALTER TABLE "+sqlTabName+" ADD CONSTRAINT "+constraintName+" CHECK( "+dbCol.getName()+" "+action+")";
	                
	                //  ALTER TABLE ce.classb1 DROP CONSTRAINT classb1_t_id_fkey;
	                String dropstmt=null;
	                dropstmt="ALTER TABLE "+sqlTabName+" DROP CONSTRAINT "+constraintName;

	                addConstraint(dbTab, constraintName,createstmt, dropstmt);
	            }
            }else if(dbCol instanceof DbColDate && (((DbColDate)dbCol).getMinValue()!=null || ((DbColDate)dbCol).getMaxValue()!=null)){
                if(dbCol.getArraySize()==DbColumn.NOT_AN_ARRAY) {
                    DbColDate dbColNum=(DbColDate)dbCol;
                    String createstmt=null;
                    String action="";
                    if(dbColNum.getMinValue()!=null || dbColNum.getMaxValue()!=null){
                        if(dbColNum.getMaxValue()==null){
                            action=">="+toDateLiteral(dbColNum.getMinValue());
                        }else if(dbColNum.getMinValue()==null){
                            action="<="+toDateLiteral(dbColNum.getMaxValue());
                        }else{
                            action="BETWEEN "+toDateLiteral(dbColNum.getMinValue())+" AND "+toDateLiteral(dbColNum.getMaxValue());
                        }
                    }
                    String constraintName=createConstraintName(dbTab,"check",dbCol.getName());
                    createstmt="ALTER TABLE "+sqlTabName+" ADD CONSTRAINT "+constraintName+" CHECK( "+dbCol.getName()+" "+action+")";
                    
                    String dropstmt=null;
                    dropstmt="ALTER TABLE "+sqlTabName+" DROP CONSTRAINT "+constraintName;
                    addConstraint(dbTab, constraintName,createstmt, dropstmt);
                }
            }else if(dbCol instanceof DbColTime && (((DbColTime)dbCol).getMinValue()!=null || ((DbColTime)dbCol).getMaxValue()!=null)){
                if(dbCol.getArraySize()==DbColumn.NOT_AN_ARRAY) {
                    DbColTime dbColNum=(DbColTime)dbCol;
                    String createstmt=null;
                    String action="";
                    if(dbColNum.getMinValue()!=null || dbColNum.getMaxValue()!=null){
                        if(dbColNum.getMaxValue()==null){
                            action=">="+toTimeLiteral(dbColNum.getMinValue());
                        }else if(dbColNum.getMinValue()==null){
                            action="<="+toTimeLiteral(dbColNum.getMaxValue());
                        }else{
                            action="BETWEEN "+toTimeLiteral(dbColNum.getMinValue())+" AND "+toTimeLiteral(dbColNum.getMaxValue());
                        }
                    }
                    String constraintName=createConstraintName(dbTab,"check",dbCol.getName());
                    createstmt="ALTER TABLE "+sqlTabName+" ADD CONSTRAINT "+constraintName+" CHECK( "+dbCol.getName()+" "+action+")";
                    
                    String dropstmt=null;
                    dropstmt="ALTER TABLE "+sqlTabName+" DROP CONSTRAINT "+constraintName;
                    addConstraint(dbTab, constraintName,createstmt, dropstmt);
                }
            }else if(dbCol instanceof DbColDateTime && (((DbColDateTime)dbCol).getMinValue()!=null || ((DbColDateTime)dbCol).getMaxValue()!=null)){
                if(dbCol.getArraySize()==DbColumn.NOT_AN_ARRAY) {
                    DbColDateTime dbColNum=(DbColDateTime)dbCol;
                    String createstmt=null;
                    String action="";
                    if(dbColNum.getMinValue()!=null || dbColNum.getMaxValue()!=null){
                        if(dbColNum.getMaxValue()==null){
                            action=">="+toDateTimeLiteral(dbColNum.getMinValue());
                        }else if(dbColNum.getMinValue()==null){
                            action="<="+toDateTimeLiteral(dbColNum.getMaxValue());
                        }else{
                            action="BETWEEN "+toDateTimeLiteral(dbColNum.getMinValue())+" AND "+toDateTimeLiteral(dbColNum.getMaxValue());
                        }
                    }
                    String constraintName=createConstraintName(dbTab,"check",dbCol.getName());
                    createstmt="ALTER TABLE "+sqlTabName+" ADD CONSTRAINT "+constraintName+" CHECK( "+dbCol.getName()+" "+action+")";
                    
                    String dropstmt=null;
                    dropstmt="ALTER TABLE "+sqlTabName+" DROP CONSTRAINT "+constraintName;
                    addConstraint(dbTab, constraintName,createstmt, dropstmt);
                }
			}
            if(dbCol instanceof DbColVarchar && dbCol.getArraySize()==DbColumn.NOT_AN_ARRAY) {
                DbColVarchar dbColTxt=(DbColVarchar)dbCol;
                if(dbColTxt.getValueRestriction()!=null){
                    StringBuffer action=new StringBuffer("IN (");
                    String sep="";
                    for(String restrictedValue:dbColTxt.getValueRestriction()){
                        action.append(sep);
                        action.append("'");
                        action.append(escapeString(restrictedValue));
                        action.append("'");
                        sep=",";
                    }
                    action.append(")");
                    String constraintName=createConstraintName(dbTab,"check",dbCol.getName());
                    String createstmt="ALTER TABLE "+sqlTabName+" ADD CONSTRAINT "+constraintName+" CHECK( "+dbCol.getName()+" "+action.toString()+")";

                    String dropstmt=null;
                    dropstmt="ALTER TABLE "+sqlTabName+" DROP CONSTRAINT "+constraintName;

                    addConstraint(dbTab, constraintName,createstmt, dropstmt);
                }else if(DbColVarchar.KIND_NORMALIZED.equals(dbColTxt.getKind()) || dbColTxt.getMinLength()!=null) {
                    
                    String action="";
                    String sep="";
                    if(DbColVarchar.KIND_NORMALIZED.equals(dbColTxt.getKind())){
                        action+=sep+"position('\\n' IN "+dbCol.getName()+")=0 AND position('\\r' IN "+dbCol.getName()+")=0 AND position('\\t' IN "+dbCol.getName()+")=0";
                        sep=" AND ";
                    }
                    if(dbColTxt.getMinLength()!=null){
                        action+=sep+"length(trim("+dbCol.getName()+"))>="+dbColTxt.getMinLength();
                        sep=" AND ";
                    }
                    String constraintName=createConstraintName(dbTab,"check",dbCol.getName());
                    String createstmt="ALTER TABLE "+sqlTabName+" ADD CONSTRAINT "+constraintName+" CHECK( "+action.toString()+")";

                    String dropstmt=null;
                    dropstmt="ALTER TABLE "+sqlTabName+" DROP CONSTRAINT "+constraintName;

                    addConstraint(dbTab, constraintName,createstmt, dropstmt);
                }
                
            }
            if(dbCol instanceof DbColVarchar && ((DbColVarchar)dbCol).getValueRestriction()!=null){
                if(dbCol.getArraySize()==DbColumn.NOT_AN_ARRAY) {
                    DbColVarchar dbColTxt=(DbColVarchar)dbCol;
                }
            }
		}
		for(java.util.Iterator<String> cnstrIt=dbTab.iteratorNativeConstraints();cnstrIt.hasNext();){
		    String constraintName=cnstrIt.next();
		    String action=dbTab.getNativeConstraint(constraintName);
            String createstmt="ALTER TABLE "+sqlTabName+" ADD CONSTRAINT "+constraintName+" "+action;
            
            String dropstmt=null;
            dropstmt="ALTER TABLE "+sqlTabName+" DROP CONSTRAINT "+constraintName;

            addConstraint(dbTab, constraintName,createstmt, dropstmt);
		    
		}
	}
	private String toDateLiteral(java.sql.Date val)
	{
	    return "DATE '"+dateFormatter.format(val)+"'";
	}
    private String toTimeLiteral(java.sql.Time val)
    {
        return "TIME '"+timeFormatter.format(val)+"'";
    }
    private String toDateTimeLiteral(java.sql.Timestamp val)
    {
        return "TIMESTAMP '"+dateTimeFormatter.format(val)+"'";
    }
}
