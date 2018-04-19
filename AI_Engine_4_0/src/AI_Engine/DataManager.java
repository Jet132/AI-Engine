package AI_Engine;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class DataManager {

	public void saveAI(int AI) {
		try {
			PrintWriter save = new PrintWriter(AI + ". AI (" + AIEngine.Inputs + ", " + AIEngine.Outputs + ", "
					+ AIEngine.Hidden + ", " + AIEngine.Layers + ").txt");
			for (int i = 0; i < AIEngine.CON_NR; i++) {
				save.println(String.valueOf(AIEngine.AIs[AI].getCON(i)));
			}
			save.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void saveCurrentEngineState() {
		try {
			PrintWriter save = new PrintWriter("AIEngine (" + AIEngine.Inputs + ", " + AIEngine.Outputs + ", "
					+ AIEngine.Hidden + ", " + AIEngine.Layers + ").txt");
			save.println(AIEngine.Inputs);
			save.println(AIEngine.Outputs);
			save.println(AIEngine.Hidden);
			save.println(AIEngine.Layers);
			save.println(AIEngine.CON_NR);
			save.println(AIEngine.DNA_Per);
			save.println(AIEngine.GENSTEP_NR);
			save.println(AIEngine.AI_NR);
			save.println(AIEngine.generation);
			save.println(AIEngine.generationStep);

			for (int i = 0; i < AIEngine.AI_NR; i++) {
				for (int j = 0; j < AIEngine.CON_NR; j++) {
					save.println(String.valueOf(AIEngine.AIs[i].getCON(j)));
				}
			}
			save.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	String[] getEngineState(String filepath) {
		ArrayList<String> str = new ArrayList<String>();
		try {
			FileReader load = new FileReader(filepath);
			BufferedReader br = new BufferedReader(load);

			int i = 0;
			try {
				while (str.add(br.readLine())) {
					if (str.get(i) == null) {
						str.remove(i);
						break;
					} else {
						// System.out.println(str.get(i));
						i++;
					}
				}
				br.close();
				load.close();

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str.toArray(new String[0]);
	}

}
