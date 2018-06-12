package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import bis.BISBase;
import bis.BISHeader;
import bis.BIS_Type;
import structs.SelectionSetting;

public class BISFieldPanel extends JPanel{

	BISHeader header;
	BISBase bisbase;	
	JLabel label = new JLabel();	
	ArrayList<CheckBoxKit> fields = new ArrayList<CheckBoxKit>();
	
	String inputpath;
	
	int[] settingbits;
	
	
	
	public BISFieldPanel(BISHeader header, BISBase bisbase, String input_path) {
		inputpath = input_path;
		this.bisbase = bisbase;
		this.setPreferredSize(new Dimension(200, 120));
		
		setLayout(new FlowLayout(FlowLayout.LEFT));
		this.header = header;		
		label.setText(header.name);
		
		Font f1 = new Font("Calibri", Font.BOLD, 14);
		label.setFont(f1);
		
		
		label.setBounds(5, 5, 150, 25);
		add(label);
		setVisible(true);
		
		Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		setBorder(loweredetched);
		setupHeaderSelection();
	}
		
	
	
	
	
	
	public void storeSettings() {
		SelectionSetting settings = new SelectionSetting(settingbits);		
	      try {
	          FileOutputStream fileOut =  new FileOutputStream(inputpath + "\\settings\\" + header.name);
	          ObjectOutputStream out = new ObjectOutputStream(fileOut);
	          out.writeObject(settings);
	          out.close();
	          fileOut.close();

	       } catch (IOException i) {
	          i.printStackTrace();
	       }
	}
	
	
	public void attemptReadSettings() {
		try {
	         FileInputStream fileIn = new FileInputStream(inputpath + "\\settings\\" + header.name);
	         ObjectInputStream in = new ObjectInputStream(fileIn);
	         SelectionSetting settings = (SelectionSetting) in.readObject();
	         if(settings != null && fields != null && settings.bits.length == fields.size()) {
	        	 for(int i = 0; i < fields.size(); i++) {
	        		 if(settings.bits[i] == 1) {
	        			 fields.get(i).cb.setSelected(true);
	        		 }	     
	        	 }
	         }        
	         in.close();
	         fileIn.close();
	      } catch (IOException i) {i.printStackTrace();return;} catch (ClassNotFoundException c) {c.printStackTrace();return;}
		
	}
	

	
	
	
	
	
	public void setupHeaderSelection() {
		
		for(int i = 0; i < header.headers.length; i++) {			
			CheckBoxKit cbk = new CheckBoxKit(header.headers[i]);
			
			cbk.setFont(new Font("Calibri", Font.BOLD, 16));
			add(cbk);
			fields.add(cbk);
			cbk.setVisible(true);					
		}
		attemptReadSettings();
	}
	
	
	
	
	
	
	public void updateHeaderSignatures() {
		int[] ret = new int[fields.size()];

		for(int i = 0; i < fields.size(); i++) {
			ret[i] = fields.get(i).getBit();
		}		
		bisbase.signatures.put(header.name, ret); 
		settingbits = ret;
		storeSettings();
	}
	
	
	
	

	public class CheckBoxKit extends JPanel{
		BISFieldPanel parent;
		JCheckBox cb = new JCheckBox();
		
		public CheckBoxKit(String field) {		
			
			setOpaque(false);
			cb.setText(field);			
			cb.setOpaque(false);
			add(cb);
			cb.setVisible(true);
			
		}
		
		
		public int getBit() {
			if(cb.isSelected()) {
				return 1;
			}
			return 0;
		}
		
		
	}
	
}
