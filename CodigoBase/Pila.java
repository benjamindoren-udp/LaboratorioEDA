/**
 * Interfaz genérica de una pila.
 * @param <T> el tipo de elemento que contendrá la pila
 */
public interface Pila<T> {
    
    /**
     * Insertar un elemento en el tope del stack.
     * @param item el item para insertar
     */
    void push(T item);

    /**
     * Elimina el objeto en el tope de la pila y lo entrega.
     * @return el elemento en el tope, o nulo si está vacío.
     */
    T pop();

    /**
     * Revisa el elemento en el tope de la pila, sin eliminarlo.
     * @return el objeto en el tope, sin eliminarlo
     */
    T peek();

    /**
     * Revisa si la pila está vacía.
     * @return true si la pila no contiene elementos, false en caso contrario.
     */
    boolean isEmpty();

    /**
     * La cantidad de elementos que hay en la pila.
     * @return tamaño de la pila.
     */
    int size();
}
