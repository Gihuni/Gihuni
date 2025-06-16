package controller;

import java.sql.*;
import java.util.ArrayList;
import model.MovieVO;

public class MovieDAO {

	/* ---------- SQL ---------- */
	private static final String SELECT_ALL = "SELECT movieNum, movieName, releaseDate, reservationCount "
			+ "FROM movie ORDER BY movieNum";

	private static final String INSERT = "INSERT INTO movie (movieNum, movieName, releaseDate, reservationCount) "
			+ "VALUES (movie_seq.NEXTVAL, ?, ?, 0)";

	private static final String UPDATE_RESERVATION_COUNT = "UPDATE movie SET reservationCount = ? WHERE movieName = ?";

	private static final String DELETE = "DELETE FROM movie WHERE movieNum = ?";

	/* ========== 전체 목록 ========== */
	public ArrayList<MovieVO> selectAll() {
		ArrayList<MovieVO> list = new ArrayList<>();
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement ps = conn.prepareStatement(SELECT_ALL);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				list.add(new MovieVO(rs.getInt("movieNum"), rs.getString("movieName"), rs.getString("releaseDate"),
						rs.getInt("reservationCount")));
			}
		} catch (SQLException e) {
			System.out.println("MovieDAO.selectAll 오류: " + e.getMessage());
		}
		return list;
	}

	/* ========== 등록 ========== */
	public void insert(MovieVO mv) {
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(INSERT)) {

			ps.setString(1, mv.getMovieName());
			ps.setString(2, mv.getReleaseDate());
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("MovieDAO.insert 오류: " + e.getMessage());
		}
	}

	/* ========== 예매수 갱신 ========== */
	public void updateReservationCount(String movieName, int count) {
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement ps = conn.prepareStatement(UPDATE_RESERVATION_COUNT)) {

			ps.setInt(1, count);
			ps.setString(2, movieName);
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("MovieDAO.updateReservationCount 오류: " + e.getMessage());
		}
	}

	/* ========== 삭제 ========== */
	public void delete(int movieNum) {
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(DELETE)) {

			ps.setInt(1, movieNum);
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("MovieDAO.delete 오류: " + e.getMessage());
		}
	}

	/* ========== 콘솔 인터랙티브 수정 ========== */
	public void updateInteractive(Connection conn) {
		try (java.util.Scanner sc = new java.util.Scanner(System.in)) {
			System.out.print("수정할 영화번호: ");
			int num = Integer.parseInt(sc.nextLine());
			System.out.print("새 제목: ");
			String newName = sc.nextLine();
			System.out.print("새 개봉일(YYYY-MM-DD): ");
			String newDate = sc.nextLine();

			String sql = "UPDATE movie SET movieName = ?, releaseDate = ? WHERE movieNum = ?";
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setString(1, newName);
				ps.setString(2, newDate);
				ps.setInt(3, num);

				int rows = ps.executeUpdate();
				System.out.println(rows > 0 ? "수정 완료" : "해당 영화번호 없음");
			}
		} catch (SQLException e) {
			System.out.println("MovieDAO.updateInteractive 오류: " + e.getMessage());
		}
	}

	public void update(MovieVO movie) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DBUtil.getConnection();
			String sql = "UPDATE movie SET movieName = ?, releaseDate = ? WHERE movieNum = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, movie.getMovieName());
			pstmt.setString(2, movie.getReleaseDate());
			pstmt.setInt(3, movie.getMovieNum());
			int result = pstmt.executeUpdate();
			if (result > 0) {
				System.out.println("영화 정보 수정 성공");
			} else {
				System.out.println("영화 번호가 존재하지 않습니다");
			}
		} catch (SQLException e) {
			System.out.println("update 오류: " + e.getMessage());
		} finally {
			DBUtil.dbClose(con, pstmt, null);
		}
	}
}