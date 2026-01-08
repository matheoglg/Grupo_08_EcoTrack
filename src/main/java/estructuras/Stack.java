/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estructuras;

import java.util.Iterator;

/**
 *
 * @author User
 * @param <E>
 */
public class Stack<E> implements Iterable<E>{

    private LinkedList<E> list = new LinkedList<>();
    
    public void push(E e) {
        list.addFirst(e);   
    }

    public E pop() {
        return list.removeFirst();  
    }

    public E peek() {
        return list.get(0);   
    }

    public boolean isEmpty() {
        return list.isEmpty(); 
    }

    public int size() {
        return list.size();
    }
    @Override
    public Iterator<E> iterator() {
        return list.iterator();
    }

}

