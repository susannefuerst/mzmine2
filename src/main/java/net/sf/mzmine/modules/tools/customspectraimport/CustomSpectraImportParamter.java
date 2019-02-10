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

import net.sf.mzmine.parameters.Parameter;
import net.sf.mzmine.parameters.impl.SimpleParameterSet;
import net.sf.mzmine.parameters.parametertypes.BooleanParameter;
import net.sf.mzmine.parameters.parametertypes.IntegerParameter;
import net.sf.mzmine.parameters.parametertypes.filenames.FileNameParameter;

/**
 * @author Susanne Fürst, susanne.fuerst@mdc-berlin.de, susannefuerst@freenet.de
 *
 */
public class CustomSpectraImportParamter extends SimpleParameterSet {

    public static final FileNameParameter CSV_SPECTRUM_FILE = new FileNameParameter(
            "Spectrum file",
            "Name of file that contains comparing spectrum information.");
    public static final IntegerParameter MASS_COLUMN_INDEX = new IntegerParameter(
            "Mass column index",
            "Index of the column containing the mass values (0 based).", 1,
            true);
    public static final IntegerParameter INTENSITY_COLUMN_INDEX = new IntegerParameter(
            "Intensity column index",
            "Index of the column containing the intensity values (0 based).", 2,
            true);
    public static final IntegerParameter LABEL_COLUMN_INDEX = new IntegerParameter(
            "Label column index",
            "Index of the column containing mass ids (optional, 0 based).",
            null, false);
    public static final BooleanParameter CONTAINS_HEADER = new BooleanParameter(
            "Header info",
            "Declares whether or not the csv file contains header information");

    public CustomSpectraImportParamter() {
        super(new Parameter[] { CSV_SPECTRUM_FILE, MASS_COLUMN_INDEX,
                INTENSITY_COLUMN_INDEX, LABEL_COLUMN_INDEX, CONTAINS_HEADER });
    }
}
