import java.util.ArrayDeque;
import java.util.Deque;

/**
 * TextAnalyzer: a small class of static helper methods that check simple
 * properties of a String, some of them using a stack.
 *
 * A stack only allows access at one end (the "top"): push adds, pop
 * removes and returns the most recently pushed item. This class uses
 * java.util.ArrayDeque as the stack, through the Deque interface --
 * NOT java.util.Stack. java.util.Stack is an older class kept in Java
 * for backward compatibility: it is synchronized (overhead most programs
 * do not need), and because it is also a List it allows indexed access,
 * which breaks the "top only" idea a stack is supposed to guarantee.
 * ArrayDeque is the class Java's own documentation recommends instead.
 */
public class TextAnalyzer {

    /**
     * TODO: Return true if input reads the same forwards and backwards,
     * ignoring letter case and ignoring spaces.
     *
     * Examples:
     *   isPalindrome("Race Car")            -> true
     *   isPalindrome("Never odd or even")   -> true
     *   isPalindrome("ab ca")               -> false
     *
     * You MUST use a Deque<Character> as a stack in your solution:
     *   Deque<Character> stack = new ArrayDeque<>();
     *   stack.push(c);   // add on top
     *   stack.pop();     // remove and return the top
     *
     * Approach:
     *   1. Normalize the string: lower case, with spaces removed (this
     *      part is already done for you below).
     *   2. Push every character of the normalized string onto the stack.
     *   3. Pop characters back off one at a time -- since a stack is
     *      LIFO, they come off in reverse order -- and compare each
     *      popped character to the matching character of the normalized
     *      string, read from the front.
     *   4. If every comparison matches, the string is a palindrome.
     */
    public static boolean isPalindrome(String input) {
        String normalized = input.toLowerCase().replaceAll("\\s+", "");
        Deque<Character> stack = new ArrayDeque<>();
        //loop through normalized string, first to last
        //push onto stack with charAt
        for (int i = 0; i < normalized.length(); i++){
            stack.push(normalized.charAt(i));
        }
        //loop through normalized string, front to back
        //pop from stack with charAt
        for (int i = 0; i < normalized.length(); i++){
            if (!stack.pop().equals(normalized.charAt(i))){
                return false
            }
        }
        // TODO: implement using the stack described above
        return true;
    }
}
