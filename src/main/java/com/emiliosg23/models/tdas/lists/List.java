package com.emiliosg23.models.tdas.lists;

import java.io.Serializable;
import java.util.Comparator;

public interface List<E> extends Iterable<E>,Serializable{
    default public boolean addFirst(E e){
        return add(0,e);
    }
    default public boolean addLast(E e){
        return add(size(),e);
    }
    default public boolean addAll(List<E> list){
        if(!list.isEmpty()){
            for(E e:list)
                addLast(e);
            return true;
        }
        return false;
    }
    default public E removeFirst(){
        return remove(0);
    }
    default public E removeLast(){
        return remove(size()-1);
    }
    
    public int size();
    public boolean isEmpty();
    public void clear();
    public int indexOf(E e);
    public boolean contains(E e);
    public boolean add(int index,E element);
    public E remove(int index);
    default public boolean remove(E e){
        int indexOf=indexOf(e);
        if(indexOf>-1){
            remove(indexOf);
            return true;
        }
        return false;
    }
    public E get(int index);
    public E set(int index,E e);
    public E replace(int index,E e);
    default public boolean replace(E oldE,E newE){
        int indexOf=indexOf(oldE);
        if(verifyElement(newE)&&(indexOf>-1)){
            replace(indexOf,newE);
            return true;
        }
        return false;
    }
    
    default public E find(E element, Comparator<E> cmp){
        for(E e:this){
            if(cmp.compare(e, element)==0)
                return e;
        }
        return null;
    }
    public List<E> findAll(E element, Comparator<E> cmp);
    public List<E> findLower(E element, Comparator<E> cmp);
    public List<E> findGreater(E element, Comparator<E> cmp);
    public List<E> findBetween(E e1,E e2, Comparator<E> cmp);
    
    //Métodos de verificación
    default public boolean verify(int index, E element){
        return verifyIndex(index)&&verifyElement(element)&&!isEmpty();
    }
    default public boolean verifyInsertion(int index,E element){
        return verifyInsertionIndex(index)&&verifyElement(element);
    }
    default public boolean verifyIndex(int index){
        boolean isInRange=index>=0&&index<size();
        if(!isInRange) {
            if (size()<=0) {
                throw new ArrayIndexOutOfBoundsException("La lista está vacía");
            }
            if (index > size()) {
                throw new ArrayIndexOutOfBoundsException("La posición ingresada no ha sido inicializado");
            }
        }
        return isInRange;
    }
    default public boolean verifyInsertionIndex(int index){
        boolean isInRange;
        if(size()==0)
            isInRange=index>=0;
        else
            isInRange=index>=0&&index<=size();
        
        if(!isInRange)
            if(index>size())
                throw new ArrayIndexOutOfBoundsException("La posición ingresada no es válida");
        return isInRange;
    }
    default public boolean verifyElement(E element){
        if(element==null)
            throw new NullPointerException("El elemento ingresado es nulo");
        return true;
    }
}
