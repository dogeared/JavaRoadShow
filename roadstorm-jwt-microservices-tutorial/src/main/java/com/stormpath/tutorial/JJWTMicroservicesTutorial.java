package com.stormpath.tutorial;

import com.stormpath.tutorial.service.SpringBootKafkaConsumer;
import kafka.admin.RackAwareMode;
import kafka.zk.AdminZkClient;
import kafka.zk.KafkaZkClient;
import org.apache.kafka.common.errors.TopicExistsException;
import org.apache.kafka.common.utils.Time;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Bean;
import scala.Option;

import java.util.Properties;

@SpringBootApplication
public class JJWTMicroservicesTutorial {

    @Value("${topic}")
    private String topic;

    @Value("${zookeeper.address}")
    private String zookeeperAddress;

    private static final Logger log = LoggerFactory.getLogger(JJWTMicroservicesTutorial.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(JJWTMicroservicesTutorial.class, args);

        boolean shouldConsume = context
            .getEnvironment()
            .getProperty("kafka.consumer.enabled", Boolean.class, Boolean.FALSE);


        if (shouldConsume && context.containsBean("springBootKafkaConsumer")) {
            SpringBootKafkaConsumer springBootKafkaConsumer =
                context.getBean("springBootKafkaConsumer", SpringBootKafkaConsumer.class);

            springBootKafkaConsumer.consume();
        }
    }

    @Bean
    @ConditionalOnProperty(name = "kafka.enabled", matchIfMissing = true)
    public TopicCreator topicCreator() {
        return new TopicCreator(this.topic, this.zookeeperAddress);
    }

    private static class TopicCreator implements SmartLifecycle {

        private final String topic;

        private final String zkAddress;

        private volatile boolean running;

        public TopicCreator(String topic, String zkAddress) {
            this.topic = topic;
            this.zkAddress = zkAddress;
        }

        @Override
        public void start() {
            KafkaZkClient zkClient = KafkaZkClient.apply(
                this.zkAddress, false, 6000, 6000,
                10, Time.SYSTEM, "myGroup", "myType", Option.apply("myName")
            );
            AdminZkClient adminZkClient = new AdminZkClient(zkClient);
//            ZkUtils zkUtils = new ZkUtils(
//                new ZkClient(this.zkAddress, 6000, 6000, ZKStringSerializer$.MODULE$), null, false
//            );
            try {
//                AdminUtils.createTopic(zkUtils, topic, 1, 1, new Properties());
                adminZkClient.createTopic(
                    topic, 1, 1, new Properties(), RackAwareMode.Disabled$.MODULE$
                );
            } catch (TopicExistsException e) {
                log.info("Topic: {} already exists.", topic);
            }
            this.running = true;
        }

        @Override
        public void stop() {}

        @Override
        public boolean isRunning() {
            return this.running;
        }

        @Override
        public int getPhase() {
            return Integer.MIN_VALUE;
        }

        @Override
        public boolean isAutoStartup() {
            return true;
        }

        @Override
        public void stop(Runnable callback) {
            callback.run();
        }

    }
}
