import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.concurrent.ArrayBlockingQueue;

public class ConcurrentFileWriter implements Runnable{

    private final ArrayBlockingQueue<String> outputQueue;
    private final PrintWriter pw;

    private boolean running = false;
    private Thread thread;

    public ConcurrentFileWriter(final String path) throws FileNotFoundException {
        outputQueue = new ArrayBlockingQueue<>(100);
        pw = new PrintWriter(path);
    }

    @Override
    public void run() {
        while(running){
            if(!outputQueue.isEmpty()){
                pw.write(outputQueue.poll());
                pw.flush();
            }
        }
        pw.close();
    }

    public synchronized void queueMessage(final String message) throws InterruptedException {
        outputQueue.put(message);
    }

    public synchronized void start(){
        if (running) {
            return;
        }
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop(){
        if(!running) {
            return;
        }

        running = false;
        try {
            thread.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
