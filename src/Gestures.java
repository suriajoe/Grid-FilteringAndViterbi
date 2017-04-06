import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

public class Gestures 
{
	boolean showHoverCursor = true;
	
	public void hoverCursor(Node node)
	{
		if(showHoverCursor)
		{
			node.hoverProperty().addListener(new ChangeListener<Boolean>(){
				@Override
				public void changed(ObservableValue<? extends Boolean> observable,
						Boolean oldValue, Boolean newValue)
				{
					if(newValue)
					{
						((Cell)node).hover();
					}
					else
					{
						((Cell)node).unhover();
					}
				}
			});
		}
	}
	
	public void keyboardCursor(AnchorPane list,Grid grid, int sX, int sY)
	{
		//current start node
		grid.getCell(sX, sY).hover();
		Scene scene = new Scene(list);

		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event)
			{
				switch(event.getCode())
				{
					case UP: north(grid,sX,sY); break;
				}
			}
		});
		
		/*
		grid.addEventHandler(KeyEvent.KEY_PRESSED,(key) ->{
			if(key.getCode()==KeyCode.UP)
			{
				if(sX+1<3)
				{
					int sx = sX;
					int sy = sY;
					grid.getCell(sx, sy).unhover();
					sx++;
					grid.getCell(sx, sy).hover();
				}
			}
		});
		*/
	}
	
	public void north(Grid grid,int sx, int sy)
	{
		grid.getCell(sx, sy).unhover();
		sx++;
		grid.getCell(sx, sy).hover();
	}
}
