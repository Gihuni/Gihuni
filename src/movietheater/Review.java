package movietheater;

public class Review {
	private int reviewNum;
	private int movieNum;
	private double reviewRate;
	private String comment;

	public Review() {
		this(0, 0, 0.0, null);
	}

	public Review(int reviewNum, int movieNum, double reviewRate, String comment) {
		super();
		this.reviewNum = reviewNum;
		this.movieNum = movieNum;
		this.reviewRate = reviewRate;
		this.comment = comment;
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

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "Review [reviewNum=" + reviewNum + ", movieNum=" + movieNum + ", reviewRate=" + reviewRate + ", comment="
				+ comment + "]";
	}

}
