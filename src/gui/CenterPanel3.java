package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.UIManager;

import bis.DataProcess;

public class CenterPanel3 extends JPanel{
	
	DataProcess process;
	
	JProgressBar progressBar = new JProgressBar(0, 100);
    public JLabel elapsedtime = new JLabel("Elapsed time: -");
    public JLabel expectedtime = new JLabel("Estimated remaining time: -");
	
	public CenterPanel3(DataProcess process) {		
		this.process = process;
		setLayout(new BorderLayout());
		
		
	    progressBar.setValue(0);
	    progressBar.setStringPainted(true);	    	    
	    progressBar.setBounds(50, 50, 500, 25);	    
	    progressBar.setForeground(Color.BLACK);
	    progressBar.setBackground(Color.white);
	    progressBar.setStringPainted(true);
		    
	    JPanel progresspanel = new JPanel();	
		progresspanel.setLayout(new BoxLayout(progresspanel, BoxLayout.PAGE_AXIS));	
		progresspanel.add(progressBar);
		progresspanel.add(Box.createRigidArea(new Dimension(10, 10)), BorderLayout.NORTH);
		//progresspanel.add(writtenmb);
		progresspanel.add(elapsedtime);
		progresspanel.add(expectedtime);
		
		JPanel boxpanel = new JPanel(new BorderLayout());
		boxpanel.setVisible(true);
		boxpanel.setPreferredSize(new Dimension(250, 150));
		boxpanel.add(Box.createRigidArea(new Dimension(10, 10)), BorderLayout.NORTH);
		boxpanel.add(Box.createRigidArea(new Dimension(10, 10)), BorderLayout.LINE_START);
		boxpanel.add(progresspanel, BorderLayout.CENTER);	
		boxpanel.add(Box.createRigidArea(new Dimension(10, 10)), BorderLayout.LINE_END);
		boxpanel.add(Box.createRigidArea(new Dimension(10, 10)), BorderLayout.SOUTH);
		
		add(boxpanel, BorderLayout.NORTH);
		
		
		
				
		
		


	    
	    
		//setBackground(Color.blue);
	    
	    

		

	}
	
	
	
	
	
	/**
	 * Run when panel is made visible. Performs all the prework only possible right after the previous panel is finished, and this
	 * is scheduled to be made visible.
	 */
	public void initiateAction() {
		// TODO Auto-generated method stub	
	}
    
	/**
	 * Run when this panel is done with its work, and the next is scheduled to be made visible.
	 * @return
	 */
	public boolean advanceAction(ParamFrame frame) {
				
		process.compileDataset(progressBar, frame);		
		return true;
	}





//	public void setwrittenMBr(int totalmbwritten, int c, int n, double currtime, int totalmb, int readmb) {		
//	    writtenmb.setText("Written: " + (totalmbwritten) + " MB");    
//	    
//	    double pct_left = (double)readmb/(double)totalmb;
//	    double tot_writ = totalmbwritten/pct_left;
//	    double tot_time = (currtime/pct_left) - currtime;
//
//	    	    
//	    elapsed.setText("Elapsed time: " + (int)tot_writ + " sek");
//	    expectedtime.setText("Estimated remaining time: " + (int)tot_time + " sek");
//		
//	}
	
	
	
	
	
}
