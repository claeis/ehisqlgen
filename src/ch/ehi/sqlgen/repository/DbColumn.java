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

// -beg- preserve=no 40503C4902EC package "DbColumn"
package ch.ehi.sqlgen.repository;

import java.util.HashMap;

public class DbColumn
{
	private String comment=null;
  private String name;

  /** get current value of name
   *  @see #setName
   */
  public  String getName()
  {
    return name;
  }

  /** set current value of name
   *  @see #getName
   */
  public  void setName(String value1)
  {
    if(name != value1){
      name = value1;
      
    }
  }

  private boolean notNull = false;

  /** get current value of notNull
   *  @see #setNotNull
   */
  public  boolean isNotNull()
  {
    return notNull;
  }

  /** set current value of notNull
   *  @see #isNotNull
   */
  public  void setNotNull(boolean value1)
  {
    if(notNull != value1){
      notNull = value1;
      
    }
  }

  private String scriptComment;

  /** get current value of scriptComment
   *  @see #setScriptComment
   */
  public  String getScriptComment()
  {
    return scriptComment;
  }

  /** set current value of scriptComment
   *  @see #getScriptComment
   */
  public  void setScriptComment(String value1)
  {
    if(scriptComment != value1){
      scriptComment = value1;
      
    }
  }

  // declare/define something only in the code
  // please fill in/modify the following section
	private HashMap customValues=new HashMap();
	/** gets a custom property value. Used to add driver or db specific settings.
	 * @param name of property.
	 * @return value or null. Never returns an empty String.
	 */
	public String getCustomValue(String name) {
		String value=(String)customValues.get(name);
		return ch.ehi.basics.tools.StringUtility.purge(value);
	}
	public void setCustomValue(String name,String value) {
		value=ch.ehi.basics.tools.StringUtility.purge(value);
		if(value==null){
			customValues.remove(name);
		}else{
			customValues.put(name, value);
		}
	}

	  private boolean primaryKey = false;
	  public  boolean isPrimaryKey()
	  {
	    return primaryKey;
	  }
	  public  void setPrimaryKey(boolean value1)
	  {
	    if(primaryKey != value1){
	      primaryKey = value1;
	      
	    }
	  }

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getComment() {
		return comment;
	}
	
}

