package Networking;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.nio.file.*;
import java.util.List;

public class WatchServiceExample extends Application {
    // TextArea textArea;
    javafx.scene.control.TextArea textArea;
    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane root = new BorderPane();
        root.setPrefSize(500 , 300);

        textArea = new javafx.scene.control.TextArea();
        textArea.setEditable(false);
        root.setCenter(textArea);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("WatchServiceExample");
        primaryStage.show();

        WatchServiceThread wst = new WatchServiceThread();
        wst.start();
    }

    class WatchServiceThread extends Thread{
        @Override
        public void run(){
            try {

                WatchService watchService = FileSystems.getDefault().newWatchService();
                Path directory = Paths.get("C:TempT");
                directory.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE,
                        StandardWatchEventKinds.ENTRY_MODIFY);
                while(true){
                    WatchKey watchKey = watchService.take(); // watchKey가 들어올 때까지 블로킹
                    List<WatchEvent<?>> list = watchKey.pollEvents(); // WatchEvent 목록을 list 형태로 얻기
                    for (WatchEvent watchEvent : list){
                        WatchEvent.Kind kind = watchEvent.kind(); // 종류 얻기
                        Path path = (Path) watchEvent.context(); // 경로 얻기
                        if(kind == StandardWatchEventKinds.ENTRY_CREATE){
                            Platform.runLater(() -> {
                                textArea.appendText("파일 생성 -> " + path.getFileName()+ "\n");

                            });
                        }
                    }
                }
            } catch (Exception e){

            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
