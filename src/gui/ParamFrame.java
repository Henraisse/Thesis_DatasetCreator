package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import bis.BISBase;
import bis.DataProcess;
import structs.Dataset;
import util.Util;

public class ParamFrame extends JFrame{
	
	public static final String EXIT_BUTTON_PATH = "src/resources/exit.png";
		
	public DataProcess process;
		
	public JPanel actionPanel = new JPanel();
	public JPanel buttonPanel = new JPanel();
	public JPanel scrollPanel = new JPanel(new CardLayout());	
	public CenterPanel1 centerPanel1;
	public CenterPanel2 centerPanel2;
	public CenterPanel3 centerPanel3;
	
	
	JLabel centerlabel = new JLabel("Welcome! Please select parameters...");	
	JButton prevButton = new JButton("Previous");
	JButton nextButton = new JButton("Next");
	int panelcounter = 0;	
	JScrollPane scroll = new JScrollPane(scrollPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	
	
	
	public ParamFrame(DataProcess process2) {
		process = process2;
		
		centerPanel1 = new CenterPanel1(process, this);
		centerPanel2 = new CenterPanel2(process);
		centerPanel3 = new CenterPanel3(process);
		
		Util.setLookAndFeel(this);
		setLayout(new BorderLayout());
		setupPanels();
		createMenuBar();

		
		setTitle("Dataset extractor");
		setSize(800, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);		
		setVisible(true);		
		
			

	}
	
	
	
	
	
	private void setupPanels() {	
		actionPanel.setVisible(true);
		actionPanel.setBackground(Color.red);		
		actionPanel.setPreferredSize(new Dimension(80, 150));	
		actionPanel.setLayout(new BorderLayout());
		this.getContentPane().add(actionPanel, BorderLayout.CENTER);
		//scrollPanel.setPreferredSize(new Dimension(500, 500));
		actionPanel.add(scroll);
	
		buttonPanel.setVisible(true);
		buttonPanel.setBackground(Color.yellow);	
		buttonPanel.setPreferredSize(new Dimension(100, 50));
		buttonPanel.setLayout(new BorderLayout());
		this.getContentPane().add(buttonPanel, BorderLayout.PAGE_END);
		
		JPanel leftbuttonPanel = new JPanel();
		leftbuttonPanel.setPreferredSize(new Dimension(200, 200));
		leftbuttonPanel.setLayout(null);
		
		prevButton.setBounds(10, 10, 180, 30);
		leftbuttonPanel.add(prevButton);
		buttonPanel.add(leftbuttonPanel, BorderLayout.LINE_START);	
		
		JPanel rightbuttonPanel = new JPanel();
		rightbuttonPanel.setPreferredSize(new Dimension(200, 200));
		rightbuttonPanel.setLayout(null);
		
		nextButton.setBounds(10, 10, 180, 30);
		rightbuttonPanel.add(nextButton);
		buttonPanel.add(rightbuttonPanel, BorderLayout.LINE_END);	
		
		JPanel centerbuttonPanel = new JPanel();	
		buttonPanel.add(centerbuttonPanel, BorderLayout.CENTER);	
		centerbuttonPanel.setLayout(new BoxLayout(centerbuttonPanel, BoxLayout.PAGE_AXIS));	
		centerbuttonPanel.add(Box.createRigidArea(new Dimension(10, 13)));
		centerbuttonPanel.add(centerlabel);
		
		centerlabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		

		
		scrollPanel.add(centerPanel1, "p1");
		scrollPanel.add(centerPanel2, "p2");
		scrollPanel.add(centerPanel3, "p3");
		centerPanel1.initiateAction();
		
		ParamFrame dis = this;
		nextButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	
		    	if(panelcounter == 0) {		   
		    		
		    		WaitDialogTask sw = new WaitDialogTask() {
		    			@Override
		    			protected void task() {
				    		boolean success = centerPanel1.advanceAction();
				    		if(success) {
				    			switchPanel(1);
				    			centerPanel2.initiateAction();
				    			centerlabel.setText("- Select parameters to be included in the dataset -");
				    		}
		    			}
		    		};
		    		sw.execute();
		    		
		    	}
		    	
		    	
		    	else if(panelcounter == 1) {
		    		
		    		WaitDialogTask sw = new WaitDialogTask() {
		    			@Override
		    			protected void task() {
				    		boolean success = centerPanel2.advanceAction();
				    		if(success) {
				    			
		    			    	switchPanel(1);
		    			    	nextButton.setText("Finish");
		    			    	nextButton.setEnabled(false);
		    			    	prevButton.setEnabled(false);
				    			centerlabel.setText("Building dataset...");
				    			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));		    				    			
		    			    	centerPanel3.advanceAction(dis);		
				    		}
		    			}
		    		};
		    		sw.execute();		    		
		    	}
		    	
		    	
		    	
