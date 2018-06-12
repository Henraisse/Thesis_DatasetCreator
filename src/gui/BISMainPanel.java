package gui;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import bis.BISBase;
import bis.BISHeader;

public class BISMainPanel extends JPanel{

	private BISBase bisbase;
	
	ArrayList<BISFieldPanel> subpanels = new ArrayList<BISFieldPanel>();
	public MESPanel mespanel;
	
	public BISMainPanel() {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBackground(Color.orange);
	}
	
	
	
	public void setBisBase(BISBase bisbase, String inputfolder) {
		this.bisbase = bisbase;		
		add(mespanel);
		setSubPanels(inputfolder);
	}

	
	
	public void setSubPanels(String inputfolder) {	
		for(BISHeader bs : bisbase.headers) {
			BISFieldPanel bp = new BISFieldPanel(bs, bisbase, inputfolder);
			subpanels.add(bp);
			add(bp);
		}		
	}



	public void prepareTask(BISBase bisbase, String inputfolder) {
		mespanel = new MESPanel();
		setBisBase(bisbase, inputfolder);
		mespanel.setInputFolder(inputfolder);	
	}
	
	
	
	
	
	
	
	
	
	
}
