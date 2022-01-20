/**
 * David Man 111940002 RO3
 * Auction class
 */

import java.io.Serializable;

@SuppressWarnings("serial")
public class Auction implements Serializable {
	//Variable for time remaining in auction
	int timeRemaining;
	//Variable for current bid of auction
	double currentBid;
	//Variable for ID of the auction
	String auctionID;
	//Variable for name of seller of auction
	String sellerName;
	//Variable for buyer of auction
	String buyerName;
	//Variable for info of item being sold in auction
	String itemInfo;
	
	/**
	 * Auction class no argument constructor
	 */
	public Auction() {
		timeRemaining = 0;
		currentBid = 0;
		auctionID = "";
		sellerName = "";
		buyerName = "";
		itemInfo = "";
	}
	
	/**
	 * Auction class argument constructor
	 * @param timeRemaining
	 *   Integer variable for time remaining
	 * @param currentBid
	 *   Double variable for current bid
	 * @param auctionID
	 *   String variable for ID of auction
	 * @param sellerName
	 *   String variable for name of seller
	 * @param buyerName
	 *   String variable for name of buyer
	 * @param itemInfo
	 *   String variable for info of item
	 */
	public Auction(int timeRemaining, double currentBid, String auctionID,
	  String sellerName, String buyerName, String itemInfo) {
		this.timeRemaining = timeRemaining;
		this.currentBid = currentBid;
		this.auctionID = auctionID;
		this.sellerName = sellerName;
		this.buyerName = buyerName;
		this.itemInfo = itemInfo;
	}
	
	public int getTimeRemaining() {
		return timeRemaining;
	}

	public double getCurrentBid() {
		return currentBid;
	}

	public String getAuctionID() {
		return auctionID;
	}
	
	public String getSellerName() {
		return sellerName;
	}
	
	public String getBuyerName() {
		return buyerName;
	}
	
	public String getItemInfo() {
		return itemInfo;
	}
	
	/**
	 * Decrement time remaining method
	 * @param time
	 *   Integer variable for time being decremented from auction
	 */
	public void decrementTimeRemaining(int time) {
		if (time > timeRemaining) {
			timeRemaining = 0;
		}
		else {
			timeRemaining -= time;
		}
	}
	
	/**
	 * New bid method for creating new bid for auction
	 * @param bidderName
	 *   String variable for name of bidder
	 * @param bidAmt
	 *   Double variable for amount of bid
	 * @throws ClosedAuctionException
	 *   Thrown if auction is closed (time is 0)
	 */
	public void newBid(String bidderName, double bidAmt)
	  throws ClosedAuctionException{
		if (timeRemaining == 0) {
			throw new ClosedAuctionException(
			  "You can no longer bid on this item");
		}
		else {
			if (bidAmt > currentBid) {
				currentBid = bidAmt;
				buyerName = bidderName;
				System.out.println("Bid accepted.");
			}
		}
	}
	
	/**
	 * toString method for auction
	 */
	public String toString() {
		return "\nAuction " + auctionID + ":\n" +
		  "    Seller: " + sellerName +
		  "\n    Buyer: " + buyerName +
		  "\n    Time: " + timeRemaining + " hours" +
		  "\n    Info: " + itemInfo + "\n";
	}
}

/**
 * ClosedAuctionException class
 */
@SuppressWarnings("serial")
class ClosedAuctionException extends Exception {
	public ClosedAuctionException(String e) {
		super (e);
	}
}