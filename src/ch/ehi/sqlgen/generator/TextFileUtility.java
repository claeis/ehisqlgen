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
 *
 */
public class TextFileUtility {
	
	/** current indentation level
	 *
	 */
	private int ind=0;
	public int getInd(){return ind;}
	/** get indent characters
	 * @return indentation characters
	 */
	public String getIndent()
	{
	  return ch.ehi.basics.tools.StringUtility.STRING(ind*2,' ');
	}

	/** increment indentation level
	 *
	 */
	public void inc_ind()
	{
	  ind=ind+1;
	}

	/** decrement indentation level
	 *
	 */
	public void dec_ind()
	{
	  ind=ind-1;
	}
	/** current line seperator
	 *
	 */
	static private String nl=null;

	/** current line number
	 *
	 */
	private int lineNumber;

	/** get the new line seperator
	 *
	 */
	public String newline()
	{
	  if(nl==null)nl=System.getProperty("line.separator");
	  lineNumber++;
	  return nl;
	}
	
}
