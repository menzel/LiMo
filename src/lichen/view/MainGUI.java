package lichen.view;

import ij.ImagePlus;
import ij.gui.ImageCanvas;
import ij.gui.ProgressBar;
import ij.gui.Toolbar;
import ij.process.ColorProcessor;
import ij.process.ImageProcessor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints.Key;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;

import javax.naming.NameNotFoundException;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import lichen.controller.AutoAnalyzer;
import lichen.controller.ColorMatcher;
import lichen.controller.ColorStack;
import lichen.controller.DataExporter;
import lichen.controller.ManualAnalyzer;
import lichen.controller.Processor;
import lichen.controller.UndoStack;
import lichen.model.Genus;
import lichen.model.Measurement;
import lichen.model.MeasurementsFactory;
import lichen.model.Species;

@SuppressWarnings("serial")
public class MainGUI extends JFrame{

	private FileHandler fh;
	private AutoAnalyzer auto;
	private Toolbar t;
	private ImagePlus imp;
	private Processor myProcessor;
	private int blurValue = 1;
	private double pixelrate;
	private int linewidth = 3;
	private double borderWidth = 0.76;
	private double magnification = 1.0;
	private static final String version = "0.9.7.2";
	private static final String date = "6.11.2013";
	private static boolean styleModern = false; 
	private static MainGUI gui;
	private JPanel colorPanel;
	private JPanel image;
	private ImageCanvas ic;
	private ManualAnalyzer manualAnalyzer;
	private UndoStack undoStack;
	private final JTextArea text = new JTextArea();
	private Point mousePressed = null; 
	private ProgressBar bar; 
	public final JTable measurements = new JTable( new DefaultTableModel(new Object[] {"Id","Name", "Fläche", "Farbe"}, 10));
	private int fillColor;
	private boolean newColor = true; 
	private final MenuBar menuBar = new MenuBar();

	/**
	 * Constructor GUI
	 * @param t2 
	 * @param myProcessor2 
	 * @param auto2 
	 * @param fh2 
	 */
	public MainGUI( FileHandler fh, AutoAnalyzer auto, Processor myProcessor, Toolbar t) { 
		gui  = this; 
		t.setTool(7); 

		this.fh = fh;
		this.auto = auto;
		this.myProcessor = myProcessor;
		this.t = t; 

		initGUI();
	}

