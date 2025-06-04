import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import javax.swing.*;

public class Chatbot extends JFrame {
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;

    private HashMap<String, String> faqResponses;

    public Chatbot() {
        setTitle("AI Chatbot");
        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(chatArea);

        inputField = new JTextField();
        sendButton = new JButton("Send");

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(inputField, BorderLayout.CENTER);
        panel.add(sendButton, BorderLayout.EAST);

        add(scrollPane, BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);

        // Rule-based FAQ responses
        faqResponses = new HashMap<>();
        faqResponses.put("hi", "Hello! How can I help you?");
        faqResponses.put("hello", "Hi there! What can I do for you?");
        faqResponses.put("how are you", "I'm just code, but I'm running great!");
        faqResponses.put("what is your name", "I'm your AI chatbot.");
        faqResponses.put("bye", "Goodbye! Have a great day!");
        faqResponses.put("help", "You can ask me things like 'What is your name?' or 'How are you?'");

        ActionListener sendAction = e -> processInput();

        sendButton.addActionListener(sendAction);
        inputField.addActionListener(sendAction);

        setVisible(true);
    }

    private void processInput() {
        String input = inputField.getText().trim();
        if (input.isEmpty()) return;

        chatArea.append("You: " + input + "\n");
        inputField.setText("");

        String response = getResponse(input.toLowerCase());
        chatArea.append("Bot: " + response + "\n");
    }

    private String getResponse(String input) {
        // Simple NLP preprocessing: remove punctuation, lower case
        input = input.replaceAll("[^a-z0-9 ]", "");

        // Check keywords in input
        for (String key : faqResponses.keySet()) {
            if (input.contains(key)) {
                return faqResponses.get(key);
            }
        }

        // Default response if no keyword matches
        return "Sorry, I didn't understand that. Could you please rephrase?";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Chatbot::new);
    }
}

