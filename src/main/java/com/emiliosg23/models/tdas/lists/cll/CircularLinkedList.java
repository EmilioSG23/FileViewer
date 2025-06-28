package com.emiliosg23.models.tdas.lists.cll;


import java.util.Comparator;
import java.util.Iterator;

import com.emiliosg23.models.tdas.lists.List;

public class CircularLinkedList<E> implements List<E>{
    private CircularLinkedNodeList<E> last;
    private int effectiveSize;
    
    public CircularLinkedList(){
        last=null;
        effectiveSize=0;
    }

    public CircularLinkedNodeList<E> getLast() {
        return last;
    }

    public void setLast(CircularLinkedNodeList<E> last) {
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
            CircularLinkedNodeList<E> temp=getLast().getNext();
            while(temp!=null){
                CircularLinkedNodeList<E> nextTemp=temp.getNext();
                temp.setContent(null);
                temp.setNext(null);
                temp=nextTemp;
            }
            effectiveSize=0;
        }
    }
    
    @Override
    public int indexOf(E e){
        if(verifyElement(e)){
            CircularLinkedNodeList<E> temp=getLast().getNext();
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
            CircularLinkedNodeList<E> temp=getLast().getNext();
            while(temp!=null){
                if(temp.getContent().equals(e))
                    return true;
                temp=temp.getNext();
            }
        }
        return false;
    }
    
    @Override
    public boolean add(int index,E e){
        if(verifyInsertion(index, e)){
            int count=0;
            CircularLinkedNodeList<E> newNode=new CircularLinkedNodeList<>(e);
            if(isEmpty()){
                setLast(newNode);
                newNode.setNext(newNode);
                effectiveSize++;
                return true;
            }
            if(index==0){
                newNode.setNext(getLast().getNext());
                getLast().setNext(newNode);
                effectiveSize++;
                return true;
            }
            if(index==size()){
                newNode.setNext(getLast().getNext());
                getLast().setNext(newNode);
                setLast(newNode);
                effectiveSize++;
                return true;
            }
            CircularLinkedNodeList<E> temp=getLast().getNext();
            CircularLinkedNodeList<E> prevTemp=getLast();
            while(index>count){
                temp=temp.getNext();
                prevTemp=prevTemp.getNext();
                count++;
            }
            prevTemp.setNext(newNode);
            newNode.setNext(temp);
            effectiveSize++;
            return true;
        }
        return false;
    }
    
    @Override
    public E remove(int index){
        if(verifyIndex(index)){
            int count=0;
            CircularLinkedNodeList<E> temp=getLast().getNext();
            E old;
            if(isEmpty())
                return null;
            if(index==0){
                CircularLinkedNodeList<E> first=getLast().getNext();
                CircularLinkedNodeList<E> nextFirst=first.getNext();
                getLast().setNext(nextFirst);
                old=first.getContent();
                first.setContent(null);
                first.setNext(null);
                effectiveSize--;
                return old;
            }
            if(index==size()-1){
                CircularLinkedNodeList<E> first=getLast().getNext();
                CircularLinkedNodeList<E> oldLast=getLast();
                CircularLinkedNodeList<E> newLast=getLast().getNext();
                while(size()-1-1>count){
                    newLast=newLast.getNext();
                    count++;
                }
                old=oldLast.getContent();
                newLast.setNext(first);
                oldLast.setContent(null);
                oldLast.setNext(null);
                effectiveSize--;
                return old;
            }
            CircularLinkedNodeList<E> prevTemp=getLast();
            while(index>count){
                temp=temp.getNext();
                prevTemp=prevTemp.getNext();
                count++;
            }
            CircularLinkedNodeList<E> nextTemp=temp.getNext();
            old=temp.getContent();
            prevTemp.setNext(nextTemp);
            temp.setContent(null);
            temp.setNext(null);
            effectiveSize--;
            return old;
        }
        return null;
    }
    
    @Override
    public E get(int index){
        if(verifyIndex(index)){
            int count=0;
            CircularLinkedNodeList<E> temp=getLast().getNext();
            while(index>count){
                temp=temp.getNext();
                count++;
            }
            return temp.getContent();
        }
        return null;
    }
    
    @Override
    public E set(int index,E e){
        return replace(index,e);
    }
    
    @Override
    public E replace(int index, E e){
        if(verify(index, e)){
            E old;
            CircularLinkedNodeList<E> temp=getLast().getNext();
            int count=0;
            while(index>count){
                temp=temp.getNext();
                count++;
            }
            old=temp.getContent();
            temp.setContent(e);
            return old;
        }
        return null;
    }
    
    @Override
    public String toString(){
        String txt="";
        if(!isEmpty()){
            CircularLinkedNodeList<E> temp=getLast().getNext();
            do{
                txt+=temp.getContent()+" ";
                temp=temp.getNext();
            }while(temp!=getLast().getNext());
        }
        return txt;
    }
    
    @Override
    public Iterator<E> iterator(){
        return new Iterator<>(){
            CircularLinkedNodeList<E> temp=getLast().getNext();
            @Override
            public boolean hasNext(){
                return temp!=null;
            }
            @Override
            public E next(){
                E old=temp.getContent();
                temp=temp.getNext();
                if(temp==getLast().getNext())
                    temp=null;
                return old;
            }
        };
    }
    
    @Override
    public List<E> findAll(E element,Comparator<E> cmp){
        List<E> result=new CircularLinkedList<>();
        for(E e:this)
            if(cmp.compare(e,element)==0)
                result.addLast(e);
        return result;
    }
    @Override
    public List<E> findLower(E element,Comparator<E> cmp){
        List<E> result=new CircularLinkedList<>();
        for(E e:this){
            if(cmp.compare(e,element)<0)
                result.addLast(e);
        }
        return result;
    }
    @Override
    public List<E> findGreater(E element,Comparator<E> cmp){
        List<E> result=new CircularLinkedList<>();
        for(E e:this){
            if(cmp.compare(e,element)>0)
                result.addLast(e);
        }
        return result;
    }
    @Override
    public List<E> findBetween(E e1,E e2,Comparator<E> cmp){
        List<E> result=new CircularLinkedList<>();
        for(E e:this){
            if(cmp.compare(e,e1)>=0&&cmp.compare(e,e2)<=0)
                result.addLast(e);
        }
        return result;
    }
}
