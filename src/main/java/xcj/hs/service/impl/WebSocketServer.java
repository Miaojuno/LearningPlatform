package xcj.hs.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

@ServerEndpoint(value = "/webSocket/chatModule")
@Component
@Slf4j
public class WebSocketServer {

  @PostConstruct
  public void init() {
    System.out.println("websocket 加载");
  }

  private static final AtomicInteger OnlineCount = new AtomicInteger(0);
  private static CopyOnWriteArraySet<Session> SessionSet = new CopyOnWriteArraySet<Session>();

  /** 连接建立成功调用的方法 */
  @OnOpen
  public void onOpen(Session session) {
    SessionSet.add(session);
    int cnt = OnlineCount.incrementAndGet(); // 在线数加1
    log.info("有连接加入，当前连接数为：{}", cnt);
  }

  /** 连接关闭调用的方法 */
  @OnClose
  public void onClose(Session session) {
    SessionSet.remove(session);
    int cnt = OnlineCount.decrementAndGet();
    log.info("有连接关闭，当前连接数为：{}", cnt);
  }

  /**
   * 收到客户端消息后调用的方法
   *
   * @param message 客户端发送过来的消息
   */
  @OnMessage
  public void onMessage(String message, Session session) {
    log.info("来自客户端的消息：{}", message);
  }

  /**
   * 出现错误
   *
   * @param session
   * @param error
   */
  @OnError
  public void onError(Session session, Throwable error) {
    log.error("发生错误：{}，Session ID： {}", error.getMessage(), session.getId());
    error.printStackTrace();
  }

  /**
   * 发送消息
   *
   * @param session
   * @param message
   */
  public static void SendMessage(Session session, String message) {
    try {
      session.getBasicRemote().sendText(message);
    } catch (IOException e) {
      log.error("发送消息出错：{}", e.getMessage());
      e.printStackTrace();
    }
  }

  /**
   * 群发消息
   *
   * @param message
   * @throws IOException
   */
  public static void BroadCastInfo(String message) throws IOException {
    for (Session session : SessionSet) {
      if (session.isOpen()) {
        SendMessage(session, message);
      }
    }
  }

  /**
   * 指定Session发送消息
   *
   * @param userAccount
   * @param message
   * @throws IOException
   */
  public static void SendMessage(String message, String userAccount) throws IOException {
    Session session = null;
    for (Session s : SessionSet) {
      if (s.getQueryString().split("=")[1].equals(userAccount)) {
        session = s;
        break;
      }
    }
    if (session != null) {
      SendMessage(session, message);
    } else {
      log.warn("没有找到你指定账户的会话：{}", userAccount);
    }
  }
}
