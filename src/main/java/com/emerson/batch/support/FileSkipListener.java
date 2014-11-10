package com.emerson.batch.support;

import java.util.Collections;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.batch.item.file.FlatFileItemWriter;


/**
 * Created with IntelliJ IDEA. User: nandyala Date: 2/13/14 Time: 9:43 AM To change this template use File | Settings |
 * File Templates.
 *
 * @author   $author$
 * @version  $Revision$, $Date$
 */
public class FileSkipListener implements org.springframework.batch.core.SkipListener {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final Log logger = LogFactory.getLog(FileSkipListener.class);

  //~ Instance fields --------------------------------------------------------------------------------------------------

  private FlatFileItemWriter errorItemWriter = new FlatFileItemWriter();

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public FlatFileItemWriter getErrorItemWriter() {
    return errorItemWriter;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.batch.core.SkipListener#onSkipInProcess(java.lang.Object, java.lang.Throwable)
   */
  @Override public void onSkipInProcess(Object s, java.lang.Throwable throwable) {
    try {
      errorItemWriter.write(Collections.singletonList(s));
    } catch (Exception e) {
      logger.info(e);
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.batch.core.SkipListener#onSkipInRead(java.lang.Throwable)
   */
  @Override public void onSkipInRead(java.lang.Throwable throwable) {
    logger.info(throwable.getCause());
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.batch.core.SkipListener#onSkipInWrite(java.lang.Object, java.lang.Throwable)
   */
  @Override public void onSkipInWrite(Object s, java.lang.Throwable throwable) {
    logger.info(throwable.getCause());
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  errorItemWriter  DOCUMENT ME!
   */
  public void setErrorItemWriter(FlatFileItemWriter errorItemWriter) {
    this.errorItemWriter = errorItemWriter;
  }

} // end class FileSkipListener
