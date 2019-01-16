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

package net.sf.mzmine.modules.tools.tracing.simulation.isotopeincorporation;

import java.text.DecimalFormat;

import net.sf.mzmine.parameters.impl.SimpleParameterSet;
import net.sf.mzmine.parameters.parametertypes.BooleanParameter;
import net.sf.mzmine.parameters.parametertypes.DoubleParameter;
import net.sf.mzmine.parameters.parametertypes.FormulaParameter;
import net.sf.mzmine.parameters.parametertypes.IntegerParameter;
import net.sf.mzmine.parameters.parametertypes.OptionalParameter;

public class SimulateIsotopeIncorporationParameter extends SimpleParameterSet {

    public static final BooleanParameter DETERMINE_ISOTOPE_COMPOSITION = new BooleanParameter(
            "Determine isotope composition",
            "If checked, the isotope, that induced a certain peak will be assigned to it.",
            false);
    public static final IntegerParameter CHARGE = new IntegerParameter("Charge",
            "The absolute value of the charge. The simulation converts it to a negative value. So by now only negative charges are supported.",
            1);
    public static final FormulaParameter FORMULA = new FormulaParameter();
    public static final CapacityParameter CAPACITY = new CapacityParameter();
    public static final DoubleParameter MIN_INTENSITY = new DoubleParameter(
            "Minimal intensity",
            "Simulated peaks below this intensity value will be filtered out.",
            new DecimalFormat("0.0000"), 0.1);
    public static final IncorporationRateParameter TOTAL_INCORPORATION = new IncorporationRateParameter(
            "Incorporation rate",
            "The total incorporation rate to simulate not an independent incorporation.");
    public static final OptionalParameter<TracerParameter> TRACER_1 = new OptionalParameter<TracerParameter>(
            new TracerParameter("Tracer 1",
                    "The first tracer, e.g C. Required if you used two tracer and want to simulate independent incorporation."));
    public static final OptionalParameter<IncorporationRateParameter> TRACER_1_INC = new OptionalParameter<IncorporationRateParameter>(
            new IncorporationRateParameter("Incorporation rate 1",
                    "Incorporation rate of the first tracer. Required if you used two tracer and want to simulate independent incorporation."));
    public static final OptionalParameter<TracerParameter> TRACER_2 = new OptionalParameter<TracerParameter>(
            new TracerParameter("Tracer 2",
                    "The second tracer, e.g. N. Required if you used two tracer and want to simulate independent incorporation."));
    public static final OptionalParameter<IncorporationRateParameter> TRACER_2_INC = new OptionalParameter<IncorporationRateParameter>(
            new IncorporationRateParameter("Incorporation rate 2",
                    "Incorporation of the second tracer. Required if you used two tracer and want to simulate independent incorporation."));
    public static final OptionalParameter<IncorporationRateParameter> TRACER_BOTH_INC = new OptionalParameter<IncorporationRateParameter>(
            new IncorporationRateParameter("Incorporation rate for both tracer",
                    "Indicates the amount of molecules that has both tracer at once incorporated. Required if you used two tracer and want to simulate independent incorporation."));

}
