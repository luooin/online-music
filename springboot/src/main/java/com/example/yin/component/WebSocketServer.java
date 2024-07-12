package com.example.yin.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.yin.model.domain.MessageVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * WebSocket的操作类
 */
@Component
@Slf4j
@ServerEndpoint("/websocket/{name}")
public class WebSocketServer {

    /**
     * 静态变量，用来记录当前在线连接数，线程安全的类。 原子类
     */
    private static final AtomicInteger onlineSessionClientCount = new AtomicInteger(0);

    /**
     * 存放所有在线的客户端
     */
    private static final Map<String, Session> onlineSessionClientMap = new ConcurrentHashMap<>();

    /**
     * 连接 name 和连接会话
     */
    private String name;

    private void broadcastOnlineStatus() {
        Set<String> onlineUserNames = onlineSessionClientMap.keySet();
        JSONObject statusMessage = new JSONObject();
        statusMessage.put("type", "onlineStatus");
        statusMessage.put("onlineCount", onlineSessionClientCount.get());
        statusMessage.put("onlineUsers", onlineUserNames);

        String message = statusMessage.toJSONString();
        onlineSessionClientMap.values().forEach(session -> {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                log.error("Error sending online status message", e);
            }
        });
    }


    @OnOpen
    public void onOpen(@PathParam("name") String name, Session session) {

        Session beforeSession = onlineSessionClientMap.get(name);

        if (beforeSession != null) {
            //在线数减1
            onlineSessionClientCount.decrementAndGet();
            log.info("连接已存在，关闭之前的连接 ==> session_id = {}， name = {}。", beforeSession.getId(), name);
            // 从 Map中移除
            onlineSessionClientMap.remove(name);
            //关闭之前的连接
            try {
                beforeSession.close();
            } catch (Exception e) {
                log.error("关闭之前的连接异常，异常信息为：{}", e.getMessage());
            }
        }
        log.info("连接建立中 ==> session_id = {}， name = {}", session.getId(), name);
        onlineSessionClientMap.put(name, session);

        //在线数加1
        onlineSessionClientCount.incrementAndGet();
        this.name = name;
        SystemNotice("欢迎"+this.name+"加入聊天室！");
        log.info("连接建立成功，当前在线数为：{} ==> 开始监听新连接：session_id = {}， name = {}。", onlineSessionClientCount, session.getId(), name);
        broadcastOnlineStatus();
    }


    @OnClose
    public void onClose(@PathParam("name") String name, Session session) {
        if (name == null || name.equals("")) {
            name = this.name;
        }
        // 从 Map中移除
        onlineSessionClientMap.remove(name);
        SystemNotice(this.name+"下线了~ "+new Date());
        //在线数减1
        onlineSessionClientCount.decrementAndGet();

        log.info("连接关闭成功，当前在线数为：{} ==> 关闭该连接信息：session_id = {}， name = {}。", onlineSessionClientCount, session.getId(), name);
        broadcastOnlineStatus();
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        JSONObject jsonObject = JSON.parseObject(message);
        String toname = jsonObject.getString("name");
        String msg = jsonObject.getString("message");
        log.info("服务端收到客户端消息 ==> fromname = {}, toname = {}, message = {}", name, toname, msg);
        //向所有人发送消息
        sendToAll(msg);
    }

    /**
     * 发生错误调用的方法
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("WebSocket发生错误，错误信息为：" + error.getMessage());
        error.printStackTrace();
    }

    /**
     * 群发消息
     *
     */
    private void sendToAll(String message) {
        // 遍历在线map集合,   onlineSessionClientMap中的每个在线客户端执行指定的操作
        onlineSessionClientMap.forEach((onlineName, toSession) -> {
            // 排除掉自己
            if (!name.equals(onlineName)) {
                log.info("服务端给客户端群发消息 ==> name = {}, toname = {}, message = {}", name, onlineName, message);
                MessageVo messageVo = new MessageVo();
                messageVo.setFrom(name);
                messageVo.setDate(new Date());
                messageVo.setMessage(message);
                //getAsyncRemote()和getBasicRemote()确实是异步与同步的区别，大部分情况下，推荐使用getAsyncRemote()。由于getBasicRemote()的同步特性，
//                异步发送消息意味着发送消息的操作是非阻塞的，即发送消息的线程不会等待消息发送完成就可以继续执行其他操作。
//                适用于需要在后台线程中发送消息或需要非阻塞操作的情况。同步发送消息意味着发送消息的操作是阻塞的，即发送消息的线程会等待消息发送完成后再继续执行。
//                由于是同步操作，可能会阻塞当前线程，不适合在需要高并发或大量消息发送的情况下使用。l
                toSession.getAsyncRemote().sendText(JSON.toJSONString(messageVo));
            }
        });
    }

    /**
     * 系统通知，用于告诉所有人，xxx上线，xxx下线
     * @param message
     */
    private void SystemNotice( String message) {
        onlineSessionClientMap.forEach((onlineName, toSession) -> {
            MessageVo messageVo = new MessageVo();
            messageVo.setFrom("系统: ");
            messageVo.setDate(new Date());
            messageVo.setMessage(message);
            try {
                toSession.getBasicRemote().sendText(JSON.toJSONString(messageVo));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}

