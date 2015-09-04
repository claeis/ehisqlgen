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

// -beg- preserve=no 40508E1C036E package "DbIndex"
package ch.ehi.sqlgen.repository;
// -end- 40508E1C036E package "DbIndex"

// -beg- preserve=no 40508E1C036E autoimport "DbIndex"
import ch.ehi.sqlgen.repository.DbTable;
import ch.ehi.sqlgen.repository.DbColumn;
// -end- 40508E1C036E autoimport "DbIndex"

// import declarations
// please fill in/modify the following section
// -beg- preserve=no 40508E1C036E import "DbIndex"

// -end- 40508E1C036E import "DbIndex"

public class DbIndex
{
  // declare/define something only in the code
  // please fill in/modify the following section
  // -beg- preserve=no 40508E1C036E detail_begin "DbIndex"

  // -end- 40508E1C036E detail_begin "DbIndex"

  // -beg- preserve=no 40508E710081 code40508E1C036E "table"
  private DbTable table;
  // -end- 40508E710081 code40508E1C036E "table"

  /** attaches a Table.
   *  
   *  @see #detachTable
   *  @see #getTable
   *  @see #containsTable
   */
  // -beg- preserve=no 40508E710081 attach_head40508E1C036E "DbIndex::attachTable"
  public void attachTable(DbTable table1)
  // -end- 40508E710081 attach_head40508E1C036E "DbIndex::attachTable"
  {
    // -beg- preserve=no 40508E710081 attach_body40508E1C036E "DbIndex::attachTable"
    if(table!=null) {throw new java.lang.IllegalStateException("already a table attached");}
    if(table1==null) {throw new java.lang.IllegalArgumentException("null may not be attached as table");}
    table = table1;
    table1._linkIndex(this);
    
    return;
    // -end- 40508E710081 attach_body40508E1C036E "DbIndex::attachTable"
  }

  /** disconnect the currently attached Table.
   *  @see #attachTable
   */
  // -beg- preserve=no 40508E710081 detach_head40508E1C036E "DbIndex::detachTable"
  public DbTable detachTable()
  // -end- 40508E710081 detach_head40508E1C036E "DbIndex::detachTable"
  {
    // -beg- preserve=no 40508E710081 detach_body40508E1C036E "DbIndex::detachTable"
    DbTable ret = null;
    if(table!=null){
      table._unlinkIndex(this);
      ret = table;
      table = null;
    }
    
    return ret;
    // -end- 40508E710081 detach_body40508E1C036E "DbIndex::detachTable"
  }

  /** get the currently attached Table.
   *  @see #attachTable
   */
  // -beg- preserve=no 40508E710081 get_head40508E1C036E "DbIndex::getTable"
  public DbTable getTable()
  // -end- 40508E710081 get_head40508E1C036E "DbIndex::getTable"
  {
    // -beg- preserve=no 40508E710081 get_body40508E1C036E "DbIndex::getTable"
    if(table==null) {throw new java.lang.IllegalStateException("no table attached");}
    return table;
    // -end- 40508E710081 get_body40508E1C036E "DbIndex::getTable"
  }

  /** tests if there is a Table attached.
   *  @see #attachTable
   */
  // -beg- preserve=no 40508E710081 test_head40508E1C036E "DbIndex::containsTable"
  public boolean containsTable()
  // -end- 40508E710081 test_head40508E1C036E "DbIndex::containsTable"
  {
    // -beg- preserve=no 40508E710081 test_body40508E1C036E "DbIndex::containsTable"
    return table!=null;
    // -end- 40508E710081 test_body40508E1C036E "DbIndex::containsTable"
  }

  /** DONT USE; link management internal
   */
  // -beg- preserve=no 40508E710081 _link_body40508E1C036E "DbIndex::_linkTable"
  public void _linkTable(DbTable table1)
  {
    table = table1;
    
    return;
  }
  // -end- 40508E710081 _link_body40508E1C036E "DbIndex::_linkTable"

