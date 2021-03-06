package company.vel.binaryTree;

import java.util.function.Function;

public class SimpleBinaryTree<T> implements IBinaryTree<T>
{
    private class SimpleTreeNode implements TreeNode<T>
    {
        public T value;
        public SimpleTreeNode left;
        public SimpleTreeNode right;

        public SimpleTreeNode(T value, SimpleTreeNode left, SimpleTreeNode right)
        {
            this.value = value;
            this.left = left;
            this.right = right;
        }

        public SimpleTreeNode(T value)
        {
            this(value, null, null);
        }

        @Override
        public T getValue()
        {
            return value;
        }

        @Override
        public TreeNode<T> getLeft()
        {
            return left;
        }

        @Override
        public TreeNode<T> getRight()
        {
            return right;
        }

    }

    private SimpleTreeNode root = null;

    private Function<String, T> fromStrFunc;
    private Function<T, String> toStrFunc;

    public SimpleBinaryTree(Function<String, T> fromStrFunc, Function<T, String> toStrFunc)
    {
        this.fromStrFunc = fromStrFunc;
        this.toStrFunc = toStrFunc;
    }

    public SimpleBinaryTree(Function<String, T> fromStrFunc)
    {
        this(fromStrFunc, Object::toString);
    }

    public SimpleBinaryTree()
    {
        this(null);
    }

    @Override
    public TreeNode<T> getRoot()
    {
        return root;
    }

    private void deleteSingleNodes(SimpleTreeNode node)
    {
        if (isOneChild(node.right, node.left))
        {
            deleteSingleNodes(node.left);
            node.value = node.left.value;
            node.right = node.left.right;
            node.left = node.left.left;
        }
        else
        {
            if (isOneChild(node.left, node.right))
            {
                deleteSingleNodes(node.right);
                node.value = node.right.value;
                node.left = node.right.left;
                node.right = node.right.right;
            }
            if (node.left != null && node.right != null)
            {
                deleteSingleNodes(node.left);
                deleteSingleNodes(node.right);
            }
        }
    }

    public void deleteSingleNodes()
    {
        deleteSingleNodes(root);
    }

    private boolean isOneChild(SimpleTreeNode nodeNull, SimpleTreeNode nodeNotNull)
    {
        return nodeNull == null && nodeNotNull != null;
    }

    public void clear()
    {
        root = null;
    }

    private T fromStr(String s) throws Exception
    {
        s = s.trim();
        if (s.length() > 0 && s.charAt(0) == '"')
        {
            s = s.substring(1);
        }
        if (s.length() > 0 && s.charAt(s.length() - 1) == '"')
        {
            s = s.substring(0, s.length() - 1);
        }
        if (fromStrFunc == null)
        {
            throw new Exception("???? ???????????????????? ?????????????? ?????????????????????? ???????????? ?? T");
        }
        return fromStrFunc.apply(s);
    }

    private static class IndexWrapper
    {
        public int index = 0;
    }

    private void skipSpaces(String bracketStr, IndexWrapper iw)
    {
        while (iw.index < bracketStr.length() && Character.isWhitespace(bracketStr.charAt(iw.index)))
        {
            iw.index++;
        }
    }

    private T readValue(String bracketStr, IndexWrapper iw) throws Exception
    {
        skipSpaces(bracketStr, iw);
        if (iw.index >= bracketStr.length())
        {
            return null;
        }
        int from = iw.index;
        boolean quote = bracketStr.charAt(iw.index) == '"';
        if (quote)
        {
            iw.index++;
        }
        while (iw.index < bracketStr.length() && (
                quote && bracketStr.charAt(iw.index) != '"' ||
                        !quote && !Character.isWhitespace(bracketStr.charAt(iw.index)) && "(),".indexOf(bracketStr.charAt(iw.index)) < 0
        ))
        {
            iw.index++;
        }
        if (quote && bracketStr.charAt(iw.index) == '"')
        {
            iw.index++;
        }
        String valueStr = bracketStr.substring(from, iw.index);
        T value = fromStr(valueStr);
        skipSpaces(bracketStr, iw);
        return value;
    }

    private SimpleTreeNode fromBracketStr(String bracketStr, IndexWrapper iw) throws Exception
    {
        T parentValue = readValue(bracketStr, iw);
        SimpleTreeNode parentNode = new SimpleTreeNode(parentValue);
        if (bracketStr.charAt(iw.index) == '(')
        {
            iw.index++;
            skipSpaces(bracketStr, iw);
            if (bracketStr.charAt(iw.index) != ',')
            {
                parentNode.left = fromBracketStr(bracketStr, iw);
                skipSpaces(bracketStr, iw);
            }
            if (bracketStr.charAt(iw.index) == ',')
            {
                iw.index++;
                skipSpaces(bracketStr, iw);
            }
            if (bracketStr.charAt(iw.index) != ')')
            {
                parentNode.right = fromBracketStr(bracketStr, iw);
                skipSpaces(bracketStr, iw);
            }
            if (bracketStr.charAt(iw.index) != ')')
            {
                throw new Exception(String.format("?????????????????? ')' [%d]", iw.index));
            }
            iw.index++;
        }

        return parentNode;
    }

    public void fromBracketNotation(String bracketStr) throws Exception
    {
        IndexWrapper iw = new IndexWrapper();
        SimpleTreeNode root = fromBracketStr(bracketStr, iw);
        if (iw.index < bracketStr.length())
        {
            throw new Exception(String.format("???????????????? ?????????? ???????????? [%d]", iw.index));
        }
        this.root = root;
    }

    public String toBracketStr()
    {
        return toBracketStr(root);
    }

    private <T> String toBracketStr(IBinaryTree.TreeNode<T> treeNode)
    {
        class Inner
        {
            void printTo(IBinaryTree.TreeNode<T> node, StringBuilder sb)
            {
                if (node == null)
                {
                    return;
                }
                sb.append(node.getValue());
                if (node.getLeft() != null || node.getRight() != null)
                {
                    sb.append(" (");
                    printTo(node.getLeft(), sb);
                    if (node.getRight() != null)
                    {
                        sb.append(", ");
                        printTo(node.getRight(), sb);
                    }
                    sb.append(")");
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        new Inner().printTo(treeNode, sb);
        return sb.toString();
    }
}
