package com.github.sineer.springbatchdemo1.processor.businessprocessors;

import org.springframework.batch.item.ItemProcessor;

import com.github.sineer.springbatchdemo1.domain.StockPosition;

public class FullNameDerivingProcessor implements ItemProcessor<StockPosition, StockPosition> {

	@Override
	public StockPosition process(StockPosition item) throws Exception {
		if (item.getValidationStatus()) {
			item.setClientFullName(item.getClientFirstName().concat(" ").concat(item.getClientLastName()));
		}
		return item;
	}
}
