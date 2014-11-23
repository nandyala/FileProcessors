package com.emerson.batch.launch;

import com.emerson.batch.util.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.integration.launch.JobLaunchRequest;
import org.springframework.batch.integration.launch.JobLaunchRequestHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FileMessageToJobRequest implements ApplicationContextAware {
    private Job job;
    private String fileParameterName;
    protected ApplicationContext applicationContext;
    private Map<String, String> jobNamesMap = new HashMap<String, String>();
    private static final Log log = LogFactory.getLog(FileMessageToJobRequest.class);

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public Map<String, String> getJobNamesMap() {
        return jobNamesMap;
    }

    public void setJobNamesMap(Map<String, String> jobNamesMap) {
        this.jobNamesMap = jobNamesMap;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void setFileParameterName(String fileParameterName) {
        this.fileParameterName = fileParameterName;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    @Transformer
    public JobLaunchRequest toRequest(Message<File> message) {
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();

        jobParametersBuilder.addString(fileParameterName,
                message.getPayload().getAbsolutePath());
        JobParameters jobParameters = jobParametersBuilder.toJobParameters();
        String fileNameWithPath = jobParameters.getString(fileParameterName);
        log.debug("Processing file::.." + fileNameWithPath);
        String fileName = fileNameWithPath.substring(fileNameWithPath.lastIndexOf('/') + 1);
        fileName = fileName.substring(0, fileName.lastIndexOf('.'));
        log.debug("working on fileName..."+fileName);
        String jobXmlName=null;
        for (Map.Entry<String, String> e : jobNamesMap.entrySet()) {
            if (fileName.startsWith(e.getKey())){
                log.debug("found matching name...");
                jobXmlName=jobNamesMap.get(e.getKey());
                log.debug("loading jobXml.."+jobXmlName);
                break;
            }
        }
        log.info("launching the job ..."+jobXmlName);
        if(!StringUtils.hasText(jobXmlName)){
          log.error("couldn't find a valid job bean based on the file naming...");
            return null;
        }
        Job job = applicationContext.getBean(jobXmlName, Job.class);

        return new JobLaunchRequest(job, jobParameters);
    }

}