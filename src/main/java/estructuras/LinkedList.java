package estructuras;

import java.util.Iterator;

/**
 *
 * @author User
 */

public class LinkedList<E> implements List<E>{
    private NodeList<E> header;
    private NodeList<E> last;
    //private int size = 0;
    
    public LinkedList(){
        this.header=null;
        this.last=null;
    }
    public NodeList<E> getHeader() {
        return header;
    }

    public void setHeader(NodeList<E> header) {
        this.header = header;
    }

    public NodeList<E> getLast() {
        return last;
    }

    public void setLast(NodeList<E> last) {
        this.last = last;
    }

    @Override
    public boolean addFirst(E e) { //dado un elemento generico
        //size++;
        if(e!=null) {
            NodeList<E> newNode=new NodeList<>(e); // constructor crea un nodo aislado
            newNode.setNext(header); //El siguiente de ese nuevo nodo es Header
            this.setHeader(newNode); //actualizar el header de la lista
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean addLast(E e) {
        //size++;
        if(e!=null){
            NodeList<E> newNode = new NodeList<>(e);
            if(last !=null){
                last.setNext(newNode);
            }
            last=newNode;
            
            if(header == null){
                header = newNode;
            }
            return true;
        } else {
            return false;
        }  
    }
    
    private NodeList<E> getPrevious(NodeList<E> node){
        NodeList<E> previous = null;
        NodeList<E> n;
        
        for(n = header; n!=node; n= n.getNext()){
            previous = n;
        }
        return previous;
    }
    
    private void recorrerHaciAtras(){
        NodeList<E> n;
        
        for (n = last; n != header; n = this.getPrevious(n)){
            System.out.println(n);
        }
    }
    
    @Override
    public E removeFirst() {
        if (isEmpty()) {return null;}

        E value = header.getContent();
        header = header.getNext();

        if (header == null) {                       // Si solo habia un nodo, ahora last tambien es null
            last = null;
        }

        return value;
    }

    @Override
    public E removeLast() {
        if (isEmpty()) {return null;}

        if (header == last) {                       // Si solo hay un nodo en la lista
            E value = header.getContent();
            header = null;                          // Se elimina
            last = null;
            return value;
        }

        // Cuando existe mas de un nodo
        NodeList<E> current = header;
        while (current.getNext() != last) {         // Mientras el nodo siguiente del actual no sea el ultimo
            current = current.getNext();            // Avanza de nodo en nodo hasta llegar al penultimo
        }

        E value = last.getContent();
        current.setNext(null);                      // Elimina conexion con el ultimo
        last = current;                             // El penultimo ahora va a ser el ultimo

        return value;

    }

    @Override
    public int size() {
        // return size; // O(1)
        // primera parte declaro variables a iterar
        // segunda parte condicion logica
        // tercera parte instrucciones
        //for (int i=0, j=0; i < 10 && j >8; i++){
        //for (NodeList<E> n = header; n ! = null ; n = n.getNext()){
        
        // Complejidad lineal O(n) a pesar de que no es tan buena como O(1) evita que tenga que darle mantenimiento a size
        int size = 0;
        NodeList<E> n; //declaro un nodo viajero
        //declaro un nodo viajero, mientras sea diferente de null, se mueve al siguiente nodo
        
        for (n = header ; n!= null ; n = n.getNext( )) {
            size++;
        }
        return size;
    }

    @Override
    public boolean isEmpty() {
        return header == null;
    }

    @Override
    public void clear() {
        header = null;
        last = null;
    }

    @Override
    public void add(int index, E element) {
        if (element == null) {return;}                 // No permite nulls en el nodo
        if (index < 0 || index > size()) {return;}     // Indices fuera del rango de la lista

        NodeList<E> nuevo = new NodeList<>(element);

        // Inserciones al inicio de la lista
        if (index == 0) {
            nuevo.setNext(header);                      // nuevo.next = header
            header = nuevo;
            if (last == null) {last = nuevo;}           // En caso de que la lista este vacia, el nuevo elemento tambien es el ultimo
            return;
        }

        // Inserciones al inicio en medio o al final
        NodeList<E> prev = header;
        for (int i = 0; i < index - 1; i++) {
            prev = prev.getNext();                      // Avanza hasta el nodo anterior al indice
        }

        // Insertar después de prev
        nuevo.setNext(prev.getNext());                  // nuevo.next = prev.next
        prev.setNext(nuevo);                            // prev.next = nuevo

        if (nuevo.getNext() == null)                    // Si esta al final, se actualiza el valor de last
            last = nuevo;
        }

    @Override
    public E remove(int index) {
        if (index < 0 || index >= size() || isEmpty()) {return null;} // Si el indice no es valido o si esta vacia
        
        NodeList<E> n;                                                // Nodo a remover
        
        // Si el nodo a remover es el primero
        if (index == 0) {
            n = header;
            header = header.getNext();                                // Se desconecta al Nodo n de la lista, removiendolo
            if (isEmpty()) {last = null;}                             // Si la lista queda vacia, su last y next son null
            return n.getContent();
        }
        
        // Si se elimina del medio o del final
        NodeList<E> prev = header;
        for (int i = 0; i < index - 1; i++) {
            prev = prev.getNext();
        }
        n = prev.getNext();                                           // n = nodo del indice dado
        prev.setNext(n.getNext());                                    // prev.next = n.next, eliminando asi el nodo n

        if (n == last) {last = prev;}                                 // Si era el ultimo, ahora el ultimo es el anterior

        return n.getContent();
        
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= size()) return null;

        NodeList<E> n = header;
        for (int i = 0; i < index; i++) {
            n = n.getNext();
        }

        return n.getContent();
    }

    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= size() || element == null) return null;   // Indice fuera de limites o elemento vacio

