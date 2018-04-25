package AI_Engine;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.apache.commons.math3.linear.RealMatrix;

public class DataManager {

	public void saveAI(int AI) {
		try {
			PrintWriter save = new PrintWriter(
					AI + ". AI (" + AIEngine.Inputs + ", " + AIEngine.Outputs + ", " + AIEngine.hidden + ").txt");
			save.println(AIEngine.Inputs);
			save.println(AIEngine.Outputs);
			save.println(AIEngine.hidden.length);
			for (int i = 0; i < AIEngine.hidden.length; i++) {
				save.println(AIEngine.hidden[i]);
			}
			RealMatrix[] weights = AIEngine.AIs[AI].getWeights();
			for (int i = 0; i < weights.length; i++) {
				for (int j = 0; j < weights[i].getColumnDimension(); j++) {
					for (int k = 0; k < weights[i].getRowDimension(); k++) {
						save.println(weights[i].getEntry(k, j));
					}
				}
			}
			RealMatrix[] baises = AIEngine.AIs[AI].getBaises();
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
