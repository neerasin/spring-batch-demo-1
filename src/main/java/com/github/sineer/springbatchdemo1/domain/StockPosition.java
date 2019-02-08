package com.github.sineer.springbatchdemo1.domain;

import java.util.ArrayList;
import java.util.List;

public class StockPosition {
	private Integer clientId;
	private String clientFirstName;
	private String clientLastName;

	// Calculated
	private String clientFullName;

	private String stockTicker;
	private Integer shareCount;
	private Integer stockPrice;

	// Calculated
	private Long totalHolding;

	// Calculated
	private Boolean validationStatus = Boolean.TRUE;

	// Calculated
	private final List<String> validationMessages = new ArrayList<String>();

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public String getClientFirstName() {
		return clientFirstName;
	}

	public void setClientFirstName(String clientFirstName) {
		this.clientFirstName = clientFirstName;
	}

	public String getClientLastName() {
		return clientLastName;
	}

	public void setClientLastName(String clientLastName) {
		this.clientLastName = clientLastName;
	}

	public String getClientFullName() {
		return clientFullName;
	}

	public void setClientFullName(String clientFullName) {
		this.clientFullName = clientFullName;
	}

	public String getStockTicker() {
		return stockTicker;
	}

	public void setStockTicker(String stockTicker) {
		this.stockTicker = stockTicker;
	}

	public Integer getShareCount() {
		return shareCount;
	}

	public void setShareCount(Integer shareCount) {
		this.shareCount = shareCount;
	}

	public Integer getStockPrice() {
		return stockPrice;
	}

	public void setStockPrice(Integer stockPrice) {
		this.stockPrice = stockPrice;
	}

	public Long getTotalHolding() {
		return totalHolding;
	}

	public void setTotalHolding(Long totalHolding) {
		this.totalHolding = totalHolding;
	}

	public Boolean getValidationStatus() {
		return validationStatus;
	}

	public void setValidationStatus(Boolean validationStatus) {
		this.validationStatus = validationStatus;
	}

	public List<String> getValidationMessages() {
		return validationMessages;
	}

	@Override
	public String toString() {
		return "StockPosition [clientId=" + clientId + ", clientFirstName=" + clientFirstName + ", clientLastName="
				+ clientLastName + ", clientFullName=" + clientFullName + ", stockTicker=" + stockTicker
				+ ", shareCount=" + shareCount + ", stockPrice=" + stockPrice + ", totalHolding=" + totalHolding
				+ ", validationStatus=" + validationStatus + ", validationMessages=" + validationMessages + "]";
	}

}
