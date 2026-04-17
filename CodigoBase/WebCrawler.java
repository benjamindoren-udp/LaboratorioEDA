import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebCrawler {

    private Pila<String> pila;
    private Cola<String> cola;

    
    public WebCrawler(Pila<String> pila, Cola<String> cola) {
        this.pila = pila;
        this.cola = cola;
    }

    public boolean esXHTMLValido(String xhtml) {
        
        while (!pila.isEmpty()) {
            pila.pop();
        }
        
        if (!xhtml.contains("<!DOCTYPE>")) {
            return false;
        }

        if (!xhtml.contains("<html") || !xhtml.contains("xmlns")) {
            return false;
        }

        Pattern pattern = Pattern.compile("<(/?)(\\w+)[^>]*>");
        Matcher matcher = pattern.matcher(xhtml);

        while (matcher.find()) {
            String esCierre = matcher.group(1); 
            String etiqueta = matcher.group(2); 

            
            if (etiqueta.equalsIgnoreCase("DOCTYPE")) continue;

            
            if (!etiqueta.equals(etiqueta.toLowerCase())) {
                return false; 
            }
            
            if (esCierre.equals("/")) {
            
                if (pila.isEmpty()) return false; 
                
                String tope = pila.pop();
                if (!tope.equals(etiqueta)) return false; 
                
            } else {
                
                pila.push(etiqueta);
            }
        }
        
        return pila.isEmpty();
    }
    
    public boolean chequearURL(String url) {
        try {
            In in = new In(url);
            if (!in.exists()) return false;
            
            
            String codigoFuente = in.readAll(); 
            
            
            return esXHTMLValido(codigoFuente);
            
        } catch (IllegalArgumentException e) {
            
            return false;
        }
    }
    
    public boolean chequeaURLs(String urlInicial, int limite) {
        
        SET<String> visitadas = new SET<String>();
        
        
        cola.enqueue(urlInicial);
        visitadas.add(urlInicial);
        int paginasRevisadas = 0;
        boolean todasValidas = true; 
        
        while (!cola.isEmpty() && paginasRevisadas < limite) {
            
            
            String urlActual = cola.dequeue();
            paginasRevisadas++;
            
            
            boolean esValido = chequearURL(urlActual);
            if (!esValido) {
                todasValidas = false; 
            }
            
            try {
                In in = new In(urlActual);
                if (in.exists()) {
                    String contenido = in.readAll();
                    
                    
                    Pattern pattern = Pattern.compile("<a[^>]*href=[\"']([^\"']+)[\"'][^>]*>");
                    Matcher matcher = pattern.matcher(contenido);
                    while (matcher.find()) {
                        String nuevoLink = matcher.group(1);
                        
                        
                        if (nuevoLink.startsWith("http") && !visitadas.contains(nuevoLink)) {
                            cola.enqueue(nuevoLink); 
                            visitadas.add(nuevoLink); 
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("error con la url " + e.getMessage());
            }
        }
        return todasValidas; 
    }

public static void simularCrawlerOffline ( int N , Cola < String > fila ) {
    fila . enqueue ( " url_raiz " ) ;
    for ( int i = 0; i < N ; i ++) {
        if (! fila . isEmpty () ) fila . dequeue () ;
        // Simulamos que encontramos 2 links por pagina
        fila . enqueue ( " link1_ " + i ) ;
        fila . enqueue ( " link2_ " + i ) ;
    }
}
    public static void main(String[] args) {
        Pila<String> pila = new PilaPrinceton<String>();
        Cola<String> cola = new ColaPrinceton<String>();
        WebCrawler crawler = new WebCrawler(pila, cola);

        String urlInicial = "Crawler Test Site.html"; 
        int limite = 200; 

        boolean resultado = crawler.chequeaURLs(urlInicial, limite);
        System.out.println("las paginas validas son: " + resultado);
    }
    
}