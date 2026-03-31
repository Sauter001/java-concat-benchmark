public class FullWidthConverter {

    // 기본값: 전각 공백
    public static String toFullWidth(String input) {
        return toFullWidth(input, '　');
    }

    // 공백 문자를 직접 지정
    public static String toFullWidth(String input, char spaceChar) {
        StringBuilder sb = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (c == ' ') {
                sb.append(spaceChar);
            } else if (c >= '!' && c <= '~') {
                sb.append((char) (c + 0xFEE0));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String board = """
                  0 1 2 3 4 5 6 7 8
                0 車 馬 象 士 + 士 象 馬 車
                1 + + + + 漢 + + + +
                2 + 包 + + + + + 包 +
                3 兵 + 兵 + 兵 + 兵 + 兵
                4 + + + + + + + + +
                5 + + + + + + + + +
                6 병 + 병 + 병 + 병 + 병
                7 + 포 + + + + + 포 +
                8 + + + + 초 + + + +
                9 차 마 상 사 + 사 상 마 차
               \s""";

        System.out.println(toFullWidth(board));
    }
}
