import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class FileManager {

    private final PrintWriter pw;

    public FileManager(final String path) throws FileNotFoundException {
        pw = new PrintWriter(new File(path));
    }

    public void writeMessage(final String message){
        pw.write(message);
        pw.flush();
    }

    public void closeWriter(){
        pw.close();
    }
}
