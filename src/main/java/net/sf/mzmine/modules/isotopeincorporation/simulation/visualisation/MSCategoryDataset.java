package net.sf.mzmine.modules.isotopeincorporation.simulation.visualisation;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.jfree.data.category.DefaultCategoryDataset;

import net.sf.mzmine.modules.isotopeincorporation.simulation.data.MSDatabase;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.MassSpectrum;
import net.sf.mzmine.modules.isotopeincorporation.simulation.data.constants.MSBarChartType;

@SuppressWarnings("serial")
public class MSCategoryDataset extends DefaultCategoryDataset {
	
	public static final String NATURAL_INC_LABEL = "0% incorporation";
	public static final String COMPLETE_INC_LABEL = "100% incorporation";
	
	private MSDatabase msDatabase;
	
	public MSCategoryDataset(MSDatabase msDatabase, MSBarChartType msBarChartType) {
		this.setMsDatabase(msDatabase);
		if (msBarChartType.equals(MSBarChartType.ALL_SPECTRA)) {
			LinkedHashMap<Entry<Double, String>, Double> mergedSpectraDataEntries = mergeAndSortByMassForBarChart(msDatabase);
			for (Entry<Entry<Double,String>, Double> entry : mergedSpectraDataEntries.entrySet()) {
				Double massCategory = entry.getValue();
				Double frequencyValue = entry.getKey().getKey();
				String incorporationType = entry.getKey().getValue();
				setValue(frequencyValue, incorporationType, massCategory);
			}
		} else if (msBarChartType.equals(MSBarChartType.NATURAL_SPECTRUM_ONLY)) {
			MassSpectrum naturalSpectrum = msDatabase.getNaturalSpectrum();
			for (Entry<Double, Double> entry : naturalSpectrum.entrySet()) {
				setValue(entry.getValue(), NATURAL_INC_LABEL, entry.getKey());
			}		
		} else if (msBarChartType.equals(MSBarChartType.PARTIALLY_LABELED_SPECTRUM_ONLY)) {
			MassSpectrum mixedSpectrum = msDatabase.getMixedSpectrum();
			for (Entry<Double, Double> entry : mixedSpectrum.entrySet()) {
				setValue(entry.getValue(), msDatabase.incorporationPerCent() + "% incorporation", entry.getKey());
			}		
		} else if (msBarChartType.equals(MSBarChartType.COMPLETELY_LABELED_SPECTRUM_ONLY)) {
			MassSpectrum markedSpectrum = msDatabase.getMarkedSpectrum();
			for (Entry<Double, Double> entry : markedSpectrum.entrySet()) {
				setValue(entry.getValue(), COMPLETE_INC_LABEL, entry.getKey());
			}
		}
	}
	
	/**
	 * Masses in the BarChart are realized as categories. To force them to be listed in ascending order the map, returned by this
	 * method may be used. The values of the returned map are the masses obtained from all the spectra (natural, marked and mixed).
	 * The map is sorted by this masses. The corresponding keys are entries itself, where the frequency of the mass is mapped to the
	 * corresponding incorporation type, the mass is derived from.
	 * Incorporation types are:
	 * 0% incorporation (= natural)
	 * 100% incorporation ( = marked)
	 * xy% incorporation (= mixed, where xy represents the incorporation that corresponds to the mixed spectrum)
	 * @param msDatabase
	 * @return Frequency (value) and catogory entries mapped to masses, sorted ascending by mass.
	 */
	private LinkedHashMap<Entry<Double, String>, Double> mergeAndSortByMassForBarChart(MSDatabase msDatabase) {
		MassSpectrum naturalSpectrum = msDatabase.getNaturalSpectrum();
		MassSpectrum markedSpectrum = msDatabase.getMarkedSpectrum();
		MassSpectrum mixedSpectrum = msDatabase.getMixedSpectrum();
		//TODO: try to find a better solution because the frequencyAndCategoryEntry need not be unique
		LinkedHashMap<Entry<Double,String>,Double> mergedSpectra = new LinkedHashMap<>();
		for (Entry<Double, Double> entry : naturalSpectrum.entrySet()) {
			Entry<Double,String> frequencyAndCategoryEntry = 
					new AbstractMap.SimpleEntry<Double,String>(entry.getValue(), NATURAL_INC_LABEL);
			mergedSpectra.put(frequencyAndCategoryEntry, entry.getKey());
		}
		for (Entry<Double, Double> entry : markedSpectrum.entrySet()) {
			Entry<Double,String> frequencyAndCategoryEntry = 
					new AbstractMap.SimpleEntry<Double,String>(entry.getValue(), COMPLETE_INC_LABEL);
			mergedSpectra.put(frequencyAndCategoryEntry, entry.getKey());
		}
		int incorporationPerCent = msDatabase.incorporationPerCent();
		for (Entry<Double, Double> entry : mixedSpectrum.entrySet()) {
			Entry<Double,String> frequencyAndCategoryEntry = 
					new AbstractMap.SimpleEntry<Double,String>(entry.getValue(), incorporationPerCent + "% incorporation");
			mergedSpectra.put(frequencyAndCategoryEntry, entry.getKey());
		}
		List<Entry<Entry<Double, String>, Double>> entryList = new ArrayList<>(mergedSpectra.entrySet());
		entryList.sort(Entry.comparingByValue());
		LinkedHashMap<Entry<Double,String>,Double> sortedList = new LinkedHashMap<>();
		for (Entry<Entry<Double, String>, Double> entry : entryList) {
			sortedList.put(entry.getKey(), entry.getValue());
        }
        return sortedList;
	}

	/**
	 * @return the msDatabase
	 */
	public MSDatabase getMsDatabase() {
		return msDatabase;
	}

	/**
	 * @param msDatabase the msDatabase to set
	 */
	public void setMsDatabase(MSDatabase msDatabase) {
		this.msDatabase = msDatabase;
	}

}
