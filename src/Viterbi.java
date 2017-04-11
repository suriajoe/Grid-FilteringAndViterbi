import java.util.LinkedList;

public class Viterbi 
{
	
	Grid grid;
	
	public Viterbi(Grid grid)
	{
		this.grid = grid;
	}
	
	private static String[] states = {"H","H","T","N","N","N","N","B","H"};
	private static String[] observations = {"N","N","H","H"};
	private static double[] start_probability = { 0.125, 0.125, 0.125, 0.125, 
												  0.125, 0.125, 0.125, 0, 0.125};
	private static double[][] transition_probability = { {0.125, 0.125, 0.125}, {0.125, 
			  0.125, 0.125}, {0.125, 0, 0.125}};
	private static double[][] emission_probability = { { 0.9, 0.9, 0.9 },
			{ 0.9, 0.9, 0.9 }, { 0.9, 0.9, 0.9 } };
	
	
	public void viterbi(Grid grid)
	{
		String[] action = {"Right","Right","Down","Down"};
		String[] reading = {"N","N","H","H"};

		Grid current = new Grid(3,3);
		
		current = grid;
		for(int i=0;i<action.length;i++)
		{
			grid = transistionViterbi(grid, action[i],reading[i]);
			grid = observationViterbi(grid,reading[i]);
			printViterbiState(grid,current,action[i],reading[i],i);
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
	
	public Grid transistionViterbi(Grid grid,String action,String reading)
	{
        // Create transition matrix
		Grid next = new Grid(3,3);
		for(int row = 0;row<3;row++)
		{
			for(int column = 0;column<3;column++)
			{
				Cell cell = new Cell(column, row);
				next.add(cell, column, row);
			}
		}
		
		//Find the Max
        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 3; j++)
            {
                if (grid.cells[i][j].reading == "B")
                {      	
                }
                else
                {
        			double nextState = next.cells[i][j].value;
        			double previousState = grid.cells[i][j].value;
        			double evidenceProb;
                	switch(action)
                	{
                	case "Right":
                		if(j == 2)
                		{
                			next.cells[i][j].value = previousState * 1.0;
                		}
                		else if(j+1 < 3 && grid.cells[i][j+1].reading == "B")
                		{
                			next.cells[i][j].value = previousState * 1.0;
                		}
                		else
                		{
                			if(grid.cells[i][j].reading == reading)
                				evidenceProb = grid.cells[i][j].value * 0.9;
                			else 
                				evidenceProb = grid.cells[i][j].value * 0.05;
                			//calculate argmax 8 states, but reduce it to 2
                			next.cells[i][j].value = Math.max(nextState+previousState*0.1
                			* Math.max(previousState*evidenceProb,previousState),
                			next.cells[i][j+1].value += grid.cells[i][j+1].value * 0.9);
                			
                		}
                		break;
                	case "Down":
                		if(i == 2)
                		{
                			next.cells[i][j].value = previousState * 1.0;
                		}
                    	else if(i+1<3 && grid.cells[i+1][j].reading == "B")
                    	{
                			next.cells[i][j].value = previousState * 1.0;
                    	}
                    	else
                    	{
                			//calculate argmax 8 states, but reduce it to 2
                			next.cells[i][j].value = Math.max(nextState+previousState*0.1,
                			next.cells[i+1][j].value += grid.cells[i+1][j].value * 0.9);
                    	}
                	}
                }
            }
        }
		
		return grid;
	}
	
	
	public Grid observationViterbi(Grid grid,String reading)
	{
		Grid next = new Grid(3,3);
		for(int row = 0;row<3;row++)
		{
			for(int column = 0;column<3;column++)
			{
				Cell cell = new Cell(column, row);
				next.add(cell, column, row);
			}
		}
		
		for(int i=0;i<3;i++)
		{
			for(int j=0;j<3;j++)
			{
				if(grid.getCell(i, j).reading == "B") //blocked
				{
					continue;
				}
				else if(grid.getCell(i, j).reading == reading)
					next.getCell(i, j).setValue(next.getCell(i,j).value + grid.getCell(i,j).value * 0.9);
				else
					next.getCell(i, j).setValue(next.getCell(i,j).value + grid.getCell(i,j).value * 0.05);
			}
		}
		
		normalize(next);
		return next;
	}
	
	public void normalize(Grid next)
	{
		double total[] = new double[next.rows];
		for(int i=0;i<next.rows;i++)
		{
			for(int j=0;j<next.columns;j++)
			{
				total[i] += next.getCell(i, j).value;
			}
		}
		for(int i=0;i<next.rows;i++)
		{
			for(int j=0;j<next.columns;j++)
			{
				next.cells[i][j].value /= total[i];
			}
		}
	}
	
	public void printViterbiState(Grid grid,Grid current,String action,String reading,int pairNum)
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
		io.writePartB(line, action, reading, pairNum);
	}
}
