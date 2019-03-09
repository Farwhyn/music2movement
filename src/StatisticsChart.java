import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.jogamp.opengl.GL2;

@SuppressWarnings("serial")
public class StatisticsChart extends PositionedChart {
	
	JTable statisticsTable;
	JLabel statisticsLabel;
	WidgetDataset datasetWidget;
	WidgetTextfieldInteger sampleCountWidget;
	
	public static ChartFactory getFactory() {
		
		return new ChartFactory() {
			
			WidgetDatasets datasetsWidget;
			WidgetTextfieldInteger sampleCountWidget;
			
			@Override public String toString() { return "Statistics Chart"; }
			
			@Override public JPanel[] getWidgets() {

				datasetsWidget = new WidgetDatasets();
				sampleCountWidget = new WidgetTextfieldInteger("Sample Count", 1000, 5, Integer.MAX_VALUE);
	
				JPanel[] widgets = new JPanel[5];
				
				widgets[0] = datasetsWidget;
				widgets[1] = null;
				widgets[2] = sampleCountWidget;
				widgets[3] = null;
				widgets[4] = null;

				return widgets;
				
			}
			
			@Override public int getMinimumSampleCount() {
				
				return 5;
				
			}
			
			@Override public PositionedChart createChart(int x1, int y1, int x2, int y2) {

				int sampleCount      = sampleCountWidget.getValue();
				Dataset[] datasets   = datasetsWidget.getDatasets();
				
				if(datasets.length == 0)
					return null;
				
				StatisticsChart chart = new StatisticsChart(x1, y1, x2, y2, sampleCount, datasets);
				
				return chart;
				
			}
			
			@Override public PositionedChart importChart(int x1, int y1, int x2, int y2, Dataset[] datasets, int sampleCount, String[] lines, int firstLineNumber) {
				
				
				StatisticsChart chart = new StatisticsChart(x1, y1, x2, y2, sampleCount, datasets);

				
				return chart;
				
			}
			
		};
		
	}
	
	
	@Override public String toString() {
		
		return "Statistics";
		
	}

