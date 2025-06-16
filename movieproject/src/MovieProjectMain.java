
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.regex.Pattern;

import controller.DBUtil;
import controller.MovieDAO;
import controller.ReservationDAO;
import controller.ReviewDAO;
import model.MovieVO;
import model.ReservationVO;
import model.ReviewVO;
import view.MainMenu;

/**
 * Movie reservation project – 전역 Scanner·DAO 호환 최종판
 */
public class MovieProjectMain {

	/* ---------- 전역 Scanner ---------- */
	private static final Scanner SCANNER = new Scanner(System.in);

	/* -------------------------------------------------- main */
	public static void main(String[] args) throws IOException {
		ArrayList<MovieVO> mvList = new ArrayList<>();
		ArrayList<ReservationVO> rsList = new ArrayList<>();
		ArrayList<ReviewVO> rvList = new ArrayList<>();

		MovieDAO mDAO = new MovieDAO();
		ReservationDAO rsDAO = new ReservationDAO();
		ReviewDAO rvDAO = new ReviewDAO();

		Connection con = DBUtil.getConnection();
		if (con == null) {
			System.out.println("DB 연결 오류 – 종료");
			return;
		}

		mvList = mDAO.selectAll();
		rsList = rsDAO.selectAll();
		rvList = rvDAO.selectAll();

		boolean stop = false;
		while (!stop) {
			MainMenu.menuDisplay();
			switch (selectNo()) {
			case 1 -> userMenu(mvList, rsList, rvList, mDAO, rsDAO, rvDAO);
			case 2 -> adminMenu(mvList, rsList, rvList, mDAO, rsDAO, rvDAO);
			case 3 -> stop = true;
			}
		}
		System.out.println("종료합니다. 감사합니다.");
		// SCANNER.close(); // 절대로 닫지 않는다
	}

	/* ================= USER MENU ================= */
	private static void userMenu(ArrayList<MovieVO> mvList, ArrayList<ReservationVO> rsList, ArrayList<ReviewVO> rvList,
			MovieDAO mDAO, ReservationDAO rsDAO, ReviewDAO rvDAO) {
		MainMenu.userDisplay();
		switch (selectNo()) {
		case 1 -> showMovieStats(mvList);
		case 2 -> paginateMovieList(mDAO.selectAll());
		case 3 -> {
			rsList = rsDAO.selectAll();
			reservationMenu(rsList, mvList, rsDAO, mDAO);
		}
		case 4 -> {
			rvList = rvDAO.selectAll();
			reviewMenu(rvList, rvDAO);
		}
		}
	}

	/* ================= ADMIN MENU ================= */
	private static void adminMenu(ArrayList<MovieVO> mvList, ArrayList<ReservationVO> rsList,
			ArrayList<ReviewVO> rvList, MovieDAO mDAO, ReservationDAO rsDAO, ReviewDAO rvDAO) {
		if (!login())
			return;
		MainMenu.managerDisplay();
		switch (selectNo()) {
		case 1 -> showMovieStats(mvList);
		case 2 -> paginateMovieList(mDAO.selectAll());
		case 3 -> {
			rsList = rsDAO.selectAll();
			reservationMenu(rsList, mvList, rsDAO, mDAO);
		}
		case 4 -> {
			rvList = rvDAO.selectAll();
			reviewMenu(rvList, rvDAO);
		}
		case 5 -> movieCrudMenu(mvList, mDAO);
		}
	}

	/* =============== MOVIE STATS ================== */
	private static void showMovieStats(ArrayList<MovieVO> mvList) {
		MainMenu.menuDisplay2();
		switch (selectNo()) {
		case 1 -> {
			Collections.sort(mvList);
			System.out.println("오름차순 정렬 완료");
		}
		case 2 -> {
			Collections.sort(mvList, Collections.reverseOrder());
			System.out.println("내림차순 정렬 완료");
		}
		case 3 -> mvList.stream().max(MovieVO::compareTo).ifPresent(m -> System.out.println("최다 예매: " + m));
		case 4 -> mvList.stream().min(MovieVO::compareTo).ifPresent(m -> System.out.println("최소 예매: " + m));
		}
		pause();
	}

	/* =============== MOVIE PAGING ================= */
	private static void paginateMovieList(ArrayList<MovieVO> list) {
		int page = 1, total = (int) Math.ceil(list.size() / 5.0);
		while (true) {
			int start = 5 * (page - 1);
			int end = Math.min(start + 5, list.size());
			System.out.printf("전체 %d / 현재 %d%n", total, page);
			for (int i = start; i < end; i++)
				System.out.println(list.get(i));
			System.out.printf("page[1-%d](-1 종료): ", total);
			try {
				page = Integer.parseInt(SCANNER.nextLine());
				if (page == -1)
					break;
				if (page < 1 || page > total)
					page = 1;
			} catch (NumberFormatException e) {
				page = 1;
			}
		}
	}

