import javax.swing.*;
import java.awt.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GUIWindow extends JFrame {
    JButton button;
    JLabel label;
    JLabel warning;
    JTextField textField;
    String placeHolder = "Ignore me!";
    JLabel choices;
    JLabel applicantNumber;
    JPanel applicantNumberPanel;
    JPanel textFieldPanel;


    public GUIWindow() {

        //
        setUIFont(new javax.swing.plaf.FontUIResource("Helvetica", Font.PLAIN, 14)); // Set default font

        // Set the title of the window
        setTitle("Java GUI Window");

        // Set the size of the window
        setSize(600, 400);

        // Make sure the window closes when the close button is clicked
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a label
        JLabel label = new JLabel("Enter your name:");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setAlignmentX(CENTER_ALIGNMENT);
        Font labelFont = label.getFont();
        label.setFont(new Font(labelFont.getName(), Font.BOLD, 14));
        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel applicantNumber = new JLabel("Applicant: 1");
        applicantNumber.setHorizontalAlignment(SwingConstants.LEFT);
        applicantNumber.setAlignmentX(LEFT_ALIGNMENT);
        Font applicantNumberFont = applicantNumber.getFont();
        applicantNumber.setFont(new Font(applicantNumberFont.getName(), Font.BOLD, 14));
        applicantNumber.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        JPanel applicantNumberPanel = new JPanel();
        applicantNumberPanel.setLayout(new BorderLayout());
        applicantNumberPanel.add(applicantNumber, BorderLayout.CENTER); 
        applicantNumberPanel.setMaximumSize(new Dimension(400, 400));
        applicantNumberPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 40, 200)); // Add padding

        JLabel warning = new JLabel("");
        warning.setHorizontalAlignment(SwingConstants.CENTER);
        warning.setAlignmentX(CENTER_ALIGNMENT);
        Font warningFont = warning.getFont();
        warning.setFont(new Font(warningFont.getName(), Font.BOLD, 18));
        warning.setForeground(Color.RED);

        JLabel choices = new JLabel(
                "<html>" +
                        "<ul>" +
                        "<li>Option 1</li>" +
                        "<li>Option 2</li>" +
                        "<li>Option 3</li>" +
                        "</ul>" +
                        "</html>");
        choices.setHorizontalAlignment(SwingConstants.CENTER);
        choices.setAlignmentX(CENTER_ALIGNMENT);
        Font choicesFont = choices.getFont();
        choices.setFont(new Font(choicesFont.getName(), Font.BOLD, 13));
        choices.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 140));

        // JPanel labelPanel = new JPanel();
        // labelPanel.setLayout(new BorderLayout());
        // labelPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Add padding
        // labelPanel.add(label, BorderLayout.CENTER);
        // labelPanel.setMaximumSize(new Dimension(300, 200));
        // labelPanel.setAlignmentX(CENTER_ALIGNMENT);
        // labelPanel.setAlignmentY(CENTER_ALIGNMENT);

        // text field pannel has rounded corners
        JPanel textFieldPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2d.dispose();
            }
        };

        textFieldPanel.setBackground(Color.WHITE);
        textFieldPanel.setLayout(new BorderLayout());
        textFieldPanel.setMaximumSize(new Dimension(300, 50));
        textFieldPanel.setBorder(new RoundedCornerBorder(10)); // Add padd

        // Create a text field for user input
        JTextField textField = new JTextField();
        textField.setColumns(20);
        textField.setHorizontalAlignment(SwingConstants.LEFT);
        textField.setAlignmentX(CENTER_ALIGNMENT);
        textField.setMaximumSize(textField.getPreferredSize());
        textField.setBorder(BorderFactory.createEmptyBorder());
        // textField.setBorder(new RoundedCornerBorder(10));

        textFieldPanel.add(textField, BorderLayout.CENTER);

        // Add document listener to remove placeholder text when user begins typing
        textField.getDocument().addDocumentListener(new DocumentListener() {
            Boolean begunType = false;

            @Override
            public void insertUpdate(DocumentEvent e) {
                System.out.println(textField.getText());
                // if begun type is false, remove the placeholder text
                if (!begunType) {
                    SwingUtilities.invokeLater(() -> beingTyping());
                }

            }

            @Override
            public void removeUpdate(DocumentEvent e) {
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // No action needed
            }

            public void beingTyping() {
                if (begunType) {
                    return;
                }
                String text = textField.getText();
                if (text == placeHolder | text == "") {
                    return;
                }
                String another = text.replace(placeHolder, "");
                begunType = true;
                textField.setText(another);
                textField.setForeground(Color.BLACK);
            }

            public void emptyField() {
                textField.setText("Enter your name");
                textField.setForeground(Color.GRAY); //
            }
        });


        textField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                // No action needed
            }

            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
               if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                   synchronized (GUIWindow.class) {
                       GUIWindow.class.notify();
                   }
               }
            }

            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                // No action needed
            }
        
        });


        // Create a button
        JButton button = new JButton("Next");
        button.setAlignmentX(CENTER_ALIGNMENT);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Button pressed");
                synchronized (GUIWindow.class) {
                    GUIWindow.class.notify(); // Notify the getInput method that the button has been pressed
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());
        // buttonPanel.setBorder(new RoundedCornerBorder(10)); // Add padding
        buttonPanel.add(button, BorderLayout.CENTER);
        buttonPanel.setMaximumSize(new Dimension(300, 100));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

        // Set the title of the window to the centered label
        setTitle("Java GUI Window");
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
       
        add(Box.createVerticalGlue());
        add(applicantNumberPanel);
        add(label);
        add(choices);
        add(textFieldPanel);
        add(buttonPanel);
        add(Box.createVerticalGlue());
        add(warning);

        // Set the window to be visible
        setVisible(true);

        // Center the window on the screen after it's been packed
        setLocationRelativeTo(null);

        this.button = button;
        this.label = label;
        this.textField = textField;
        this.warning = warning;
        this.choices = choices;
        this.applicantNumber = applicantNumber;
        this.applicantNumberPanel = applicantNumberPanel;
        this.textFieldPanel = textFieldPanel;
    }

    // Custom border class for rounded corners
    public class RoundedCornerBorder implements Border {
        private int radius;

        public RoundedCornerBorder(int radius) {
            this.radius = radius;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setColor(c.getBackground());
            g2d.fillRoundRect(x, y, width - 1, height - 1, radius, radius);

            g2d.setColor(Color.GRAY);
            g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);

            g2d.dispose();
        }

        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius - 2, this.radius - 2, this.radius - 1, this.radius - 1);
        }

        public boolean isBorderOpaque() {
            return true;
        }
    }

    public static void openWindow() {
        // Create an instance of the GUIWindow class
        GUIWindow window = new GUIWindow();
    }

    public static void setUIFont(javax.swing.plaf.FontUIResource f) {
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, f);
            }
        }
    }

    public void setButtonText(String text) {
        button.setText(text);
    }

    public void setLabel(String text) {
        label.setText(text);
    }

    public void setPlaceHolder(String text) {
        textField.setText(text);
        textField.setForeground(Color.GRAY);
        placeHolder = text;
    }

    public void setWarning(String text) {
        warning.setText(text);
    }

    public void setChoices(String text) {
        choices.setText(text);
    }

    public String getInput() {
        // continosly check to see if the user has pressed the button, and don't return
        synchronized (GUIWindow.class) {
            try {
                GUIWindow.class.wait();
                System.out.println("Button pressed here");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return textField.getText();
    }

    public void setApplicantNumber(int number) {
        applicantNumber.setText("Applicant: " + number);
    }

    public void removeElements() {
        // this.remove(label);
        System.out.println("Removing elements");
        this.button.setVisible(false);
        this.warning.setVisible(false);
        this.textField.setVisible(false);
        this.choices.setVisible(false);
        this.applicantNumber.setVisible(false);
        this.applicantNumberPanel.setVisible(false);
        this.textFieldPanel.setVisible(false);
    }

}