	public StatisticsChart(int x1, int y1, int x2, int y2, int chartDuration, Dataset[] chartInputs) {
		
		super(x1, y1, x2, y2);
		
		// prepare the panel
		setLayout(new BorderLayout());
		
		String[] columnNames = new String[datasets.length];
		for(int i = 0; i < columnNames.length; i++)
			columnNames[i] = datasets[i].name;
		
		statisticsTable = new JTable(new String[13][datasets.length], columnNames);
		statisticsTable.setFont(new Font("Consolas", Font.PLAIN, statisticsTable.getFont().getSize()));
		statisticsTable.setRowHeight((int) statisticsTable.getFont().getStringBounds("Abcdefghijklmnopqrstuvwxyz", new FontRenderContext(null, true, true)).getHeight()); // fix display scaling issue
		statisticsTable.getTableHeader().setReorderingAllowed(false); // prevent users from dragging columns, which breaks the logic below
		
		DefaultTableCellRenderer rightAligned = new DefaultTableCellRenderer();
		rightAligned.setHorizontalAlignment(SwingConstants.RIGHT);
		for(int i = 0; i < statisticsTable.getColumnModel().getColumnCount(); i++)
			statisticsTable.getColumnModel().getColumn(i).setCellRenderer(rightAligned);
		
		statisticsLabel = new JLabel("Statistics (Last " + duration + " Samples)");
		statisticsLabel.setFont(new Font("Geneva", Font.BOLD,  20 * (int) Controller.getDisplayScalingFactor()));
		statisticsLabel.setHorizontalAlignment(SwingConstants.CENTER);
		statisticsLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		JScrollPane scrollableStatisticsTable = new JScrollPane(statisticsTable);
		scrollableStatisticsTable.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		add(statisticsLabel, BorderLayout.NORTH);
		add(scrollableStatisticsTable, BorderLayout.CENTER);
		
		// spawn a thread that draws the chart
		Thread thread = new Thread(new Runnable() {
			@Override public void run() {
				
				while(true) {
					
					long startTime = System.currentTimeMillis();

					// redraw the table					
					int sampleCount = sampleCountWidget.get(datasets);
					int maxX = sampleCount - 1;
					int minX = maxX - duration;
					if(minX < 0) minX = 0;
		
					for(int column = 0; column < datasets.length; column++) {
						
						double[] samples = new double[maxX - minX + 1];
						for(int i = 0; i < samples.length; i++)
							samples[i] = datasets[column].get(i + minX);
						
						DescriptiveStatistics stats = new DescriptiveStatistics(samples);
						String[] rowText = new String[13];
						rowText[0]  = String.format("                    Mean: % 9.3f %s",    stats.getMean(),              datasets[column].unit);
						rowText[1]  = String.format("                 Minimum: % 9.3f %s",    stats.getMin(),               datasets[column].unit);
						rowText[2]  = String.format("                 Maximum: % 9.3f %s",    stats.getMax(),               datasets[column].unit);
						rowText[3]  = String.format("      Standard Deviation: % 9.3f %s",    stats.getStandardDeviation(), datasets[column].unit);
						rowText[4]  = String.format("         90th Percentile: % 9.3f %s",    stats.getPercentile(90.0),    datasets[column].unit);
						rowText[5]  = String.format("         80th Percentile: % 9.3f %s",    stats.getPercentile(80.0),    datasets[column].unit);
						rowText[6]  = String.format("         70th Percentile: % 9.3f %s",    stats.getPercentile(70.0),    datasets[column].unit);
						rowText[7]  = String.format("         60th Percentile: % 9.3f %s",    stats.getPercentile(60.0),    datasets[column].unit);
						rowText[8]  = String.format("(Median) 50th Percentile: % 9.3f %s",    stats.getPercentile(50.0),    datasets[column].unit);
						rowText[9]  = String.format("         40th Percentile: % 9.3f %s",    stats.getPercentile(40.0),    datasets[column].unit);
						rowText[10] = String.format("         30th Percentile: % 9.3f %s",    stats.getPercentile(30.0),    datasets[column].unit);
						rowText[11] = String.format("         20th Percentile: % 9.3f %s",    stats.getPercentile(20.0),    datasets[column].unit);
						rowText[12] = String.format("         10th Percentile: % 9.3f %s",    stats.getPercentile(10.0),    datasets[column].unit);
						int col = column;
						
						SwingUtilities.invokeLater(new Runnable() {
							@Override public void run() {
								
								for(int row = 0; row < rowText.length; row++)
									statisticsTable.setValueAt(rowText[row], row, col);
								
							}
						});
						
					}
					
					// end this thread if we are no longer visible (the user clicked the "Reset" button)
					if(!isShowing())
						break;
					
					// wait if needed before drawing a new frame
					long timeToNextFrame = startTime + Controller.getTargetFramePeriod() - System.currentTimeMillis();
					if(timeToNextFrame <= 0)
						continue;
					else
						try{ Thread.sleep(timeToNextFrame); } catch(Exception e) {}
					
				}
				
			}
			
		});
		String inputNames = "";
		for(int i = 0; i < datasets.length; i++)
			inputNames += datasets[i].name + ", ";
		thread.setName(String.format("StatisticsChart of: %s", inputNames));
		thread.start();

	}


	@Override
	public void drawChart(GL2 gl, int width, int height, int lastSampleNumber, double zoomLevel, int mouseX,
			int mouseY) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String[] exportChart() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void importChart(String[] lines, int firstLineNumber) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JPanel[] getWidgets() {
		// TODO Auto-generated method stub
		JPanel[] widgets = new JPanel[10];
		
		widgets[0] = datasetWidget;
		widgets[1] = sampleCountWidget;
		return widgets;
	}

	
}
