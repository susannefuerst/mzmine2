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

import java.awt.Color;
import java.io.IOException;

import io.github.msdk.isotopes.isotopepattern.TracedIsotopePatternGeneratorAlgorithm;
import io.github.msdk.isotopes.isotopepattern.impl.TracedIsotopePattern;
import net.sf.mzmine.modules.tools.tracing.data.SimulatedSpectrumDataset;
import net.sf.mzmine.modules.visualization.spectra.SpectraVisualizerWindow;
import net.sf.mzmine.project.impl.RawDataFileImpl;
import net.sf.mzmine.taskcontrol.Task;
import net.sf.mzmine.taskcontrol.TaskPriority;
import net.sf.mzmine.taskcontrol.TaskStatus;

public class SimulateIsotopeIncorporationTask implements Task {

    private TaskStatus status = TaskStatus.WAITING;
    private String errorMessage;
    private SimulateIsotopeIncorporationParameter parameter;

    public SimulateIsotopeIncorporationTask(
            SimulateIsotopeIncorporationParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public void run() {
        SpectraVisualizerWindow newWindow;
        try {
            TracedIsotopePattern pattern = simulatePattern(parameter);
            SimulatedSpectrumDataset dataset = createDataset(parameter,
                    pattern);
            newWindow = new SpectraVisualizerWindow(
                    new RawDataFileImpl("simulatedDataFile"));
            newWindow.getSpectrumPlot().addDataSet(dataset, Color.green, true);
            newWindow.setVisible(true);
            status = TaskStatus.FINISHED;
        } catch (Exception e) {
            status = TaskStatus.ERROR;
            errorMessage = e.toString();
            return;
        }

    }

    public static TracedIsotopePattern simulatePattern(
            SimulateIsotopeIncorporationParameter parameter)
            throws IOException {
        String formula = parameter
                .getParameter(
                        SimulateIsotopeIncorporationParameter.CHEMICAL_FORMULA)
                .getValue();
        String capacity = parameter
                .getParameter(
                        SimulateIsotopeIncorporationParameter.CAPACITY_FORMULA)
                .getValue();
        Double minAbundance = parameter
                .getParameter(
                        SimulateIsotopeIncorporationParameter.MIN_ABUNDANCE)
                .getValue();
        String tracer1 = parameter
                .getParameter(SimulateIsotopeIncorporationParameter.TRACER_1)
                .getValue();
        Double tracer1IncRate = parameter
                .getParameter(
                        SimulateIsotopeIncorporationParameter.TRACER_1_INC)
                .getValue();
        String tracer2 = parameter
                .getParameter(SimulateIsotopeIncorporationParameter.TRACER_2)
                .getValue();
        Double tracer2IncRate = parameter
                .getParameter(
                        SimulateIsotopeIncorporationParameter.TRACER_2_INC)
                .getValue();
        Double bothIncRate = parameter
                .getParameter(
                        SimulateIsotopeIncorporationParameter.TRACER_BOTH_INC)
                .getValue();
        return TracedIsotopePatternGeneratorAlgorithm.simulateTracedPattern(
                formula, capacity, tracer1, tracer2, tracer1IncRate,
                tracer2IncRate, bothIncRate, minAbundance, 100.0f, 0.00005);
    }

    public static SimulatedSpectrumDataset createDataset(
            SimulateIsotopeIncorporationParameter parameter,
            TracedIsotopePattern pattern) throws IOException {
        String formula = parameter
                .getParameter(
                        SimulateIsotopeIncorporationParameter.CHEMICAL_FORMULA)
                .getValue();
        String capacity = parameter
                .getParameter(
                        SimulateIsotopeIncorporationParameter.CAPACITY_FORMULA)
                .getValue();
        SpectraVisualizerWindow newWindow = new SpectraVisualizerWindow(
                new RawDataFileImpl("simulatedDataFile"));
        SimulatedSpectrumDataset dataSet = new SimulatedSpectrumDataset(pattern,
                "Simulated pattern for " + formula + " with traced "
                        + capacity);
        return dataSet;
    }

    @Override
    public String getTaskDescription() {
        return "Simulate isotope incorporation";
    }

    @Override
    public double getFinishedPercentage() {
        return 0;
    }

    @Override
    public TaskStatus getStatus() {
        return status;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public TaskPriority getTaskPriority() {
        return TaskPriority.NORMAL;
    }

    @Override
    public void cancel() {
        status = TaskStatus.CANCELED;
    }

}
