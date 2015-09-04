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

// -beg- preserve=no 40506A9801BB package "DbConstraint"
package ch.ehi.sqlgen.repository;
// -end- 40506A9801BB package "DbConstraint"

// -beg- preserve=no 40506A9801BB autoimport "DbConstraint"

// -end- 40506A9801BB autoimport "DbConstraint"

// import declarations
// please fill in/modify the following section
// -beg- preserve=no 40506A9801BB import "DbConstraint"

// -end- 40506A9801BB import "DbConstraint"

public class DbConstraint
{
  // declare/define something only in the code
  // please fill in/modify the following section
  // -beg- preserve=yes 40506A9801BB detail_begin "DbConstraint"
	public static int DELETE_CASCADE=1;
	public static int DELETE_SET_NULL=2;
	public static int DELETE_PREVENT=3;
	public static int DONT_ENFORCE=4;
	
  // -end- 40506A9801BB detail_begin "DbConstraint"

  // -beg- preserve=no 405087F302B2 var40506A9801BB "pkAttr"
  private String pkAttr;
  // -end- 405087F302B2 var40506A9801BB "pkAttr"

  /** get current value of pkAttr
   *  @see #setPkAttr
   */
  // -beg- preserve=no 405087F302B2 get_head40506A9801BB "pkAttr"
  public  String getPkAttr()
  // -end- 405087F302B2 get_head40506A9801BB "pkAttr"
  {
    // -beg- preserve=no 405087F302B2 get_body40506A9801BB "pkAttr"
    return pkAttr;
    // -end- 405087F302B2 get_body40506A9801BB "pkAttr"
  }

  /** set current value of pkAttr
   *  @see #getPkAttr
   */
  // -beg- preserve=no 405087F302B2 set_head40506A9801BB "pkAttr"
  public  void setPkAttr(String value1)
  // -end- 405087F302B2 set_head40506A9801BB "pkAttr"
  {
    // -beg- preserve=no 405087F302B2 set_body40506A9801BB "pkAttr"
    if(pkAttr != value1){
      pkAttr = value1;
      
    }
    // -end- 405087F302B2 set_body40506A9801BB "pkAttr"
  }

  // -beg- preserve=no 4050881403A0 var40506A9801BB "fkTable"
  private String fkTable;
  // -end- 4050881403A0 var40506A9801BB "fkTable"

  /** get current value of fkTable
   *  @see #setFkTable
   */
  // -beg- preserve=no 4050881403A0 get_head40506A9801BB "fkTable"
  public  String getFkTable()
  // -end- 4050881403A0 get_head40506A9801BB "fkTable"
  {
    // -beg- preserve=no 4050881403A0 get_body40506A9801BB "fkTable"
    return fkTable;
    // -end- 4050881403A0 get_body40506A9801BB "fkTable"
  }

  /** set current value of fkTable
   *  @see #getFkTable
   */
  // -beg- preserve=no 4050881403A0 set_head40506A9801BB "fkTable"
  public  void setFkTable(String value1)
  // -end- 4050881403A0 set_head40506A9801BB "fkTable"
  {
    // -beg- preserve=no 4050881403A0 set_body40506A9801BB "fkTable"
    if(fkTable != value1){
      fkTable = value1;
      
    }
    // -end- 4050881403A0 set_body40506A9801BB "fkTable"
  }

  // -beg- preserve=no 4050881C00BC var40506A9801BB "fkAttr"
  private String fkAttr;
  // -end- 4050881C00BC var40506A9801BB "fkAttr"

  /** get current value of fkAttr
   *  @see #setFkAttr
   */
  // -beg- preserve=no 4050881C00BC get_head40506A9801BB "fkAttr"
  public  String getFkAttr()
  // -end- 4050881C00BC get_head40506A9801BB "fkAttr"
  {
    // -beg- preserve=no 4050881C00BC get_body40506A9801BB "fkAttr"
    return fkAttr;
    // -end- 4050881C00BC get_body40506A9801BB "fkAttr"
  }

  /** set current value of fkAttr
   *  @see #getFkAttr
   */
  // -beg- preserve=no 4050881C00BC set_head40506A9801BB "fkAttr"
  public  void setFkAttr(String value1)
  // -end- 4050881C00BC set_head40506A9801BB "fkAttr"
  {
    // -beg- preserve=no 4050881C00BC set_body40506A9801BB "fkAttr"
    if(fkAttr != value1){
      fkAttr = value1;
      
    }
    // -end- 4050881C00BC set_body40506A9801BB "fkAttr"
  }

  // -beg- preserve=no 405088490071 var40506A9801BB "action"
  private int action;
  // -end- 405088490071 var40506A9801BB "action"

  /** get current value of action
   *  @see #setAction
   */
  // -beg- preserve=no 405088490071 get_head40506A9801BB "action"
  public  int getAction()
  // -end- 405088490071 get_head40506A9801BB "action"
  {
    // -beg- preserve=no 405088490071 get_body40506A9801BB "action"
    return action;
    // -end- 405088490071 get_body40506A9801BB "action"
  }

  /** set current value of action
   *  @see #getAction
   */
  // -beg- preserve=no 405088490071 set_head40506A9801BB "action"
  public  void setAction(int value1)
  // -end- 405088490071 set_head40506A9801BB "action"
  {
    // -beg- preserve=no 405088490071 set_body40506A9801BB "action"
    if(action != value1){
      action = value1;
      
    }
    // -end- 405088490071 set_body40506A9801BB "action"
  }

  // -beg- preserve=no 405088D200B4 var40506A9801BB "name"
  private String name;
  // -end- 405088D200B4 var40506A9801BB "name"

  /** get current value of name
   *  name of constraint
   *  @see #setName
   */
  // -beg- preserve=no 405088D200B4 get_head40506A9801BB "name"
  public  String getName()
  // -end- 405088D200B4 get_head40506A9801BB "name"
  {
    // -beg- preserve=no 405088D200B4 get_body40506A9801BB "name"
    return name;
    // -end- 405088D200B4 get_body40506A9801BB "name"
  }

  /** set current value of name
   *  @see #getName
   */
  // -beg- preserve=no 405088D200B4 set_head40506A9801BB "name"
  public  void setName(String value1)
  // -end- 405088D200B4 set_head40506A9801BB "name"
  {
    // -beg- preserve=no 405088D200B4 set_body40506A9801BB "name"
    if(name != value1){
      name = value1;
      
    }
    // -end- 405088D200B4 set_body40506A9801BB "name"
  }

  // declare/define something only in the code
  // please fill in/modify the following section
  // -beg- preserve=no 40506A9801BB detail_end "DbConstraint"

  // -end- 40506A9801BB detail_end "DbConstraint"

}

