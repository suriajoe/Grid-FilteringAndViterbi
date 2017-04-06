import javafx.scene.layout.AnchorPane;

public class Cell extends AnchorPane
{
	int column;
	int row;
	double value = 1; //blocked = 0, normal = 1, Hard to Traverse = 2, highway = 3
	int type = 1; //blocked = 0, normal = 1, Hard to Traverse = 2, highway = 3
	
	
	public Cell(int column, int row)
	{
		this.column = column;
		this.row = row;
		getStyleClass().add("cell");
		
		setOpacity(0.9);
	}
	
    public Cell(int column, int row, double value, int type)
    {
     	this.column = column;
     	this.row = row;
     	this.value = value;
     	this.type = type;
     	
     	getStyleClass().add("cell");
     	
     	setOpacity(0.9);
    }
	
    public int getColumn()
    {
    	return this.column;
    }
    
    public void setColumn(int col)
    {
    	this.column = col;
    }
    
    public int getRow()
    {
    	return this.row;
    }

    public void setRow(int r)
    {
    	this.row = r;
    }
	
    public double getValue()
    {
    	return this.value;
    }
    
    public void setValue(double v)
    {
    	this.value = v;
    }
    
    public int getType()
    {
    	return this.type;
    }
    
    public void setType(int t)
    {
    	this.type = t;
    }
    
    public void normal()
    {
    	getStyleClass().remove("cell-blocked");
    	getStyleClass().remove("cell-hardToTraverse");
    	getStyleClass().remove("cell-highway");
    	getStyleClass().add("cell-normal");
    }
    public void blocked()
    {
    	getStyleClass().add("cell-blocked");
    }
    public void hardToTraverse()
    {
    	getStyleClass().add("cell-hardToTraverse");
    }
    public void highway()
    {
    	getStyleClass().add("cell-highway");
    }
    
    public void hover()
    {
        getStyleClass().remove("cell-hover");
        if(this.type == 0)
        {
        	getStyleClass().remove("cell-blocked");
        }
        else if(this.type == 2)
        {
        	getStyleClass().remove("cell-hardToTraverse");
        }
        else if(this.type == 3)
        {
        	getStyleClass().remove("cell-highway");
        }
        
        getStyleClass().add("cell-hover");
    }
    public void unhover()
    {
        getStyleClass().remove("cell-hover");
        
        if(this.type == 0)
        {
        	getStyleClass().add("cell-blocked");
        }
        else if(this.type == 2)
        {
        	getStyleClass().add("cell-hardToTraverse");
        }
        else if(this.type == 3)
        {
        	getStyleClass().add("cell-highway");
        }
    }

}
