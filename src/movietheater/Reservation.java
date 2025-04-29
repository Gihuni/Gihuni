package movietheater;

public class Reservation {
	private String phoneNum;
	private String userName;
	private String movieName;
	private int seatNum;

	public Reservation() {
		this(null, null, null, 0);
	}

	public Reservation(String phoneNum, String userName, String movieName, int seatNum) {
		super();
		this.phoneNum = phoneNum;
		this.userName = userName;
		this.movieName = movieName;
		this.seatNum = seatNum;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMovieName() {
		return movieName;
	}

	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}

	public int getSeatNum() {
		return seatNum;
	}

	public void setSeatNum(int seatNum) {
		this.seatNum = seatNum;
	}

	@Override
	public String toString() {
		return "Reservation [phoneNum=" + phoneNum + ", userName=" + userName + ", movieName=" + movieName
				+ ", seatNum=" + seatNum + "]";
	}

}