        NodeList<E> n = header;
        for (int i = 0; i < index; i++) {
            n = n.getNext();
        }

        E contentOld = n.getContent();
        n.setContent(element);
        return contentOld;
    }
    
    public String toString() {
        String s="";
        for (NodeList<E> n=header; n!=null; n=n.getNext()) {
            s+=n.getContent()+", ";
        }
        return s;
    }
    
    public Iterator<E> iterator(){
        Iterator<E> it=new Iterator<E>() {
                NodeList<E> cursor = header;
                
            @Override
            public boolean hasNext() {
                return cursor != null;
            }

            @Override
            public E next() {
                E e = cursor.getContent();
                cursor = cursor.getNext();
                return e;
            }
        };
        
        
        return it;
    }
    
    
    // Metodo 1
    public void alternateRemove(int k){
        if(isEmpty() || k <= 0) {return;}                       // Si no hay nodos o si el indice no se puede remover
        
        int indice = 1;                                         // Se inicializa el indice del primer nodo
        NodeList<E> actual = header;                            // Nodos indice
        NodeList<E> prev = null;
        
        while(actual != null){                                  // Mientras siga en un nodo
            if(indice % k == 0){                                // Si es multiplo del parametro k se procede a eliminar el actual
                if(prev != null){                               // Si el nodo anterior no es null
                    prev.setNext(actual.getNext());             // prev.next = actual.next, se elimina el k-esimo nodo
                }else{
                    header = actual.getNext();                  // header = actual.next, se elimina el primero
                }
                if(actual == last){                             // Si el eliminado es el ultimo nodo
                    last = prev;                                // El anterior sera ahora el ultimo
                }
            }else{
                prev = actual;                                  // Si no es k-esimo el actual pasa a ser el anterior
            }
            actual = actual.getNext();                          // El siguiente nodo sera ahora el actual
            indice++;
        }
    }
    
    // Metodo 2
    public LinkedList<E> getEveryNth(int n){
        LinkedList<E> lecturas = new LinkedList<>();            // Lista a retornar
        if(isEmpty() || n <= 0) {return lecturas;}              // Si no hay nodos o indice no se puede obtener retorna la lista vacia
        
        int indice = 1;                                         // Se inicializa el indice del primer nodo
        NodeList<E> actual = header;                            // Nodo header para iterar
        
        while(actual != null){                                  // Mientras no se llegue al final
            if(indice % n == 0){                                // Si es multiplo
                lecturas.addLast(actual.getContent());          // Se agrega el elemento de este indice a la lista
            } 
            actual = actual.getNext();                          // Se actualiza al siguiente nodo
            indice++;
        }
        return lecturas;
    }
    
    // Metodo 3
    public void shiftLeft(int n){
        int len = size();                                       // Tamano de la linkedlist
        if (isEmpty() || n <= 0 || len <= 1) {return;}          // Validacion lista vacia, parametro negativo y si solo hay un nodo

        n = n % len;                                            // Normalizacion para evitar iteraciones innecesarias
        if (n == 0) {return;}                                   // Si al normalizar es 0 se retorna (no se mueven)
        
        NodeList<E> actual = header;
        for(int i = 1; i < n; i++){                             // Se busca el nodo que estara en posicion last
            actual = actual.getNext();
        }
        
        NodeList<E> headerN = actual.getNext();                 // El nodo next despues del obtenido sera el nuevo header
        if(headerN == null){return;}                            // Cuando n = tamano
        
        actual.setNext(null);                                   // Se actualiza el next del nuevo last a null
        last.setNext(header);                                   // El next del last anterior sera el header
        header = headerN;                                       // Se actualizan las conexiones
        last = actual;                                          // Como dijimos, el ultimo en moverse sera el last
    }
    
    // Metodo 4
    public E findMiddle(){ // Asumiendo tamano impar
        if(isEmpty()) {return null;}                            // Si la lista esta vacia, retorna null
        
        NodeList<E> uno = header;                               // Se crean los jumpers
        NodeList<E> dos = header;
        
        while(dos != null && dos.getNext() != null){            // Mientras el jumper de dos y su next no sean null(Osea el next del ultimo)
            uno = uno.getNext();                                // Avanza 1 nodo
            dos = dos.getNext().getNext();                      // Avanza 2 nodos
        }
        
        return uno.getContent();                                // El elemento del nodo de en medio
    }
    
    // Metodo 5
    public void swapEnds(){
        if(isEmpty() || size() == 1) {return;}                  // Lista vacia o de un solo elemento
        
        E temp = header.getContent();                           // Elemento del header
        header.setContent(last.getContent());                   // Se cambia el elemento del header al del last
        last.setContent(temp);                                  // El elemento del last toma el valor del header, completando el intercambio
    }
    
    // Metodo 6
    public LinkedList<E> filterByPrefix(String prefix) {          // Se asume que los elementos son strings
        LinkedList<E> filtradas = new LinkedList<>();             // Creacion lista
        if(isEmpty() || prefix == null) {return filtradas;}       // Si esta vacia o el parametro es null, retorna la lista vacia
       
        NodeList<E> actual = header;
        
        while(actual != null){                                    // Llega hasta el ultimo nodo
            E elemento = actual.getContent();                     // Saca el content del nodo actual
            if(elemento instanceof String){                       // Verifica si es String(Opcional porque el metodo decia que se asume)
                String nombre = (String) elemento;                // Se hace cast del elemento
                if(nombre.startsWith(prefix)){                    // Si empieza con el prefijo se agrega a la lista como ultimo nodo
                    filtradas.addLast(elemento);
                }
            }
            actual = actual.getNext();                            // Avanza al siguiente nodo
        }
        return filtradas;
    }
    
    // Metodo 7
    public void interleave(LinkedList<E> other) {
        if (other == null || other.isEmpty()) {return;}           // Si la otra lista esta vacia no hace nada
        
        if (this.isEmpty()) {                                          // Si la lista que tenemos esta vacia, se convierte en la otra
            this.header = other.header;                                // Se actualizan conexiones
            this.last = other.last;
            return;
        }

        NodeList<E> actual1 = this.header;                        // Header de nuestra lista
        NodeList<E> actual2 = other.header;                       // Header de la lista parametro
        

        while (actual1 != null && actual2 != null) {              // Mientras no se llegue al final
            NodeList<E> next1 = actual1.getNext();                // Next de cada nodo actual
            NodeList<E> next2 = actual2.getNext();

            actual1.setNext(actual2);                             // Se inserta el nodo de la lista other entre el
            
            if (next1 != null) {                                  // nodo actual y next de nuestra lista
                actual2.setNext(next1);
            }                                                     
            if (next1 == null) {
                this.last = actual2;
            }
            actual1 = next1;                                      // Se avanza al siguiente nodo de ambas listas
            actual2 = next2;
            
        }
        if (actual2 != null) {
            this.last.setNext(actual2);                             // Actualizamos last hasta el último nodo
            
            NodeList<E> temp = actual2;                             // Nodo temporal con valor de actual2
            while (temp.getNext() != null) {                        // Mientras hata nodos en other
                temp = temp.getNext();                              // Avanza hasta ser el ultimo
            }
            this.last = temp;                                       // Actualiza last
        }
        
    }  
}