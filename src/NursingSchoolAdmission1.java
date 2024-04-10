

import java.util.*;
import java.util.stream.Collectors;

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
        double score;
        String answer;
        int layer;
        boolean numberNode = false;
        boolean stringNode = false;
        Map<Double, Node> numberMap = new HashMap<>();
        Map<String, Node> stringMap = new HashMap<>();

        static String[] questions = {
                "what is your current GPA?",
                "what is your TEAS score?",
                "have you completed the required science prerequisites?",
                "have you completed the required English prerequisites?",
                "Have you attained a Bachelor's Degree?",
                "How many years of work experience do you have?"
        };

        public Node(double score, int layer) {
            this.score = score;
            this.layer = layer;
        }

        public Node(double score, String answer, int layer) {
            this.score = score;
            this.answer = answer;
            this.layer = layer;
        }

        public void addNumberOptions(Node[] nodes) {
            if (stringNode) {
                System.out.println("Cannot add number options to a string node");
                return;
            }
            numberNode = true;
            for (Node node : nodes) {
                numberMap.put(node.score, node);
            }
        }

        public void addStringOptions(Node[] nodes) {
            if (numberNode) {
                System.out.println("Cannot add string options to a number node");
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

        public void printQuestion() {
            System.out.println(questions[layer]);
        }

        public void printChoices() {
            if (numberNode) {
                System.out.println("Choices:");
                Double[] keys = numberMap.keySet().toArray(new Double[0]);
                Arrays.sort(keys);
                for (double key : keys) {
                    System.out.println("-> " + key);
                }
                System.out.print("Enter your choice: ");
            } else if (stringNode) {
                System.out.println("Choices: ");
                stringMap.keySet().forEach(key -> System.out.println("-> " + key));
                System.out.print("Enter your choice: ");
            }
        }

        public Node getNextNode(Scanner scanner) {
            if (numberNode) {
                double scoreDouble;
                while (true) {
                    try {
                        scoreDouble = Double.parseDouble(scanner.nextLine());
                        if (numberMap.containsKey(scoreDouble)) {
                            break; // Break the loop if input is valid
                        } else {
                            System.out.println("Invalid choice. Please enter a valid choice.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a number.");
                    }
                }
                return numberMap.get(scoreDouble);
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
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Applicant> applicants = new ArrayList<>();
        int applicantCounter = 1; // Counter for applicants

        while (applicantCounter <= 5) {
            System.out.println("Enter applicant's name: ");
            String name = scanner.nextLine();
            double ranking = 0;

            Node root = new Node(0, 0); // Root node doesn't contribute to score

            // Define GPA Nodes
            Node gpaNodes[] = {
                    new Node(3.5, 1),
                    new Node(3.6, 1),
                    new Node(3.7, 1),
                    new Node(3.8, 1),
                    new Node(3.9, 1),
                    new Node(4.0, 1),
            };

            // Define TEAS Nodes
            Node teasNodes[] = {
                    new Node(1.0, 2),
                    new Node(2.0, 2),
                    new Node(3.0, 2),
                    new Node(4.0, 2),
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
                    new Node(0, "0", 6),
                    new Node(1, "1-2", 6), // Incremental points for more experience
                    new Node(2, "3-4", 6),
                    new Node(3, "5+", 6),
            };

            // Linking the nodes
            for (int i = 0; i < bachelorsDegreeNodes.length; i++) {
                bachelorsDegreeNodes[i].addStringOptions(workExperienceNodes);
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
                gpaNodes[i].addNumberOptions(teasNodes);
            }

            root.addNumberOptions(gpaNodes);

            Node nextNode = root;
            for (int i = 0; i < Node.questions.length; i++) {
                nextNode.printQuestion();
                nextNode.printChoices();
                nextNode = nextNode.getNextNode(scanner);
                if (nextNode == null) {
                    System.out.println("Invalid choice for " + Node.questions[i] + ". Please try again.");
                    i--; // Decrement to ask the same question again
                } else {
                    ranking += nextNode.score; // Update ranking calculation to sum
                }
            }

            applicants.add(new Applicant(name, ranking));
            applicantCounter++; // Increment the applicant counter
        }

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
