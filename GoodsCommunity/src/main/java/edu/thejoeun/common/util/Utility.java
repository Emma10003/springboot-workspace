package edu.thejoeun.common.util;

/**
 * 프로그램에서 전체적으로 사용할 유용한 기능 모음
 */
public class Utility {
    /**
     * Cross Site Scripting(XSS) 방지 처리 <br>
     * XSS 란? 웹 어플리케이션에서 발생하는 취약점 <br>
     *         권한이 없는 사용자가 사이트에 스크립트를 작성하는 것 <br>
     * <br>
     * 문제상황 : 악의적인 사용자가 게시글이나 댓글에 < script > 해킹코드 < /script > 작성 <br>
     *          -> html 내부에서는 script 가 동작함 <br>
     *             만약 이 내용을 그대로 저장하고, 다른 사용자가 그 게시물을 볼 때 <br>
     *             이 스크립트가 그대로 실행되면, 보는 사람의 쿠키 정보(로그인 정보)를 훔쳐가거나, <br>
     *             다른 악성 사이트로 이동시킬 수 있음. <br>
     * <br>
     * 해결 방법(이 코드의 역할) : HTML 태그로 해석될 수 있는 특수문자를 안전한 문자로 변경 <br>
     *  < 문자를 &lt; 로 변경하는 형식 <br>
     *  => 브라우저는 < script > 를 "스크립트 실행" 명령으로 인식하는 게 아니라, <br>
     *     단순한 텍스트로 < script > 글자를 화면에만 표시 <br>
     * <br>
     * <br>
     * @param content 사용자가 작성한 데이터를 가져옴
     * @return 개발자가 원하는 형태로 반환
     */
    public static String XSSHandling(String content) {
        // 스크립트나 마크업 언어에서 기호나 기능을 나타내는 문자를 변경 처리

        // & - &amp;
        // < - &li;
        // > - &et;
        // " - &quot;
        content = content.replaceAll("&", "&amp;");
        content = content.replaceAll("<", "&li;");
        content = content.replaceAll(">", "&et;");
        content = content.replaceAll("\"", "&quot;");

        return content;
    }
}
