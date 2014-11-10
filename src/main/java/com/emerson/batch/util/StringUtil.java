package com.emerson.batch.util;

import java.io.UnsupportedEncodingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Created with IntelliJ IDEA. User: nandyala Date: 2/16/14 Time: 10:03 PM To change this template use File | Settings |
 * File Templates.
 *
 * @author   $author$
 * @version  $Revision$, $Date$
 */
public class StringUtil {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final Log logger = LogFactory.getLog(StringUtil.class);

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   input  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static String getNumberString(String input) {
    input = stripInvalidChar(input);
    input = input.replaceAll(",", "");

    return input;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   in  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static String stripInvalidChar(String in) {
    byte[] utf = new byte[] {};

    try {
      utf = in.getBytes("UTF-8");

      int len = utf.length;

      for (int i = 0; i < len; i++) {
        int ii = utf[i];

        if (ii < 0) {
          utf[i] = 32;
        }
      }
    } catch (UnsupportedEncodingException c) {
      logger.error(c);

      return "";
    }

    return new String(utf);
  }
} // end class StringUtil
