import java.util.NoSuchElementException;

public class ColaDePilas<T> implements Cola<T> {

    private Stack<T> pilaEntrada = new Stack<>();
    private Stack<T> pilaSalida = new Stack<>();

    @Override
    public void enqueue(T item) {
        pilaEntrada.push(item);
    }

    @Override
    public T dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("la cola esta vacia");
        }
        voltearDatos();
        return pilaSalida.pop();
    }

    @Override
    public T peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("no hay elementos");
        }
        voltearDatos();
        return pilaSalida.peek();
    }

    @Override
    public boolean isEmpty() {
        return pilaEntrada.isEmpty() && pilaSalida.isEmpty();
    }

    @Override
    public int size() {
        return pilaEntrada.size() + pilaSalida.size();
    }

    // se invierte el orden
    private void voltearDatos() {
        if (pilaSalida.isEmpty()) {
            while (!pilaEntrada.isEmpty()) {
                pilaSalida.push(pilaEntrada.pop());
            }
        }
    }
}