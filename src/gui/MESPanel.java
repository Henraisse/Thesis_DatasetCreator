package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import structs.SelectionSetting;
import util.Util;


/**
 * The panel that displays the selection of measurement parameters.
 * 
 * @author Henrik Ronnholm
 *
 */
public class MESPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	String inputfolder;
	String[] headers = new String[1];
	JLabel label = new JLabel();		
	int[] headerSelection = new int[1];
	
	ArrayList<CheckBoxKit> fields = new ArrayList<CheckBoxKit>();



	/**
	 * Constructor.
	 */
	public MESPanel() {
		this.setPreferredSize(new Dimension(200, 350));
		setLayout(new FlowLayout(FlowLayout.LEFT));

		label.setText("Measurement Data");

		setBackground(Color.lightGray);
		
		Font f1 = new Font("Calibri", Font.BOLD, 14);
		label.setFont(f1);


		label.setBounds(5, 5, 150, 25);
		add(label);
		setVisible(true);

		Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		setBorder(loweredetched);
	}



	/**
	 * Save the settings and store them for later use.
	 */
	public void storeSettings() {
		SelectionSetting settings = new SelectionSetting(headerSelection);		
	      try {
	          FileOutputStream fileOut =  new FileOutputStream(inputfolder + "\\settings\\measurement_main");
	          ObjectOutputStream out = new ObjectOutputStream(fileOut);
	          out.writeObject(settings);
	          out.close();
	          fileOut.close();

	       } catch (IOException i) {
	          i.printStackTrace();
	       }
	}
	
	
	
	/**
	 * Attempt to read the saved settings.
	 */
	public void attemptReadSettings() {
		try {
	         FileInputStream fileIn = new FileInputStream(inputfolder + "\\settings\\measurement_main");
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
		} catch (IOException i) {
			//i.printStackTrace();
			return;
		} catch (ClassNotFoundException c) {
			c.printStackTrace();
			return;
		}

	}


	
	/**
	 * Run every time parameter selection is changed. Changes the selection and updates the stored settings.
	 */
	public void updateHeaderSelection() {
		headerSelection = new int[fields.size()];
		for(int i = 0; i < fields.size(); i++) {
			headerSelection[i] = fields.get(i).getBit();
		}
		storeSettings();
	}


	
	
	
	/**
	 * Returns the measurement parameter string array.
	 * @return
	 */
	public String[] getMESHeaderStrings() {
		return headers;
	}
	
	
	
	/**
	 * Returns the measurement parameter string.
	 * @return
	 */
	public String getMESHeaderString() {		
		StringBuilder sb = new StringBuilder();		
		updateHeaderSelection();
		
		for(int i = 0; i < headers.length; i++) {
			if (headerSelection[i] == 1) {
				sb.append(headers[i] + ";");
			}
		}
		return sb.toString();
	}
	
	
	
	/**
	 * Returns the measurement parameter bit array (int array).
	 * @return
	 */
	public int[] getMESHeaderBits() {		
		return headerSelection;
	}

	
	
	/**
	 * Sets the input folder.
	 * @param inputfolder
	 */
	public void setInputFolder(String inputfolder) {
		this.inputfolder = inputfolder;
			
		File folder = new File(inputfolder + "\\measurements");
		for(File file : folder.listFiles()) {	
			headers = Util.get_headers(file);
			if(headers.length == 48) {
				break;
			}
		}		
		setupHeaderSelection(inputfolder);		
		attemptReadSettings();
	}



	/**
	 * Setup the parameter selection boxes.
	 * @param inputfolder
	 */
	public void setupHeaderSelection(String inputfolder) {
		for(int i = 0; i < headers.length; i++) {			
			CheckBoxKit cbk = new CheckBoxKit(headers[i]);
			
			cbk.setFont(new Font("Calibri", Font.BOLD, 16));
			add(cbk);
			fields.add(cbk);
			cbk.setVisible(true);					
		}		
	}

	
	
	
	/**
	 * Represents a complete parameter box in the parameter selection list.
	 * 
	 * @author Henrik Ronnholm
	 *
	 */
	public class CheckBoxKit extends JPanel{
		private static final long serialVersionUID = 1L;
		
		BISFieldPanel parent;
		JCheckBox cb = new JCheckBox();

		/**
		 * Constructor.
		 * @param field
		 */
		public CheckBoxKit(String field) {		
			setOpaque(false);
			cb.setText(field);			
			cb.setOpaque(false);
			add(cb);
			cb.setVisible(true);
		}

		
		
		/**
		 * Returns the bit for if the checkbox is selected or not.
		 * @return
		 */
		public int getBit() {
			if(cb.isSelected()) {
				return 1;
			}
			return 0;
		}

	}
}
