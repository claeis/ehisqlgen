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

// -beg- preserve=no 40506685031A package "DbColDecimal"
package ch.ehi.sqlgen.repository;
// -end- 40506685031A package "DbColDecimal"

// -beg- preserve=no 40506685031A autoimport "DbColDecimal"
import ch.ehi.sqlgen.repository.DbColumn;
// -end- 40506685031A autoimport "DbColDecimal"

// import declarations
// please fill in/modify the following section
// -beg- preserve=no 40506685031A import "DbColDecimal"

// -end- 40506685031A import "DbColDecimal"

public class DbColDecimal extends DbColumn
{
  // declare/define something only in the code
  // please fill in/modify the following section
  // -beg- preserve=no 40506685031A detail_begin "DbColDecimal"

  // -end- 40506685031A detail_begin "DbColDecimal"

  // -beg- preserve=no 4050674F02B6 var40506685031A "size"
  private int size = 10;
  // -end- 4050674F02B6 var40506685031A "size"

  /** get current value of size
   *  @see #setSize
   */
  // -beg- preserve=no 4050674F02B6 get_head40506685031A "size"
  public  int getSize()
  // -end- 4050674F02B6 get_head40506685031A "size"
  {
    // -beg- preserve=no 4050674F02B6 get_body40506685031A "size"
    return size;
    // -end- 4050674F02B6 get_body40506685031A "size"
  }

  /** set current value of size
   *  @see #getSize
   */
  // -beg- preserve=no 4050674F02B6 set_head40506685031A "size"
  public  void setSize(int value1)
  // -end- 4050674F02B6 set_head40506685031A "size"
  {
    // -beg- preserve=no 4050674F02B6 set_body40506685031A "size"
    if(size != value1){
      size = value1;
      
    }
    // -end- 4050674F02B6 set_body40506685031A "size"
  }

  // -beg- preserve=no 40506754008D var40506685031A "precision"
  private int precision = 2;
  // -end- 40506754008D var40506685031A "precision"

  /** get current value of precision
   *  @see #setPrecision
   */
  // -beg- preserve=no 40506754008D get_head40506685031A "precision"
  public  int getPrecision()
  // -end- 40506754008D get_head40506685031A "precision"
  {
    // -beg- preserve=no 40506754008D get_body40506685031A "precision"
    return precision;
    // -end- 40506754008D get_body40506685031A "precision"
  }

  /** set current value of precision
   *  @see #getPrecision
   */
  // -beg- preserve=no 40506754008D set_head40506685031A "precision"
  public  void setPrecision(int value1)
  // -end- 40506754008D set_head40506685031A "precision"
  {
    // -beg- preserve=no 40506754008D set_body40506685031A "precision"
    if(precision != value1){
      precision = value1;
      
    }
    // -end- 40506754008D set_body40506685031A "precision"
  }

  // declare/define something only in the code
  // please fill in/modify the following section
  // -beg- preserve=no 40506685031A detail_end "DbColDecimal"

  // -end- 40506685031A detail_end "DbColDecimal"

}

