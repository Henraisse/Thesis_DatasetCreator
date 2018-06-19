package gui;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import bis.BISBase;
import bis.BISHeader;

/**
 * Container panel that holds all parameter selection subpanels.
 * 
 * @author Henrik Ronnholm
 *
 */
public class BISMainPanel extends JPanel{
	private static final long serialVersionUID = 1L;

	private BISBase bisbase;
	
	ArrayList<BISFieldPanel> subpanels = new ArrayList<BISFieldPanel>();
	public MESPanel mespanel;
	
	/**
	 * Constructor.
	 */
	public BISMainPanel() {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBackground(Color.orange);
	}


	
	/**
	 * Setups the parameter subpanels.
	 * @param inputfolder
	 */
	public void setSubPanels(String inputfolder) {	
		for(BISHeader bs : bisbase.headers) {
			BISFieldPanel bp = new BISFieldPanel(bs, bisbase, inputfolder);
			subpanels.add(bp);
			add(bp);
		}		
	}


	
	/**
	 * Setups the measurement parameter panel.
	 * @param bisbase
	 * @param inputfolder
	 */
	public void prepareTask(BISBase bisbase, String inputfolder) {
		mespanel = new MESPanel();
		this.bisbase = bisbase;		
		add(mespanel);
		setSubPanels(inputfolder);
		mespanel.setInputFolder(inputfolder);	
	}
	
}
