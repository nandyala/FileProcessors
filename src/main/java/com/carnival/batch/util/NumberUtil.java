package com.carnival.batch.util;

import org.springframework.util.StringUtils;

import com.carnival.batch.support.SampleSkipException;


/**
 * Created with IntelliJ IDEA. User: nandyala Date: 2/16/14 Time: 10:10 PM To change this template use File | Settings |
 * File Templates.
 *
 * @author   $author$
 * @version  $Revision$, $Date$
 */
public class NumberUtil {
  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   str  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static boolean isDigits(String str) {
    if (StringUtils.isEmpty(str)) {
      return false;
    }

    for (int i = 0; i < str.length(); i++) {
      if (!Character.isDigit(str.charAt(i))) {
        return false;
      }
    }

    return true;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   str  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static boolean isNumber(String str) {
    if (StringUtils.isEmpty(str)) {
      return false;
    }

    char[]  chars       = str.toCharArray();
    int     sz          = chars.length;
    boolean hasExp      = false;
    boolean hasDecPoint = false;
    boolean allowSigns  = false;
    boolean foundDigit  = false;

    // deal with any possible sign up front
    int start = (chars[0] == '-') ? 1 : 0;

    if (sz > (start + 1)) {
      if ((chars[start] == '0') && (chars[start + 1] == 'x')) {
        int i = start + 2;

        if (i == sz) {
          return false; // str == "0x"
        }

        // checking hex (it can't be anything else)
        for (; i < chars.length; i++) {
          if (((chars[i] < '0') || (chars[i] > '9'))
                && ((chars[i] < 'a') || (chars[i] > 'f'))
                && ((chars[i] < 'A') || (chars[i] > 'F'))) {
            return false;
          }
        }

        return true;
      }
    }

    sz--; // don't want to loop to the last char, check it afterwords
          // for type qualifiers

    int i = start;

    // loop to the next to last char or to the last char if we need another digit to
    // make a valid number (e.g. chars[0..5] = "1234E")
    while ((i < sz) || ((i < (sz + 1)) && allowSigns && !foundDigit)) {
      if ((chars[i] >= '0') && (chars[i] <= '9')) {
        foundDigit = true;
        allowSigns = false;

      } else if (chars[i] == '.') {
        if (hasDecPoint || hasExp) {
          // two decimal points or dec in exponent
          return false;
        }

        hasDecPoint = true;
      } else if ((chars[i] == 'e') || (chars[i] == 'E')) {
        // we've already taken care of hex.
        if (hasExp) {
          // two E's
          return false;
        }

        if (!foundDigit) {
          return false;
        }

        hasExp     = true;
        allowSigns = true;
      } else if ((chars[i] == '+') || (chars[i] == '-')) {
        if (!allowSigns) {
          return false;
        }

        allowSigns = false;
        foundDigit = false; // we need a digit after the E
      } else {
        return false;
      }                     // end if-else

      i++;
    } // end while

    if (i < chars.length) {
      if ((chars[i] >= '0') && (chars[i] <= '9')) {
        // no type qualifier, OK
        return true;
      }

      if ((chars[i] == 'e') || (chars[i] == 'E')) {
        // can't have an E at the last byte
        return false;
      }

      if (chars[i] == '.') {
        if (hasDecPoint || hasExp) {
          // two decimal points or dec in exponent
          return false;
        }

        // single trailing decimal point after non-exponent is ok
        return foundDigit;
      }

      if (!allowSigns
            && ((chars[i] == 'd')
              || (chars[i] == 'D')
              || (chars[i] == 'f')
              || (chars[i] == 'F'))) {
        return foundDigit;
      }

      if ((chars[i] == 'l')
            || (chars[i] == 'L')) {
        // not allowing L with an exponent
        return foundDigit && !hasExp;
      }

      // last character is illegal
      return false;
    } // end if

    // allowSigns is true iff the val ends in 'E'
    // found digit it to make sure weird stuff like '.' and '1E-' doesn't pass
    return !allowSigns && foundDigit;
  } // end method isNumber

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   input  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  SampleSkipException  DOCUMENT ME!
   */
  public static Boolean isValidNumber(String input) {
    input = StringUtil.getNumberString(input);

    if (!isNumber(input)) {
      throw new SampleSkipException();
    }

    return Boolean.TRUE;
  }

} // end class NumberUtil
