import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.util.List;

public class IO {

	Grid grid;
	double width = 600;
	double height = 600;
	
	public IO(Grid grid)
	{
		this.grid = grid;
	}
	public IO()
	{
		this.grid = null;
	}
	public void writeGroundTruth(String initialCoor, String[] coordinate, String[] actionType, 
			String[] sensor, int sameMapRunNum, int mapNum)
	{
		String str;
		LinkedList<String> line = new LinkedList<String>();
		line.add("x0y0:" +initialCoor);
		
		for(int i=0;i<coordinate.length;i++)
		{
			line.add("x"+(i+1)+"y"+(i+1)+":"+coordinate[i]);
		}
		for(int i=0;i<actionType.length;i++)
		{
			line.add("a"+(i+1)+":"+actionType[i]);
		}
		str = "e0:";
		for(int i=0;i<sensor.length-1;i++)
		{
			str = str + sensor[i]+ " e" +(i+1)+":";
		}
		str = str + sensor[99] + "";
		line.add(str);
		
		Path file;
		if(sameMapRunNum%10 == 0 && mapNum == 0)									//10
			file = Paths.get("GroundTruthData\\GroundTruth" +sameMapRunNum+".txt"); 
		else if(sameMapRunNum%10 == 0)												//20,30 etc.
			file = Paths.get("GroundTruthData\\GroundTruth" +mapNum + 0 +".txt"); 
		else																		//rest
			file = Paths.get("GroundTruthData\\GroundTruth" + (mapNum-1)+sameMapRunNum+".txt"); 
		
		try{
			Files.write(file, line, Charset.forName("UTF-8"));
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public LinkedList<String []> readGroundTruth(int sameMapRunNum, int mapNum,LinkedList<int[]> coor)
	{
		LinkedList<String []> list = new LinkedList<String []>();

		try{
			List<String> line;
			Path file;
			if(sameMapRunNum%10 == 0 && mapNum == 0)									//10
				file = Paths.get("GroundTruthData\\GroundTruth" +sameMapRunNum+".txt"); 
			else if(sameMapRunNum%10 == 0)												//20,30 etc.
				file = Paths.get("GroundTruthData\\GroundTruth" +mapNum + 0 +".txt"); 
			else																		//rest
				file = Paths.get("GroundTruthData\\GroundTruth" + (mapNum-1)+sameMapRunNum+".txt"); 
			
			line = Files.readAllLines(file, Charset.forName("UTF-8"));
			
			int [] x = new int[101];
			int [] y = new int[101];
			String [] actions = new String[100];
			String [] reading = new String[100];
			if(!line.isEmpty())
			{
				//store coordinates in array
				for(int i=0;i<101;i++)
				{
					String str;
					String[] strArray;
					int paren;
					str = line.get(i);
					paren = str.indexOf("(");
					str = str.substring(paren);
					str = str.replace("(", "");
					str = str.replace(")", "");
					str = str.trim();
					strArray = str.split(",");
					x[i] = Integer.parseInt(strArray[0]);
					y[i] = Integer.parseInt(strArray[1]);
				}
				for(int i=101,j=0;i<202;i++,j++)
				{
					if(line.get(i).contains("U"))
						actions[j] = "U";
					else if(line.get(i).contains("D"))
						actions[j] = "D";
					else if(line.get(i).contains("R"))
						actions[j] = "R";
					else if(line.get(i).contains("L"))
						actions[j] = "L";
				}
				for(int j=0,k=3;j<10;j++,k+=5)
				{
					reading[j] = String.valueOf(line.get(201).charAt(k));
				}
				for(int j=10,k=54;j<100;j++,k+=6)
				{
					reading[j] = String.valueOf(line.get(201).charAt(k));
				}
				list.add(actions);
				list.add(reading);
				coor.add(x);
				coor.add(y);
			}
		}
		catch(IOException i)
		{
			i.printStackTrace();
		}
		return list;
	}
	
	public void writeLargeMap(int mapNum)
	{
		LinkedList<String> line = new LinkedList<String>();
		
		for(int i=0;i<grid.rows;i++)
		{
			String str = "";
			for(int j=0;j<grid.columns;j++)
			{
				str += grid.getCell(i, j).getReading();
			}
			line.add(str);
		}
		Path file = Paths.get("LargeMap\\Map" + mapNum + ".txt");
		
		try{
			Files.write(file, line, Charset.forName("UTF-8"));
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public Grid readLargeMap(int mapNum)
	{
	  try{
		Path file = Paths.get("LargeMap\\Map" + mapNum + ".txt");
		List<String> line;
		line = Files.readAllLines(file, Charset.forName("UTF-8"));
		
		if(!line.isEmpty())
		{
			grid = new Grid(line.size(),line.get(0).length());
		    // fill grid with empty cells
		    for(int row = 0; row < line.size(); row++) 
		     {
		        for(int column = 0; column < line.get(0).length(); column++) 
		        {
		           Cell cell = new Cell(column, row, 0, "");

		           grid.add(cell, column, row);
		        }
		    }
			
			int numOfBlockedCells = 0;
			int numOfTotalCells = line.size() * line.get(0).length();
			double cellProb; 
			//count num of Blocked Cells
			for(int i=0;i<line.size();i++)
			{
				for(int j=0;j<line.get(0).length();j++)
				{
					if(line.get(i).charAt(j) == 'B')
						numOfBlockedCells++;
				}
			}
			
			cellProb = 0.00011; //estimate for 100x100 grid
			//System.out.println(cellProb);
			for(int i=0;i<line.size();i++)
			{
				for(int j=0;j<line.get(0).length();j++)
				{
					switch(line.get(i).charAt(j))
					{
					case 'N': grid.cells[i][j].setValue(cellProb);
							  grid.cells[i][j].setType(1);
							  grid.cells[i][j].setReading("N");
							  break;
					case 'T': grid.cells[i][j].setValue(cellProb);
							  grid.cells[i][j].setType(2);
					  		  grid.cells[i][j].setReading("T");
					  		  break;
					case 'H': grid.cells[i][j].setValue(cellProb);
							  grid.cells[i][j].setType(3);
					 		  grid.cells[i][j].setReading("H");
					 		  break;
					case 'B': grid.cells[i][j].setValue(0);
							  grid.cells[i][j].setType(0);
					  		  grid.cells[i][j].setReading("B");
					  		  break;
					}
				}
			}
			return grid;
		}
	  }
	  catch(IOException i)
	  {
		  i.printStackTrace();
	  }
	return grid;
		
	}

	public void writeGroundTruthPath(Grid grid,int sameMapRunNum, int mapNum, int iterationNum,
									 String[] action, String[] reading)
	{
		LinkedList<String> line = new LinkedList<String>();
		
		String actionPath = "a1-a" + (iterationNum+1)+ ":";
		for(int i=0;i<iterationNum+1;i++)
		{
			actionPath = actionPath + " " + action[i] ;
		}
		line.add(actionPath);
		
		String readingPath = "e1-e" + (iterationNum+1)+ ":";
		for(int i=0;i<iterationNum+1;i++)
		{
			readingPath = readingPath + " " + reading[i];
		}
		line.add(readingPath);		
		
		Path file = Paths.get("GroundTruthPath\\Map" + mapNum + " GroundTruthData" 
							 + sameMapRunNum + " Iteration" + (iterationNum+1) + ".txt");
		
		try{
			Files.write(file, line, Charset.forName("UTF-8"));
		}
		catch(IOException i)
		{
			i.printStackTrace();
		}
	}
	public void writePartA(LinkedList<String> line,String action,String reading,int pairNum)
	{
		line.addFirst("Action:"+ action+", Reading: " + reading +" -Probability Pairing");
		Path file = Paths.get("PartA\\Map" + ".txt");
		
		try{
			if(pairNum == 0)
				Files.write(file, line, Charset.forName("UTF-8"));
			else if(pairNum == 3)
			{
				line.clear();
				line.add("Final Action:"+ action+", Reading: " + reading +" -Probability Pairing");
				Files.write(file, line, Charset.forName("UTF-8"),StandardOpenOption.APPEND);
			}
			else	
				Files.write(file, line, Charset.forName("UTF-8"),StandardOpenOption.APPEND);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	public void write(Grid next)
	{
		LinkedList<String> line = new LinkedList<String>();
		
		//line.add("Final Result");
		for(int i=0;i<next.rows;i++)
		{
			String str = "";
			for(int j=0;j<next.columns;j++)
			{
				str = str + "["+next.getCell(i, j).getReading() + "] "+ next.getCell(i, j).value;
			}
			line.add(str);
		}
		Path file = Paths.get("PartA\\Map" + ".txt");
		
		try{
			Files.write(file, line, Charset.forName("UTF-8"),StandardOpenOption.APPEND);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	public void writePartB(LinkedList<String> line,String action,String reading,int pairNum)
	{
		line.addFirst("Action:"+ action+", Reading: " + reading +" -Probability Pairing");
		Path file = Paths.get("PartB\\Map" + ".txt");
		
		try{
			if(pairNum == 0)
				Files.write(file, line, Charset.forName("UTF-8"));
			else if(pairNum == 3)
			{
				line.clear();
				line.add("Final Action:"+ action+", Reading: " + reading +" -Probability Pairing");
				Files.write(file, line, Charset.forName("UTF-8"),StandardOpenOption.APPEND);
			}
			else	
				Files.write(file, line, Charset.forName("UTF-8"),StandardOpenOption.APPEND);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	public void writeB(Grid next)
	{
		LinkedList<String> line = new LinkedList<String>();
		
		//line.add("Final Result");
		for(int i=0;i<next.rows;i++)
		{
			String str = "";
			for(int j=0;j<next.columns;j++)
			{
				str = str + "["+next.getCell(i, j).getReading() + "] "+ next.getCell(i, j).value;
			}
			line.add(str);
		}
		Path file = Paths.get("PartB\\Map" + ".txt");
		
		try{
			Files.write(file, line, Charset.forName("UTF-8"),StandardOpenOption.APPEND);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void readMap()
	{
		
	}
}
