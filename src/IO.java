import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;

public class IO {

	Grid grid;
	
	public IO(Grid grid)
	{
		this.grid = grid;
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
		str = "{";
		for(int i=0;i<sensor.length-1;i++)
		{
			str = str + sensor[i]+ ",";
		}
		str = str + sensor[99] + "}";
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
	
	public void writeMap(int mapNum)
	{
		LinkedList<String> line = new LinkedList<String>();
		
		for(int i=0;i<grid.rows;i++)
		{
			String str = "";
			for(int j=0;j<grid.columns;j++)
			{
				str = str + grid.getCell(i, j).getType();
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
	public void writePartAB(LinkedList<String> line,String action,String reading,int testNum,int pairNum)
	{
		line.addFirst("Action:"+ action+", Reading: " + reading +"- Probability Pairing");
		Path file = Paths.get("PartA\\Map"+ testNum + ".txt");
		
		try{
			if(pairNum == 0)
				Files.write(file, line, Charset.forName("UTF-8"),StandardOpenOption.TRUNCATE_EXISTING);
			Files.write(file, line, Charset.forName("UTF-8"),StandardOpenOption.APPEND);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
	public void write(Grid next,int testNum)
	{
		LinkedList<String> line = new LinkedList<String>();
		
		line.add("Final Result");
		for(int i=0;i<next.rows;i++)
		{
			String str = "";
			for(int j=0;j<next.columns;j++)
			{
				str = str + "["+next.getCell(i, j).getReading() + "] "+ next.getCell(i, j).value;
			}
			line.add(str);
		}
		Path file = Paths.get("PartA\\Map"+ testNum + ".txt");
		
		try{
			Files.write(file, line, Charset.forName("UTF-8"));
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void readGroundTruth()
	{
		
	}
	public void readMap()
	{
		
	}
}
