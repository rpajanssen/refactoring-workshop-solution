package com.abnamro.nl.channels.geninfo.bankmail.util;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * ErrorChecker<BR>
 * Error handler class for XML validation for DOM Parser
 * 
 */
public class ErrorChecker extends DefaultHandler {

  /** Variable indicating if error occure in XML validation. */
  private boolean validationError = false;

  /** Exception thrown back in case of error in validating XML file. */
  private SAXParseException saxParseException = null;

  /**
   * handle for error in validating XML.
   * 
   * @param exception the exception
   * @throws SAXException the SAX exception
   */
  @Override
  public void error(SAXParseException exception) throws SAXException {
    validationError = true;
    saxParseException = exception;
    throw saxParseException;
  }

  /**
   * Handler for fatal errros occuring in validating XML file.
   * 
   * @param exception the exception
   * @throws SAXException the SAX exception
   */
  @Override
  public void fatalError(SAXParseException exception) throws SAXException {
    validationError = true;
    saxParseException = exception;
    throw saxParseException;
  }

  /**
   * Handler for warning occuring in validating XML file.
   * 
   * @param exception the exception
   * @throws SAXException the SAX exception
   */
  @Override
  public void warning(SAXParseException exception) throws SAXException {
    validationError = true;
    saxParseException = exception;
    throw saxParseException;
  }

  /**
   * Checks if is XML valid.
   * 
   * @return true, if is XML valid
   */
  public boolean isXMLValid() {
    return validationError;
  }
}
