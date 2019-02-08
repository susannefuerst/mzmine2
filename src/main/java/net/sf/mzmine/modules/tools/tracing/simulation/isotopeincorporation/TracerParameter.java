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

import java.util.Collection;

import net.sf.mzmine.parameters.parametertypes.StringParameter;

public class TracerParameter extends StringParameter {

    private static final String TRACER_PATTERN = "^([0-9]+)([A-Z][a-z]?)$";

    public TracerParameter(String name, String description) {
        super(name, description, null);
    }

    @Override
    public boolean checkValue(Collection<String> errorMessages) {
        boolean superCheck = super.checkValue(errorMessages);

        String value = getValue();
        if (value == null) {
            return superCheck;
        }
        value = value.trim();

        if ((value != null) && (!value.matches(TRACER_PATTERN))) {
            errorMessages
                    .add("\"" + value + "\" is not a valid isotope formula.");
            return false;
        }
        return superCheck;
    }
}
