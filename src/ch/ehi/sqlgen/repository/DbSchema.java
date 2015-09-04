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

// -beg- preserve=no 40503CC70030 package "DbSchema"
package ch.ehi.sqlgen.repository;
// -end- 40503CC70030 package "DbSchema"

// -beg- preserve=no 40503CC70030 autoimport "DbSchema"
import ch.ehi.sqlgen.repository.DbTable;
// -end- 40503CC70030 autoimport "DbSchema"

// import declarations
// please fill in/modify the following section
// -beg- preserve=no 40503CC70030 import "DbSchema"

// -end- 40503CC70030 import "DbSchema"

public class DbSchema
{
  // declare/define something only in the code
  // please fill in/modify the following section
  // -beg- preserve=no 40503CC70030 detail_begin "DbSchema"

  // -end- 40503CC70030 detail_begin "DbSchema"

  // -beg- preserve=no 40503CD0010F code40503CC70030 "table"
  private java.util.Set table = new java.util.HashSet();
  // -end- 40503CD0010F code40503CC70030 "table"

  /** add a Table.
   *  
   *  @see #removeTable
   *  @see #containsTable
   *  @see #iteratorTable
   *  @see #clearTable
   *  @see #sizeTable
   */
  // -beg- preserve=no 40503CD0010F add_head40503CC70030 "DbSchema::addTable"
  public void addTable(DbTable table1)
  // -end- 40503CD0010F add_head40503CC70030 "DbSchema::addTable"
  {
    // -beg- preserve=no 40503CD0010F add_body40503CC70030 "DbSchema::addTable"
    table.add(table1);
    return;
    // -end- 40503CD0010F add_body40503CC70030 "DbSchema::addTable"
  }

  /** disconnect a Table.
   *  @see #addTable
   */
  // -beg- preserve=no 40503CD0010F remove_head40503CC70030 "DbSchema::removeTable"
  public DbTable removeTable(DbTable table1)
  // -end- 40503CD0010F remove_head40503CC70030 "DbSchema::removeTable"
  {
    // -beg- preserve=no 40503CD0010F remove_body40503CC70030 "DbSchema::removeTable"
    DbTable ret=null;
    if(table1==null || !table.contains(table1)){
      throw new java.lang.IllegalArgumentException("cannot remove null or unknown object");
    }
    ret = table1;
    table.remove(table1);
    
    return ret;
    // -end- 40503CD0010F remove_body40503CC70030 "DbSchema::removeTable"
  }

  /** tests if a given Table is connected.
   *  @see #addTable
   */
  // -beg- preserve=no 40503CD0010F test_head40503CC70030 "DbSchema::containsTable"
  public boolean containsTable(DbTable table1)
  // -end- 40503CD0010F test_head40503CC70030 "DbSchema::containsTable"
  {
    // -beg- preserve=no 40503CD0010F test_body40503CC70030 "DbSchema::containsTable"
    return table.contains(table1);
    // -end- 40503CD0010F test_body40503CC70030 "DbSchema::containsTable"
  }

  /** used to enumerate all connected Tables.
   *  @see #addTable
   */
  // -beg- preserve=no 40503CD0010F get_all_head40503CC70030 "DbSchema::iteratorTable"
  public java.util.Iterator iteratorTable()
  // -end- 40503CD0010F get_all_head40503CC70030 "DbSchema::iteratorTable"
  {
    // -beg- preserve=no 40503CD0010F get_all_body40503CC70030 "DbSchema::iteratorTable"
    return table.iterator();
    // -end- 40503CD0010F get_all_body40503CC70030 "DbSchema::iteratorTable"
  }

  /** disconnect all Tables.
   *  @see #addTable
   */
  // -beg- preserve=no 40503CD0010F remove_all_head40503CC70030 "DbSchema::clearTable"
  public void clearTable()
  // -end- 40503CD0010F remove_all_head40503CC70030 "DbSchema::clearTable"
  {
    // -beg- preserve=no 40503CD0010F remove_all_body40503CC70030 "DbSchema::clearTable"
    if(sizeTable()>0){
      table.clear();
      
    }
    // -end- 40503CD0010F remove_all_body40503CC70030 "DbSchema::clearTable"
  }

  /** returns the number of Tables.
   *  @see #addTable
   */
  // -beg- preserve=no 40503CD0010F size_head40503CC70030 "DbSchema::sizeTable"
  public int sizeTable()
  // -end- 40503CD0010F size_head40503CC70030 "DbSchema::sizeTable"
  {
    // -beg- preserve=no 40503CD0010F size_body40503CC70030 "DbSchema::sizeTable"
    return table.size();
    // -end- 40503CD0010F size_body40503CC70030 "DbSchema::sizeTable"
  }

  // -beg- preserve=no 405041C10064 var40503CC70030 "name"
  private String name;
  // -end- 405041C10064 var40503CC70030 "name"

  /** get current value of name
   *  @see #setName
   */
  // -beg- preserve=no 405041C10064 get_head40503CC70030 "name"
  public  String getName()
  // -end- 405041C10064 get_head40503CC70030 "name"
  {
    // -beg- preserve=no 405041C10064 get_body40503CC70030 "name"
    return name;
    // -end- 405041C10064 get_body40503CC70030 "name"
  }

  /** set current value of name
   *  @see #getName
   */
  // -beg- preserve=no 405041C10064 set_head40503CC70030 "name"
  public  void setName(String value1)
  // -end- 405041C10064 set_head40503CC70030 "name"
  {
    // -beg- preserve=no 405041C10064 set_body40503CC70030 "name"
    if(name != value1){
      name = value1;
      
    }
    // -end- 405041C10064 set_body40503CC70030 "name"
  }

  // declare/define something only in the code
  // please fill in/modify the following section
  // -beg- preserve=no 40503CC70030 detail_end "DbSchema"

  // -end- 40503CC70030 detail_end "DbSchema"

}

