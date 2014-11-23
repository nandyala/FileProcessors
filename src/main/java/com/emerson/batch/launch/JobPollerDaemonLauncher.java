package com.emerson.batch.launch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by nandyala on 11/22/14.
 */
public class JobPollerDaemonLauncher {


    private static final Log logger = LogFactory.getLog(JobPollerDaemonLauncher.class);

    //~ Methods ----------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  args  DOCUMENT ME!
     */
    public static void main(String[] args) {
        logger.info("starting loading the context for jobDaemons..");
        String[] springConfig = {
                "classpath*:spring/batch/support/applicationContext.xml",
                "classpath*:spring/batch/support/jobDaemon.xml"
        };

        ApplicationContext context = new ClassPathXmlApplicationContext(springConfig);
        logger.info("finished loading the context for jobDaemons..");
        logger.info("trying to get the jobLauncher bean for jobDaemons from context..");
        org.springframework.batch.core.launch.JobLauncher jobLauncher = (org.springframework.batch.core.launch.JobLauncher)
                context.getBean("jobLauncher");
        logger.info("trying to get the job bean jobDaemonTasklet from context..");

        Job job         = (Job) context.getBean("jobDaemonTasklet");

        try {
            logger.info("Starting the job execution...");
            JobExecution execution = jobLauncher.run(job, new JobParameters());
            logger.info("Exit Status : " + execution.getStatus());

        } catch (Exception e) {
            e.printStackTrace();
        }

        logger.info("Done");

    }
}
