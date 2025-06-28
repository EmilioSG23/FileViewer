package com.emiliosg23.models.tdas.lists.dll;

import java.util.Comparator;
import java.util.Iterator;

import com.emiliosg23.models.tdas.lists.List;

public class DoublyLinkedList<E> implements List<E>{
    private DoublyLinkedNodeList<E> header;
    private DoublyLinkedNodeList<E> last;
    private int effectiveSize;
    
    public DoublyLinkedList(){
        header=null;
        last=null;
        effectiveSize=0;
    }

    public DoublyLinkedNodeList<E> getHeader() {
        return header;
    }

    public void setHeader(DoublyLinkedNodeList<E> header) {
        this.header = header;
    }

    public DoublyLinkedNodeList<E> getLast() {
        return last;
    }

    public void setLast(DoublyLinkedNodeList<E> last) {
        this.last = last;
    }
    
    @Override
    public int size(){
        return effectiveSize;
    }
    @Override
    public boolean isEmpty(){
        return size()==0;
    }
    
    @Override
    public void clear(){
        if(!isEmpty()){
            DoublyLinkedNodeList<E> temp=getHeader();
            while(temp!=null){
                DoublyLinkedNodeList<E> nextTemp=temp.getNext();
                temp.setContent(null);
                temp.setNext(null);
                temp.setPrevious(null);
                temp=nextTemp;
            }
            setHeader(null);
            setLast(null);
            effectiveSize=0;
        }
    }
    
    @Override
    public int indexOf(E e){
        if(verifyElement(e)){
            int i=0;
            DoublyLinkedNodeList<E> temp=getHeader();
            while(temp!=null){
                if(temp.getContent().equals(e))
                    return i;
                i++;
                temp=temp.getNext();
            }
        }
        return -1;
    }
    
    @Override
    public boolean contains(E e){
        if(verifyElement(e)){
            DoublyLinkedNodeList<E> temp=getHeader();
            while(temp!=null){
                if(temp.getContent().equals(e))
                    return true;
                temp=temp.getNext();
            }
        }
        return false;
    }
    
    @Override
    public boolean add(int index, E e){
        if(verifyInsertion(index, e)){
            int count=0;
            DoublyLinkedNodeList<E> temp=getHeader();
            DoublyLinkedNodeList<E> newNode=new DoublyLinkedNodeList<>(e);
            if(isEmpty()){
                setHeader(newNode);
                setLast(newNode);
                effectiveSize++;
                return true;
            }
            if(index==0){
                newNode.setNext(getHeader());
                header.setPrevious(newNode);
                setHeader(newNode);
                effectiveSize++;
                return true;
            }
            if(index==size()){
                newNode.setPrevious(getLast());
                last.setNext(newNode);
                setLast(newNode);
                effectiveSize++;
                return true;
            }
            
            while(index>count){
                temp=temp.getNext();
                count++;
            }
            DoublyLinkedNodeList<E> previousTemp=temp.getPrevious();
            newNode.setNext(temp);
            newNode.setPrevious(previousTemp);
            temp.setPrevious(newNode);
            previousTemp.setNext(newNode);
            effectiveSize++;
            return true;
        }
        return false;
    }
    
    @Override
    public E remove(int index){
        if(verifyIndex(index)){
            int count=0;
            DoublyLinkedNodeList<E> temp=getHeader();
            E old;
            if(isEmpty())
                return null;
            if(index==0){
                DoublyLinkedNodeList<E> oldTemp=getHeader();
                old=oldTemp.getContent();
                setHeader(getHeader().getNext());
                getHeader().setPrevious(null);
                oldTemp.setContent(null);
                oldTemp.setNext(null);
                oldTemp.setPrevious(null);
                effectiveSize--;
                return old;
            }
            if(index==size()-1){
                DoublyLinkedNodeList<E> oldTemp=getLast();
                old=oldTemp.getContent();
                setLast(getLast().getPrevious());
                getLast().setNext(null);
                oldTemp.setContent(null);
                oldTemp.setNext(null);
                oldTemp.setPrevious(null);
                effectiveSize--;
                return old;
            }
            while(index>count){
                temp=temp.getNext();
                count++;
            }
            DoublyLinkedNodeList<E> nextTemp=temp.getNext();
            DoublyLinkedNodeList<E> previousTemp=temp.getPrevious();
            previousTemp.setNext(nextTemp);
            nextTemp.setPrevious(previousTemp);
            old=temp.getContent();
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
            DoublyLinkedNodeList<E> temp=getHeader();
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
    public E set(int index,E e){
        return replace(index,e);
    }
    
		@Override
    public E replace(int index,E e){
        if(verify(index, e)){
            E old;
            DoublyLinkedNodeList<E> temp=getHeader();
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
        String text="";
        if(!isEmpty()){
            DoublyLinkedNodeList<E> temp=getHeader();
            while(temp!=null){
                text+=temp.getContent()+" ";
                temp=temp.getNext();
            }
        }
        return text;
    }
    
		@Override
    public Iterator<E> iterator(){
        return new Iterator<E>(){
            DoublyLinkedNodeList<E> temp=getHeader();
						@Override
            public boolean hasNext(){
                return temp!=null;
            }
						@Override
            public E next(){
                E old=temp.getContent();
                temp=temp.getNext();
                return old;
            }
        };
    }
    
    public Iterator<E> reverseIterator(){
        return new Iterator<E>(){
            DoublyLinkedNodeList<E> temp=getLast();
						@Override
            public boolean hasNext(){
                return temp!=null;
            }
						@Override
            public E next(){
                E old=temp.getContent();
                temp=temp.getPrevious();
                return old;
            }
        };
    }
    
		@Override
    public List<E> findAll(E element,Comparator<E> cmp){
        List<E> result=new DoublyLinkedList<>();
        for(E e:this)
            if(cmp.compare(e,element)==0)
                result.addLast(e);
        return result;
    }

		@Override
    public List<E> findLower(E element,Comparator<E> cmp){
        List<E> result=new DoublyLinkedList<>();
        for(E e:this){
            if(cmp.compare(e,element)<0)
                result.addLast(e);
        }
        return result;
    }

		@Override
    public List<E> findGreater(E element,Comparator<E> cmp){
        List<E> result=new DoublyLinkedList<>();
        for(E e:this){
            if(cmp.compare(e,element)>0)
                result.addLast(e);
        }
        return result;
    }

		@Override
    public List<E> findBetween(E e1,E e2,Comparator<E> cmp){
        List<E> result=new DoublyLinkedList<>();
        for(E e:this){
            if(cmp.compare(e,e1)>=0&&cmp.compare(e,e2)<=0)
                result.addLast(e);
        }
        return result;
    }
}
