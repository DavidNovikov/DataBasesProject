
public class Relationship {
	private int creatorID;
	private int itemID;

	public Relationship(int creatorID, int itemID) {
		this.creatorID = creatorID;
		this.itemID = itemID;
	}

	public Relationship() {
		this.creatorID = -1;
		this.itemID = -1;
	}

	public int getItemID() {
		return itemID;
	}

	public int getCreatorID() {
		return creatorID;
	}

	public void setItemID(int itemID) {
		this.itemID = itemID;
	}

	public void setCreatorID(int creatorID) {
		this.creatorID = creatorID;
	}
}
