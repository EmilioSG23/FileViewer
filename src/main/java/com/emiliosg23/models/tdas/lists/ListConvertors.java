package com.emiliosg23.models.tdas.lists;

import com.emiliosg23.models.tdas.lists.al.ArrayList;
import com.emiliosg23.models.tdas.lists.dcll.DoublyCircularLinkedList;
import com.emiliosg23.models.tdas.lists.dll.DoublyLinkedList;
import com.emiliosg23.models.tdas.lists.ll.LinkedList;

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

		private static <E, T extends List<E>> T convert(List<E> from, T to){
			to.addAll(from);
			return to;
		}
}
