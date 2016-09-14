/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * 
 * WSO2.Telco Inc. licences this file to you under  the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.wso2telco.services.dep.sandbox.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wso2telco.core.msisdnvalidator.InvalidMSISDNException;
import com.wso2telco.core.msisdnvalidator.MSISDN;
import com.wso2telco.core.msisdnvalidator.MSISDNUtil;
import com.wso2telco.services.dep.sandbox.exception.SandboxException;
import com.wso2telco.services.dep.sandbox.exception.SandboxException.SandboxErrorType;

public class CommonUtil {

	static Log LOG = LogFactory.getLog(CommonUtil.class);
	
	private static final String MSISDN_SPLITTER = ":|\\+";

	public static void validateMsisdn(String msisdn) throws SandboxException {

		MSISDNUtil msisdnUtil = new MSISDNUtil();
		try {
			MSISDN parsedMsisdn = msisdnUtil.parse(msisdn);
		} catch (InvalidMSISDNException ex) {
			LOG.info(ex);
			String errorMessage = "Invalid MSISDN";
			throw new SandboxException(SandboxErrorType.INVALID_MSISDN);
		}

	}

	public static void validatePositiveNumber(String number, String parameterName) throws SandboxException {
		if (!StringUtils.isEmpty(number) && !NumberUtils.isDigits(number)) {
			throw new SandboxException(SandboxErrorType.INVALIN_INPUT_VALUE);
		}
	}

	public static Integer convertStringToInteger(String number) {
		Integer value = Integer.valueOf(0);

		if (!StringUtils.isEmpty(number) && NumberUtils.isDigits(number)) {
			value = Integer.valueOf(number);
		}

		return value;
	}
	
	public static String getNullOrTrimmedValue(String value) {
		String outputValue = null;

		if (value != null && value.trim().length() > 0) {
			outputValue = value.trim();
		}

		return outputValue;
	}
	
	public static String extractNumberFromMsisdn(String msisdn) {
		String phoneNumber = "";
		String[] splittedMsisdn = msisdn.trim().split(MSISDN_SPLITTER);

		if (splittedMsisdn != null && splittedMsisdn.length > 0) {
			phoneNumber = splittedMsisdn[splittedMsisdn.length - 1];
		}

		return phoneNumber;
	}

}
