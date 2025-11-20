package edu.thejoeun.common.util;

import edu.thejoeun.member.model.dto.Member;
import jakarta.servlet.http.HttpSession;

public class SessionUtil {
    private static final String LOGIN_USER = "loginUser";
    public static void setLoginUser(HttpSession session, Member member) {
        session.setAttribute(LOGIN_USER, member);
        session.setMaxInactiveInterval(1800);
    }
    public static void invalidateLoginUser(HttpSession session) {
        session.removeAttribute(LOGIN_USER);
    }
}
