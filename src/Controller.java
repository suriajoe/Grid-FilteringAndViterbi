import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

import org.tc33.jheatchart.HeatChart;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Controller extends Pane{

	@FXML private AnchorPane list;
	@FXML Button createGrid,largeMap,A,B,C,D,E;
	@FXML TextField coordinates;
	@FXML Label locationResult;
	
	private StringProperty result;
	
	static int rows = 3;
	static int columns = 3;
	
	double width = 600;
	double height = 600;
	Grid grid; 
	//Grid current;
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
				if(!list.getChildren().isEmpty())
					flush();
				partA();
			}
		});
		
		B.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event){
				if(!list.getChildren().isEmpty())
					flush();
				partB();
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
		
		D.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event){
				try {
					partD();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		E.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event){
				partE();
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

		defaultGrid();
		filtering(grid);
		io.write(grid);
	}
	public void partB()
	{
		//actions = right, right, down, down
		//reading = N	   N	  H		H
		IO io = new IO(grid);
		String[] states = {"H","H","T","N","N","N","N","B","H"};
		String[] observations = {"N","N","H","H"};
		double[] start_probability = { 0.125, 0.125, 0.125, 0.125, 
				   0.125, 0.125, 0.125, 0, 0.125};
		//9 states with 9 separate prob
		double[][] transition_probability = { { 0.1, 0.9, 0,0.9, 0, 0, 0, 0, 0 }, //11
				{ 0.9, 0.1, 0.9,0, 0.9, 0, 0, 0, 0 },							//12
				{ 0, 0.9, 0.1,0,0, 0.9, 0, 0, 0 },								//13
				{ 0.9, 0, 0,0.1, 0.9, 0, 0.9, 0, 0 },							//21
				{ 0, 0.9, 0,0.9, 0.1, 0.9, 0, 0, 0 },							//22
				{ 0, 0, 0.9,0, 0.9, 0.1, 0, 0, 0.9 },							//23
				{ 0, 0, 0,0.9, 0, 0, 0.1, 0, 0 },								//31
				{ 0, 0, 0,0, 0, 0, 0, 0, 0 },									//32
				{ 0, 0, 0,0, 0, 0.9, 0, 0, 0.1 },								//33
				};
		double[][] emission_probability = { { 0.9, 0.9, 0,0.9, 0, 0, 0, 0, 0 }, //11
				{ 0.9, 0.9, 0.9,0, 0.9, 0, 0, 0, 0 },							//12
				{ 0, 0.9, 0.9,0,0, 0.9, 0, 0, 0 },								//13
				{ 0.9, 0, 0,0.9, 0.9, 0, 0.9, 0, 0 },							//21
				{ 0, 0.9, 0,0.9, 0.9, 0.9, 0, 0, 0 },							//22
				{ 0, 0, 0.9,0, 0.9, 0.9, 0, 0, 0.9 },							//23
				{ 0, 0, 0,0.9, 0, 0, 0.9, 0, 0 },								//31
				{ 0, 0, 0,0, 0, 0, 0, 0, 0 },									//32
				{ 0, 0, 0,0, 0, 0.9, 0, 0, 0.9 },								//33
				};
		
		//defaultGrid();
		Viterbi v = new Viterbi(grid);
		//v.viterbi(grid);
		//io.writeB(grid);
	}
	public void partD() throws IOException
	{
		IO io = new IO();
		
		LinkedList<int []> coordinates = new LinkedList<int []>();	//not used in part D
		for(int i=0;i<10;i++)
		{
			for(int j=0;j<1;j++)
			{
				LinkedList<String []> list = new LinkedList<String []>();
				
				int mapNum = i;
				int sameMapNum = j;
				list = io.readGroundTruth(mapNum+1, sameMapNum+1,coordinates);
				grid = io.readLargeMap(mapNum+1);
				filteringLarge(grid,list.get(0),list.get(1),sameMapNum,mapNum);
			}
		}
	
	}
	
	public void partE() 
	{	
		IO io = new IO();
		
		LinkedList<int[]> coordinates = new LinkedList<int[]>();
		LinkedList<double[]> data = new LinkedList<double[]>();
		ArrayList<Double> probAvg = new ArrayList<Double>();
		for(int i=0;i<100;i++)
		{
			probAvg.add(0.0);
		}
		
		int mapRuns = 10;
		int groundTruthRuns = 10;
		for(int i=0;i<mapRuns;i++)
		{
			for(int j=0;j<groundTruthRuns;j++)
			{
				LinkedList<String []> list = new LinkedList<String []>();
				
				int mapNum = i;
				int sameMapNum = j;
				list = io.readGroundTruth(mapNum+1, sameMapNum+1,coordinates);
				grid = io.readLargeMap(mapNum+1);
				data.add(filteringMLE(grid,list.get(0),list.get(1),sameMapNum,mapNum,coordinates,probAvg));
			}
		}
		System.out.println("MLE passed");
		float [] avgError = new float[data.get(0).length];
		//error amount per iteration
		for(int i=0;i<data.get(0).length;i++)
		{
			for(int j=0;j<data.size();j++)
			{
				double [] array = data.get(j);
				avgError[i] = avgError[i] + (float)array[i];
			}
		}
		for(int i=0;i<avgError.length;i++)
		{
			avgError[i] = avgError[i]/(mapRuns*groundTruthRuns);
		}
		LineChart chart = new LineChart();
		chart.plot(avgError);
		
		float[] avgProb = new float[100];
		for(int i=0;i<probAvg.size();i++)
		{
			avgProb[i] =(float) (probAvg.get(i)/(mapRuns*groundTruthRuns));
		}
		chart.plotProbAvg(avgProb);
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
		
		return "B";
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
			int numOfBlockedCells = 0;
			boolean blocked = true;
			Random rand = new Random();
			
			grid = new Grid(colNum,rowNum,width,height);
			
			//initialize grid
			for(int row = 0;row<rowNum;row++)
			{
				for(int column = 0;column<colNum;column++)
				{
					Cell cell = new Cell(column, row);
					grid.add(cell, column, row);
				}
			}
			
			//randomize cells
			for(int i = 0;i<grid.rows;i++)
			{
				for(int j = 0;j<grid.columns;j++)
				{	
					cellType = rand.nextInt(100);
					
					if(cellType <= normal)
					{
						grid.getCell(i,j).setType(1);	
						grid.getCell(i,j).setReading("N");
						grid.getCell(i,j).setValue(1);
					}
					else if(cellType > normal && cellType <= highway)
					{
						grid.getCell(i,j).setType(3);	
						grid.getCell(i,j).setReading("H");
						grid.getCell(i,j).setValue(3);
						//grid.getCell(i,j).highway();
					}
					else if(cellType > highway && cellType <= hardTraverse)
					{
						grid.getCell(i,j).setType(2);	
						grid.getCell(i,j).setReading("T");
						grid.getCell(i,j).setValue(2);
						//grid.getCell(i,j).hardToTraverse();
					}
					else if(cellType > hardTraverse && cellType <= block)
					{
						grid.getCell(i,j).setType(0);	
						grid.getCell(i,j).setReading("B");
						grid.getCell(i,j).setValue(0);
						numOfBlockedCells++;
						//grid.getCell(i,j).blocked();
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
			//accurateSensor(startX,startY);	
			sx = startX;
			sy = startY;
			
			//list.getChildren().addAll(grid);
			
			int count = 1;
			while(count <= 10)
			{
				randomActions(count,mapNum,numOfBlockedCells);
				count++;
			}
			IO io = new IO(grid);
			io.writeLargeMap(mapNum);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void randomActions(int sameMapRunNum, int mapNum,int numOfBlockedCells)
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
		
		float numOfTotalCells = grid.rows * grid.columns;
		float numOfFreeCells = numOfTotalCells - numOfBlockedCells;
		
		while(count < 100)
		{
			action = rand.nextInt(4);
			
			if(action == 0) //Up
			{
				if(sx > 0)//bounded
				{
					if(grid.getCell(sx-1, sy).getType() != 0)	//blocked
					{
						if(rand.nextInt(100) <= move)			//90% chance follow action
						{
							sx--;
							coordinate[count] = "("+sx+","+sy+")";
							actionType[count] = "U";
							sensor[count] = sensor(sx,sy);
						}
						else									//10% chance to stay in place
						{
							coordinate[count] = "("+sx+","+sy+")";
							actionType[count] = "U";
							sensor[count] = sensor(sx,sy);
						}
					}
					else										//blocked
					{
						coordinate[count] = "("+sx+","+sy+")";
						actionType[count] = "U";
						sensor[count] = sensor(sx,sy);
					}
				}
				else											//bounded
				{
					coordinate[count] = "("+sx+","+sy+")";
					actionType[count] = "U";
					sensor[count] = sensor(sx,sy);
				}
			}
			else if(action == 1)//Down
			{
				if(sx < grid.rows-1)//bounded
				{
					if(grid.getCell(sx+1, sy).getType() != 0)//blocked
					{
						if(rand.nextInt(100) <= move)//90% chance follow action
						{
							sx++;
							coordinate[count] = "("+sx+","+sy+")";
							actionType[count] = "D";
							sensor[count] = sensor(sx,sy);
						}
						else						//10% chance to stay in place
						{
							coordinate[count] = "("+sx+","+sy+")";
							actionType[count] = "D";
							sensor[count] = sensor(sx,sy);
						}
					}
					else										//blocked
					{
						coordinate[count] = "("+sx+","+sy+")";
						actionType[count] = "D";
						sensor[count] = sensor(sx,sy);
					}
				}
				else											//bounded
				{
					coordinate[count] = "("+sx+","+sy+")";
					actionType[count] = "D";
					sensor[count] = sensor(sx,sy);
				}
			}
			else if(action == 2)//Right
			{
				if(sy < grid.columns-1)//bounded
				{
					if(grid.getCell(sx, sy+1).getType() != 0)//blocked
					{
						if(rand.nextInt(100) <= move)//90% chance follow action
						{
							sy++;
							coordinate[count] = "("+sx+","+sy+")";
							actionType[count] = "R";
							sensor[count] = sensor(sx,sy);
						}
						else						//10% chance to stay in place
						{
							coordinate[count] = "("+sx+","+sy+")";
							actionType[count] = "R";
							sensor[count] = sensor(sx,sy);
						}
					}
					else										//blocked
					{
						coordinate[count] = "("+sx+","+sy+")";
						actionType[count] = "R";
						sensor[count] = sensor(sx,sy);
					
					}
				}
				else											//bounded
				{
					coordinate[count] = "("+sx+","+sy+")";
					actionType[count] = "R";
					sensor[count] = sensor(sx,sy);
				}
			}
			else if(action == 3)//Left
			{
				if(sy > 0)//bounded
				{
					if(grid.getCell(sx, sy-1).getType() != 0)	//blocked
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

		Grid current = new Grid(grid.columns,grid.rows);
		
		LinkedList<Grid> states = new LinkedList<Grid>();
		states.add(grid);

		current = grid;
		printState(grid,current,action[0],reading[0],0,true);
		for(int i=0;i<action.length;i++)
		{
			Grid condMat = grid;
			condMat = observationModel(grid,reading[i]);
			LinkedList<LinkedList<Cell>> pastNodes = transitionModel(action[i],condMat);
			normalize(condMat);
			Grid transMat = priorBelief(states.get(i),condMat,pastNodes);
			normalize(transMat);
			states.add(transMat);
			
			printState(transMat,current,action[i],reading[i],i,false);
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
	
	private LinkedList<LinkedList<Cell>> transitionModel(String action, Grid grid)
    {
        // Create transition matrix
		next = new Grid(grid.columns,grid.rows);
		for(int row = 0;row<grid.rows;row++)
		{
			for(int column = 0;column<grid.columns;column++)
			{
				Cell cell = new Cell(column, row);
				next.add(cell, column, row);
			}
		}
		
		LinkedList<LinkedList<Cell>> prior = new LinkedList<LinkedList<Cell>>();
		//Grid prior = new Grid(grid.columns,grid.rows);
		
		//sum the states
        for(int i = 0; i < grid.rows; i++)
        {
        	LinkedList<Cell> rowList = new LinkedList<Cell>();
            for(int j = 0; j < grid.columns; j++)
            {
                //if (grid.cells[i][j].reading == "B")
                {         	
                }
                //else
                {
                switch(action)
                {
                	case "R":
                    case "Right":        	
                        if (j <= grid.columns-1 && j > 0)
                        {
                        	if(grid.cells[i][j-1].reading == "B")
                        	{
                        		rowList.add(new Cell(j,i));
                        		grid.cells[i][j].value = grid.cells[i][j].value * 0.1;
                        	}
                        	else
                        	{
                        		rowList.add(new Cell(j-1,i));
                        		grid.cells[i][j].value = grid.cells[i][j].value * 0.9;
                        	}
                        }
                        if (j==0)
                        {
                        	if(grid.cells[i][j+1].reading == "B")
                        	{
                        		rowList.add(new Cell(j,i));
                        		grid.cells[i][j].value = grid.cells[i][j].value * 0.9;
                        	}
                        	else
                        	{
                        		rowList.add(new Cell(j,i));
                        		grid.cells[i][j].value = grid.cells[i][j].value * 0.1;
                        	}
                        }                   
                        break;
                    case "L":
                    case "Left":
                        if (j < grid.columns-1 && j>= 0)
                        {
                        	if(grid.cells[i][j+1].reading == "B")
                        	{
                        		rowList.add(new Cell(j,i));
                        		grid.cells[i][j].value = grid.cells[i][j].value * 0.1;
                        	}
                        	else
                        	{
                        		rowList.add(new Cell(j+1,i));
                        		grid.cells[i][j].value = grid.cells[i][j].value * 0.9;
                        	}
                        }
                        if (j==grid.columns-1)
                        {
                        	if(grid.cells[i][j-1].reading == "B")
                        	{
                        		rowList.add(new Cell(j,i));
                        		grid.cells[i][j].value = grid.cells[i][j].value * 0.9;
                        	}
                        	else
                        	{
                        		rowList.add(new Cell(j,i));
                        		grid.cells[i][j].value = grid.cells[i][j].value * 0.1;
                        	}
                        }
                        break;
                    case "D":
                    case "Down":
                        if (i>0 && i<=grid.rows-1)
                        {
                        	if(grid.cells[i-1][j].reading == "B")
                        	{
                        		rowList.add(new Cell(j,i));
                        		grid.cells[i][j].value = grid.cells[i][j].value * 0.1;
                        	}
                        	else
                        	{
                        		rowList.add(new Cell(j,i-1));
                        		grid.cells[i][j].value = grid.cells[i][j].value * 0.9;
                        	}
                        }
                        if (i==0)
                        {
                        	if(grid.cells[i+1][j].reading == "B")
                        	{
                        		rowList.add(new Cell(j,i));
                        		grid.cells[i][j].value = grid.cells[i][j].value * 0.9;
                        	}
                        	else
                        	{
                        		rowList.add(new Cell(j,i));
                        		grid.cells[i][j].value = grid.cells[i][j].value * 0.1;
                        	}
                        }

                    	break;
                    case "U":
                    case "UP":
                        if (i>=0 && i < grid.rows-1)
                        {
                        	if(grid.cells[i+1][j].reading == "B")
                        	{
                        		rowList.add(new Cell(j,i));
                        		grid.cells[i][j].value = grid.cells[i][j].value * 0.1;
                        	}
                        	else
                        	{
                        		rowList.add(new Cell(j,i+1));
                        		grid.cells[i][j].value = grid.cells[i][j].value * 0.9;
                        	}
                        }
                        if (i == grid.rows-1)
                        {
                        	if(grid.cells[i-1][j].reading == "B")
                        	{
                        		rowList.add(new Cell(j,i));
                        		grid.cells[i][j].value = grid.cells[i][j].value * 0.9;
                        	}
                        	else
                        	{
                        		rowList.add(new Cell(j,i));
                        		grid.cells[i][j].value = grid.cells[i][j].value * 0.1;
                        	}
                        }
                        break;
                  }
                }//else
            }//inner loop
            prior.add(rowList);
        }
		//normalize(next);
        return prior;
     }
	
	public Grid observationModel(Grid curr, String reading)
	{
		//create observation matrix		
		for(int i=0;i<curr.rows;i++)
		{
			for(int j=0;j<curr.columns;j++)
			{
				if(curr.getCell(i, j).reading == "B") //blocked
				{
					curr.cells[i][j].value = 0;
				}
				else if(curr.getCell(i, j).reading == reading)
					curr.cells[i][j].value = 0.9;
				else
					curr.cells[i][j].value = 0.1;
			}
		}
		
		return curr;
	}
	
	public void normalize(Grid next)
	{
		double total = 0.0;
		for(int i=0;i<next.rows;i++)
		{
			for(int j=0;j<next.columns;j++)
			{
				total += next.getCell(i, j).value;
			}
		}
		for(int i=0;i<next.rows;i++)
		{
			for(int j=0;j<next.columns;j++)
			{
				next.cells[i][j].value /= total;
			}
		}
	}
	public Grid priorBelief(Grid transMat,Grid condMat,LinkedList<LinkedList<Cell>> ancestors)
	{
		Grid prev = transMat;
		
		for(int i = 0; i < transMat.rows; i++)
		{
			for(int j = 0; j < transMat.columns; j++)
			{
				int idx1 = ancestors.get(i).get(j).getColumn();
				int idx2 = ancestors.get(i).get(j).getRow();
				double probability = transMat.cells[idx2][idx1].value  * condMat.cells[i][j].value;
				
				prev.cells[i][j].value = probability;
			}
		}
		
		return prev;
	}
	public void filteringLarge(Grid grid,String[] action,String[] reading,int sameMapRunNum, int mapNum)
	{
		Grid current = new Grid(grid.columns,grid.rows);
				
		LinkedList<Grid> states = new LinkedList<Grid>();
		states.add(grid);
		current = grid;
		heatMap(grid,0,mapNum);
		for(int i=0;i<action.length;i++)
		{
			Grid condMat = grid;
			condMat = observationModel(grid,reading[i]);
			LinkedList<LinkedList<Cell>> pastNodes = transitionModel(action[i],condMat);
			normalize(condMat);
			Grid transMat = priorBelief(states.get(i),condMat,pastNodes);
			normalize(transMat);
			states.add(transMat);			
			
			if(sameMapRunNum == 0)
			heatMap(transMat,i+1,mapNum);

			//print out ground truth path
			if(i == 9 || i == 49 || i == 99 && sameMapRunNum == 0){
				heatMap(transMat,i+1,mapNum);
				IO io = new IO(transMat);
				if(mapNum == 0 && sameMapRunNum == 0)
					io.writeGroundTruthPath(transMat,sameMapRunNum,mapNum, i,action,reading);
			}
		}
		
		for(int i=0;i<grid.rows;i++)
		{
			for(int j=0;j<grid.columns;j++)
			{
				grid.cells[i][j].reading = current.cells[i][j].reading;
			}
		}
		//print(grid);
		//this.grid = grid;
	}
	
	public void heatMap(Grid grid,int actionNum,int mapNum)
	{
		double[][] data = new double[grid.rows][grid.columns];
		
		for(int i=0;i<grid.rows;i++)
		{
			for(int j=0;j<grid.columns;j++)
			{
				data[i][j] = grid.cells[i][j].value;
			}
		}
		HeatChart map = new HeatChart(data);
		
		map.setTitle("HeatMap with Action Number " + actionNum);
		map.setXAxisLabel("X Axis");
		map.setYAxisLabel("Y Axis");
		map.setBackgroundColour(Color.WHITE);
		map.setLowValueColour(Color.WHITE);
		map.setHighValueColour(Color.GREEN);
		map.setColourScale(0.05);
		
		//System.out.println(map.max(data) + " " + map.min(data));
		try{
			map.saveToFile(new File("HeatMap\\Map"+mapNum + " HeatIteration"+ actionNum + ".png"));
		}
		catch(IOException i)
		{
			i.printStackTrace();
		}
	}
	public double[] filteringMLE(Grid grid,String[] action,String[] reading,int sameMapRunNum, 
							 int mapNum, LinkedList<int []> coor,ArrayList<Double> probAvg)
	{
		Grid current = new Grid(grid.columns,grid.rows);
		
		LinkedList<Grid> stateNum = new LinkedList<Grid>();
		stateNum.add(grid);
		current = grid;
		//heatMap(grid,0);
		int [] xArray = coor.get(0);
		int [] yArray = coor.get(1);
		int trueX = xArray[0];			//first element in array has initial coor
		int trueY = yArray[0];
		double errorAmount[] = new double[95];
		for(int i=0;i<action.length;i++)
		{
			Grid condMat = grid;
			condMat = observationModel(grid,reading[i]);
			LinkedList<LinkedList<Cell>> pastNodes = transitionModel(action[i],condMat);
			normalize(condMat);
			Grid transMat = priorBelief(stateNum.get(i),condMat,pastNodes);
			normalize(transMat);
			stateNum.add(transMat);
			
			probAvg.set(i, probAvg.get(i) + transMat.cells[trueX][trueY].value);

			if(i > 4)
			{
				trueX = xArray[i];
				trueY = yArray[i];
				errorAmount[i-5] = MLE(transMat,trueX,trueY);
			}
		}
		
		//LineChart chart = new LineChart();
		//chart.plot(errorAmount);
		//print(grid);
		//this.grid = grid;
		return errorAmount;
	}
	
	public double MLE(Grid grid, int trueX, int trueY)
	{
		double mle = grid.cells[0][0].value;
		int mleX = 0;
		int mleY = 0;
		//loop to find largest prob
		for(int i=0;i<grid.rows;i++)
		{
			for(int j=0;j<grid.columns;j++)
			{
				if(grid.cells[i][j].value > mle)
				{
					mle = grid.cells[i][j].value;
					mleX = i;
					mleY = j;
				}
			}
		}
		double euclidean;
		double eucx = (trueX+1)-(mleX+1);
		double eucy = (trueY+1)-(mleY+1);
		euclidean = Math.sqrt(Math.pow(eucx, 2)+Math.pow(eucy, 2));	
		
		return euclidean;
	}
	
	public void printState(Grid grid,Grid current,String action,String reading,int pairNum,boolean initial)
	{
		LinkedList<String> line = new LinkedList<String>();
		
		for(int i=0;i<grid.rows;i++)
		{
			String str = "";
			for(int j=0;j<grid.columns;j++)
			{
				str = str + "["+current.getCell(i, j).getReading() + "] "+ grid.getCell(i, j).value;
			}
			line.add(str);
		}
		line.add("");
		IO io = new IO(grid);
		io.writePartA(line, action, reading, pairNum,initial);
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
	
}
