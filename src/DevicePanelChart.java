import javax.swing.JPanel;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.jogamp.opengl.GL2;


public class DevicePanelChart extends PositionedChart {

	public DevicePanelChart(int x1, int y1, int x2, int y2) {
		super(x1, y1, x2, y2);
		yAutoscaleRelativeFrequency = new AutoScale(AutoScale.MODE_EXPONENTIAL, 30, 0.20f);
		yAutoscaleFrequency = new AutoScale(AutoScale.MODE_EXPONENTIAL, 30, 0.20f);
		
		// create the control widgets and event handlers
		datasetsWidget = new WidgetDatasets(newDatasets -> {datasets = newDatasets;
		                                                    bins = new int[datasets.length][binCount];
		                                                    samples = new Samples[datasets.length];
		                                                    for(int i = 0; i < samples.length; i++)
		                                                    	samples[i] = new Samples();
		                                                    });
		
		sampleCountWidget = new WidgetTextfieldInteger("Sample Count",
		                                               SampleCountDefault,
		                                               SampleCountMinimum,
		                                               SampleCountMaximum,
		                                               newSampleCount -> sampleCount = newSampleCount);
		
		binCountWidget = new WidgetTextfieldInteger("Bin Count",
		                                            BinCountDefault,
		                                            BinCountMinimum,
		                                            BinCountMaximum,
		                                            newBinCount -> {binCount = newBinCount;
		                                                            if(datasets != null)
		                                                            	bins = new int[datasets.length][binCount];
		                                                            });
		
		xAxisTypeWidget = new WidgetHistogramXaxisType(xAxisMinimumDefault,
		                                               xAxisMaximumDefault,
		                                               xAxisCenterDefault,
		                                               xAxisLowerLimit,
		                                               xAxisUpperLimit,
		                                               (newXautoscaleMin, newManualMinX) ->     { xAutoscaleMin = newXautoscaleMin; manualMinX = newManualMinX; },
		                                               (newXautoscaleMax, newManualMaxX) ->     { xAutoscaleMax = newXautoscaleMax; manualMaxX = newManualMaxX; },
		                                               (newXaxisIsCentered, newXcenterValue) -> { xAxisIsCentered = newXaxisIsCentered; xCenterValue = newXcenterValue; });
		
		yAxisTypeWidget = new WidgetHistogramYaxisType(yAxisRelativeFrequencyMinimumDefault,
		                                               yAxisRelativeFrequencyMaximumDefault,
		                                               yAxisRelativeFrequencyLowerLimit,
		                                               yAxisRelativeFrequencyUpperLimit,
		                                               yAxisFrequencyMinimumDefault,
		                                               yAxisFrequencyMaximumDefault,
		                                               yAxisFrequencyLowerLimit,
		                                               yAxisFrequencyUpperLimit,
		                                               (newYaxisShowsRelativeFrequency, newYaxisShowsFrequency) -> { yAxisShowsRelativeFrequency = newYaxisShowsRelativeFrequency; yAxisShowsFrequency = newYaxisShowsFrequency; },
		                                               (newYminimumIsZero, newManualMinY) ->                       { yMinimumIsZero = newYminimumIsZero; manualMinY = newManualMinY; },
		                                               (newYautoscaleMax, newManualMaxY) ->                        { yAutoscaleMax = newYautoscaleMax; manualMaxY = newManualMaxY; });
		
		showXaxisTitleWidget = new WidgetCheckbox("Show X-Axis Title",
		                                          true,
		                                          newShowXaxisTitle -> showXaxisTitle = newShowXaxisTitle);
		
		showXaxisScaleWidget = new WidgetCheckbox("Show X-Axis Scale",
		                                          true,
		                                          newShowXaxisScale -> showXaxisScale = newShowXaxisScale);
		
		showYaxisTitleWidget = new WidgetCheckbox("Show Y-Axis Title",
		                                          true,
		                                          newShowYaxisTitle -> showYaxisTitle = newShowYaxisTitle);
		
		showYaxisScaleWidget = new WidgetCheckbox("Show Y-Axis Scale",
		                                          true,
		                                          newShowYaxisScale -> showYaxisScale = newShowYaxisScale);
		
		showLegendWidget = new WidgetCheckbox("Show Legend",
		                                      true,
		                                      newShowLegend -> showLegend = newShowLegend);
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
		return null;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Device Panel Chart";
	}

}
