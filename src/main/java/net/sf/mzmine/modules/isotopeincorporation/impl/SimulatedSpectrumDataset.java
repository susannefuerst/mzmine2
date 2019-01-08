package net.sf.mzmine.modules.isotopeincorporation.impl;

import org.jfree.data.xy.AbstractXYDataset;
import org.jfree.data.xy.IntervalXYDataset;

public class SimulatedSpectrumDataset extends AbstractXYDataset implements IntervalXYDataset {

    private static final long serialVersionUID = 1L;
    private LabeledSimpleDataPoint[] dataPoints;
    private String label;

    public SimulatedSpectrumDataset(LabeledSimpleDataPoint[] dataPoints, String label) {
        this.dataPoints = dataPoints;
        this.label = label;
    }

    public String getDataPointLabel(int series, int item) {
        return ((LabeledSimpleDataPoint) dataPoints[item]).getLabel();
    }

    @Override
    public int getSeriesCount() {
        return 1;
    }

    @Override
    public Comparable<?> getSeriesKey(int series) {
        return label;
    }

    public int getItemCount(int series) {
        return dataPoints.length;
    }

    public Number getX(int series, int item) {
        return dataPoints[item].getMZ();
    }

    public Number getY(int series, int item) {
        return dataPoints[item].getIntensity();
    }

    public Number getEndX(int series, int item) {
        return getX(series, item).doubleValue();
    }

    public double getEndXValue(int series, int item) {
        return getX(series, item).doubleValue();
    }

    public Number getEndY(int series, int item) {
        return getY(series, item);
    }

    public double getEndYValue(int series, int item) {
        return getYValue(series, item);
    }

    public Number getStartX(int series, int item) {
        return getX(series, item).doubleValue();
    }

    public double getStartXValue(int series, int item) {
        return getX(series, item).doubleValue();
    }

    public Number getStartY(int series, int item) {
        return getY(series, item);
    }

    public double getStartYValue(int series, int item) {
        return getYValue(series, item);
    }

}
