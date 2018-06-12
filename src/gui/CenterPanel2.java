package gui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;

import bis.DataProcess;

public class CenterPanel2 extends JPanel{
	
	DataProcess process;
	
	
	public BISMainPanel bispanel = new BISMainPanel();
	
	public CenterPanel2(DataProcess process) {
		this.process = process;
		setBackground(Color.green);
    	setLayout(new BorderLayout());
    	add(bispanel);
    	bispanel.setVisible(true);
	}
	
	

	
    


	
	

	/**
	 * Run when panel is made visible. Performs all the prework only possible right after the previous panel is finished, and this
	 * is scheduled to be made visible.
	 */
	public void initiateAction() {
		process.processStep1(bispanel);
	}
    
	/**
	 * Run when this panel is done with its work, and the next is scheduled to be made visible.
	 * @return
	 */
	public boolean advanceAction() {
		bispanel.mespanel.updateHeaderSelection();
		for(BISFieldPanel bfp : bispanel.subpanels) {
			bfp.updateHeaderSignatures();
		}
		
		process.processStep3(bispanel.mespanel);
		return true;
	}
	
	
	
	
}