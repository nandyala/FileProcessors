/**
 * 
 */
package com.emerson.batch.util;

import org.apache.log4j.Logger;

import java.io.File;
import java.util.Random;

/**
 * @author ozhurakousky
 *
 */
public class FileProcessor {
	private Random random = new Random();
	private Logger logger = Logger.getLogger(FileProcessor.class);

	public File process(File file) throws Exception{	
		Thread.sleep(random.nextInt(10)*500);
		logger.info("Processing File: " + file);
		return file;
	}
}
