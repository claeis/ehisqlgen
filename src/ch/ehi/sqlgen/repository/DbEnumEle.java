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

// -beg- preserve=no 4091349E011D package "DbEnumEle"
package ch.ehi.sqlgen.repository;
// -end- 4091349E011D package "DbEnumEle"

// -beg- preserve=no 4091349E011D autoimport "DbEnumEle"

// -end- 4091349E011D autoimport "DbEnumEle"

// import declarations
// please fill in/modify the following section
// -beg- preserve=no 4091349E011D import "DbEnumEle"

// -end- 4091349E011D import "DbEnumEle"

public class DbEnumEle
{
  // declare/define something only in the code
  // please fill in/modify the following section
  // -beg- preserve=no 4091349E011D detail_begin "DbEnumEle"

  // -end- 4091349E011D detail_begin "DbEnumEle"

  // -beg- preserve=no 409134EF00F1 var4091349E011D "iliCode"
  private String iliCode;
  // -end- 409134EF00F1 var4091349E011D "iliCode"

  /** get current value of iliCode
   *  @see #setIliCode
   */
  // -beg- preserve=no 409134EF00F1 get_head4091349E011D "iliCode"
  public  String getIliCode()
  // -end- 409134EF00F1 get_head4091349E011D "iliCode"
  {
    // -beg- preserve=no 409134EF00F1 get_body4091349E011D "iliCode"
    return iliCode;
    // -end- 409134EF00F1 get_body4091349E011D "iliCode"
  }

  /** set current value of iliCode
   *  @see #getIliCode
   */
  // -beg- preserve=no 409134EF00F1 set_head4091349E011D "iliCode"
  public  void setIliCode(String value1)
  // -end- 409134EF00F1 set_head4091349E011D "iliCode"
  {
    // -beg- preserve=no 409134EF00F1 set_body4091349E011D "iliCode"
    if(iliCode != value1){
      iliCode = value1;
      
    }
    // -end- 409134EF00F1 set_body4091349E011D "iliCode"
  }

  // -beg- preserve=no 41DC133F0016 var4091349E011D "sequence"
  private int sequence = 0;
  // -end- 41DC133F0016 var4091349E011D "sequence"

  /** get current value of sequence
   *  @see #setSequence
   */
  // -beg- preserve=no 41DC133F0016 get_head4091349E011D "sequence"
  public  int getSequence()
  // -end- 41DC133F0016 get_head4091349E011D "sequence"
  {
    // -beg- preserve=no 41DC133F0016 get_body4091349E011D "sequence"
    return sequence;
    // -end- 41DC133F0016 get_body4091349E011D "sequence"
  }

  /** set current value of sequence
   *  @see #getSequence
   */
  // -beg- preserve=no 41DC133F0016 set_head4091349E011D "sequence"
  public  void setSequence(int value1)
  // -end- 41DC133F0016 set_head4091349E011D "sequence"
  {
    // -beg- preserve=no 41DC133F0016 set_body4091349E011D "sequence"
    if(sequence != value1){
      sequence = value1;
      
    }
    // -end- 41DC133F0016 set_body4091349E011D "sequence"
  }

  // declare/define something only in the code
  // please fill in/modify the following section
  // -beg- preserve=no 4091349E011D detail_end "DbEnumEle"

  // -end- 4091349E011D detail_end "DbEnumEle"

}

