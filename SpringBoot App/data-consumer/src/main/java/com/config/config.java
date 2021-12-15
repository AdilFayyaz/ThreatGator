package com.config;

import com.model.RedditThread;
import com.model.redditKafka;
import com.model.redditKafkaC;
import com.model.tweetKafka;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class config {
    @Bean
    public ConsumerFactory<String, redditKafka>
    redditThreadConsumer()
    {
        // HashMap to store the configurations
        Map<String, Object> map = new HashMap<>();

        // put the host IP in the map
        map.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");

        // put the group ID of consumer in the map
        map.put(ConsumerConfig.GROUP_ID_CONFIG, "id");
        map.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        map.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(map, new StringDeserializer(), new JsonDeserializer<>(redditKafka.class));
    }

    @Bean
    public ConsumerFactory<String, redditKafkaC>
    redditCommentsConsumer()
    {
        // HashMap to store the configurations
        Map<String, Object> map = new HashMap<>();

        // put the host IP in the map
        map.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");

        // put the group ID of consumer in the map
        map.put(ConsumerConfig.GROUP_ID_CONFIG, "id");
        map.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        map.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(map, new StringDeserializer(), new JsonDeserializer<>(redditKafkaC.class));
    }

    @Bean
    public ConsumerFactory<String, tweetKafka>
    tweetConsumer()
    {
        // HashMap to store the configurations
        Map<String, Object> map = new HashMap<>();

        // put the host IP in the map
        map.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");

        // put the group ID of consumer in the map
        map.put(ConsumerConfig.GROUP_ID_CONFIG, "id");
        map.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        map.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(map, new StringDeserializer(), new JsonDeserializer<>(tweetKafka.class));
    }



    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, redditKafka>
    RedditThreadListener()
    {
        ConcurrentKafkaListenerContainerFactory<String, redditKafka> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(redditThreadConsumer());
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, redditKafkaC>
    RedditCommentListener()
    {
        ConcurrentKafkaListenerContainerFactory<String, redditKafkaC> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(redditCommentsConsumer());
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, tweetKafka>
    tweetListener()
    {
        ConcurrentKafkaListenerContainerFactory<String, tweetKafka> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(tweetConsumer());
        return factory;
    }
}
