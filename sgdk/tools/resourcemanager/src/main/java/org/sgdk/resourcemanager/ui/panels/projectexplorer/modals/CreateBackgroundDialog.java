package org.sgdk.resourcemanager.ui.panels.projectexplorer.modals;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.lang3.StringUtils;
import org.sgdk.resourcemanager.entities.SGDKBackground;
import org.sgdk.resourcemanager.entities.SGDKElement;
import org.sgdk.resourcemanager.entities.SGDKFolder;
import org.sgdk.resourcemanager.entities.factory.SGDKEntityFactory;
import org.sgdk.resourcemanager.ui.ResourceManagerFrame;

public class CreateBackgroundDialog extends JDialog{

	/**
	 * 
	 */
	
	private static final int minimizeWidth = 340;
	private static final int minimizeHeight = 220;
	
	private JTextField backgroundPathText = new JTextField();
	private JFileChooser backgroundPath = new JFileChooser(System.getProperty("user.home"));
	private JButton acceptButon = new JButton("Ok");
	private File[] selectedFiles = null;
	private static final long serialVersionUID = 1L;	
	
	public CreateBackgroundDialog(ResourceManagerFrame parent, SGDKElement parentNode) {
		super(parent, "New Background");
		parent.setEnabled(false);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = new Long(Math.round(screenSize.getWidth())).intValue();
		int screenHeight = new Long(Math.round(screenSize.getHeight())).intValue();
		
		setBounds(new Rectangle(screenWidth/2 - minimizeWidth/2, screenHeight/2 - minimizeHeight/2, minimizeWidth, minimizeHeight));
		setResizable(false);
//		setAlwaysOnTop(true);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				clean();
				parent.setEnabled(true);
			}
		});
		
		backgroundPath.addChoosableFileFilter(new FileFilter() {			
			@Override
			public String getDescription() {
				return StringUtils.join(SGDKBackground.ValidFormat.values(), ", ");
			}
			
			@Override
			public boolean accept(File f) {
				return SGDKBackground.isValidFormat(f.getAbsolutePath()) || f.isDirectory();
			}
		});
		backgroundPath.setAcceptAllFileFilterUsed(false);
		backgroundPath.setMultiSelectionEnabled(true);
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
			
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 1d/6d;
		c.weighty = 1d/2d;		
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		add(new JLabel("Background Path: "), c);
		
		c.fill = GridBagConstraints.HORIZONTAL;	
		c.anchor = GridBagConstraints.LINE_START;
		c.weightx = 5d/6d;
		c.weighty = 1d/2d;		
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 5;
		c.gridheight = 1;
		add(backgroundPathText, c);		
		
		backgroundPathText.addMouseListener(new MouseAdapter() {
			@Override
            public void mouseClicked(MouseEvent e){				
				int returnVal = backgroundPath.showDialog(parent, "New Background");
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					selectedFiles = backgroundPath.getSelectedFiles();
					String[] values = new String[selectedFiles.length];
					int i = 0;
					for(File f : selectedFiles) {
						values[i] = f.getAbsolutePath();
						i++;
					}
					backgroundPathText.setText(StringUtils.join(values, ","));
		        }
            }
		});
		
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.LINE_END;
		c.weightx = 1d/6d;
		c.weighty = 1d/2d;		
		c.gridx = 5;
		c.gridy = 2;
		c.gridwidth = 1;
		c.gridheight = 1;
		add(acceptButon, c);
		
		acceptButon.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean validForm = true;
				
				if(validForm && (backgroundPathText.getText() == null || backgroundPathText.getText().isEmpty())) {
					validForm = false;
					JPanel panel = new JPanel();
					JOptionPane.showMessageDialog(panel,
							 "Invalid Background File",
							 "Error",
					        JOptionPane.ERROR_MESSAGE);
				}
				
				if (validForm) {
					for(File f : selectedFiles) {
						SGDKBackground background = SGDKEntityFactory.createSGDKBackground(f.getAbsolutePath(), (SGDKFolder)parentNode);
						if(background != null) {						
							parent.getProjectExplorer().getProjectExplorerTree().addElement(background, parentNode);
						}
					}
					
					clean();
					parent.setEnabled(true);
					setVisible(false);					
				}
			}
		});
		
		setVisible(true);
	}

	protected void clean() {
		backgroundPathText.setText("");
	}

}
