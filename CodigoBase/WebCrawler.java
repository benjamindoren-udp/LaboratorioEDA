import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebCrawler {

    // Atributos solicitados explícitamente en el PDF
    private Pila<String> pila;
    private Cola<String> cola;

    // Constructor: Recibe las estructuras que ya construiste
    public WebCrawler(Pila<String> pila, Cola<String> cola) {
        this.pila = pila;
        this.cola = cola;
    }

    /**
     * Revisa si el texto de la página cumple las reglas XHTML.
     */
    public boolean esXHTMLValido(String xhtml) {
        // 1. Limpiamos la pila por si quedó sucia de una revisión anterior
        while (!pila.isEmpty()) {
            pila.pop();
        }

        // 2. Regla mandatoria: Incluir <!DOCTYPE>
        if (!xhtml.contains("<!DOCTYPE>")) {
            return false;
        }

        // 3. Regla mandatoria: El atributo xmlns en <html>
        if (!xhtml.contains("<html") || !xhtml.contains("xmlns")) {
            return false;
        }

        // 4. Usamos Expresiones Regulares para extraer las etiquetas.
        // Esto busca "<", un "/" opcional, una palabra (la etiqueta), y cierra con ">"
        Pattern pattern = Pattern.compile("<(/?)(\\w+)[^>]*>");
        Matcher matcher = pattern.matcher(xhtml);

        while (matcher.find()) {
            String esCierre = matcher.group(1); // Si es de cierre, vale "/". Si es apertura, vale "".
            String etiqueta = matcher.group(2); // Ej: "div", "p", "html"

            // Ignoramos el DOCTYPE para no meterlo a la pila
            if (etiqueta.equalsIgnoreCase("DOCTYPE")) continue;

            // Regla: Los elementos deben escribirse en minúsculas
            if (!etiqueta.equals(etiqueta.toLowerCase())) {
                return false; 
            }

            // Regla LIFO con la Pila
            if (esCierre.equals("/")) {
                // Es etiqueta de cierre (ej: </div>). Debe coincidir con la última abierta.
                if (pila.isEmpty()) return false; // Se cerró algo pero no había nada abierto
                
                String tope = pila.pop();
                if (!tope.equals(etiqueta)) return false; // Choque de etiquetas (abrió <p> y cerró </div>)
                
            } else {
                // Es etiqueta de apertura (ej: <div>). Se va a la pila.
                pila.push(etiqueta);
            }
        }

        // Si todo está perfecto, cada apertura tuvo su cierre, y la pila queda vacía.
        return pila.isEmpty();
    }

    /**
     * Recibe una URL, usa la clase In de Princeton para descargar su texto,
     * y la valida con nuestro método de la pila.
     */
    public boolean chequearURL(String url) {
        try {
            // Usamos In (sin import, porque está en tu misma carpeta)
            In in = new In(url);
            if (!in.exists()) return false;
            
            // Leemos todo el código de la página web
            String codigoFuente = in.readAll(); 
            
            // Pasamos el código fuente a nuestro validador
            return esXHTMLValido(codigoFuente);
            
        } catch (IllegalArgumentException e) {
            // Si la URL está rota o no se puede conectar, la damos por inválida
            return false;
        }
    }
    
    // TODO: Falta el método chequeaURLs (El algoritmo de Búsqueda BFS)

    /**
     * Navega por internet partiendo de una URL, usando una Cola (BFS).
     */
    public boolean chequeaURLs(String urlInicial, int limite) {
        // SET de Princeton para recordar las URLs ya visitadas
        SET<String> visitadas = new SET<String>();
        
        // Empezamos metiendo la primera página a la cola
        cola.enqueue(urlInicial);
        visitadas.add(urlInicial);

        int paginasRevisadas = 0;
        boolean todasValidas = true; // Asumimos que todo está bien hasta que se demuestre lo contrario

        // Ciclo BFS: Mientras la cola tenga páginas y no nos pasemos del límite
        while (!cola.isEmpty() && paginasRevisadas < limite) {
            
            // 1. Sacamos la siguiente URL de la cola para visitarla
            String urlActual = cola.dequeue();
            paginasRevisadas++;
            
            // 2. Validamos el XHTML de esta página
            boolean esValido = chequearURL(urlActual);
            if (!esValido) {
                todasValidas = false; // Si una sola falla, el resultado final será falso
            }

            // 3. Extraemos nuevos links de esta página para seguir navegando
            try {
                In in = new In(urlActual);
                if (in.exists()) {
                    String contenido = in.readAll();
                    
                    // Regex para buscar <a href="ALGUNA_URL">
                    Pattern pattern = Pattern.compile("<a[^>]*href=[\"']([^\"']+)[\"'][^>]*>");
                    Matcher matcher = pattern.matcher(contenido);

                    while (matcher.find()) {
                        String nuevoLink = matcher.group(1);
                        
                        // Si es un link válido (empieza con http) y no lo hemos visitado
                        if (nuevoLink.startsWith("http") && !visitadas.contains(nuevoLink)) {
                            cola.enqueue(nuevoLink); // Lo metemos a la agenda
                            visitadas.add(nuevoLink); // Lo anotamos en el cuaderno de visitados
                        }
                    }
                }
            } catch (Exception e) {
                // Ignorar errores de lectura de la página y seguir con la siguiente
            }
        }

        return todasValidas; // Retornamos si todas las que revisamos estaban perfectas
    }

    
    // TODO: Falta el public static void main
}