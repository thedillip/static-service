package com.dillip.api.component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dillip.api.request.WeightSlipRequest;
import com.dillip.api.response.MediaFile;
import com.dillip.api.service.StaticService;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;

@Component
@Slf4j
public class JobSchedulerComponent {

	@Autowired
	private StaticService staticService;

	HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2)
			.connectTimeout(Duration.ofSeconds(30)).build();

	@Scheduled(cron = "0 0/15 * * * *")
	public void callRenderStartApi() throws IOException, InterruptedException, JRException {
		HttpRequest httpRequest = HttpRequest.newBuilder().header("Content-Type", "application/json").GET()
				.uri(URI.create("https://restapi-production-d89e.up.railway.app/")).build();

		HttpResponse httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
		Object body = httpResponse.body();
		WeightSlipRequest weightSlipRequest = WeightSlipRequest.builder().address("BOUDH").vehicleNumber("OD02AB8329")
				.grossWeight("40000").tareWeight("20000").grossWeightDate(LocalDateTime.now())
				.tareWeightDate(LocalDateTime.now()).checked(false).build();
		MediaFile exportReport = staticService.exportReport(weightSlipRequest);
		log.info("########## Inside JobSchedulerComponent::callRenderStartApi :: CRON Job ##########");
		log.info("########## Response = {} ##########", body);
		log.info("Export Report Response = {}", exportReport);
	}
}
