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

package ch.ehi.sqlgen.repository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import ch.ehi.sqlgen.repository.DbColumn;
import ch.ehi.sqlgen.repository.DbConstraint;
import ch.ehi.sqlgen.repository.DbIndex;
import ch.ehi.sqlgen.repository.DbEnumEle;

public class DbTable
{
  private java.util.List<DbColumn> column = new java.util.ArrayList<DbColumn>();
  private String comment=null;
  private DbSchema schema=null;
  private java.util.Map<String,String> nativeConstraints=new java.util.TreeMap<String,String>();

  /** add a Column.
   *  
   *  @see #removeColumn
   *  @see #containsColumn
   *  @see #iteratorColumn
   *  @see #clearColumn
   *  @see #sizeColumn
   */
  public void addColumn(DbColumn column1)
  {
    column.add(column1);
    
    return;
  }

  public void addColumn(int index,DbColumn column1)
  {
    column.add(index,column1);
    
    return;
  }

  /** disconnect a Column.
   *  @see #addColumn
   */
  public DbColumn removeColumn(DbColumn column1)
  {
    DbColumn ret=null;
    if(column1==null || !column.contains(column1)){
      throw new java.lang.IllegalArgumentException("cannot remove null or unknown object");
    }
    ret = column1;
    column.remove(column1);
    
    return ret;
  }

  /** disconnect a Column.
   *  @see #addColumn
   */
  public DbColumn removeColumn(int index)
  {
    DbColumn ret=null;
    ret = (DbColumn)column.remove(index);
    
    return ret;
  }

  /** change a Column.
   *  @see #addColumn
   */
  public DbColumn setColumn(int index,DbColumn column1)
  {
    DbColumn ret=null;
    ret = (DbColumn)column.set(index,column1);
    
    return ret;
  }

  /** tests if a given Column is connected.
   *  @see #addColumn
   */
  public boolean containsColumn(DbColumn column1)
  {
    return column.contains(column1);
  }

  /** used to enumerate all connected Columns.
   *  @see #addColumn
   */
  public java.util.Iterator<DbColumn> iteratorColumn()
  {
    return column.iterator();
  }

  /** disconnect all Columns.
   *  @see #addColumn
   */
  public void clearColumn()
  {
    if(sizeColumn()>0){
      column.clear();
      
    }
  }

  /** returns the number of Columns.
   *  @see #addColumn
   */
  public int sizeColumn()
  {
    return column.size();
  }

  private java.util.Set<DbConstraint> constraint = new java.util.HashSet<DbConstraint>();

  /** add a Constraint.
   *  
   *  @see #removeConstraint
   *  @see #containsConstraint
   *  @see #iteratorConstraint
   *  @see #clearConstraint
   *  @see #sizeConstraint
   */
  public void addConstraint(DbConstraint constraint1)
  {
    constraint.add(constraint1);
    
    return;
  }

  /** disconnect a Constraint.
   *  @see #addConstraint
   */
  public DbConstraint removeConstraint(DbConstraint constraint1)
  {
    DbConstraint ret=null;
    if(constraint1==null || !constraint.contains(constraint1)){
      throw new java.lang.IllegalArgumentException("cannot remove null or unknown object");
    }
    ret = constraint1;
    constraint.remove(constraint1);
    
    return ret;
  }

  /** tests if a given Constraint is connected.
   *  @see #addConstraint
   */
  public boolean containsConstraint(DbConstraint constraint1)
  {
    return constraint.contains(constraint1);
  }

  /** used to enumerate all connected Constraints.
   *  @see #addConstraint
   */
  public java.util.Iterator<DbConstraint> iteratorConstraint()
  {
    return constraint.iterator();
  }

  /** disconnect all Constraints.
   *  @see #addConstraint
   */
  public void clearConstraint()
  {
    if(sizeConstraint()>0){
      constraint.clear();
      
    }
  }

  /** returns the number of Constraints.
   *  @see #addConstraint
   */
  public int sizeConstraint()
  {
    return constraint.size();
  }

  private java.util.Set<DbIndex> index = new java.util.HashSet<DbIndex>();

  /** add a Index.
   *  
   *  @see #removeIndex
   *  @see #containsIndex
   *  @see #iteratorIndex
   *  @see #clearIndex
   *  @see #sizeIndex
   */
  public void addIndex(DbIndex index1)
  {
    index.add(index1);
    index1._linkTable(this);
    String idxName=index1.getName();
    if(idxName!=null){
    	schema.addConstraintName(idxName);
    }
    return;
  }

  /** disconnect a Index.
   *  @see #addIndex
   */
  public DbIndex removeIndex(DbIndex index1)
  {
    DbIndex ret=null;
    if(index1==null || !index.contains(index1)){
      throw new java.lang.IllegalArgumentException("cannot remove null or unknown object");
    }
    ret = index1;
    index.remove(index1);
    index1._unlinkTable(this);
    
    return ret;
  }

  /** tests if a given Index is connected.
   *  @see #addIndex
   */
  public boolean containsIndex(DbIndex index1)
  {
    return index.contains(index1);
  }

  /** used to enumerate all connected Indexs.
   *  @see #addIndex
   */
  public java.util.Iterator<DbIndex> iteratorIndex()
  {
    return index.iterator();
  }