	/* =============== RESERVATION MENU ============= */
	private static void reservationMenu(ArrayList<ReservationVO> rsList, ArrayList<MovieVO> mvList,
			ReservationDAO rsDAO, MovieDAO mDAO) {
		MainMenu.menuDisplay3();
		switch (selectNo()) {
		case 1 -> reservationInsert(rsList, mvList, rsDAO, mDAO);
		case 2 -> reservationCancel(rsList, rsDAO);
		case 3 -> reservationSearchByPhoneDB();
		case 4 -> reservationUpdate(rsList, rsDAO, mDAO);
		}
	}

	private static void reservationInsert(ArrayList<ReservationVO> rsList, ArrayList<MovieVO> mvList,
			ReservationDAO rsDAO, MovieDAO mDAO) {
		String phone = patternInspection("전화(000-0000-0000): ", "\\d{3}-\\d{4}-\\d{4}");
		String name = patternInspection("이름: ", "^[가-힣]{2,4}$");
		System.out.print("영화명: ");
		String movie = SCANNER.nextLine().trim();
		int seat = Integer.parseInt(patternInspection("좌석(0-999): ", "^[0-9]{1,3}$"));

		for (ReservationVO r : rsList)
			if (normalizePhone(r.getPhoneNum()).equals(normalizePhone(phone)) && r.getSeatNum() == seat) {
				System.out.println("이미 예매된 좌석");
				pause();
				return;
			}

		ReservationVO res = new ReservationVO(phone, name, movie, seat);
		rsList.add(res);
		rsDAO.insertIfNotExists(res);

		int cnt = rsDAO.countByMovie(movie);
		mDAO.updateReservationCount(movie, cnt);
		mvList.stream().filter(m -> m.getMovieName().equals(movie)).findFirst()
				.ifPresent(m -> m.setReservationCount(cnt));

		System.out.printf("예매 완료 (총 %d)%n", cnt);
		pause();
	}

	private static void reservationCancel(ArrayList<ReservationVO> rsList, ReservationDAO rsDAO) {
		String phone = patternInspection("취소 전화: ", "\\d{3}-\\d{4}-\\d{4}");
		
		int count = rsDAO.deleteByPhone(phone);
		if(count != 0) {
			System.out.println("취소 완료");
		}else {
			System.out.println("예매내역이 없습니다.");
		}
		
		pause();
	}

