package com.skillbox.cryptobot.client;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BinanceClient {
    private final HttpGet httpGet;
    private final ObjectMapper mapper;
    private final HttpClient httpClient;

    private final CacheManager cacheManager;



    public BinanceClient(@Value("${binance.api.getPrice}") String uri, CacheManager cacheManager) {
        httpGet = new HttpGet(uri);
        mapper = new ObjectMapper();
        httpClient = HttpClientBuilder.create()
                .setSSLHostnameVerifier(new NoopHostnameVerifier())

                .build();
        this.cacheManager = cacheManager;
    }


    public double getBitcoinPrice() throws IOException {
        evictMyCache();
        log.info("Performing client call to binanceApi to get bitcoin price");
        try {
            return mapper.readTree(EntityUtils.toString(httpClient.execute(httpGet).getEntity()))
                    .path("price").asDouble();
        } catch (IOException e) {
            log.error("Error while getting price from binance", e);
            throw e;
        }
    }

    @CacheEvict(value = "myCache", allEntries = true, condition = "#result != null")
    public void evictMyCache() {
        // Метод для очистки кэша, который не имеет аргументов, но требует обработки
        Objects.requireNonNull(cacheManager.getCache("myCache")).clear();
    }
}
