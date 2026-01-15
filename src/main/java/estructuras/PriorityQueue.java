/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estructuras;


import java.util.Comparator;
import java.util.Iterator;
/**
 *
 * @author USER
 */
public class PriorityQueue<E> {
    private LinkedList<E> list;
    private Comparator<E> comparator;

    public PriorityQueue(Comparator<E> comparator) {
        this.list = new LinkedList<>();
        this.comparator = comparator;
    }
    
    public PriorityQueue() {
    this.list = new LinkedList<>();
    this.comparator = (a, b) -> ((Comparable<E>) a).compareTo(b);
}

    public void enqueue(E e) {
        if (e == null) return; //caso base, si el elemento es nulo
        if (list.isEmpty()) { //caso base, lista vacia
            list.addLast(e);
            return;
        }
        Iterator<E> it = list.iterator();
        int index = 0;
        while (it.hasNext()) { //recorremos la lista con iterador
            E actual = it.next();
            //si nuevo elemento tiene + prioridad que el actual
            if (comparator.compare(e, actual) > 0) {
                list.add(index, e); 
                return; 
            }
            index++;
        }
        list.addLast(e); //si llegó aquí, e tiene prioridad + baja
    }

    public E dequeue() {
        return list.removeFirst();
    }

    public E peek() {
        return list.get(0);
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }    
}
