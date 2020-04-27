/************************************************************
 *  Name:  Michael Simon                   			        *
 *                    										*
 *  Info:  Creates GUI for sorting algorithms.              *
 ***********************************************************/
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.security.SecureRandom;


public class SortAnimationPanel extends JPanel implements Runnable {

    private static final Logger logger = LogManager.getLogger(SortAnimationPanel.class);
    private int panelWidth;
    private int panelHeight;
    private int sortDir;
    private int sleepTime;
    private int rType;
    private int sType;
    private boolean doStop = false;
    private volatile boolean paused = false;
    private int[] randArray;// = new int[this.getPanelWidth()];
    private final Object pauseLock = new Object();
    private long start;
    private long elapsedTime;


    // constructor
    public SortAnimationPanel() {
        setPanelWidth(panelWidth);
        setPanelHeight(panelHeight);
    }

    // set and get panel width
    public void setPanelWidth(int panelWidth)
    {
        this.panelWidth = panelWidth;
    }
    public int getPanelWidth()
    {
        return this.panelWidth;
    }

    // set and get panelHeight
    public void setPanelHeight(int panelHeight)
    {
        this.panelHeight = panelHeight;
    }
    public int getPanelHeight()
    {
        return this.panelHeight;
    }

    // set sort direction
    public void setDirection(int sortDir)
    {
        this.sortDir = sortDir;
    }

    // set and get sleep time
    public void setSleep(int sleepTime)
    {
        this.sleepTime = sleepTime;
    }
    private int getSleep() {
        if (sleepTime == 1) {
            logger.info("Fast");
            this.sleepTime = 10;
        }
        else if (sleepTime == 2) {
            logger.info("Med");
            this.sleepTime = 50;
        }
        else if (sleepTime == 3) {
            logger.info("Slow");
            this.sleepTime = 100;
        }
        return sleepTime;
    }
    // set and get elapsed time
    private void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }
    private String getElapsedTime() {

        String elapsedTimeStr = Long.toString(this.elapsedTime);
        return elapsedTimeStr + " ms";
    }
    // randomize input array
    public int[] randomize(int[] randArray) {
        SecureRandom generator = new SecureRandom();

        for (int i = 0; i < this.panelWidth; i++) {
            int randNum = generator.nextInt(this.panelHeight);
            randArray[i] = randNum;
        }
        return randArray;
    }
    // set and get random array
    public void setRandArray(int[] randArray) {
        this.randArray = randArray;

    }
