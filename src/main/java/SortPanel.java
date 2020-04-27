/************************************************************
 *  Name:  Michael Simon                   			        *
 *                    										*
 *  Info:  Creates GUI for sorting algorithms.              *
 ***********************************************************/
import javax.swing.*;

import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;


public class SortPanel extends JPanel {

    private final JPanel mainPanel;
    private final JPanel buttonPanel;
    private final JPanel timerPanel;

    public JLabel timerText;
    public JPanel drawPanel;
    public final JComboBox<String> randJComboBox;  // hold rand type names
    public final JComboBox<String> typeJComboBox;  // hold sort type names
    public final JComboBox<String> dirJComboBox;   // hold direction
    public static final String[] rand =
            {"Java Secure Random","example2","example3"};
    public static final String[] sort =
            {"Bubble","Heap","Insertion","Merge","Selection","Quick"};
    public static final String[] direction =
            {"Ascending","Descending"};
    public final int [] randNum =
            {1,2,3};
    public final int[] sortNum =
            {1,2,3,4,5,6};
    public final int[] dirNum =
            {1,2};
    SortAnimationPanel animPanel;

    // constructor
    public SortPanel() {

        setLayout(new BorderLayout());
        animPanel = new SortAnimationPanel();

        // panels
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        timerPanel = new JPanel();
        timerPanel.setBackground(Color.white);
        drawPanel = new JPanel(new BorderLayout());
        drawPanel.setBackground(Color.white);
        buttonPanel = new JPanel();

        // combo boxes
        randJComboBox = new JComboBox<>(rand);
        typeJComboBox = new JComboBox<>(sort);
        dirJComboBox = new JComboBox<>(direction);

        randJComboBox.setMaximumRowCount(3);
        typeJComboBox.setMaximumRowCount(6);
        dirJComboBox.setMaximumRowCount(2);

        // add timer field to timer panel
        timerText = new JLabel(" ");
        timerPanel.add(timerText);

        // add items to button panel
        buttonPanel.add(randJComboBox);
        buttonPanel.add(typeJComboBox);
        buttonPanel.add(dirJComboBox);

        // add sub-panels to mainPanel
        mainPanel.add(timerPanel, BorderLayout.NORTH);
        mainPanel.add(drawPanel);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // add mainPanel to JPanel
        add(mainPanel);
    }
}
