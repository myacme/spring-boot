package com.example.boot.bean;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ljx
 * @version 1.0.0
 * @create 2022/8/22 18:36
 */
@JsonIgnoreProperties(value = {"handler"})
@Component
public class LoginUser {
	@JsonIgnore
//	@ApiModelProperty(hidden = true)
	public static Map<String, String> LOGON_USER_MAP = new HashMap<>();

	@JsonIgnore
//	@ApiModelProperty(hidden = true)
	private User logonUser;

	private String username;

	private String password;

	private String telephone;

	public User getLogonUser() {
		return logonUser;
	}

	public void setLogonUser(User logonUser) {
		this.logonUser = logonUser;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
}