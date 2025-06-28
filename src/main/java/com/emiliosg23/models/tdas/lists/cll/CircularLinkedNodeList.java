package com.emiliosg23.models.tdas.lists.cll;

public class CircularLinkedNodeList<E> {
    private E content;
    private CircularLinkedNodeList<E> next;
    
    public CircularLinkedNodeList(E e){
        this.content=e;
        next=null;
    }

    public E getContent() {
        return content;
    }

    public void setContent(E content) {
        this.content = content;
    }

    public CircularLinkedNodeList<E> getNext() {
        return next;
    }

    public void setNext(CircularLinkedNodeList<E> next) {
        this.next = next;
    }
    
}
