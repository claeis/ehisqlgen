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

  // -beg- preserve=no 45D09B2E0239 var40509E2001F7 "min1"
  private double min1;
  // -end- 45D09B2E0239 var40509E2001F7 "min1"

  /** get current value of min1
   *  @see #setMin1
   */
  // -beg- preserve=no 45D09B2E0239 get_head40509E2001F7 "min1"
  public  double getMin1()
  // -end- 45D09B2E0239 get_head40509E2001F7 "min1"
  {
    // -beg- preserve=no 45D09B2E0239 get_body40509E2001F7 "min1"
    return min1;
    // -end- 45D09B2E0239 get_body40509E2001F7 "min1"
  }

  /** set current value of min1
   *  @see #getMin1
   */
  // -beg- preserve=no 45D09B2E0239 set_head40509E2001F7 "min1"
  public  void setMin1(double value1)
  // -end- 45D09B2E0239 set_head40509E2001F7 "min1"
  {
    // -beg- preserve=no 45D09B2E0239 set_body40509E2001F7 "min1"
    if(min1 != value1){
      min1 = value1;
      
    }
    // -end- 45D09B2E0239 set_body40509E2001F7 "min1"
  }

  // -beg- preserve=no 45D09B350211 var40509E2001F7 "max1"
  private double max1;
  // -end- 45D09B350211 var40509E2001F7 "max1"

  /** get current value of max1
   *  @see #setMax1
   */
  // -beg- preserve=no 45D09B350211 get_head40509E2001F7 "max1"
  public  double getMax1()
  // -end- 45D09B350211 get_head40509E2001F7 "max1"
  {
    // -beg- preserve=no 45D09B350211 get_body40509E2001F7 "max1"
    return max1;
    // -end- 45D09B350211 get_body40509E2001F7 "max1"
  }

  /** set current value of max1
   *  @see #getMax1
   */
  // -beg- preserve=no 45D09B350211 set_head40509E2001F7 "max1"
  public  void setMax1(double value1)
  // -end- 45D09B350211 set_head40509E2001F7 "max1"
  {
    // -beg- preserve=no 45D09B350211 set_body40509E2001F7 "max1"
    if(max1 != value1){
      max1 = value1;
      
    }
    // -end- 45D09B350211 set_body40509E2001F7 "max1"
  }

  // -beg- preserve=no 45D09B3A0268 var40509E2001F7 "min2"
  private double min2;
  // -end- 45D09B3A0268 var40509E2001F7 "min2"

  /** get current value of min2
   *  @see #setMin2
   */
  // -beg- preserve=no 45D09B3A0268 get_head40509E2001F7 "min2"
  public  double getMin2()
  // -end- 45D09B3A0268 get_head40509E2001F7 "min2"
  {
    // -beg- preserve=no 45D09B3A0268 get_body40509E2001F7 "min2"
    return min2;
    // -end- 45D09B3A0268 get_body40509E2001F7 "min2"
  }

  /** set current value of min2
   *  @see #getMin2
   */
  // -beg- preserve=no 45D09B3A0268 set_head40509E2001F7 "min2"
  public  void setMin2(double value1)
  // -end- 45D09B3A0268 set_head40509E2001F7 "min2"
  {
    // -beg- preserve=no 45D09B3A0268 set_body40509E2001F7 "min2"
    if(min2 != value1){
      min2 = value1;
      
    }
    // -end- 45D09B3A0268 set_body40509E2001F7 "min2"
  }

  // -beg- preserve=no 45D09B3E008D var40509E2001F7 "max2"
  private double max2;
  // -end- 45D09B3E008D var40509E2001F7 "max2"

  /** get current value of max2
   *  @see #setMax2
   */
  // -beg- preserve=no 45D09B3E008D get_head40509E2001F7 "max2"
  public  double getMax2()
  // -end- 45D09B3E008D get_head40509E2001F7 "max2"
  {
    // -beg- preserve=no 45D09B3E008D get_body40509E2001F7 "max2"
    return max2;
    // -end- 45D09B3E008D get_body40509E2001F7 "max2"
  }

  /** set current value of max2
   *  @see #getMax2
   */
  // -beg- preserve=no 45D09B3E008D set_head40509E2001F7 "max2"
  public  void setMax2(double value1)
  // -end- 45D09B3E008D set_head40509E2001F7 "max2"
  {
    // -beg- preserve=no 45D09B3E008D set_body40509E2001F7 "max2"
    if(max2 != value1){
      max2 = value1;
      
    }
    // -end- 45D09B3E008D set_body40509E2001F7 "max2"
  }

  // -beg- preserve=no 45D09B430301 var40509E2001F7 "min3"
  private double min3;
  // -end- 45D09B430301 var40509E2001F7 "min3"

  /** get current value of min3
   *  @see #setMin3
   */
  // -beg- preserve=no 45D09B430301 get_head40509E2001F7 "min3"
  public  double getMin3()
  // -end- 45D09B430301 get_head40509E2001F7 "min3"
  {
    // -beg- preserve=no 45D09B430301 get_body40509E2001F7 "min3"
    return min3;
    // -end- 45D09B430301 get_body40509E2001F7 "min3"
  }

  /** set current value of min3
   *  @see #getMin3
   */
  // -beg- preserve=no 45D09B430301 set_head40509E2001F7 "min3"
  public  void setMin3(double value1)
  // -end- 45D09B430301 set_head40509E2001F7 "min3"
  {
    // -beg- preserve=no 45D09B430301 set_body40509E2001F7 "min3"
    if(min3 != value1){
      min3 = value1;
      
    }
    // -end- 45D09B430301 set_body40509E2001F7 "min3"
  }

  // -beg- preserve=no 45D09B48029A var40509E2001F7 "max3"
  private double max3;
  // -end- 45D09B48029A var40509E2001F7 "max3"

  /** get current value of max3
   *  @see #setMax3
   */
  // -beg- preserve=no 45D09B48029A get_head40509E2001F7 "max3"
  public  double getMax3()
  // -end- 45D09B48029A get_head40509E2001F7 "max3"
  {
    // -beg- preserve=no 45D09B48029A get_body40509E2001F7 "max3"
    return max3;
    // -end- 45D09B48029A get_body40509E2001F7 "max3"
  }

  /** set current value of max3
   *  @see #getMax3
   */
  // -beg- preserve=no 45D09B48029A set_head40509E2001F7 "max3"
  public  void setMax3(double value1)
  // -end- 45D09B48029A set_head40509E2001F7 "max3"
  {
    // -beg- preserve=no 45D09B48029A set_body40509E2001F7 "max3"
    if(max3 != value1){
      max3 = value1;
      
    }
    // -end- 45D09B48029A set_body40509E2001F7 "max3"
  }

  // declare/define something only in the code
  // please fill in/modify the following section
  // -beg- preserve=no 40509E2001F7 detail_end "DbColGeometry"

  // -end- 40509E2001F7 detail_end "DbColGeometry"

}

