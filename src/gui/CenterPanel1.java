package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import bis.DataProcess;
import util.Util;

/**
 * Makes up the first panel visible on the frame. Used to select some setup parameters 
 * and boundaries and directories and other stuff.
 * 
 * 
 * @author Henrik Ronnholm
 *
 */
public class CenterPanel1 extends JPanel{
	private static final long serialVersionUID = 1L;
	
	DataProcess process;
	ParamFrame parent;
	
	public JTextField inputfield = new JTextField("");
	public JTextField outputfield = new JTextField("");
	public JTextField namefield = new JTextField("");
	JButton chooseInput = new JButton("...");
	JButton chooseOutput = new JButton("...");
	
	String[] inspection_choices = { "B5","B4", "B3","B2","B1","All"};
	String[] speed_choices = { "H5","H4", "H3","H2","H1","H0"};
	
	JComboBox<String> besclass = new JComboBox<String>(inspection_choices);
	JComboBox<String> speedclass = new JComboBox<String>(speed_choices);
	
    public JTextField sizefield = new JTextField("");
    
    public JTextField inspectionDaysfield = new JTextField("");
    public JTextField inspectionOffsetfield = new JTextField("");
	
	public String inputfolder = "";
	public String outputfolder = "";
	
	
	
	/**
	 * Constructor. Sets up the panel and all its components.
	 * @param process
	 * @param parent
	 */
	public CenterPanel1(DataProcess process, ParamFrame parent) {
		this.parent = parent;
		this.process = process;

    	setLayout(null);
    	
    	JLabel inputlabel = new JLabel("Input directory");
    	inputlabel.setBounds(170, 30, 225, 25);
    	inputlabel.setForeground(Color.gray);
    	inputlabel.setFont(new Font("Calibri", Font.PLAIN, 12));
    	add(inputlabel);   	
    	chooseInput.setBounds(10, 50, 25, 25);    	
    	add(chooseInput);   	
    	inputfield.setBounds(40, 50, 400, 25);    	
    	add(inputfield);
    	   	   	
    	JLabel outputlabel = new JLabel("Output directory");
    	outputlabel.setBounds(170, 85, 225, 25);
    	outputlabel.setForeground(Color.gray);
    	outputlabel.setFont(new Font("Calibri", Font.PLAIN, 12));
    	add(outputlabel);   	
    	chooseOutput.setBounds(10, 105, 25, 25);
    	add(chooseOutput);   	
    	outputfield.setBounds(40, 105, 400, 25);
    	add(outputfield);
    	
    	JLabel bclabel = new JLabel("Inspection class:");
    	bclabel.setBounds(120, 170, 250, 25);
    	bclabel.setForeground(Color.gray);
    	bclabel.setFont(new Font("Calibri", Font.PLAIN, 12));
    	add(bclabel);
    	besclass.setBounds(230, 170, 50, 25);
    	besclass.setEnabled(false);
    	add(besclass);
    	
    	JLabel sclabel = new JLabel("Speed class:");
    	sclabel.setBounds(120, 140, 250, 25);
    	sclabel.setForeground(Color.gray);
    	sclabel.setFont(new Font("Calibri", Font.PLAIN, 12));
    	add(sclabel);
    	speedclass.setBounds(230, 140, 50, 25);
    	add(speedclass);
    	 	
    	JLabel sizelabel = new JLabel("Max file size (MB):");
    	sizelabel.setBounds(110, 200, 350, 25);
    	sizelabel.setFont(new Font("Calibri", Font.PLAIN, 12));
    	sizelabel.setForeground(Color.gray);
    	sizefield.setText("2000");
    	add(sizelabel);   	   	
    	sizefield.setBounds(230, 200, 50, 25);
    	add(sizefield);
    	
    	JLabel dayslabel = new JLabel("Time between measurements(Days):");
    	dayslabel.setBounds(11, 230, 350, 25);
    	dayslabel.setFont(new Font("Calibri", Font.PLAIN, 12));
    	dayslabel.setForeground(Color.gray);
    	inspectionDaysfield.setText("60");
    	add(dayslabel);   	   	
    	inspectionDaysfield.setBounds(230, 230, 50, 25);
    	add(inspectionDaysfield);
    	
    	JLabel offsetlabel = new JLabel("Spread (0.3 = 30%):");
    	offsetlabel.setBounds(83, 260, 350, 25);
    	offsetlabel.setFont(new Font("Calibri", Font.PLAIN, 12));
    	offsetlabel.setForeground(Color.gray);
    	inspectionOffsetfield.setText("0.05");
    	add(offsetlabel);   	   	
    	inspectionOffsetfield.setBounds(230, 260, 50, 25);
    	add(inspectionOffsetfield);
    	   	
    	JLabel namelabel = new JLabel("Dataset name (Optional)");
    	namelabel.setBounds(120, 300, 350, 25);
    	namelabel.setFont(new Font("Calibri", Font.PLAIN, 12));
    	namelabel.setForeground(Color.gray);
    	namefield.setText("");
    	add(namelabel);   	   	
    	namefield.setBounds(30, 320, 350, 25);
    	add(namefield);

		chooseInput.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	//Handle open button action.
		    	JFileChooser fc = new JFileChooser();
		    	fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		    	
		    	int returnVal = fc.showOpenDialog(null);


		    	if (returnVal == JFileChooser.APPROVE_OPTION) {
		    		File file = fc.getSelectedFile();
		    		inputfield.setText(file.getAbsolutePath());
		    	}

		    }
		});
		
		chooseOutput.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	//Handle open button action.
		    	JFileChooser fc = new JFileChooser();
		    	fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		    	
		    	int returnVal = fc.showOpenDialog(null);


		    	if (returnVal == JFileChooser.APPROVE_OPTION) {
		    		File file = fc.getSelectedFile();
		    		outputfield.setText(file.getAbsolutePath());
		    	}

		    }
		});
    }
	
    
    
    /**
     * Checks that the selected input folder is valid.
     * @return
     */
    public boolean isValidInputFolder() {
    	File input = new File(inputfield.getText());
    	boolean inputFolderExists = input.exists();
    	return inputFolderExists;
    }
    
    
    /**
     * Is meant to be a check of the input content folder, but I guess I'll do it later. heh.
     * @return
     */
	private boolean isValidInputContentFolder() {
		return true;
	}
	
	
	
	/**
	 * Create an output folder if it doesn't exist yet.
	 * @return
	 */
    public boolean createOutputFolder() {
        File file = new File(outputfield.getText());
        if (!file.exists()) {
        	boolean mkdr = file.mkdir();
            if (mkdr) {
            	outputfolder = file.getAbsolutePath();
            	return true;
            } else {
                return false;
            }
        }
        outputfolder = file.getAbsolutePath();
        return true;
    }



	/**
	 * Run when panel is made visible. Performs all the prework only possible right after the previous panel is finished, and this
	 * is scheduled to be made visible.
	 * -----------------------------------
	 * 
	 */
	public void initiateAction() {
		//Read a log-file from the last run, and pre-fill all data fields	
    	inputfield.setText("C:\\Users\\Henra\\Desktop\\DATA COLLECTION\\input_folder");
    	outputfield.setText("C:\\Users\\Henra\\Desktop\\DATA COLLECTION\\dataset_output");
	}
    
	/**
	 * Run when this panel is done with its work, and the next is scheduled to be made visible.
	 * @return
	 */
	public boolean advanceAction() {
		//Check that all fields have valid values, and send them to the DataProcess for further processing.
		boolean in = isValidInputFolder();
		boolean incontent = isValidInputContentFolder();
		boolean out = createOutputFolder();
		boolean mb = Util.isInteger(sizefield.getText());
		

		if(in && out && mb) {
			inputfolder = inputfield.getText();
			process.preProcessStep(inputfolder, outputfolder, (String)besclass.getSelectedItem(), besclass.getSelectedIndex(), sizefield.getText(), namefield.getText(), inspectionDaysfield.getText(), inspectionOffsetfield.getText(), parent, (String)speedclass.getSelectedItem(), speedclass.getSelectedIndex());

			return true;
		}
		
		
		else if(!in){
			JOptionPane.showMessageDialog(this, "Not a valid input folder! Select another!", "Error", JOptionPane.ERROR_MESSAGE);
		}else if(!out){
			JOptionPane.showMessageDialog(this, "Failed to create output folder!", "Error", JOptionPane.ERROR_MESSAGE);
		}else if(!mb){
			JOptionPane.showMessageDialog(this, "Max output file size is not an integer!", "Error", JOptionPane.ERROR_MESSAGE);
		}else if(!incontent){
			JOptionPane.showMessageDialog(this, "Input folder is empty!", "Error", JOptionPane.ERROR_MESSAGE);
		}
		return false;
	}





	
}
