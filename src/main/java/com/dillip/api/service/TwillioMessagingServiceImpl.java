package com.dillip.api.service;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.dillip.api.config.TwillioProperties;
import com.dillip.api.request.SendMessageRequest;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class TwillioMessagingServiceImpl implements TwillioMessagingService {

	private final TwillioProperties twillioProperties;

	@PostConstruct
	public void twillioInit() {
		Twilio.init(twillioProperties.getSid(), twillioProperties.getAuthToken());
	}

	@Override
	public String sendMesssage(final SendMessageRequest sendMessageRequest) {
		log.info("########## Inside TwillioMessagingServiceImpl::sendMesssage() ##########");
		Message.creator(new PhoneNumber(sendMessageRequest.getMobileNumber()),
				new PhoneNumber(twillioProperties.getNumber()), sendMessageRequest.getMessage()).create();
		return "Message Sent Successfully.";
	}

}
