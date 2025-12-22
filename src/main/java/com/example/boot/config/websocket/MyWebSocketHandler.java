package com.example.boot.config.websocket;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Scanner;

/**
 * 自定义WebSocket处理器
 *
 * @author ljx
 * @version 1.0.0
 * @create 2025/12/18 17:07
 */
public class MyWebSocketHandler extends TextWebSocketHandler {

    private WebSocketSession session;
    private final Scanner scanner = new Scanner(System.in);
    private boolean running = true;

    private boolean firstConnection = true;

    private final Queue<String> messageQueue = new ArrayDeque<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 连接建立后
        this.session = session;
        session.sendMessage(new TextMessage("连接成功"));

        // 检查缓存的消息并发送
        while (!messageQueue.isEmpty()) {
            String message = messageQueue.poll();
            session.sendMessage(new TextMessage("缓存消息: " + message));
            System.out.println("已发送缓存消息到客户端: " + message);
        }

        // 启动一个线程监听控制台输入
        if (firstConnection) {
            startConsoleListener();
            firstConnection = false;
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        // 处理前端发送的消息
        String payload = message.getPayload();
        System.out.println("收到消息: " + payload);
    }

    @Override
    public void afterConnectionClosed(@NotNull WebSocketSession session, org.springframework.web.socket.CloseStatus closeStatus) throws Exception {
        System.out.println("连接已关闭，状态码: " + closeStatus.getCode());
        // 清空当前session
        this.session = null;
    }

    /**
     * 启动控制台监听器
     */
    private void startConsoleListener() {
        Thread consoleThread = new Thread(() -> {
            System.out.println("WebSocket连接已建立，可以通过控制台输入消息发送给客户端");
            System.out.println("输入 'exit' 退出控制台监听");

            while (running) {
                try {
                    if (scanner.hasNextLine()) {
                        String input = scanner.nextLine();
                        if ("exit".equals(input)) {
                            running = false;
                            System.out.println("退出控制台监听");
                            break;
                        }

                        // 检查session是否存在且开放
                        if (session != null && session.isOpen() && !input.isEmpty()) {
                            session.sendMessage(new TextMessage("控制台消息: " + input));
                            System.out.println("已发送消息到客户端: " + input);
                        } else if (!input.isEmpty()) {
                            messageQueue.offer(input);
                            System.out.println("WebSocket连接不可用，先缓存消息: " + input);
                        }
                    }

                    // 短暂休眠以避免过度占用CPU
                    Thread.sleep(100);
                } catch (Exception e) {
                    System.err.println("发送消息时出错: " + e.getMessage());
                    break;
                }
            }
        });

        // 设置为守护线程
        consoleThread.setDaemon(true);
        consoleThread.start();
    }
}