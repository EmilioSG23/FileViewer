package com.emiliosg23.models.tdas.trees;

import com.emiliosg23.models.tdas.lists.List;
import com.emiliosg23.models.tdas.lists.dll.DoublyLinkedList;

public class MultiTree<T> {
    private MultiTreeNode<T> root;
    
    public MultiTree(){
        this(new MultiTreeNode<>());
    }
    public MultiTree(MultiTreeNode<T> root){
        this.root=root;
    }
    public MultiTree(T content){
        this.root=new MultiTreeNode<>(content);
    }

    public MultiTreeNode<T> getRoot() {
        return root;
    }

    public void setRoot(MultiTreeNode<T> root) {
        this.root = root;
    }
    
    public boolean isEmpty(){
        return this.root==null;
    }
    public boolean isLeaf(){
        return this.root.getChildren().isEmpty();
    }
    
    public boolean addChild(MultiTree<T> child){
        return root.getChildren().addLast(child);
    }
    public boolean addChild(T child){
        return root.getChildren().addLast(new MultiTree<>(child));
    }
    public boolean removeChild(MultiTree<T> child){
        return root.getChildren().remove(child);
    }
    public boolean removeChild(T child){
        return root.getChildren().remove(new MultiTree<>(child));
    }
    public boolean containsChild(MultiTree<T> child){
        return root.getChildren().contains(child);
    }
    public boolean containsChild(T child){
        return root.getChildren().contains(new MultiTree<>(child));
    }

		@Override
    public String toString(){
        String txt="";
        if(isEmpty())
            return txt;
        txt+=getRoot().getContent()+" ";
        for(MultiTree<T> child:getRoot().getChildren())
            txt+=child.toString();
        return txt;
    }
    
    public boolean equals(MultiTree<T> tree){
        return getRoot().equals(tree.getRoot());
    }
     
		public List<T> traverseTree() {
			List<T> order = new DoublyLinkedList<>();
			order.addLast(getRoot().getContent());
			for (MultiTree<T> child : getRoot().getChildren())
				order.addAll(child.traverseTree());
			return order;
		}
}
