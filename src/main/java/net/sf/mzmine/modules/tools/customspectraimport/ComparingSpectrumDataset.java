/*
 * Copyright 2006-2018 The MZmine 2 Development Team
 * 
 * This file is part of MZmine 2.
 * 
 * MZmine 2 is free software; you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * 
 * MZmine 2 is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with MZmine 2; if not,
 * write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301
 * USA
 */
/**
 * 
 */
package net.sf.mzmine.modules.tools.customspectraimport;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import net.sf.mzmine.modules.tools.tracing.data.LabeledSimpleDataPoint;
import net.sf.mzmine.modules.tools.tracing.data.SimulatedSpectrumDataset;

/**
 * @author Susanne Fürst, susanne.fuerst@mdc-berlin.de, susannefuerst@freenet.de
 *
 */
public class ComparingSpectrumDataset extends SimulatedSpectrumDataset {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private LabeledSimpleDataPoint highestDataPoint;

    /**
     * @param dataPoints
     * @param seriesKey
     */
    public ComparingSpectrumDataset(
            ArrayList<LabeledSimpleDataPoint> dataPoints, String seriesKey) {
        super(dataPoints, seriesKey);
    }

    /**
     * @param dataPoints
     * @param seriesKey
     * @param highestDataPoint
     */
    public ComparingSpectrumDataset(
            ArrayList<LabeledSimpleDataPoint> dataPoints, String seriesKey,
            LabeledSimpleDataPoint highestDataPoint) {
        super(dataPoints, seriesKey);
        this.highestDataPoint = highestDataPoint;
    }

    public static ComparingSpectrumDataset fromCsv(File csvFile,
            int massColumnIndex, int intensityColumnIndex, int labelColumnIndex,
            boolean containsHeader) {
        CSVParser parser;
        ArrayList<LabeledSimpleDataPoint> dataPoints = new ArrayList<LabeledSimpleDataPoint>();
        LabeledSimpleDataPoint highestDataPoint = new LabeledSimpleDataPoint(0,
                0, "");
        String seriesKey = csvFile.getName();
        float highestIntensity = 0f;
        try {
            parser = CSVParser.parse(csvFile, Charset.defaultCharset(),
                    CSVFormat.RFC4180);
            List<CSVRecord> records = parser.getRecords();
            for (CSVRecord csvRecord : records) {
                try {
                    if (csvRecord.getRecordNumber() == 1 && containsHeader) {
                        continue;
                    }
                    double mass = Double
                            .parseDouble(csvRecord.get(massColumnIndex));
                    float intensity = Float
                            .parseFloat(csvRecord.get(intensityColumnIndex));
                    String label = csvRecord.get(labelColumnIndex) != null
                            ? csvRecord.get(labelColumnIndex)
                            : "";
                    LabeledSimpleDataPoint datapoint = new LabeledSimpleDataPoint(
                            mass, intensity, label);
                    if (intensity > highestIntensity) {
                        highestIntensity = intensity;
                        highestDataPoint = datapoint;
                    }
                    dataPoints.add(datapoint);
                } catch (ArrayIndexOutOfBoundsException e) {
                    // there may be an empty row
                    continue;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
            e.printStackTrace();
        }

        return new ComparingSpectrumDataset(dataPoints, seriesKey,
                highestDataPoint);
    }

    public LabeledSimpleDataPoint getHighestDataPoint() {
        return highestDataPoint;
    }

    public void setHighestDataPoint(LabeledSimpleDataPoint highestDataPoint) {
        this.highestDataPoint = highestDataPoint;
    }

    public void normalize(float scaleFactor) {
        ArrayList<LabeledSimpleDataPoint> scaledDataPoints = new ArrayList<LabeledSimpleDataPoint>();
        for (LabeledSimpleDataPoint dataPoint : getDataPoints()) {
            double mass = dataPoint.getMZ();
            float intensity = (float) (dataPoint.getIntensity()
                    / highestDataPoint.getIntensity() * scaleFactor);
            String label = dataPoint.getLabel();
            LabeledSimpleDataPoint scaledDataPoint = new LabeledSimpleDataPoint(
                    mass, intensity, label);
            scaledDataPoints.add(scaledDataPoint);
        }
        highestDataPoint = new LabeledSimpleDataPoint(highestDataPoint.getMZ(),
                scaleFactor, highestDataPoint.getLabel());
        this.dataPoints = scaledDataPoints;
    }

}
