import java.io.FileWriter;
import java.io.IOException;

public class ThreadRace {
    private int counter = 0;
    private FileWriter writer;

    public static void main(String[] args) {
        ThreadRace threadRace = new ThreadRace();
        try (FileWriter fileWriter = new FileWriter("src/main/resources/RaceResult")) {
            threadRace.writer = fileWriter;
            threadRace.moving();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private synchronized void increment() {
        counter++;
        try {
            writer.write(Thread.currentThread().getName() + " value = " + counter + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void moving() throws InterruptedException {
        Thread thread = new Thread(new RacerWithImplRunnable());
        RacerWithExThread racerWithExThread = new RacerWithExThread();
        racerWithExThread.start();
        thread.start();
        racerWithExThread.join();
        thread.join();

    }

    class RacerWithExThread extends Thread {
        @Override
        public void run() {
            while (counter < 99) {
                increment();
            }
        }
    }

    class RacerWithImplRunnable implements Runnable {

        @Override
        public void run() {
            while (counter < 99) {
                increment();
            }
        }
    }
}
