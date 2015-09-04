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

// -beg- preserve=no 405066900171 package "DbColNumber"
package ch.ehi.sqlgen.repository;
// -end- 405066900171 package "DbColNumber"

// -beg- preserve=no 405066900171 autoimport "DbColNumber"
import ch.ehi.sqlgen.repository.DbColumn;
// -end- 405066900171 autoimport "DbColNumber"

// import declarations
// please fill in/modify the following section
// -beg- preserve=no 405066900171 import "DbColNumber"

// -end- 405066900171 import "DbColNumber"

public class DbColNumber extends DbColumn
{
  // declare/define something only in the code
  // please fill in/modify the following section
  // -beg- preserve=no 405066900171 detail_begin "DbColNumber"

  // -end- 405066900171 detail_begin "DbColNumber"

  // -beg- preserve=no 4050673901C4 var405066900171 "size"
  private int size = 10;
  // -end- 4050673901C4 var405066900171 "size"

  /** get current value of size
   *  @see #setSize
   */
  // -beg- preserve=no 4050673901C4 get_head405066900171 "size"
  public  int getSize()
  // -end- 4050673901C4 get_head405066900171 "size"
  {
    // -beg- preserve=no 4050673901C4 get_body405066900171 "size"
    return size;
    // -end- 4050673901C4 get_body405066900171 "size"
  }

  /** set current value of size
   *  @see #getSize
   */
  // -beg- preserve=no 4050673901C4 set_head405066900171 "size"
  public  void setSize(int value1)
  // -end- 4050673901C4 set_head405066900171 "size"
  {
    // -beg- preserve=no 4050673901C4 set_body405066900171 "size"
    if(size != value1){
      size = value1;
      
    }
    // -end- 4050673901C4 set_body405066900171 "size"
  }

  // declare/define something only in the code
  // please fill in/modify the following section
  // -beg- preserve=no 405066900171 detail_end "DbColNumber"

  // -end- 405066900171 detail_end "DbColNumber"

}

