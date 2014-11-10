package com.emerson.batch.launch;

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
public class EmersonJobLauncher {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final Log logger = LogFactory.getLog(EmersonJobLauncher.class);

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  args  DOCUMENT ME!
   */
  public static void main(String[] args) {
    String[] springConfig = {
      "classpath*:spring/batch/config/context.xml",
      "classpath*:spring/batch/jobs/csvFileReader.xml"
    };

    ApplicationContext context = new ClassPathXmlApplicationContext(springConfig);

    org.springframework.batch.core.launch.JobLauncher jobLauncher = (org.springframework.batch.core.launch.JobLauncher)
      context.getBean("jobLauncher");
    Job                                               job         = (Job) context.getBean("csvFileReader");

    try {
      JobExecution execution = jobLauncher.run(job, new JobParameters());
      logger.info("Exit Status : " + execution.getStatus());

    } catch (Exception e) {
      e.printStackTrace();
    }

    logger.info("Done");

  }
} // end class EmersonJobLauncher