	private static void reservationSearchByPhoneDB() {
		String phone = patternInspection("조회 전화: ", "\\d{3}-\\d{4}-\\d{4}");
		try (Connection conn = DBUtil.getConnection()) {
			String sql = "SELECT * FROM reservation WHERE PHONENUM = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, phone);
			ResultSet rs = ps.executeQuery();
			boolean found = false;
			while (rs.next()) {
				System.out.printf("예매자:%s | 영화:%s | 좌석:%d\n", rs.getString("userName"), rs.getString("movieName"),
						rs.getInt("seatNum"));
				found = true;
			}
			if (!found)
				System.out.println("DB에 내역 없음");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		pause();
	}

	private static void reservationUpdate(ArrayList<ReservationVO> rsList, ReservationDAO rsDAO, MovieDAO mDAO) {
		String phone = patternInspection("수정 전화: ", "\\d{3}-\\d{4}-\\d{4}");
		ReservationVO r = rsList.stream().filter(v -> normalizePhone(v.getPhoneNum()).equals(normalizePhone(phone)))
				.findFirst().orElse(null);
		if (r == null) {
			System.out.println("없음");
			pause();
			return;
		}

		String name = patternInspection("새 이름: ", "^[가-힣]{2,4}$");
		System.out.print("새 영화명: ");
		String movie = SCANNER.nextLine().trim();
		int seat = Integer.parseInt(patternInspection("새 좌석: ", "^[0-9]{1,3}$"));

		r.setUserName(name);
		r.setMovieName(movie);
		r.setSeatNum(seat);
		rsDAO.update(r);

		int cnt = rsDAO.countByMovie(movie);
		mDAO.updateReservationCount(movie, cnt);
		System.out.println("수정 완료");
		pause();
	}

	/* =============== REVIEW MENU ================= */
	private static void reviewMenu(ArrayList<ReviewVO> rvList, ReviewDAO rvDAO) {
		MainMenu.menuDisplay4(); // 1.등록 2.목록
		switch (selectNo()) {
		case 1 -> reviewInsert(rvList, rvDAO);
		case 2 -> reviewPaging(rvDAO);
		}
	}

	private static void reviewInsert(ArrayList<ReviewVO> rvList, ReviewDAO rvDAO) {
		int movieNum = Integer.parseInt(patternInspection("영화 번호: ", "^[0-9]+$"));
		double rate = Double.parseDouble(patternInspection("평점(0~10): ", "^(?:\\d|10)(?:\\.\\d)?$"));
		System.out.print("한줄평: ");
		String comment = SCANNER.nextLine();

		ReviewVO rv = new ReviewVO(0, movieNum, rate, comment); // PK는 시퀀스로
		rvList.add(rv);
		rvDAO.insert(rv);
		System.out.println("리뷰 등록 완료");
		pause();
	}

	private static void reviewPaging(ReviewDAO rvDAO) {
		ArrayList<ReviewVO> list = rvDAO.selectAll();
		int page = 1, total = (int) Math.ceil(list.size() / 5.0);
		while (true) {
			int start = 5 * (page - 1);
			int end = Math.min(start + 5, list.size());
			System.out.printf("전체 %d / 현재 %d%n", total, page);
			for (int i = start; i < end; i++)
				System.out.println(list.get(i));
			System.out.printf("page[1-%d](-1 종료): ", total);
			try {
				page = Integer.parseInt(SCANNER.nextLine());
				if (page == -1)
					break;
				if (page < 1 || page > total)
					page = 1;
			} catch (NumberFormatException e) {
				page = 1;
			}
		}
	}

	/* =============== MOVIE CRUD (ADMIN) ============ */
	private static void movieCrudMenu(ArrayList<MovieVO> mvList, MovieDAO mDAO) {
		MainMenu.menuDisplay5(); // 1.등록 2.수정 3.삭제
		switch (selectNo()) {
		case 1 -> movieInsert(mvList, mDAO);
		case 2 -> movieUpdate(mvList, mDAO);
		case 3 -> movieDelete(mvList, mDAO);
		}
	}

	private static void movieInsert(ArrayList<MovieVO> mvList, MovieDAO mDAO) {
		System.out.print("영화 번호: ");
		int num = Integer.parseInt(SCANNER.nextLine());
		System.out.print("영화 제목: ");
		String name = SCANNER.nextLine();
		System.out.print("개봉일 (YYYY-MM-DD): ");
		String date = SCANNER.nextLine();

		MovieVO movie = new MovieVO(num, name, date, 0);
		mvList.add(movie);
		mDAO.insert(movie);
		System.out.println("영화 등록 완료");
		pause();
	}

	private static void movieUpdate(ArrayList<MovieVO> mvList, MovieDAO mDAO) {
		System.out.print("수정할 영화 번호: ");
		int num = Integer.parseInt(SCANNER.nextLine());
		MovieVO movie = mvList.stream().filter(m -> m.getMovieNum() == num).findFirst().orElse(null);
		if (movie == null) {
			System.out.println("해당 영화 없음");
			pause();
			return;
		}

		System.out.print("새 제목: ");
		String newName = SCANNER.nextLine();
		System.out.print("새 개봉일: ");
		String newDate = SCANNER.nextLine();

		movie.setMovieName(newName);
		movie.setReleaseDate(newDate);
		mDAO.update(movie);
		System.out.println("수정 완료");
		pause();
	}

	private static void movieDelete(ArrayList<MovieVO> mvList, MovieDAO mDAO) {
		System.out.print("삭제할 영화 번호: ");
		int num = Integer.parseInt(SCANNER.nextLine());
		if (mvList.removeIf(m -> m.getMovieNum() == num)) {
			mDAO.delete(num);
			System.out.println("삭제 완료");
		} else {
			System.out.println("해당 영화 없음");
		}
		pause();
	}

	/* =============== UTIL ================= */
	private static int selectNo() {
		System.out.print("번호 입력: ");
		try {
			return Integer.parseInt(SCANNER.nextLine());
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	private static boolean login() {
		System.out.print("관리자 PW: ");
		return "admin1234".equals(SCANNER.nextLine());
	}

	private static void pause() {
		System.out.print("엔터를 누르세요...");
		try {
			SCANNER.nextLine();
		} catch (Exception ignored) {
		}
	}

	private static String normalizePhone(String p) {
		return p == null ? "" : p.replace("-", "");
	}

	private static String patternInspection(String msg, String regex) {
		while (true) {
			System.out.print(msg);
			String s = SCANNER.nextLine();
			if (Pattern.matches(regex, s))
				return s;
			System.out.println("형식 오류. 다시 입력하세요.");
		}
	}
}
