package com.example.demo.config;

import java.util.Map;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


// Job 參數傳遞
// 參數格式: key = value
// 參數是誰可以得到 Step
// 如何得到: 實作 StepExecutionListener
@Configuration
public class JobConfig_6_params implements StepExecutionListener {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	private Map<String, JobParameter> params;
	
	@Bean
	public Step paramStep() {
		return stepBuilderFactory.get("ParamStep")
				.listener(this) // 加入監聽
				.tasklet((contribution, chunkContext) -> {
					// 輸出接收到的參數內容
					System.out.println(params);
					System.out.println(params.get("info"));
					return RepeatStatus.FINISHED;
				})
				.allowStartIfComplete(true)
				.build();
	}
	
	@Bean
	public Job paramJob() {
		return jobBuilderFactory.get("ParamJob")
				.start(paramStep())
				.build();
	}
	
	@Override
	public void beforeStep(StepExecution stepExecution) {
		System.out.println("beforeStep");
		// Run As > Run Configurations > Args 裡面設定 info=HelloBatch > 按下 Run
		// 因為 param 在資料表中是放在 String_VAL 欄位是 unique, 所以每一次執行需要帶入不同參數
		params = stepExecution.getJobParameters().getParameters();
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
