/**
 * Interfaz genérica de una cola FIFO.
 * @param <T> el tipo de elemento que almacenará la cola.
 */
public interface Cola<T> {
    
    /**
     * Inserta un item al final de la cola.
     * @param item el elemento a insertar.
     */
    void enqueue(T item);

    /**
     * Elimina el item al inicio de la cola y lo retorna.
     * @return el item del inicio, o nulo si la cola está vacía
     */
    T dequeue();

    /**
     * Retorna, pero no elimina, el item del inicio de la cola.
     * @return el elemento del inicio de la cola, o null si está vacía
     */
    T peek();

    /**
     * Retorna el número de elementos en la cola.
     * @return el tamaño de la cola.
     */
    int size();

    /**
     * Revisa si la cola está vacía.
     * @return true si no hay elementos, false en caso contrario
     */
    boolean isEmpty();

}

