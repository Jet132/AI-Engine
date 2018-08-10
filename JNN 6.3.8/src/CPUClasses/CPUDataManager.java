package CPUClasses;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.math3.linear.RealMatrix;

import JNN.JNN;

public class CPUDataManager {

	public void saveAI(int AI) {
		try {
			PrintWriter save = new PrintWriter(
					AI + ". AI ("+new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())+").txt");
			save.println(JNN.layout.length);
			for (int i = 0; i < JNN.layout.length; i++) {
				save.println(JNN.layout[i]);
			}
			RealMatrix[] weights = ((CPUAgent) JNN.Agents[AI]).getWeights();
			for (int i = 0; i < weights.length; i++) {
				for (int j = 0; j < weights[i].getColumnDimension(); j++) {
					for (int k = 0; k < weights[i].getRowDimension(); k++) {
						save.println(weights[i].getEntry(k, j));
					}
				}
			}
			RealMatrix[] baises = ((CPUAgent) JNN.Agents[AI]).getBaises();
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
