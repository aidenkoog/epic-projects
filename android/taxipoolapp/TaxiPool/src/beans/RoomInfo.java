package beans;

public class RoomInfo extends Operation{
	public RoomInfo(int state) {
		super(state);
	}
	public RoomInfo(){
		super.state = Operation.OP_CREATE_ROOM;
	}

	private static final long serialVersionUID = 1L;
	
	
	
	private String roomId;
	private int numberOfPeople;
	private String departure;
	private String destination;
	private String time;
	private String contents;
	public int getCurrentNumberOfPeople() {
		return currentNumberOfPeople;
	}
	public void setCurrentNumberOfPeople(int currentNumberOfPeople) {
		this.currentNumberOfPeople = currentNumberOfPeople;
	}

	private String ownerUserPhoneNumber;
	private int isActivated;
	private int currentNumberOfPeople;
	
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	
	
	public String getOwnerUserPhoneNumber() {
		return ownerUserPhoneNumber;
	}
	public void setOwnerUserPhoneNumber(String ownerUserPhoneNumber) {
		this.ownerUserPhoneNumber = ownerUserPhoneNumber;
	}
	public int getIsActivated() {
		return isActivated;
	}
	public void setIsActivated(int isActivated) {
		this.isActivated = isActivated;
	}
	public String toString(){
		return numberOfPeople + ":" + departure + ":" + destination + ":" + time + ":" + contents+":" + currentNumberOfPeople;
	}

	public int getNumberOfPeople() {
		return numberOfPeople;
	}

	public void setNumberOfPeople(int numberOfPeople) {
		this.numberOfPeople = numberOfPeople;
	}

	public String getDeparture() {
		return departure;
	}

	public void setDeparture(String departure) {
		this.departure = departure;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	
	

}
