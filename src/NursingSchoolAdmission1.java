
import java.util.*;
import java.util.stream.Collectors;

import javax.swing.JButton;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class NursingSchoolAdmission1 {

    static class Applicant {
        String name;
        double ranking;

        public Applicant(String name, double ranking) {
            this.name = name;
            this.ranking = ranking;
        }
    }

    static class Node {
        String answer;
        int layer;
        boolean rangedNode = false;
        boolean stringNode = false;
        Map<String, Node> stringMap = new HashMap<>();
        Node[] numberNodes;
        double maxRange = -100000;
        double minRange = 100000;
        double low;
        double high;
        double score;

        static String[] questions = {
                "what is your current GPA?",
                "what is your TEAS score?",
                "have you completed the required science prerequisites?",
                "have you completed the required English prerequisites?",
                "Have you attained a Bachelor's Degree?",
                "How many years of work experience do you have?"
        };

        public Node(double score, String answer, int layer) {
            this.score = score;
            this.answer = answer;
            this.layer = layer;
        }

        public Node(double low, double high, double score, int layer) {
            this.layer = layer;
            this.low = low;
            this.high = high;
            this.score = score;
        }

        public void addStringOptions(Node[] nodes) {
            if (rangedNode) {
                System.out.println("Cannot add string options to a ranged node");
                return;
            }
            stringNode = true;
            for (Node node : nodes) {
                stringMap.put(node.answer, node);
            }
            if (stringNode) {
                // Ensure the choices are sorted in a specific order for string nodes
                stringMap = stringMap.entrySet().stream()
                        .sorted(Map.Entry.comparingByKey())
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (oldValue, newValue) -> oldValue, LinkedHashMap::new));
            }
        }

        public void addRangedOptions(Node[] nodes) {
            if (stringNode) {
                System.out.println("Cannot add ranged options to a string node");
                return;
            }
            rangedNode = true;
            numberNodes = nodes;
            for (Node node : nodes) {
                if (node.high > maxRange) {
                    maxRange = node.high;
                }
                if (node.low < minRange) {
                    minRange = node.low;
                }
            }
        }

        public String printQuestion() {
            System.out.println(questions[layer]);
            return questions[layer];
        }

        public String printChoices() {
            if (rangedNode) {
                System.out.print("Enter a number(" + this.minRange + ", " + this.maxRange + "): ");
                String choices = "Enter a number(" + this.minRange + ", " + this.maxRange + "): ";
                return choices;
            } else if (stringNode) {
                System.out.println("Choices: ");
                stringMap.keySet().forEach(key -> System.out.println("-> " + key));
                System.out.print("Enter your choice: ");
                String choices = "<html><ul>";
                for (String key : stringMap.keySet()) {
                    choices += "<li>" + key + "</li>";
                }
                choices += "</ul></html> ";
                return choices;
            }
            return null;
        }

        public Node getNextNode(Scanner scanner) {
            if (rangedNode) {
                double scoreDouble;
                while (true) {
                    try {
                        scoreDouble = Double.parseDouble(scanner.nextLine());
                        // make sure it is withing min and max range
                        if (scoreDouble >= minRange && scoreDouble <= maxRange) {
                            for (Node node : numberNodes) {
                                if (scoreDouble >= node.low && scoreDouble <= node.high) {
                                    return node;
                                }
                            }
                        } else {
                            System.out.println("Invalid input. Please enter a number within the range.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a number.");
                    }
                }
            } else if (stringNode) {
                String answer = scanner.nextLine().toLowerCase();
                while (!stringMap.containsKey(answer)) {
                    System.out.println("Invalid choice. Please enter a valid choice.");
                    answer = scanner.nextLine().toLowerCase();
                }
                return stringMap.get(answer);
            }
            return null;
        }
        public Node getNextNode(GUIWindow window) {
            if (rangedNode) {
                double scoreDouble;
                while (true) {
                    try {
                        scoreDouble = Double.parseDouble(window.getInput());
                        // make sure it is withing min and max range
                        if (scoreDouble >= minRange && scoreDouble <= maxRange) {
                            for (Node node : numberNodes) {
                                if (scoreDouble >= node.low && scoreDouble <= node.high) {
                                    return node;
                                }
                            }
                        } else {
                            System.out.println("Invalid input. Please enter a number within the range.");
                            window.setWarning("Invalid input. Please enter a number within the range.");

                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a number.");
                        window.setWarning("Invalid input. Please enter a number.");
                    }
                }
            } else if (stringNode) {
                String answer = window.getInput().toLowerCase();
                while (!stringMap.containsKey(answer)) {
                    System.out.println("Invalid choice. Please enter a valid choice.");
                    window.setWarning("Invalid choice. Please enter a valid choice.");
                    answer = window.getInput();
                }
                return stringMap.get(answer);
            }
            return null;
        }
    }

    public static void main(String[] args) {
        // mainLoop();

        GUIWindow wind = new GUIWindow();

        mainLoop(wind);

        // wind.removeElements();

        // String htmlTable = "<html>\n" +
        // "    <table border=\"1\">\n" +
        // "        <tr>\n" +
        // "            <th>Applicant</th>\n" +
        // "            <th>Name</th>\n" +
        // "            <th>Ranking</th>\n" +
        // "            <th>Eligibility</th>\n" +
        // "        </tr>\n" +
        // "        <tr>\n" +
        // "            <td>Applicant 1</td>\n" +
        // "            <td>sdfds</td>\n" +
        // "            <td>17.5</td>\n" +
        // "            <td>Very eligible</td>\n" +
        // "        </tr>\n" +
        // "    </table>\n" +
        // "</html>";


        // wind.setLabel(htmlTable);

        

        


    }

    public static void mainLoop(GUIWindow window) {

        List<Applicant> applicants = new ArrayList<>();
        int applicantCounter = 1; // Counter for applicants
        Node root = initializeNodes();

        while (applicantCounter <= 3) {
            window.setApplicantNumber(applicantCounter);
            System.out.println("Enter your name: ");
            window.setLabel("Enter your name:");
            window.setButtonText("Next");
            window.setPlaceHolder("");
            window.setChoices("Enter your name:");
            String name = window.getInput();
            double ranking = 0;
            Node nextNode = root;
            for (int i = 0; i < Node.questions.length; i++) {
                window.setPlaceHolder("");
                window.setWarning("");
                String question = nextNode.printQuestion();
                window.setLabel(question);
                String choices = nextNode.printChoices();
                window.setChoices(choices);
                nextNode = nextNode.getNextNode(window);
                if (nextNode == null) {
                    System.out.println("Invalid choice for " + Node.questions[i] + ". Please try again.");
                    window.setWarning("Invalid choice for " + Node.questions[i] + ". Please try again.");
                    i--; // Decrement to ask the same question again
                } else {
                    ranking += nextNode.score; // Update ranking calculation to sum
                }
            }

            applicants.add(new Applicant(name, ranking));
            applicantCounter++; // Increment the applicant counter
        }

        //remove all gui elements
        window.removeElements();

        // Sort applicants based on ranking
        applicants.sort(Comparator.comparingDouble(a -> -a.ranking));

        // Print summary
        System.out.println("Applicant\tRanking\tEligibility");
        int counter = 1;
        for (Applicant applicant : applicants) {
            System.out.printf("Applicant %d\t%-10s\t%-8.1f\t%s%n", counter, applicant.name, applicant.ranking,
                    getEligibilityStatus(applicant.ranking));
            counter++;
        }

        // Generate HTML table
        String htmlTable = generateHTMLTable(applicants.toArray(new Applicant[applicants.size()]));
        window.setLabel(htmlTable);
    }

    public static String generateHTMLTable(Applicant[] applicants){
        String htmlTable = "<html>\n" +
        "    <table border=\"1\">\n" +
        "        <tr>\n" +
        "            <th>Applicant</th>\n" +
        "            <th>Name</th>\n" +
        "            <th>Ranking</th>\n" +
        "            <th>Eligibility</th>\n" +
        "        </tr>\n";
        for (int i = 0; i < applicants.length; i++) {
            htmlTable += "        <tr>\n" +
            "            <td>Applicant " + (i + 1) + "</td>\n" +
            "            <td>" + applicants[i].name + "</td>\n" +
            "            <td>" + applicants[i].ranking + "</td>\n" +
            "            <td>" + getEligibilityStatus(applicants[i].ranking) + "</td>\n" +
            "        </tr>\n";
        }
        htmlTable += "    </table>\n" +
        "</html>";
        return htmlTable;
    }

    public static Node initializeNodes() {
        Node root = new Node(0, "0", 0); // Root node doesn't contribute to score

        // Define GPA Nodes
        Node gpaNodes[] = {
                new Node(0.0, 1.0, 0.5, 1), // Incremental points for higher scores
                new Node(1.1, 2.0, 1.5, 1),
                new Node(2.1, 3.0, 2.5, 1),
                new Node(3.1, 4.0, 3.5, 1),
                new Node(4.1, 5.0, 4.5, 1),
        };

        // Define TEAS Nodes
        Node teasNodes[] = {
                new Node(1.0, 2.0, 1.5, 2), // Incremental points for higher scores
                new Node(2.0, 3.0, 2.5, 2),
                new Node(3.0, 4.0, 3.5, 2),
                new Node(4.0, 5.0, 4.5, 2),
        };

        // Define Prerequisite Nodes
        Node sciencePreReqs[] = {
                new Node(1, "yes", 3),
                new Node(0, "no", 3),
        };
        Node englishPreReqs[] = {
                new Node(1, "yes", 4),
                new Node(0, "no", 4),
        };

        // Define Bachelor's Degree Nodes
        Node bachelorsDegreeNodes[] = {
                new Node(2, "yes", 5), // More points for having a degree
                new Node(0, "no", 5),
        };

        // Define Work Experience Nodes
        Node workExperienceNodes[] = {
                new Node(0.0, 0.9, .5, 6), // Incremental points for more experience
                new Node(1.0, 1.9, 1.5, 6),
                new Node(2.0, 2.9, 2.5, 6),
                new Node(3.0, 3.9, 3.5, 6),
                new Node(4.0, 5.0, 4.5, 6),
                new Node(5.0, 6.0, 5.5, 6),
        };

        // Linking the nodes
        for (int i = 0; i < bachelorsDegreeNodes.length; i++) {
            bachelorsDegreeNodes[i].addRangedOptions(workExperienceNodes);
        }
        for (int i = 0; i < englishPreReqs.length; i++) {
            englishPreReqs[i].addStringOptions(bachelorsDegreeNodes);
        }

        for (int i = 0; i < sciencePreReqs.length; i++) {
            sciencePreReqs[i].addStringOptions(englishPreReqs);
        }

        for (int i = 0; i < teasNodes.length; i++) {
            teasNodes[i].addStringOptions(sciencePreReqs);
        }

        for (int i = 0; i < gpaNodes.length; i++) {
            gpaNodes[i].addRangedOptions(teasNodes);
        }

        root.addRangedOptions(gpaNodes);

        return root;
    }

    // Method to determine eligibility status based on ranking
    private static String getEligibilityStatus(double ranking) {
        if (ranking >= 15) {
            return "Very eligible";
        } else if (ranking >= 12) {
            return "Eligible";
        } else if (ranking >= 8) {
            return "Somewhat eligible";
        } else {
            return "Not eligible";
        }
    }
}
