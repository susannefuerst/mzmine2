/*
 * Copyright 2006-2010 The MZmine 2 Development Team
 * 
 * This file is part of MZmine 2.
 * 
 * MZmine 2 is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * MZmine 2 is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * MZmine 2; if not, write to the Free Software Foundation, Inc., 51 Franklin St,
 * Fifth Floor, Boston, MA 02110-1301 USA
 */

package net.sf.mzmine.modules.rawdatamethods.filtering.datasetfilters.preview;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.logging.Logger;

import net.sf.mzmine.data.RawDataFile;
import net.sf.mzmine.data.RawDataFileWriter;
import net.sf.mzmine.data.impl.SimpleParameterSet;
import net.sf.mzmine.main.MZmineCore;
import net.sf.mzmine.modules.rawdatamethods.filtering.datasetfilters.DataSetFiltersParameters;
import net.sf.mzmine.modules.visualization.tic.TICDataSet;
import net.sf.mzmine.modules.visualization.tic.TICPlot;
import net.sf.mzmine.util.Range;
import net.sf.mzmine.util.dialogs.ParameterSetupDialogWithChromatogramPreview;

/**
 * This class extends ParameterSetupDialog class, including a spectraPlot. This
 * is used to preview how the selected raw data filter and his parameters works
 * over the raw data file.
 */
public class RawDataFilterSetupDialog extends ParameterSetupDialogWithChromatogramPreview {

    private Logger logger = Logger.getLogger(this.getClass().getName());
    protected SimpleParameterSet mdParameters;
        
    private RawDataFilter rawDataFilter;
    private int rawDataFilterTypeNumber;

    /**
     * @param parameters
     * @param rawDataFilterTypeNumber
     */
    public RawDataFilterSetupDialog(DataSetFiltersParameters parameters,
            int rawDataFilterTypeNumber) {

        super(
                DataSetFiltersParameters.rawDataFilterNames[rawDataFilterTypeNumber] + "'s parameter setup dialog ",
                parameters.getRawDataFilteringParameters(rawDataFilterTypeNumber),
                null);


        this.rawDataFilterTypeNumber = rawDataFilterTypeNumber;

        // Parameters of local raw data filter to get preview values
        mdParameters = parameters.getRawDataFilteringParameters(rawDataFilterTypeNumber);


    }

	protected void loadPreview(TICPlot ticPlot, RawDataFile dataFile,
			Range rtRange, Range mzRange) {        

		// First, remove all current data sets
		ticPlot.removeAllTICDataSets();
		
		// Add the original raw data file
        int scanNumbers[] = dataFile.getScanNumbers(1, rtRange);
       	TICDataSet ticDataset = new TICDataSet(dataFile, scanNumbers, mzRange, null);
       	ticPlot.addTICDataset(ticDataset);

		// Create an instance of the raw data filter
		String rawDataFilterClassName = DataSetFiltersParameters.rawDataFilterClasses[rawDataFilterTypeNumber];
        try {
            Class rawDataFilterClass = Class.forName(rawDataFilterClassName);
            Constructor rawDataFilterConstruct = rawDataFilterClass.getConstructors()[0];
            rawDataFilter = (RawDataFilter) rawDataFilterConstruct.newInstance(mdParameters);
        } catch (Exception e) {
            MZmineCore.getDesktop().displayErrorMessage(
                    "Error trying to make an instance of raw data filter " + rawDataFilterClassName);
            logger.warning("Error trying to make an instance of raw data filter " + rawDataFilterClassName);
            return;
        }

		try {
	        // Create a new filtered raw data file
			RawDataFileWriter 	rawDataFileWriter = MZmineCore.createNewFile(dataFile.getName() + " filtered");
	        RawDataFile newDataFile = rawDataFilter.getNewDataFiles(dataFile, rawDataFileWriter);
	        
	        // If successful, add the new data file 
	        if (newDataFile != null) {
	            int newScanNumbers[] = newDataFile.getScanNumbers(1, rtRange);
	           	TICDataSet newDataset = new TICDataSet(newDataFile, newScanNumbers, mzRange, null);
	           	ticPlot.addTICDataset(newDataset);
	        }
	        
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

    }
}
