/**
 * David Man 111940002 RO3
 * Auction Table class
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import big.data.*;

@SuppressWarnings("serial")
public class AuctionTable implements Serializable {
	//HashMap that represents the auction table
	HashMap<String, Auction> auctionTable;
	
	/**
	 * Auction Table class no argument constructor
	 */
	public AuctionTable() {
		auctionTable = new HashMap<String, Auction>();
	}
	
	/**
	 * Build from URL method
	 * @param URL
	 *   String variable that represents URL given
	 * @return
	 *   Returns Auction Table class object updated with all auctions
	 * @throws IllegalArgumentException
	 *   Thrown when URL is invalid or data source cannot be found
	 */
	public static AuctionTable buildFromURL(String URL)
	  throws IllegalArgumentException {
		try {
			AuctionTable table = new AuctionTable();
			
			System.out.println("\nLoading...");
			
			DataSource ds = DataSource.connect(URL).load();
			String[] names = ds.fetchStringArray(
			  "listing/seller_info/seller_name");
			String[] bids = ds.fetchStringArray(
			  "listing/auction_info/current_bid");
			String[] times= ds.fetchStringArray(
			  "listing/auction_info/time_left");
			String[] ids = ds.fetchStringArray(
			  "listing/auction_info/id_num");
			String[] bidders = ds.fetchStringArray(
			  "listing/auction_info/high_bidder/bidder_name");
			String[] memorys = ds.fetchStringArray(
			  "listing/item_info/memory");
			String[] hds = ds.fetchStringArray(
			  "listing/item_info/hard_drive");
			String[] cpus = ds.fetchStringArray(
			  "listing/item_info/cpu");
			
			for (int i = 0; i < names.length; i++) {
				int time = table.convertToHours(times[i]);
				double currentBid = table.convertToDouble(bids[i]);
				String itemInfo = cpus[i] + " - " +
				  memorys[i] + " - " + hds[i];
	
				Auction newAuction = new Auction(
				  time, currentBid, ids[i], names[i], bidders[i], itemInfo);
				
				table.auctionTable.put(ids[i], newAuction);
			}
			
			System.out.println("Auction data loaded successfully!");
			
			return table;
		}
		catch (Exception e) {
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * Put auction method
	 * @param auctionID
	 *   String variable for ID of auction
	 * @param auction
	 *   Auction class object representing auction
	 * @throws IllegalArgumentException
	 *   Thrown when auction with given ID already exists
	 */
	public void putAuction(String auctionID, Auction auction)
	  throws IllegalArgumentException {
		if (auctionTable.containsKey(auctionID)) {
			throw new IllegalArgumentException(
			  "auctionID is already stored in table");
		}
		
		auctionTable.put(auctionID, auction);
		
		System.out.println(
		  "\nAuction " + auctionID + " inserted into table.");
	}
	
	/**
	 * Get auction method
	 * @param auctionID
	 *   String variable for ID of auction
	 * @return
	 *   Returns Auction class object representing specified auction in table
	 */
	public Auction getAuction(String auctionID) {
		if (auctionTable.containsKey(auctionID))
			return auctionTable.get(auctionID);
		else
			return null;
	}
	
	/**
	 * Let Time Pass method
	 * @param numHours
	 *   Integer variable representing number of hours that will pass
	 * @throws IllegalArgumentException
	 *   Thrown when invalid number of hours are given (negative or 0)
	 */
	public void letTimePass(int numHours) throws IllegalArgumentException {
		if (numHours < 0) {
			throw new IllegalArgumentException(
			  "number of hours has to be greater than 0");
		}
		
		System.out.println("\nTime passing...");
		
		Set<String> set = auctionTable.keySet();
		for (String key: set) {
			auctionTable.get(key).decrementTimeRemaining(numHours);;
		}
		
		System.out.println("Auction times updated.");
	}
	
	/**
	 * Remove Expired Auctions method for removing auctions with no time left
	 */
	public void removeExpiredAuctions() {
		System.out.println("\nRemoving expired auctions...");
		
		ArrayList<String> temp = new ArrayList<String>(auctionTable.keySet());
		
		for (String key: temp) {
			if (auctionTable.get(key).getTimeRemaining() == 0) {
				auctionTable.remove(key);
			}
		}
		
		System.out.println("All expired auctions removed.");
	}
	
	/**
	 * Print Table method for printing table of all auctions
	 */
	public void printTable() {
		System.out.print(
		  "\n Auction ID |      Bid   |        Seller         |" +
		  "          Buyer          |    Time   |  Item Info\n" +
		  "=====================================================" +
		  "======================================================" +
		  "========================\n");
		
		Set<String> set = auctionTable.keySet();
		for (String key: set) {
			String bid;
			if (auctionTable.get(key).getCurrentBid() == 0)
				bid = "";
			else
				bid = String.valueOf(auctionTable.get(key).getCurrentBid());
			String info = auctionTable.get(key).getItemInfo();
			if (auctionTable.get(key).getItemInfo().length() > 42)
				info = auctionTable.get(key).getItemInfo().substring(0, 42);
			
			System.out.println(String.format(
			  "%11s | $%9s | %-22s| %-22s | %3s hours | %-42s",
			  auctionTable.get(key).getAuctionID(),
			  bid,
			  auctionTable.get(key).getSellerName(),
			  auctionTable.get(key).getBuyerName(),
			  auctionTable.get(key).getTimeRemaining(),
			  info));
		}
	}
	
	/**
	 * Convert to Hours method
	 * @param times
	 *   String variable representing line found from URL of time of auction
	 * @return
	 *   Returns total amount of hours for the auction
	 */
	public int convertToHours(String times) {
		int hours = 0;
		
		if (times.contains("days")) {
			int days = Integer.parseInt(
			  times.substring(0, times.indexOf(" ")));
			hours += days * 24;
			times = times.substring(times.indexOf("days, ") + 6);
		}
		if (times.contains("hours")) {
			int h = Integer.parseInt(times.substring(0, times.indexOf(" ")));
			hours += h;
		}
		
		return hours;
	}
	
	/**
	 * Convert to Double method
	 * @param bids
	 *   String variable representing amount found for the bid of the auction
	 * @return
	 *   Returns the converted amount from string to double value
	 */
	public double convertToDouble(String bids) {
		double bid = 0;
		
		if (bids.contains("$")) {
			bids = bids.replaceAll(",", "");
			bid = Double.parseDouble(bids.substring(bids.indexOf("$") + 1));
		}
		
		return bid;
	}
}