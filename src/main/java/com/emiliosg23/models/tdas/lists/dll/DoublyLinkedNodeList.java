package com.emiliosg23.models.tdas.lists.dll;

public class DoublyLinkedNodeList<E> {
    private E content;
    private DoublyLinkedNodeList<E> previous;
    private DoublyLinkedNodeList<E> next;
    
    public DoublyLinkedNodeList(E elem){
        this.content=elem;
        previous=null;
        next=null;
    }

    public E getContent() {
        return content;
    }

    public void setContent(E content) {
        this.content = content;
    }

    public DoublyLinkedNodeList<E> getPrevious() {
        return previous;
    }

    public void setPrevious(DoublyLinkedNodeList<E> previous) {
        this.previous = previous;
    }

    public DoublyLinkedNodeList<E> getNext() {
        return next;
    }

    public void setNext(DoublyLinkedNodeList<E> next) {
        this.next = next;
    }
    
}
