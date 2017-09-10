package com.healthiq.takehome;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.commons.lang3.time.DateUtils;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

public class TimeSeriesChart extends JFrame {

	private static final long serialVersionUID = 1L;

	public TimeSeriesChart(double[] glycemicIndex, double[] glycation) {
		initUI(glycemicIndex, glycation);
		setVisible(true);
	}

	private void initUI(double[] glycemicIndex, double[] glycation) {
		JFreeChart chart = createChart(glycemicIndex, glycation);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize( new java.awt.Dimension(960, 540));
		chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		chartPanel.setBackground(Color.white);
		add(chartPanel);

		pack();
		setTitle("Blood Sugar");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private JFreeChart createChart(double[] glycemicIndex, double[] glycation) {
		final TimeSeries glycemicSeries = new TimeSeries("Glycemic Index");
		final TimeSeries glycationSeries = new TimeSeries("Glycation");

		Date today = new Date();
		today = DateUtils.truncate(today, Calendar.DATE);

		for (int i = 0; i < glycemicIndex.length; i++) {
			Minute minute = new Minute(DateUtils.addMinutes(today, i));
			glycemicSeries.add(minute, glycemicIndex[i]);
			glycationSeries.add(minute, glycation[i]);
		}
		
		TimeSeriesCollection glycemicSeriesDataSet = new TimeSeriesCollection();
		glycemicSeriesDataSet.addSeries(glycemicSeries);
		
		TimeSeriesCollection glycationSeriesDataSet = new TimeSeriesCollection();
		glycationSeriesDataSet.addSeries(glycationSeries);

		XYPlot plot = new XYPlot();
		plot.setDataset(0, glycemicSeriesDataSet);
		plot.setDataset(1, glycationSeriesDataSet);
		
		XYSplineRenderer glycemicRenderer = new XYSplineRenderer();
		glycemicRenderer.setSeriesStroke(0, new BasicStroke(1));
		plot.setRenderer(0, glycemicRenderer);
		plot.setRangeAxis(0, new DateAxis("Glycemic Index"));
		
		XYSplineRenderer glycationRenderer = new XYSplineRenderer();
		glycationRenderer.setSeriesPaint(1, Color.BLUE);
		glycationRenderer.setSeriesStroke(1, new BasicStroke(1));
		plot.setRenderer(1, glycationRenderer);
		plot.setRangeAxis(1, new DateAxis("Glycation"));

		plot.setDomainAxis(new DateAxis("Time"));

	    //Map the data to the appropriate axis
	    plot.mapDatasetToRangeAxis(0, 0);
	    plot.mapDatasetToRangeAxis(1, 1);   		
		
		plot.setBackgroundPaint(Color.white);
		plot.setRangeGridlinesVisible(true);
		plot.setDomainGridlinesVisible(true);

	    //generate the chart
	    JFreeChart chart = new JFreeChart("MyPlot", getFont(), plot, true);
	    chart.setBackgroundPaint(Color.WHITE);
	    JPanel chartPanel = new ChartPanel(chart);
	    chart.setTitle(new TextTitle("Blood Sugar", new Font("Serif", Font.BOLD, 18)));

		return chart;
	}

}
