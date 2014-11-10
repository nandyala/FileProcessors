package com.emerson.batch.processor;

import java.util.Date;

import com.emerson.batch.support.ExtendedFieldSet;
import com.emerson.batch.util.NumberUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.transform.FieldSet;

import com.emerson.batch.support.SampleSkipException;


/**
 * Created with IntelliJ IDEA. User: nandyala Date: 2/12/14 Time: 8:58 PM To change this template use File | Settings |
 * File Templates.
 *
 * @author   $author$
 * @version  $Revision$, $Date$
 */
public class FileProcessor implements ItemProcessor {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final Log logger = LogFactory.getLog(FileProcessor.class);

  //~ Instance fields --------------------------------------------------------------------------------------------------

  private String dateFormat;

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
   * @see  org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
   */
  @Override
  @SuppressWarnings("unchecked")
  public Object process(Object data) {
    if (logger.isDebugEnabled()) {
      logger.debug("processing record....");
    }

    ExtendedFieldSet record = new ExtendedFieldSet((FieldSet) data);
    Date             date   = record.readDate("ReceiptDate", dateFormat);
    NumberUtil.isNumber(record.readString("ReceiptDate"));

    if (date == null) {
      throw new SampleSkipException();
    }

    return data;

  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  dateFormat  DOCUMENT ME!
   */
  public void setDateFormat(String dateFormat) {
    this.dateFormat = dateFormat;
  }
} // end class FileProcessor
