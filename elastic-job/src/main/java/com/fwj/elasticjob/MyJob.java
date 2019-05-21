package com.fwj.elasticjob;

import com.dangdang.elasticjob.lite.annotation.ElasticSimpleJob;

import org.springframework.stereotype.Component;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

@ElasticSimpleJob(cron="* * * * * ?",jobName="test123",shardingTotalCount=2,jobParameter="测试参数",shardingItemParameters="0=A,1=B")
@Component
public class MyJob implements SimpleJob{

    @Override
    public void execute(ShardingContext content) {
        //do something
        System.out.println("JobName:"+content.getJobName());
        System.out.println("JobParameter:"+content.getJobParameter());
        System.out.println("ShardingItem:"+content.getShardingItem());
        System.out.println("ShardingParameter:"+content.getShardingParameter());
        System.out.println("ShardingTotalCount:"+content.getShardingTotalCount());
        System.out.println("TaskId:"+content.getTaskId());
        System.out.println("---------------------------------------");

    }
}