package com.github.sineer.springbatchdemo1.processor.validators;

import org.springframework.batch.item.ItemProcessor;

import com.github.sineer.springbatchdemo1.domain.StockPosition;

public class StockPriceValidatingProcessor implements ItemProcessor<StockPosition, StockPosition> {
	private static final String MESSAGE = "ClientId: %s has negative stocks price for the tikcer: %s";

	@Override
	public StockPosition process(StockPosition item) throws Exception {
		if (item.getStockPrice() < 0) {
			item.setValidationStatus(Boolean.FALSE);
			item.getValidationMessages().add(
					String.format(StockPriceValidatingProcessor.MESSAGE, item.getClientId(), item.getStockTicker()));
		}
		return item;
	}
}
