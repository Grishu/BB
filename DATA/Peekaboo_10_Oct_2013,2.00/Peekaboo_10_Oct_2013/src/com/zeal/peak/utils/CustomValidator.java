package com.zeal.peak.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;
import android.widget.EditText;

/**
 * This class contain methods for validate inputfield value.
 * 
 */
public class CustomValidator {

	public static boolean m_isError = false;

	/**
	 * This is method to check edit text empty or not.
	 * 
	 * @param p_editText
	 *            - edittext
	 * @param p_nullMsg
	 *            -display message if edittext null
	 */
	public static void validateForEmptyValue(EditText p_editText,
			String p_nullMsg) {
		if (p_editText != null && p_nullMsg != null) {
			// use trim() while checking for blank values
			if (TextUtils.isEmpty(p_editText.getText().toString().trim())) {
				m_isError = true;
				p_editText.setError(p_nullMsg);
			}
		}
	}

	/**
	 * This is method to check null and valid emailid or not.
	 * 
	 * @param p_editText
	 *            - edittext
	 * @param p_nullMsg
	 *            -display message if edittext null
	 * @param p_validMsg
	 *            -display message if email id not valid.
	 */
	public static void validateEmail(EditText p_editText, String p_nullMsg,
			String p_validMsg) {

		if (p_editText != null && p_nullMsg != null && p_validMsg != null) {
			Pattern m_pattern = Pattern
					.compile("([\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Za-z]{2,4})");
			Matcher m_matcher = m_pattern.matcher(p_editText.getText()
					.toString().trim());
			if (!m_matcher.matches()) {
				m_isError = true;
				p_editText.setError(p_validMsg);
			}
			validateForEmptyValue(p_editText, p_nullMsg);
		}

	}

	/**
	 * // - Phone Number Validation
	 * 
	 * Method to check if the entered String is a valid phone number of
	 * specified digits Phone number length between 8 to 11
	 * 
	 * @param p_editText
	 *            -edittext
	 * @param p_nullMsg
	 *            -display message if edittext null
	 * @param p_validMsg
	 *            -display message if email id not valid.
	 */
	public static void validatePhoneNumber(EditText p_editText,
			String p_nullMsg, String p_validMsg) {
		if (p_editText != null && p_nullMsg != null && p_validMsg != null) {
			Pattern m_pattern = Pattern
					.compile("^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{2,5})$");
			Matcher m_matcher = m_pattern.matcher(p_editText.getText()
					.toString().trim());
			if (!m_matcher.matches()) {
				m_isError = true;
				p_editText.setError(p_validMsg);
			}

		}
	}
}