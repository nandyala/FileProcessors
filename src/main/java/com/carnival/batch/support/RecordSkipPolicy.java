package com.carnival.batch.support;

import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;


/**
 * Created with IntelliJ IDEA. User: nandyala Date: 2/16/14 Time: 8:45 PM To change this template use File | Settings |
 * File Templates.
 *
 * @author   $author$
 * @version  $Revision$, $Date$
 */
public class RecordSkipPolicy implements SkipPolicy {
  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.batch.core.step.skip.SkipPolicy#shouldSkip(java.lang.Throwable, int)
   */
  @Override public boolean shouldSkip(final Throwable t, final int skipCount) throws SkipLimitExceededException {
    return true;
  }
}