	/**
	 * Inits the GUI, sets up the view
	 */
	private void initGUI(){

		if(styleModern){
			UIManager.put("nimbusBase", Color.BLACK);
			UIManager.put("nimbusBlueGrey", Color.GRAY);
			//			UIManager.put("nimbusBlueGrey", new Color(12,130,198));
			UIManager.put("control", Color.DARK_GRAY);
			//		UIManager.put("text", Color.white);

			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					try {
						UIManager.setLookAndFeel(info.getClassName());
					} catch (ClassNotFoundException e1) {
						e1.printStackTrace();
					} catch (InstantiationException e1) {
						e1.printStackTrace();
					} catch (IllegalAccessException e1) {
						e1.printStackTrace();
					} catch (UnsupportedLookAndFeelException e1) {
						e1.printStackTrace();
					}
					break;
				}
			}
		} 

		setSize(850,700);
		setPreferredSize(new Dimension(850, 700));
		setLocationRelativeTo(null);
		setTitle("LiMo-Analyse");
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE); 
		getContentPane().setLayout(new BorderLayout());

		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				if(imp == null ){ 
					System.exit(0);
				}else{
					if(JOptionPane.showConfirmDialog(null, "Sie haben ein Bild geöffnet, wollen Sie das Programm wirklich schließen?") == JOptionPane.YES_OPTION) 
						System.exit(0); 

				} 
			}
		});

		Toolbar.setBrushSize(1);
		//---------------------------------------//
		// Menu Bar:
		//	menuBar.setPreferredSize(new Dimension(1, 30));


		Menu fileMenu = new Menu("Datei");
		Menu editMenu = new Menu("Bild");
		Menu dataMenu = new Menu("Daten");
		Menu autoMenu = new Menu("Auto Analyse");
		Menu settingsMenu = new Menu("Einstellungen");

		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(dataMenu);
		menuBar.add(autoMenu);
		menuBar.add(settingsMenu); 


		MenuItem Bnew  = new MenuItem("Neue Folie", new MenuShortcut(KeyEvent.VK_1, false)); 
		MenuItem Bsave = new MenuItem("Ergebnisse speichern");
		MenuItem Bclose = new MenuItem("Programm Schließen"); 

		final MenuItem Bdraw = new MenuItem("Stift"); 
		final MenuItem Bpicker = new MenuItem("Farbwähler");

		MenuItem BpictureSave = new MenuItem("Bild speichern");
		MenuItem Barea = new MenuItem("Messfläche wählen");
		MenuItem Bborder = new MenuItem("Randdicke wählen");
		MenuItem BsheetSize = new MenuItem("Foliengröße wählen");
		MenuItem Brotate = new MenuItem("Bild um 180° drehen"); 
		MenuItem BcolorList = new MenuItem("Füllfarben wählen"); 
		MenuItem Bauto = new MenuItem("Automatische Analyse");

		MenuItem Bchooser = new MenuItem("Farben zuweisen");
		MenuItem BshowResults = new MenuItem("Ergebnisse anzeigen", new MenuShortcut(KeyEvent.VK_3)); 
		MenuItem Bmanual = new MenuItem("manuelle Analyse", new MenuShortcut(KeyEvent.VK_2)); 

		final MenuItem Breset = new MenuItem("Daten zurücksetzen", new MenuShortcut(KeyEvent.VK_0));


		MenuItem Babout = new MenuItem("über");

		fileMenu.add(Bnew);
		fileMenu.add(Babout);

		editMenu.add(Bpicker);
		editMenu.add(Bdraw);
		editMenu.add(Barea);
		editMenu.add(BpictureSave);
		//editMenu.add(Bblur);
		editMenu.add(Brotate); 

		dataMenu.add(Bmanual);
		dataMenu.add(BshowResults);
		dataMenu.add(Bsave);
		dataMenu.add(Breset); 

		settingsMenu.add(BsheetSize);
		settingsMenu.add(Bborder);
		//settingsMenu.add(BcolorList); //TODO: reenable

		autoMenu.add(Bauto);
		autoMenu.add(Bchooser);

		//		setJMenuBar(menuBar);
		setMenuBar(menuBar);
		//---------------------------------------//

		// Lichen Panel : 
		JPanel lichenPanel = new JPanel(); 
		lichenPanel.setLayout(new BorderLayout()); 

		String columnNames[] = {"ID", "Name","Farbe"}; 
		final JTable table = new JTable(getTableData(),columnNames); 
		table.setShowGrid(false);
		table.setEnabled(false);
		table.setMinimumSize(new Dimension(3000, 5000));
		table.getColumnModel().getColumn(0).setPreferredWidth(15);
		table.getColumnModel().getColumn(1).setPreferredWidth(150); 
		table.getColumnModel().getColumn(2).setPreferredWidth(10); 

		table.getColumnModel().getColumn(2).setCellRenderer(new ColorCellRenderer());

		JTableHeader header = table.getTableHeader(); 
		header.setReorderingAllowed(false);

		JScrollPane scrollPane = new JScrollPane(table);
		//		scrollPane.add(lichenPanel);
		scrollPane.setPreferredSize(new Dimension(280, 1));
		lichenPanel.setPreferredSize(new Dimension(260, 1));

		//search panel + table headings
		JPanel search = new JPanel();
		search.setLayout(new BorderLayout());
		final JTextField searchField = new JTextField("", 18);
		final JButton searchButton = createButton("Suche");
		search.add(searchField, BorderLayout.CENTER);
		search.add(searchButton, BorderLayout.EAST);
		search.add(header, BorderLayout.SOUTH);

		lichenPanel.add(search, BorderLayout.NORTH); 
		lichenPanel.add(scrollPane, BorderLayout.CENTER);
		//lichenPanel.setEnabled(true);
		//	lichenPanel.setVisible(false);
		getContentPane().add(lichenPanel, BorderLayout.WEST); 


		//---------------------------------------//

		final JPanel imageFrame = new JPanel(new BorderLayout());
		imageFrame.setBorder(BorderFactory.createLineBorder(new Color(0, 34, 102), 1));

		imageFrame.setSize(200, 200);

		image = new JPanel(new GridLayout());
		image.setSize(200, 200); 
		image.setBackground(Color.GRAY);


		final JScrollBar imageHorizontal  = new JScrollBar(0); 
		imageHorizontal.setUnitIncrement(1000);

		imageHorizontal.setPreferredSize(new Dimension(1,16)); 
		imageHorizontal.addAdjustmentListener(new AdjustmentListener() {


			@Override
			public void adjustmentValueChanged(AdjustmentEvent ae) {
				if(ic != null){ 

					Rectangle srcRect = ic.getSrcRect(); 

					srcRect.x = (int)((ae.getValue()*(imp.getWidth()-ic.getSize().width)/90)*magnification); 

					ic.repaint();
					imp.updateAndDraw(); 

				}else{
					imageHorizontal.setValue(0);
				} 
			}
		});

		final JScrollBar imageVertical = new JScrollBar(1); 
		imageVertical.setPreferredSize(new Dimension(16,1)); 
		imageVertical.addAdjustmentListener(new AdjustmentListener() {

			@Override
			public void adjustmentValueChanged(AdjustmentEvent ae) {
				if(ic != null){ 

					Rectangle srcRect = ic.getSrcRect(); 
					srcRect.y = (int) ((ae.getValue()*(imp.getHeight()-ic.getSize().height)/90)*magnification);

					ic.repaint();
					imp.updateAndDraw();
				}else{
					imageVertical.setValue(0);
				}
			}
		});


		//path north here
		final JTextField pathField = new JTextField("");
		pathField.setEditable(false);

		imageFrame.add(pathField, BorderLayout.NORTH);

		imageFrame.add(image, BorderLayout.CENTER);
		imageFrame.add(imageHorizontal, BorderLayout.SOUTH);
		imageFrame.add(imageVertical, BorderLayout.EAST);


		//final JScrollPane ImageScrollPane = new JScrollPane(image);
		//	ImageScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		//	ImageScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS); 
		//	imageFrame.add(ImageScrollPane, BorderLayout.CENTER);

		getContentPane().add(imageFrame, BorderLayout.CENTER); 



		//---------------------------------------//
		// Result Table:

		JPanel resultPanel = new JPanel(new BorderLayout()); 


		String RcolumnNames[] = {"ID", "Name","Fläche[mm^2]","Fläche%"}; 
		DefaultTableModel model = new DefaultTableModel(RcolumnNames,15); 
		//TODO: evaluate fixed table length 

		final JTable data = new JTable(model);
		//		data.setPreferredSize(new Dimension(1, 150));
		data.setShowGrid(true); 
		data.setEnabled(false);

		data.getColumnModel().getColumn(0).setPreferredWidth(30);
		data.getColumnModel().getColumn(1).setPreferredWidth(200); 
		data.getColumnModel().getColumn(2).setPreferredWidth(50); 
		data.getColumnModel().getColumn(3).setPreferredWidth(50); 

		//	data.getColumnModel().getColumn(4).setCellRenderer(new ResultsColorCellRenderer()); 
		//resultPanel.add(data.getTableHeader(), BorderLayout.NORTH);

		JScrollPane RscrollPane = new JScrollPane(data);
		RscrollPane.add(resultPanel); 
		RscrollPane.setPreferredSize(new Dimension(1,130)); 

		getContentPane().add(RscrollPane, BorderLayout.SOUTH);


		// end Results table

		/**
		 * MouseListener, triggers when user clicks on image
		 *
		 */
		class staticMouseListener implements MouseListener {

			@Override
			public void mouseReleased(MouseEvent e) { 
				if(Toolbar.getToolId() == 0){ //rectange selection

					Genus.getInstance().setArea(imp.getRoi());
					imp.setRoi(null, true);
					text.setText(text.getText() + "\nMessfläche gesetzt: " + Math.round(Genus.getInstance().getArea()) + " mm^2"); 
				}


				if(Toolbar.getToolId() == 4){ // line selection 
					try{


						imp.getRoi().setStrokeWidth(linewidth);
						imp.getRoi().setStrokeColor(colorPanel.getBackground());

						imp.getProcessor().drawRoi((imp.getRoi()));
						imp.updateAndDraw(); 
						imp.setRoi(0,0,0,0);
						mousePressed = null;

					}catch (NullPointerException a){
						// No ROI defined. nothing to do here 
					} 
				}


				if(Toolbar.getToolId() == 21){ // pencil 
					try{ 

						if(!mousePressed.equals(ic.getMousePosition())){ 
							imp.getProcessor().drawLine(mousePressed.x, mousePressed.y, ic.getMousePosition().x, ic.getMousePosition().y, linewidth);

							imp.updateAndDraw();
							mousePressed = null;
						}

					}catch (NullPointerException a){
						//mouse left screen, is processed in MouseExited
					} 
				}

				if(Toolbar.getToolId() == 20){ // Hand tool

					try{ 
						double w = ic.getSize().width/90;
						double h = ic.getSize().height/90;

						imageHorizontal.setValue((int) (imageHorizontal.getValue()- (ic.getMousePosition().x - mousePressed.x)/w));
						imageVertical.setValue((int) (imageVertical.getValue() - (ic.getMousePosition().y - mousePressed.y)/h)); 


					}catch(NullPointerException c){
						// Mouse released outside of image, nothing to do here
					}
				}

			}

			@Override
			public void mousePressed(MouseEvent e) { 
				if(Toolbar.getToolId() == 20 || Toolbar.getToolId() == 21){ // Hand Tool
					mousePressed = ic.getMousePosition(); //set old position for ic viewpoint movement 
				} 
			}

			@Override
			public void mouseExited(MouseEvent e) { 

				if(Toolbar.getToolId() == 21 && mousePressed !=null){ // Mouse released outside of screen 
					//	imp.getProcessor().drawLine(mousePressed.x, mousePressed.y, e.getLocationOnScreen().x, e.getLocationOnScreen().y, linewidth);
					//	imp.updateAndDraw();
					mousePressed = null;
				}


			}


			@Override
			public void mouseEntered(MouseEvent e) { 
			}

			@Override
			public void mouseClicked(MouseEvent e) { 

				switch (Toolbar.getToolId()) {

				case 11:  // lupe
					//					getIc().zoomIn((int)(getIc().getMousePosition().x), (int)(getIc().getMousePosition().y));

					magnification = imp.getCanvas().getMagnification();
					System.out.println(magnification);
					getContentPane().revalidate(); 
					break;

				case 13: //color picker
					updateColor();
					break;

				case 15: // IJ pencil 
					//	undoStack.add(undoPos); //TODO: set filled pixels for undo 
					break;

				case 5: 
					//draw dot
					//		imp.getProcessor().drawLine(ic.getMousePosition().x, ic.getMousePosition().y, ic.getMousePosition().x, ic.getMousePosition().y,linewidth );
					//	imp.getProcessor().draw(imp.getRoi());
					//	imp.updateAndDraw(); 
					//	mousePressed = null;

					break; 
				default: 
					//					System.out.println(Toolbar.getToolId());
					break;
				}

			}
		}

		//				getIc().addMouseWheelListener(new MouseWheelListener() {
		class mouseWheelListener implements MouseWheelListener{

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {


				if(e.getWheelRotation() > 0){
					getIc().zoomOut(e.getXOnScreen(), e.getYOnScreen());
					//							getIc().zoomOut((int)(getIc().getSrcRect().getCenterX()), (int)(getIc().getSrcRect().getCenterY()));
					magnification = imp.getCanvas().getMagnification();
					getContentPane().revalidate(); 

				}else{ 
					getIc().zoomIn(e.getXOnScreen(), e.getYOnScreen()); 
					//				getIc().zoomIn((int)(getIc().getSrcRect().getCenterX()), (int)(getIc().getSrcRect().getCenterY()));
					magnification = imp.getCanvas().getMagnification();
					getContentPane().revalidate(); 

				}
			}
		}


		//manual measurement start

		JPanel manualPanel = new JPanel(new BorderLayout());
		manualPanel.setPreferredSize(new Dimension(250,1));

		final JPanel userPanel = new JPanel();
		final JPanel view = new JPanel();
		view.setLayout(new BorderLayout());

		JPanel manualTools = new JPanel(new GridLayout(2, 2));
		final JButton BhandTool = createButton("Hand");
		final JButton BLupe = createButton("Lupe");

		final JButton Bpencil = createButton("Stift");
		final JButton Bline = createButton("Linie");

		final JButton BcolorChooser = createButton("Farbwähler");

		JPanel point = new JPanel(new BorderLayout());
		final JButton BmanualSelector = createButton("Thallus Auswahltool");
		BmanualSelector.setForeground(Color.gray);
		point.add(BmanualSelector, BorderLayout.CENTER);


		BmanualSelector.setPreferredSize(new Dimension(250, 25));

		manualTools.add(BhandTool);
		manualTools.add(BLupe);
		manualTools.add(Bpencil);
		manualTools.add(Bline);
		manualTools.add(BcolorChooser); 

		userPanel.add(manualTools);
		userPanel.add(point); 

		userPanel.setLayout(new BoxLayout(userPanel,1)); 

		final JButton addArea = MainGUI.createButton("Hinzufügen");

		JPanel maxChooser = new JPanel(new GridLayout(0, 2));

		final JTextField max = new JTextField("20%"); 
		JTextField maxLabel = new JTextField("Max Fläche:");
		maxLabel.setEditable(false);

		maxChooser.add(maxLabel);
		maxChooser.add(max);

		final JPanel lineChooser = new JPanel(new GridLayout(0,2));

		final JTextField lineWidthChooser = new JTextField("3"); 
		JTextField lineWidthChooserLabel = new JTextField("Stiftdicke");
		lineWidthChooserLabel.setEditable(false);

		lineChooser.add(lineWidthChooserLabel);
		lineChooser.add(lineWidthChooser);


		colorPanel = new JPanel();
		colorPanel.setBackground(Toolbar.getForegroundColor());

		JTextField colorField = new JTextField("Stiftfarbe: ");
		colorField.setEditable(false);
		maxChooser.add(colorField);
		maxChooser.add(colorPanel);

		userPanel.add(maxChooser);
		userPanel.add(lineChooser);

		JButton result = MainGUI.createButton("Fläche"); 
		final JButton undo = MainGUI.createButton("rückgängig"); 
		JPanel manualMitte = new JPanel(new GridLayout(0,2));
		manualMitte.add(undo);
		manualMitte.add(result);

		userPanel.add(manualMitte); 


		measurements.setShowGrid(true);
		measurements.setEnabled(true);
		measurements.setSelectionMode(0);
		measurements.setRowSelectionInterval(0, 0);

		measurements.getColumnModel().getColumn(0).setPreferredWidth(10);
		measurements.getColumnModel().getColumn(1).setPreferredWidth(100);
		measurements.getColumnModel().getColumn(2).setPreferredWidth(50);
		measurements.getColumnModel().getColumn(3).setPreferredWidth(10);

		measurements.getColumnModel().getColumn(3).setCellRenderer(new ManualColorCellRenderer()); 

		JTableHeader manualHeader = measurements.getTableHeader(); 
		userPanel.add(manualHeader);
		userPanel.add(measurements); 


		JPanel assignPanel = new JPanel(new BorderLayout()); 

		final JTextField id = new JTextField();
		id.setText("Id");
		id.setPreferredSize(new Dimension(30, 30));
		assignPanel.add(id, BorderLayout.WEST);

		final JButton assign = MainGUI.createButton("Zuordnen");
		assignPanel.add(assign);

		userPanel.add(assignPanel, BorderLayout.EAST); 

		text.setLineWrap(true);
		text.setWrapStyleWord(false);

		final JScrollPane mainPane = new JScrollPane(text);
		mainPane.setPreferredSize(new Dimension(30, 1));

		view.add(mainPane,BorderLayout.CENTER);
		text.setText("Um die Manuelle Analyse zu starten öffnen Sie ein Bild und klicken Sie auf 'Manuelle Analyse starten' unter 'Daten'.");

		manualPanel.add(userPanel, BorderLayout.SOUTH); 
		manualPanel.add(view, BorderLayout.CENTER);	


		JPanel northPanel = new JPanel(new GridLayout(0,2));

		bar  = new ProgressBar(manualPanel.getWidth()-40, 20);
		northPanel.add(bar);

		manualPanel.add(northPanel, BorderLayout.NORTH);
		getContentPane().add(manualPanel, BorderLayout.EAST);


		final JTextField mousePos = new JTextField();
		mousePos.setEditable(false);
		northPanel.add(mousePos); 



		//--------Tools Action Listener--------//

		class mouseMotionListener implements MouseMotionListener{


			@Override
			public void mouseMoved(MouseEvent a) {
				try{ 
					mousePos.setText("x:y " + (int)(getIc().getMousePosition().x/magnification) + ":" + (int)(getIc().getMousePosition().y/magnification));
					getIc().mouseMoved(a);

				}catch(NullPointerException c){
					//Nothing to do here, seems to happen from time to time
				}
			}

			@Override
			public void mouseDragged(MouseEvent arg0) {

			}
		}


		Bline.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) { 
				Bdraw.getActionListeners()[0].actionPerformed(null);
			} 
		});


		BcolorChooser.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) { 
				Bpicker.getActionListeners()[0].actionPerformed(null);
			} 
		});

		BhandTool.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				t.setTool(20);

				BmanualSelector.setBorderPainted(false);
				BhandTool.setBorderPainted(true);
				BLupe.setBorderPainted(false);

			} 
		});

		BsheetSize.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) { 

				final JFrame borderWidthChooser = new JFrame("Foliengröße wählen");
				borderWidthChooser.setSize(400, 120);
				borderWidthChooser.setVisible(true);

				JPanel ovPanel = new JPanel(new BorderLayout()); 
				JPanel panel = new JPanel(new GridLayout(2,2)); 

				ovPanel.add(panel, BorderLayout.CENTER);
				borderWidthChooser.add(ovPanel);

				final JTextField value = new JTextField("");

				final JTextArea infoLabel = new JTextArea("Bitte die Länge der langen Seite in mm eintragen,\noder"
						+ " eine Voreinstellung auswählen:");
				infoLabel.setPreferredSize(new Dimension(400, 35));

				infoLabel.setEditable(false);
				infoLabel.setEnabled(false);

				infoLabel.setBackground(Color.gray);
				infoLabel.setDisabledTextColor(Color.white);

				JButton dina4 = new JButton("DinA4");
				JButton done = new JButton("Wert setzen");
				JButton dina3 = new JButton("DinA3");

				ovPanel.add(infoLabel, BorderLayout.NORTH);

				panel.add(value); 
				panel.add(dina4);
				panel.add(done);
				panel.add(dina3);

				done.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						try{ 
							double lw = Double.parseDouble(value.getText());
							Processor.setSheetWidth(lw); 
							borderWidthChooser.setVisible(false);

						}catch (IllegalArgumentException e){
							infoLabel.setText("ungültiger Wert"); 
						} 
					}
				});

				dina3.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						try{ 
							Processor.setSheetWidth(420); 

							borderWidthChooser.setVisible(false);

						}catch (IllegalArgumentException e){
							infoLabel.setText("ungültiger Wert");

						} 
					}
				});

				dina4.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						try{ 
							Processor.setSheetWidth(297); 

							borderWidthChooser.setVisible(false);

						}catch (IllegalArgumentException e){
							infoLabel.setText("ungültiger Wert"); 
						} 
					}
				}); 

			}
		});

		Brotate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				imp.getProcessor().rotate(180);	
				imp.updateAndDraw(); 
			}
		});

		Bborder.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) { 

				final JFrame borderWidthChooser = new JFrame("Thallus Rahmendicke wählen");
				borderWidthChooser.setSize(280, 20);
				borderWidthChooser.setVisible(true);

				JPanel panel = new JPanel(new GridLayout(2,2)); 

				borderWidthChooser.add(panel);

				final JTextField value = new JTextField(borderWidth + "");
				JTextField valueLabel = new JTextField("Strichdicke in mm:");
				final JTextField infoLabel = new JTextField("");

				infoLabel.setEditable(false);
				valueLabel.setEditable(false);

				JButton done = new JButton("Wert setzen");

				panel.add(valueLabel);
				panel.add(value);
				panel.add(infoLabel);
				panel.add(done);

				done.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						try{ 
							double lw = Double.parseDouble(value.getText());
							borderWidth = lw; 
							borderWidthChooser.setVisible(false);

						}catch (IllegalArgumentException e){
							infoLabel.setText("ungültiger Wert");

						} 
					}
				}); 
			}
		});

		BLupe.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				t.setTool(11); 

				if(newColor) 
					fillColor = imp.getProcessor().getValue();
				newColor = false;


				BmanualSelector.setBorderPainted(false);
				BhandTool.setBorderPainted(false);
				BLupe.setBorderPainted(true);

			}
		});

		BmanualSelector.addActionListener(new ActionListener() { 

			@Override
			public void actionPerformed(ActionEvent arg0) {
				t.setTool(7);

				imp.getProcessor().setValue(fillColor); 
				newColor = true; 

				BmanualSelector.setBorderPainted(true);
				BhandTool.setBorderPainted(false);
				BLupe.setBorderPainted(false);	

			}
		});


		//---tools action listener//

		lineWidthChooser.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{ 
					linewidth = Integer.parseInt( lineWidthChooser.getText());

					text.setText(text.getText() + "\n" + "Wert gesetzt: " + linewidth);
				}catch(IllegalArgumentException e){

					text.setText(text.getText() + "\n" + "Bitte einen gültigen Wert eingeben, zb 3"); 
				}
			}
		});

		/**
		 * New maxium area percent value set
		 */
		max.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{

					String usertext = max.getText();
					if(usertext.contains("%"))
						usertext = usertext.substring(0, usertext.length()-1);

					double value = Double.parseDouble(usertext); 
					manualAnalyzer.setMax(imp.getWidth()*imp.getHeight()*(value/100)); 

					text.setText(text.getText() + "\n" + "Wert gesetzt: " + value + "%");
				}catch(NullPointerException e){ 
					text.setText(text.getText() + "\n" + "Kein Bild geöffnet");
				} catch(IllegalArgumentException e){ 
					text.setText(text.getText() + "\n" + "Bitte einen gültigen Wert eingeben, zb 20%"); 
				}
			}
		}); 


		/**
		 * Is called when the users adds area (in fast mode this is called by the actionListener
		 * @post x,y for undo are set
		 */
		addArea.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				manualAnalyzer.addArea(); 
				text.setText(text.getText() + "\n" + "Hinzugefügt: " + imp.getRoi().getPosX() + " " + imp.getRoi().getPosY());

			} 
		}); 

		/**
		 * Undo flood fill, substracts value from overall area
		 */
		undo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(manualAnalyzer.undo()){
					text.setText(text.getText() + "\n" + "Rückgängig");

					int index =0;
					//set overview table
					Object[][] measurementValues = getMeasurements();

					for(Object[] row: measurementValues){
						if(index ==10)
							return;

						int columnCount =0;
						for(Object o: row){ 
							if(o == null)
								o =""; 
							measurements.setValueAt(o, index, columnCount++);
						} 
						index++;
					}
					//set table


				}else{ 
					text.setText(text.getText() + "\n" + "Rückgängig nicht möglich"); 
				}
			}
		});

		/**
		 * calculate result for display
		 */
		result.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				double area = manualAnalyzer.getArea();

				text.setText(text.getText()+"\n" + "Ergebnis: " + Math.round(area * 100.0) /100.0 + " mm^2"); 
			} 
		}); 

		id.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				assign.doClick();

			}
		});

		/**
		 * Assign area to lichen depeding on the selected row 
		 * if a blank row is selected a new measurement is made
		 */ 
		class assignActionListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				MeasurementsFactory factory = MeasurementsFactory.getInstance();
				int speciesID;
				try{ 

					if(factory.returnAll().size() <= measurements.getSelectedRow()){

						speciesID = Integer.parseInt(id.getText()); 
						if(manualAnalyzer.assign(speciesID)){ 
							text.setText(text.getText() + "\n" + "Fläche erfolgreich zugewiesen"); 
							id.setText("");	

							if(measurements.getSelectedRow() < measurements.getRowCount()-1 ){ 
								measurements.setRowSelectionInterval(measurements.getSelectedRow()+1, measurements.getSelectedRow()+1);//move selection one row down
							}else{ 
								text.setText(text.getText() + "\n" + "Nur 10 Einträge möglich");
							}

						}else{ 
							text.setText(text.getText() + "\n" + "Es existiert kein Eintrag mit dieser ID, oder die ID besitzt bereits eine Farbe"); 
						} 

					}else{ // re-adding area to existing measurement 

						Object o = measurements.getValueAt(measurements.getSelectedRow(), 0); 

						manualAnalyzer.readd(o);
						text.setText(text.getText() + "\n" + "Fläche erfolgreich zugewiesen"); 

						id.setText("");
					}

					int index =0;

					//set overview table
					Object[][] measurementValues = getMeasurements();
					for(Object[] row: measurementValues){
						if(row[0] == null)
							return;
						int columnCount =0;
						for(Object o: row){ 
							measurements.setValueAt(o, index, columnCount++);
						} 
						index++;
					}
					//set table

					newColor = true;

				} catch(NumberFormatException e){ 
					text.setText(text.getText() + "\n" + "Es wurde keine Nummer eingegeben");
				} 
			} 
		} 

		assign.addActionListener(new assignActionListener()); 

		//manual measurement end 

		pack();
		setVisible(true);

		// ActionListener
		//------------------------//
		Bmanual.addActionListener(new ActionListener() {


			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(manualAnalyzer == null){ 

					if(imp !=null){ 
						BmanualSelector.setForeground(new Color(51,51,51)); 
						BmanualSelector.doClick();
						manualAnalyzer = new ManualAnalyzer(imp);


						text.setText("Wählen Sie jeweils den Innenbereich eines Thallus mit dem Thallus auswählen tool\n " +
								"Wenn alle ausgewählt sind klicken Sie auf 'Fläche berechnen'\n" +
								"Entspricht die Fläche der Gewünschten tragen Sie die ID im Feld ID ein und klicken Sie auf 'Zuordnen'"); 
					}else{
						JOptionPane.showMessageDialog(null, "Öffnen Sie bitte zuerst ein Bild");
					}
				}
			}
		});

		/**
		 * Opens a new Image, if another image is already opend the user is asked and on confirm all data from the old image is deleted
		 */
		Bnew.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(imp != null){
					if(JOptionPane.showConfirmDialog(null, "Sie haben bereits ein Bild geöffnet, möchten Sie" +
							" dieses schließen und ein neues öffnen?") == JOptionPane.YES_OPTION){ 

						MeasurementsFactory factory = MeasurementsFactory.getInstance();
						factory.reset();
						Genus.getInstance().reset();

						flushTable(measurements);
						flushTable(data);

						imp = fh.openImagePlus(); 
						pathField.setText(fh.getLastDir());

						imp.show();
						manualAnalyzer = null;
					}

				}else{
					imp = fh.openImagePlus(); 
					pathField.setText(fh.getLastDir());
					imp.show();

					manualAnalyzer = null;
				}

				try{ 
					getIc().addMouseListener(new staticMouseListener()); 
					getIc().addMouseMotionListener(new mouseMotionListener());
					getIc().addMouseWheelListener(new mouseWheelListener());

					//					ImageScrollPane.setViewportView(imp.getCanvas()); 

				}catch (NullPointerException e){
					//triggers when single windows for each images are opened, no problem here

				} 

				imp.getProcessor().setValue(makeColor(255, 5, 5));
				//set Scrollbars to 0:
				imageHorizontal.setValue(0);
				imageVertical.setValue(0);
			}
		});

		/**
		 * Save ResultTable Data to .csv file with DataExporter.java 
		 */
		Bsave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				DataExporter d = new DataExporter(); 
				Genus lichen = Genus.getInstance();
				Object[][] Exdata = lichen.getExportResultTableData(); 

				if(Exdata[2][0] != null){
					JFileChooser chooser = new JFileChooser();

					chooser.setFileFilter(new FileNameExtensionFilter("csv", ".csv"));
					chooser.showSaveDialog(null); 
					String path;
					try{ 
						path = chooser.getSelectedFile().getAbsolutePath(); 

						if(d.export(Exdata, path)){ 
							JOptionPane.showMessageDialog(null, "Erfolgreich exportiert");
						}else{ 
							JOptionPane.showMessageDialog(null, "Export Fehler");
						}

					}catch (NullPointerException e){
						System.err.println("no file choosen");
					} 
				}else{
					JOptionPane.showMessageDialog(null, "Keine Daten vorhanden");
				}

			}
		});

		Bclose.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) { 
				if(JOptionPane.showConfirmDialog(null, "Wollen sie das Programm wirklich " +
						"schließen?") == JOptionPane.YES_OPTION){ 
					System.exit(0);
				} 
			}
		}); 

		/**
		 * About this program window 
		 */
		Babout.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFrame aboutFrame = new JFrame("über Flechtenanalyse");
				//				aboutFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE); 
				aboutFrame.setSize(500, 180);
				String text = "Autor: Michael Menzel, Fachbereich MNI Technische Hochschule Mittelhessen\n\n" +
						"Im Auftrag von Prof. Dr. Ute Windisch, Fachbereich KMUB (THM) \n\n" +
						"Flechtenanalyseprogramm, Versionsnr: " + version + "\n"
						+ "Erstellungsdatum: " + date  + "\n\n"+
						"Das Programm steht unter:\nGNU General Public License, version 3 (GPL-3.0)\n" +
						"http://opensource.org/licenses/GPL-3.0"; 
				JTextArea aboutText = new JTextArea(text);
				aboutText.setEditable(false);
				aboutFrame.add(aboutText);
				aboutFrame.setVisible(true);
			}
		});

		Breset.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				MeasurementsFactory factory = MeasurementsFactory.getInstance();
				factory.reset();
				Genus.getInstance().reset();

				flushTable(measurements);
				flushTable(data);

				imp  = fh.reloadImage();
				imp.show(); 

				manualAnalyzer = null;

				JOptionPane.showMessageDialog(null, "Speicher erfolgreich geleert");

				try{ 
					getIc().addMouseListener(new staticMouseListener()); 
					getIc().addMouseMotionListener(new mouseMotionListener());
					getIc().addMouseWheelListener(new mouseWheelListener());

				}catch (NullPointerException e){
					//triggers when single windows for each images are opened, no problem here

				}



			}
		});

		Bauto.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) { 

				int[] image = auto.autoAnalyze(imp);

				ImageProcessor pr = new ColorProcessor(imp.getWidth(), imp.getHeight(), image);
				imp.setProcessor(pr); 

				getContentPane().revalidate(); 
			}
		});

		
		Bpencil.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) { 
						t.installBuiltinTool("Brush");
			}
		});

		Bdraw.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				//				t.installBuiltinTool("Brush"); 
				//	t.installBuiltinTool("Pencil"); 

				if(newColor) 
					fillColor = imp.getProcessor().getValue();
				newColor = false;

				//		t.installBuiltinTool("Brush");
				t.setTool(4);

				//		Toolbar.setBrushSize((int)Math.round(pixelrate*0.76));
			}
		});

		Bpicker.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) { 

				if(newColor) 
					fillColor = imp.getProcessor().getValue();
				newColor = false;

				t.setTool("dropper");
			}
		}); 

		Barea.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				t.setTool(0);
			}
		}); 

		BpictureSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String path;

				JFileChooser jc = new JFileChooser(fh.getLastDir());
				jc.setSelectedFile(new File(fh.getLastDir()));
				jc.showDialog(null, "Ziel wählen"); 
				path = jc.getSelectedFile().toString();

				if(path !=null)
					fh.saveImage(imp, path);

			}
		});

		searchField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				searchButton.doClick();
			}
		});

		searchButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				//		System.out.println(searchField.getText());
				String searchTerm = searchField.getText();
				Genus lichen = Genus.getInstance();

				ArrayList<Species>list = lichen.returnAll();
				for(Species species: list){
					//		if(species.getName().startsWith(searchTerm) || species.getName().contains(searchTerm)){
					if(species.getName().startsWith(searchTerm)){ 
						for(int j =0; j< table.getRowCount();j++){

							if(table.getValueAt(j, 0) != null)
								if(Integer.toString(species.getId()).equals(table.getValueAt(j, 0).toString())){ 
									table.getSelectionModel().setSelectionInterval(j	,j);
									table.scrollRectToVisible(new Rectangle(table.getCellRect(j,0, true)));
									return;							
								} 
						}

					}
				}
			}
		}); 


		BcolorList.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				final JFrame choose = new JFrame("Wähle Farbreihenfolge"); 
				choose.setLayout(new BorderLayout());
				choose.setSize(200, 280);

				final Color[] colors = ColorStack.getAllColors(); 
				final JTable chooseTable = new JTable(colors.length,2); 
				final JTextArea notice = new JTextArea("Bitte Reihenfolge eintragen");
				notice.setEditable(false);

				chooseTable.getTableHeader().setReorderingAllowed(false);
				chooseTable.getColumnModel().getColumn(0).setPreferredWidth(30);
				chooseTable.getColumnModel().getColumn(1).setPreferredWidth(50); 

				chooseTable.getColumnModel().getColumn(1).setCellRenderer(new ChooserColorCellRenderer()); 

				JPanel buttonPanel  = new JPanel(new GridLayout(0, 2));
				JButton finish = createButton("Fertig");
				JButton cancel = createButton("Abbruch");

				buttonPanel.add(finish);
				buttonPanel.add(cancel);


				JPanel choosePanel = new JPanel(new BorderLayout());
				choosePanel.add(buttonPanel, BorderLayout.SOUTH);

				choosePanel.add(chooseTable, BorderLayout.CENTER); 

				choose.add(choosePanel, BorderLayout.CENTER);
				choose.add(notice, BorderLayout.SOUTH);
				choose.setVisible(true);

				/**
				 * Cancels the insertion and does nothing to color stack
				 */
				cancel.addActionListener(new ActionListener() {


					@Override
					public void actionPerformed(ActionEvent e) {

						choose.setVisible(false); 
						choose.setEnabled(false);
					}
				});

				/**
				 * Read user entered values and apply to ColorStack 
				 */
				finish.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						chooseTable.setEnabled(false);
						chooseTable.clearSelection();
						Color newColors[] = new Color[11];

						for(int i =0; i< colors.length; i++){

							try{ 
								Object o =chooseTable.getModel().getValueAt(i, 0); 
								newColors[Integer.parseInt(o.toString())] = colors[i]; 

							}catch(NullPointerException n){ 
								System.out.println("bla");
								//No Id inserted, get value somehow 
							}catch(IllegalArgumentException n){

								System.out.println("bla");
								//Id out of range 
							}
						} 
						ColorStack.setColorPos(newColors);
						choose.setVisible(false);
					}
				}); 

			}
		});

		/**
		 * Color choose
		 */
		Bchooser.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				final JFrame choose = new JFrame("Wähle Farben"); 
				choose.setLayout(new BorderLayout());
				choose.setSize(300, 400);
				final ColorMatcher matcher = new ColorMatcher(); 

				final Color[] colors; 
				colors = matcher.getColors();

				if(colors.length == 0){
					JOptionPane.showMessageDialog(null, "Keine Farben vorhanden");

				}else{ 

					final JTable chooseTable = new JTable(colors.length,3); 
					final JTextArea notice = new JTextArea("Bitte IDs eintragen");

					chooseTable.getTableHeader().setReorderingAllowed(false);
					chooseTable.getColumnModel().getColumn(0).setPreferredWidth(30);
					chooseTable.getColumnModel().getColumn(1).setPreferredWidth(200); 
					chooseTable.getColumnModel().getColumn(2).setPreferredWidth(50); 

					chooseTable.getColumnModel().getColumn(2).setCellRenderer(new ResultsColorCellRenderer()); 

					JButton finish = createButton("Fertig");
					JPanel choosePanel = new JPanel();
					chooseTable.setShowGrid(true);

					choosePanel.add(finish); 
					choosePanel.add(chooseTable); 

					choose.add(choosePanel, BorderLayout.CENTER);
					choose.add(notice, BorderLayout.SOUTH);
					choose.setVisible(true);


					/**
					 * Read user entered values and apply to data
					 */
					finish.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent arg0) { 
							chooseTable.setEnabled(false);
							chooseTable.clearSelection();

							for(int i =0; i< colors.length; i++){

								int id;
								if(chooseTable.getModel().getValueAt(i, 0) == null){
									id = Genus.getInstance().getSize();//get id of last lichen: "unbekannt"

								}else{ 
									id = Integer.parseInt(chooseTable.getModel().getValueAt(i,0).toString());
								}

								try {
									matcher.setColor(id, i);

									notice.setText("Zuweisung erfolgreich");
									choose.setVisible(false);

								} catch (NameNotFoundException e) {
									notice.setText("ID " + id + " wurde nicht gefunden");
									System.out.println("not found ");
								} 
							} 
						}
					}); 

				}
			}
		}); 

		/**
		 * ShowResults
		 * Adds the results from getResultsTableData() to the result table data 
		 */
		BshowResults.addActionListener(new ActionListener() {


			@Override
			public void actionPerformed(ActionEvent arg0) {

				Object[][] Values = getResultTableData();

				int i =0;
				int j=0;

				if(Values != null){
					for(Object[] row: Values){
						if(row[0] == null && row[2] == null){
							return;
						}
						for(Object value: row){ 

							data.setValueAt(value, i, j); 
							j++;
						} 
						j=0;
						i++;
					} 
				}
			}

		}); 
	}
	//------------------------//

	/**
	 * Gets all measurements as Object[][] to fill the table 
	 * @return Object array with values from measurements
	 */
	private Object[][] getMeasurements() {
		Object[][] tableData = new Object[20][4];
		int i =0; 

		MeasurementsFactory factory = MeasurementsFactory.getInstance();
		Genus lichen = Genus.getInstance();
		ArrayList<Measurement> list = factory.returnAll();
		for(Measurement tmp: list){ 

			tableData[i][0] = tmp.getSpecies(); 

			try {
				tableData[i][1] = lichen.getSpeciesFromID(tmp.getSpecies()).getName();
			} catch (NameNotFoundException e) { } //impossible to reach here //impossible to reach here, tested before

			tableData[i][2] = Math.round(tmp.getArea()*100.0)/100.0;
			tableData[i++][3] = ""; 

		}
		return tableData;
	}

	/**
	 * Converts from Integer value to RGB Color
	 * @param value integer value represents a Color (imageJ coding)
	 * @return java Color object
	 */
	public Color convertFromIJIntToColor(int c) {
		int r,g,b;
		r = (c&0xff0000)>>16;	
							g =(c&0xff00)>>8;
						b = c&0xff;

						return new Color(r,g,b);
	}

	/**
	 * Calculate areas, and pack together with id's names etc.
	 * @return  Object[][] with result Data
	 */
	private Object[][] getResultTableData() {

		Genus lichen = Genus.getInstance();
		Object[][] val = lichen.getResultTableData();
		if(val == null){
			JOptionPane.showMessageDialog(null, "Noch keine Daten vorhanden.\n"); 
		}
		return val; 

	}

	/**
	 * @return Object[][] with all lichen names, species 
	 */
	private Object[][] getTableData() { 
		return Genus.getInstance().getTableData();

	}

	/**
	 * Creates a Button
	 * @param buttonName
	 * @return the button
	 */
	protected static JButton createButton(String buttonName) {
		JButton button = new JButton(buttonName);
		button.setName(buttonName);

		return button;
	}

	/**
	 * creates color in ImageJ Integer form with bitshift 
	 * @param i - Rvalue
	 * @param j - Gvalue
	 * @param k - Bvalue
	 * @return color in one integer 
	 */
	public int makeColor(int i, int j, int k) {
		return (((int)i&0x0ff)<<16)|(((int)j&0x0ff)<<8)|((int)k&0x0ff); 
	}

	/**
	 * @return the imp
	 */
	public ImagePlus getImp() {
		return imp;
	}

	/**
	 * 
	 * @param imp
	 */
	public void setImp(ImagePlus imp){
		this.imp = imp;
	}
	/**
	 * @return the myProcessor
	 */
	public Processor getMyProcessor() {
		return myProcessor;
	}

	/**
	 * getter for toolbar
	 * @return
	 */
	public Toolbar getT() {
		return t;
	}

	public int getBlurValue() {
		return blurValue;
	}

	public static MainGUI getInstance(){
		return gui;
	}

	/**
	 * Removes the old Image from the view if one exists
	 * Adds the imageCanvas to the MainGUI Frame
	 * @param ic - imageCanvans
	 * @post Image is added to the view
	 */
	public void addImageCanvas(ImageCanvas ic) {

		if(this.ic != null){ 
			this.image.remove(this.ic);
		} 

		ic.setSize(new Dimension(image.getSize().width, image.getSize().height));
		this.ic = ic;

		this.image.add(ic);


	}

	public ImageCanvas getIc() {
		return ic;
	} 


	/**
	 * flushs the given table
	 * @post table is empty
	 * @param table - table to be flushed
	 */
	public void flushTable(JTable table){
		for(int i = 0; i < table.getRowCount();i++){
			for(int j = 0; j < table.getColumnCount(); j++){
				table.setValueAt("", i,j); 
			} 
		} 
	}

	/**
	 * Setter Pixelrate for linewidth with brush
	 * @param pixelrate- pixel per mm
	 */
	public void setPixelrate(double pixelrate) {
		this.pixelrate = pixelrate;

	}

	/**
	 * Called by color picker
	 * Sets active color to GUI
	 */
	public void updateColor() { 
		colorPanel.setBackground(Toolbar.getForegroundColor());
	} 

	/**
	 * Getter Progress//Bar in MainGUi
	 * @return
	 */
	public ProgressBar getBar(){ 
		return this.bar;
	}

	/**
	 * @return the borderWidth
	 */
	public double getBorderWidth() {
		return borderWidth;
	}

	/**
	 * 
	 * @param undoStack
	 */
	public void setUndoStack(UndoStack undoStack) {
		this.undoStack = undoStack;

	}

	/**
	 * @return the text
	 */
	public JTextArea getText() {
		return text;
	}

	/**
	 * @param styleModern the styleModern to set
	 */
	public static void setStyleModern(boolean styleModern) {
		MainGUI.styleModern = styleModern;
	}

}
