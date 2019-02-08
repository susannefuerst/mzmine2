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

import net.sf.mzmine.parameters.Parameter;
import net.sf.mzmine.parameters.impl.SimpleParameterSet;
import net.sf.mzmine.parameters.parametertypes.DoubleParameter;
import net.sf.mzmine.parameters.parametertypes.StringParameter;

public class SimulateIsotopeIncorporationParameter extends SimpleParameterSet {

    public static final StringParameter CHEMICAL_FORMULA = new ChargedFormulaParameter();
    public static final StringParameter CAPACITY_FORMULA = new CapacityParameter();
    public static final DoubleParameter MIN_ABUNDANCE = new DoubleParameter(
            "Minimal intensity",
            "Simulated peaks below this intensity value will be filtered out.",
            new DecimalFormat("0.0000"), 0.1);
    public static final StringParameter TRACER_1 = new TracerParameter(
            "Tracer 1",
            "The first tracer, e.g C. Required if you used two tracer and want to simulate independent incorporation.");
    public static final DoubleParameter TRACER_1_INC = new IncorporationRateParameter(
            "Incorporation rate 1",
            "Incorporation rate of the first tracer. Required if you used two tracer and want to simulate independent incorporation.");
    public static final StringParameter TRACER_2 = new TracerParameter(
            "Tracer 2",
            "The second tracer, e.g. N. Required if you used two tracer and want to simulate independent incorporation.");
    public static final DoubleParameter TRACER_2_INC = new IncorporationRateParameter(
            "Incorporation rate 2",
            "Incorporation of the second tracer. Required if you used two tracer and want to simulate independent incorporation.");
    public static final DoubleParameter TRACER_BOTH_INC = new IncorporationRateParameter(
            "Incorporation rate for both tracer",
            "Indicates the amount of molecules that has both tracer at once incorporated. Required if you used two tracer and want to simulate independent incorporation.");

    public SimulateIsotopeIncorporationParameter() {
        super(new Parameter[] { CHEMICAL_FORMULA, CAPACITY_FORMULA,
                MIN_ABUNDANCE, TRACER_1, TRACER_1_INC, TRACER_2, TRACER_2_INC,
                TRACER_BOTH_INC });
    }

}
