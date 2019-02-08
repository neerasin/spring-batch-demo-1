package com.github.sineer.springbatchdemo1.app;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileUrlResource;

import com.github.sineer.springbatchdemo1.domain.StockPosition;
import com.github.sineer.springbatchdemo1.processor.businessprocessors.FullNameDerivingProcessor;
import com.github.sineer.springbatchdemo1.processor.businessprocessors.TotalHoldingCalculatingProcessor;
import com.github.sineer.springbatchdemo1.processor.validators.ClientNameValidatingProcessor;
import com.github.sineer.springbatchdemo1.processor.validators.ShareCountValidatingProcessor;
import com.github.sineer.springbatchdemo1.processor.validators.StockPriceValidatingProcessor;
import com.github.sineer.springbatchdemo1.tasklet.CreateCsvFileTasklet;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
	private static final Integer ROW_COUNT = 100;
	private static final String INPUT_FILE_PATH = "E:\\\\TEMP_FILES\\\\StockPosition\\\\input.csv";
	private static final String OUTPUT_FILE_PATH = "E:\\\\TEMP_FILES\\\\StockPosition\\\\output.csv";

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Bean
	public FlatFileItemReader<StockPosition> reader() throws MalformedURLException {

		return new FlatFileItemReaderBuilder<StockPosition>()
				.name("stockPositionItemReader").resource(new FileUrlResource(BatchConfiguration.INPUT_FILE_PATH))
				.linesToSkip(1).delimited().delimiter(";").names(new String[] { "clientId", "clientFirstName",
						"clientLastName", "stockTicker", "shareCount", "stockPrice" })
				.fieldSetMapper(new BeanWrapperFieldSetMapper<StockPosition>() {
					{
						setTargetType(StockPosition.class);
					}
				}).build();
	}

	@Bean
	public CompositeItemProcessor<StockPosition, StockPosition> processor() {
		final CompositeItemProcessor<StockPosition, StockPosition> cip = new CompositeItemProcessor<StockPosition, StockPosition>();

		final List<ItemProcessor<StockPosition, StockPosition>> processors = new ArrayList<ItemProcessor<StockPosition, StockPosition>>();
		processors.add(new StockPriceValidatingProcessor());
		processors.add(new ShareCountValidatingProcessor());
		processors.add(new ClientNameValidatingProcessor());
		processors.add(new FullNameDerivingProcessor());
		processors.add(new TotalHoldingCalculatingProcessor());

		cip.setDelegates(processors);
		return cip;
	}

	@Bean
	public ItemWriter<StockPosition> writer() throws Exception {
		final DelimitedLineAggregator<StockPosition> lineAggregator = new DelimitedLineAggregator<>();
		lineAggregator.setDelimiter(",");

		final BeanWrapperFieldExtractor<StockPosition> fieldExtractor = new BeanWrapperFieldExtractor<>();
		fieldExtractor.setNames(new String[] { "clientId", "clientFirstName", "clientLastName", "clientFullName",
				"stockTicker", "shareCount", "stockPrice", "totalHolding", "validationStatus", "validationMessages" });
		lineAggregator.setFieldExtractor(fieldExtractor);

		return new FlatFileItemWriterBuilder<StockPosition>().name("csvWriterBean")
				.resource(new FileUrlResource(BatchConfiguration.OUTPUT_FILE_PATH)).lineAggregator(lineAggregator)
				.shouldDeleteIfExists(true).build();
	}

	// tag::jobstep[]
	@Bean
	public Job importUserJob(JobCompletionNotificationListener listener) throws Exception {
		return jobBuilderFactory.get("importUserJob").incrementer(new RunIdIncrementer()).listener(listener)
				.flow(createCsvFileToImportStep()).next(readAndWriteCsvFile()).end().build();
	}

	@Bean
	public Step createCsvFileToImportStep() throws Exception {
		return stepBuilderFactory.get("createCsvFileToImportStep")
				.tasklet(new CreateCsvFileTasklet(BatchConfiguration.INPUT_FILE_PATH, BatchConfiguration.ROW_COUNT))
				.build();
	}

	@Bean
	public Step readAndWriteCsvFile() throws Exception {
		return stepBuilderFactory.get("readAndWriteCsvFileStep").<StockPosition, StockPosition>chunk(10000)
				.reader(reader()).processor(processor()).writer(writer()).build();
	}
	// end::jobstep[]
}
