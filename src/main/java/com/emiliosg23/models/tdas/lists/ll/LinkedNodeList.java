package com.emiliosg23.models.tdas.lists.ll;

import java.io.Serializable;

public class LinkedNodeList<E> implements Serializable{
    private E content;
    private LinkedNodeList<E> next;
    
    public LinkedNodeList(E content){
        this.content=content;
        next=null;
    }

    public E getContent() {
        return content;
    }

    public void setContent(E content) {
        this.content = content;
    }

    public LinkedNodeList<E> getNext() {
        return next;
    }

    public void setNext(LinkedNodeList<E> next) {
        this.next = next;
    }
    
    
}
