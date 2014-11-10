package com.emerson.batch.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.util.StringUtils;


/**
 * Created with IntelliJ IDEA. User: nandyala Date: 2/12/14 Time: 10:11 PM To change this template use File | Settings |
 * File Templates.
 *
 * @author   $author$
 * @version  $Revision$, $Date$
 */
public class DateUtil {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final Log    logger      = LogFactory.getLog(DateUtil.class);
  private static List<String> dateFormats = Arrays.asList(
      "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss",
      "MM/dd/yyyy HH:mm:ss", "MM/dd/yyyy'T'HH:mm:ss", "yyyy-MM-dd",
      "MM/dd/yyyy");

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  dateFormat  DOCUMENT ME!
   */
  public static void addDateFormats(String dateFormat) {
    List<String> formatList = new ArrayList<String>();
    formatList.addAll(DateUtil.dateFormats);
    formatList.add(dateFormat);

    DateUtil.dateFormats = formatList;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static List<String> getDateFormats() {
    return dateFormats;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   dateToValidate  DOCUMENT ME!
   * @param   dateFormat      DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static boolean isThisDateValid(String dateToValidate, String dateFormat) {
    if (dateToValidate == null) {
      return false;
    }

    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
    sdf.setLenient(false);

    try {
      // if not valid, it will throw ParseException
      Date date = sdf.parse(dateToValidate);
      logger.info(date);

    } catch (ParseException e) {
      logger.error(e);

      return false;
    }

    return true;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   dataString  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static boolean isValidDateString(String dataString) {
    if (StringUtils.hasText(dataString)) {
      for (String dateFormat : dateFormats) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

        try {
          Date testDate = sdf.parse(trim(dataString));

          if (!sdf.format(testDate).equals(trim(dataString))) {
            return false;
          }

          return true;
        } catch (ParseException e) {
          // not match for this pattern, try next one
        }
      }
    }

    return false;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  dateFormats  DOCUMENT ME!
   */
  public static void setDateFormats(List<String> dateFormats) {
    DateUtil.dateFormats = dateFormats;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   input  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static String trim(String input) {
    if (input != null) {
      String output = input.trim();
      output = output.replaceAll("^\"*\'*\"*|\"*\'*\"*$", "");

      return output;
    }

    return null;
  }

} // end class DateUtil