  /** DONT USE; link management internal
   */
  // -beg- preserve=no 40508E710081 _unlink_body40508E1C036E "DbIndex::_unlinkTable"
  public void _unlinkTable(DbTable table1)
  {
    table = null;
    
    return;
  }
  // -end- 40508E710081 _unlink_body40508E1C036E "DbIndex::_unlinkTable"

  // -beg- preserve=no 4090DE8E0220 code40508E1C036E "attr"
  private java.util.Set attr = new java.util.HashSet();
  // -end- 4090DE8E0220 code40508E1C036E "attr"

  /** add a Attr.
   *  
   *  @see #removeAttr
   *  @see #containsAttr
   *  @see #iteratorAttr
   *  @see #clearAttr
   *  @see #sizeAttr
   */
  // -beg- preserve=no 4090DE8E0220 add_head40508E1C036E "DbIndex::addAttr"
  public void addAttr(DbColumn attr1)
  // -end- 4090DE8E0220 add_head40508E1C036E "DbIndex::addAttr"
  {
    // -beg- preserve=no 4090DE8E0220 add_body40508E1C036E "DbIndex::addAttr"
    attr.add(attr1);
    
    return;
    // -end- 4090DE8E0220 add_body40508E1C036E "DbIndex::addAttr"
  }

  /** disconnect a Attr.
   *  @see #addAttr
   */
  // -beg- preserve=no 4090DE8E0220 remove_head40508E1C036E "DbIndex::removeAttr"
  public DbColumn removeAttr(DbColumn attr1)
  // -end- 4090DE8E0220 remove_head40508E1C036E "DbIndex::removeAttr"
  {
    // -beg- preserve=no 4090DE8E0220 remove_body40508E1C036E "DbIndex::removeAttr"
    DbColumn ret=null;
    if(attr1==null || !attr.contains(attr1)){
      throw new java.lang.IllegalArgumentException("cannot remove null or unknown object");
    }
    ret = attr1;
    attr.remove(attr1);
    
    return ret;
    // -end- 4090DE8E0220 remove_body40508E1C036E "DbIndex::removeAttr"
  }

  /** tests if a given Attr is connected.
   *  @see #addAttr
   */
  // -beg- preserve=no 4090DE8E0220 test_head40508E1C036E "DbIndex::containsAttr"
  public boolean containsAttr(DbColumn attr1)
  // -end- 4090DE8E0220 test_head40508E1C036E "DbIndex::containsAttr"
  {
    // -beg- preserve=no 4090DE8E0220 test_body40508E1C036E "DbIndex::containsAttr"
    return attr.contains(attr1);
    // -end- 4090DE8E0220 test_body40508E1C036E "DbIndex::containsAttr"
  }

  /** used to enumerate all connected Attrs.
   *  @see #addAttr
   */
  // -beg- preserve=no 4090DE8E0220 get_all_head40508E1C036E "DbIndex::iteratorAttr"
  public java.util.Iterator iteratorAttr()
  // -end- 4090DE8E0220 get_all_head40508E1C036E "DbIndex::iteratorAttr"
  {
    // -beg- preserve=no 4090DE8E0220 get_all_body40508E1C036E "DbIndex::iteratorAttr"
    return attr.iterator();
    // -end- 4090DE8E0220 get_all_body40508E1C036E "DbIndex::iteratorAttr"
  }

  /** disconnect all Attrs.
   *  @see #addAttr
   */
  // -beg- preserve=no 4090DE8E0220 remove_all_head40508E1C036E "DbIndex::clearAttr"
  public void clearAttr()
  // -end- 4090DE8E0220 remove_all_head40508E1C036E "DbIndex::clearAttr"
  {
    // -beg- preserve=no 4090DE8E0220 remove_all_body40508E1C036E "DbIndex::clearAttr"
    if(sizeAttr()>0){
      attr.clear();
      
    }
    // -end- 4090DE8E0220 remove_all_body40508E1C036E "DbIndex::clearAttr"
  }

