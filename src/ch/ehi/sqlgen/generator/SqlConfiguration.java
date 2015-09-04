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

/**
 * @author ce
 * @version $Revision: 1.1.1.1 $ $Date: 2006/04/27 06:56:43 $
 */
public interface SqlConfiguration {
	public final String PREFIX="ch.ehi.sqlgen";
	public final String SQL_PATH=PREFIX+".sqlPath"; // String $base+"/sql"
	public final String SQL_HEADER=PREFIX+".sqlHeader"; // String
	public final String JDBC_CONNECTION=PREFIX+".jdbcConnection"; // java.sql.Connection
	public final String BASE=PREFIX+".base"; // String "c:/tmp/ce/egris"
	public final String PACKAGE_PREFIX=PREFIX+".packagePrefix"; // String "org.umleditor.repository"
	public final String CREATE_GEOM_INDEX=PREFIX+".createGeomIndex"; // True/False
}