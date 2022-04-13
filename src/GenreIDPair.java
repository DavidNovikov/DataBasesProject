
public class GenreIDPair {
	private int itemID;
	private String itemGenre;
	
	public GenreIDPair(int itemID, String itemGenre) {
		this.itemID = itemID;
		this.itemGenre = itemGenre;
	}
	
	public GenreIDPair() {
		this.itemID = -1;
		this.itemGenre = null;
	}
	
	public int getItemID() {
		return itemID;
	}
	
	public String getItemGenre() {
		return itemGenre;
	}
	
	public void setItemID(int itemID) {
		this.itemID = itemID;
	}
	
	public void setGenre(String itemGenre) {
		this.itemGenre = itemGenre;
	}
}
