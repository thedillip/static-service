package com.dillip.api.component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JobSchedulerComponent {

	HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2)
			.connectTimeout(Duration.ofSeconds(30)).build();

	@Scheduled(cron = "0 0/15 * * * *")
	public void callRenderStartApi() throws IOException, InterruptedException {
		HttpRequest httpRequest = HttpRequest.newBuilder().header("Content-Type", "application/json").GET()
				.uri(URI.create("https://restapi-production-d89e.up.railway.app/")).build();

		HttpResponse httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
		Object body = httpResponse.body();
		log.info("########## Inside JobSchedulerComponent::callRenderStartApi :: CRON Job ##########");
		log.info("########## Response = {} ##########", body);
	}
}
