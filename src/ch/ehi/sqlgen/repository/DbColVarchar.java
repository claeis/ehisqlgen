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

// -beg- preserve=no 4050659101ED package "DbColVarchar"
package ch.ehi.sqlgen.repository;
// -end- 4050659101ED package "DbColVarchar"

// -beg- preserve=no 4050659101ED autoimport "DbColVarchar"
import ch.ehi.sqlgen.repository.DbColumn;
// -end- 4050659101ED autoimport "DbColVarchar"

// import declarations
// please fill in/modify the following section
// -beg- preserve=no 4050659101ED import "DbColVarchar"

// -end- 4050659101ED import "DbColVarchar"

public class DbColVarchar extends DbColumn
{
  // declare/define something only in the code
  // please fill in/modify the following section
  // -beg- preserve=no 4050659101ED detail_begin "DbColVarchar"

  // -end- 4050659101ED detail_begin "DbColVarchar"

  // -beg- preserve=no 405067070154 var4050659101ED "size"
  private int size = 40;
  // -end- 405067070154 var4050659101ED "size"

  /** get current value of size
   *  @see #setSize
   */
  // -beg- preserve=no 405067070154 get_head4050659101ED "size"
  public  int getSize()
  // -end- 405067070154 get_head4050659101ED "size"
  {
    // -beg- preserve=no 405067070154 get_body4050659101ED "size"
    return size;
    // -end- 405067070154 get_body4050659101ED "size"
  }

  /** set current value of size
   *  @see #getSize
   */
  // -beg- preserve=no 405067070154 set_head4050659101ED "size"
  public  void setSize(int value1)
  // -end- 405067070154 set_head4050659101ED "size"
  {
    // -beg- preserve=no 405067070154 set_body4050659101ED "size"
    if(size != value1){
      size = value1;
      
    }
    // -end- 405067070154 set_body4050659101ED "size"
  }

  // declare/define something only in the code
  // please fill in/modify the following section
  // -beg- preserve=no 4050659101ED detail_end "DbColVarchar"

  // -end- 4050659101ED detail_end "DbColVarchar"

}

