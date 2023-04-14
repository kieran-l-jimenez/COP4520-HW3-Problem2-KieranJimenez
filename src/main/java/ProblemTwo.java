import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.*;

/* Problem 2: Atmospheric Temperature Reading Module
 * 8 sensors - 8 threads
 * Temperature Reading: Every minute temperature sensor generates random number [-100, 70]
 * Every minute do readings
 * Every hour :
 *      Report 5 highest temps
 *      Report 5 lowest temperatures
 *      Report 10-minute interval with the greatest temperature difference
 * NOTE: For brevity might want to just simulate with 1 minute = 1 ns, 1 hour = 60 ns
 * Shared Storage - 60 integers and index represents minute of the hour taken
 */

public class ProblemTwo {
    //All the recorded temperatures go into one memory space. It would be trivial if we wanted to put all operations in
        //same memory space/int[]
    public static int[] sharedMem;
    public static int[] loTemps;
    public static int[] hiTemps;
    public static int[] avgTemps;
    public static int[] diffTemp;

    public static void main(String[] args) throws InterruptedException {
        sharedMem = new int[480];
        avgTemps = new int[60];
        loTemps = new int[5];
        hiTemps = new int[5];

        roverThread[] CPU = new roverThread[8];
        for (int i = 0; i < CPU.length; i++) {
            CPU[i] = new roverThread();
            CPU[i].setName(String.valueOf(i));
        }
        for (ProblemTwo.roverThread roverThread : CPU) {
            roverThread.start();
        }

        compileReport();
    }

    private static void compileReport() throws InterruptedException {
        System.out.println("The average temperatures recorded were: ");
        for (int i = 0; i < 60; i++) {
            int t = 0;
            for (int j = 0; j < 8; j++) {
                t += sharedMem[(i*8) + j];
            }
            avgTemps[i] = t/8;
            System.out.println(avgTemps[i]);
        }

        highsThread hThread = new highsThread();
        lowsThread lThread = new lowsThread();
        differenceThread difThread = new differenceThread();
        hThread.start();
        lThread.start();
        difThread.start();
        hThread.join();
        lThread.join();
        difThread.join();

        System.out.println("The 5 highest temperatures recorded were:");
        System.out.println(Arrays.toString(hiTemps));
        System.out.println("The 5 lowest temperatures recorded were:");
        System.out.println(Arrays.toString(loTemps));
        System.out.println("The greatest temperature difference was:");
        System.out.println("From minute "+diffTemp[2]+" to "+(diffTemp[2]+9)+": "+Math.abs(diffTemp[0]-diffTemp[1]));
    }

    public static class roverThread extends Thread {
        public void run() {
            int id = Integer.parseInt(this.getName());
            ThreadLocalRandom sensor = ThreadLocalRandom.current();

            for (int i = 0; i < 60; i++) {
                try {
                    sharedMem[ (i*8) + id] = sensor.nextInt(-100, 71);
                    //wait(0, 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class lowsThread extends Thread {
        public void run() {
            int[] temp = avgTemps;
            Arrays.sort(temp);
            loTemps[0] = temp[0];
            loTemps[1] = temp[1];
            loTemps[2] = temp[2];
            loTemps[3] = temp[3];
            loTemps[4] = temp[4];
        }
    }

    public static class highsThread extends Thread {
        public void run() {
            int[] temp = avgTemps;
            Arrays.sort(temp);
            hiTemps = new int[]{temp[59], temp[58], temp[57], temp[56], temp[55]};
        }
    }

    public static class differenceThread extends Thread {
        public void run() {
            diffTemp = new int[]{avgTemps[0], avgTemps[9], 0};
            for (int i = 10; i < 60; i++) {
                if (Math.abs(diffTemp[0]-diffTemp[1]) < Math.abs(avgTemps[i-9]-avgTemps[i])) {
                    diffTemp[0] = avgTemps[i-9];
                    diffTemp[1] = avgTemps[i];
                    diffTemp[2] = i-9;
                }
            }
        }
    }
}
