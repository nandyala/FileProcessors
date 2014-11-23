package com.emerson.batch.launch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;

import java.util.Date;

/**
 * This tasklet is a dummy tasklet and this runs indefinitely and this loads the xmls
 * required for polling and launching jobs.
 */
public class JobDaemonTasklet implements Tasklet, InitializingBean {
    //~ Static fields/initializers ---------------------------------------------------------------------------------------

    private static final Log log = LogFactory.getLog(JobDaemonTasklet.class);
    //~ Instance fields --------------------------------------------------------------------------------------------------

  @Override public void afterPropertiesSet() throws Exception { }



    @Override public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("Listener started...");

        boolean continueLoop = true;

        try {
            while (continueLoop) {
                Thread.sleep(60000);
                log.info("Listening..." + new Date());

            }
        } catch (Exception e) {
            log.error(e);
        }       // end try-catch


        return RepeatStatus.FINISHED;
    } // end method execute



} // end class JmsStartupTasklet

