public class Experimento {

    public static String generarXHTMLSintetico(int N) {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n<html xmlns=\"http://www.w3.org/1999/xhtml\">\n");
        
        for (int i = 0; i < N - 1; i++) {
            sb.append("<div>\n");
        }
        
        for (int i = 0; i < N - 1; i++) {
            sb.append("</div>\n");
        }
        
        sb.append("</html>");
        return sb.toString();
    }

    public static void main(String[] args) {
        int reps = 100;
        int[] iterador = {10, 11, 12, 13, 14, 15};
        
        for (int i = 0; i < iterador.length; i++) {
            int exp = iterador[i];
            int N = (int) Math.pow(2, exp);
            String xhtmlPrueba = generarXHTMLSintetico(N);
            
            String nombreArchivo = "resultados_N" + N + ".csv";
            Out out = new Out(nombreArchivo);
            out.println("PilaPrinceton, PilaDeCola, ColaPrinceton, ColaDePilas"); 

            for (int r = 0; r < reps; r++) {
                
                // medicion pila princenton
                WebCrawler crawler1 = new WebCrawler(new PilaPrinceton<String>(), new ColaPrinceton<String>());
                StopwatchCPU timer1 = new StopwatchCPU();
                crawler1.esXHTMLValido(xhtmlPrueba);
                double tiempoT1 = timer1.elapsedTime();

                // medicion pila de cola
                WebCrawler crawler2 = new WebCrawler(new PilaDeCola<String>(), new ColaPrinceton<String>());
                StopwatchCPU timer2 = new StopwatchCPU();
                crawler2.esXHTMLValido(xhtmlPrueba);
                double tiempoT2 = timer2.elapsedTime();

                //medicion cola princenton
                Cola<String> colaPrinceton = new ColaPrinceton<String>();
                StopwatchCPU timer3 = new StopwatchCPU();
                WebCrawler.simularCrawlerOffline(N, colaPrinceton);
                double tiempoT3 = timer3.elapsedTime();

                //medicion cola de pilas
                Cola<String> colaDePilas = new ColaDePilas<String>();
                StopwatchCPU timer4 = new StopwatchCPU();
                WebCrawler.simularCrawlerOffline(N, colaDePilas);
                double tiempoT4 = timer4.elapsedTime(); 

                out.println(tiempoT1 + "," + tiempoT2 + "," + tiempoT3 + "," + tiempoT4);
            }
            
            out.close();
        }
    }
}