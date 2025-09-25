package com.sk.skala.stockapi.service;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.sk.skala.stockapi.config.Constant;
import com.sk.skala.stockapi.config.Error;
import com.sk.skala.stockapi.data.dto.PlayerSession;
import com.sk.skala.stockapi.exception.ResponseException;
import com.sk.skala.stockapi.tools.JsonTool;
import com.sk.skala.stockapi.tools.JwtTool;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;


/**
 * SessionHandler
 * 
 * 세션 관리
 * 
 * JWT 토큰을 쿠키에 저장/조회하여 플레이어 인증 상태 확인
 */
@Component
public class SessionHandler {

	/**
	 * 현재 요청의 JWT 쿠키를 검증하여 PlayerSession 객체로 반환
	 * 
	 * @return 로그인한 플레이어 세션 정보
	 * @throw ResponseException 쿠키가 없거나 유효하지 않은 경우
	 */
	public PlayerSession getPlayerSession() {
		ServletRequestAttributes attributes =
				(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (Constant.JWT_ACCESS_COOKIE.equals(cookie.getName())) {
					String payload = JwtTool.getValidPayload(cookie.getValue());
					return JsonTool.toObject(payload, PlayerSession.class);
				}
			}
		}
		throw new ResponseException(Error.SESSION_NOT_FOUND);
	}

	/**
	 * 현재 로그인된 플레이어 ID 반환
	 * 
	 * @return 플레이어 ID (없는 경우 null 반환)
	 */
	public String getPlayerId() {
		PlayerSession playerSession = getPlayerSession();
		return playerSession != null ? playerSession.getPlayerId() : null;
	}

	/**
	 * JWT 토큰을 생성하여 쿠키에 저장
	 * 
	 * @param playerSession 로그인 성공 시의 플레이어 세션 객체
	 * @return password를 비운 PlayerSesson
	 */
	public PlayerSession storeAccessToken(PlayerSession playerSession) {
		// hide password
		playerSession.setPlayerPassword("");

		String token = JwtTool.generateToken(playerSession.getPlayerId(), playerSession);
		Cookie cookie = new Cookie(Constant.JWT_ACCESS_COOKIE, token);
		cookie.setMaxAge(Constant.JWT_ACCESS_TTL);
		cookie.setPath("/");
		cookie.setSecure(false);

		ServletRequestAttributes attr =
				(ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		attr.getResponse().addCookie(cookie);

		return playerSession;
	}

}
