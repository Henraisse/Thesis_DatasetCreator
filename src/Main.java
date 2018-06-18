import bis.DataProcess;
import gui.ParamFrame;

/**
 * Main class. contains main method. Creates the computation module (DataProcess) and the GUI module (ParamFrame).
 * @author Henrik Rönnholm
 *
 */
public class Main {

	public static void main(String[] args) {
		
		DataProcess process = new DataProcess();		
		@SuppressWarnings("unused")
		ParamFrame frame = new ParamFrame(process);
	}
	
}
