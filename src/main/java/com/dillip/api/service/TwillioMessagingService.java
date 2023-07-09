package com.dillip.api.service;

import com.dillip.api.request.SendMessageRequest;

public interface TwillioMessagingService {
	String sendMesssage(SendMessageRequest sendMessageRequest);
}
