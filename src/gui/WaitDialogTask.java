package gui;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;

class WaitDialogTask extends SwingWorker<Void, Void> {
 	
	JDialog pleasewait;
	
	@Override
	protected Void doInBackground() throws Exception {
		openPleaseWaitDialog();
		return null;
	}
			
	@Override
	protected void done() {
		task();		
		pleasewait.dispose();
	}
					
	protected void task() {
		
	}
	
	private void openPleaseWaitDialog() {
		pleasewait = new JDialog(); // modal
		pleasewait.setPreferredSize(new Dimension(200, 100));
		pleasewait.setResizable(false);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int w = pleasewait.getSize().width;
		int h = pleasewait.getSize().height;
		int x = (dim.width - w) / 2;
		int y = (dim.height - h) / 2;

		pleasewait.setLocation(x, y);
		
		JLabel label = new JLabel("Please Wait...");
		label.setVisible(true);
		label.setHorizontalAlignment(JLabel.CENTER);
		pleasewait.add(label);
		pleasewait.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		pleasewait.pack();
		pleasewait.setModal(false);
		pleasewait.setVisible(true);			
	}		
}