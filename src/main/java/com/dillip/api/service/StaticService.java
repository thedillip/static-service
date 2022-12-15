package com.dillip.api.service;

import java.io.IOException;

import com.dillip.api.request.ContactDetails;
import com.dillip.api.request.WeightSlipRequest;
import com.dillip.api.response.MediaFile;

import net.sf.jasperreports.engine.JRException;

public interface StaticService {

	MediaFile exportReport(WeightSlipRequest weightSlipRequest) throws JRException, IOException;

	String sendEmail(ContactDetails contact);

	String startReportApi();

}
