package com.emiliosg23.models.tdas.lists.ll;

import java.util.Comparator;
import java.util.Iterator;

import com.emiliosg23.models.tdas.lists.List;

public class LinkedList<E> implements List<E>{

    private LinkedNodeList<E> first;
    private LinkedNodeList<E> last;
    private int size;
    
    public LinkedList(){
        first=null;
        last=null;
        size=0;
    }
    //Métodos internos de LinkedList    
    public LinkedNodeList<E> getFirst() {
        return first;
    }

    public void setFirst(LinkedNodeList<E> first) {
        this.first = first;
    }

    public LinkedNodeList<E> getLast() {
        return last;
    }

    public void setLast(LinkedNodeList<E> last) {
        this.last = last;
    }    
    
    //Métodos implementados de List
    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size==0;
    }

    @Override
    public void clear() {
        if(!isEmpty()){
            LinkedNodeList<E> temp=first;
            while(temp!=null){
                LinkedNodeList<E> nextTemp=temp.getNext();
                temp.setNext(null);
                temp.setContent(null);
                temp=nextTemp;
            }
            first=null;
            last=null;
            size=0;
        }
    }
    
		@Override
    public int indexOf(E e){
        if(verifyElement(e)){
            int count=0;
            LinkedNodeList<E> temp=first;
            while(temp!=null){
                if(temp.getContent().equals(e))
                    return count;
                count++;
                temp=temp.getNext();
            }
        }
        return -1;
    }
    
		@Override
    public boolean contains(E e){
        if(verifyElement(e)){
            LinkedNodeList<E> temp=first;
            while(temp!=null){
                if(temp.getContent().equals(e))
                    return true;
                temp=temp.getNext();
            }
        }
        return false;
    }

    @Override
    public boolean add(int index, E element) {
        if(verifyInsertion(index,element)){
            LinkedNodeList<E> nuevo= new LinkedNodeList<>(element);
            LinkedNodeList<E> current=first;
            if(index==0){
                if(isEmpty())
                    last=nuevo;
                nuevo.setNext(first);
                first=nuevo;
                size++;
                return true;
            }
            if(index==size()){
                if(isEmpty())
                    first=nuevo;
                last.setNext(nuevo);
                last=nuevo;
                size++;
                return true;
            }
            LinkedNodeList<E> next=first.getNext();
            //Desplazamiento
            for(int i=1;i<index;i++){
                current=current.getNext();
                next=next.getNext();
            }
            current.setNext(nuevo);
            nuevo.setNext(next);
            size++;
            return true;
        }
        return false;
    }

    @Override
    public E remove(int index) {
        E oldE=null;
        if(verifyIndex(index)){
            LinkedNodeList<E> prevTemp=null;
            LinkedNodeList<E> temp=getFirst();
            LinkedNodeList<E> nextTemp=getFirst().getNext();
            if(isEmpty())
                return null;
            if(index==0){
                oldE=getFirst().getContent();
                setFirst(getFirst().getNext());
                size--;
                return oldE;
            }
            if(index==size()-1){
                oldE=getLast().getContent();
                while(temp.getNext()!=null){
                    if(temp==getFirst()){
                        prevTemp=getFirst();
                        temp=getFirst().getNext();
                    }else{
                        prevTemp=prevTemp.getNext();
                        temp=temp.getNext();
                    }
                }
                setLast(prevTemp);
                getLast().setNext(null);
                temp.setNext(null);
                size--;
                return temp.getContent();
            }
            for(int i=0;i<index;i++){
                if(temp==getFirst()){
                    prevTemp=getFirst();
                    temp=getFirst().getNext();
                    nextTemp=temp.getNext();
                }else{
                    prevTemp=prevTemp.getNext();
                    temp=temp.getNext();
                    nextTemp=nextTemp.getNext();
                }
            }
            prevTemp.setNext(nextTemp);
            temp.setNext(null);
            size--;
            return temp.getContent();
        }
        return oldE;
    }

    @Override
    public E get(int index) {
        E element=null;
        if(verifyIndex(index)){
            LinkedNodeList<E> temp=getFirst();
            for(int i=0;i<index;i++){
                temp=temp.getNext();
            }
            element=temp.getContent();
        }
        return element;
    }

		@Override
    public E set(int index, E element) {
        return replace(index,element);
        /*E oldE=null;
        if(isEmpty())
            return oldE;
        if(verify(index,element)){
            LinkedNodeList<E> nuevo=new LinkedNodeList<>(element);
            LinkedNodeList<E> prevTemp=null;
            LinkedNodeList<E> temp=getFirst();
            LinkedNodeList<E> nextTemp=getFirst().getNext();
            for(int i=0;i<index;i++){
                if(temp==getFirst()){
                    prevTemp=getFirst();
                    temp=getFirst().getNext();
                    nextTemp=temp.getNext();
                }else{
                    prevTemp=prevTemp.getNext();
                    temp=temp.getNext();
                    nextTemp=nextTemp.getNext();
                }
            }
            oldE=temp.getContent();
            prevTemp.setNext(nuevo);
            nuevo.setNext(nextTemp);
        }
        return oldE;*/
    }
    
		@Override
    public E replace(int index,E e){
        if(verify(index,e)){
            E temp=get(index);
            LinkedNodeList<E> tempNode=first;
            int i=0;
            while(i<index){
                tempNode=tempNode.getNext();
                i++;
            }
            tempNode.setContent(e);
            return temp;
        }
        return null;
    }
    
		@Override
    public String toString(){
        String txt="";
        if(!isEmpty()){
            LinkedNodeList<E> temp=getFirst();
            for(int i=0;i<size();i++){
                txt+=(temp.getContent()+" ");
                temp=temp.getNext();
            }
        }
        return txt;
    }
    
		@Override
    public Iterator<E> iterator(){
        return new Iterator<E>(){
            LinkedNodeList<E> cursor=first;
            @Override
            public boolean hasNext() {
                return cursor!=null;
            }
            @Override
            public E next() {
                E oldE=cursor.getContent();
                cursor=cursor.getNext();
                return oldE;
            }
        };
    }
    
		@Override
    public List<E> findAll(E element,Comparator<E> cmp){
        List<E> result=new LinkedList<>();
        for(E e:this)
            if(cmp.compare(e,element)==0)
                result.addLast(e);
        return result;
    }

		@Override
    public List<E> findLower(E element,Comparator<E> cmp){
        List<E> result=new LinkedList<>();
        for(E e:this){
            if(cmp.compare(e,element)<0)
                result.addLast(e);
        }
        return result;
    }

		@Override
    public List<E> findGreater(E element,Comparator<E> cmp){
        List<E> result=new LinkedList<>();
        for(E e:this){
            if(cmp.compare(e,element)>0)
                result.addLast(e);
        }
        return result;
    }

		@Override
    public List<E> findBetween(E e1,E e2,Comparator<E> cmp){
        List<E> result=new LinkedList<>();
        for(E e:this){
            if(cmp.compare(e,e1)>=0&&cmp.compare(e,e2)<=0)
                result.addLast(e);
        }
        return result;
    }
}
