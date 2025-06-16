package model;

public class ReviewVO {
	private int reviewNum;
	private int movieNum;
	private double reviewRate;
	private String commentary;

	public ReviewVO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ReviewVO(int reviewNum, int movieNum, double reviewRate, String commentary) {
		super();
		this.reviewNum = reviewNum;
		this.movieNum = movieNum;
		this.reviewRate = reviewRate;
		this.commentary = commentary;
	}

	public int getReviewNum() {
		return reviewNum;
	}

	public void setReviewNum(int reviewNum) {
		this.reviewNum = reviewNum;
	}

	public int getMovieNum() {
		return movieNum;
	}

	public void setMovieNum(int movieNum) {
		this.movieNum = movieNum;
	}

	public double getReviewRate() {
		return reviewRate;
	}

	public void setReviewRate(double reviewRate) {
		this.reviewRate = reviewRate;
	}

	public String getCommentary() {
		return commentary;
	}

	public void setCommentary(String commentary) {
		this.commentary = commentary;
	}

	@Override
	public String toString() {
		return "ReviewVO [reviewNum=" + reviewNum + ", movieNum=" + movieNum + ", reviewRate=" + reviewRate
				+ ", commentary=" + commentary + "]";
	}

}
