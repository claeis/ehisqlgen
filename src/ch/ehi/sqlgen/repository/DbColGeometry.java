// Copyright (c) 2002, Eisenhut Informatik
// All rights reserved.
// $Date: 2006/04/27 06:56:43 $
// $Revision: 1.1.1.1 $
//

// -beg- preserve=no 40509E2001F7 package "DbColGeometry"
package ch.ehi.sqlgen.repository;
// -end- 40509E2001F7 package "DbColGeometry"

// -beg- preserve=no 40509E2001F7 autoimport "DbColGeometry"
import ch.ehi.sqlgen.repository.DbColumn;
// -end- 40509E2001F7 autoimport "DbColGeometry"

// import declarations
// please fill in/modify the following section
// -beg- preserve=no 40509E2001F7 import "DbColGeometry"

// -end- 40509E2001F7 import "DbColGeometry"

public class DbColGeometry extends DbColumn
{
  // declare/define something only in the code
  // please fill in/modify the following section
  // -beg- preserve=yes 40509E2001F7 detail_begin "DbColGeometry"
  public static final int POINT=0;
  public static final int LINESTRING=2;
  public static final int POLYGON=3;
  public static final int MULTIPOINT=4;
  public static final int MULTILINESTRING=5;
  public static final int MULTIPOLYGON=6;
  public static final int GEOMETRYCOLLECTION=7;
  public static final int CIRCULARSTRING=8;
  public static final int COMPOUNDCURVE=9;
  public static final int CURVEPOLYGON=10;
  public static final int MULTICURVE=11;
  public static final int MULTISURFACE=12;
  public static final int POLYHEDRALSURFACE = 15;
  public static final int TIN = 16;
  public static final int TRIANGLE = 17;
  // -end- 40509E2001F7 detail_begin "DbColGeometry"

  // -beg- preserve=no 45BE29090179 var40509E2001F7 "type"
  private int type;
  // -end- 45BE29090179 var40509E2001F7 "type"

  /** get current value of type
   *  @see #setType
   */
  // -beg- preserve=no 45BE29090179 get_head40509E2001F7 "type"
  public  int getType()
  // -end- 45BE29090179 get_head40509E2001F7 "type"
  {
    // -beg- preserve=no 45BE29090179 get_body40509E2001F7 "type"
    return type;
    // -end- 45BE29090179 get_body40509E2001F7 "type"
  }

  /** set current value of type
   *  @see #getType
   */
  // -beg- preserve=no 45BE29090179 set_head40509E2001F7 "type"
  public  void setType(int value1)
  // -end- 45BE29090179 set_head40509E2001F7 "type"
  {
    // -beg- preserve=no 45BE29090179 set_body40509E2001F7 "type"
    if(type != value1){
      type = value1;
      
    }
    // -end- 45BE29090179 set_body40509E2001F7 "type"
  }

  // -beg- preserve=no 45BE2910001B var40509E2001F7 "srsId"
  private String srsId;
  // -end- 45BE2910001B var40509E2001F7 "srsId"

  /** get current value of srsId
   *  the ID of the Spatial Reference System as defined by the Authority cited in srsAuth
   *  @see #setSrsId
   */
  // -beg- preserve=no 45BE2910001B get_head40509E2001F7 "srsId"
  public  String getSrsId()
  // -end- 45BE2910001B get_head40509E2001F7 "srsId"
  {
    // -beg- preserve=no 45BE2910001B get_body40509E2001F7 "srsId"
    return srsId;
    // -end- 45BE2910001B get_body40509E2001F7 "srsId"
  }

  /** set current value of srsId
   *  @see #getSrsId
   */
  // -beg- preserve=no 45BE2910001B set_head40509E2001F7 "srsId"
  public  void setSrsId(String value1)
  // -end- 45BE2910001B set_head40509E2001F7 "srsId"
  {
    // -beg- preserve=no 45BE2910001B set_body40509E2001F7 "srsId"
    if(srsId != value1){
      srsId = value1;
      
    }
    // -end- 45BE2910001B set_body40509E2001F7 "srsId"
  }

  // -beg- preserve=no 45BE291F0149 var40509E2001F7 "srsAuth"
  private String srsAuth;
  // -end- 45BE291F0149 var40509E2001F7 "srsAuth"

