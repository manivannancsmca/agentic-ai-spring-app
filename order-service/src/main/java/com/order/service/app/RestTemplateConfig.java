package com.order.service.app;

import io.github.resilience4j.core.registry.EntryAddedEvent;
import io.github.resilience4j.core.registry.EntryRemovedEvent;
import io.github.resilience4j.core.registry.EntryReplacedEvent;
import io.github.resilience4j.core.registry.RegistryEventConsumer;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.order.service.app.config.RestTemplateErrorHandler;

import java.time.Duration;
import java.util.Collections;

@Slf4j
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(10000);

        return builder
                .requestFactory(() -> new BufferingClientHttpRequestFactory(factory))
                .additionalInterceptors(loggingInterceptor())
                .errorHandler(new RestTemplateErrorHandler())
                .build();
    }

    private ClientHttpRequestInterceptor loggingInterceptor() {
        return (request, body, execution) -> {
            log.debug("Outgoing Request: {} {}", request.getMethod(), request.getURI());
            return execution.execute(request, body);
        };
    }

    @Bean
    public RegistryEventConsumer<CircuitBreaker> circuitBreakerEventConsumer() {
        return new RegistryEventConsumer<>() {
            @Override public void onEntryAddedEvent(EntryAddedEvent<CircuitBreaker> event) {
                event.getAddedEntry().getEventPublisher()
                    .onStateTransition(e -> log.warn("CircuitBreaker {} state changed from {} to {}", 
                        e.getCircuitBreakerName(), e.getStateTransition().getFromState(), e.getStateTransition().getToState()));
            }
            @Override public void onEntryRemovedEvent(EntryRemovedEvent<CircuitBreaker> event) {}
            @Override public void onEntryReplacedEvent(EntryReplacedEvent<CircuitBreaker> event) {}
        };
    }

    @Bean
    public RegistryEventConsumer<Retry> retryEventConsumer() {
        return new RegistryEventConsumer<>() {
            @Override public void onEntryAddedEvent(EntryAddedEvent<Retry> event) {
                event.getAddedEntry().getEventPublisher()
                    .onRetry(e -> log.warn("Retrying {} attempt {} due to: {}", 
                        e.getName(), e.getNumberOfRetryAttempts(), e.getLastThrowable().getMessage()));
            }
            @Override public void onEntryRemovedEvent(EntryRemovedEvent<Retry> event) {}
            @Override public void onEntryReplacedEvent(EntryReplacedEvent<Retry> event) {}
        };
    }
}
