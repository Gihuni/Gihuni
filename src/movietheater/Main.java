package movietheater;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
	public static String menuTitle1;
	public static String menuTitle2;
	public static String menuTitle3;

	public static void main(String[] args) throws IOException {
		// 변수선언
		ArrayList<Movie> mvList = new ArrayList<Movie>();
		ArrayList<Reservation> rsList = new ArrayList<Reservation>();
		ArrayList<Review> rvList = new ArrayList<Review>();

		movieFileUpload(mvList);
		reservationFileUpload(rsList);
		reviewFileUpload(rvList);

		boolean stopFlag = false;
		// 반복
		while (!stopFlag) {
			// 메뉴
			MainMenu.menuDisplay();
			int no = 0;
			no = selectNo();

			// 메뉴 선택 실행
			switch (no) {
			case 1: {
				// 메뉴
				MainMenu.userDisplay();
				int num1 = 0;
				num1 = selectNo2();
				switch (num1) {
				case 1:
					MainMenu.menuDisplay2();
					int num2 = 0;
					num2 = selectNo2();
					switch (num2) {
					case 1: {
						Scanner scan = new Scanner(System.in);
						Collections.sort(mvList);
						System.out.println("영화 예매순위를 오름차순으로 정렬했습니다.");
						System.out.print("계속 >");
						scan.nextLine();
					}
						break;
					case 2: {
						Scanner scan = new Scanner(System.in);
						Collections.sort(mvList, Collections.reverseOrder());
						System.out.println("영화 예매순위를 내림차순으로 정렬했습니다.");
						System.out.print("계속 >");
						scan.nextLine();
					}
						break;
					case 3: {
						Scanner scan = new Scanner(System.in);
						int max = Integer.MIN_VALUE;
						int index = -1;
						for (int i = 0; i < mvList.size(); i++) {
							Movie mvData = mvList.get(i);
							if (max < mvData.getReservationCount()) {
								max = mvData.getReservationCount();
								index = i;
							}
						}
						Movie mv = new Movie();
						mv.setReservationCount(max);
						System.out.printf("가장 예매수가 높은 영화의 예매수가 %5d 입니다.\n", max);
						System.out.printf("최다 예매 영화의 정보:\n%s\n", mvList.get(index));
						System.out.print("계속 >");
						scan.nextLine();
					}
						break;
					case 4: {
						Scanner scan = new Scanner(System.in);
						int min = Integer.MAX_VALUE;
						int index = -1;
						for (int i = 0; i < mvList.size(); i++) {
							Movie mvData = mvList.get(i);
							if (min > mvData.getReservationCount()) {
								min = mvData.getReservationCount();
								index = i;
							}
						}
						Movie mv = new Movie();
						mv.setReservationCount(min);
						System.out.printf("가장 예매수가 낮은 영화의 예매수는 %5d 입니다.\n", min);
						System.out.printf("최소 예매 영화의 정보:\n%s\n", mvList.get(index));
						System.out.print("계속 >");
						scan.nextLine();
					}
						break;
					}// switch(num2)
					break;
				case 2: {
					Scanner scan = new Scanner(System.in);
					int page = 1;
					while (true) {
						// 전체페이지
						int totalPage = mvList.size() / 5;
						int remainValue = mvList.size() % 5;
						if (remainValue != 0) {
							totalPage += 1;
						}
						// 해당페이지 시작위치 끝위치 정하기
						int start = 5 * (page - 1);
						int stop = start + 5;

						// 마지막 페이지일때 나머지값이 있을 때 끝위치 1~4증가
						if (page == totalPage && remainValue != 0) {
							stop = start + remainValue;
						}
						System.out.printf("전체 %d 페이지 / 현재 %d 페이지\n", totalPage, page);
						for (int i = start; i < stop; i++) {
							System.out.println(mvList.get(i).toString());
						}
						System.out.printf("page[1-%d](-1 : 종료) 페이지선택 > ", totalPage);
						try {
							page = Integer.parseInt(scan.nextLine());
							if (page == -1)
								break;
							if (page < 1 || page > totalPage) {
								System.out.println("유효한 페이지 범위를 입력해주세요.");
								page = 1;
							}
						} catch (NumberFormatException e) {
							System.out.println("숫자를 입력해주세요.");
							page = 1;
						}
					} // while
				}
					break;
				case 3:
					MainMenu.menuDisplay3();
					int num3 = 0;
					num3 = selectNo2();
					switch (num3) {
					case 1: {
						Scanner scan = new Scanner(System.in);
						Reservation rs = new Reservation();
						String userName = null;
						int seatNum = 0;
						System.out.print("예매자 전화번호 : ");
						String phoneNum = scan.nextLine();
						rs.setPhoneNum(phoneNum);
						do {
							System.out.print("예매자 이름 : ");
							String _userName = scan.nextLine();
							boolean isInputCheck = Pattern.matches("^[가-힣]{2,4}$", _userName);
							if (isInputCheck) {
								rs.setUserName(_userName);
								break;
							}
							System.out.println("이름을 제대로 입력해주세요.");
						} while (true);

						System.out.print("예매할 영화 이름 : ");
						String movieName = scan.nextLine();
						rs.setMovieName(movieName);
						do {
							System.out.print("예매할 좌석 번호 : ");
							int _seatNum = Integer.parseInt(scan.nextLine());
							boolean isInputCheck2 = Pattern.matches("^[0-9]{1,3}$", String.valueOf(_seatNum));
							if (isInputCheck2) {
								rs.setSeatNum(_seatNum);
								break;
							}
							System.out.println("0~999 사이의 값을 입력해주세요");
						} while (true);

						for (Movie data : mvList) {
							if (data.getMovieName().equals(movieName)) {
								data.setReservationCount(data.getReservationCount() + 1);
							}
						}
						rsList.add(rs);
						System.out.printf("%s 영화 예매가 완료되었습니다.", movieName);
						reservationSave(rsList);
						movieSave(mvList);
						System.out.print("계속 >");
						scan.nextLine();
					}
						break;
					case 2: {
						Scanner scan = new Scanner(System.in);
						System.out.print("예매 취소할 사람의 전화번호 : ");
						String phoneNum = scan.nextLine();
						boolean findFlag = false;
						for (Reservation rs : rsList) {
							if (rs.getPhoneNum().equals(phoneNum)) {
								System.out.println(rs.toString());
								findFlag = true;
								rsList.remove(rs);
								reservationSave(rsList);
								break;
							}
						}
						if (findFlag == false) {
							System.out.printf("%s 는 찾을 수 없는 번호입니다.\n", phoneNum);
						}
					}
						break;
					case 3: {
						Scanner scan = new Scanner(System.in);
						System.out.println("예매자의 전화번호를 입력해 주세요 : ");
						String phoneNum = scan.nextLine();
						boolean searchFlag = false;
						for (Reservation rs : rsList) {
							if (rs.getPhoneNum().equals(phoneNum)) {
								System.out.println(rs.toString());
								searchFlag = true;
							}
						}
						if (searchFlag == false) {
							System.out.printf("%s는 찾을 수 없는 번호입니다.\n", phoneNum);
						}
						System.out.print("계속 >");
						scan.nextLine();
					}
						break;
					case 4:
						reservationUpdate(rsList);
						for (Movie mv : mvList) {
							int count = 0;
							String mvName = mv.getMovieName();
							for (Reservation rs : rsList) {
								if (mvName.equals(rs.getMovieName())) {
									count++;
								}
							}
							mv.setReservationCount(count);
						}
						reservationSave(rsList);
						movieSave(mvList);
						break;
					}
					break;
				case 4:
					MainMenu.menuDisplay4();
					int num4 = 0;
					num4 = selectNo3();
					switch (num4) {
					case 1: {
						Scanner scan = new Scanner(System.in);
						// 리뷰 번호 증가식 필요
						int max = Integer.MIN_VALUE;
						int index = -1;
						for (int i = 0; i < rvList.size(); i++) {
							Review rvData = rvList.get(i);
							if (max < rvData.getReviewNum()) {
								max = rvData.getReviewNum();
								index = i;
							}
						}
						System.out.print("리뷰 남길 영화 번호 : ");
						int movieNum = Integer.parseInt(scan.nextLine());
						System.out.print("리뷰 평점 : ");
						double reviewRate = Double.parseDouble(scan.nextLine());
						System.out.print("한줄평 : ");
						String comment = scan.nextLine();
						Review rv = new Review(max + 1, movieNum, reviewRate, comment);
						rvList.add(rv);
						System.out.printf("%d 영화의 리뷰가 추가되었습니다.\n", movieNum);
						reviewSave(rvList);
						System.out.print("계속 >");
						scan.nextLine();
					}
						break;
					case 2: {
						Scanner scan = new Scanner(System.in);
						int page = 1;
						while (true) {
							// 전체페이지
							int totalPage = rvList.size() / 5;
							int remainValue = rvList.size() % 5;
							if (remainValue != 0) {
								totalPage += 1;
							}
							// 해당페이지 시작위치 끝위치 정하기
							int start = 5 * (page - 1);
							int stop = start + 5;

							// 마지막 페이지일때 나머지값이 있을 때 끝위치 1~4증가
							if (page == totalPage && remainValue != 0) {
								stop = start + remainValue;
							}
							System.out.printf("전체 %d 페이지 / 현재 %d 페이지\n", totalPage, page);
							for (int i = start; i < stop; i++) {
								System.out.println(rvList.get(i).toString());
							}
							System.out.printf("page[1-%d](-1 : 종료) 페이지선택 > ", totalPage);
							try {
								page = Integer.parseInt(scan.nextLine());
								if (page == -1)
									break;
								if (page < 1 || page > totalPage) {
									System.out.println("유효한 페이지 범위를 입력해주세요.");
									page = 1;
								}
							} catch (NumberFormatException e) {
								System.out.println("숫자를 입력해주세요.");
								page = 1;
							}
						} // while
					}
						break;
					}
				}// switch(num1)
				break;
			}
			case 2: {
				while (true) {
					if (login()) {
						break;
					} else {
						System.out.println("아이디와 비밀번호를 다시 확인해주세요.");
					}
				}
				// 메뉴
				MainMenu.managerDisplay();
				int num1 = 0;
				num1 = selectNo4();
				switch (num1) {
				case 1:
					MainMenu.menuDisplay2();
					int num2 = 0;
					num2 = selectNo2();
					switch (num2) {
					case 1: {
						Scanner scan = new Scanner(System.in);
						Collections.sort(mvList);
						System.out.println("영화 예매순위를 오름차순으로 정렬했습니다.");
						System.out.print("계속 >");
						scan.nextLine();
					}
						break;
					case 2: {
						Scanner scan = new Scanner(System.in);
						Collections.sort(mvList, Collections.reverseOrder());
						System.out.println("영화 예매순위를 내림차순으로 정렬했습니다.");
						System.out.print("계속 >");
						scan.nextLine();
					}
						break;
					case 3: {
						Scanner scan = new Scanner(System.in);
						int max = Integer.MIN_VALUE;
						int index = -1;
						for (int i = 0; i < mvList.size(); i++) {
							Movie mvData = mvList.get(i);
							if (max < mvData.getReservationCount()) {
								max = mvData.getReservationCount();
								index = i;
							}
						}
						Movie mv = new Movie();
						mv.setReservationCount(max);
						System.out.printf("가장 예매수가 높은 영화의 예매수가 %5d 입니다.\n", max);
						System.out.printf("최다 예매 영화의 정보:\n%s\n", mvList.get(index));
						System.out.print("계속 >");
						scan.nextLine();
					}
						break;
					case 4: {
						Scanner scan = new Scanner(System.in);
						int min = Integer.MAX_VALUE;
						int index = -1;
						for (int i = 0; i < mvList.size(); i++) {
							Movie mvData = mvList.get(i);
							if (min > mvData.getReservationCount()) {
								min = mvData.getReservationCount();
								index = i;
							}
						}
						Movie mv = new Movie();
						mv.setReservationCount(min);
						System.out.printf("가장 예매수가 낮은 영화의 예매수는 %5d 입니다.\n", min);
						System.out.printf("최소 예매 영화의 정보:\n%s\n", mvList.get(index));
						System.out.print("계속 >");
						scan.nextLine();
					}
						break;
					}// switch(num2)
					break;
				case 2: {
					Scanner scan = new Scanner(System.in);
					int page = 1;
					while (true) {
						// 전체페이지
						int totalPage = mvList.size() / 5;
						int remainValue = mvList.size() % 5;
						if (remainValue != 0) {
							totalPage += 1;
						}
						// 해당페이지 시작위치 끝위치 정하기
						int start = 5 * (page - 1);
						int stop = start + 5;

						// 마지막 페이지일때 나머지값이 있을 때 끝위치 1~4증가
						if (page == totalPage && remainValue != 0) {
							stop = start + remainValue;
						}
						System.out.printf("전체 %d 페이지 / 현재 %d 페이지\n", totalPage, page);
						for (int i = start; i < stop; i++) {
							System.out.println(mvList.get(i).toString());
						}
						System.out.printf("page[1-%d](-1 : 종료) 페이지선택 > ", totalPage);
						try {
							page = Integer.parseInt(scan.nextLine());
							if (page == -1)
								break;
							if (page < 1 || page > totalPage) {
								System.out.println("유효한 페이지 범위를 입력해주세요.");
								page = 1;
							}
						} catch (NumberFormatException e) {
							System.out.println("숫자를 입력해주세요.");
							page = 1;
						}
					} // while
				}
					break;
				case 3:
					MainMenu.menuDisplay3();
					int num3 = 0;
					num3 = selectNo2();
					switch (num3) {
					case 1: {
						Scanner scan = new Scanner(System.in);
						Reservation rs = new Reservation();
						String userName = null;
						int seatNum = 0;
						System.out.print("예매자 전화번호 : ");
						String phoneNum = scan.nextLine();
						rs.setPhoneNum(phoneNum);
						do {
							System.out.print("예매자 이름 : ");
							String _userName = scan.nextLine();
							boolean isInputCheck = Pattern.matches("^[가-힣]{2,4}$", _userName);
							if (isInputCheck) {
								rs.setUserName(_userName);
								break;
							}
							System.out.println("이름을 제대로 입력해주세요.");
						} while (true);

						System.out.print("예매할 영화 이름 : ");
						String movieName = scan.nextLine();
						rs.setMovieName(movieName);
						do {
							System.out.print("예매할 좌석 번호 : ");
							int _seatNum = Integer.parseInt(scan.nextLine());
							boolean isInputCheck2 = Pattern.matches("^[0-9]{1,3}$", String.valueOf(_seatNum));
							if (isInputCheck2) {
								rs.setSeatNum(_seatNum);
								break;
							}
							System.out.println("0~999 사이의 값을 입력해주세요");
						} while (true);

						for (Movie data : mvList) {
							if (data.getMovieName().equals(movieName)) {
								data.setReservationCount(data.getReservationCount() + 1);
							}
						}
						rsList.add(rs);
						System.out.printf("%s 영화 예매가 완료되었습니다.", movieName);
						reservationSave(rsList);
						movieSave(mvList);
						System.out.print("계속 >");
						scan.nextLine();
					}
						break;
					case 2: {
						Scanner scan = new Scanner(System.in);
						System.out.print("예매 취소할 사람의 전화번호 : ");
						String phoneNum = scan.nextLine();
						boolean findFlag = false;
						for (Reservation rs : rsList) {
							if (rs.getPhoneNum().equals(phoneNum)) {
								System.out.println(rs.toString());
								findFlag = true;
								rsList.remove(rs);
								reservationSave(rsList);
								break;
							}
						}
						if (findFlag == false) {
							System.out.printf("%s 는 찾을 수 없는 번호입니다.\n", phoneNum);
						}
					}
						break;
					case 3: {
						Scanner scan = new Scanner(System.in);
						System.out.println("예매자의 전화번호를 입력해 주세요 : ");
						String phoneNum = scan.nextLine();
						boolean searchFlag = false;
						for (Reservation rs : rsList) {
							if (rs.getPhoneNum().equals(phoneNum)) {
								System.out.println(rs.toString());
								searchFlag = true;
							}
						}
						if (searchFlag == false) {
							System.out.printf("%s는 찾을 수 없는 번호입니다.\n", phoneNum);
						}
						System.out.print("계속 >");
						scan.nextLine();
					}
						break;
					case 4:
						reservationUpdate(rsList);
						for (Movie mv : mvList) {
							int count = 0;
							String mvName = mv.getMovieName();
							for (Reservation rs : rsList) {
								if (mvName.equals(rs.getMovieName())) {
									count++;
								}
							}
							mv.setReservationCount(count);
						}
						reservationSave(rsList);
						movieSave(mvList);
						break;
					}
					break;
				case 4:
					MainMenu.menuDisplay4();
					int num4 = 0;
					num4 = selectNo3();
					switch (num4) {
					case 1: {
						Scanner scan = new Scanner(System.in);
						// 리뷰 번호 증가식 필요
						int max = Integer.MIN_VALUE;
						int index = -1;
						for (int i = 0; i < rvList.size(); i++) {
							Review rvData = rvList.get(i);
							if (max < rvData.getReviewNum()) {
								max = rvData.getReviewNum();
								index = i;
							}
						}
						System.out.print("리뷰 남길 영화 번호 : ");
						int movieNum = Integer.parseInt(scan.nextLine());
						System.out.print("리뷰 평점 : ");
						double reviewRate = Double.parseDouble(scan.nextLine());
						System.out.print("한줄평 : ");
						String comment = scan.nextLine();
						Review rv = new Review(max + 1, movieNum, reviewRate, comment);
						rvList.add(rv);
						System.out.printf("%d 영화의 리뷰가 추가되었습니다.\n", movieNum);
						reviewSave(rvList);
						System.out.print("계속 >");
						scan.nextLine();
					}
						break;
					case 2: {
						Scanner scan = new Scanner(System.in);
						int page = 1;
						while (true) {
							// 전체페이지
							int totalPage = rvList.size() / 5;
							int remainValue = rvList.size() % 5;
							if (remainValue != 0) {
								totalPage += 1;
							}
							// 해당페이지 시작위치 끝위치 정하기
							int start = 5 * (page - 1);
							int stop = start + 5;

							// 마지막 페이지일때 나머지값이 있을 때 끝위치 1~4증가
							if (page == totalPage && remainValue != 0) {
								stop = start + remainValue;
							}
							System.out.printf("전체 %d 페이지 / 현재 %d 페이지\n", totalPage, page);
							for (int i = start; i < stop; i++) {
								System.out.println(rvList.get(i).toString());
							}
							System.out.printf("page[1-%d](-1 : 종료) 페이지선택 > ", totalPage);
							try {
								page = Integer.parseInt(scan.nextLine());
								if (page == -1)
									break;
								if (page < 1 || page > totalPage) {
									System.out.println("유효한 페이지 범위를 입력해주세요.");
									page = 1;
								}
							} catch (NumberFormatException e) {
								System.out.println("숫자를 입력해주세요.");
								page = 1;
							}
						} // while
					}
						break;
					}
					break;
				case 5:
					MainMenu.menuDisplay5();
					int num5 = 0;
					num5 = selectNo5();
					switch (num5) {
					case 1: {
						Scanner scan = new Scanner(System.in);
						int max = Integer.MIN_VALUE;
						int index = -1;
						for (int i = 0; i < mvList.size(); i++) {
							Movie mvData = mvList.get(i);
							if (max < mvData.getMovieNum()) {
								max = mvData.getMovieNum();
								index = i;
							}
						}

						System.out.print("등록할 영화이름 : ");
						String movieName = scan.nextLine();

						System.out.print("등록할 영화 개봉일 : ");
						String releaseDate = scan.nextLine();

						Movie mv = new Movie(max + 1, movieName, releaseDate, 0);
						mvList.add(mv);
						System.out.printf("%s 영화 등록이 완료되었습니다.", movieName);
						movieSave(mvList);
						System.out.print("계속 >");
						scan.nextLine();
					}
						break;
					case 2:
						movieUpdate(mvList);
						movieSave(mvList);
						break;
					case 3: {
						Scanner scan = new Scanner(System.in);
						System.out.print("등록 취소할 영화의 번호 : ");
						int movieNum = Integer.parseInt(scan.nextLine());
						boolean findFlag = false;
						for (Movie mv : mvList) {
							if (mv.getMovieNum() == movieNum) {
								System.out.println(mv.toString());
								findFlag = true;
								mvList.remove(mv);
								movieSave(mvList);
								break;
							}
						}
						if (findFlag == false) {
							System.out.printf("%d 는 찾을 수 없는 번호입니다.\n", movieNum);
						}
					}
						break;
					}
					break;
				}// switch(num1)
				break;
			}
			case 3:
				stopFlag = true;
				break;
			}// switch(no)
		}
		System.out.println("종료하겠습니다. 감사합니다.");

	}// main

	public static void reservationUpdate(ArrayList<Reservation> rsList) {
		Scanner scan = new Scanner(System.in);
		String phoneNum = patternInspection(scan, "수정할 예매자의 번호를 입력해주세요 : ", "\\d{3}-\\d{4}-\\d{4}");
		Reservation findrs = null;
		for (Reservation rs : rsList) {
			if (rs.getPhoneNum().equals(phoneNum)) {
				findrs = rs;
				break;
			}
		}
		if (findrs == null) {
			System.out.printf("%s 번호의 예매자를 찾을 수 없습니다.\n", phoneNum);
			System.out.print("계속 >");
			scan.nextLine();
			return;
		}
		System.out.printf("%s 번호의 예매자 정보는 %s\n", phoneNum, findrs.toString());
		System.out.printf("현재 이름 : %s => ", findrs.getUserName());
		String userName = patternInspection(scan, "수정할 이름 >", "^[가-힣]{2,4}$");
		System.out.printf("현재 예약한 영화 : %s => ", findrs.getMovieName());
		String movieName = patternInspection(scan, "바꿀 영화 >", "^[A-Za-z가-힣ㄱ-ㅎ0-9]{1,20}$");
		System.out.printf("현재 예약한 좌석 : %d => ", findrs.getSeatNum());
		int seatNum = Integer.parseInt(patternInspection(scan, "바꿀 좌석 >", "^[0-9]{1,3}$"));
		findrs.setUserName(userName);
		findrs.setMovieName(movieName);
		findrs.setSeatNum(seatNum);
		System.out.printf("%s 번호의 예매 내역이 수정완료되었습니다.\n", phoneNum);
		scan.nextLine();
	}

	public static void movieUpdate(ArrayList<Movie> mvList) {
		Scanner scan = new Scanner(System.in);
		int movieNum = Integer.parseInt(patternInspection(scan, "수정할 영화의 번호를 입력해주세요 : ", "^[0-9]{1,2}$"));
		Movie findmv = null;
		for (Movie mv : mvList) {
			if (mv.getMovieNum() == movieNum) {
				findmv = mv;
				break;
			}
		}
		if (findmv == null) {
			System.out.printf("%s 번호의 영화를 찾을 수 없습니다.\n", movieNum);
			System.out.print("계속 >");
			scan.nextLine();
			return;
		}
		System.out.printf("%d 번호의 영화 정보는 %s\n", movieNum, findmv.toString());
		System.out.printf("현재 이름 : %s => ", findmv.getMovieName());
		String movieName = patternInspection(scan, "수정할 이름 >", "^[A-Za-z가-힣ㄱ-ㅎ0-9]{1,20}$");
		System.out.printf("현재 등록된 개봉일 : %s => ", findmv.getReleaseDate());
		String releaseDate = patternInspection(scan, "수정할 개봉일 >", "\\d{4}-\\d{2}-\\d{2}");
		findmv.setMovieName(movieName);
		findmv.setReleaseDate(releaseDate);
		System.out.printf("%d 번호의 영화 등록 내역이 수정완료되었습니다.\n", movieNum);
		scan.nextLine();
	}

	public static void reservationSave(ArrayList<Reservation> rsList) throws IOException {
		Scanner scan = new Scanner(System.in);
		FileOutputStream fo = new FileOutputStream("res/reservationInfoTable.txt");
		PrintStream out = new PrintStream(fo);

		// 파일메뉴를 추가한다.
		out.printf("%s", Main.menuTitle1);

		// ArrayList 내용을 한개씩 가져와서 파일에 저장한다.
		for (int i = 0; i < rsList.size(); i++) {
			Reservation rs = rsList.get(i);
			out.printf("\n%s,%s,%s,%d", rs.getPhoneNum(), rs.getUserName(), rs.getMovieName(), rs.getSeatNum());
		}

		System.out.println("저장되었습니다.");
		scan.nextLine();
		out.close();
		try {
			fo.close();
		} catch (IOException e) {
		}
	}

	public static void reviewSave(ArrayList<Review> rvList) throws IOException {
		Scanner scan = new Scanner(System.in);
		FileOutputStream fo = new FileOutputStream("res/reviewTable.txt");
		PrintStream out = new PrintStream(fo);

		// 파일메뉴를 추가한다.
		out.printf("%s", Main.menuTitle2);

		// ArrayList 내용을 한개씩 가져와서 파일에 저장한다.
		for (int i = 0; i < rvList.size(); i++) {
			Review rv = rvList.get(i);
			out.printf("\n%d,%d,%.1f,%s", rv.getReviewNum(), rv.getMovieNum(), rv.getReviewRate(), rv.getComment());
		}

		System.out.println("저장되었습니다.");
		scan.nextLine();
		out.close();
		try {
			fo.close();
		} catch (IOException e) {
		}
	}

	public static void movieSave(ArrayList<Movie> mvList) throws IOException {
		Scanner scan = new Scanner(System.in);
		FileOutputStream fo = new FileOutputStream("res/movieTable.txt");
		PrintStream out = new PrintStream(fo);

		// 파일메뉴를 추가한다.
		out.printf("%s", Main.menuTitle3);

		// ArrayList 내용을 한개씩 가져와서 파일에 저장한다.
		for (int i = 0; i < mvList.size(); i++) {
			Movie mv = mvList.get(i);
			out.printf("\n%d,%s,%s,%d", mv.getMovieNum(), mv.getMovieName(), mv.getReleaseDate(),
					mv.getReservationCount());
		}

		System.out.println("저장되었습니다.");
		scan.nextLine();
		out.close();
		try {
			fo.close();
		} catch (IOException e) {
		}
	}

	private static void reviewFileUpload(ArrayList<Review> rvList) {
		FileInputStream fi;
		try {
			fi = new FileInputStream("res/reviewTable.txt");
			Scanner scan = new Scanner(fi);
			// 첫라인 없앤다.
			if (scan.hasNextLine()) {
				Main.menuTitle2 = scan.nextLine();
			}
			while (true) {
				if (!scan.hasNextLine()) {
					break;
				}
				String data = scan.nextLine();
				String[] tokens = data.split(",");
				int reviewNum = Integer.parseInt(tokens[0]);
				int movieNum = Integer.parseInt(tokens[1]);
				double reviewRate = Double.parseDouble(tokens[2]);
				String comment = tokens[3];
				Review rv = new Review(reviewNum, movieNum, reviewRate, comment);
				rvList.add(rv);
			}
			scan.close();
			fi.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void reservationFileUpload(ArrayList<Reservation> rsList) {
		FileInputStream fi;
		try {
			fi = new FileInputStream("res/reservationInfoTable.txt");
			Scanner scan = new Scanner(fi);
			// 첫라인 없앤다.
			if (scan.hasNextLine()) {
				Main.menuTitle1 = scan.nextLine();
			}
			while (true) {
				if (!scan.hasNextLine()) {
					break;
				}
				String data = scan.nextLine();
				String[] tokens = data.split(",");
				String phoneNum = tokens[0];
				String userName = tokens[1];
				String movieName = tokens[2];

				int seatNum = Integer.parseInt(tokens[3]);
				Reservation rs = new Reservation(phoneNum, userName, movieName, seatNum);
				rsList.add(rs);

			}
			scan.close();
			fi.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void movieFileUpload(ArrayList<Movie> mvList) {
		FileInputStream fi;
		try {
			fi = new FileInputStream("res/movieTable.txt");
			Scanner scan = new Scanner(fi);
			// 첫라인 없앤다.
			if (scan.hasNextLine()) {
				Main.menuTitle3 = scan.nextLine();
			}
			while (true) {
				if (!scan.hasNextLine()) {
					break;
				}
				String data = scan.nextLine();
				String[] tokens = data.split(",");
				int movieNum = Integer.parseInt(tokens[0]);
				String movieName = tokens[1];
				String releaseDate = tokens[2];
				int reservationCount = Integer.parseInt(tokens[3]);
				Movie mv = new Movie(movieNum, movieName, releaseDate, reservationCount);
				mvList.add(mv);
			}
			scan.close();
			fi.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static int selectNo() {
		Scanner scan = new Scanner(System.in);
		int no = 0;
		no = Integer.parseInt(patternInspection(scan, "메뉴선택 : ", "^[1-3]{1}$"));
		return no;
	}

	private static int selectNo2() {
		Scanner scan = new Scanner(System.in);
		int no = 0;
		no = Integer.parseInt(patternInspection(scan, "메뉴선택 : ", "^[1-4]{1}$"));
		return no;
	}

	private static int selectNo3() {
		Scanner scan = new Scanner(System.in);
		int no = 0;
		no = Integer.parseInt(patternInspection(scan, "메뉴선택 : ", "^[1-2]{1}$"));
		return no;
	}

	private static int selectNo4() {
		Scanner scan = new Scanner(System.in);
		int no = 0;
		no = Integer.parseInt(patternInspection(scan, "메뉴선택 : ", "^[1-5]{1}$"));
		return no;
	}

	private static int selectNo5() {
		Scanner scan = new Scanner(System.in);
		int no = 0;
		no = Integer.parseInt(patternInspection(scan, "메뉴선택 : ", "^[1-3]{1}$"));
		return no;
	}

	public static boolean login() {
		Scanner scan = new Scanner(System.in);
		final String correctId = "Admin";
		final String correctPw = "Admin1234";

		System.out.print("아이디: ");
		String inputId = scan.nextLine();

		System.out.print("비밀번호: ");
		String inputPw = scan.nextLine();

		return inputId.equals(correctId) && inputPw.equals(correctPw);
	}

	public static String patternInspection(Scanner scan, String string, String regex) {
		System.out.print(string);
		String input = scan.nextLine();
		if (Pattern.matches(regex, input)) {
			return input;
		} else {
			System.out.println("유효한 값을 입력해주세요.");
			return patternInspection(scan, string, regex);
		}
	}

}
