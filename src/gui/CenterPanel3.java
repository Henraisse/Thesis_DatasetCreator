package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import bis.DataProcess;

public class CenterPanel3 extends JPanel{
	private static final long serialVersionUID = 1L;

	DataProcess process;
	
	JProgressBar progressBar = new JProgressBar(0, 100);
    public JLabel elapsedtime = new JLabel("Elapsed time: -");
    public JLabel expectedtime = new JLabel("Estimated remaining time: -");
	
    
    /**
     * Constructor.
     * @param process
     */
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

	
}
