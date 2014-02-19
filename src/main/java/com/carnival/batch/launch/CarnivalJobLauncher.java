package com.carnival.batch.launch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * DOCUMENT ME!
 *
 * @author   $author$
 * @version  $Revision$, $Date$
 */
public class CarnivalJobLauncher {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final Log logger = LogFactory.getLog(CarnivalJobLauncher.class);

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  args  DOCUMENT ME!
   */
  public static void main(String[] args) {
    String[] springConfig = {
      "classpath*:spring/batch/config/context.xml",
      "classpath*:spring/batch/jobs/pipeFileReader.xml"
    };

    ApplicationContext context = new ClassPathXmlApplicationContext(springConfig);

    org.springframework.batch.core.launch.JobLauncher jobLauncher = (org.springframework.batch.core.launch.JobLauncher)
      context.getBean("jobLauncher");
    Job                                               job         = (Job) context.getBean("fileReader");

    try {
      JobExecution execution = jobLauncher.run(job, new JobParameters());
      logger.info("Exit Status : " + execution.getStatus());

    } catch (Exception e) {
      e.printStackTrace();
    }

    logger.info("Done");

  }
} // end class CarnivalJobLauncher
