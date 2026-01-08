/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estructuras;

/**
 *
 * @author Hogar
 */
public class DoublyNodeList<E> {
    private E content;
    private DoublyNodeList<E> next;
    private DoublyNodeList<E> previous;

    public DoublyNodeList(E contenido) {
        this.content = contenido;
        this.next = null;
        this.previous = null;
    }

    public E getContenido() {
        return content;
    }

    public void setContenido(E contenido) {
        this.content = contenido;
    }

    public DoublyNodeList<E> getNext() {
        return next;
    }

    public void setNext(DoublyNodeList<E> next) {
        this.next = next;
    }

    public DoublyNodeList<E> getAnterior() {
        return previous;
    }

    public void setAnterior(DoublyNodeList<E> anterior) {
        this.previous = anterior;
    }
}
