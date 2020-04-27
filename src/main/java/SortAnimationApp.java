/************************************************************
 *  Name:  Michael Simon                   			        *
 *                    										*
 *  Info:  Creates GUI for sorting algorithms.              *
 ***********************************************************/

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
//import java.awt.event.ItemListener;
import java.awt.Taskbar;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class SortAnimationApp extends JFrame {

    private static final Logger logger = LogManager.getLogger(SortAnimationApp.class);
    private final JPanel controlPanel;
    private final JPanel displayPanel;
    public static SortPanel sPanel1;
    public static SortPanel sPanel2;

    private final JButton populateButton;
    private final JButton sortButton;
    public static JButton pauseButton;
    public static JButton stopButton;

    public static JComboBox<String> sleepJComboBox; // hold sleep dur names
    private static final String[] sleep =
            {"Fast", "Medium", "Slow"};
    private final int[] sleepNum =
            {1, 2, 3};
    private static int sleepTime = 1; // default speed = "fast"
    private static int[] randArray1;
    private static int[] randArray2;

    //constructor
    private SortAnimationApp() throws IOException {
        super("Sort Visualizer");
        setLayout(new BorderLayout());

        // panels
        displayPanel = new JPanel(new GridLayout());
        controlPanel = new JPanel();
        sPanel1 = new SortPanel(); // from SortPanel class
        sPanel2 = new SortPanel();

        // combo box
        sleepJComboBox = new JComboBox<>(sleep);
        sleepJComboBox.setMaximumRowCount(3);

        // buttons
        populateButton = new JButton("Populate Array");
        sortButton = new JButton("Sort"); // start animation
        pauseButton = new JButton("Pause");
        stopButton = new JButton("Stop");

        sortButton.setEnabled(false);
        pauseButton.setEnabled(false);
        stopButton.setEnabled(false);

        // add sort panels to display panel
        displayPanel.add(sPanel1);
        displayPanel.add(sPanel2);

        // add buttons to control panel
        controlPanel.add(populateButton);
        controlPanel.add(sortButton);
        controlPanel.add(sleepJComboBox);
        controlPanel.add(pauseButton);
        controlPanel.add(stopButton);

        // add sub-panels to JFrame
        this.add(displayPanel);
        this.add(controlPanel, BorderLayout.SOUTH);

        // register event handlers
        ButtonHandler handler = new ButtonHandler();
        populateButton.addActionListener(handler);
        sortButton.addActionListener(handler);
        stopButton.addActionListener(handler);
        pauseButton.addActionListener(handler);

        //sleepJComboBox.addItemListener(new ItemListener() {  // replaced with lambda
        //@Override
        //public void itemStateChanged(ItemEvent e) {

        sleepJComboBox.addItemListener((ItemEvent e) -> {  // lambda
            if (e.getStateChange() == ItemEvent.SELECTED) {
                // assoc. selected sleep duration with index number
                sleepTime = sleepNum[sleepJComboBox.getSelectedIndex()];
                sPanel1.animPanel.setSleep(sleepTime);
                sPanel2.animPanel.setSleep(sleepTime);
            }
        });
    }

    public static void main(String[] args) throws IOException {
        System.setProperty("apple.awt.application.name", "Sort Visualizer");
        BufferedImage image = ImageIO.read(new File("/Users/mfsimon/Documents/470/assn09/src/sort.png"));
        Taskbar.getTaskbar().setIconImage(image);
        // Taskbar taskbar = Taskbar.getTaskbar();
        // taskbar.setIconImage(image);

        SortAnimationApp application = new SortAnimationApp();
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        application.setSize(1350, 420);
        application.setVisible(true);
        application.setMinimumSize(new Dimension(770, 233));
        //application.setResizable(false);
    }

    private class ButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {

            if (event.getSource() == populateButton) {

                // dynamic panel sizing
                sPanel1.animPanel.setPanelWidth(sPanel1.drawPanel.getWidth());
                sPanel2.animPanel.setPanelWidth(sPanel1.drawPanel.getWidth());
                sPanel1.animPanel.setPanelHeight(sPanel1.drawPanel.getHeight());
                sPanel2.animPanel.setPanelHeight(sPanel1.drawPanel.getHeight());

                // panel size console output
                logger.info(sPanel1.animPanel.getPanelWidth());
                logger.info(sPanel1.animPanel.getPanelHeight());
                logger.info(sPanel2.animPanel.getPanelWidth());
                logger.info(sPanel2.animPanel.getPanelHeight());

                // rand array init
                randArray1 = new int[sPanel1.animPanel.getPanelWidth()];
                randArray2 = new int[sPanel2.animPanel.getPanelWidth()];

                // make rand array
                randArray1 = sPanel1.animPanel.randomize(randArray1);
                randArray2 = randArray1.clone(); // duplicate array

                // set rand array
                sPanel1.animPanel.setRandArray(randArray1);
                sPanel2.animPanel.setRandArray(randArray2); // opt same randArray

                // repaint
                sPanel1.revalidate();
                sPanel2.revalidate();
                sPanel1.repaint();
                sPanel2.repaint();

                // animPanel to drawPanel
                sPanel1.drawPanel.add(sPanel1.animPanel);
                sPanel2.drawPanel.add(sPanel2.animPanel);

                // enable/disable items
                populateButton.setEnabled(false);
                sortButton.setEnabled(true);
            }
            if (event.getSource() == sortButton) {

                // create new thread object
                Thread t1 = new Thread(sPanel1.animPanel);
                Thread t2 = new Thread(sPanel2.animPanel);

                // start new thread
                t1.start();
                t2.start();

                // assoc. selected type with index number
                int rType1 = sPanel1.randNum[sPanel1.randJComboBox.getSelectedIndex()];
                int rType2 = sPanel2.randNum[sPanel2.randJComboBox.getSelectedIndex()];
                int sType1 = sPanel1.sortNum[sPanel1.typeJComboBox.getSelectedIndex()];
                int sType2 = sPanel2.sortNum[sPanel2.typeJComboBox.getSelectedIndex()];
                int sortDir1 = sPanel1.dirNum[sPanel1.dirJComboBox.getSelectedIndex()];
                int sortDir2 = sPanel2.dirNum[sPanel2.dirJComboBox.getSelectedIndex()];

                // set rand type, sort type, direction and sleep time
                sPanel1.animPanel.setRandType(rType1);
                sPanel2.animPanel.setRandType(rType2);
                sPanel1.animPanel.setSortType(sType1);
                sPanel2.animPanel.setSortType(sType2);
                sPanel1.animPanel.setDirection(sortDir1);
                sPanel2.animPanel.setDirection(sortDir2);
                sPanel1.animPanel.setSleep(sleepTime);
                sPanel2.animPanel.setSleep(sleepTime);

                // enable/disable items
                sortButton.setEnabled(false);
                pauseButton.setEnabled(true);
                stopButton.setEnabled(true);
                sPanel1.randJComboBox.setEnabled(false);
                sPanel2.randJComboBox.setEnabled(false);
                sPanel1.typeJComboBox.setEnabled(false);
                sPanel2.typeJComboBox.setEnabled(false);
                sPanel1.dirJComboBox.setEnabled(false);
                sPanel2.dirJComboBox.setEnabled(false);
            }
            if (event.getSource() == pauseButton) {
                String command = event.getActionCommand();
                if (command.equals("Pause")) {
                    pauseButton.setText("Resume");
                    sPanel1.animPanel.pause();
                    sPanel2.animPanel.pause();
                } else {
                    pauseButton.setText("Pause");
                    sPanel1.animPanel.resume();
                    sPanel2.animPanel.resume();
                }
            }
            if (event.getSource() == stopButton) {
                sPanel1.animPanel.resume();
                sPanel2.animPanel.resume();
                sPanel1.animPanel.doStop();
                sPanel2.animPanel.doStop();
                pauseButton.setText("Pause");
                sPanel1.drawPanel.removeAll(); // clears panel
                sPanel2.drawPanel.removeAll();
                sPanel1.drawPanel.updateUI();
                sPanel2.drawPanel.updateUI();

                // reset timer text
                sPanel1.timerText.setText(" ");
                sPanel2.timerText.setText(" ");

                // enable/disable items
                populateButton.setEnabled(true);
                stopButton.setEnabled(false);
                pauseButton.setEnabled(false);
                sleepJComboBox.setEnabled(true);
                sPanel1.randJComboBox.setEnabled(true);
                sPanel2.randJComboBox.setEnabled(true);
                sPanel1.typeJComboBox.setEnabled(true);
                sPanel2.typeJComboBox.setEnabled(true);
                sPanel1.dirJComboBox.setEnabled(true);
                sPanel2.dirJComboBox.setEnabled(true);
                stopButton.setText("Stop");
            }
        }
    }
}
