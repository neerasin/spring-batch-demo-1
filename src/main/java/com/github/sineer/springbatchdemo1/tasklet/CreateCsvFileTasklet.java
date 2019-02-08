package com.github.sineer.springbatchdemo1.tasklet;

import java.io.File;

import org.hsqldb.util.CSVWriter;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import com.github.sineer.springbatchdemo1.domain.StockPosition;

public class CreateCsvFileTasklet implements Tasklet {
	private static final String GOOG = "GOOG";
	private static final String FIRST = "First";
	private static final String SECOND = "Second";

	private final String inputFilePath;
	private final Integer rowCount;

	public CreateCsvFileTasklet(String inputFilePath, Integer rowCount) {
		super();
		this.inputFilePath = inputFilePath;
		this.rowCount = rowCount;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		final CSVWriter writer = new CSVWriter(new File(inputFilePath), null);
		writer.writeHeader(new String[] { "clientId", "clientFirstName", "clientLastName", "stockTicker", "shareCount",
				"stockPrice" });
		StockPosition position = null;
		for (int i = 1; i <= rowCount; i++) {
			position = new StockPosition();
			position.setClientId(i);
			position.setClientFirstName(CreateCsvFileTasklet.FIRST + i);
			position.setClientLastName(CreateCsvFileTasklet.SECOND + i);
			position.setStockTicker(CreateCsvFileTasklet.GOOG);
			if ((i % 5) == 0) {
				position.setShareCount(i * -1);
			} else {
				position.setShareCount(i);
			}
			position.setStockPrice(1);

			writer.writeData(new String[] { position.getClientId().toString(), position.getClientFirstName(),
					position.getClientLastName(), position.getStockTicker(), position.getShareCount().toString(),
					position.getStockPrice().toString() });
		}
		writer.close();
		return RepeatStatus.FINISHED;
	}

}