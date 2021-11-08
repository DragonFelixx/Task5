package company.vel.gui;

import company.vel.binaryTree.SimpleBinaryTree;
import company.vel.utils.BinaryTreePainter;
import company.vel.utils.SwingUtils;
import company.vel.binaryTree.*;
import company.vel.utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Frame extends JFrame
{
    private JPanel rootPanel;
    private JLabel textInput;
    private JTextField textFieldBracketNotationTree;
    private JButton buttonBuildTree;
    private JButton buttonRemoveSingleNode;
    private JPanel paintPanel;
    private JPanel panel;
    private JPanel panelButtons;
    private JLabel labelImage;
    private JPanel panelPaintArea;
    private JScrollPane scrollPane;
    private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    SimpleBinaryTree<Integer> tree = new SimpleBinaryTree<>();
    BufferedImage image = new BufferedImage(screenSize.width, screenSize.height, BufferedImage.TYPE_INT_BGR);
    Graphics2D g2dRoot = image.createGraphics();

    public Frame()
    {
        this.setTitle("Binary Tree");
        rootPanel = new JPanel();
        panel = new JPanel();
        panelButtons = new JPanel();
        paintPanel = new JPanel();
        labelImage = new JLabel();
        panelPaintArea = new JPanel();
        textInput = new JLabel("Enter tree in bracket notation");
        textFieldBracketNotationTree = new JTextField("6 (3 (2 (1), 5), 8 (7, 16 (15 (12 (9)))))");
        buttonBuildTree = new JButton("Build tree");
        buttonRemoveSingleNode = new JButton("Remove single node");
        textFieldBracketNotationTree.setPreferredSize(new Dimension(300, 25));
        scrollPane = new JScrollPane(paintPanel);
        scrollPane.setPreferredSize(new Dimension(screenSize.width - 100, screenSize.height - 100));
        buttonBuildTree.addActionListener(e ->
        {
            tree = new SimpleBinaryTree<>(Integer::parseInt);
            try
            {
                tree.fromBracketNotation(textFieldBracketNotationTree.getText());
            } catch (Exception exception)
            {
                exception.printStackTrace();
                SwingUtils.showErrorMessageBox(exception);
            }
            panelPaintArea.add(scrollPane);
            repaintTree();
        });

        buttonRemoveSingleNode.addActionListener(e ->
        {
            tree.deleteSingleNodes();
            repaintTree();
        });

        addComponentsToPanel();
        initFrame();
    }

    private void repaintTree()
    {
        Dimension paintSize = BinaryTreePainter.paint(tree, g2dRoot);
        int width = paintSize.width;
        int height = paintSize.height;
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);
        BinaryTreePainter.paint(tree, g2d);
        labelImage.setIcon(new ImageIcon(image));
        paintPanel.add(labelImage);
        panelPaintArea.revalidate();
    }

    private void addComponentsToPanel()
    {
        panel.add(textInput);
        panel.add(textFieldBracketNotationTree);
        panelButtons.add(buttonBuildTree, BorderLayout.NORTH);
        panelButtons.add(buttonRemoveSingleNode, BorderLayout.NORTH);
        rootPanel.add(panel);
        rootPanel.add(panelButtons);
        rootPanel.add(panelPaintArea);
    }

    private void initFrame()
    {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(rootPanel);
        int width = screenSize.width - 200;
        int height = screenSize.height - 200;
        int x = (screenSize.width - width) / 2;
        int y = (screenSize.height - height) / 2;
        this.setBounds(x, y, width, height);
    }
}