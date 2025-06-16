package controller;

import java.sql.*;
import java.util.ArrayList;
import model.ReviewVO;

public class ReviewDAO {

	/* ---------- SQL ---------- */
	private static final String SELECT_ALL = "SELECT reviewNum, movieNum, reviewRate, commentary "
			+ "FROM review ORDER BY reviewNum";

	private static final String INSERT = "INSERT INTO review (reviewNum, movieNum, reviewRate, commentary) "
			+ "VALUES (review_seq.NEXTVAL, ?, ?, ?)";

	/* ========== 전체조회 ========== */
	public ArrayList<ReviewVO> selectAll() {
		ArrayList<ReviewVO> list = new ArrayList<>();
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement ps = conn.prepareStatement(SELECT_ALL);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				list.add(new ReviewVO(rs.getInt("reviewNum"), rs.getInt("movieNum"), rs.getDouble("reviewRate"),
						rs.getString("commentary")));
			}
		} catch (SQLException e) {
			System.out.println("ReviewDAO.selectAll 오류: " + e.getMessage());
		}
		return list;
	}

	/* ========== 등록 (시퀀스) ========== */
	public void insert(ReviewVO rv) {
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(INSERT)) {

			ps.setInt(1, rv.getMovieNum());
			ps.setDouble(2, rv.getReviewRate());
			ps.setString(3, rv.getCommentary());
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("ReviewDAO.insert 오류: " + e.getMessage());
		}

	}

	public int getNextReviewNum() {
		String sql = "SELECT review_seq.NEXTVAL FROM dual";
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			return rs.next() ? rs.getInt(1) : 0;
		} catch (SQLException e) {
			System.out.println("ReviewDAO.getNextReviewNum 오류: " + e.getMessage());
			return 0;
		}
	}
}