  /** get current value of srsAuth
   *  the name of the standard or standards body that is being cited for this reference system.
   *  @see #setSrsAuth
   */
  // -beg- preserve=no 45BE291F0149 get_head40509E2001F7 "srsAuth"
  public  String getSrsAuth()
  // -end- 45BE291F0149 get_head40509E2001F7 "srsAuth"
  {
    // -beg- preserve=no 45BE291F0149 get_body40509E2001F7 "srsAuth"
    return srsAuth;
    // -end- 45BE291F0149 get_body40509E2001F7 "srsAuth"
  }

  /** set current value of srsAuth
   *  @see #getSrsAuth
   */
  // -beg- preserve=no 45BE291F0149 set_head40509E2001F7 "srsAuth"
  public  void setSrsAuth(String value1)
  // -end- 45BE291F0149 set_head40509E2001F7 "srsAuth"
  {
    // -beg- preserve=no 45BE291F0149 set_body40509E2001F7 "srsAuth"
    if(srsAuth != value1){
      srsAuth = value1;
      
    }
    // -end- 45BE291F0149 set_body40509E2001F7 "srsAuth"
  }

  // -beg- preserve=no 45BE2B440026 var40509E2001F7 "dimension"
  private int dimension;
  // -end- 45BE2B440026 var40509E2001F7 "dimension"

  /** get current value of dimension
   *  The spatial dimension (2, 3 or 4 dimensional) of the column
   *  @see #setDimension
   */
  // -beg- preserve=no 45BE2B440026 get_head40509E2001F7 "dimension"
  public  int getDimension()
  // -end- 45BE2B440026 get_head40509E2001F7 "dimension"
  {
    // -beg- preserve=no 45BE2B440026 get_body40509E2001F7 "dimension"
    return dimension;
    // -end- 45BE2B440026 get_body40509E2001F7 "dimension"
  }

  /** set current value of dimension
   *  @see #getDimension
   */
  // -beg- preserve=no 45BE2B440026 set_head40509E2001F7 "dimension"
  public  void setDimension(int value1)
  // -end- 45BE2B440026 set_head40509E2001F7 "dimension"
  {
    // -beg- preserve=no 45BE2B440026 set_body40509E2001F7 "dimension"
    if(dimension != value1){
      dimension = value1;
      
    }
    // -end- 45BE2B440026 set_body40509E2001F7 "dimension"
  }


  // declare/define something only in the code
  // please fill in/modify the following section
  // -beg- preserve=yes 40509E2001F7 detail_end "DbColGeometry"
  private String max1;
  private String max2;
  private String max3;
  private String min1;
  private String min2;
  private String min3;

public String getMax1AsString() {
    return max1;
}

public void setMax1(String max1) {
    if(max1!=null) {
        Double.valueOf(max1);
    }
    this.max1 = max1;
}

public String getMax2AsString() {
    return max2;
}

public void setMax2(String max2) {
    if(max2!=null) {
        Double.valueOf(max2);
    }
    this.max2 = max2;
}

public String getMax3AsString() {
    return max3;
}

public void setMax3(String max3) {
    if(max3!=null) {
        Double.valueOf(max3);
    }
    this.max3 = max3;
}

public String getMin1AsString() {
    return min1;
}

public void setMin1(String min1) {
    if(min1!=null) {
        Double.valueOf(min1);
    }
    this.min1 = min1;
}

public String getMin2AsString() {
    return min2;
}

public void setMin2(String min2) {
    if(min2!=null) {
        Double.valueOf(min2);
    }
    this.min2 = min2;
}

public String getMin3AsString() {
    return min3;
}

public void setMin3(String min3) {
    if(min3!=null) {
        Double.valueOf(min3);
    }
    this.min3 = min3;
}
public double getMax1() {
    return Double.valueOf(max1);
}

public void setMax1(double max1) {
    this.max1 = Double.toString(max1);
}
public double getMax2() {
    return Double.valueOf(max2);
}

public void setMax2(double max2) {
    this.max2 = Double.toString(max2);
}

public double getMax3() {
    return Double.valueOf(max3);
}

public void setMax3(double max3) {
    this.max3 = Double.toString(max3);
}

public double getMin1() {
    return Double.valueOf(min1);
}

public void setMin1(double min1) {
    this.min1 = Double.toString(min1);
}

public double getMin2() {
    return Double.valueOf(min2);
}

public void setMin2(double min2) {
    this.min2 = Double.toString(min2);
}

public double getMin3() {
    return Double.valueOf(min3);
}

public void setMin3(double min3) {
    this.min3 = Double.toString(min3);
}

  
  // -end- 40509E2001F7 detail_end "DbColGeometry"

}

