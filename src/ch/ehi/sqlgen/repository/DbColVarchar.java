package ch.ehi.sqlgen.repository;

import ch.ehi.sqlgen.repository.DbColumn;


public class DbColVarchar extends DbColumn
{
  public static final int UNLIMITED=-1;
  private int size = UNLIMITED; 
  private String restrictedValues[]=null;
  private Integer minLength=null;
  private String kind=null;
  public static final String KIND_NORMALIZED="NORMALIZED";

  /** get current value of size
   *  @see #setSize
   */
  public  int getSize()
  {
    return size;
  }

  /** set current value of size
   *  @see #getSize
   */
  public  void setSize(int value1)
  {
    if(size != value1){
      size = value1;
    }
  }
  public String[] getValueRestriction() {
      return restrictedValues;
  }

  public void setValueRestriction(String restrictedValues[]) {
      this.restrictedValues = restrictedValues;
  }

public Integer getMinLength() {
    return minLength;
}

public void setMinLength(Integer minLength) {
    this.minLength = minLength;
}

public String getKind() {
    return kind;
}

public void setKind(String kind) {
    this.kind = kind;
}
}

