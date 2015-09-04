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

import ch.ehi.sqlgen.repository.*;

/** common interface of SQL DDL code generators.
 * Do not call directly, use GeneratorDriver instead.
 * @author ce
 * @see ch.ehi.sqlgen.generator.GeneratorDriver
 *
 */
public interface Generator {
	public void visitSchemaBegin(ch.ehi.basics.settings.Settings config,DbSchema schema)
		throws java.io.IOException;
	public void visitSchemaEnd(DbSchema schema)
		throws java.io.IOException;
	public void visit1Begin()
		throws java.io.IOException;
	public void visit1End()
		throws java.io.IOException;
	public void visit2Begin()
		throws java.io.IOException;
	public void visit2End()
		throws java.io.IOException;
	public void visit1TableBegin(DbTable table)
		throws java.io.IOException;
	public void visit1TableEnd(DbTable table)
		throws java.io.IOException;
	public void visit2TableBegin(DbTable table)
		throws java.io.IOException;
	public void visit2TableEnd(DbTable table)
		throws java.io.IOException;
	public void visitColumn(DbColumn column)
		throws java.io.IOException;
	/** called before visitColumn
	 */
	public void visitTableBeginColumn(DbTable table)
		throws java.io.IOException;
	/** called after visitColumn
	 */
	public void visitTableEndColumn(DbTable table)
		throws java.io.IOException;
	public void visitIndex(DbIndex idx)
		throws java.io.IOException;
	/** called before visitIndex
	 */
	public void visitTableBeginIndex(DbTable table)
		throws java.io.IOException;
	/** called after visitIndex
	 */
	public void visitTableEndIndex(DbTable table)
		throws java.io.IOException;
	public void visitConstraint(DbConstraint cnstr)
		throws java.io.IOException;
	/** called before visitConstraint
	 */
	public void visitTableBeginConstraint(DbTable table)
		throws java.io.IOException;
	/** called after visitConstraint
	 */
	public void visitTableEndConstraint(DbTable table)
		throws java.io.IOException;
	public void visitEnumEle(DbEnumEle ele)
		throws java.io.IOException;
	/** called before visitEnumEle
	 */
	public void visitTableBeginEnumEle(DbTable table)
		throws java.io.IOException;
	/** called after visitEnumEle
	 */
	public void visitTableEndEnumEle(DbTable table)
		throws java.io.IOException;
}
