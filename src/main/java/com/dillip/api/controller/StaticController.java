package com.dillip.api.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dillip.api.request.ContactDetails;
import com.dillip.api.request.SendMessageRequest;
import com.dillip.api.request.WeightSlipRequest;
import com.dillip.api.response.ApiEntity;
import com.dillip.api.response.ApiResponseObject;
import com.dillip.api.response.MediaFile;
import com.dillip.api.service.StaticService;
import com.dillip.api.service.TwillioMessagingService;
import com.dillip.api.util.StaticServiceConstant;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;

@RestController
@CrossOrigin
@Slf4j
public class StaticController {

	@Autowired
	private StaticService staticService;
	@Autowired
	private HttpServletResponse response;
	@Autowired
	private TwillioMessagingService twillioMessagingService;

	@Value("${render.base.key}")
	private String key;

	@Value("${stripe.success.url}")
	private String successUrl;

	@Value("${stripe.cancel.url}")
	private String cancelUrl;

	@Operation(summary = "Welcome Message")
	@GetMapping(path = "/")
	public ResponseEntity<ApiResponseObject> startApi() {
		HttpStatus status = null;
		HttpHeaders httpHeaders = new HttpHeaders();
		String message = null;
		String reMessage = null;
		try {
			reMessage = staticService.startReportApi();

			if (reMessage != null) {
				message = "Server is UP";
				status = HttpStatus.OK;
				log.info("########## API is UP ##########");
			} else {
				message = StaticServiceConstant.ERR_MSG;
				status = HttpStatus.BAD_REQUEST;
			}
		} catch (Exception e) {
			log.info("########## Exception Occured ########## " + e);
			message = e.getMessage();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<>(new ApiEntity<String>(message, reMessage), httpHeaders, status);
	}

	@Operation(summary = "Download Weight Slip in PDF")
	@PostMapping(path = "/weightslip")
	public ResponseEntity<ApiResponseObject> generateReport(
			@Parameter(name = "in_weightSlipRequest", description = "WeightSlipRequest", required = true) @RequestBody WeightSlipRequest weightSlipRequest)
			throws JRException, IOException {
		HttpStatus status = null;
		HttpHeaders httpHeaders = new HttpHeaders();
		String message = null;
		MediaFile response = null;
		log.info("########## Hitting generateReport() in Controller ########### :: WeightSlipRequest :: "
				+ weightSlipRequest);
		try {
			response = staticService.exportReport(weightSlipRequest);

			if (response != null) {
				message = "Weight Slip has been generated Successfully";
				status = HttpStatus.OK;
			} else {
				message = StaticServiceConstant.ERR_MSG;
				status = HttpStatus.BAD_REQUEST;
			}
		} catch (Exception e) {
			log.info("########## Exception Occured ########## " + e);
			message = e.getMessage();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<>(new ApiEntity<MediaFile>(message, response), httpHeaders, status);
	}

	@Operation(summary = "Send Email")
	@PostMapping(path = "/send-email")
	public ResponseEntity<ApiResponseObject> sendEmail(@RequestBody ContactDetails contact) {
		HttpStatus status = null;
		HttpHeaders httpHeaders = new HttpHeaders();
		String message = null;
		String response = null;
		log.info("########## Hitting sendEmail() in Controller Layer :: ContactDetails :: " + contact);
		try {
			response = staticService.sendEmail(contact);

			if (response.equals(StaticServiceConstant.SUCCESS_MSG)) {
				message = "Thank You ! Your feedback has been Submitted.";
				status = HttpStatus.OK;
			} else {
				message = "Sorry ! An Error Occured While Sending Your Message.";
				status = HttpStatus.BAD_REQUEST;
			}
		} catch (Exception e) {
			log.info("########## Exception Occured sendEmail() in Controller Layer ########## " + e);
			message = e.getMessage();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<>(new ApiEntity<String>(message, response), httpHeaders, status);
	}

	@Operation(summary = "Download Weight Slip in excel ")
	@PostMapping(path = "/excel")
	public void generateReportInExcel(
			@Parameter(name = "in_weightSlipRequest", description = "WeightSlipRequest", required = true) @RequestBody WeightSlipRequest weightSlipRequest)
			throws JRException, IOException {

		staticService.getDocument(weightSlipRequest, response);

//		HttpStatus status = null;
//		HttpHeaders httpHeaders = new HttpHeaders();
//		String message = null;
//		MediaFile response = null;
//		log.info("########## Hitting generateReport() in Controller ########### :: WeightSlipRequest :: "
//				+ weightSlipRequest);
//		try {
//			response = staticService.exportReport(weightSlipRequest);
//
//			if (response != null) {
//				message = "Weight Slip has been generated Successfully";
//				status = HttpStatus.OK;
//			} else {
//				message = StaticServiceConstant.ERR_MSG;
//				status = HttpStatus.BAD_REQUEST;
//			}
//		} catch (Exception e) {
//			log.info("########## Exception Occured ########## " + e);
//			message = e.getMessage();
//			status = HttpStatus.INTERNAL_SERVER_ERROR;
//		}
//		return new ResponseEntity<>(new ApiEntity<MediaFile>(message, response), httpHeaders, status);
	}

	@Operation(summary = "Send SMS to Mobile Number")
	@PostMapping(path = "/send-message")
	public ResponseEntity<ApiResponseObject> sendMeassage(@RequestBody final SendMessageRequest sendMessageRequest) {

		HttpStatus status = null;
		HttpHeaders httpHeaders = new HttpHeaders();
		String message = null;
		String response = null;
		log.info("############# Hitting /send-message API in Controller Layer ###############");
		try {
			response = twillioMessagingService.sendMesssage(sendMessageRequest);
			status = HttpStatus.CREATED;
		} catch (Exception e) {
			log.info("############# Exception Occured in /send-message in Controller Layer ##########" + e);
			message = e.getMessage();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		return new ResponseEntity<>(new ApiEntity<>(message, response), httpHeaders, status);
	}

	@GetMapping(value = "/test")
	public ResponseEntity<String> test() {
		log.info("########## Key = {} ##########", key);
		return ResponseEntity.ok(key);
	}

	@PostMapping(value = "/stripe/create-checkout-session")
	public Map<String, String> createCheckoutSession(@RequestBody Map<String, Object> data) throws StripeException {
		Long amount = Long.parseLong(data.get("amount").toString()); // in cents (e.g., 5000 = $50)

		SessionCreateParams params = SessionCreateParams.builder().setMode(SessionCreateParams.Mode.PAYMENT)
				.setSuccessUrl(successUrl + "?session_id={CHECKOUT_SESSION_ID}").setCancelUrl(
						cancelUrl)
				.addLineItem(
						SessionCreateParams.LineItem.builder().setQuantity(1L)
								.setPriceData(
										SessionCreateParams.LineItem.PriceData.builder().setCurrency("usd")
												.setUnitAmount(amount)
												.setProductData(SessionCreateParams.LineItem.PriceData.ProductData
														.builder().setName("Sample Product").build())
												.build())
								.build())
				.build();

		Session session = Session.create(params);

		Map<String, String> response = new HashMap<>();
		response.put("checkoutUrl", session.getUrl());
		return response;
	}

	@GetMapping(value = "/payment/success")
	public String paymentSuccess(@RequestParam(name = "session_id") String sessionId) {
		return "Payment Success! Session ID: " + sessionId;
	}

	@GetMapping(value = "/payment/cancel")
	public String paymentCancel() {
		return "Payment Canceled!";
	}

	@PostMapping(value = "/webhook/stripe-events")
	public ResponseEntity<String> handleWebhook(@RequestBody String payload) {
		log.info("Received raw Stripe event: {}", payload);
		return ResponseEntity.ok("Webhook received");
	}
}