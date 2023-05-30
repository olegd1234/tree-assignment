package com.tree;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Node<T> {

    private T data;
    private List<Node<T>> childNodes;

    public Node(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }
    public void addChild(Node<T> child) {
        if (childNodes == null) {
            childNodes = new ArrayList<>();
        }
        childNodes.add(child);
    }
}
