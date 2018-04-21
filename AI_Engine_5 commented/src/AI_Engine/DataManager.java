package AI_Engine;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.apache.commons.math3.linear.RealMatrix;

public class DataManager {

	public void saveAI(int AI) {
		//saves AI of your choice
		try {
			PrintWriter save = new PrintWriter(
					AI + ". AI (" + AIEngine.Inputs + ", " + AIEngine.Outputs + ", " + AIEngine.hidden.length + ").txt"); //starts printstream to savefile
			save.println(AIEngine.Inputs);//prints Inputcount
			save.println(AIEngine.Outputs);//prints Outputcount
			save.println(AIEngine.hidden.length);//prints hiddenlayer count
			//prints neuroncount of every hiddenlayer
			for (int i = 0; i < AIEngine.hidden.length; i++) {
				save.println(AIEngine.hidden[i]);
			}
			RealMatrix[] weights = AIEngine.AIs[AI].getWeights();//gets weights from AI
			//saves every weight by running through every layer then every column and at last every row
			//this is important when translating, so other people with other languages can use your result
			for (int i = 0; i < weights.length; i++) {
				for (int j = 0; j < weights[i].getColumnDimension(); j++) {
					for (int k = 0; k < weights[i].getRowDimension(); k++) {
						save.println(weights[i].getEntry(k, j));
					}
				}
			}
			RealMatrix[] baises = AIEngine.AIs[AI].getBaises();//gets baises of AI
			//saves AI by going through each layer then each column
			for (int i = 0; i < baises.length; i++) {
				for (int j = 0; j < baises[i].getColumnDimension(); j++) {

					save.println(baises[i].getEntry(0, j));

				}
			}
			save.close();//closes printstream (which also saves the file)
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
