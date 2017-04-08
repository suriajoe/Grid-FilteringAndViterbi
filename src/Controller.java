import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Random;

import javafx.scene.text.*;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Controller extends Pane{

	@FXML private AnchorPane list;
	@FXML Button createGrid,largeMap,A,B,C;
	@FXML TextField coordinates;
	@FXML Label locationResult;
	
	private StringProperty result;
	
	static int rows = 3;
	static int columns = 3;
	
	double width = 600;
	double height = 600;
	Grid grid; 
	Grid current;
	Grid previous;
	Grid next;
	static int sx = 0, sy = 0; //current Cell coordinates
	
	public void start(Stage mainStage)
	{		
		createGrid.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event){
				if(!list.getChildren().isEmpty())
					flush();
				createGrid();
			}
		});
		
		largeMap.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event){
				if(!list.getChildren().isEmpty())
					flush();
				createLargeMap(1);
			}
		});
		
		A.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event){
				partA();
			}
		});
		
		C.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event){
				int count = 1;
				while(count <= 10)
				{
					if(!list.getChildren().isEmpty())
						flush();
					createLargeMap(count);
					count++;
				}
			}
		});
		
		coordinates.setOnKeyPressed(new EventHandler<KeyEvent>()
		{
			@Override
			public void handle(KeyEvent ke)
			{
				if(ke.getCode().equals(KeyCode.ENTER))
					checkLocation(coordinates.getText());
			}
		});
		coordinates.setFocusTraversable(false);
	}
	
	public void createGrid()
	{
		try{
			grid = new Grid(columns,rows,width,height);
			
			int startX = 0,startY = 0;
			int cellType;
			boolean blocked = true;
			Random rand = new Random();
			
			for(int row = 0;row<rows;row++)
			{
				for(int column = 0;column<columns;column++)
				{
					Cell cell = new Cell(column, row);
					//gesture.hoverCursor(cell);
					grid.add(cell, column, row);
				}
			}
			
			for(int i = 0;i<grid.cells.length;i++)
			{
				for(int j = 0;j<grid.cells.length;j++)
				{
					
					cellType = rand.nextInt(4);
					switch(cellType)
					{
						case 0: grid.getCell(i,j).setType(0);
								grid.getCell(i,j).setValue(0);
								grid.getCell(i,j).blocked();
								break;
						case 1: grid.getCell(i,j).setType(1);	
								grid.getCell(i,j).setValue(1);
								break;
						case 2: grid.getCell(i,j).setType(2);	
								grid.getCell(i,j).setValue(2);
								grid.getCell(i,j).hardToTraverse();
								break;
						case 3: grid.getCell(i,j).setType(3);	
								grid.getCell(i,j).setValue(3);	
								grid.getCell(i,j).highway();
								break;
					}
				}	
			}
			
			//Start in a random cell that is not blocked
			while(blocked)
			{		
				startX = rand.nextInt(3);
				startY = rand.nextInt(3);
				if(grid.getCell(startX, startY).getType() == 0)
				{
					
				}
				else
				{
					//grid.getCell(startX, startY).setType(1);
					//grid.getCell(startX, startY).setValue(1);
					//grid.getCell(startX, startY).normal();
					blocked = false;
				}
			}
			accurateSensor(startX,startY);	
			sx = startX;
			sy = startY;
			
			list.getChildren().addAll(grid);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	public void defaultGrid()
	{
		try{
			grid = new Grid(columns,rows,width,height);
			
			int startX = 0,startY = 0;
			int cellType;
			boolean blocked = true;
			Random rand = new Random();
			
			Cell cell1 = new Cell(0,0,3,"H"); //H
			Cell cell2 = new Cell(1,0,3,"H"); //H
			Cell cell3 = new Cell(2,0,2,"T"); //T
			Cell cell4 = new Cell(0,1,1,"N"); //N
			Cell cell5 = new Cell(1,1,1,"N"); //N
			Cell cell6 = new Cell(2,1,1,"N"); //N
			Cell cell7 = new Cell(0,2,1,"N"); //N
			Cell cell8 = new Cell(1,2,0,"B"); //B
			Cell cell9 = new Cell(2,2,3,"H"); //H
			
			grid.add(cell1, 0, 0);
			grid.add(cell2, 1, 0);
			grid.add(cell3, 2, 0);
			grid.add(cell4, 0, 1);
			grid.add(cell5, 1, 1);
			grid.add(cell6, 2, 1);
			grid.add(cell7, 0, 2);
			grid.add(cell8, 1, 2);
			grid.add(cell9, 2, 2);

			for(int i = 0;i<grid.cells.length;i++)
			{
				for(int j = 0;j<grid.cells.length;j++)
				{			
					cellType = grid.getCell(i, j).getType();
					switch(cellType)
					{
						case 0: grid.getCell(i,j).blocked();
								grid.getCell(i,j).setValue(0);
								break;
						case 1: grid.getCell(i,j).setValue(0.125);
								break;
						case 2: grid.getCell(i,j).hardToTraverse();
								grid.getCell(i,j).setValue(0.125);
								break;
						case 3: grid.getCell(i,j).highway();
								grid.getCell(i,j).setValue(0.125);
								break;
					}
				}	
			}
			
			//Start in a random cell that is not blocked
			while(blocked)
			{		
				startX = rand.nextInt(3);
				startY = rand.nextInt(3);
				if(grid.getCell(startX, startY).getType() == 0)
				{
					
				}
				else
				{
					blocked = false;
				}
			}
			//accurateSensor(startX,startY);	
			sx = startX;
			sy = startY;
			
			list.getChildren().addAll(grid);
			filtering(grid);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	public void partA()
	{
		//actions = right, right, down, down
		//reading = N	   N	  H		H
		IO io = new IO(grid);

		for(int i=0;i<4;i++)
		{
			if(!list.getChildren().isEmpty())
				flush();
			defaultGrid();
			io.write(grid,i);
		}
	}
	
	public void accurateSensor(int x,int y)
	{
		int type = grid.getCell(x, y).getType();
		
		switch(type)
		{
			case 0: System.out.println("Current Cell: B"); break; //should never land on a blocked cell
			case 1: System.out.println("Current Cell: N"); break;
			case 2: System.out.println("Current Cell: T"); break;
			case 3: System.out.println("Current Cell: H"); break;
		}
	}
	
	public String sensor(int x, int y)
	{
		int type = grid.getCell(x,y).getType();
		Random rand = new Random();
		
		int correct = 89; 		//simulate 90% correct terrain
		int incorrectOne = 94; 	//simulate 5% incorrect terrain
		int incorrectTwo = 99;	//simulate 5% incorrect terrain
		
		int terrainSensor = 0;
		
		if(type == 1)
		{
			terrainSensor = rand.nextInt(100);
			if(terrainSensor <= correct)
				return "N";
			else if(terrainSensor > correct && terrainSensor <= incorrectOne)
				return "T";
			else if(terrainSensor > incorrectOne && terrainSensor <= incorrectTwo)
				return "H";
		}
		else if(type == 2)
		{
			terrainSensor = rand.nextInt(100);
			if(terrainSensor <= correct)
				return "T";
			else if(terrainSensor > correct && terrainSensor <= incorrectOne)
				return "N";
			else if(terrainSensor > incorrectOne && terrainSensor <= incorrectTwo)
				return "H";
		}
		else if(type == 3)
		{
			terrainSensor = rand.nextInt(100);
			if(terrainSensor <= correct)
				return "H";
			else if(terrainSensor > correct && terrainSensor <= incorrectOne)
				return "N";
			else if(terrainSensor > incorrectOne && terrainSensor <= incorrectTwo)
				return "T";
		}		
		return "";
	}
	
	public void north()
	{
		if(grid != null)
		{
		if(sx > 0)
		{
			if(grid.getCell(sx-1,sy).getType() != 0)
			{
				sx--;

				System.out.println("Move:UP, Current Cell: " + sensor(sx,sy));
			}
			else
			{
				System.out.println("Move:UP Failed, Upper Cell is blocked. Current Cell: " + sensor(sx,sy));
			}
		}
		else
		{
			System.out.println("Upper Bound Reached. No Position changed. Current Cell: " + sensor(sx,sy));
		}
		}
	}
	public void south()
	{
		if(grid != null)
		{
		if(sx < grid.rows-1)
		{
			if(grid.getCell(sx+1,sy).getType() != 0)
			{	
				sx++;

				System.out.println("Move:DOWN, Current Cell: " + sensor(sx,sy));
			}
			else
			{
				System.out.println("Move:DOWN Failed, Lower Cell is blocked. Current Cell: " + sensor(sx,sy));
			}
		}
		else
		{
			System.out.println("Lower Bound Reached. No Position changed. Current Cell: " + sensor(sx,sy));
		}
		}
	}
	public void east()
	{
		if(grid != null)
		{
		if(sy < grid.columns-1)
		{
			if(grid.getCell(sx,sy+1).getType() != 0)
			{
				sy++;

				System.out.println("Move:RIGHT, Current Cell: " + sensor(sx,sy));
			}
			else
			{
				System.out.println("Move:RIGHT Failed, Right Cell is blocked. Current Cell: " + sensor(sx,sy));
			}
		}
		else
		{
			System.out.println("Right Bound Reached. No Position changed. Current Cell: " + sensor(sx,sy));
		}
		}
	}
	public void west()
	{
		if(grid != null)
		{
		if(sy > 0)
		{
			if(grid.getCell(sx,sy-1).getType() != 0)
			{
				sy--;

				System.out.println("Move:LEFT, Current Cell: " + sensor(sx,sy));
			}
			else
			{
				System.out.println("Move:LEFT Failed, Left Cell is blocked. Current Cell: " + sensor(sx,sy));
			}
		}
		else
		{
			System.out.println("Left Bound Reached. No Position changed. Current Cell: " + sensor(sx,sy));
		}
		}
	}
	
	public void flush()
	{
		list.getChildren().clear();
		grid = null;
	}
	
	
	public void checkLocation(String str)
	{
		int x;
		int y;
		String strArray[];
		
		str = str.trim();
		if(str.contains(","))
		{
			strArray = str.split(",");
			x = Integer.parseInt(strArray[0]);
			y = Integer.parseInt(strArray[1]);
		
			result = new SimpleStringProperty();
			locationResult.textProperty().bind(result);
			if((sx+1) == x && (sy+1) == y)
			{
				result.setValue("Result: True");
			}
			else
				result.setValue("Result: False");
		}
	}
	
	public void createLargeMap(int mapNum)
	{
		try{			
			int startX = 0,startY = 0;
			int rowNum = 100;
			int colNum = 100;
			int cellType;
			int normal = 49;
			int highway = 69;
			int hardTraverse = 89;
			int block = 99;
			boolean blocked = true;
			Random rand = new Random();
			
			grid = new Grid(colNum,rowNum,width,height);
			
			for(int row = 0;row<rowNum;row++)
			{
				for(int column = 0;column<colNum;column++)
				{
					Cell cell = new Cell(column, row);
					grid.add(cell, column, row);
				}
			}
			
			for(int i = 0;i<grid.rows;i++)
			{
				for(int j = 0;j<grid.columns;j++)
				{	
					cellType = rand.nextInt(100);
					
					if(cellType <= normal)
					{
						grid.getCell(i,j).setType(1);	
						grid.getCell(i,j).setValue(1);
					}
					else if(cellType > normal && cellType <= highway)
					{
						grid.getCell(i,j).setType(3);	
						grid.getCell(i,j).setValue(3);
						grid.getCell(i,j).highway();
					}
					else if(cellType > highway && cellType <= hardTraverse)
					{
						grid.getCell(i,j).setType(2);	
						grid.getCell(i,j).setValue(2);
						grid.getCell(i,j).hardToTraverse();
					}
					else if(cellType > hardTraverse && cellType <= block)
					{
						grid.getCell(i,j).setType(0);	
						grid.getCell(i,j).setValue(0);
						grid.getCell(i,j).blocked();
					}
				}	
			}
			
			//Start in a random cell that is not blocked
			while(blocked)
			{		
				startX = rand.nextInt(100);
				startY = rand.nextInt(100);
				if(grid.getCell(startX, startY).getType() == 0)
				{
					
				}
				else
				{
					blocked = false;
				}
			}
			accurateSensor(startX,startY);	
			sx = startX;
			sy = startY;
			
			list.getChildren().addAll(grid);
			
			int count = 1;
			while(count <= 10)
			{
				randomActions(count,mapNum);
				count++;
			}
			IO io = new IO(grid);
			io.writeMap(mapNum);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void randomActions(int sameMapRunNum, int mapNum)
	{
		int count = 0;
		int action;
		int move = 89;
		int stay = 99;
		int correct = 89;
		int incorrectOne = 94;
		int incorrectTwo = 99;
		Random rand = new Random();
		String initialCoor = "("+sx+","+sy+")";  //coordinates of initial point
		String[] coordinate = new String[100];
		String[] actionType = new String[100];
		String[] sensor = new String[100];
		
		while(count < 100)
		{
			action = rand.nextInt(4);
			
			if(action == 0) //Up
			{
				if(sx > 0)
				{
					if(grid.getCell(sx-1, sy).getType() != 0)
					{
						if(rand.nextInt(100) <= move)//90% chance follow action
						{
							sx--;
							coordinate[count] = "("+sx+","+sy+")";
							actionType[count] = "U";
							sensor[count] = sensor(sx,sy);
						}
						else
						{
							coordinate[count] = "("+sx+","+sy+")";
							actionType[count] = "U";
							sensor[count] = sensor(sx,sy);
						}
					}
					else
					{
						coordinate[count] = "("+sx+","+sy+")";
						actionType[count] = "U";
						sensor[count] = sensor(sx,sy);
					}
				}
				else
				{
					coordinate[count] = "("+sx+","+sy+")";
					actionType[count] = "U";
					sensor[count] = sensor(sx,sy);
				}
			}
			else if(action == 1)//Down
			{
				if(sx < grid.rows-1)
				{
					if(grid.getCell(sx+1, sy).getType() != 0)
					{
						if(rand.nextInt(100) <= move)//90% chance follow action
						{
							sx++;
							coordinate[count] = "("+sx+","+sy+")";
							actionType[count] = "D";
							sensor[count] = sensor(sx,sy);
						}
						else
						{
							coordinate[count] = "("+sx+","+sy+")";
							actionType[count] = "D";
							sensor[count] = sensor(sx,sy);
						}
					}
					{
						coordinate[count] = "("+sx+","+sy+")";
						actionType[count] = "D";
						sensor[count] = sensor(sx,sy);
					}
				}
				else
				{
					coordinate[count] = "("+sx+","+sy+")";
					actionType[count] = "D";
					sensor[count] = sensor(sx,sy);
				}
			}
			else if(action == 2)//Right
			{
				if(sy < grid.columns-1)
				{
					if(grid.getCell(sx, sy+1).getType() != 0)
					{
						if(rand.nextInt(100) <= move)//90% chance follow action
						{
							sy++;
							coordinate[count] = "("+sx+","+sy+")";
							actionType[count] = "R";
							sensor[count] = sensor(sx,sy);
						}
						else
						{
							coordinate[count] = "("+sx+","+sy+")";
							actionType[count] = "R";
							sensor[count] = sensor(sx,sy);
						}
					}
					else
					{
						coordinate[count] = "("+sx+","+sy+")";
						actionType[count] = "R";
						sensor[count] = sensor(sx,sy);
					
					}
				}
				else
				{
					coordinate[count] = "("+sx+","+sy+")";
					actionType[count] = "R";
					sensor[count] = sensor(sx,sy);
				}
			}
			else if(action == 3)//Left
			{
				if(sy > 0)
				{
					if(grid.getCell(sx, sy-1).getType() != 0)
					{
						if(rand.nextInt(100) <= move)			//90% chance follow action
						{
							sy--;
							coordinate[count] = "("+sx+","+sy+")";
							actionType[count] = "L";
							sensor[count] = sensor(sx,sy);
						}
						else									//10% stay in place
						{
							coordinate[count] = "("+sx+","+sy+")";
							actionType[count] = "L";
							sensor[count] = sensor(sx,sy);
						}
					}
					else										//blocked
					{
						coordinate[count] = "("+sx+","+sy+")";
						actionType[count] = "L";
						sensor[count] = sensor(sx,sy);
					}
				}
				else											//bounded
				{
					coordinate[count] = "("+sx+","+sy+")";
					actionType[count] = "L";
					sensor[count] = sensor(sx,sy);
				}
			}	
			count++;
		}
		IO io = new IO(grid);
		io.writeGroundTruth(initialCoor,coordinate,actionType,sensor,sameMapRunNum,mapNum);
	}

	public void filtering(Grid grid)
	{
		String[] action = {"Right","Right","Down","Down"};
		String[] reading = {"N","N","H","H"};

		current = new Grid(columns,rows,width,height);
		
		current = grid;
		for(int i=0;i<action.length;i++)
		{
			grid = TransitionModel(action[i],grid);
			grid = ObservationModel(grid,reading[i]);
		}
		
		for(int i=0;i<grid.rows;i++)
		{
			for(int j=0;j<grid.columns;j++)
			{
				grid.cells[i][j].reading = current.cells[i][j].reading;
			}
		}
		//print(grid);
		this.grid = grid;
	}
	
	public void print(Grid next)
	{
		for(int i=0;i<next.rows;i++)
		{
			for(int j=0;j<next.columns;j++)
			{
				System.out.print("["+next.getCell(i, j).getReading() + "] "+ next.getCell(i, j).value);
			}
			System.out.println();
		}
	}
	/*
	public Grid TransitionModel(String action, Grid grid)
	{
		//dot product or state vector  
		switch(action)
		{
		case "Right":
			// Transition from 0, 0
            next.getCell(0, 0).setValue(next.getCell(0,0).value + grid.getCell(0, 0).value * 0.1);
            next.getCell(0, 1).setValue(next.getCell(0,1).value + grid.getCell(0, 1).value * 0.9);

            // Transition from 0, 1
            next.getCell(0, 1).setValue(next.getCell(0,1).value + grid.getCell(0, 1).value * 0.1);
            next.getCell(0, 2).setValue(next.getCell(0,2).value + grid.getCell(0, 2).value * 0.9);

            // Transition from 0, 2
            next.getCell(0, 2).setValue(next.getCell(0,2).value + grid.getCell(0, 2).value * 1.0);

            // Transition from 1, 0
            next.getCell(1, 0).setValue(next.getCell(1,0).value + grid.getCell(1, 0).value * 0.1);
            next.getCell(1, 1).setValue(next.getCell(1,1).value + grid.getCell(1, 1).value * 0.9);

            // Transition from 1, 1
            next.getCell(1, 1).setValue(next.getCell(1,1).value + grid.getCell(1, 1).value * 0.1);
            next.getCell(1, 2).setValue(next.getCell(1,2).value + grid.getCell(1, 2).value * 0.9);

            // Transition from 1, 2
            next.getCell(1, 2).setValue(next.getCell(1,2).value + grid.getCell(1, 2).value * 1.0);

            // Transition from 2, 1
            next.getCell(2, 1).setValue(next.getCell(2,1).value + grid.getCell(2, 1).value * 1.0);

            // Transition from 2, 2
            next.getCell(2, 2).setValue(next.getCell(2,2).value + grid.getCell(2, 2).value * 1.0);

            break;
		case "Down":
			//transition from 0,0
            next.getCell(0, 0).setValue(next.getCell(0,0).value + grid.getCell(0, 0).value * 0.1);
            next.getCell(1, 0).setValue(next.getCell(1,0).value + grid.getCell(1, 0).value * 0.9);

			//transistion from 0,1
            next.getCell(0, 0).setValue(next.getCell(0,1).value + grid.getCell(0, 1).value * 0.1);
            next.getCell(1, 1).setValue(next.getCell(1,1).value + grid.getCell(1, 1).value * 0.9);
			
			//transistion from 0,2
            next.getCell(0, 2).setValue(next.getCell(0,2).value + grid.getCell(0, 2).value * 0.1);
            next.getCell(1, 1).setValue(next.getCell(1,2).value + grid.getCell(1, 2).value * 0.9);
			
			//transistion from 1,0
            next.getCell(1, 0).setValue(next.getCell(1,0).value + grid.getCell(1,0).value * 0.1);
            next.getCell(2, 1).setValue(next.getCell(2,0).value + grid.getCell(2,0).value * 0.9);
			
			//transistion from 1,1
            next.getCell(1, 1).setValue(next.getCell(1,1).value + grid.getCell(1, 1).value * 1.0);
			
			//transistion from 1,2
            next.getCell(0, 1).setValue(next.getCell(0,1).value + grid.getCell(0, 1).value * 0.1);
            next.getCell(1, 1).setValue(next.getCell(1,1).value + grid.getCell(1, 1).value * 0.9);

			//transistion from 2,0
            next.getCell(2, 0).setValue(next.getCell(2,0).value + grid.getCell(2, 0).value * 1.0);
			
			//transistion from 2,2
            next.getCell(2, 2).setValue(next.getCell(2,2).value + grid.getCell(2, 2).value * 1.0);
            break;
		}
		return next;
	
	}
	*/
	
	private Grid TransitionModel(String action, Grid grid)
    {
        // Create transition matrix
		next = new Grid(columns,rows,width,height);
		for(int row = 0;row<rows;row++)
		{
			for(int column = 0;column<columns;column++)
			{
				Cell cell = new Cell(column, row);
				next.add(cell, column, row);
			}
		}
		
		//sum the states
        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 3; j++)
            {
                if (grid.cells[i][j].reading == "B")
                {
                	
                }
                else
                {
                switch(action)
                {
                    case "Right":
                        if (j == 2)
                        {
                        	next.cells[i][j].value += grid.cells[i][j].value * 1.0;
                        }
                        else if(j+1 < 3 && grid.cells[i][j+1].reading == "B")
                        {
                        	next.cells[i][j].value += grid.cells[i][j].value * 1.0;
                        }
                        else
                        {
                        	next.cells[i][j].value += grid.cells[i][j].value * 0.1;
                        	next.cells[i][j+1].value += grid.cells[i][j+1].value * 0.9;
                        }
                        break;
                    case "Down":
                    	if(i == 2)
                    	{
                        	next.cells[i][j].value += grid.cells[i][j].value * 1.0;
                    	}
                    	else if(i+1<3 && grid.cells[i+1][j].reading == "B")
                    	{
                        	next.cells[i][j].value += grid.cells[i][j].value * 1.0;
                    	}
                    	else
                    	{
                        	next.cells[i][j].value += grid.cells[i][j].value * 0.1;
                        	next.cells[i+1][j].value += grid.cells[i+1][j].value * 0.9;
                    	}
                }
                }
            }
        }
        return next;
     }
	
	public Grid ObservationModel(Grid curr, String reading)
	{
		next = new Grid(columns,rows,width,height);
		for(int row = 0;row<rows;row++)
		{
			for(int column = 0;column<columns;column++)
			{
				Cell cell = new Cell(column, row);
				next.add(cell, column, row);
			}
		}
		
		for(int i=0;i<3;i++)
		{
			for(int j=0;j<3;j++)
			{
				if(curr.getCell(i, j).reading == "B") //blocked
				{
					continue;
				}
				else if(curr.getCell(i, j).reading == reading)
					next.getCell(i, j).setValue(next.getCell(i,j).value + curr.getCell(i,j).value * 0.9);
				else
					next.getCell(i, j).setValue(next.getCell(i,j).value + curr.getCell(i,j).value * 0.05);
			}
		}
		
		normalize(next);
		return next;
	}
	
	public void normalize(Grid next)
	{
		double total[] = new double[next.rows];
		for(int i=0;i<3;i++)
		{
			for(int j=0;j<3;j++)
			{
				total[i] += next.getCell(i, j).value;
			}
		}
		for(int i=0;i<3;i++)
		{
			for(int j=0;j<3;j++)
			{
				next.cells[i][j].value /= total[i];
			}
		}
	}
}
