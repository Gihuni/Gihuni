package controller;

import java.sql.*;
import java.util.ArrayList;
import model.ReservationVO;

public class ReservationDAO {

	/* ---------- SQL ---------- */
	private static final String SELECT_ALL = "SELECT phoneNum, userName, movieName, seatNum "
			+ "FROM reservation ORDER BY seatNum";

	private static final String INSERT = "INSERT INTO reservation (phoneNum, userName, movieName, seatNum) "
			+ "VALUES (?,?,?,?)";

	private static final String DUP_CHECK = "SELECT COUNT(*) FROM reservation WHERE phoneNum = ? AND seatNum = ?";

	private static final String DELETE_PHONE = "DELETE FROM reservation WHERE PHONENUM = ?";

	private static final String COUNT_BY_MOVIE = "SELECT COUNT(*) FROM reservation WHERE movieName = ?";

	private static final String UPDATE = "UPDATE reservation SET userName=?, movieName=?, seatNum=? WHERE phoneNum=?";

	/* ========== 전체조회 ========== */
	public ArrayList<ReservationVO> selectAll() {
		ArrayList<ReservationVO> list = new ArrayList<>();
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement ps = conn.prepareStatement(SELECT_ALL);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				list.add(new ReservationVO(rs.getString("phoneNum"), rs.getString("userName"),
						rs.getString("movieName"), rs.getInt("seatNum")));
			}
		} catch (SQLException e) {
			System.out.println("ReservationDAO.selectAll 오류: " + e.getMessage());
		}
		return list;
	}

	/* ========== 중복 확인 후 삽입 ========== */
	public void insertIfNotExists(ReservationVO r) {
		try (Connection conn = DBUtil.getConnection(); PreparedStatement dup = conn.prepareStatement(DUP_CHECK)) {

			dup.setString(1, r.getPhoneNum());
			dup.setInt(2, r.getSeatNum());

			ResultSet rs = dup.executeQuery();
			if (rs.next() && rs.getInt(1) == 0) {
				try (PreparedStatement ins = conn.prepareStatement(INSERT)) {
					ins.setString(1, r.getPhoneNum());
					ins.setString(2, r.getUserName());
					ins.setString(3, r.getMovieName());
					ins.setInt(4, r.getSeatNum());
					ins.executeUpdate();
				}
			}
		} catch (SQLException e) {
			System.out.println("ReservationDAO.insertIfNotExists 오류: " + e.getMessage());
		}
	}

	/* ========== 전화번호로 삭제 ========== */
	public int deleteByPhone(String phoneRaw) {
		int count;
		String phone = phoneRaw.replaceAll("-", "");
		try (Connection conn = DBUtil.getConnection(); 
				PreparedStatement ps = conn.prepareStatement(DELETE_PHONE)) {
				ps.setString(1, phoneRaw);
				count = ps.executeUpdate();
				return count;
				
		} catch (SQLException e) {
			System.out.println("ReservationDAO.deleteByPhone 오류: " + e.getMessage());
			return 0;
		}
	}

	/* ========== 영화별 예매수 ========== */
	public int countByMovie(String movieName) {
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(COUNT_BY_MOVIE)) {

			ps.setString(1, movieName);
			ResultSet rs = ps.executeQuery();
			return rs.next() ? rs.getInt(1) : 0;
		} catch (SQLException e) {
			System.out.println("ReservationDAO.countByMovie 오류: " + e.getMessage());
			return 0;
		}
	}

	/* ========== 수정 ========== */
	public void update(ReservationVO r) {
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(UPDATE)) {

			ps.setString(1, r.getUserName());
			ps.setString(2, r.getMovieName());
			ps.setInt(3, r.getSeatNum());
			ps.setString(4, r.getPhoneNum());
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("ReservationDAO.update 오류: " + e.getMessage());
		}
	}
}
