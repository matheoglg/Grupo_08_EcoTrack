/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estructuras;

import java.util.Iterator;
import java.util.NoSuchElementException;


public class DoublyCircularLinkedList<E> implements List<E>{
    private DoublyNodeList<E> head;
    private DoublyNodeList<E> last;

    public DoublyCircularLinkedList() {
        this.head = null;
        this.last = null;
    }
    
    // Getters y setters
    public DoublyNodeList<E> getHead() {
        return head;
    }

    public void setHead(DoublyNodeList<E> head) {
        this.head = head;
    }

    public DoublyNodeList<E> getLast() {
        return last;
    }

    public void setLast(DoublyNodeList<E> last) {
        this.last = last;
    }
    
    // Metodos de la lista circular doblemente enlazada
    
//metodo 1: devuelve el número total de elementos
    @Override
    public int size() {
        if (isEmpty()) return 0;
        int cont = 0;
        //se detiene al llegar al último
        for (DoublyNodeList<E> current = head; current != last; current = current.getNext()) {
            cont++;}
        return cont + 1; //suma el ultimo que se saltó
    }

    //metodo 2: retorna true si la lista no contiene elementos
    @Override
    public boolean isEmpty() {
        return head == null; //para evitar recursion con size
    }

    //metodo 3: elimina todos los nodos de la lista
    @Override
    public void clear() {
        head = null;
        last = null;
    }

    //metodo 4: devuelve el contenido del nodo en la posición indicada
    @Override
    public E get(int index) {
        if (index < 0 || index >= size()) throw new IndexOutOfBoundsException();
        DoublyNodeList<E> current = head;
        for (int i = 0; i < index; i++) {
            current = current.getNext();}
        return current.getContent();
    }

    //metodo 5: reemplaza el contenido y devuelve el anterior
    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= size()) throw new IndexOutOfBoundsException();
        DoublyNodeList<E> current = head;
        for (int i = 0; i < index; i++) {
            current = current.getNext();}
        E old = current.getContent();
        current.setContent(element);
        return old;
    }

    //metodo 6: agrega un nuevo nodo al inicio de la lista
    @Override
    public boolean addFirst(E e) {
        if (e == null) throw new IllegalArgumentException();
        DoublyNodeList<E> newN = new DoublyNodeList<>(e);
        if (isEmpty()) {
            last = newN;
            head = newN; //inicializa head
            newN.setNext(newN);
            newN.setPrevious(newN);
            return true;
        }
        newN.setNext(head);
        newN.setPrevious(last);
        head.setPrevious(newN);
        last.setNext(newN);
        head = newN; //nuevo nodo, nuevo head
        return true;
    }

    //metodo 7: agrega un nuevo nodo al final
    @Override
    public boolean addLast(E e) {
        if (e == null) throw new IllegalArgumentException();
        DoublyNodeList<E> newN = new DoublyNodeList<>(e);
        if (isEmpty()) {
            last = newN;
            head = newN;
            newN.setNext(newN);
            newN.setPrevious(newN);
            return true;
        }
        newN.setPrevious(last);
        newN.setNext(head);
        last.setNext(newN);
        head.setPrevious(newN);
        last = newN;
        return true;
    }

    //metodo 8: agrega un elemento en una posición específica
    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size()) throw new IndexOutOfBoundsException();
        if (index == 0) { addFirst(element); return; }
        if (index == size()) { addLast(element); return; }
        DoublyNodeList<E> current = head;
        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }
        DoublyNodeList<E> newN = new DoublyNodeList<>(element);
        newN.setNext(current);
        newN.setPrevious(current.getPrevious());
        current.getPrevious().setNext(newN);
        current.setPrevious(newN);
    }

    //metodo 9: elimina el primer nodo y retorna su contenido
    @Override
    public E removeFirst() {
        if (isEmpty()) throw new NoSuchElementException();
        E old = head.getContent();
        if (size() == 1) {
            last = null;
            head = null;
            return old;}
        last.setNext(head.getNext());
        head.getNext().setPrevious(last);
        head = head.getNext(); //actualiza el inicio
        return old;
    }

    //metodo 10: elimina el último nodo y retorna su contenido
    @Override
    public E removeLast() {
        if (isEmpty()) throw new NoSuchElementException();
        E old = last.getContent();
        if (size() == 1) {
            last = null;
            head = null;
            return old;}
        head.setPrevious(last.getPrevious());
        last.getPrevious().setNext(head);
        last = last.getPrevious(); //mueve el puntero last atras
        return old;
    }

    //metodo 11: elimina el nodo en la posición index y devuelve el contenido
    @Override
    public E remove(int index) {
        if (index < 0 || index >= size()) throw new IndexOutOfBoundsException();
        if (index == 0) return removeFirst();
        if (index == size() - 1) return removeLast();
        DoublyNodeList<E> current = head;
        for (int i = 0; i < index; i++) {
            current = current.getNext();}
        E old = current.getContent();
        current.getPrevious().setNext(current.getNext());
        current.getNext().setPrevious(current.getPrevious());
        return old;
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException("no implementado aún");
    }
}