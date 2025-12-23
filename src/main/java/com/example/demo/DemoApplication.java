package com.example.demo;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicClassicHttpRequest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sap.cloud.sdk.cloudplatform.connectivity.ApacheHttpClient5Accessor;
import com.sap.cloud.sdk.cloudplatform.connectivity.Destination;
import com.sap.cloud.sdk.cloudplatform.connectivity.DestinationAccessor;

@SpringBootApplication
@RestController
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@GetMapping("/call-destination")
	public String callDestinationEndpoint(@RequestParam String destinationName, @RequestParam String path)
			throws Exception {
		// Step 1: Fetch destination from BTP Destination Service
		Destination destination = DestinationAccessor.getDestination(destinationName);
		HttpClient client = ApacheHttpClient5Accessor.getHttpClient(destination);
		ClassicHttpRequest request = new BasicClassicHttpRequest("GET", path);

		return client.execute(request, response -> {
			// Get the response body as a string
			String responseBody = EntityUtils.toString(response.getEntity());
			
			// Print to console
			System.out.println("Response Status: " + response.getCode() + " " + response.getReasonPhrase());
			System.out.println("Response Body: " + responseBody);
			
			// Return the response data
			return responseBody;
		});

	}

}
