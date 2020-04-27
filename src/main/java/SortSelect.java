//public class SortSelect {
//
//    // bubble sort
//    public void bubbleSort(int[] numArray) throws InterruptedException {
//
//        doStart();
//        if (sortDir == 1) {
//            int n = numArray.length;
//            int temp;
//
//            for (int i = 0; i < n; i++) {
//                for (int j = 1; j < (n - i); j++) {
//                    if (numArray[j - 1] > numArray[j]) {
//                        synchronized (pauseLock) {
//                            if (keepRunning()) {
//                                temp = numArray[j - 1];
//                                numArray[j - 1] = numArray[j];
//                                numArray[j] = temp;
//                                if (paused) {
//                                    pauseLock.wait();
//                                }
//                                repaint();
//                            } else
//                                return;
//                        }
//                    }
//                }
//                setElapsedTime(System.currentTimeMillis() - start);
//                SortAnimationApp.sPanel1.timerText.setText(getElapsedTime());
//                Thread.sleep(getSleep());
//            }
//        }
//        else {
//            int n = numArray.length;
//            int temp;
//
//            for (int i = 0; i < n; i++) {
//                for (int j = 1; j < (n - i); j++) {
//                    if (numArray[j - 1] < numArray[j]) {
//                        synchronized (pauseLock) {
//                            if (keepRunning()) {
//                                temp = numArray[j - 1];
//                                numArray[j - 1] = numArray[j];
//                                numArray[j] = temp;
//                                if (paused) {
//                                    pauseLock.wait();
//                                }
//                                repaint();
//                            } else
//                                return;
//                        }
//                    }
//                }
//                setElapsedTime(System.currentTimeMillis() - start);
//                SortAnimationApp.sPanel1.timerText.setText(getElapsedTime());
//                Thread.sleep(getSleep());
//            }
//        }
//        reset();
//    }
//}