		    	else if(panelcounter == 2) {	
		    		System.exit(0);
		    	}
		    	
		    }


		});
		
		prevButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	switchPanel(-1);
		    }
		});
		
	}
	
	

	
	

	
	
	
	public void finished() {
    	nextButton.setEnabled(true);
    	centerlabel.setText("Dataset complete.");
    	//custom title, no icon
    	JOptionPane.showMessageDialog(this, "Dataset complete!", "Task", JOptionPane.PLAIN_MESSAGE);    	            	
    	setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}


	/*
	 * Setup the menu bar with an exit button and File and so on.
	 */
    private void createMenuBar() {

        JMenuBar menubar = new JMenuBar();
        ImageIcon exitIcon = new ImageIcon(EXIT_BUTTON_PATH);

        JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);
        
        JMenu help = new JMenu("Help");
        help.setMnemonic(KeyEvent.VK_H);
        
        //---------------------------------------------------------
        
        JMenuItem eMenuItem = new JMenuItem("Exit", exitIcon);
        eMenuItem.setMnemonic(KeyEvent.VK_E);
        eMenuItem.setToolTipText("Exit application");       
        eMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
            	System.exit(0);
            }
        });       
        file.add(eMenuItem);
        menubar.add(file);
        setJMenuBar(menubar);
        
        //---------------------------------------------------------
        
        
        JMenuItem about = new JMenuItem("About", exitIcon);
        about.setMnemonic(KeyEvent.VK_E);
        about.setToolTipText("Read About-text");      
        ParamFrame dis = this;
        about.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {

            	//custom title, no icon
            	JOptionPane.showMessageDialog(dis, "This program was made as a tool for my thesis in cooperation with Infranord, \nspring 2018. For more information, contact me at henron@kth.se!",
            	    "About",
            	    JOptionPane.PLAIN_MESSAGE);
            	
            }
        });       
        help.add(about);
        
        //---------------------------------------------------------
        menubar.add(file);
        menubar.add(help);
        setJMenuBar(menubar);
        
        

    }
	
	
    
  
    
    /*
     * Advance to the next panel or the previous panel, depending on the input integer.
     * @param i
     */
    private void switchPanel(int i) {
    	panelcounter += i;
    	if(panelcounter < 0) {panelcounter = 0;}
    	if(panelcounter > 2) {panelcounter = 2;}
    	
    	centerPanel1.setVisible(false);
    	centerPanel2.setVisible(false);
    	centerPanel3.setVisible(false);
    	
    	CardLayout cardLayout = (CardLayout) scrollPanel.getLayout();
    	if(panelcounter == 0) {
    		cardLayout.show(scrollPanel, "p1");

    	}else if(panelcounter == 1) {
    		cardLayout.show(scrollPanel, "p2");

    	}else if(panelcounter == 2) {
    		cardLayout.show(scrollPanel, "p3");
 
    	}	
    	repaint();    	
    }
    

    
    
    



    
    

    
    
    

    
    
    
    
}
