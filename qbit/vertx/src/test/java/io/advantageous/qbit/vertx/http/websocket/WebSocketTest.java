package io.advantageous.qbit.vertx.http.websocket;

import io.advantageous.boon.core.Sys;
import io.advantageous.qbit.http.client.HttpClient;
import io.advantageous.qbit.http.client.HttpClientBuilder;
import io.advantageous.qbit.http.server.HttpServer;
import io.advantageous.qbit.http.server.HttpServerBuilder;
import io.advantageous.qbit.http.server.websocket.WebSocketMessage;
import io.advantageous.qbit.http.websocket.WebSocket;
import io.advantageous.qbit.util.PortUtils;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import static org.junit.Assert.assertEquals;

public class WebSocketTest {


    @Test
    public void testText() throws Exception{

        final int port = PortUtils.findOpenPortStartAt(4000);
        final HttpServer httpServer = HttpServerBuilder.httpServerBuilder().setPort(port).build();
        final AtomicReference<Object> bodyRef = new AtomicReference<>();
        final AtomicReference<String> messageRef = new AtomicReference<>();

        final CountDownLatch countDownLatch = new CountDownLatch(2);
        httpServer.setWebSocketMessageConsumer(webSocketMessage -> {
            bodyRef.set(webSocketMessage.body());
            webSocketMessage.getSender().sendText("world");
            countDownLatch.countDown();
        });

        httpServer.startServer();
        Sys.sleep(2_000);


        final HttpClient httpClient = HttpClientBuilder.httpClientBuilder().setPort(port).buildAndStart();
        Sys.sleep(1_000);

        final WebSocket webSocket = httpClient.createWebSocket("/foo");



        webSocket.setTextMessageConsumer(message -> {

            messageRef.set(message);
            countDownLatch.countDown();
        });

        webSocket.openAndWait();

        webSocket.sendText("hello");
        Sys.sleep(1_000);

        countDownLatch.await(5, TimeUnit.SECONDS);


        assertEquals("world", messageRef.get());
        assertEquals("hello", bodyRef.get().toString());



    }


    @Test
    public void testBinary() throws Exception{

        final int port = PortUtils.findOpenPortStartAt(4001);
        final HttpServer httpServer = HttpServerBuilder.httpServerBuilder().setPort(port).build();
        final AtomicReference<Object> bodyRef = new AtomicReference<>();
        final AtomicReference<byte[]> messageRef = new AtomicReference<>();

        final CountDownLatch countDownLatch = new CountDownLatch(2);
        httpServer.setWebSocketMessageConsumer(webSocketMessage -> {
            bodyRef.set(webSocketMessage.body());
            webSocketMessage.getSender().sendBytes("world".getBytes());
            countDownLatch.countDown();
        });

        httpServer.startServer();
        Sys.sleep(2_000);


        final HttpClient httpClient = HttpClientBuilder.httpClientBuilder().setPort(port).buildAndStart();
        Sys.sleep(1_000);

        final WebSocket webSocket = httpClient.createWebSocket("/foo");



        webSocket.setBinaryMessageConsumer(message -> {

            messageRef.set(message);
            countDownLatch.countDown();
        });

        webSocket.openAndWait();

        webSocket.sendBinary("hello".getBytes());
        Sys.sleep(1_000);

        countDownLatch.await(5, TimeUnit.SECONDS);


        assertEquals("world", new String(messageRef.get(), StandardCharsets.UTF_8));
        assertEquals("hello", new String(((byte[]) bodyRef.get()), StandardCharsets.UTF_8));



    }

}