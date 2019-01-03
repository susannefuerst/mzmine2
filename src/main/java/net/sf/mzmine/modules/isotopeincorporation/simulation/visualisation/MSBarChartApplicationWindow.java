package net.sf.mzmine.modules.isotopeincorporation.simulation.visualisation;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.chart.ui.UIUtils;

import net.sf.mzmine.modules.isotopeincorporation.simulation.data.MSDatabase;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.MSShiftDatabase;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.constants.MSBarChartType;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.constants.PathConstants;

/**
 * A simple application window to visualize the mass spectra of an {@link MSDatabase}.
 * @author sfuerst
 *
 */
@SuppressWarnings("serial")
public class MSBarChartApplicationWindow extends ApplicationFrame {
	
	/**
	 * Creates a simple application window to visualize the mass spectra of an {@link MSDatabase}. If the {@link MSDatabase}
	 * is an instance of {@link MSShiftDatabase} with shift data included, there will be a tool tip for each bar to indicate
	 * the included isotopes.
	 * @param title
	 * @param msDatabase
	 */
	public MSBarChartApplicationWindow(String title, MSDatabase msDatabase, MSBarChartType msBarChartType) {
		super(title);
		JFreeChart chart = MSCategoryBarChartCreator.createMSBarChart(msDatabase, msBarChartType);
		ChartPanel chartPanel = new ChartPanel(chart, false);
		chartPanel.setPreferredSize(new Dimension(1700, 850));
		setContentPane(chartPanel);
	}
	
	/**
	 * MSBarChartApplicationWindow demo method
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		File file = new File(PathConstants.TEST_RESOURCES.toAbsolutePath("MSShiftDatabaseTest01.csv"));
		MSShiftDatabase msDatabase = new MSShiftDatabase(file.getAbsolutePath());
		MSBarChartApplicationWindow demo = new MSBarChartApplicationWindow("Bar Demo 1", msDatabase, MSBarChartType.NATURAL_SPECTRUM_ONLY);
		demo.pack();
		UIUtils.centerFrameOnScreen(demo);
		demo.setVisible(true);
	}

}
