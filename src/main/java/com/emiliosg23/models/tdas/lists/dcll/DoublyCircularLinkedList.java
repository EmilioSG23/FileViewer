package com.emiliosg23.models.tdas.lists.dcll;


import java.util.Comparator;
import java.util.Iterator;

import com.emiliosg23.models.tdas.lists.List;

public class DoublyCircularLinkedList<E> implements List<E>{
    private DoublyCircularLinkedNodeList<E> last;
    private int effectiveSize;
    
    public DoublyCircularLinkedList(){
        last=null;
        effectiveSize=0;
    }

    public DoublyCircularLinkedNodeList<E> getLast() {
        return last;
    }

    public void setLast(DoublyCircularLinkedNodeList<E> last) {
        this.last = last;
    }
    
		@Override
    public int size(){
        return effectiveSize;
    }
    
		@Override
    public boolean isEmpty(){
        return effectiveSize==0;
    }
    
		@Override
    public void clear(){
        if(!isEmpty()){
            DoublyCircularLinkedNodeList<E> temp= getLast().getNext();
            while(temp!=null){
                DoublyCircularLinkedNodeList<E> nextTemp=temp.getNext();
                temp.setContent(null);
                temp.setNext(null);
                temp.setPrevious(null);
                temp=nextTemp;
            }
            setLast(null);
            effectiveSize=0;
        }
    }
    
		@Override
    public int indexOf(E e){
        if(verifyElement(e)){
            DoublyCircularLinkedNodeList<E> temp=getLast().getNext();
            int i=0;
            do{
                if(temp.getContent().equals(e))
                    return i;
                i++;
                temp=temp.getNext();
            }while(temp!=getLast().getNext());
        }
        return -1;
    }
    
		@Override
    public boolean contains(E e){
        if(verifyElement(e)){
            DoublyCircularLinkedNodeList<E> temp=getLast().getNext();
            do{
                if(temp.getContent().equals(e))
                    return true;
                temp=temp.getNext();
            }while(temp!=getLast().getNext());
        }
        return false;
    }
    
		@Override
    public boolean add(int index, E e){
        if(verifyInsertion(index, e)){
            DoublyCircularLinkedNodeList<E> newNode=new DoublyCircularLinkedNodeList<>(e);
            if(isEmpty()){
                setLast(newNode);
                getLast().setNext(newNode);
                effectiveSize++;
                return true;
            }
            if(index==0){
                newNode.setNext(getLast().getNext());
                getLast().getNext().setPrevious(newNode);
                newNode.setPrevious(getLast());
                getLast().setNext(newNode);
                effectiveSize++;
                return true;
            }
            if(index==size()){
                newNode.setNext(getLast().getNext());
                getLast().getNext().setPrevious(newNode);
                newNode.setPrevious(getLast());
                getLast().setNext(newNode);
                setLast(newNode);
                effectiveSize++;
                return true;
            }
            DoublyCircularLinkedNodeList<E> temp=getLast().getNext();
            int count=0;
            while(index>count){
                temp=temp.getNext();
                count++;
            }
            newNode.setPrevious(temp.getPrevious());
            temp.getPrevious().setNext(newNode);
            newNode.setNext(temp);
            temp.setPrevious(newNode); 
            effectiveSize++;
            return true;
        }
        return false;
    }
    
		@Override
    public E remove(int index){
        if(verifyIndex(index)){
            E old;
            if(isEmpty())
                return null;
            if(index==0){
                DoublyCircularLinkedNodeList<E> first=getLast().getNext();
                old=first.getContent();
                getLast().setNext(first.getNext());
                first.getNext().setPrevious(getLast());
                first.setContent(null);
                first.setNext(null);
                first.setPrevious(null);
                effectiveSize--;
                return old;
            }
            if(index==size()-1){
                DoublyCircularLinkedNodeList<E> lastNode=getLast();
                old=lastNode.getContent();
                lastNode.getPrevious().setNext(lastNode.getNext());
                lastNode.getNext().setPrevious(lastNode.getPrevious());
                setLast(getLast().getPrevious());
                lastNode.setContent(null);
                lastNode.setNext(null);
                lastNode.setPrevious(null);
                effectiveSize--;
                return old;
            }
            DoublyCircularLinkedNodeList<E> temp=getLast().getNext();
            int count=0;
            while(index>count){
                temp=temp.getNext();
                count++;
            }
            old=temp.getContent();
            temp.getPrevious().setNext(temp.getNext());
            temp.getNext().setPrevious(temp.getPrevious());
            temp.setContent(null);
            temp.setNext(null);
            temp.setPrevious(null);
            effectiveSize--;
            return old;
        }
        return null;
    }
    
		@Override
    public E get(int index){
        if(verifyIndex(index)){
            DoublyCircularLinkedNodeList<E> temp=getLast().getNext();
            int count=0;
            while(index>count){
                temp=temp.getNext();
                count++;
            }
            return temp.getContent();
        }
        return null;
    }
    
		@Override
    public E set(int index, E e){
        return replace(index,e);
    }
    
		@Override
    public E replace(int index, E e){
        if(verify(index, e)){
            DoublyCircularLinkedNodeList<E> temp=getLast().getNext();
            int count=0;
            while(index>count){
                temp=temp.getNext();
                count++;
            }
            E old=temp.getContent();
            temp.setContent(e);
            return old;
        }
        return null;
    }
    
		@Override
    public String toString(){
        String txt="";
        DoublyCircularLinkedNodeList<E> temp=getLast().getNext();
        do{
            txt+=temp.getContent()+" ";
            temp=temp.getNext();
        }while(temp!=getLast().getNext());
        return txt;
    }
    
		@Override
    public Iterator<E> iterator(){
        return new Iterator<E>(){
            DoublyCircularLinkedNodeList<E> temp=getLast().getNext();
						@Override
            public boolean hasNext() {
                return temp!=null;
            }
						@Override
            public E next() {
                E old=temp.getContent();
                temp=temp.getNext();
                if(temp==getLast().getNext())
                    temp=null;
                return old;
            }
        };
    }
    
    public Iterator<E> reverseIterator(){
        return new Iterator<E>(){
            DoublyCircularLinkedNodeList<E> temp=getLast();
						@Override
            public boolean hasNext() {
                return temp!=null;
            }
						@Override
            public E next() {
                E old=temp.getContent();
                temp=temp.getPrevious();
                if(temp==getLast())
                    temp=null;
                return old;
            }
        };
    }
    
		@Override
    public List<E> findAll(E element,Comparator<E> cmp){
        List<E> result=new DoublyCircularLinkedList<>();
        for(E e:this)
            if(cmp.compare(e,element)==0)
                result.addLast(e);
        return result;
    }

		@Override
    public List<E> findLower(E element,Comparator<E> cmp){
        List<E> result=new DoublyCircularLinkedList<>();
        for(E e:this){
            if(cmp.compare(e,element)<0)
                result.addLast(e);
        }
        return result;
    }

		@Override
    public List<E> findGreater(E element,Comparator<E> cmp){
        List<E> result=new DoublyCircularLinkedList<>();
        for(E e:this){
            if(cmp.compare(e,element)>0)
                result.addLast(e);
        }
        return result;
    }

		@Override
    public List<E> findBetween(E e1,E e2,Comparator<E> cmp){
        List<E> result=new DoublyCircularLinkedList<>();
        for(E e:this){
            if(cmp.compare(e,e1)>=0&&cmp.compare(e,e2)<=0)
                result.addLast(e);
        }
        return result;
    }
}
