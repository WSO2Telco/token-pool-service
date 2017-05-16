/*******************************************************************************
 * Copyright (c) 2015-2017, WSO2.Telco Inc. (http://www.wso2telco.com)
 *
 * All Rights Reserved. WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
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
package com.wso2telco.dep.tpservice.manager;


import com.wso2telco.dep.tpservice.conf.ConfigReader;
import com.wso2telco.dep.tpservice.dao.WhoDAO;
import com.wso2telco.dep.tpservice.model.EmailDTO;
import com.wso2telco.dep.tpservice.model.TLSMailConfigDTO;
import com.wso2telco.dep.tpservice.model.WhoDTO;
import com.wso2telco.dep.tpservice.util.exception.BusinessException;
import com.wso2telco.dep.tpservice.util.exception.GenaralError;
import com.wso2telco.dep.tpservice.util.exception.TokenException;
import com.wso2telco.dep.tpservice.util.exception.TokenException.TokenError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public  class EmailManager {

    protected EmailDTO emailDTO;
    public ConfigReader configReader ;
    public WhoDAO whoDAO;
    private Session session ;
    private String mailFrom;
    
  private   EmailManager(){
    	TLSMailConfigDTO tLSMailConfigDTO  = configReader.getInstance().getConfigDTO().getTLSMailConfigDTO();
    	mailFrom = tLSMailConfigDTO.getFrom();

    	whoDAO = new WhoDAO();
    	
    	Properties props = new Properties();
    	props.put("mail.smtp.auth", tLSMailConfigDTO.isAuth());
    	props.put("mail.smtp.starttls.enable",tLSMailConfigDTO.isStarttlsEnable());
    	props.put("mail.smtp.host", tLSMailConfigDTO.getHost());
    	props.put("mail.smtp.port", tLSMailConfigDTO.getPort());

    	  session = Session.getInstance(props,
    	  new javax.mail.Authenticator() {
    		protected PasswordAuthentication getPasswordAuthentication() {
    			return new PasswordAuthentication(tLSMailConfigDTO.getUsername(), tLSMailConfigDTO.getPassword());
    		}
    	  });
    }
   
    // Thread local variable containing each thread's ID
    private static final ThreadLocal<EmailManager> threadId =
        new ThreadLocal<EmailManager>() {
            @Override protected EmailManager initialValue() {
                return new EmailManager();
        }
    };

    // Returns the current thread's unique ID, assigning it if necessary
    
	public static EmailManager getInstance(){
		return threadId.get();
	} 
	
	
    private static Logger log = LoggerFactory.getLogger(EmailManager.class);
    
    /**
     * this will send Connection fail  mail to pre-configured recipients 
     * @param tokenOwner
     * @throws BusinessException
     */
    public void sendConnectionFailNotification(WhoDTO tokenOwner,String subject,String msg, TokenException e) throws BusinessException{
    	
    	try {
    		
    		List<InternetAddress> senderList = new ArrayList<>();
    		
    		List<EmailDTO> rowEmailDTOList = whoDAO.getEmailAddress(tokenOwner.getId());
    		
    		if(rowEmailDTOList==null || rowEmailDTOList.isEmpty()){
    			log.error("error occurs because of empty email list "+TokenError.EMPTY_SENDER_LIST); ;
    			return;
    		}
    		
    		for (EmailDTO emailDTO : rowEmailDTOList) {
    			senderList.add(  new InternetAddress(emailDTO.getEmailAddress()) );
			}
    		
    		
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(mailFrom));
			
			message.setRecipients(Message.RecipientType.TO, senderList.toArray( new InternetAddress [senderList.size()]));
			message.setSubject(subject);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			String content = new String(baos.toByteArray(), StandardCharsets.UTF_8);
			
			message.setText(msg
				+ "\n\n "+ content);

			Transport.send(message);

		} catch (MessagingException eMessagingException) {

    	    log.error("Error occoured when sending emails "+GenaralError.INTERNAL_SERVER_ERROR,eMessagingException);
			return;
		}

    }
}