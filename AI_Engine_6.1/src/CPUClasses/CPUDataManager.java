package CPUClasses;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.math3.linear.RealMatrix;

import AI_Engine.AIEngine;

public class CPUDataManager {

	public void saveAI(int AI) {
		try {
			PrintWriter save = new PrintWriter(
					AI + ". AI ("+new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())+").txt");
			save.println(AIEngine.layout.length);
			for (int i = 0; i < AIEngine.layout.length; i++) {
				save.println(AIEngine.layout[i]);
			}
			RealMatrix[] weights = ((CPUAI) AIEngine.AIs[AI]).getWeights();
			for (int i = 0; i < weights.length; i++) {
				for (int j = 0; j < weights[i].getColumnDimension(); j++) {
					for (int k = 0; k < weights[i].getRowDimension(); k++) {
						save.println(weights[i].getEntry(k, j));
					}
				}
			}
			RealMatrix[] baises = ((CPUAI) AIEngine.AIs[AI]).getBaises();
			for (int i = 0; i < baises.length; i++) {
				for (int j = 0; j < baises[i].getColumnDimension(); j++) {

					save.println(baises[i].getEntry(0, j));

				}
			}
			save.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
