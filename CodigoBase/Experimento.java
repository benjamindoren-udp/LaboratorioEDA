import java.util.Locale;

public class Experimento {

    public static String generarXHTMLSintetico(int N) {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n<html xmlns=\"http://www.w3.org/1999/xhtml\">\n");
        for (int i = 0; i < N - 1; i++) sb.append("<div>\n");
        for (int i = 0; i < N - 1; i++) sb.append("</div>\n");
        sb.append("</html>");
        return sb.toString();
    }

    public static void main(String[] args) {
        int reps = 100;
        int iterador[] = {10, 11, 12, 13, 14, 15};
        
        //pone un comentario menos hecho por el aimigo para explicar que es esto
        int multiplicadorTrabajo = 1000; 
        
        Out out = new Out("resultados_laboratorio.csv");
        out.println("N,PilaPrinceton,PilaDeCola,ColaPrinceton,ColaDePilas"); 

        for (int exp : iterador) {
            int N = (int) Math.pow(2, exp);
            System.out.println("Midiendo tiempos para N = " + N + " (Esto tomará unos segundos)...");

            String xhtmlPrueba = generarXHTMLSintetico(N);

            for (int r = 0; r < reps; r++) {
                
                //medicion pila princenton
                WebCrawler crawler1 = new WebCrawler(new PilaPrinceton<String>(), new ColaPrinceton<String>());
                StopwatchCPU timer1 = new StopwatchCPU();
                for (int i = 0; i < multiplicadorTrabajo; i++) {
                    crawler1.esXHTMLValido(xhtmlPrueba);
                }
                double tiempoT1 = timer1.elapsedTime() / multiplicadorTrabajo;
                String t1 = String.format(Locale.US, "%.8f", tiempoT1);


                //medicion pila de cola
                WebCrawler crawler2 = new WebCrawler(new PilaDeCola<String>(), new ColaPrinceton<String>());
                StopwatchCPU timer2 = new StopwatchCPU();
                for (int i = 0; i < multiplicadorTrabajo; i++) {
                    crawler2.esXHTMLValido(xhtmlPrueba);
                }
                double tiempoT2 = timer2.elapsedTime() / multiplicadorTrabajo;
                String t2 = String.format(Locale.US, "%.8f", tiempoT2);


                //medicion cola princenton
                StopwatchCPU timer3 = new StopwatchCPU();
                for (int i = 0; i < multiplicadorTrabajo; i++) {
                    Cola<String> colaPrinceton = new ColaPrinceton<String>();
                    WebCrawler.simularCrawlerOffline(N, colaPrinceton);
                }
                double tiempoT3 = timer3.elapsedTime() / multiplicadorTrabajo;
                String t3 = String.format(Locale.US, "%.8f", tiempoT3);


                //medicion cola de pilas
                StopwatchCPU timer4 = new StopwatchCPU();
                for (int i = 0; i < multiplicadorTrabajo; i++) {
                    Cola<String> colaDePilas = new ColaDePilas<String>();
                    WebCrawler.simularCrawlerOffline(N, colaDePilas);
                }
                double tiempoT4 = timer4.elapsedTime() / multiplicadorTrabajo;
                String t4 = String.format(Locale.US, "%.8f", tiempoT4);

                out.println(N + "," + t1 + "," + t2 + "," + t3 + "," + t4);
            }
        }
        
        out.close();
    }
}