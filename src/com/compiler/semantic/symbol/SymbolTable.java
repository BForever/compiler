package com.compiler.semantic.symbol;

import com.compiler.semantic.type.Type;
import org.bytedeco.javacpp.LLVM;

import java.util.Map;

/**
 * 符号表定义（采用多叉树支持嵌套作用域，采用红黑树存储）
 */
public class SymbolTable {
    private SymbolNode currentNode;

    public void enterScope(SymbolNode parent) {
        SymbolNode node = new SymbolNode();
        if (parent != null) {
            if (parent.getLeft() == null) {
                parent.setLeft(node);
                parent.setRight(node);
            } else {
                parent.getRight().setSibling(node);
                parent.setRight(node);
            }
            node.setParent(parent);
        }
        currentNode = node;
    }

    public void leaveScope() {
        currentNode = currentNode.getParent();
    }

    public void put(String name, Type type) {
        Map<String, SymbolInfo> map = currentNode.getMap();

        if (map.get(name) == null) {
            map.put(name, new SymbolInfo(type,null));
        }
    }

    public void put(String name, Type type, LLVM.LLVMValueRef value) {
        Map<String, SymbolInfo> map = currentNode.getMap();

        if (map.get(name) == null) {
            map.put(name, new SymbolInfo(type,value));
        }
    }

    public SymbolInfo get(String name) {
        SymbolNode node = currentNode;

        while (node != null) {
            if (node.getMap().get(name) != null) {
                return node.getMap().get(name);
            }
            node = node.getParent();
        }

        return null;
    }

    public SymbolInfo getInCurrentScope(String name) {
        return currentNode.getMap().get(name);
    }

    public SymbolNode getCurrent() {
        return currentNode;
    }
}
