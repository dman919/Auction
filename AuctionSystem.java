/**
 * David Man 111940002 RO3
 * Auction System class
 */

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

public class AuctionSystem {
	//Auction table member variable
	AuctionTable auctionTable;
	//Username member string variable
	String username;
	
	public AuctionTable getAuctionTable() {
		return auctionTable;
	}
	/**
	 * AuctionTable mutator method
	 * @param auctionTable
	 *   AuctionTable class object for auction table
	 */
	public void setAuctionTable(AuctionTable auctionTable) {
		this.auctionTable = auctionTable;
	}
	
	public String getUsername() {
		return username;
	}
	/**
	 * Username mutator method
	 * @param username
	 *   String variable for username
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	@SuppressWarnings({ "static-access", "resource" })
	public static void main(String[] args) throws IOException {
		Scanner input = new Scanner(System.in);
		
		AuctionSystem auctionSystem = new AuctionSystem();
		
		try {			
			FileInputStream file = new FileInputStream("auction.obj");
			ObjectInputStream inStream = new ObjectInputStream(file);
			auctionSystem.setAuctionTable(
			  (AuctionTable) inStream.readObject());
			System.out.println("Starting...\n" +
					  "Loading previous Auction Table.\n");

		}
		catch (Exception e) {
		//AuctionSystem class object
			System.out.println("Starting...\n" +
			  "No previous auction table detected.\n" + 
			  "Creating new table...\n");
			
			auctionSystem.setAuctionTable(new AuctionTable());
		}
		
		//Setting username
		System.out.print("Please select a username: ");
		auctionSystem.setUsername(input.next());
		
		String s = "";
		
		//Loop that runs until prompted to end
		while (!s.equals("Q") || !s.equals("q")) {
			System.out.println(
			  "\nMenu:\n" + 
			  "    (D) - Import Data from URL\n" + 
			  "    (A) - Create a New Auction\n" + 
			  "    (B) - Bid on an Item\n" + 
			  "    (I) - Get Info on Auction\n" + 
			  "    (P) - Print All Auctions\n" + 
			  "    (R) - Remove Expired Auctions\n" + 
			  "    (T) - Let Time Pass\n" + 
			  "    (Q) - Quit\n");
			
			System.out.print("Please select an option: ");
			s = input.next();
			
			//Import data from URL
			if (s.equals("D") || s.equals("d")) {
				System.out.print("Please enter a URL: ");
				String URL = input.next();
				try {
					auctionSystem.setAuctionTable(
					  auctionSystem.getAuctionTable().buildFromURL(URL));
				}
				catch (IllegalArgumentException e) {
					System.out.println("Invalid URL");
				}
			}
			
			//Create a new auction
			if (s.equals("A") || s.equals("a")) {
				System.out.println("\nCreating new Auction as " +
				  auctionSystem.getUsername() + ".");
				
				System.out.print("Please enter an Auction ID: ");
				String ID = input.next();
				
				System.out.print("Please enter an Auction time (hours): ");
				int time = input.nextInt();
				
				System.out.print("Please enter some Item Info: ");
				input.nextLine();
				String itemInfo = input.nextLine();
				System.out.print("");
				
				Auction newAuction = new Auction(
				  time, 0.0, ID, auctionSystem.getUsername(), "", itemInfo);
				
				auctionSystem.getAuctionTable().putAuction(ID, newAuction);
			}
			
			//Bid on an item
			if (s.equals("B") || s.equals("b")) {
				System.out.print("Please enter an Auction ID: ");
				String ID  = input.next();
				
				//Checking if auction is closed (no more time)
				if (auctionSystem.getAuctionTable().
				  getAuction(ID).getTimeRemaining() == 0) {
					System.out.println("\nAuction " + ID + " is CLOSED\n" + 
					  "    Current Bid: $ " +
					  auctionSystem.getAuctionTable().
					    getAuction(ID).getCurrentBid());
					try {
						auctionSystem.getAuctionTable().
						  getAuction(ID).newBid(ID, 0);
					}
					catch (ClosedAuctionException e) {
						System.out.println();
					}
					
					System.out.println(
					  "You can no longer bid on this item.");
				}
				else {
					System.out.print("\nAuction " + ID + " is OPEN\n" + 
							  "    Current Bid: $ ");
					if (auctionSystem.getAuctionTable().
					  getAuction(ID).getCurrentBid() == 0)
						System.out.println("None");
					else
						System.out.println(auctionSystem.getAuctionTable().
						  getAuction(ID).getCurrentBid());
					System.out.print("\nWhat would you like to bid?: ");
					double bid = input.nextDouble();
					try {
						auctionSystem.getAuctionTable().getAuction(ID).
						  newBid(auctionSystem.getUsername(), bid);
					}
					catch (ClosedAuctionException e) {
						System.out.print(e);
					}
				}
			}
			
			//Get info on auction
			if (s.equals("I") || s.equals("i")) {
				System.out.print("Please enter an Auction ID: ");
				String ID = input.next();
				
				System.out.print(auctionSystem.getAuctionTable().
				  getAuction(ID).toString());
			}
			
			//Print all auctions in a formatted table
			if (s.equals("P") || s.equals("p")) {
				auctionSystem.getAuctionTable().printTable();
			}
			
			//Remove expired auctions
			if (s.equals("R") || s.equals("r")) {
				auctionSystem.getAuctionTable().removeExpiredAuctions();
			}
			
			//Let time pass
			if (s.equals("T") || s.equals("t")) {
				System.out.print("How many hours should pass: ");
				int time = input.nextInt();
				
				auctionSystem.getAuctionTable().letTimePass(time);
			}
			
			//Terminate program
			if (s.equals("Q") || s.equals("q")) {
				System.out.print("\nWriting Auction Table to file...\n" +
				  "Done!\n\n" + 
				  "Goodbye.");
				FileOutputStream file = new FileOutputStream("auction.obj");
				ObjectOutputStream outStream = new ObjectOutputStream(file);
				outStream.writeObject(auctionSystem.getAuctionTable());
				System.exit(0);
			}
		}
	}
}
