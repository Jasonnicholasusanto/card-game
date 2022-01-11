import java.io.IOException;

public class ProgressBar {

    private static final int PROGRESSBAR_LENGTH = 20;

    public void loading(int loadTime) throws InterruptedException {
        for(int i=0; i<=100; i++){
            drawProgressBar(i, 100);
            Thread.sleep(loadTime);
        }
    }

    public void drawProgressBar(int numerator, int denominator) {
        int percent = (int) (((double) numerator / (double) denominator) * 100);

        String bar = "[";
        int lines = round((PROGRESSBAR_LENGTH * numerator) / denominator);
        int blanks = PROGRESSBAR_LENGTH - lines;

        for (int i = 0; i < lines; i++) {
            bar += "|";
        }

        for (int i = 0; i < blanks; i++){
            bar += " ";
        }

        bar += "] " + percent + "%";

        System.out.print(bar + "\r");
    }

    private int round(double dbl) {
        int noDecimal = (int) dbl;
        double decimal = dbl - noDecimal;

        if (decimal >= 0.5)
            return noDecimal + 1;
        else
            return noDecimal;
    }

}
