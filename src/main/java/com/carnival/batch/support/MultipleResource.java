package com.carnival.batch.support;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.net.URI;
import java.net.URL;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import org.springframework.util.StringUtils;


/**
 * Created with IntelliJ IDEA. User: nandyala Date: 2/12/14 Time: 8:45 PM To change this template use File | Settings |
 * File Templates.
 *
 * @author   $author$
 * @version  $Revision$, $Date$
 */
public class MultipleResource implements Resource, Iterator<Resource>, InitializingBean {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** Current resource. */
  protected Resource currentResource;

  /** iterator for the resources. */
  protected Iterator<Resource> dataIterator = null;

  /** list of all data path with ant pattern. */
  protected String[] dataPaths;

  /** list of all data resource. */
  protected List<Resource> dataResources = new ArrayList<Resource>();

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
   */
  @Override public void afterPropertiesSet() throws Exception {
    // look through all paths
    for (String path : dataPaths) {
      if (StringUtils.hasText(path)) {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

        try {
          Resource[] configResources = resourcePatternResolver.getResources(path);

          for (Resource resource : configResources) {
            dataResources.add(resource);
          }
        } catch (Exception e) {
          // Just skip the invalid path
        }
      }
    }

    dataIterator = dataResources.iterator();

    if (hasNext()) {
      next();
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.core.io.Resource#contentLength()
   */
  @Override public long contentLength() throws IOException {
    return 0;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.core.io.Resource#createRelative(java.lang.String)
   */
  @Override public Resource createRelative(String relativePath) throws IOException {
    return this.currentResource.createRelative(relativePath);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.core.io.Resource#exists()
   */
  @Override public boolean exists() {
    if (this.currentResource != null) {
      return this.currentResource.exists();
    } else {
      return false;
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * The currentResource.
   *
   * @return  the currentResource
   */
  public Resource getCurrentResource() {
    return this.currentResource;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.core.io.Resource#getDescription()
   */
  @Override public String getDescription() {
    return this.currentResource.getDescription();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.core.io.Resource#getFile()
   */
  @Override public File getFile() throws IOException {
    return this.currentResource.getFile();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.core.io.Resource#getFilename()
   */
  @Override public String getFilename() {
    return this.currentResource.getFilename();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.core.io.InputStreamSource#getInputStream()
   */
  @Override public InputStream getInputStream() throws IOException {
    return this.currentResource.getInputStream();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.core.io.Resource#getURI()
   */
  @Override public URI getURI() throws IOException {
    return this.currentResource.getURI();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.core.io.Resource#getURL()
   */
  @Override public URL getURL() throws IOException {
    return this.currentResource.getURL();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  java.util.Iterator#hasNext()
   */
  @Override public boolean hasNext() {
    return dataIterator.hasNext();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.core.io.Resource#isOpen()
   */
  @Override public boolean isOpen() {
    return this.currentResource.isOpen();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.core.io.Resource#isReadable()
   */
  @Override public boolean isReadable() {
    // TODO Auto-generated method stub
    return true;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.core.io.Resource#lastModified()
   */
  @Override public long lastModified() throws IOException {
    // TODO Auto-generated method stub
    return 0;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  java.util.Iterator#next()
   */
  @Override public Resource next() {
    currentResource = dataIterator.next();

    return currentResource;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  java.util.Iterator#remove()
   */
  @Override public void remove() {
    dataIterator.remove();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  dataPaths  the dataPaths to set
   */
  public void setDataPaths(String[] dataPaths) {
    this.dataPaths = dataPaths;
  }

} // end class MultipleResource
