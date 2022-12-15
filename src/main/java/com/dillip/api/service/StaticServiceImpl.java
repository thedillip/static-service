package com.dillip.api.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.mail.internet.InternetAddress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import com.dillip.api.request.ContactDetails;
import com.dillip.api.request.WeightSlipRequest;
import com.dillip.api.response.MediaFile;
import com.dillip.api.util.StaticServiceConstant;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
@Slf4j
public class StaticServiceImpl implements StaticService {
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Value("${swagger-ui.url}")
	private String swaggerUiUrl;
	@Value("${spring.mail.username}")
	private String springMailUserName;
	@Value("${spring.mail.name}")
	private String springMailName;
	
	@Override
	public MediaFile exportReport(WeightSlipRequest weightSlipRequest) throws JRException, IOException {
		log.info("########## Hitting exportReport() method in ServiceImpl Layer ##########");

		String fileName = "Weight Slip_" + weightSlipRequest.getVehicleNumber().toUpperCase() + "_" +LocalDateTime.now().toString() + ".pdf";

		List<WeightSlipRequest> list = new ArrayList<>();
		list.add(new WeightSlipRequest());

		String netWeight = String.valueOf(Integer.parseInt(weightSlipRequest.getGrossWeight())
				- Integer.parseInt(weightSlipRequest.getTareWeight()));

		ClassPathResource classPathResource = new ClassPathResource("WeightSlip.jrxml");
		JasperReport jasperReport = JasperCompileManager.compileReport(classPathResource.getInputStream());
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("address", weightSlipRequest.getAddress());
		parameters.put("vehicleNumber", weightSlipRequest.getVehicleNumber());
		parameters.put("grossWeight", weightSlipRequest.getGrossWeight());
		parameters.put("tareWeight", weightSlipRequest.getTareWeight());
		parameters.put("netWeight", netWeight);
		parameters.put("grossWeightDate", formattedDate(weightSlipRequest.getGrossWeightDate()));
		parameters.put("tareWeightDate", formattedDate(weightSlipRequest.getTareWeightDate()));
		parameters.put("grossWeightTime", formattedTime(weightSlipRequest.getGrossWeightDate()));
		parameters.put("tareWeightTime", formattedTime(weightSlipRequest.getTareWeightDate()));

		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
		byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);

		HttpHeaders headers = new HttpHeaders();

		MediaFile mediaFile = new MediaFile();
		mediaFile.setFileName(fileName);
		mediaFile.setByteData(data);

		Date date = new Date();
		headers.set(HttpHeaders.CONTENT_DISPOSITION,
				"attachment;filename=Weight Slip_" + String.valueOf(date) + ".pdf");
		log.info("########## Report Generated in PDF ......... ##########");
		return mediaFile;
	}
	
	public String formattedDate(LocalDateTime date) {
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return date.format(myFormatObj);
	}

	public String formattedTime(LocalDateTime date) {
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("hh : mm : ss  a");
		return date.format(myFormatObj).toUpperCase();
	}

	@Override
	public String sendEmail(ContactDetails contact) {
		log.info("########## Entered into sendEmail() in ServiceImpl Layer ##########");

		String emailBody = "Dear, " + contact.getName() + "\n\n"
				+ "I hope you are having a productive day.\n\nI greatly appreciate the time you spent for visiting my Portfolio.\n\n"
				+ "Thank you for sharing your valuable feedback - Keep in Touch"
				+ "\n\nNOTE: This is an auto generated mail. Please do not reply to this message or on this email address.\n\n"
				+ "Thanks & Regards \nDillip K Sahoo\nContact Number :- +91 8117941692\nMailto:- thedillip1@gmail.com\nWebsite:- https://dillipfolio.web.app";

		String subject = "Welcome to DillipFolio – Thanks for Visiting !!";
		
		log.info("########## Email Body ########## :: Email Content :: "+emailBody);

//		SimpleMailMessage message = new SimpleMailMessage();
//
//		message.setFrom("thedillip1@gmail.com");
//		message.setTo(contact.getEmail());
//		message.setText(emailBody);
//		message.setSubject(subject);
		
		MimeMessagePreparator preparator = (mimeMessage) -> {
	        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
	        helper.setFrom(new InternetAddress(springMailUserName, springMailName));
	        helper.setTo(contact.getEmail());
	        helper.setSubject(subject);
	        helper.setText(emailBody, false);
	    };   

//		mailSender.send(message);
	    mailSender.send(preparator);

		log.info("########## Mail has been send Successfully :: SUCCESS ##########");

		ContactDetails contactDetails = new ContactDetails();
		contactDetails.setName(contact.getName());
		contactDetails.setEmail(contact.getEmail());
		contactDetails.setMessage(contact.getMessage());
		contactDetails.setSubject(contact.getSubject());

		return StaticServiceConstant.SUCCESS_MSG;
	}

	@Override
	public String startReportApi() {
		log.info("########## API has been Started :: Status :: UP :: SUCCESS ##########");
		return swaggerUiUrl;
	}

}