  /** disconnect all Indexs.
   *  @see #addIndex
   */
  public void clearIndex()
  {
    if(sizeIndex()>0){
      for(java.util.Iterator p = index.iterator(); p.hasNext();){
        ((DbIndex)p.next())._unlinkTable(this);
      }
      index.clear();
      
    }
  }

  /** returns the number of Indexs.
   *  @see #addIndex
   */
  public int sizeIndex()
  {
    return index.size();
  }

  /** DONT USE; link management internal
   */
  public void _linkIndex(DbIndex index1)
  {
    index.add(index1);
    String idxName=index1.getName();
    if(idxName!=null){
    	schema.addConstraintName(idxName);
    }
    return;
  }

  /** DONT USE; link management internal
   */
  public void _unlinkIndex(DbIndex index1)
  {
    index.remove(index1);
    
    return;
  }
  private java.util.Set<DbEnumEle> enumEle = new java.util.HashSet<DbEnumEle>();

  /** add a EnumEle.
   *  
   *  @see #removeEnumEle
   *  @see #containsEnumEle
   *  @see #iteratorEnumEle
   *  @see #clearEnumEle
   *  @see #sizeEnumEle
   */
  public void addEnumEle(DbEnumEle enumEle1)
  {
    enumEle.add(enumEle1);
    
    return;
  }

  /** disconnect a EnumEle.
   *  @see #addEnumEle
   */
  public DbEnumEle removeEnumEle(DbEnumEle enumEle1)
  {
    DbEnumEle ret=null;
    if(enumEle1==null || !enumEle.contains(enumEle1)){
      throw new java.lang.IllegalArgumentException("cannot remove null or unknown object");
    }
    ret = enumEle1;
    enumEle.remove(enumEle1);
    
    return ret;
  }

  /** tests if a given EnumEle is connected.
   *  @see #addEnumEle
   */
  public boolean containsEnumEle(DbEnumEle enumEle1)
  {
    return enumEle.contains(enumEle1);
  }

  /** used to enumerate all connected EnumEles.
   *  @see #addEnumEle
   */
  public java.util.Iterator<DbEnumEle> iteratorEnumEle()
  {
    return enumEle.iterator();
  }

  /** disconnect all EnumEles.
   *  @see #addEnumEle
   */
  public void clearEnumEle()
  {
    if(sizeEnumEle()>0){
      enumEle.clear();
      
    }
  }

  /** returns the number of EnumEles.
   *  @see #addEnumEle
   */
  public int sizeEnumEle()
  {
    return enumEle.size();
  }

  private DbTableName name;

  /** get current value of name
   *  @see #setName
   */
  public  DbTableName getName()
  {
		return name;
  }

  /** set current value of name
   *  @see #getName
   */
  public  void setName(DbTableName value1)
  {
    if(name != value1){
      name = value1;
      
    }
  }

  private boolean requiresSequence = false;

  /** get current value of requiresSequence
   *  @see #setRequiresSequence
   */
  public  boolean isRequiresSequence()
  {
    return requiresSequence;
  }

  /** set current value of requiresSequence
   *  @see #isRequiresSequence
   */
  public  void setRequiresSequence(boolean value1)
  {
    if(requiresSequence != value1){
      requiresSequence = value1;
      
    }
  }

  private String iliName;

  /** get current value of iliName
   *  @see #setIliName
   */
  public  String getIliName()
  {
    return iliName;
  }

  /** set current value of iliName
   *  @see #getIliName
   */
  public  void setIliName(String value1)
  {
    if(iliName != value1){
      iliName = value1;
      
    }
  }

	public DbColumn getColumn(String name){
		java.util.Iterator<DbColumn> coli=iteratorColumn();
		while(coli.hasNext()){
			DbColumn col=(DbColumn)coli.next();
			if(col.getName().equals(name)){
				return col;			
			}
		}
		throw new java.lang.IllegalArgumentException("DbColumn "+name+"doesnt exist");
	}
	private HashMap<String,String> customValues=new HashMap<String,String>();
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
    public String getNativeConstraint(String name) {
        String value=nativeConstraints.get(name);
        return value;
    }
    public void setNativeConstraint(String name,String value) {
        value=ch.ehi.basics.tools.StringUtility.purge(value);
        if(value==null){
            nativeConstraints.remove(name);
        }else{
            nativeConstraints.put(name, value);
        }
    }
    public java.util.Iterator<String> iteratorNativeConstraints() {
        return nativeConstraints.keySet().iterator();
    }
    public int sizeNativeConstraints() {
        return nativeConstraints.size();
    }
	private boolean deleteDataIfTableExists=false;

	public boolean isDeleteDataIfTableExists() {
		return deleteDataIfTableExists;
	}

	public void setDeleteDataIfTableExists(boolean deleteDataIfTableExists) {
		this.deleteDataIfTableExists = deleteDataIfTableExists;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getComment() {
		return comment;
	}

	protected void _linkSchema(DbSchema dbschema)
	{
		schema=dbschema;
	}
	protected void _unlinkSchema()
	{
		schema=null;
	}
	public DbSchema getSchema()
	{
		return schema;
	}
}

