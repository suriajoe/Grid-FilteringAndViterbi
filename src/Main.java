import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application{

	public void start(Stage primaryStage) throws Exception
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("Grid.fxml"));
		AnchorPane root = (AnchorPane)loader.load();
		
		Controller controller = loader.getController();
		controller.start(primaryStage);
		
		Scene scene = new Scene(root, 600, 700);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.setTitle("Grid Path");
		primaryStage.show();
		
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent)
			{
				switch(keyEvent.getCode())
				{
					case UP: controller.north(); break;
					case DOWN: controller.south();break;
					case RIGHT: controller.east();break;
					case LEFT: controller.west();break;
				}
			}
		});
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

}
