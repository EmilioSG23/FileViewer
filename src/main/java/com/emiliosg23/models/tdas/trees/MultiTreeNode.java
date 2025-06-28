package com.emiliosg23.models.tdas.trees;


import com.emiliosg23.models.tdas.lists.dll.DoublyLinkedList;
import com.emiliosg23.models.tdas.lists.List;

public class MultiTreeNode<T> {
    private T content;
    private List<MultiTree<T>> children;
    
    public MultiTreeNode(){
        this(null,new DoublyLinkedList<>());
    }
    public MultiTreeNode(T content){
        this(content,new DoublyLinkedList<>());
    }
    public MultiTreeNode(T content, List<MultiTree<T>> children){
        this.content=content;
        this.children=children;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public List<MultiTree<T>> getChildren() {
        return children;
    }
    
    public boolean equals(MultiTreeNode<T> node){
      return content.equals(node.getContent());
    }
}
