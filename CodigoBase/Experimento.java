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
        int reps = 50000;
        int iterador[] = {10,11,12,13,14,15};
        
        // Utilizamos la clase Out de Princeton para crear el archivo CSV
        Out out = new Out("resultados_pilas.csv");
        out.println("N,Estructura,Tiempo(s)"); // Encabezados del CSV

        // Iteramos sobre cada tamaño N
        for (int exp : iterador) {
            int N = (int) Math.pow(2, exp);
            System.out.println("Midiendo para N = " + N + " (" + reps + " repeticiones)");

            // REGLA DE ORO: Generar los datos sintéticos AFUERA de la medición de tiempo
            String xhtmlPrueba = generarXHTMLSintetico(N);

            // ========================================================
            // MEDICIÓN 1: Usando PilaPrinceton
            // ========================================================
            for (int r = 0; r < reps; r++) {
                // Preparamos el entorno antes de medir
                Pila<String> pilaPrinceton = new PilaPrinceton<String>();
                Cola<String> colaFicticia = new ColaPrinceton<String>(); // Solo para que el constructor compile
                WebCrawler crawler = new WebCrawler(pilaPrinceton, colaFicticia);

                // INICIO DE MEDICIÓN
                StopwatchCPU timer1 = new StopwatchCPU();
                
                crawler.esXHTMLValido(xhtmlPrueba); // Ejecutamos el método a medir
                
                double tiempo1 = timer1.elapsedTime();
                // FIN DE MEDICIÓN

                // Guardamos el resultado en el CSV
                out.println(N + ",PilaPrinceton," + tiempo1);
            }

            // ========================================================
            // MEDICIÓN 2: Usando tu PilaConFila (Si ya la tienes hecha)
            // ========================================================
            
            
            for (int r = 0; r < reps; r++) {
                Pila<String> pilaDeCola = new PilaDeCola<String>();
                Cola<String> cola = new ColaPrinceton<String>();
                WebCrawler crawler2 = new WebCrawler(pilaDeCola, cola);

                StopwatchCPU timer2 = new StopwatchCPU();
                crawler2.esXHTMLValido(xhtmlPrueba);
                double tiempo2 = timer2.elapsedTime();

                out.println(N + ",PilaDeCola," + tiempo2);
            }
            
        }

        // Importante cerrar el archivo al terminar
        out.close();
        System.out.println("¡Experimento finalizado! Revisa el archivo 'resultados_exp1_pilas.csv'");
    }
}