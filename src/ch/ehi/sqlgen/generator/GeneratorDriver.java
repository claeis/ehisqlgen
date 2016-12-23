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
package ch.ehi.sqlgen.generator;

import java.io.File;
import java.util.Iterator;

import ch.ehi.sqlgen.generator_impl.ms_mdb.GeneratorMdb;
import ch.ehi.sqlgen.repository.*;

/** public interface to the generators.
 * To be used by applications that issue SQL DDL statements.
 * @author ce
 * @version $Revision: 1.1 $ $Date: 2006/05/29 14:18:19 $
 */
public class GeneratorDriver {
	private Generator gen=null;
    
    public GeneratorDriver(Generator generator) {
        super();
        gen = generator;
    }
	public void visitSchema(ch.ehi.basics.settings.Settings config,DbSchema schema /*,Generator gen1*/)
	throws java.io.IOException
	{
		//gen=gen1;
		// setup
		gen.visitSchemaBegin(config,schema);

		gen.visit1Begin();
		// visit tables (1st pass)
		java.util.Iterator tabi=schema.iteratorTable();
		while(tabi.hasNext()){
			DbTable tab=(DbTable)tabi.next();
			visit1Table(tab);
		}
		gen.visit1End();

		gen.visit2Begin();
		// visit tables (2nd pass)
		tabi=schema.iteratorTable();
		while(tabi.hasNext()){
			DbTable tab=(DbTable)tabi.next();
			visit2Table(tab);
		}
		gen.visit2End();
		
		//  cleanup
		gen.visitSchemaEnd(schema);
	}
	
	private void visit1Table(DbTable table)
	throws java.io.IOException
	{
		// setup
		gen.visit1TableBegin(table);
		
		// visit columns
		gen.visitTableBeginColumn(table);
		java.util.Iterator coli=table.iteratorColumn();
		while(coli.hasNext()){
			DbColumn col=(DbColumn)coli.next();
			gen.visitColumn(table,col);
		}
		gen.visitTableEndColumn(table);

		
		//  cleanup
		gen.visit1TableEnd(table);
	}
	
	private void visit2Table(DbTable table)
	throws java.io.IOException
	{
		// setup
		gen.visit2TableBegin(table);

		// visit indices
		gen.visitTableBeginIndex(table);
		java.util.Iterator idxi=table.iteratorIndex();
		while(idxi.hasNext()){
			DbIndex idx=(DbIndex)idxi.next();
			gen.visitIndex(idx);
		}
		gen.visitTableEndIndex(table);

		// visit constraints
		gen.visitTableBeginConstraint(table);
		java.util.Iterator cnstri=table.iteratorConstraint();
		while(cnstri.hasNext()){
			DbConstraint cnstr=(DbConstraint)cnstri.next();
			gen.visitConstraint(cnstr);
		}
		gen.visitTableEndConstraint(table);
		
		// visit enum eles
		gen.visitTableBeginEnumEle(table);
		java.util.Iterator elei=table.iteratorEnumEle();
		while(elei.hasNext()){
			DbEnumEle ele=(DbEnumEle)elei.next();
			gen.visitEnumEle(ele);
		}
		gen.visitTableEndEnumEle(table);
		
		//  cleanup
		gen.visit2TableEnd(table);
	}
}