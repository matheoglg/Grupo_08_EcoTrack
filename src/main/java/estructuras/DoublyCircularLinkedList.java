/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estructuras;

/**
 *
 * @author Hogar
 */
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
    
    
    
    
}
