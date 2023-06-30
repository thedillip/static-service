package com.dillip.api.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.dillip.api.request.ContactDetails;
import com.dillip.api.request.WeightSlipRequest;
import com.dillip.api.response.ApiEntity;
import com.dillip.api.response.ApiResponseObject;
import com.dillip.api.response.MediaFile;
import com.dillip.api.service.StaticService;
import com.dillip.api.util.StaticServiceConstant;
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
}
