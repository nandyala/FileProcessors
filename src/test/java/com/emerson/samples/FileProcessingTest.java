/**
 *
 */
package com.emerson.samples;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.messaging.PollableChannel;

import java.io.File;

/**
 * @author Oleg Zhurakousky
 *
 */
public class FileProcessingTest {
	private int fileCount = 4;
	private Logger logger = Logger.getLogger(FileProcessingTest.class);

	@Before
	public void createDirectory(){
		File directory = new File("input");
		if (directory.exists()){
			directory.delete();
		}
		directory.mkdir();
	}

	@Test
	public void testSequentialFileProcessing() throws Exception {
		logger.info("\n\n#### Starting Sequential processing test ####");
/*		logger.info("Populating directory with files");
		for (int i = 0; i < fileCount; i++) {
			File file = new File("input/file_" + i + ".txt");
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
		    out.write("hello " + i);
		    out.close();
		}
		logger.info("Populated directory with files");
		Thread.sleep(2000);*/
		logger.info("Starting Spring Integration Sequential File processing");
		ConfigurableApplicationContext ac =
				new ClassPathXmlApplicationContext("classpath*:spring/batch/support/filePollerJob.xml");
		PollableChannel filesOutChannel = ac.getBean("filesOutChannel", PollableChannel.class);
		for (int i = 0; i < fileCount; i++) {
			logger.info("Finished processing " + filesOutChannel.receive(10000).getPayload());
		}
		ac.stop();
	}

}
