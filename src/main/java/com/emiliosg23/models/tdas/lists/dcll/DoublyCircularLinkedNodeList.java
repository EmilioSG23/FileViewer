package com.emiliosg23.models.tdas.lists.dcll;

public class DoublyCircularLinkedNodeList<E> {
    private E content;
    private DoublyCircularLinkedNodeList<E> next;
    private DoublyCircularLinkedNodeList<E> previous;
    
    public DoublyCircularLinkedNodeList(E e){
        this.content=e;
        next=null;
        previous=null;
    }

    public E getContent() {
        return content;
    }

    public void setContent(E content) {
        this.content = content;
    }

    public DoublyCircularLinkedNodeList<E> getNext() {
        return next;
    }

    public void setNext(DoublyCircularLinkedNodeList<E> next) {
        this.next = next;
    }

    public DoublyCircularLinkedNodeList<E> getPrevious() {
        return previous;
    }

    public void setPrevious(DoublyCircularLinkedNodeList<E> previous) {
        this.previous = previous;
    }
}
