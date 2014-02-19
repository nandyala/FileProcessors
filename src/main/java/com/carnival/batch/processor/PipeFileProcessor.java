package com.carnival.batch.processor;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.transform.FieldSet;

import com.carnival.batch.support.ExtendedFieldSet;


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
    if (logger.isDebugEnabled()) {
      logger.debug("processing record....");
    }

    ExtendedFieldSet record     = new ExtendedFieldSet((FieldSet) data);
    String           recordCode = record.readString("recordType");
    logger.info("processing recordType.." + recordCode);

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
