import java.io.*;
import java.awt.RenderingHints;

import org.jfree.chart.JFreeChart; 
import org.jfree.chart.ChartFactory; 
import org.jfree.chart.ChartUtilities; 
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.FastScatterPlot;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class LineChart {

	public void plot(float [] errorAmount)
	{
		DefaultCategoryDataset chart = new DefaultCategoryDataset();
		
		float [][] data = new float[2][errorAmount.length];
		for(int i=0;i<errorAmount.length;i++)
		{
			data[0][i] = i+5;
			data[1][i] = (float) errorAmount[i];
		}
		
		JFreeChart lineChartObject = ChartFactory.createLineChart(
				"Average Error", "Iteration", "Error Distance Amount", 
				chart,PlotOrientation.VERTICAL,true,true,false);
		
        final NumberAxis domainAxis = new NumberAxis("Number of Iterations");
        domainAxis.setAutoRangeIncludesZero(false);
        final NumberAxis rangeAxis = new NumberAxis("Average Error Amount");
        rangeAxis.setAutoRangeIncludesZero(true);
        final FastScatterPlot plot = new FastScatterPlot(data, domainAxis, rangeAxis);
        JFreeChart scatter = new JFreeChart("Average Error Amount", plot);
		
        //scatter.getRenderingHints().put
        //(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
		int width = 800;
		int height = 800;
		try{
		File lineChart = new File("ErrorGraph\\ErrorAvg.jpeg");
	    ChartUtilities.saveChartAsJPEG(lineChart ,scatter, width ,height);
		}
		catch(IOException i)
		{
			i.printStackTrace();
		}

	}
	
	public void plotProbAvg(float [] errorAmount)
	{		
		float [][] data = new float[2][errorAmount.length];
		for(int i=0;i<errorAmount.length;i++)
		{
			data[0][i] = i;
			data[1][i] = (float) errorAmount[i];
		}
		
        final NumberAxis domainAxis = new NumberAxis("Number of Iterations");
        domainAxis.setAutoRangeIncludesZero(false);
        final NumberAxis rangeAxis = new NumberAxis("Average Probability");
        rangeAxis.setAutoRangeIncludesZero(true);
        final FastScatterPlot plot = new FastScatterPlot(data, domainAxis, rangeAxis);
        JFreeChart scatter = new JFreeChart("Average Probability", plot);
		
        //scatter.getRenderingHints().put
        //(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
		int width = 800;
		int height = 800;
		try{
		File lineChart = new File("ErrorGraph\\ProbAvg.jpeg");
	    ChartUtilities.saveChartAsJPEG(lineChart ,scatter, width ,height);
		}
		catch(IOException i)
		{
			i.printStackTrace();
		}

	}
}
