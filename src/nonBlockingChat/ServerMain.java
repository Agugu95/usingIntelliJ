package nonBlockingChat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain {
    Selector selector;
    ServerSocketChannel serverSocketChannel;
    List<ClientList> connections = new Vector<ClientList>();
    ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public static void main(String[] args) {
        ServerMain s = new ServerMain();
        s.start();
    }

    public static void showConsole(String message){
        System.out.println(message);
    }

    public class ClientList {
        SocketChannel socketChannel;
        // 클라이언트에 보낼 데이터 저장 필드
        String sendData;
        public ClientList(SocketChannel socketChannel){
            this.socketChannel = socketChannel;
            try {
                socketChannel.configureBlocking(false);
                SelectionKey key = socketChannel.register(selector, SelectionKey.OP_READ);
                // 키에 객체 첨부 (연결 된 클라이언트)
                key.attach(this);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        public void receive(SelectionKey Key){
            try {
                ByteBuffer buf = ByteBuffer.allocate(100);

                // 클라이언트 비정상 종료 시 IOException
                int byteCount = socketChannel.read(buf);

                // 클라이언트 정상 종료 시
                if(byteCount == -1){
                    throw new IOException();
                }
                String message = "요청: " + socketChannel.getRemoteAddress();
                showConsole(message);

                // 버퍼 pos 초기화
                buf.flip();

                // 문자열 변환
                Charset charset = Charset.forName("UTF-8");
                String data = charset.decode(buf).toString();

                // 각 클라이언트 별 전송
                for (ClientList client : connections) {
                    client.sendData = data;
                    SelectionKey key = client.socketChannel.keyFor((selector));
                    // 셀렉터에 등록 된 모든 키에 쓰기 작업
                    key.interestOps(SelectionKey.OP_WRITE);
                }
                // 변경 작업을 감지하기 위해 블로킹 해제 후 다시 실행
                selector.wakeup();
            } catch (Exception e){
                try {
                    connections.remove(this);
                    showConsole(e + ": 클라이언트 오류");
                    socketChannel.close();
                } catch (IOException e1){
                    e.printStackTrace();
                }
            }
        }

        public void send(SelectionKey key){
            try {
                Charset charset = Charset.forName("UTF-8");
                ByteBuffer buf = charset.encode(sendData);
                socketChannel.write(buf);
                key.interestOps(SelectionKey.OP_READ);
                selector.wakeup();
            } catch (Exception e){
                try {
                    connections.remove(this);
                    socketChannel.close();
                } catch (IOException e1){
                    e.printStackTrace();
                }
            }
        }

    }

    public void start(){
        try {
            // 셀렉터 객체 생성
            selector = Selector.open();
            // 서버 소켓 객체 생성
            serverSocketChannel = ServerSocketChannel.open();
            // 논블로킹 바인딩
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress("localhost", 8080));

            // 서버 소켓 객체를 셀렉터에 ACCEPT 작업으로 등록
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        }catch(Exception e){
            if (serverSocketChannel.isOpen()) {
                stop();
            }
            // 메소드 중단을 위한..???
            return;
        }

        Runnable runnable = () -> {
         while(true){
             try{
                 // 작업 준비가 된 채널이 없다면 블로킹
                 int keyCount = selector.select();
                 if(keyCount == 0){
                     continue;
                 }
                 // 작업 준비가 된 키를 얻고, SET으로 리턴
                 Set<SelectionKey> selkeys = selector.selectedKeys();
                 Iterator<SelectionKey> keyIterator = selkeys.iterator();
                 while (keyIterator.hasNext()) {
                     // Iterator로 저장 된 키를 가져옴
                     SelectionKey key = keyIterator.next();

                     // 작업 유형 별 키 분류
                     if (key.isAcceptable()) {
                         accept(key);
                     }
                     if (key.isReadable()) {
                         ClientList list = (ClientList)key.attachment();
                         list.receive(key);
                     }
                 }
             } catch (Exception e){

             }
         }

        };
        executorService.submit(runnable);
    }

    public void stop(){
        try {
            Iterator<ClientList> iterator = connections.iterator();
            while(iterator.hasNext()){
                ClientList client = iterator.next();
                client.socketChannel.close();
                iterator.remove();
                serverSocketChannel.close();
                selector.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void accept(SelectionKey key) {
        try {
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
            SocketChannel socketChannel = serverSocketChannel.accept();
            ClientList client = new ClientList(socketChannel);
            connections.add(client);
        } catch (Exception e){
            stop();
        }
    }
}

