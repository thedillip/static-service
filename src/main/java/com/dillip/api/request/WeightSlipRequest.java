package com.dillip.api.request;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class WeightSlipRequest {
	private String address;
	private String vehicleNumber;
	private String grossWeight;
	private String tareWeight;
	private LocalDateTime grossWeightDate;
	private LocalDateTime tareWeightDate;
	@JsonIgnore
	private String message;
	private boolean checked;
	
	public WeightSlipRequest() {
		super();
		this.message = "Thank You  !  Visit Again";
	}
}
