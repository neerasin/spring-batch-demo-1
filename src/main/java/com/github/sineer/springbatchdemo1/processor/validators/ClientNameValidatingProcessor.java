package com.github.sineer.springbatchdemo1.processor.validators;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.util.StringUtils;

import com.github.sineer.springbatchdemo1.domain.StockPosition;

public class ClientNameValidatingProcessor implements ItemProcessor<StockPosition, StockPosition> {
	private static final String MESSAGE = "ClientId: %s has either missing first name or last name for the ticker: %s";

	@Override
	public StockPosition process(StockPosition item) throws Exception {
		if (StringUtils.isEmpty(item.getClientFirstName()) || StringUtils.isEmpty(item.getClientLastName())) {
			item.setValidationStatus(Boolean.FALSE);
			item.getValidationMessages().add(
					String.format(ClientNameValidatingProcessor.MESSAGE, item.getClientId(), item.getStockTicker()));
		}
		return item;
	}
}
