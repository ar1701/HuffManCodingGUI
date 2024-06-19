import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

class HuffmanNode {
    int frequency;
    char data;
    HuffmanNode left, right;
}

class HuffmanComparator implements Comparator<HuffmanNode> {
    public int compare(HuffmanNode x, HuffmanNode y) {
        return x.frequency - y.frequency;
    }
}

public class HuffmanCodingGUI extends JFrame {
    private JTextField inputField;
    private JTextArea outputArea;
    private JTextArea huffmanCodesArea;
    private Map<Character, String> huffmanCodeMap = new HashMap<>();
    private Map<String, Character> reverseHuffmanCodeMap = new HashMap<>();
    private HuffmanNode root;

    public HuffmanCodingGUI() {
        setTitle("Huffman Coding");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JLabel inputLabel = new JLabel("Enter the input string: ");
        panel.add(inputLabel);

        inputField = new JTextField(30);
        panel.add(inputField);

        JButton encodeButton = new JButton("Encode");
        panel.add(encodeButton);

        encodeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String input = inputField.getText();
                if (!input.isEmpty()) {
                    processHuffmanCoding(input);
                }
            }
        });

        add(panel, BorderLayout.NORTH);

        huffmanCodesArea = new JTextArea(10, 50);
        huffmanCodesArea.setEditable(false);
        add(new JScrollPane(huffmanCodesArea), BorderLayout.CENTER);

        outputArea = new JTextArea(5, 50);
        outputArea.setEditable(false);
        add(new JScrollPane(outputArea), BorderLayout.SOUTH);
    }

    private void processHuffmanCoding(String input) {
        // Clear previous results
        huffmanCodesArea.setText("");
        outputArea.setText("");
        huffmanCodeMap.clear();
        reverseHuffmanCodeMap.clear();

        // Calculate the frequency of each character in the string
        Map<Character, Integer> freqMap = new HashMap<>();
        for (char c : input.toCharArray()) {
            freqMap.put(c, freqMap.getOrDefault(c, 0) + 1);
        }

        int n = freqMap.size();
        char[] charArray = new char[n];
        int[] charFreq = new int[n];

        int index = 0;
        for (Map.Entry<Character, Integer> entry : freqMap.entrySet()) {
            charArray[index] = entry.getKey();
            charFreq[index] = entry.getValue();
            index++;
        }

        root = buildHuffmanTree(charArray, charFreq);

        huffmanCodesArea.setText("Character Huffman Codes:\n");
        for (Map.Entry<Character, String> entry : huffmanCodeMap.entrySet()) {
            huffmanCodesArea.append(entry.getKey() + ": " + entry.getValue() + "\n");
        }

        String encodedString = encodeString(input);
        String decodedString = decodeString(root, encodedString);

        outputArea.setText("Encoded string: " + encodedString + "\n");
        outputArea.append("Decoded string: " + decodedString);
    }

    private void buildCodeMap(HuffmanNode root, String s) {
        if (root.left == null && root.right == null && Character.isLetterOrDigit(root.data)) {
            huffmanCodeMap.put(root.data, s);
            reverseHuffmanCodeMap.put(s, root.data);
            return;
        }
        if (root.left != null) {
            buildCodeMap(root.left, s + "0");
        }
        if (root.right != null) {
            buildCodeMap(root.right, s + "1");
        }
    }

    private HuffmanNode buildHuffmanTree(char[] charArray, int[] charFreq) {
        int n = charArray.length;
        PriorityQueue<HuffmanNode> q = new PriorityQueue<>(n, new HuffmanComparator());

        for (int i = 0; i < n; i++) {
            HuffmanNode hn = new HuffmanNode();
            hn.data = charArray[i];
            hn.frequency = charFreq[i];
            hn.left = null;
            hn.right = null;
            q.add(hn);
        }

        HuffmanNode root = null;

        while (q.size() > 1) {
            HuffmanNode x = q.poll();
            HuffmanNode y = q.poll();

            HuffmanNode f = new HuffmanNode();
            f.frequency = x.frequency + y.frequency;
            f.data = '-';
            f.left = x;
            f.right = y;
            root = f;
            q.add(f);
        }

        buildCodeMap(root, "");
        return root;
    }

    private String encodeString(String input) {
        StringBuilder encodedString = new StringBuilder();
        for (char c : input.toCharArray()) {
            encodedString.append(huffmanCodeMap.get(c));
        }
        return encodedString.toString();
    }

    private String decodeString(HuffmanNode root, String encodedString) {
        StringBuilder decodedString = new StringBuilder();
        HuffmanNode currentNode = root;
        for (char bit : encodedString.toCharArray()) {
            if (bit == '0') {
                currentNode = currentNode.left;
            } else {
                currentNode = currentNode.right;
            }
            if (currentNode.left == null && currentNode.right == null) {
                decodedString.append(currentNode.data);
                currentNode = root;
            }
        }
        return decodedString.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new HuffmanCodingGUI().setVisible(true);
            }
        });
    }
}
