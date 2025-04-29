package movietheater;

public interface MainMenu {

	public static void menuDisplay() {
		System.out.println("***************************");
		System.out.print("1. 사용자\t");
		System.out.println("2. 관리자");
		System.out.println("3. 종료");
		System.out.println("***************************");
	}

	public static void userDisplay() {
		System.out.println("***************************");
		System.out.print("1. 영화 예매 순위\t");
		System.out.println("2. 개봉 영화 확인");
		System.out.print("3. 영화 예매\t");
		System.out.println("4. 영화 리뷰");
		System.out.println("***************************");
	}

	public static void managerDisplay() {
		System.out.println("***************************");
		System.out.print("1. 영화 예매 순위\t");
		System.out.println("2. 개봉 영화 확인");
		System.out.print("3. 영화 예매\t");
		System.out.println("4. 영화 리뷰");
		System.out.println("5. 영화 관리");
		System.out.println("***************************");
	}

	public static void menuDisplay2() {
		System.out.println("***************************");
		System.out.print("1. 영화 예매순 정렬(오름차순)\t");
		System.out.println("2. 영화 예매순 정렬(내림차순)");
		System.out.print("3. 예매순 최대값\t");
		System.out.println("4. 예매순 최소값");
		System.out.println("***************************");
	}

	public static void menuDisplay3() {
		System.out.println("***************************");
		System.out.print("1. 예매 신청\t");
		System.out.println("2. 예매 취소");
		System.out.print("3. 예매 내역 확인\t");
		System.out.println("4. 예매 수정");
		System.out.println("***************************");
	}

	public static void menuDisplay4() {
		System.out.println("***************************");
		System.out.print("1. 리뷰 작성\t");
		System.out.println("2. 리뷰 조회");
		System.out.println("***************************");
	}

	public static void menuDisplay5() {
		System.out.println("***************************");
		System.out.print("1. 영화 등록\t");
		System.out.println("2. 영화 수정");
		System.out.println("3. 영화 삭제");
		System.out.println("***************************");
	}
}
