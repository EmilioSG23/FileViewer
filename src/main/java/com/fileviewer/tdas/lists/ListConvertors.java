package com.fileviewer.tdas.lists;

import com.fileviewer.tdas.lists.al.ArrayList;
import com.fileviewer.tdas.lists.dcll.DoublyCircularLinkedList;
import com.fileviewer.tdas.lists.dll.DoublyLinkedList;
import com.fileviewer.tdas.lists.ll.LinkedList;

/**
 * Utilidad para convertir entre implementaciones de {@link List}.
 *
 * @param <E> tipo de los elementos
 */
public class ListConvertors<E> {

    public static <E> DoublyCircularLinkedList<E> toDCLL(List<E> list) {
        return convert(list, new DoublyCircularLinkedList<>());
    }

    public static <E> DoublyLinkedList<E> toDLL(List<E> list) {
        return convert(list, new DoublyLinkedList<>());
    }

    public static <E> LinkedList<E> toLL(List<E> list) {
        return convert(list, new LinkedList<>());
    }

    public static <E> ArrayList<E> toAL(List<E> list) {
        return convert(list, new ArrayList<>());
    }

    private static <E, T extends List<E>> T convert(List<E> from, T to) {
        to.addAll(from);
        return to;
    }
}
