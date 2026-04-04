package com.fileviewer.tdas.trees;

import com.fileviewer.tdas.lists.List;
import com.fileviewer.tdas.lists.dll.DoublyLinkedList;

/**
 * Árbol genérico de múltiples hijos (n-ario).
 *
 * <p>
 * Cada nodo tiene un contenido de tipo {@code T} y una lista ordenada de
 * subárboles hijos. <strong>No es thread-safe</strong>: no debe compartirse
 * entre hilos durante la construcción. Una vez construido, es seguro para
 * lectura concurrente.</p>
 *
 * @param <T> tipo del contenido almacenado en cada nodo
 * @see MultiTreeNode
 */
public class MultiTree<T> {

    private MultiTreeNode<T> root;

    /**
     * Crea un árbol con un nodo raíz vacío.
     */
    public MultiTree() {
        this(new MultiTreeNode<>());
    }

    /**
     * Crea un árbol con el nodo raíz indicado.
     *
     * @param root nodo raíz
     */
    public MultiTree(MultiTreeNode<T> root) {
        this.root = root;
    }

    /**
     * Crea un árbol con un nodo raíz que contiene el valor dado.
     *
     * @param content contenido del nodo raíz
     */
    public MultiTree(T content) {
        this.root = new MultiTreeNode<>(content);
    }

    /**
     * @return nodo raíz del árbol
     */
    public MultiTreeNode<T> getRoot() {
        return root;
    }

    /**
     * @param root nuevo nodo raíz
     */
    public void setRoot(MultiTreeNode<T> root) {
        this.root = root;
    }

    /**
     * @return {@code true} si el árbol no tiene raíz
     */
    public boolean isEmpty() {
        return this.root == null;
    }

    /**
     * @return {@code true} si la raíz no tiene hijos
     */
    public boolean isLeaf() {
        return this.root.getChildren().isEmpty();
    }

    /**
     * Añade un subárbol como hijo de la raíz.
     *
     * @param child subárbol a añadir
     * @return {@code true} si se añadió correctamente
     */
    public boolean addChild(MultiTree<T> child) {
        if (child == null) {
            throw new IllegalArgumentException("Cannot add a null child tree");
        }
        return root.getChildren().addLast(child);
    }

    /**
     * Añade un elemento como nuevo hijo hoja de la raíz.
     *
     * @param child contenido del nuevo nodo hijo
     * @return {@code true} si se añadió correctamente
     */
    public boolean addChild(T child) {
        if (child == null) {
            throw new IllegalArgumentException("Cannot add a null child");
        }
        return root.getChildren().addLast(new MultiTree<>(child));
    }

    public boolean removeChild(MultiTree<T> child) {
        return root.getChildren().remove(child);
    }

    public boolean removeChild(T child) {
        return root.getChildren().remove(new MultiTree<>(child));
    }

    public boolean containsChild(MultiTree<T> child) {
        return root.getChildren().contains(child);
    }

    public boolean containsChild(T child) {
        return root.getChildren().contains(new MultiTree<>(child));
    }

    @Override
    public String toString() {
        String txt = "";
        if (isEmpty()) {
            return txt;
        }
        txt += getRoot().getContent() + " ";
        for (MultiTree<T> child : getRoot().getChildren()) {
            txt += child.toString();
        }
        return txt;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MultiTree<?> other)) {
            return false;
        }
        if (this.root == null && other.root == null) {
            return true;
        }
        if (this.root == null || other.root == null) {
            return false;
        }
        return getRoot().equals(other.getRoot());
    }

    @Override
    public int hashCode() {
        return root == null ? 0 : root.hashCode();
    }

    /**
     * Recorre el árbol en preorden y retorna todos los contenidos.
     *
     * @return lista con todos los elementos del árbol en preorden
     */
    public List<T> traverseTree() {
        List<T> order = new DoublyLinkedList<>();
        order.addLast(getRoot().getContent());
        for (MultiTree<T> child : getRoot().getChildren()) {
            order.addAll(child.traverseTree());
        }
        return order;
    }
}