  /** returns the number of Attrs.
   *  @see #addAttr
   */
  // -beg- preserve=no 4090DE8E0220 size_head40508E1C036E "DbIndex::sizeAttr"
  public int sizeAttr()
  // -end- 4090DE8E0220 size_head40508E1C036E "DbIndex::sizeAttr"
  {
    // -beg- preserve=no 4090DE8E0220 size_body40508E1C036E "DbIndex::sizeAttr"
    return attr.size();
    // -end- 4090DE8E0220 size_body40508E1C036E "DbIndex::sizeAttr"
  }

  // -beg- preserve=no 40508E260246 var40508E1C036E "name"
  private String name;
  // -end- 40508E260246 var40508E1C036E "name"

  /** get current value of name
   *  @see #setName
   */
  // -beg- preserve=no 40508E260246 get_head40508E1C036E "name"
  public  String getName()
  // -end- 40508E260246 get_head40508E1C036E "name"
  {
    // -beg- preserve=no 40508E260246 get_body40508E1C036E "name"
    return name;
    // -end- 40508E260246 get_body40508E1C036E "name"
  }

  /** set current value of name
   *  @see #getName
   */
  // -beg- preserve=no 40508E260246 set_head40508E1C036E "name"
  public  void setName(String value1)
  // -end- 40508E260246 set_head40508E1C036E "name"
  {
    // -beg- preserve=no 40508E260246 set_body40508E1C036E "name"
    if(name != value1){
      name = value1;
      
    }
    // -end- 40508E260246 set_body40508E1C036E "name"
  }

  // -beg- preserve=no 4107651900A9 var40508E1C036E "primary"
  private boolean primary = false;
  // -end- 4107651900A9 var40508E1C036E "primary"

  /** get current value of primary
   *  @see #setPrimary
   */
  // -beg- preserve=no 4107651900A9 get_head40508E1C036E "primary"
  public  boolean isPrimary()
  // -end- 4107651900A9 get_head40508E1C036E "primary"
  {
    // -beg- preserve=no 4107651900A9 get_body40508E1C036E "primary"
    return primary;
    // -end- 4107651900A9 get_body40508E1C036E "primary"
  }

  /** set current value of primary
   *  @see #isPrimary
   */
  // -beg- preserve=no 4107651900A9 set_head40508E1C036E "primary"
  public  void setPrimary(boolean value1)
  // -end- 4107651900A9 set_head40508E1C036E "primary"
  {
    // -beg- preserve=no 4107651900A9 set_body40508E1C036E "primary"
    if(primary != value1){
      primary = value1;
      
    }
    // -end- 4107651900A9 set_body40508E1C036E "primary"
  }

  // -beg- preserve=no 410765F501A0 var40508E1C036E "unique"
  private boolean unique = true;
  // -end- 410765F501A0 var40508E1C036E "unique"

  /** get current value of unique
   *  @see #setUnique
   */
  // -beg- preserve=no 410765F501A0 get_head40508E1C036E "unique"
  public  boolean isUnique()
  // -end- 410765F501A0 get_head40508E1C036E "unique"
  {
    // -beg- preserve=no 410765F501A0 get_body40508E1C036E "unique"
    return unique;
    // -end- 410765F501A0 get_body40508E1C036E "unique"
  }

  /** set current value of unique
   *  @see #isUnique
   */
  // -beg- preserve=no 410765F501A0 set_head40508E1C036E "unique"
  public  void setUnique(boolean value1)
  // -end- 410765F501A0 set_head40508E1C036E "unique"
  {
    // -beg- preserve=no 410765F501A0 set_body40508E1C036E "unique"
    if(unique != value1){
      unique = value1;
      
    }
    // -end- 410765F501A0 set_body40508E1C036E "unique"
  }

  // declare/define something only in the code
  // please fill in/modify the following section
  // -beg- preserve=yes 40508E1C036E detail_end "DbIndex"
  public  void addAttr(String value1)
  {
  	DbColumn col=getTable().getColumn(value1);
  	addAttr(col);
  }

  // -end- 40508E1C036E detail_end "DbIndex"

}

