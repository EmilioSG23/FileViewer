
package com.emiliosg23.models.tdas.lists.al;

import java.util.Comparator;
import java.util.Iterator;

import com.emiliosg23.models.tdas.lists.List;

public class ArrayList<E> implements List<E>{
    private int capacity=100;
    private E[] elements;
    private int effectiveSize;
    
    @SuppressWarnings("unchecked")
		public ArrayList(){
        elements=(E[]) new Object[capacity];
        effectiveSize=0;
    }
    
    //Métodos internos del ArrayList
    private boolean isFull(){
        return capacity==size();
    }
    
    @SuppressWarnings("ManualArrayToCollectionCopy")
    private void addCapacity(){
        capacity*=2;
        @SuppressWarnings("unchecked")
				E[] nuevo=(E[]) new Object[capacity];
        for(int i=0;i<size();i++){
            nuevo[i]=elements[i];
        }
        elements=nuevo;
    }
    
    //Métodos implementados de List
    @Override
    public int size() {
        return effectiveSize;
    }

    @Override
    public boolean isEmpty() {
        return effectiveSize==0;
    }

    @Override
    public void clear() {
        if(!isEmpty()){
            for(int i=0;i<size();i++)
                elements[i]=null;
            effectiveSize=0;
        }
    }
    
    @Override
    public int indexOf(E e){
        if(verifyElement(e))
            for(int i=0;i<size();i++)
                if(elements[i].equals(e))
                    return i;
        return -1;
    }
    
    @Override
    public boolean contains(E e){
        if(verifyElement(e))
            for(int i=0;i<size();i++)
                if(elements[i].equals(e))
                    return true; 
        return false;
    }

    @Override
    public boolean add(int index, E element) {
        if(verifyInsertion(index,element)){
            if(isFull()||isEmpty())
                addCapacity();
            //Desplazamiento
            for(int i=size();i>index;i--)
                elements[i]=elements[i-1];
            elements[index]=element;
            effectiveSize++;
            return true;
        }
        return false;
    }

    @Override
    public E remove(int index) {
        E oldE=null;
        if(verifyIndex(index)){
            if(isEmpty())
                return null;
            oldE=elements[index];
            //Desplazamiento
            for(int i=index;i<size();i++)
                elements[i]=elements[i+1];
            elements[size()]=null;
            effectiveSize--;
        }
        return oldE;
    }

    @Override
    public E get(int index) {
        if(verifyIndex(index)&&!isEmpty())
            return elements[index];
        return null;
    }

    @Override
    public E set(int index, E element) {
        return replace(index,element);
    }
    
    @Override
    public E replace(int index,E e){
        if(verify(index,e)){
            E temp=get(index);
            elements[index]=e;
            return temp;
        }
        return null;
    }
    
		@Override
    public String toString(){
        String text="";
        if(!isEmpty()){
            for(int i=0;i<size();i++){
                text+=(elements[i]+" ");
            }
        }
        return text;
    }
    
		@Override
    public Iterator<E> iterator(){
        return new Iterator<E>(){
            private int index=0;
            @Override
            public boolean hasNext(){
                return index<size();
            }
            @Override
            public E next(){
                E tmp= get(index);
                index++;
                return tmp;
            }
        };
    }
    
		@Override
    public List<E> findAll(E element,Comparator<E> cmp){
        List<E> result=new ArrayList<>();
        for(E e:this)
            if(cmp.compare(e,element)==0)
                result.addLast(e);
        return result;
    }

		@Override
    public List<E> findLower(E element,Comparator<E> cmp){
        List<E> result=new ArrayList<>();
        for(E e:this){
            if(cmp.compare(e,element)<0)
                result.addLast(e);
        }
        return result;
    }

		@Override
    public List<E> findGreater(E element,Comparator<E> cmp){
        List<E> result=new ArrayList<>();
        for(E e:this){
            if(cmp.compare(e,element)>0)
                result.addLast(e);
        }
        return result;
    }

		@Override
    public List<E> findBetween(E e1,E e2,Comparator<E> cmp){
        List<E> result=new ArrayList<>();
        for(E e:this){
            if(cmp.compare(e,e1)>=0&&cmp.compare(e,e2)<=0)
                result.addLast(e);
        }
        return result;
    }
}
