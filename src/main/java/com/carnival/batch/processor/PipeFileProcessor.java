package com.carnival.batch.processor;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.transform.FieldSet;

import com.carnival.batch.support.ExtendedFieldSet;
import com.carnival.batch.support.SampleSkipException;
import com.carnival.batch.util.DateUtil;
import com.carnival.batch.util.NumberUtil;
import com.carnival.batch.util.StringUtil;


/**
 * Created with IntelliJ IDEA. User: nandyala Date: 2/12/14 Time: 8:58 PM To change this template use File | Settings |
 * File Templates.
 *
 * @author   $author$
 * @version  $Revision$, $Date$
 */
public class PipeFileProcessor implements ItemProcessor {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final Log logger = LogFactory.getLog(PipeFileProcessor.class);

  //~ Instance fields --------------------------------------------------------------------------------------------------

  private String dateFormat;
  private String delimiter;

  private Map<String, String> recordTypeMap;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public String getDateFormat() {
    return dateFormat;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public String getDelimiter() {
    return delimiter;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public Map<String, String> getRecordTypeMap() {
    return recordTypeMap;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.batch.item.ItemProcessor#process(Object)
   */
  @Override
  @SuppressWarnings("unchecked")
  public Object process(Object data) {
    ExtendedFieldSet record     = new ExtendedFieldSet((FieldSet) data);
    String           recordCode = record.readString("recordType");

    if ("HEADER".equalsIgnoreCase(recordTypeMap.get(recordCode))) {
      if (logger.isDebugEnabled()) {
        logger.debug("processing HEADER record....");
      }


      String recordDate = record.readString("Date");

      if (!DateUtil.isThisDateValid(recordDate, dateFormat)) {
        throw new SampleSkipException();
      }

      String        customData1  = StringUtil.stripInvalidChar(record.readString("CustomData1"));
      String        customData2  = StringUtil.stripInvalidChar(record.readString("CustomData2"));
      String        customData3  = StringUtil.stripInvalidChar(record.readString("CustomData3"));
      StringBuilder resultStrBuf = new StringBuilder();
      resultStrBuf.append(recordCode).append(delimiter).append(recordDate).append(delimiter).append(customData1).append(
        delimiter).append(customData2).append(delimiter).append(customData3);

      return resultStrBuf.toString();

    } else if ("Request_471".equalsIgnoreCase(recordTypeMap.get(recordCode))) {
      if (logger.isDebugEnabled()) {
        logger.debug("processing Request_471 record....");
      }

      String ibe_accounting_date = record.readString("ibe_accounting_date");

      if (!DateUtil.isThisDateValid(ibe_accounting_date, dateFormat)) {
        logger.error("invalid ibe_accounting_date..");
        throw new SampleSkipException();
      }

      String ibe_debit_amount = StringUtil.getNumberString(record.readString("ibe_debit_amount"));

      if (!NumberUtil.isNumber(ibe_debit_amount)) {
        logger.error("invalid ibe_debit_amount..");

        throw new SampleSkipException();
      }

      String ibe_credit_amount = StringUtil.getNumberString(record.readString("ibe_credit_amount"));

      if (!NumberUtil.isNumber(ibe_credit_amount)) {
        logger.error("invalid ibe_credit_amount..");

        throw new SampleSkipException();
      }

      String        ibe_location      = StringUtil.stripInvalidChar(record.readString("ibe_location"));
      String        ibe_center        = StringUtil.stripInvalidChar(record.readString("ibe_center"));
      String        ibe_account       = StringUtil.stripInvalidChar(record.readString("ibe_account"));
      String        ibe_analysis      = StringUtil.stripInvalidChar(record.readString("ibe_analysis"));
      String        ibe_intercompany  = StringUtil.stripInvalidChar(record.readString("ibe_intercompany"));
      String        ibe_currency_code = StringUtil.stripInvalidChar(record.readString("ibe_currency_code"));
      StringBuilder resultStrBuf      = new StringBuilder();

      resultStrBuf.append(recordCode).append(delimiter).append(ibe_location).append(delimiter).append(ibe_center)
        .append(delimiter).append(ibe_account).append(delimiter).append(ibe_analysis).append(delimiter).append(
        ibe_intercompany).append(delimiter).append(ibe_accounting_date).append(delimiter).append(ibe_currency_code)
        .append(delimiter).append(ibe_debit_amount).append(delimiter).append(ibe_credit_amount);


      return resultStrBuf.toString();

    } // end if
    else if ("Request_472".equalsIgnoreCase(recordTypeMap.get(recordCode))) {
      if (logger.isDebugEnabled()) {
        logger.debug("processing Request_472 record....");
      }

      String ibe_accounting_date = record.readString("ibe_accounting_date");

      if (!DateUtil.isThisDateValid(ibe_accounting_date, dateFormat)) {
        logger.error("invalid ibe_accounting_date..");
        throw new SampleSkipException();
      }

      String ibe_debit_amount = StringUtil.getNumberString(record.readString("ibe_debit_amount"));

      if (!NumberUtil.isNumber(ibe_debit_amount)) {
        logger.error("invalid ibe_debit_amount..");

        throw new SampleSkipException();
      }

      String ibe_credit_amount = StringUtil.getNumberString(record.readString("ibe_credit_amount"));

      if (!NumberUtil.isNumber(ibe_credit_amount)) {
        logger.error("invalid ibe_credit_amount..");

        throw new SampleSkipException();
      }

      String        ibe_location      = StringUtil.stripInvalidChar(record.readString("ibe_location"));
      String        ibe_center        = StringUtil.stripInvalidChar(record.readString("ibe_center"));
      String        ibe_account       = StringUtil.stripInvalidChar(record.readString("ibe_account"));
      String        ibe_analysis      = StringUtil.stripInvalidChar(record.readString("ibe_analysis"));
      String        ibe_intercompany  = StringUtil.stripInvalidChar(record.readString("ibe_intercompany"));
      String        ibe_currency_code = StringUtil.stripInvalidChar(record.readString("ibe_currency_code"));
      StringBuilder resultStrBuf      = new StringBuilder();

      resultStrBuf.append(recordCode).append(delimiter).append(ibe_location).append(delimiter).append(ibe_center)
        .append(delimiter).append(ibe_account).append(delimiter).append(ibe_analysis).append(delimiter).append(
        ibe_intercompany).append(delimiter).append(ibe_accounting_date).append(delimiter).append(ibe_currency_code)
        .append(delimiter).append(ibe_debit_amount).append(delimiter).append(ibe_credit_amount);


      return resultStrBuf.toString();

    } // end if


    return null;

  } // end method process

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  dateFormat  DOCUMENT ME!
   */
  public void setDateFormat(String dateFormat) {
    this.dateFormat = dateFormat;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  delimiter  DOCUMENT ME!
   */
  public void setDelimiter(String delimiter) {
    this.delimiter = delimiter;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  recordTypeMap  DOCUMENT ME!
   */
  public void setRecordTypeMap(Map<String, String> recordTypeMap) {
    this.recordTypeMap = recordTypeMap;
  }
} // end class PipeFileProcessor
