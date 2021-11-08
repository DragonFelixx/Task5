package company.vel.binaryTree;

import java.awt.*;

public interface IBinaryTree<T>
{
    interface TreeNode<T>
    {
        T getValue();

        default TreeNode<T> getLeft()
        {
            return null;
        }

        default TreeNode<T> getRight()
        {
            return null;
        }

        default Color getColor()
        {
            return Color.BLACK;
        }
    }
    TreeNode<T> getRoot();
}