//    public int[] getRandArray()
//    {
//        return this.randArray;
//    }

    // set and get rand type index
    public void setRandType(int rType)
    {
        this.rType = rType;
    }
    private void getRandType() {

        switch(rType)
        {
            case 1: {
                logger.info("Java SecureRandom");
                break;
            }
            case 2: {
                logger.info("Java Random");
                break;
            }
            case 3: {
                logger.info("random type 1");
                break;
            }
        }
    }
    // set and get sort type index
    public void setSortType(int sType)
    {
        this.sType = sType;
    }
    private void getSortType(int[] numArray) throws InterruptedException {

        // sort algorithm object
        // TODO - make singleton?
//        SortSelect select = new SortSelect();

        //long start;//, elapsedTime;
        switch(sType) {
            case 1: {
                logger.info("Bubble Sort");
                start = System.currentTimeMillis();
                bubbleSort(numArray);
                logger.info("Bubble Sort: " + this.elapsedTime + "ms");

                break;
            }
            case 2: {
                logger.info("Heap Sort");
                start = System.currentTimeMillis();
                heapSort(numArray);
                logger.info("Heap Sort: " + this.elapsedTime + "ms");

                break;
            }
            case 3: {
                logger.info("Insertion Sort");
                start = System.currentTimeMillis();
                insertionSort(numArray);
                logger.info("Insertion Sort: " + this.elapsedTime + "ms");

                break;
            }
            case 4: {
                logger.info("Merge Sort");
                start = System.currentTimeMillis();
                mergeSort(numArray, 0, numArray.length-1);
                logger.info("Merge Sort: " + this.elapsedTime + "ms");

                break;
            }
            case 5: {
                logger.info("Selection Sort");
                start = System.currentTimeMillis();
                selectionSort(numArray);
                logger.info("Selection Sort: " + this.elapsedTime + "ms");

                break;
            }
            case 6: {
                logger.info("Quick Sort");
                start = System.currentTimeMillis();
                quickSort(numArray, 0 , numArray.length-1);
                logger.info("Quick Sort: " + this.elapsedTime + "ms");

                break;
            }
        }
    }

    private synchronized boolean keepRunning() {
        return this.doStop == false;
    }

    private synchronized void doStart() {
        this.doStop = false;
    }

    public synchronized void doStop() {
        this.doStop = true;
    }

    public synchronized void pause() {
        this.paused = true;
    }

    public synchronized void resume() {
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notifyAll();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.white);
        g.setColor(Color.blue);

        for (int i = 0; i < getPanelWidth(); i++) {
            g.drawLine(i, getPanelHeight(), i, getPanelHeight() - randArray[i]);
        }
    }
    // thread execution code
    public void run() {
        try {
            //logger.info(Thread.currentThread().getName());  // print thread number
            getRandType();
            getSortType(randArray);
        }
        catch (InterruptedException exception) {
            exception.printStackTrace();
            Thread.currentThread().interrupt(); // re-interrupt the thread
        }
    }
    // reset after sort complete
    private void reset() {
        SortAnimationApp.sleepJComboBox.setEnabled(false);
        SortAnimationApp.pauseButton.setEnabled(false);
        SortAnimationApp.stopButton.setText("Reset");
    }
    // bubble sort
    private void bubbleSort(int[] numArray) throws InterruptedException {

        doStart();
        if (sortDir == 1) {
            int n = numArray.length;
            int temp;

            for (int i = 0; i < n; i++) {
                for (int j = 1; j < (n - i); j++) {
                    if (numArray[j - 1] > numArray[j]) {
                        synchronized (pauseLock) {
                            if (keepRunning()) {
                                temp = numArray[j - 1];
                                numArray[j - 1] = numArray[j];
                                numArray[j] = temp;
                                if (paused) {
                                    pauseLock.wait();
                                }
                                repaint();
                            } else
                                return;
                        }
                    }
                }
                setElapsedTime(System.currentTimeMillis() - start);
                SortAnimationApp.sPanel1.timerText.setText(getElapsedTime());
                Thread.sleep(getSleep());
            }
        }
        else {
            int n = numArray.length;
            int temp;

            for (int i = 0; i < n; i++) {
                for (int j = 1; j < (n - i); j++) {
                    if (numArray[j - 1] < numArray[j]) {
                        synchronized (pauseLock) {
                            if (keepRunning()) {
                                temp = numArray[j - 1];
                                numArray[j - 1] = numArray[j];
                                numArray[j] = temp;
                                if (paused) {
                                    pauseLock.wait();
                                }
                                repaint();
                            } else
                                return;
                        }
                    }
                }
                setElapsedTime(System.currentTimeMillis() - start);
                SortAnimationApp.sPanel1.timerText.setText(getElapsedTime());
                Thread.sleep(getSleep());
            }
        }
        reset();
    }
    // heap sort
    private void heapSort(int[] numArray) throws InterruptedException {

        doStart();
        if (sortDir == 1) {
            int n = numArray.length;

            for (int i = n / 2 - 1; i >= 0; i--)
                heapify(numArray, n, i);

            for (int i = n - 1; i >= 0; i--) {
                synchronized (pauseLock) {
                    if (keepRunning()) {
                        int temp = numArray[0];
                        numArray[0] = numArray[i];
                        numArray[i] = temp;

                        heapify(numArray, i, 0);

                        if (paused) {
                            pauseLock.wait();
                        }
                        repaint();
                    } else
                        return;
                }
                setElapsedTime(System.currentTimeMillis() - start);
                SortAnimationApp.sPanel2.timerText.setText(getElapsedTime());
                Thread.sleep(getSleep());
            }
        }
        reset();
    }
    private void heapify(int[] numArray, int n, int i) {
        int largest = i;
        int l = 2 * i + 1;
        int r = 2 * i + 2;

        if (l < n && numArray[l] > numArray[largest])
            largest = l;

        if (r < n && numArray[r] > numArray[largest])
            largest = r;

        if (largest != i) {
            int swap = numArray[i];
            numArray[i] = numArray[largest];
            numArray[largest] = swap;

            heapify(numArray, n, largest);
        }
    }
    // insertion sort
    private void insertionSort(int[] numArray) throws InterruptedException {

        doStart();
        if (sortDir == 1) {
            int i, j, newValue;
            for (i = 1; i < numArray.length; i++) {
                newValue = numArray[i];
                j = i;
                while (j > 0 && numArray[j - 1] > newValue) {
                    synchronized (pauseLock) {
                        if (keepRunning()) {
                            numArray[j] = numArray[j - 1];
                            j--;
                            if (paused) {
                                pauseLock.wait();
                            }
                            repaint();
                        } else
                            return;
                    }
                }
                numArray[j] = newValue;

                setElapsedTime(System.currentTimeMillis() - start);
                SortAnimationApp.sPanel2.timerText.setText(getElapsedTime());
                Thread.sleep(getSleep());
            }
        }
        else {
            return;
        }
        reset();
    }
    // merge sort
    private void mergeSort(int[] numArray, int l, int r) throws InterruptedException
    {
        doStart();
        if (sortDir == 1) {
            if (l < r) {
                // Find the middle point
                int m = (l + r) / 2;

                // Sort first and second halves
                mergeSort(numArray, l, m);
                mergeSort(numArray, m + 1, r);

                // Merge the sorted halves
                merge(numArray, l, m, r);

                setElapsedTime(System.currentTimeMillis() - start);
                SortAnimationApp.sPanel2.timerText.setText(getElapsedTime());
                Thread.sleep(getSleep());
            }
        }
        else {
            return;
        }
        reset();
    }
    private void merge(int[] numArray, int l, int m, int r) throws InterruptedException
    {
        // Find sizes of two subarrays to be merged
        int n1 = m - l + 1;
        int n2 = r - m;

        /* Create temp arrays */
        int L[] = new int [n1];
        int R[] = new int [n2];

        /*Copy data to temp arrays*/
        for (int i=0; i<n1; ++i)
            L[i] = numArray[l + i];
        for (int j=0; j<n2; ++j)
            R[j] = numArray[m + 1 + j];


        /* Merge the temp arrays */

        // Initial indexes of first and second subarrays
        int i = 0, j = 0;

        // Initial index of merged subarry array
        int k = l;
        while (i < n1 && j < n2) {
            synchronized (pauseLock) {
                if (keepRunning()) {
                    if (L[i] <= R[j]) {
                        numArray[k] = L[i];
                        i++;
                    }
                    else {
                        numArray[k] = R[j];
                        j++;
                    }
                    k++;
                    if (paused) {
                        pauseLock.wait();
                    }
                    repaint();
                }
                else
                    return;
            }
        }

        /* Copy remaining elements of L[] if any */
        while (i < n1) {
            numArray[k] = L[i];
            i++;
            k++;
        }

        /* Copy remaining elements of R[] if any */
        while (j < n2) {
            numArray[k] = R[j];
            j++;
            k++;
        }
    }
    // selection sort
    private void selectionSort(int[] numArray) throws InterruptedException {

        doStart();
        if (sortDir == 1) {
            for (int i = 0; i < numArray.length - 1; i++) {
                for (int j = i + 1; j < numArray.length; j++) {
                    synchronized (pauseLock) {
                        if (keepRunning()) {
                            if (numArray[i] > numArray[j]) {
                                int temp = numArray[i];
                                numArray[i] = numArray[j];
                                numArray[j] = temp;
                                if (paused) {
                                    pauseLock.wait();
                                }
                                repaint();
                            }
                        }
                        else
                            return;
                    }
                }

                setElapsedTime(System.currentTimeMillis() - start);
                SortAnimationApp.sPanel2.timerText.setText(getElapsedTime());
                Thread.sleep(getSleep());
            }
        }
        else {
            for (int i = 0; i < numArray.length - 1; i++) {
                for (int j = i + 1; j < numArray.length; j++) {
                    synchronized (pauseLock) {
                        if (keepRunning()) {
                            if (numArray[i] < numArray[j]) {
                                int temp = numArray[i];
                                numArray[i] = numArray[j];
                                numArray[j] = temp;
                                if (paused) {
                                    pauseLock.wait();
                                }
                                repaint();
                            }
                        }
                        else
                            return;
                    }
                }

                setElapsedTime(System.currentTimeMillis() - start);
                SortAnimationApp.sPanel2.timerText.setText(getElapsedTime());
                Thread.sleep(getSleep());
            }
        }
        reset();
    }
    // quick sort
    private void quickSort(int[] numArray, int begin, int end) throws InterruptedException {

        doStart();
        if (sortDir == 1) {
            int i = begin;
            int k = end;

            if (end - begin >= 1) {
                int pivot = numArray[begin];

                while (k > i) {
                    while (numArray[i] <= pivot && i <= end && k > i) {
                        i++;
                    }

                    while (numArray[k] > pivot && k >= begin && k >= i) {
                        k--;
                    }
                    if (k > i) {
                        synchronized (pauseLock) {
                            if (keepRunning()) {
                                int temp = numArray[i];
                                numArray[i] = numArray[k];
                                numArray[k] = temp;
                                if (paused) {
                                    pauseLock.wait();
                                }
                                repaint();
                            }
                            else
                                return;
                        }
                    }
                }
                int temp1 = numArray[begin];
                numArray[begin] = numArray[k];
                numArray[k] = temp1;
                repaint();
                quickSort(numArray, begin, k - 1);
                Thread.sleep(getSleep());
                quickSort(numArray, k + 1, end);

                setElapsedTime(System.currentTimeMillis() - start);
                SortAnimationApp.sPanel2.timerText.setText(getElapsedTime());
            }
            else
                return;
        }
        else {
            int i = begin;
            int k = end;

            if (end - begin >= 1) {
                int pivot = numArray[begin];

                while (k > i) {
                    while (numArray[i] >= pivot && i <= end && k > i) {
                        i++;
                    }

                    while (numArray[k] < pivot && k >= begin && k >= i) {
                        k--;
                    }
                    if (k > i) {
                        synchronized (pauseLock) {
                            if (keepRunning()) {
                                int temp = numArray[i];
                                numArray[i] = numArray[k];
                                numArray[k] = temp;
                                if (paused) {
                                    pauseLock.wait();
                                }
                                repaint();
                            }
                            else
                                return;
                        }
                    }
                }
                int temp1 = numArray[begin];
                numArray[begin] = numArray[k];
                numArray[k] = temp1;
                repaint();
                quickSort(numArray, begin, k - 1);
                Thread.sleep(getSleep());
                quickSort(numArray, k + 1, end);

                setElapsedTime(System.currentTimeMillis() - start);
                SortAnimationApp.sPanel2.timerText.setText(getElapsedTime());
            }
            else
                return;
        }
        reset();
    }
}
