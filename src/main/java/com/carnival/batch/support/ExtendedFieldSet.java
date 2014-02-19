package com.carnival.batch.support;

import java.io.UnsupportedEncodingException;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.util.Date;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.batch.item.file.transform.FieldSet;

import org.springframework.util.StringUtils;


/**
 * DOCUMENT ME!
 *
 * @author   $author$
 * @version  $Revision$, $Date$
 */
public class ExtendedFieldSet implements FieldSet {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final Log    log   = LogFactory.getLog(ExtendedFieldSet.class);
  private static final String MINUS = "-";

  /** DOCUMENT ME! */
  public static String DATE_PATTERN = "yyyyMMdd";

  /** DOCUMENT ME! */
  public static String        EU_DATE_PATTERN = "ddMMyyyy";
  private static final String SIGN            = "Sign";
  private static final String PLUS            = "+";

  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** DOCUMENT ME! */
  protected final FieldSet fieldSet;

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new ExtendedFieldSet object.
   *
   * @param  fieldSet  DOCUMENT ME!
   */
  public ExtendedFieldSet(FieldSet fieldSet) {
    this.fieldSet = fieldSet;
  }

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   candidate        DOCUMENT ME!
   * @param   hasDecimalPoint  DOCUMENT ME!
   * @param   scale            DOCUMENT ME!
   * @param   defaultValue     DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  IllegalArgumentException  DOCUMENT ME!
   */
  public BigDecimal doReadBigDecimal(String candidate, boolean hasDecimalPoint,
    int scale, BigDecimal defaultValue) {
    try {
      if (!StringUtils.hasText(candidate)) {
        return defaultValue;
      }

      // Get rid of white spaces
      candidate = StringUtils.trimAllWhitespace(candidate);

      // Remove leading "0"
      candidate = candidate.replaceAll("^0+", "");

      // Remove ","
      candidate = candidate.replace(",", "");

      // Check decimal point
      hasDecimalPoint = candidate.contains(".");

      if (candidate.startsWith("-")) {
        return new BigDecimal(candidate);
      }

      // Added by Etisbew To handle Negative Decimal for DFS Job on 05th Feb
      // 2009
      String tempString = candidate;

      if (candidate.endsWith("-")) {
        candidate = candidate.substring(0, candidate.length() - 1);
      }

      int len = candidate.length();

      if (len == 0) {
        return defaultValue;
      }

      if (candidate.endsWith(".")) {
        candidate       = candidate.replace(".", "");
        hasDecimalPoint = false;
        len             = candidate.length();

        if (!StringUtils.hasText(candidate)) {
          return defaultValue;
        }
      }

      String ibmCode = decodeIBMNumber(candidate.charAt(len - 1));

      // Added by Etisbew To handle Negative Decimal for DFS Job on 05th Feb
      // 2009
      String signPart = null;

      if (tempString.endsWith("-")) {
        signPart = "-";
      } else {
        signPart = Character.toString(ibmCode.charAt(0));
      }
      // Commented by Etisbew on 05th Feb 2009
      // String signPart = Character.toString(ibmCode.charAt(0));

      // Last digit: IBM safe
      String lastDigit = Character.toString(ibmCode.charAt(1));

      if (hasDecimalPoint) {
        String numberPart1 = candidate.substring(0, len - 1);

        return new BigDecimal(signPart + numberPart1 + lastDigit);
      }

      if ((scale < 0) || (scale > len)) {
        // invalid scale and no decimal point
        String numberPart1 = candidate.substring(0, len - 1);

        if (PLUS.equalsIgnoreCase(signPart)) {
          signPart = "";
        }

        if (scale < 0) {
          scale = 0;
        }

        return new BigDecimal(new BigInteger(signPart + numberPart1 + lastDigit), scale);
      }

      String     intPart      = candidate.substring(0, len - scale); // integer part
      String     decimalPart1 = candidate.substring(len - scale, len - 1);
      BigDecimal ret          = new BigDecimal(signPart + intPart + "." + decimalPart1
          + lastDigit);

      // if (ret.compareTo(new BigDecimal("0")) == 0)
      // return null;
      // else
      return ret;

    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Unparseable number: " + candidate);
    } // end try-catch
  }   // end method doReadBigDecimal

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   value         DOCUMENT ME!
   * @param   defaultValue  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public Integer doReadInteger(String value, Integer defaultValue) {
    try {
      if (StringUtils.hasText(value)) {
        return Integer.parseInt(value);
      } else {
        return defaultValue;
      }
    } catch (Exception e) {
      return defaultValue;
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   value         DOCUMENT ME!
   * @param   defaultValue  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public Long doReadLong(String value, Long defaultValue) {
    try {
      if (StringUtils.hasText(value)) {
        return Long.parseLong(value);
      } else {
        return defaultValue;
      }
    } catch (Exception e) {
      return defaultValue;
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   candidate        DOCUMENT ME!
   * @param   hasDecimalPoint  DOCUMENT ME!
   * @param   scale            DOCUMENT ME!
   * @param   defaultValue     DOCUMENT ME!
   * @param   isSignPart       DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  IllegalArgumentException  DOCUMENT ME!
   */
  public BigDecimal doReadSignedBigDecimal(String candidate, boolean hasDecimalPoint,
    int scale, BigDecimal defaultValue, boolean isSignPart) {
    try {
      if (!StringUtils.hasText(candidate)) {
        return defaultValue;
      }

      // Get rid of white spaces
      candidate = candidate.trim();

      // Remove leading "0"
      candidate = candidate.replaceAll("^0+", "");

      // Remove ","
      candidate = candidate.replace(",", "");

      // Check decimal point
      hasDecimalPoint = candidate.contains(".");

      if ("-".equals(candidate) || "+".equals(candidate)) {
        return BigDecimal.ZERO;
      }

// if (candidate.startsWith("-")) {
// return new BigDecimal(candidate);
// }
      // Added by Etisbew To handle Negative Decimal for DFS Job on 05th Feb
      // 2009
      String tempString = candidate;
      int    startPos   = 0;

      if (candidate.endsWith("-")) {
        candidate = candidate.substring(0, candidate.length() - 1);
      }

      int len = candidate.length();

      if (len == 0) {
        return defaultValue;
      }

      if (candidate.endsWith(".")) {
        candidate       = candidate.replace(".", "");
        hasDecimalPoint = false;
        len             = candidate.length();

        if (!StringUtils.hasText(candidate)) {
          return defaultValue;
        }
      }

      String ibmCode = decodeIBMNumber(candidate.charAt(len - 1));

      // Added by Etisbew To handle Negative Decimal for DFS Job on 05th Feb
      // 2009
      String signPart = null;

      if (tempString.endsWith("-") || tempString.startsWith("-")) {
        signPart = "-";
      } else {
        signPart = Character.toString(ibmCode.charAt(0));
      }

      if (isSignPart) {
        startPos = 1;
      }
      // Commented by Etisbew on 05th Feb 2009
      // String signPart = Character.toString(ibmCode.charAt(0));

      // Last digit: IBM safe
      String lastDigit = Character.toString(ibmCode.charAt(1));

      if (hasDecimalPoint) {
        String numberPart1 = candidate.substring(startPos, len - 1);

        return new BigDecimal(signPart + numberPart1 + lastDigit);
      }

      if ((scale < 0) || (scale > len)) {
        // invalid scale and no decimal point
        String numberPart1 = candidate.substring(startPos, len - 1);

        if (PLUS.equalsIgnoreCase(signPart)) {
          signPart = "";
        }

        if (scale < 0) {
          scale = 0;
        }

        return new BigDecimal(new BigInteger(signPart + numberPart1 + lastDigit), scale);
      }

      String     intPart      = candidate.substring(startPos, len - scale); // integer part
      String     decimalPart1 = candidate.substring(len - scale, len - 1);
      BigDecimal ret          = new BigDecimal(signPart + intPart + "." + decimalPart1
          + lastDigit);

      // if (ret.compareTo(new BigDecimal("0")) == 0)
      // return null;
      // else
      return ret;

    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Unparseable number: " + candidate);
    } // end try-catch
  }   // end method doReadSignedBigDecimal

  //~ ------------------------------------------------------------------------------------------------------------------

  // end method doReadSignedBigDecimal

  // Delegate


  /**
   * @see  org.springframework.batch.item.file.transform.FieldSet#getFieldCount()
   */
  @Override public int getFieldCount() {
    return fieldSet.getFieldCount();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.batch.item.file.transform.FieldSet#getNames()
   */
  @Override public String[] getNames() {
    return fieldSet.getNames();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.batch.item.file.transform.FieldSet#getProperties()
   */
  @Override public Properties getProperties() {
    return fieldSet.getProperties();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   recordName  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public Integer getSignedIntValue(String recordName) {
    String  sign  = readAndValidateString(recordName + SIGN);
    Integer value = readInt(recordName);

    if (MINUS.equalsIgnoreCase(sign)) {
      value = -1 * value;
    }

    return value;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   recordName  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public BigDecimal getSignedValue(String recordName) {
    String sign = readAndValidateString(recordName + SIGN);

    if (log.isDebugEnabled()) {
      log.debug(recordName + SIGN + " :: " + sign);
    }

    BigDecimal value = readBigDecimal(recordName);

    if (log.isDebugEnabled()) {
      log.debug(recordName + " :: " + value);
    }

    if (value != null) {
      if (MINUS.equalsIgnoreCase(sign)) {
        value = value.negate();
      }
    } else {
      value = new BigDecimal(0.0);
    }

    return value;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   recordName  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public BigDecimal getSignedValueWithoutScale(String recordName) {
    String sign = readAndValidateString(recordName + SIGN);

    if (log.isDebugEnabled()) {
      log.debug(recordName + SIGN + " :: " + sign);
    }

    BigDecimal defaultValue = new BigDecimal(0.0);
    BigDecimal value        = readBigDecimal(recordName, false, -1, defaultValue);

    if (log.isDebugEnabled()) {
      log.debug(recordName + " :: " + value);
    }

    if (value != null) {
      if (MINUS.equalsIgnoreCase(sign)) {
        value = value.negate();
      }
    } else {
      value = new BigDecimal(0.0);
    }

    return value;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.batch.item.file.transform.FieldSet#getValues()
   */
  @Override public String[] getValues() {
    return fieldSet.getValues();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.batch.item.file.transform.FieldSet#hasNames()
   */
  @Override public boolean hasNames() {
    // TODO Auto-generated method stub
    return false;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * This method is to remove the invalid characters for MySQL.
   *
   * @param   name  DOCUMENT ME!
   *
   * @return  this method is to remove the invalid characters for MySQL.
   */
  public String readAndValidatePhoneNumberString(String name) {
    String value = stripInvalidChar(fieldSet.readString(name));

    if (StringUtils.hasText(value)) {
      Long longVal = Long.valueOf(value);

      if (longVal != null) {
        if (longVal.equals(0L)) {
          return null;
        } else {
          return longVal.toString();
        }
      }
    }

    return value;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * This method is to remove the invalid characters for MySQL.
   *
   * @param   name  DOCUMENT ME!
   *
   * @return  this method is to remove the invalid characters for MySQL.
   */
  public String readAndValidateString(String name) {
    return stripInvalidChar(fieldSet.readString(name));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Read the {@link java.math.BigDecimal} value at index '<code>index</code>'.
   *
   * @param   index  the field index.
   *
   * @return  read the {@link java.math.BigDecimal} value at index '<code>index</code>'.
   *
   * @throws  IndexOutOfBoundsException  if the index is out of bounds.
   */
  @Override public BigDecimal readBigDecimal(int index) {
    return readBigDecimal(index, null);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Read the {@link java.math.BigDecimal} value from column with given ' <code>name</code>.
   *
   * @param   name  the field name.
   *
   * @return  read the {@link java.math.BigDecimal} value from column with given ' <code>name</code>.
   *
   * @throws  IllegalArgumentException  if a column with given name is not defined.
   */
  @Override public BigDecimal readBigDecimal(String name) {
    return readBigDecimal(name, null);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   index            DOCUMENT ME!
   * @param   hasDecimalPoint  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public BigDecimal readBigDecimal(int index, boolean hasDecimalPoint) {
    return doReadBigDecimal(readString(index), hasDecimalPoint, 2, null);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   name             DOCUMENT ME!
   * @param   hasDecimalPoint  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public BigDecimal readBigDecimal(String name, boolean hasDecimalPoint) {
    return doReadBigDecimal(readString(name), hasDecimalPoint, 2, null);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Read the {@link java.math.BigDecimal} value at index '<code>index</code>', returning the supplied <code>
   * defaultValue</code> if the trimmed string value at index'<code>index</code>' is blank.
   *
   * @param   index         the field index.
   * @param   defaultValue  DOCUMENT ME!
   *
   * @return  read the {@link java.math.BigDecimal} value at index '<code>index</code>', returning the supplied <code>
   *          defaultValue</code> if the trimmed string value at index'<code>index</code>' is blank.
   *
   * @throws  IndexOutOfBoundsException  if the index is out of bounds.
   */
  @Override public BigDecimal readBigDecimal(int index, BigDecimal defaultValue) {
    return readBigDecimal(index, false, 2, defaultValue);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Read the {@link java.math.BigDecimal} value from column with given '<code>name</code> , returning the supplied
   * <code>defaultValue</code> if the trimmed string value at index '<code>index</code>' is blank.
   *
   * @param   name          the field name.
   * @param   defaultValue  DOCUMENT ME!
   *
   * @return  read the {@link java.math.BigDecimal} value from column with given '<code>name</code> , returning the
   *          supplied <code>defaultValue</code> if the trimmed string value at index '<code>index</code>' is blank.
   *
   * @throws  IllegalArgumentException  if a column with given name is not defined.
   */
  @Override public BigDecimal readBigDecimal(String name, BigDecimal defaultValue) {
    return readBigDecimal(name, false, 2, defaultValue);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   index            DOCUMENT ME!
   * @param   hasDecimalPoint  DOCUMENT ME!
   * @param   defaultValue     DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public BigDecimal readBigDecimal(int index, boolean hasDecimalPoint,
    BigDecimal defaultValue) {
    return doReadBigDecimal(readString(index), hasDecimalPoint, 2, defaultValue);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   name             DOCUMENT ME!
   * @param   hasDecimalPoint  DOCUMENT ME!
   * @param   defaultValue     DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public BigDecimal readBigDecimal(String name, boolean hasDecimalPoint,
    BigDecimal defaultValue) {
    return doReadBigDecimal(readString(name), hasDecimalPoint, 2, defaultValue);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  // Added to parse SP records
  /**
   * DOCUMENT ME!
   *
   * @param   index            DOCUMENT ME!
   * @param   hasDecimalPoint  DOCUMENT ME!
   * @param   signPart         DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public BigDecimal readBigDecimal(int index, boolean hasDecimalPoint, boolean signPart) {
    return doReadSignedBigDecimal(readString(index), hasDecimalPoint, 2, null, true);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Read the {@link java.math.BigDecimal} value at index '<code>index</code>', returning the supplied <code>
   * defaultValue</code> if the trimmed string value at index'<code>index</code>' is blank.
   *
   * @param   index            the field index.
   * @param   hasDecimalPoint  DOCUMENT ME!
   * @param   scale            DOCUMENT ME!
   * @param   defaultValue     DOCUMENT ME!
   *
   * @return  read the {@link java.math.BigDecimal} value at index '<code>index</code>', returning the supplied <code>
   *          defaultValue</code> if the trimmed string value at index'<code>index</code>' is blank.
   *
   * @throws  IndexOutOfBoundsException  if the index is out of bounds.
   */
  public BigDecimal readBigDecimal(int index, boolean hasDecimalPoint,
    int scale, BigDecimal defaultValue) {
    return doReadBigDecimal(readString(index), hasDecimalPoint, scale,
        defaultValue);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   name             DOCUMENT ME!
   * @param   hasDecimalPoint  DOCUMENT ME!
   * @param   scale            DOCUMENT ME!
   * @param   defaultValue     DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public BigDecimal readBigDecimal(String name, boolean hasDecimalPoint,
    int scale, BigDecimal defaultValue) {
    return doReadBigDecimal(readString(name), hasDecimalPoint, scale,
        defaultValue);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.batch.item.file.transform.FieldSet#readBoolean(int)
   */
  @Override public boolean readBoolean(int index) {
    return fieldSet.readBoolean(index);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.batch.item.file.transform.FieldSet#readBoolean(String)
   */
  @Override public boolean readBoolean(String name) {
    return fieldSet.readBoolean(name);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.batch.item.file.transform.FieldSet#readBoolean(int, String)
   */
  @Override public boolean readBoolean(int index, String trueValue) {
    return fieldSet.readBoolean(index, trueValue);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.batch.item.file.transform.FieldSet#readBoolean(String, String)
   */
  @Override public boolean readBoolean(String name, String trueValue) {
    return fieldSet.readBoolean(name, trueValue);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.batch.item.file.transform.FieldSet#readByte(int)
   */
  @Override public byte readByte(int index) {
    return fieldSet.readByte(index);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.batch.item.file.transform.FieldSet#readByte(String)
   */
  @Override public byte readByte(String name) {
    return fieldSet.readByte(name);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.batch.item.file.transform.FieldSet#readChar(int)
   */
  @Override public char readChar(int index) {
    return fieldSet.readChar(index);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.batch.item.file.transform.FieldSet#readChar(String)
   */
  @Override public char readChar(String name) {
    return fieldSet.readChar(name);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Read the <code>java.util.Date</code> value in default format at designated column <code>index</code>.
   *
   * @param   index  the field index.
   *
   * @return  read the <code>java.util.Date</code> value in default format at designated column <code>index</code>.
   *
   * @throws  IndexOutOfBoundsException  if the index is out of bounds.
   */
  @Override public Date readDate(int index) {
    return readDate(index, DATE_PATTERN);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Read the <code>java.sql.Date</code> value in given format from column with given <code>name</code>.
   *
   * @param   name  the field name.
   *
   * @return  read the <code>java.sql.Date</code> value in given format from column with given <code>name</code>.
   *
   * @throws  IllegalArgumentException  if a column with given name is not defined.
   */
  @Override public Date readDate(String name) {
    return readDate(name, DATE_PATTERN);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Override to handle all zero date pattern. Return <b>null</b> for all zero date.
   *
   * @param   name     DOCUMENT ME!
   * @param   pattern  DOCUMENT ME!
   *
   * @return  override to handle all zero date pattern.
   */
  @Override public Date readDate(String name, String pattern) {
    String candidate = readString(name);

    if (isAllZerosOrNoDigits(candidate)) {
      return null;
    }

    try {
      return fieldSet.readDate(name, pattern);
    } catch (Exception e) {
      log.error("invalid date..." + readString(name));

      return null;
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.batch.item.file.transform.FieldSet#readDate(int, java.lang.String)
   */
  @Override public Date readDate(int index, String pattern) {
    return readDate(fieldSet.readString(index), pattern);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   index         DOCUMENT ME!
   * @param   defaultValue  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @Override public Date readDate(int index, Date defaultValue) {
    try {
      return readDate(index);
    } catch (Exception e) {
      return null;
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   name          DOCUMENT ME!
   * @param   defaultValue  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @Override public Date readDate(String name, Date defaultValue) {
    try {
      return readDate(name);
    } catch (Exception e) {
      return null;
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   name          DOCUMENT ME!
   * @param   pattern       DOCUMENT ME!
   * @param   defaultValue  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @Override public Date readDate(String name, String pattern, Date defaultValue) {
    try {
      return readDate(name, pattern);
    } catch (Exception e) {
      return null;
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   index         DOCUMENT ME!
   * @param   pattern       DOCUMENT ME!
   * @param   defaultValue  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @Override public Date readDate(int index, String pattern, Date defaultValue) {
    try {
      return readDate(index, pattern);
    } catch (Exception e) {
      return null;
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.batch.item.file.transform.FieldSet#readDouble(int)
   */
  @Override public double readDouble(int index) {
    return fieldSet.readDouble(index);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.batch.item.file.transform.FieldSet#readDouble(java.lang.String)
   */
  @Override public double readDouble(String name) {
    return fieldSet.readDouble(name);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.batch.item.file.transform.FieldSet#readFloat(int)
   */
  @Override public float readFloat(int index) {
    return fieldSet.readFloat(index);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.batch.item.file.transform.FieldSet#readFloat(java.lang.String)
   */
  @Override public float readFloat(String name) {
    return fieldSet.readFloat(name);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.batch.item.file.transform.FieldSet#readInt(int)
   */
  @Override public int readInt(int index) {
    return fieldSet.readInt(index);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.batch.item.file.transform.FieldSet#readInt(java.lang.String)
   */
  @Override public int readInt(String name) {
    return fieldSet.readInt(name);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.batch.item.file.transform.FieldSet#readInt(int, int)
   */
  @Override public int readInt(int index, int defaultValue) {
    return fieldSet.readInt(index, defaultValue);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.batch.item.file.transform.FieldSet#readInt(java.lang.String, int)
   */
  @Override public int readInt(String name, int defaultValue) {
    return fieldSet.readInt(name, defaultValue);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   index         DOCUMENT ME!
   * @param   defaultValue  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public Integer readInt(int index, Integer defaultValue) {
    return doReadInteger(readString(index), defaultValue);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   name          DOCUMENT ME!
   * @param   defaultValue  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public Integer readInt(String name, Integer defaultValue) {
    return doReadInteger(readString(name), defaultValue);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.batch.item.file.transform.FieldSet#readLong(int)
   */
  @Override public long readLong(int index) {
    return fieldSet.readLong(index);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.batch.item.file.transform.FieldSet#readLong(java.lang.String)
   */
  @Override public long readLong(String name) {
    return fieldSet.readLong(name);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.batch.item.file.transform.FieldSet#readLong(int, long)
   */
  @Override public long readLong(int index, long defaultValue) {
    return fieldSet.readLong(index, defaultValue);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.batch.item.file.transform.FieldSet#readLong(java.lang.String, long)
   */
  @Override public long readLong(String name, long defaultValue) {
    return fieldSet.readLong(name, defaultValue);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   index         DOCUMENT ME!
   * @param   defaultValue  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public Long readLong(int index, Long defaultValue) {
    return doReadLong(readString(index), defaultValue);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   name          DOCUMENT ME!
   * @param   defaultValue  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public Long readLong(String name, Long defaultValue) {
    return doReadLong(readString(name), defaultValue);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.batch.item.file.transform.FieldSet#readRawString(int)
   */
  @Override public String readRawString(int index) {
    return fieldSet.readRawString(index);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.batch.item.file.transform.FieldSet#readRawString(java.lang.String)
   */
  @Override public String readRawString(String name) {
    return fieldSet.readRawString(name);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.batch.item.file.transform.FieldSet#readShort(int)
   */
  @Override public short readShort(int index) {
    return fieldSet.readShort(index);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.batch.item.file.transform.FieldSet#readShort(java.lang.String)
   */
  @Override public short readShort(String name) {
    return fieldSet.readShort(name);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.batch.item.file.transform.FieldSet#readString(int)
   */
  @Override public String readString(int index) {
    return fieldSet.readString(index);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.batch.item.file.transform.FieldSet#readString(java.lang.String)
   */
  @Override public String readString(String name) {
    return fieldSet.readString(name);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * A decoded string containing a sign and a digit.
   *
   * @param   c  DOCUMENT ME!
   *
   * @return  a decoded string containing a sign and a digit.
   *
   * @throws  IllegalArgumentException  DOCUMENT ME!
   */
  protected String decodeIBMNumber(char c) throws IllegalArgumentException {
    char ibmChar = Character.toUpperCase(c);

    if (ibmChar == '{') {
      return "+0";
    }

    if (('A' <= ibmChar) && (ibmChar <= 'I')) { // 'A' - 'I'
      return "+" + Integer.toString(ibmChar - 'A' + 1);
    }

    if (ibmChar == '}') {
      return "-0";
    }

    if (('J' <= ibmChar) && (ibmChar <= 'R')) { // 'J' - 'R'
      return "-" + Integer.toString(ibmChar - 'J' + 1);
    }

    if (Character.isDigit(ibmChar)) {
      return "+" + Character.toString(ibmChar);
    } else {
      throw new IllegalArgumentException("Unparseable number: "
        + Character.toString(ibmChar));
    }
  } // end method decodeIBMNumber

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   candidate  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  protected boolean isAllZerosOrNoDigits(String candidate) {
    // replace all non digits characters to empty
    candidate = candidate.replaceAll("[^0-9]", "");

    if (!StringUtils.hasText(candidate)) {
      return true;
    }

    Long val = 0L;

    try {
      val = Long.valueOf(candidate);
    } catch (Exception e) {
      // Should be all digits
      return false;
    }

    // all 0
    return val.compareTo(0L) == 0;

  }

  //~ ------------------------------------------------------------------------------------------------------------------

  private String stripInvalidChar(String in) {
    byte[] utf = new byte[] {};

    try {
      utf = in.getBytes("UTF-8");

      int len = utf.length;

      for (int i = 0; i < len; i++) {
        int ii = utf[i];

        if (ii < 0) {
          utf[i] = 32;
        }
      }
    } catch (UnsupportedEncodingException c) {
      return "";
    }

    return new String(utf);
  }
} // end class ExtendedFieldSet
