import java.util.NoSuchElementException;

public class PilaDeCola<T> implements Pila<T> {

    //cola para LIFO
    private Queue<T> cola = new Queue<>();

    @Override
    public void push(T item) {
        int aux = cola.size(); //se usa para ordenar despues

        // se agrega un elemento
        cola.enqueue(item);

        // muevo los anteriores al final para que el nuevo quede al frente
        for (int i = 0; i < aux; i++) {
            cola.enqueue(cola.dequeue());
        }
    }

    @Override
    public T pop() {
        if (isEmpty()) {
            throw new NoSuchElementException("la pila esta vacia");
        }

        return cola.dequeue();
    }

    @Override
    public T peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("no hay nada en la pila");
        }

        return cola.peek();
    }

    @Override
    public boolean isEmpty() {
        return cola.isEmpty();
    }

    @Override
    public int size() {
        return cola.size();
    }
}
