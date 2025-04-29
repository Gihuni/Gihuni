package movietheater;

import java.util.Objects;

public class Movie implements Comparable {
	// 멤버변수(영화번호, 영화제목, 날짜)
	private int movieNum;
	private String movieName;
	private String releaseDate;
	private int reservationCount;

	// 생성자
	public Movie() {
		this(0, null, null, 0);
	}

	public Movie(int movieNum, String movieName, String releaseDate, int reservationCount) {
		super();
		this.movieNum = movieNum;
		this.movieName = movieName;
		this.releaseDate = releaseDate;
		this.reservationCount = reservationCount;
	}

	// 멤버함수
	public int getMovieNum() {
		return movieNum;
	}

	public void setMovieNum(int movieNum) {
		this.movieNum = movieNum;
	}

	public String getMovieName() {
		return movieName;
	}

	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public int getReservationCount() {
		return reservationCount;
	}

	public void setReservationCount(int reservationCount) {
		this.reservationCount = reservationCount;
	}

	@Override
	public String toString() {
		return "Movie [movieNum=" + movieNum + ", movieName=" + movieName + ", releaseDate=" + releaseDate
				+ ", reservationCount=" + reservationCount + "]";
	}

	@Override
	public int compareTo(Object object) {
		Movie mv = null;
		if (object instanceof Movie) {
			mv = (Movie) object;
		}
		if (this.reservationCount > mv.getReservationCount()) {
			return 1;
		} else if (this.reservationCount < mv.getReservationCount()) {
			return -1;
		} else {
			return 0;
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(movieName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Movie other = (Movie) obj;
		return Objects.equals(movieName, other.movieName);
	}

}
