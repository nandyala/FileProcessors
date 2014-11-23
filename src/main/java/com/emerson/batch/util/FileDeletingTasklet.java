package com.emerson.batch.util;


import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/**
 * Tasklet for deleting files
 *
 * @author Jagadeesh
 */
public class FileDeletingTasklet implements Tasklet, InitializingBean {
    private static final Log log = LogFactory.getLog(FileDeletingTasklet.class);

    private Resource inputFileName;

    private Resource outPutDirName;

    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        if (inputFileName.exists()) {

            try {
                log.info("file exists and copy it to output dir");
                FileUtils.copyFileToDirectory(inputFileName.getFile(), outPutDirName.getFile());
                log.info("File copy successful");
                log.info("start remote file delete");
                FileUtils.forceDelete(inputFileName.getFile());
                log.info("finished remote file delete");
            } catch (IOException e) {
                log.error(e);
            }
        }
        else{
            log.error("files doesn't exists..");
        }
        return RepeatStatus.FINISHED;
    }


    public void afterPropertiesSet() throws Exception {
        Assert.notNull(inputFileName, "input file is not set");
        Assert.notNull(outPutDirName, "outPutDirName is not set");
    }

    public Resource getInputFileName() {
        return inputFileName;
    }

    public void setInputFileName(Resource inputFileName) {
        this.inputFileName = inputFileName;
    }

    public Resource getOutPutDirName() {
        return outPutDirName;
    }

    public void setOutPutDirName(Resource outPutDirName) {
        this.outPutDirName = outPutDirName;
    }
}
