/**
 * The MailroomManager class is the main UI for the mail room. This class creates 6 different PackageStack classes and
 * organizes the Packages alphabetically into these stacks based on the recipient's name. The sixth stack will be used 
 * as the floor stack used to move the packages to the recipients.
 * @author Benny Feng
 * 	email: benny.feng@stonybrook.edu
 * 	SBU ID: 111053634
 * 	HW#3 CSE-214 R04 Michael Alvin
 */
import java.util.*;
public class MailroomManager {
	/**
	 * Main method for the MailroomManager class
	 * @param args
	 * 	Unused
	 */
	public static void main(String[] args) {
		PackageStack stack1 = new PackageStack(false);
		PackageStack stack2 = new PackageStack(false);
		PackageStack stack3 = new PackageStack(false);
		PackageStack stack4 = new PackageStack(false);
		PackageStack stack5 = new PackageStack(false);
		PackageStack floor = new PackageStack(true);

		displayMenu(stack1,stack2,stack3,stack4,stack5,floor);
	}
	/**
	 * Display a menu of options for the user
	 * @param stack1
	 * 	Stack1 passed from main
	 * @param stack2
	 * 	Stack2 passed from main
	 * @param stack3
	 * 	Stack3 passed from main
	 * @param stack4
	 * 	Stack 4 passed from main
	 * @param stack5
	 * 	Stack 5 passed from main
	 * @param floor
	 * 	Floor stack passed from main
	 */
	public static void displayMenu(PackageStack stack1, PackageStack stack2, PackageStack stack3,  
	  PackageStack stack4, PackageStack stack5, PackageStack floor) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Welcome to the Irving Mailroom Manager. You can try to make it better,"
		 + "\nbut the odds are stacked against you. It is day 0.");
		System.out.println("Menu:");
		System.out.println("     D) Deliver a package"
		  + "\n     G) Get someone's package"
		  + "\n     T) Make it tomorrow" 
		  + "\n     P) Print the stacks"
		  + "\n     M) Move a package from one stack to another"
		  + "\n     F) Find packages in the wrong stack and move to floor"
		  + "\n     L) List all packages awaiting a user"
		  + "\n     E) Empty the floor."
		  + "\n     Q) Quit");
		boolean remainOn = true;
		int day = 0;
		String option = "";
		
		while(remainOn){
			System.out.print("Please select a menu option: ");
			option = scanner.nextLine();
			
			switch(option.toLowerCase()) {
				case "d":
					sortPackage(deliverPackage(day),stack1,stack2,stack3,stack4,stack5,floor);
					break;
				case "g":
					getPackage(stack1,stack2,stack3,stack4,stack5,floor);
					break;
				case "t":
					day++;
					System.out.println("It is now day " + day);
					int removed = removeOldPackages(day,stack1,stack2,stack3,stack4,stack5,floor);
					if(removed > 0)
						System.out.println(removed + " package(s) returned to sender "
						  + "to sender");
					break;
				case "p":
					printStack(stack1,stack2,stack3,stack4,stack5,floor);
					break;
				case "m":
					movePackage(stack1,stack2,stack3,stack4,stack5,floor);
					break;
				case "f":
					organizePackages(stack1,stack2,stack3,stack4,stack5,floor);
					System.out.println("Misplaced packages moved to floor.");
					break;
				case "l":
					listPackages(stack1,stack2,stack3,stack4,stack5,floor);
					break;
				case "e":
					while(!floor.IsEmpty()) {
						try {
							floor.pop();
						} catch (EmptyStackException e) {
							//unreachable
						}
					}
					System.out.println("Floor has been cleared. The trash can is satisfied.");
					break;
				case "q":
					System.out.println("Adios amigo. Use Amazon Lockers next time.");
					System.exit(0);
					break;
				default:
					System.out.println("Invalid Input. Please re-enter. default case");
					break;
			}
		}
	}
	/**
	 * Prints current PackageStacks as formatted table
	 * @param stack1
	 * 	Stack1 passed from displayMenu
	 * @param stack2
	 *  Stack2 passed from displayMenu
	 * @param stack3
	 *  Stack3 passed from displayMenu
	 * @param stack4
	 *  Stack4 passed from displayMenu
	 * @param stack5
	 *  Stack5 passed from displayMenu
	 * @param floor
	 *  Floor stack passed from displayMenu
	 */
	public static void printStack(PackageStack stack1,PackageStack stack2,PackageStack stack3,
	  PackageStack stack4,PackageStack stack5,PackageStack floor) {
		System.out.println("Current Packages: ");
		System.out.println("---------------------------------");
		System.out.print("Stack 1 (A-G):|");
		printStackHelper(stack1);
		System.out.print("Stack 2 (H-J):|");
		printStackHelper(stack2);
		System.out.print("Stack 3 (K-M):|");
		printStackHelper(stack3);
		System.out.print("Stack 4 (N-R):|");
		printStackHelper(stack4);
		System.out.print("Stack 5 (S-Z):|");
		printStackHelper(stack5);
		System.out.print("Floor: |");
		printStackHelper(floor);
		System.out.println();
	}
	/**
	 * Prints each Package in a PackageStack
	 */
	public static void printStackHelper(PackageStack stack) {
		if(stack.IsEmpty()) {
			System.out.print("empty.");
			System.out.println();
			return;
		}
		String packages = "";
		PackageStack temp = new PackageStack(false);
		while(!stack.IsEmpty()) {
			try {
				packages = stack.peek() + " " + packages;
				temp.push(stack.pop());
			} catch (FullStackException | EmptyStackException e) {
				System.out.println("error");
			}
		}
		while(!temp.IsEmpty()) {
			try {
				stack.push(temp.pop());
			} catch (FullStackException | EmptyStackException e) {
				System.out.println("error");
			}
		}
		System.out.print(packages);
		System.out.println();
	}
	/**
	 * Creates a new Package object
	 * @param day
	 * 	The day which the Package was delivered
	 */
	public static Package deliverPackage(int day) {
		Scanner input = new Scanner(System.in);
		boolean remainOn = true;
		String name = "";
		double weight = 0;
		while(remainOn){
			while(remainOn) {
				System.out.print("Please enter recipient name: ");
				name = input.nextLine();
				char firstLetter = Character.toUpperCase(name.charAt(0));
				if(firstLetter >= 'A' && firstLetter <= 'Z') {
					break;
				}
				else {
					System.out.println("Who named you, some half-wit with a stutter? Enter new name.");
					continue;
				}
			}
			while(remainOn) {
				System.out.print("Please enter the weight (lbs): ");
				try {
					weight = input.nextDouble();
					break;
				}
				catch(InputMismatchException e) {
					System.out.println("Do you know what a number is? Re-enter weight.");
					input.next();
					continue;
				}
			}
			break;
		}
		input.close();
		return new Package(name,day,weight);
	}
	/**
	 * Sorts a package into one of the stacks based on first name. Helper method for deliverPackage
	 * @param x
	 * 	Package to put in a stack
	 * @param stack1
	 * 	Stack 1 passed from displayMenu
	 * @param stack2
	 * 	Stack 2 passed from displayMenu
	 * @param stack3
	 * 	Stack 3 passed from displayMenu
	 * @param stack4
	 * 	Stack 4 passed from displayMenu
	 * @param stack5
	 * 	Stack 5 passed from displayMenu
	 * @param floor
	 * 	Floor stack passed from displayMenu
	 */
	public static void sortPackage(Package x,PackageStack stack1,PackageStack stack2,PackageStack stack3,
	  PackageStack stack4,PackageStack stack5,PackageStack floor) {
		char firstLetter = Character.toUpperCase(x.getRecipient().charAt(0));
		if(firstLetter >= 'A' && firstLetter <= 'G') {
			try {
				stack1.push(x);
			} catch (FullStackException e) {
				try {
					stack2.push(x);
				} catch (FullStackException e1) {
					try {
						stack3.push(x);
					} catch (FullStackException e2) {
						try {
							stack4.push(x);
						} catch (FullStackException e3) {
							try {
								stack5.push(x);
							} catch (FullStackException e4) {
								try {
									floor.push(x);
								} catch (FullStackException e5) {
									System.out.println("No room for your package.");
								}
							}
						}
					}
				}
			}
		}
		else if(firstLetter >= 'H' && firstLetter <= 'J') {
			try {
				stack2.push(x);
			} catch (FullStackException e) {
				try {
					stack1.push(x);
				} catch (FullStackException e1) {
					try {
						stack3.push(x);
					} catch (FullStackException e2) {
						try {
							stack4.push(x);
						} catch (FullStackException e3) {
							try {
								stack5.push(x);
							} catch (FullStackException e4) {
								try {
									floor.push(x);
								} catch (FullStackException e5) {
									System.out.println("unreachable error");
								}
							}
						}
					}
				}
			}
		}
		else if(firstLetter >= 'K' && firstLetter <= 'M'){
			try {
				stack3.push(x);
			} catch (FullStackException e) {
				try {
					stack2.push(x);
				} catch (FullStackException e1) {
					try {
						stack4.push(x);
					} catch (FullStackException e2) {
						try {
							stack1.push(x);
						} catch (FullStackException e3) {
							try {
								stack5.push(x);
							} catch (FullStackException e4) {
								try {
									floor.push(x);
								} catch (FullStackException e5) {
									//unreachable error
								}
							}
						}
					}
				}
			}
		}
		else if(firstLetter >= 'N' && firstLetter <= 'R') {
			try {
				stack4.push(x);
			} catch (FullStackException e) {
				try {
					stack3.push(x);
				} catch (FullStackException e1) {
					try {
						stack5.push(x);
					} catch (FullStackException e2) {
						try {
							stack2.push(x);
						} catch (FullStackException e3) {
							try {
								stack1.push(x);
							} catch (FullStackException e4) {
								try {
									floor.push(x);
								} catch (FullStackException e5) {
									//unreachable error
								}
							}
						}
					}
				}
			}
		}
		else if(firstLetter >= 'S' && firstLetter <= 'Z') {
			try {
				stack5.push(x);
			} catch (FullStackException e) {
				try {
					stack4.push(x);
				} catch (FullStackException e1) {
					try {
						stack3.push(x);
					} catch (FullStackException e2) {
						try {
							stack2.push(x);
						} catch (FullStackException e3) {
							try {
								stack1.push(x);
							} catch (FullStackException e4) {
								try {
									floor.push(x);
								} catch (FullStackException e5) {
									//unreachable error
								}
							}
						}
					}
				}
			}
		}
		System.out.println("A " + x.getWeight() + " pound package is awaiting"
		+ " pickup by " + x.getRecipient());
	}
	/**
	 * Moves a package from a source PackageStack to a destination PackageStack 
	 * @param stack1
	 * 	Stack1 passed from displayMenu
	 * @param stack2
	 * 	Stack2 passed from displayMenu
	 * @param stack3
	 * 	Stack3 passed from displayMenu
	 * @param stack4
	 * 	Stack4 passed from displayMenu
	 * @param stack5
	 * 	Stack5 passed from displayMenu
	 * @param floor
	 * 	Floor stack passed from displayMenu
	 */
	public static void movePackage(PackageStack stack1,PackageStack stack2,PackageStack stack3,
	  PackageStack stack4,PackageStack stack5,PackageStack floor) {
		Scanner input = new Scanner(System.in);
		boolean remainOn = true;
		int source = -1, destination = -1;;
		while(remainOn) {
			while(remainOn) {
				System.out.print("Please enter the source stack (enter 0 for floor): ");
				try{
					source = input.nextInt();
					if(source > 5 || source < 0) {
						System.out.println("Not a correct source stack.");
						input.next();
						continue;
					}
				}
				catch(InputMismatchException e) {
					System.out.println("Incorrect input. Try again.");
					input.next();
					continue;
				}
				break;
			}
			while(remainOn) {
				System.out.print("Please enter the desination stack: ");
				try{
					destination = input.nextInt();
					if(destination > 5 || destination < 0) {
						System.out.println("Not a correct source stack.");
						input.next();
						continue;
					}
				}
				catch(InputMismatchException e) {
					System.out.println("Incorrect input. Try again.");
					input.next();
					continue;
				}
				break;
			}
			break;
		}
		PackageStack sourceStack = null;
		PackageStack destinationStack = null;
		switch (source) {
			case 0:
				sourceStack = floor;
				break;
			case 1:
				sourceStack = stack1;
				break;
			case 2:
				sourceStack = stack2;
				break;
			case 3:
				sourceStack = stack3;
				break;
			case 4:
				sourceStack = stack4;
				break;
			case 5:
				sourceStack = stack5;
				break;
			default:
				System.out.println("ERROR");
		}
		switch (destination) {
			case 0:
				destinationStack = floor;
				break;
			case 1:
				destinationStack = stack1;
				break;
			case 2:
				destinationStack = stack2;
				break;
			case 3:
				destinationStack = stack3;
				break;
			case 4:
				destinationStack = stack4;
				break;
			case 5:
				destinationStack = stack5;
				break;
			default:
				System.out.println("ERROR");
		}
		try {
			destinationStack.push(sourceStack.pop());
		}
		catch (FullStackException e) {
			System.out.println("Stack is full");
		}
		catch(EmptyStackException ex) {
			System.out.println("Stack is empty");
		}
		input.close();
	}
	/**
	 * Removes old packages from all stacks
	 * @param day
	 * @param stack1
	 * 	Stack 1 passed from displayMenu
	 * @param stack2
	 * 	Stack 2 passed from displayMenu
	 * @param stack3
	 * 	Stack 3 passed from displayMenu
	 * @param stack4
	 * 	Stack 4 passed from displayMenu
	 * @param stack5
	 * 	Stack 5 passed from displayMenu
	 * @param floor
	 * 	Floor stack passed from displayMenu
	 * @return
	 * 	Return the total number of removed packages
	 */
	public static int removeOldPackages(int day,PackageStack stack1,PackageStack stack2,PackageStack stack3,
	  PackageStack stack4,PackageStack stack5,PackageStack floor) {
		int counter = 0;
		counter += removeOldPackagesHelper(day,stack1);
		counter += removeOldPackagesHelper(day,stack2);
		counter += removeOldPackagesHelper(day,stack3);
		counter += removeOldPackagesHelper(day,stack4);
		counter += removeOldPackagesHelper(day,stack5);
		return counter;
	}
	/**
	 * Removes Packages from PackageStacks if they are over 5 days old
	 * @param day
	 * 	Current Day
	 * @param stack1
	 * 	Stack to check for old Packages
	 * @return
	 * 	Return number of packages removed from stack
	 */
	public static int removeOldPackagesHelper(int day, PackageStack stack1) {
		PackageStack temp = new PackageStack(true);
		int counter = 0;
		while(!stack1.IsEmpty()) {
			try {
				if(day - stack1.peek().getArrivalDate() >= 5) {
					stack1.pop();
					counter++;
				}
			} catch (EmptyStackException e) {
				e.printStackTrace();
			}
			
			try {
				temp.push(stack1.pop());
			} catch (FullStackException | EmptyStackException e) {
				//do nothing
			}
		}
		while(!temp.IsEmpty()) {
			try {
				stack1.push(temp.pop());
			} catch (FullStackException | EmptyStackException e) {
				e.printStackTrace();
			}
		}
		return counter;
	}
	/**
	 * Retrieves the top most package for a person
	 * @param stack1
	 * 	Stack 1 passed from displayMenu
	 * @param stack2
	 * 	Stack 2 passed from displayMenu
	 * @param stack3
	 * 	Stack 3 passed from displayMenu
	 * @param stack4
	 * 	Stack 4 passed from displayMenu
	 * @param stack5
	 * 	Stack 5 passed from displayMenu
	 * @param floor
	 * 	Floor stack passed from displayMenu
	 */
	public static void getPackage(PackageStack stack1,PackageStack stack2,PackageStack stack3,
	  PackageStack stack4,PackageStack stack5,PackageStack floor) {
		Scanner input = new Scanner(System.in);
		boolean remainOn = true;
		String name = "";
		PackageStack temp = new PackageStack(true);
		char firstLetter = '0';
		
		while(remainOn) {
			System.out.print("Please enter the recipient name: ");
			name = input.nextLine();
			firstLetter = Character.toUpperCase(name.charAt(0));
			if(firstLetter < 'A' || firstLetter > 'Z') {
				System.out.println("Bad name input. Re-enter");
				continue;
			}
			break;
		}
		firstLetter = Character.toUpperCase(name.charAt(0));
		//floor stack checking for Package
		while(!floor.IsEmpty()) {
			try {
				if(name.equals(floor.peek().getRecipient())) {
					System.out.println("Package was found on floor stack");
					System.out.println("Give " + name + " " + floor.peek().getWeight() + " lb package delivered on " 
					  + floor.peek().getArrivalDate());
					floor.pop();
					return;
				}
			} catch (EmptyStackException e) {
				//unreachable
			}
			try {
				temp.push(floor.pop());
			} catch (FullStackException | EmptyStackException e) {
				//unreachable
			}
		}
		input.close();
		int count = 0;
		if(firstLetter >= 'A' && firstLetter <= 'G') {
			count = getPackageHelper(stack5,floor,name);
			if(count != -1) {
				getPackageHelper2(stack1,stack2,stack3,stack4,stack5,floor,count,5,name);
				return;
			}
			count = getPackageHelper(stack4,floor,name);
			if(count != -1) {
				getPackageHelper2(stack1,stack2,stack3,stack4,stack5,floor,count,4,name);
				return;
			}
			count = getPackageHelper(stack3,floor,name);
			if(count != -1) {
				getPackageHelper2(stack1,stack2,stack3,stack4,stack5,floor,count,3,name);
				return;
			}
			count = getPackageHelper(stack2,floor,name);
			if(count != -1) {
				getPackageHelper2(stack1,stack2,stack3,stack4,stack5,floor,count,2,name);
				return;
			}
			count = getPackageHelper(stack1,floor,name);
			if(count != -1) {
				getPackageHelper2(stack1,stack2,stack3,stack4,stack5,floor,count,1,name);
				return;
			}
		}
		else if(firstLetter >= 'H' && firstLetter <= 'J' ) {
			count = getPackageHelper(stack5,floor,name);
			if(count != -1) {
				getPackageHelper2(stack1,stack2,stack3,stack4,stack5,floor,count,5,name);
				return;
			}
			count = getPackageHelper(stack4,floor,name);
			if(count != -1) {
				getPackageHelper2(stack1,stack2,stack3,stack4,stack5,floor,count,4,name);
				return;
			}
			count = getPackageHelper(stack3,floor,name);
			if(count != -1) {
				getPackageHelper2(stack1,stack2,stack3,stack4,stack5,floor,count,3,name);
				return;
			}
			count = getPackageHelper(stack1,floor,name);
			if(count != -1) {
				getPackageHelper2(stack1,stack2,stack3,stack4,stack5,floor,count,1,name);
				return;
			}
			count = getPackageHelper(stack2,floor,name);
			if(count != -1) {
				getPackageHelper2(stack1,stack2,stack3,stack4,stack5,floor,count,2,name);
				return;
			}
		}
		else if(firstLetter >= 'K' && firstLetter <= 'M') {
			count = getPackageHelper(stack5,floor,name);
			if(count != -1) {
				getPackageHelper2(stack1,stack2,stack3,stack4,stack5,floor,count,5,name);
				return;
			}
			count = getPackageHelper(stack1,floor,name);
			if(count != -1) {
				getPackageHelper2(stack1,stack2,stack3,stack4,stack5,floor,count,1,name);
				return;
			}
			count = getPackageHelper(stack4,floor,name);
			if(count != -1) {
				getPackageHelper2(stack1,stack2,stack3,stack4,stack5,floor,count,4,name);
				return;
			}
			count = getPackageHelper(stack2,floor,name);
			if(count != -1) {
				getPackageHelper2(stack1,stack2,stack3,stack4,stack5,floor,count,2,name);
				return;
			}
			count = getPackageHelper(stack3,floor,name);
			if(count != -1) {
				getPackageHelper2(stack1,stack2,stack3,stack4,stack5,floor,count,3,name);
				return;
			}
		}
		else if(firstLetter >= 'N' && firstLetter <= 'R') {
			count = getPackageHelper(stack1,floor,name);
			if(count != -1) {
				getPackageHelper2(stack1,stack2,stack3,stack4,stack5,floor,count,1,name);
				return;
			}
			count = getPackageHelper(stack2,floor,name);
			if(count != -1) {
				getPackageHelper2(stack1,stack2,stack3,stack4,stack5,floor,count,2,name);
				return;
			}
			count = getPackageHelper(stack5,floor,name);
			if(count != -1) {
				getPackageHelper2(stack1,stack2,stack3,stack4,stack5,floor,count,5,name);
				return;
			}
			count = getPackageHelper(stack3,floor,name);
			if(count != -1) {
				getPackageHelper2(stack1,stack2,stack3,stack4,stack5,floor,count,3,name);
				return;
			}
			count = getPackageHelper(stack4,floor,name);
			if(count != -1) {
				getPackageHelper2(stack1,stack2,stack3,stack4,stack5,floor,count,4,name);
				return;
			}
		}
		else if(firstLetter >= 'S' && firstLetter <= 'Z') {
			count = getPackageHelper(stack1,floor,name);
			if(count != -1) {
				getPackageHelper2(stack1,stack2,stack3,stack4,stack5,floor,count,1,name);
				return;
			}
			count = getPackageHelper(stack2,floor,name);
			if(count != -1) {
				getPackageHelper2(stack1,stack2,stack3,stack4,stack5,floor,count,2,name);
				return;
			}
			count = getPackageHelper(stack3,floor,name);
			if(count != -1) {
				getPackageHelper2(stack1,stack2,stack3,stack4,stack5,floor,count,3,name);
				return;
			}
			count = getPackageHelper(stack4,floor,name);
			if(count != -1) {
				getPackageHelper2(stack1,stack2,stack3,stack4,stack5,floor,count,4,name);
				return;
			}
			count = getPackageHelper(stack5,floor,name);
			if(count != -1) {
				getPackageHelper2(stack1,stack2,stack3,stack4,stack5,floor,count,5,name);
				return;
			}
		}
	}
	/**
	 * Helper method for getPackage. Moves packages to floor for a stack, and returns it to original stack if desired 
	 * package is not found. 
	 * @param stack
	 * 	Stack to check from
	 * @param floor
	 * 	Floor stack passed from getPackage
	 * @param name
	 * 	Name of the recipient
	 * @return
	 * 	Returns the number of Packages moved from Stack to Floor
	 */
	public static int getPackageHelper(PackageStack stack, PackageStack floor, String name) {
		int counter = 0;
		
		while(!stack.IsEmpty()) {
			try {
				if(stack.peek().getRecipient().equals(name)) {
					return counter;
				}
			} catch (EmptyStackException e) {
				//unreachable
			}
			try {
				counter++;
				floor.push(stack.pop());
			} catch (FullStackException | EmptyStackException e) {
				//
			}
		}
		for(int i=0; i<counter; i++) {
			try {
				stack.push(floor.pop());
			} catch (FullStackException | EmptyStackException e) {
				//
			}
		}
		
		return -1;
	}
	/**
	 * Checks if a PackageStack has the most recent package for a recipient
	 * @param stack1
	 * 	Stack1 passed from getPackage
	 * @param stack2
	 * 	Stack2 passed from getPackage
	 * @param stack3
	 * 	Stack3 passed from getPackage
	 * @param stack4
	 * 	Stack4 passed from getPackage
	 * @param stack5
	 * 	Stack5 passed from getPackage
	 * @param floor
	 * 	Floor passed from getPackage
	 * @param count
	 * 	How many packages to move back from floor to original stack, if it's -1 do nothing
	 * @param stackNum
	 * 	What is the number of the PackageStack
	 * @param name
	 * 	Name of the recipient to find the package for
	 */
	public static void getPackageHelper2(PackageStack stack1,PackageStack stack2,PackageStack stack3,PackageStack stack4,
	  PackageStack stack5, PackageStack floor, int count, int stackNum, String name) {
		PackageStack stack;
		switch (stackNum){
			case 1:
				stack = stack1;
				break;
			case 2:
				stack = stack2;
				break;
			case 3:
				stack = stack3;
				break;
			case 4:
				stack = stack4;
				break;
			case 5:
				stack = stack5;
				break;
			default:
				stack = floor;
				break;
		}
		if(count != -1) {
			if(count > 0)
				System.out.println("Move " + count + " packages from Stack" + stackNum + " to floor");
			printStack(stack1,stack2,stack3,stack4,stack5,floor);
			try {
				System.out.println("Give " + name + " " + stack.peek().getWeight() + 
				  " lb package delivered on day " + stack.peek().getArrivalDate());
				stack.pop();
			} catch (EmptyStackException e) {
				//
			}
			printStack(stack1,stack2,stack3,stack4,stack5,floor);
			if(count > 0)
				System.out.println("Return " + count + " packages to stack 5 from floor");
			for(int i=0; i<count; i++) {
				try {
					stack.push(floor.pop());
				} catch (FullStackException | EmptyStackException e) {
					//
				}
			}
			printStack(stack1,stack2,stack3,stack4,stack5,floor);
			return;
		}
	}
	/**
	 * Finds packages that are in the incorrect stack and moves them to floor
	 * @param stack1
	 * 	Stack1 passed from displayMenu
	 * @param stack2
	 * 	Stack2 passed from displayMenu
	 * @param stack3
	 * 	Stack3 passed from displayMenu
	 * @param stack4
	 * 	Stack4 passed from displayMenu
	 * @param stack5
	 * 	Stack5 passed from displayMenu
	 * @param floor
	 * 	Floor stack passed from displayMenu
	 */
	public static void organizePackages(PackageStack stack1,PackageStack stack2,PackageStack stack3,PackageStack stack4,
	  PackageStack stack5,PackageStack floor) {
		PackageStack temp = new PackageStack(false);
		char firstLetter = '0';
		//Stack 1
		while(!stack1.IsEmpty()) {
			try {
				firstLetter = Character.toUpperCase(stack1.peek().getRecipient().charAt(0));
			} catch (EmptyStackException e) {
				//unreachable
			}
			if(firstLetter < 'A' || firstLetter > 'G') {
				try {
					floor.push(stack1.pop());
					continue;
				} catch (FullStackException | EmptyStackException e) {
					//unreachable
				}
			}
			try {
				temp.push(stack1.pop());
			} catch (FullStackException | EmptyStackException e) {
				//unreachable
			}
		}
		while(!temp.IsEmpty()) {
			try {
				stack1.push(temp.pop());
			} catch (FullStackException | EmptyStackException e) {
				//not reachable
			}
		}
		//Stack2
		while(!stack2.IsEmpty()) {
			try {
				firstLetter = Character.toUpperCase(stack2.peek().getRecipient().charAt(0));
			} catch (EmptyStackException e) {
				//unreachable
			}
			if(firstLetter < 'H' || firstLetter > 'J') {
				try {
					floor.push(stack2.pop());
					continue;
				} catch (FullStackException | EmptyStackException e) {
					//unreachable
				}
			}
			try {
				temp.push(stack2.pop());
			} catch (FullStackException | EmptyStackException e) {
				//not reachable
			}
		}
		while(!temp.IsEmpty()) {
			try {
				stack2.push(temp.pop());
			} catch (FullStackException | EmptyStackException e) {
				e.printStackTrace();
			}
		}
		//Stack3
		while(!stack3.IsEmpty()) {
			try {
				firstLetter = Character.toUpperCase(stack3.peek().getRecipient().charAt(0));
			} catch (EmptyStackException e) {
				//unreachable
			}
			if(firstLetter < 'K' || firstLetter > 'M') {
				try {
					floor.push(stack3.pop());
					continue;
				} catch (FullStackException | EmptyStackException e) {
					//unreachable
				}
			}
			try {
				temp.push(stack3.pop());
			} catch (FullStackException | EmptyStackException e) {
				//not reachable
			}
		}
		while(!temp.IsEmpty()) {
			try {
				stack3.push(temp.pop());
			} catch (FullStackException | EmptyStackException e) {
				e.printStackTrace();
			}
		}
		//Stack4
		while(!stack4.IsEmpty()) {
			try {
				firstLetter = Character.toUpperCase(stack4.peek().getRecipient().charAt(0));
			} catch (EmptyStackException e) {
				//unreachable
			}
			if(firstLetter < 'N' || firstLetter > 'R') {
				try {
					floor.push(stack4.pop());
					continue;
				} catch (FullStackException | EmptyStackException e) {
					//unreachable
				}
			}
			try {
				temp.push(stack4.pop());
			} catch (FullStackException | EmptyStackException e) {
				//not reachable
			}
		}
		while(!temp.IsEmpty()) {
			try {
				stack4.push(temp.pop());
			} catch (FullStackException | EmptyStackException e) {
				//not reachable
			}
		}
		//Stack5
		while(!stack5.IsEmpty()) {
			try {
				firstLetter = Character.toUpperCase(stack5.peek().getRecipient().charAt(0));
			} catch (EmptyStackException e) {
				//unreachable
			}
			if(firstLetter < 'S' || firstLetter > 'Z') {
				try {
					floor.push(stack5.pop());
					continue;
				} catch (FullStackException | EmptyStackException e) {
					//unreachable
				}
			}
			try {
				temp.push(stack5.pop());
			} catch (FullStackException | EmptyStackException e) {
				//not reachable
			}
		}
		while(!temp.IsEmpty()) {
			try {
				stack5.push(temp.pop());
			} catch (FullStackException | EmptyStackException e) {
				//not reachable
			}
		}
	}
	/**
	 * Lists the packages that a recipient has
	 * @param stack1
	 * 	Stack1 passed from displayMenu
	 * @param stack2
	 * 	Stack2 passed from displayMenu
	 * @param stack3
	 * 	Stack3 passed from displayMenu
	 * @param stack4
	 * 	Stack4 passed from displayMenu
	 * @param stack5
	 * 	Stack5 passed from displayMenu
	 * @param floor
	 * 	Floor stack passed from displayMenu
	 */
	public static void listPackages(PackageStack stack1,PackageStack stack2,PackageStack stack3,PackageStack stack4,
	  PackageStack stack5,PackageStack floor) {
		Scanner input = new Scanner(System.in);
		String name = "";
		int counter = 1;
		boolean remainOn = true;
		char firstLetter;
		PackageStack temp = new PackageStack(true);
		boolean exists = false;
		
		while(remainOn) {
			System.out.print("Please enter the recipient name:  ");
			name = input.nextLine();
			firstLetter = Character.toUpperCase(name.charAt(0));
			if(firstLetter < 'A' || firstLetter > 'Z') {
				System.out.println("Bad name input. Re-enter");
				continue;
			}
			break;
		}
		//Check stack1
		while(!stack1.IsEmpty()) {
			try {
				if(stack1.peek().getRecipient().equals(name))
					System.out.println("Package " + counter + " is in Stack 1 was delivered on day " 
					  + stack1.peek().getArrivalDate() + " and weighs " + stack1.peek().getWeight() + " lbs.");
				counter++;
				exists = true;
			} catch (EmptyStackException e) {
				//empty
			}
			try {
				temp.push(stack1.pop());
			} catch (FullStackException | EmptyStackException e) {
				//empty
			}
		}
		while(!temp.IsEmpty()) {
			try {
				stack1.push(temp.pop());
			} catch (FullStackException | EmptyStackException e) {
				
			}
		}
		//Check stack2
		while(!stack2.IsEmpty()) {
			try {
				if(stack2.peek().getRecipient().equals(name))
					System.out.println("Package " + counter + " is in Stack 1 was delivered on day " 
					  + stack2.peek().getArrivalDate() + " and weighs " + stack2.peek().getWeight() + " lbs.");
				counter++;
				exists = true;
			} catch (EmptyStackException e) {
				//empty
			}
			try {
				temp.push(stack2.pop());
			} catch (FullStackException | EmptyStackException e) {
				//empty
			}
		}
		while(!temp.IsEmpty()) {
			try {
				stack2.push(temp.pop());
			} catch (FullStackException | EmptyStackException e) {
				
			}
		}
		//Check stack3
		while(!stack3.IsEmpty()) {
			try {
				if(stack3.peek().getRecipient().equals(name))
					System.out.println("Package " + counter + " is in Stack 1 was delivered on day " 
					  + stack3.peek().getArrivalDate() + " and weighs " + stack3.peek().getWeight() + " lbs.");
				counter++;
				exists = true;
			} catch (EmptyStackException e) {
				//empty
			}
			try {
				temp.push(stack3.pop());
			} catch (FullStackException | EmptyStackException e) {
				//empty
			}
		}
		while(!temp.IsEmpty()) {
			try {
				stack3.push(temp.pop());
			} catch (FullStackException | EmptyStackException e) {
				
			}
		}
		//Check stack4
		while(!stack4.IsEmpty()) {
			try {
				if(stack4.peek().getRecipient().equals(name))
					System.out.println("Package " + counter + " is in Stack 1 was delivered on day " 
					  + stack4.peek().getArrivalDate() + " and weighs " + stack4.peek().getWeight() + " lbs.");
				counter++;
				exists = true;
			} catch (EmptyStackException e) {
				//empty
			}
			try {
				temp.push(stack4.pop());
			} catch (FullStackException | EmptyStackException e) {
				//empty
			}
		}
		while(!temp.IsEmpty()) {
			try {
				stack4.push(temp.pop());
			} catch (FullStackException | EmptyStackException e) {
				
			}
		}
		//Check stack5
		while(!stack5.IsEmpty()) {
			try {
				if(stack5.peek().getRecipient().equals(name))
					System.out.println("Package " + counter + " is in Stack 1 was delivered on day " 
					  + stack5.peek().getArrivalDate() + " and weighs " + stack5.peek().getWeight() + " lbs.");
				counter++;
				exists = true;
			} catch (EmptyStackException e) {
				//empty
			}
			try {
				temp.push(stack5.pop());
			} catch (FullStackException | EmptyStackException e) {
				//empty
			}
		}
		while(!temp.IsEmpty()) {
			try {
				stack5.push(temp.pop());
			} catch (FullStackException | EmptyStackException e) {
				
			}
		}
		//Check floorstack
		while(!floor.IsEmpty()) {
			try {
				if(floor.peek().getRecipient().equals(name))
					System.out.println("Package " + counter + " is in Stack 1 was delivered on day " 
					  + floor.peek().getArrivalDate() + " and weighs " + floor.peek().getWeight() + " lbs.");
				counter++;
				exists = true;
			} catch (EmptyStackException e) {
				//empty
			}
			try {
				temp.push(floor.pop());
			} catch (FullStackException | EmptyStackException e) {
				//empty
			}
		}
		while(!temp.IsEmpty()) {
			try {
				floor.push(temp.pop());
			} catch (FullStackException | EmptyStackException e) {
				
			}
		}
		if(!exists)
			System.out.println(name + " doesn't have any packages.");
		input.close();
	}
}
