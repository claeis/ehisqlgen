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
package ch.ehi.sqlgen.generator_impl.ms_mdb;

import ch.ehi.sqlgen.generator.Generator;
import ch.ehi.sqlgen.generator.SqlConfiguration;
import ch.ehi.sqlgen.generator.TextFileUtility;
import ch.ehi.sqlgen.repository.*;

import java.io.File;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author ce
 */
public class GeneratorMdb implements Generator {
	private BufferedWriter out=null;
	private TextFileUtility fmt=null;
	private boolean createSegments=false;
	public void visitSchemaBegin(ch.ehi.basics.settings.Settings config,DbSchema schema)
	throws java.io.IOException
	{
		fmt=new TextFileUtility();
		// general setup
		File sqlRoot=null;
		sqlRoot=new File(config.getValue(SqlConfiguration.SQL_PATH));
		sqlRoot.mkdirs();
		
		// script file setup
		File genSqlFile;
		genSqlFile=new File(sqlRoot,schema.getName()+".bas");
		out = new BufferedWriter(new java.io.FileWriter(genSqlFile));
		// include file header
		String header=config.getValue(config.getValue(SqlConfiguration.SQL_HEADER));
		if(header!=null){
			java.io.InputStream template = new java.io.FileInputStream(header);
			copyStream(out, template);
			template.close();		
			//out.write(fmt.getIndent()+linecmt()+"Copyright (c) 2004, Eisenhut Informatik AG"+fmt.newline());
			//out.write(fmt.getIndent()+linecmt()+"All rights reserved."+fmt.newline());
		}
		out.write(fmt.newline());
		out.write(fmt.getIndent()+linecmt()+"requires Microsoft DAO 3.6 Object Library"+fmt.newline());
		out.write(fmt.newline());
		out.write(fmt.getIndent()+"Option Explicit"+fmt.newline());
		out.write(fmt.newline());
		if(schema.sizeTable()>30){
			createSegments=true;
		}else{
			createSegments=false;
		}

	}
	public void visitSchemaEnd(DbSchema schema)
	throws java.io.IOException
	{
		
		// include helper functions
		java.io.InputStream template = getClass().getResourceAsStream(ch.ehi.basics.i18n.ResourceBundle.class2packagePath(GeneratorMdb.class)+"/UtilsMSAccess.bas");
		copyStream(out, template);		
		template.close();
		out.close();
		out=null;
	}
	private static void copyStream(java.io.BufferedWriter out, java.io.InputStream in) throws IOException {
		byte[] bt = new byte[1024];
		int i;
		while((i=in.read(bt)) != -1)
					{
						out.write(new String(bt,0,i));
					}
	}
	private java.util.List segment; // list of lines
	private int tablec;
	/** name of current segment
	 */
	private String segName;
	private void startSegment(){ 
		segment=new java.util.ArrayList(); // list of lines
		tablec=0;
		segName=null;
	}
	public void visit1Begin()
		throws java.io.IOException
	{
		startSegment();
	}
	public void visit1End()
		throws java.io.IOException
	{
		if(segment.size()>0){
			fmt.dec_ind();
			out.write(fmt.getIndent()+"End Sub"+fmt.newline());
		}
		out.write(fmt.newline());
		out.write(fmt.getIndent()+"Sub dbCreateP1(database As database)"+fmt.newline());
		fmt.inc_ind();
		java.util.Iterator linei=segment.iterator();
		while(linei.hasNext()){
		  String line=(String)linei.next();
		  out.write(fmt.getIndent()+line+fmt.newline());
		}
		fmt.dec_ind();
		out.write(fmt.getIndent()+"End Sub"+fmt.newline());
	}
	public void visit2Begin()
		throws java.io.IOException
	{
		startSegment();
	}
	public void visit2End()
		throws java.io.IOException
	{
		if(segment.size()>0){
			fmt.dec_ind();
			out.write(fmt.getIndent()+"End Sub"+fmt.newline());
		}
		out.write(fmt.newline());
		out.write(fmt.getIndent()+"Sub dbCreateP2(database As database)"+fmt.newline());
		fmt.inc_ind();
		java.util.Iterator linei=segment.iterator();
		while(linei.hasNext()){
		  String line=(String)linei.next();
		  out.write(fmt.getIndent()+line+fmt.newline());
		}
		fmt.dec_ind();
		out.write(fmt.getIndent()+"End Sub"+fmt.newline());
	}
	public void visit1TableBegin(DbTable table)
	throws java.io.IOException
	{
		if(tablec==0){
			// not first segment
			if(segment.size()>0){
				// terminate last
				fmt.dec_ind();
				out.write(fmt.getIndent()+"End Sub"+fmt.newline());
			}
			segName=Integer.toString(segment.size());
			out.write(fmt.getIndent()+"Sub dbCreateP1_"+segName+"(database As database)"+fmt.newline());
			fmt.inc_ind();
			out.write(fmt.getIndent()+"Dim tableDef As TableDef"+fmt.newline());
			out.write(fmt.getIndent()+"Dim index As Index"+fmt.newline());
			out.write(fmt.getIndent()+"Dim field As Field"+fmt.newline());
			out.write(fmt.getIndent()+"Dim rskey As Recordset"+fmt.newline());
			out.write(fmt.getIndent()+"Dim createTable As Boolean"+fmt.newline());
			out.write(fmt.newline());
			out.write(fmt.getIndent()+"Set rskey = database.OpenRecordset(\"T_Key_Object\", dbOpenTable)"+fmt.newline());
			out.write(fmt.getIndent()+"rskey.index = \"PrimaryKey\""+fmt.newline());
			out.write(fmt.newline());
			String line="dbCreateP1_"+segName+" database";
			segment.add(line);
		}
		out.write(fmt.newline());
		out.write(fmt.getIndent()+linecmt()+table.getIliName()+fmt.newline());
		out.write(fmt.getIndent()+"createTable = not existsTable(database,\""+table.getName()+"\")"+fmt.newline());
		out.write(fmt.getIndent()+"if createTable then"+fmt.newline());
		fmt.inc_ind();
		out.write(fmt.getIndent()+"Set tableDef = database.CreateTableDef(\""+table.getName()+"\")"+fmt.newline());
		fmt.dec_ind();
		out.write(fmt.getIndent()+"else"+fmt.newline());
		fmt.inc_ind();
		out.write(fmt.getIndent()+"Set tableDef = database.TableDefs(\""+table.getName()+"\")"+fmt.newline());
		fmt.dec_ind();
		out.write(fmt.getIndent()+"end if"+fmt.newline());
		fmt.inc_ind();
	}
	public void visit1TableEnd( DbTable table)
	throws java.io.IOException
	{
		if(table.isRequiresSequence()){
			out.write(fmt.newline());
			out.write(fmt.getIndent()+"addSequence rskey, tableDef.Name"+fmt.newline());
		}
		fmt.dec_ind();
		out.write(fmt.newline());
		out.write(fmt.getIndent()+"if createTable then"+fmt.newline());
		fmt.inc_ind();
		out.write(fmt.getIndent()+"database.TableDefs.Append tableDef"+fmt.newline());
		fmt.dec_ind();
		out.write(fmt.getIndent()+"end if"+fmt.newline());
		tablec++;
		if(tablec==30){
			tablec=0;
		}
	}
	public void visit2TableBegin(DbTable table)
	throws java.io.IOException
	{
		if(tablec==0){
			// not first segment
			if(segment.size()>0){
				// terminate last
				fmt.dec_ind();
				out.write(fmt.getIndent()+"End Sub"+fmt.newline());
			}
			segName=Integer.toString(segment.size());
			out.write(fmt.getIndent()+"Sub dbCreateP2_"+segName+"(database As database)"+fmt.newline());
			fmt.inc_ind();
			out.write(fmt.getIndent()+"Dim tableDef As TableDef"+fmt.newline());
			out.write(fmt.getIndent()+"Dim index As Index"+fmt.newline());
			out.write(fmt.getIndent()+"Dim field As Field"+fmt.newline());
			out.write(fmt.getIndent()+"Dim relation As Relation"+fmt.newline());
			out.write(fmt.getIndent()+"Dim rsnls As Recordset"+fmt.newline());
			out.write(fmt.getIndent()+"Dim rsTrsl As Recordset"+fmt.newline());
			out.write(fmt.getIndent()+"Dim rsenum As Recordset"+fmt.newline());
			out.write(fmt.getIndent()+"Dim rskey As Recordset"+fmt.newline());
			out.write(fmt.getIndent()+"Set rskey = database.OpenRecordset(\"T_Key_Object\")"+fmt.newline());
			out.write(fmt.getIndent()+"rskey.index = \"PrimaryKey\""+fmt.newline());
			out.write(fmt.newline());
			String line="dbCreateP2_"+segName+" database";
			segment.add(line);
		}
		out.write(fmt.newline());
		out.write(fmt.getIndent()+linecmt()+table.getIliName()+fmt.newline());
		out.write(fmt.getIndent()+"Set tableDef = database.TableDefs(\""+table.getName()+"\")"+fmt.newline());
		fmt.inc_ind();
	}
	public void visit2TableEnd(DbTable table)
	throws java.io.IOException
	{
		fmt.dec_ind();
		tablec++;
		if(tablec==30){
			tablec=0;
		}
	}
	public void visitColumn(DbColumn column)
	throws java.io.IOException
	{
		String type="";
		String size="";
		String notSupported=null;
		if(column instanceof DbColBoolean){
			type="dbBoolean";
			size="";
		}else if(column instanceof DbColDateTime){
			type="dbDate";
			size="";
		}else if(column instanceof DbColDecimal){
			type="dbDouble";
			size="";
		}else if(column instanceof DbColGeometry){
			type="dbBinary";
			size="";
		}else if(column instanceof DbColId){
			type="dbLong";
			size="";
		}else if(column instanceof DbColNumber){
			type="dbLong";
			size="";
		}else if(column instanceof DbColVarchar){
			int colsize=((DbColVarchar)column).getSize();
			if(colsize>254){
				type="dbMemo";
			}else{
				type="dbText";
			}
			size=", "+Integer.toString(colsize);
		}else{
			type="dbText";
			size=", 20";
			notSupported="NOTSUPPORTED: "+column.getClass().getName();
		}
		out.write(fmt.newline());
		String scriptCmt=column.getScriptComment();
		if(scriptCmt!=null){
			out.write(fmt.getIndent()+linecmt()+scriptCmt+fmt.newline());
		}
		out.write(fmt.getIndent()+"if not existsField(tableDef,\""+column.getName()+"\") then"+fmt.newline());
		fmt.inc_ind();
		if(notSupported!=null){
			out.write(fmt.getIndent()+linecmt()+notSupported+fmt.newline());
		}
		out.write(fmt.getIndent()+"Set field = tableDef.CreateField(\""+column.getName()+"\", "+type+size+")"+fmt.newline());
		if(column.isNotNull()){
			out.write(fmt.getIndent()+"field.Required = True"+fmt.newline());
		}
		out.write(fmt.getIndent()+"tableDef.Fields.Append field"+fmt.newline());
		fmt.dec_ind();
		out.write(fmt.getIndent()+"end if"+fmt.newline());

	 	if((column instanceof DbColId) && ((DbColId)column).isPrimaryKey()){
			out.write(fmt.getIndent()+"if not existsindex(tableDef,\"PrimaryKey\") then"+fmt.newline());
			fmt.inc_ind();
			out.write(fmt.getIndent()+"Set index = tableDef.CreateIndex(\"PrimaryKey\")"+fmt.newline());
			out.write(fmt.getIndent()+"Set field = index.CreateField(\""+column.getName()+"\")"+fmt.newline());
			out.write(fmt.getIndent()+"index.Fields.Append field"+fmt.newline());
			out.write(fmt.getIndent()+"index.Primary = True"+fmt.newline());
			out.write(fmt.getIndent()+"tableDef.Indexes.Append index"+fmt.newline());
			fmt.dec_ind();
			out.write(fmt.getIndent()+"end if"+fmt.newline());
	 	}

	}
	public void visitTableBeginColumn(DbTable table)
	throws java.io.IOException
	{
	}
	public void visitTableEndColumn(DbTable table)
	throws java.io.IOException
	{
	}
	public void visitIndex(DbIndex idx)
	throws java.io.IOException
	{
		// Set tableDef = database.TableDefs("T_NLS")
		out.write(fmt.getIndent()+"if not existsindex(tableDef,\""+idx.getName()+"\") then"+fmt.newline());
		fmt.inc_ind();
		out.write(fmt.getIndent()+"Set index = tableDef.CreateIndex(\""+idx.getName()+"\")"+fmt.newline());
		java.util.Iterator attri=idx.iteratorAttr();
		while(attri.hasNext()){
			DbColumn attr=(DbColumn)attri.next();
			out.write(fmt.getIndent()+"Set field = index.CreateField(\""+attr.getName()+"\")"+fmt.newline());
			out.write(fmt.getIndent()+"index.Fields.Append field"+fmt.newline());
		}
		if(idx.isPrimary()){
			out.write(fmt.getIndent()+"index.Primary = True"+fmt.newline());
		}else if(idx.isUnique()){
			out.write(fmt.getIndent()+"index.Unique = True"+fmt.newline());
		}
		out.write(fmt.getIndent()+"tableDef.Indexes.Append index"+fmt.newline());
		fmt.dec_ind();
		out.write(fmt.getIndent()+"end if"+fmt.newline());

	}
	public void visitTableBeginIndex(DbTable table)
	throws java.io.IOException
	{
	}
	public void visitTableEndIndex(DbTable table)
	throws java.io.IOException
	{
	}
	private DbTable relTable=null;
	public void visitConstraint(DbConstraint cnstr)
	throws java.io.IOException
	{
		out.write(fmt.getIndent()+"if not existsrelation(database,\""+cnstr.getName()+"\") then"+fmt.newline());
		fmt.inc_ind();
		out.write(fmt.getIndent()+"Set relation = database.CreateRelation(\""+cnstr.getName()+"\", \""+relTable.getName()+"\", \""+cnstr.getFkTable()+"\")"+fmt.newline());
		out.write(fmt.getIndent()+"Set field = relation.CreateField(\""+cnstr.getPkAttr()+"\")"+fmt.newline());
		out.write(fmt.getIndent()+"field.ForeignName = \""+cnstr.getFkAttr()+"\""+fmt.newline());
		out.write(fmt.getIndent()+"relation.Fields.Append field"+fmt.newline());
		if(cnstr.getAction()==DbConstraint.DELETE_CASCADE){
			out.write(fmt.getIndent()+"relation.Attributes = dbRelationDontEnforce"+fmt.newline());
		}else if(cnstr.getAction()==DbConstraint.DELETE_PREVENT){
			out.write(fmt.getIndent()+"relation.Attributes = dbRelationDontEnforce"+fmt.newline());
		}else if(cnstr.getAction()==DbConstraint.DELETE_SET_NULL){
			out.write(fmt.getIndent()+"relation.Attributes = dbRelationDontEnforce"+fmt.newline());
		}else if(cnstr.getAction()==DbConstraint.DONT_ENFORCE){
			out.write(fmt.getIndent()+"relation.Attributes = dbRelationDontEnforce"+fmt.newline());
		}
		out.write(fmt.getIndent()+"database.Relations.Append relation"+fmt.newline());
		fmt.dec_ind();
		out.write(fmt.getIndent()+"end if"+fmt.newline());
	}
	public void visitTableBeginConstraint(DbTable table)
	throws java.io.IOException
	{
		relTable=table;
	}
	public void visitTableEndConstraint(DbTable table)
	throws java.io.IOException
	{
		relTable=null;
	}
	private DbTable enumTable=null;
	public void visitEnumEle(DbEnumEle ele)
	throws java.io.IOException
	{
		String eleName=ele.getIliCode();
		out.write(fmt.getIndent()+"dbAddEnumEle rskey, rsenum, rsnls, rstrsl, \"de\", \""+eleName+"\", \""+eleName+"\", "+ele.getSequence()+fmt.newline());

	}
	public void visitTableBeginEnumEle(DbTable table)
	throws java.io.IOException
	{
		if(table.sizeEnumEle()>0){
			enumTable=table;
			out.write(fmt.getIndent()+"Set rsnls = database.OpenRecordset(\"T_NLS\", dbOpenTable)"+fmt.newline());
			out.write(fmt.getIndent()+"Set rsTrsl = database.OpenRecordset(\"T_Translation\", dbOpenTable)"+fmt.newline());
			out.write(fmt.getIndent()+"Set rsenum = database.OpenRecordset(\""+table.getName()+"\", dbOpenTable)"+fmt.newline());
		}
	}
	public void visitTableEndEnumEle(DbTable table)
	throws java.io.IOException
	{
		enumTable=null;
	}
	/** get start of a line comment
	 * 
	 */
	private String linecmt(){
		return "' ";
	}

}
