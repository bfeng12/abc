/**
 * The PackageStack class is an object which contains the name of the recipient,
 * the date of arrival of the package, and its weight in pounds.
 * @author Benny Feng
 * 	email: benny.feng@stonybrook.edu
 * 	SBU ID: 111053634
 * 	HW#3 CSE-214 R04 Michael Alvin
 */
import java.util.*;
public class PackageStack {
	private final int CAPACITY = 7;
	private boolean infinite;
	private ArrayList<Package> stack = new ArrayList<Package>();
	
	public PackageStack(boolean infinite) {
		this.infinite = infinite;
	}
	/**
	 * Adds a new Package to the stack
	 * @param x
	 * 	The Package to add to the stack
	 * @throws FullStackException
	 * 	Exception thrown if the Stack is at capacity
	 */
	public void push(Package x) throws FullStackException {
		if(stack.size() >= CAPACITY && !infinite)
			throw new FullStackException();
		stack.add(x);
	}
	/**
	 * Removes top Package on the Stack
	 * @return
	 * 	Returns removed Package
	 * @throws EmptyStackException
	 * 	Exception thrown if the Stack is empty
	 */
	public Package pop() throws EmptyStackException {
		if(stack.size() == 0)
			throw new EmptyStackException();
		return stack.remove(stack.size()-1);
	}
	/**
	 * Returns the top Package on the Stack
	 * @return
	 * 	Returns the top Package
	 * @throws EmptyStackException
	 * 	Exception thrown if the Stack is empty
	 */
	public Package peek() throws EmptyStackException {
		if(stack.size() == 0)
			throw new EmptyStackException();
		return stack.get(stack.size()-1);
	}
	/**
	 * Checks if the Stack is at capacity
	 * @return
	 * 	Returns true stack is at capacity, false if not
	 */
	public boolean IsFull() {
		if(stack.size() >= 7 && !infinite)
			return true;
		return false;
	}
	/**
	 * Checks if the Stack is empty
	 * @return
	 * 	Returns true if stack is empty, false if not
	 */
	public boolean IsEmpty() {
		if(stack.size() == 0)
			return true;
		return false;
	}
}
