package com.abnamro.nl.channels.geninfo.bankmail.util;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.html.HTMLEditorKit;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * ClientTagValidator: Parse the Client HTML content. 
 * 
 * <PRE>
 * &lt;B&gt;History:&lt;/B&gt;
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS			  	01-11-2012	Initial version	  Release 1.0
 * </PRE>
 * 
 * @author
 * @see
 */
public class ClientTagValidator extends HTMLEditorKit.ParserCallback {

	/** The is valid. */
	private boolean isValid = true;

	/** The content. */
	private String content;

	/** The parser. */
	private HTMLEditorKit.Parser parser;

	/** The implied tag list. */
	private static final List<Tag> IMPLIED_TAG_LIST = Arrays.asList(Tag.HTML, Tag.HEAD,
			Tag.BODY);
	/** The allowed tag list. */
	private static final List<Tag> ALLOWED_TAG_LIST = Arrays.asList(Tag.P, Tag.BR);

	/** The closing tag optional. */
	private static final List<Tag> ONLY_CLOSINGTAG_ALLOWED = Arrays.asList(Tag.BR);

	/**  List of not allowed strings. */
	private static final List<String> LIST_DISALLOWED_XML_STRINGS = Arrays.asList(new String[]{"<!doctype", "<!element", "<!attlist", "<!entity", "<!notation", "<!--", "<![cdata["});
	
	/**
	 * Instantiates a new tag validator.
	 * 
	 * @param content
	 *            the content
	 */
	public ClientTagValidator(String content) {
		IMPLIED_TAG_LIST.toArray(new Tag[IMPLIED_TAG_LIST.size()]);
		this.content = content;
		ParserGetter kit = new ParserGetter();
		parser = kit.getParser();
	}

	/**
	 * Handle start tag.
	 * 
	 * @param t
	 *            the t
	 * @param a
	 *            the a
	 * @param pos
	 *            the pos
	 */
	@Override
	public void handleStartTag(Tag t, MutableAttributeSet a, int pos) {
		if (!isImplied(t, a, pos)) {
			if (!ALLOWED_TAG_LIST.contains(t)) {
				isValid = false;
			}else{
				if(a != null && a.getAttributeCount() > 0){
					
					isValid = false;
				}
			}
		}
	}

	/**
	 * Handle error.
	 * 
	 * @param errorMsg
	 *            the error msg
	 * @param pos
	 *            the pos
	 */
	@Override
	public void handleError(String errorMsg, int pos) {
		String endTagError = "unmatched.endtag";
		if ((errorMsg != null) && (errorMsg.contains(endTagError))) {
			int end = errorMsg.indexOf("?");
			if (end != -1) {
				String tagName = errorMsg.substring(endTagError.length(), end);
				Tag tag = HTML.getTag(tagName);
				if ((tag != null) && (!ONLY_CLOSINGTAG_ALLOWED.contains(tag))) {
					isValid = false;
				}
			}
		}
		super.handleError(errorMsg, pos);
	}

	/**
	 * Handle end tag.
	 * 
	 * @param t
	 *            the t
	 * @param pos
	 *            the pos
	 */
	@Override
	public void handleEndTag(Tag t, int pos) {
		if ((!ALLOWED_TAG_LIST.contains(t) || isImplied(t, null, pos)) && !IMPLIED_TAG_LIST.contains(t)) {
			isValid = false;
		}
	}

	/**
	 * Handle simple tag.
	 * 
	 * @param t
	 *            the t
	 * @param a
	 *            the a
	 * @param pos
	 *            the pos
	 */
	@Override
	public void handleSimpleTag(Tag t, MutableAttributeSet a, int pos) {
		if (!isImplied(t, a, pos) && !ALLOWED_TAG_LIST.contains(t)) {
			isValid = false;
		}
	}

	/**
	 * Flag to indicate if the content is valid.
	 * 
	 * @return true, if is valid
	 * @throws IOException
	 *             IOException
	 */
	public boolean isValid() throws IOException {

		if(containsText(content)){
			return false;
		}
		
		if (containsTag(Tag.HTML) || containsTag(Tag.BODY)
				|| containsTag(Tag.HEAD)) {
			return false;
		}

		StringReader reader = new StringReader(content);
		parser.parse(reader, this, false);
		reader.close();
		return this.isValid;
	}


	/**
	 * Contains text.
	 *
	 * @param content the content
	 * @return true, if successful
	 */
	private boolean containsText(String content) {
		String lcContent = content.toLowerCase(Locale.ENGLISH);
		boolean containsDisAllowedString = false;
		for(String disAllowedString : LIST_DISALLOWED_XML_STRINGS) {
			if(lcContent.contains(disAllowedString)){
				containsDisAllowedString = true;
				break;
			}
		}
		return containsDisAllowedString;
	}

	/**
	 * Contains tag.
	 * 
	 * @param tag the tag
	 * @return true, if successful
	 */
	private boolean containsTag(Tag tag) {
		String lcContent = content.toLowerCase(Locale.ENGLISH);
		return lcContent.contains("<" + tag.toString() + ">")
				|| lcContent.contains("<" + tag.toString() + "/>");

	}

	/**
	 * Checks if is implied.
	 * 
	 * @param t
	 *            the t
	 * @param a
	 *            the a
	 * @param pos
	 *            the pos
	 * @return true, if is implied
	 */
	private boolean isImplied(Tag t, MutableAttributeSet a, int pos) {

		if (a != null) {
			Object isImplied = a
					.getAttribute(HTMLEditorKit.ParserCallback.IMPLIED);
			if (isImplied != null) {
				return ((Boolean) isImplied).booleanValue();
			}
		} else {
			if ((pos == 0 || pos == content.length() - 1)) {
				return true;
			}
		}
		return false;
	}

}

