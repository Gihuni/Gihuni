package model;

import java.util.Objects;

public class MovieVO implements Comparable<MovieVO> {
	private int movieNum;
	private String movieName;
	private String releaseDate;
	private int reservationCount;

	public MovieVO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MovieVO(int movieNum, String movieName, String releaseDate, int reservationCount) {
		super();
		this.movieNum = movieNum;
		this.movieName = movieName;
		this.releaseDate = releaseDate;
		this.reservationCount = reservationCount;
	}

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
		return "MovieVO [movieNum=" + movieNum + ", movieName=" + movieName + ", releaseDate=" + releaseDate
				+ ", reservationCount=" + reservationCount + "]";
	}

	@Override
	public int compareTo(MovieVO other) {
		return Integer.compare(this.reservationCount, other.reservationCount);
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
		MovieVO other = (MovieVO) obj;
		return Objects.equals(movieName, other.movieName);
	}

}
