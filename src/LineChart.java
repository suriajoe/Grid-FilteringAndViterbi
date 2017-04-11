import java.io.*;

import org.jfree.chart.JFreeChart; 
import org.jfree.chart.ChartFactory; 
import org.jfree.chart.ChartUtilities; 
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class LineChart {

	public void plot(double [] errorAmount)
	{
		DefaultCategoryDataset chart = new DefaultCategoryDataset();
		
		for(int i=0;i<errorAmount.length;i++)
		{
			chart.addValue(errorAmount[i], "error distance amount", "i+5");
		}
		
		JFreeChart lineChartObject = ChartFactory.createLineChart(
				"Average Error", "Iteration", "Error Distance Amount", 
				chart,PlotOrientation.VERTICAL,true,true,false);
		
		int width = 800;
		int height = 800;
		File lineChart = new File("ErrorGraph\\LineChart.jpeg");
	    ChartUtilities.saveChartAsJPEG(lineChart ,lineChartObject, width ,height);

	}
}
