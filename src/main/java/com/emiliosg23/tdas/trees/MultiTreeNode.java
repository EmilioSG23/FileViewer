package com.emiliosg23.tdas.trees;

import com.emiliosg23.tdas.lists.List;
import com.emiliosg23.tdas.lists.dll.DoublyLinkedList;

/**
 * Nodo de un árbol n-ario genérico.
 *
 * <p>
 * Almacena un contenido de tipo {@code T} y una lista de subárboles hijos. Por
 * defecto, los hijos se implementan con {@link DoublyLinkedList}.</p>
 *
 * @param <T> tipo del contenido
 * @see MultiTree
 */
public class MultiTreeNode<T> {

    private T content;
    private List<MultiTree<T>> children;

    /**
     * Crea un nodo vacío sin contenido.
     */
    public MultiTreeNode() {
        this(null, new DoublyLinkedList<>());
    }

    /**
     * Crea un nodo con contenido y sin hijos.
     *
     * @param content contenido del nodo
     */
    public MultiTreeNode(T content) {
        this(content, new DoublyLinkedList<>());
    }

    /**
     * Crea un nodo con contenido e hijos explícitos.
     *
     * @param content contenido del nodo
     * @param children lista de subárboles hijos
     */
    public MultiTreeNode(T content, List<MultiTree<T>> children) {
        this.content = content;
        this.children = children;
    }

    /**
     * @return contenido del nodo
     */
    public T getContent() {
        return content;
    }

    /**
     * @param content nuevo contenido
     */
    public void setContent(T content) {
        this.content = content;
    }

    /**
     * @return lista de subárboles hijos
     */
    public List<MultiTree<T>> getChildren() {
        return children;
    }

    /**
     * Compara dos nodos por su contenido.
     *
     * @param node nodo a comparar
     * @return {@code true} si tienen contenido igual
     */
    public boolean equals(MultiTreeNode<T> node) {
        return content.equals(node.getContent());
    }
}
