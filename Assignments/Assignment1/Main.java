/*********************************************************
 *
 * DO NOT MODIFY THIS FILE.
 *
 * This is the driver used to test the classes you complete for
 * Assignment 1: SequenceList.java / LinkedSequenceList.java (Part 1),
 * TextAnalyzer.java (Part 2), and TicketQueue.java (Part 3).
 *
 * Part 4 (DesignAnalysis.java) is graded separately by reading your
 * answers and justifications -- it is not tested here.
 *
 *********************************************************/
// Shú Perez
import java.util.NoSuchElementException;

public class Main {
    public static void main(String[] args) {
        int assignmentScore = 0;
        boolean errorFlag;

        System.out.println("Assignment 1 testing driver starting ...\n");

        /**************************************
         * Part 1 -- LinkedSequenceList
         **************************************/

        LinkedSequenceList<String> sequence = new LinkedSequenceList<>();
        sequence.add("A");
        sequence.add("B");
        sequence.add("C");
        sequence.add("D");
        // sequence is now [A, B, C, D]

        errorFlag = false;
        sequence.add(2, "X"); // expected: [A, B, X, C, D]
        if (!sequence.toString().equals("[A, B, X, C, D]")) {
            System.out.println("ERROR 1: add(int, T) test failed. Got: " + sequence);
            errorFlag = true;
        }
        if (!errorFlag) assignmentScore += 10;

        errorFlag = false;
        String removed = sequence.remove(0); // expected: removed "A", list [B, X, C, D]
        if (removed == null || !removed.equals("A") || !sequence.toString().equals("[B, X, C, D]")) {
            System.out.println("ERROR 2: remove(int) test failed. Got removed=" + removed + ", list=" + sequence);
            errorFlag = true;
        }
        if (!errorFlag) assignmentScore += 10;

        errorFlag = false;
        sequence.reverseInPlace(); // [B, X, C, D] -> [D, C, X, B]
        if (!sequence.toString().equals("[D, C, X, B]")) {
            System.out.println("ERROR 3: reverseInPlace() test failed. Got: " + sequence);
            errorFlag = true;
        }
        if (!errorFlag) assignmentScore += 10;

        /**************************************
         * Part 2 -- TextAnalyzer.isPalindrome
         **************************************/

        errorFlag = false;
        if (!TextAnalyzer.isPalindrome("Was it a car or a cat I saw")) {
            System.out.println("ERROR 4: isPalindrome test failed on a true case");
            errorFlag = true;
        }
        if (!errorFlag && !TextAnalyzer.isPalindrome("Race Car")) {
            System.out.println("ERROR 5: isPalindrome test failed on a true case");
            errorFlag = true;
        }
        if (!errorFlag && !TextAnalyzer.isPalindrome("Never odd or even")) {
            System.out.println("ERROR 6: isPalindrome test failed on a true case");
            errorFlag = true;
        }
        if (!errorFlag && TextAnalyzer.isPalindrome("civic2")) {
            System.out.println("ERROR 7: isPalindrome test failed on a false case");
            errorFlag = true;
        }
        if (!errorFlag) assignmentScore += 20;

        /**************************************
         * Part 3 -- TicketQueue
         **************************************/

        errorFlag = false;
        TicketQueue ticketQueue = new TicketQueue();
        ticketQueue.addCustomer("C1");
        ticketQueue.addCustomer("C2");
        ticketQueue.addCustomer("C3");

        String served = ticketQueue.serveNext(); // expected "C1"
        if (served == null || !served.equals("C1") || ticketQueue.getServedCount() != 1
                || ticketQueue.size() != 2) {
            System.out.println("ERROR 8: serveNext() test failed. Got served=" + served);
            errorFlag = true;
        }
        if (!errorFlag) {
            try {
                new TicketQueue().serveNext();
                System.out.println("ERROR 9: serveNext() on an empty queue should throw NoSuchElementException");
                errorFlag = true;
            } catch (NoSuchElementException expected) {
                // correct behavior
            } catch (Throwable t) {
                System.out.println("ERROR 9: serveNext() on an empty queue threw the wrong type: " + t);
                errorFlag = true;
            }
        }
        if (!errorFlag) assignmentScore += 10;

        errorFlag = false;
        boolean waitingC3 = ticketQueue.isCustomerWaiting("C3");
        boolean waitingC1 = ticketQueue.isCustomerWaiting("C1"); // already served, should be false
        if (!waitingC3 || waitingC1 || ticketQueue.size() != 2) {
            System.out.println("ERROR 10: isCustomerWaiting(...) test failed");
            errorFlag = true;
        }
        if (!errorFlag) assignmentScore += 10;

        /**************************************
         * Part 4 -- DesignAnalysis (not auto-tested)
         **************************************/

        System.out.println("\nPart 4 (DesignAnalysis) is graded from your answers and justifications,");
        System.out.println("not tested here (30 points, added to your score separately).");

        System.out.println("\nTesting complete. Assignment 1 code score is: " + assignmentScore + " / 70");
    }
}
