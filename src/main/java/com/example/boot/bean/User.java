package com.example.boot.bean;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author ljx
 * @version 1.0.0
 * @create 2022/7/26 17:24
 */
@JsonIgnoreProperties(value = {"handler"})
public class User {

	private String username;

	@JsonIgnore
	private String password;

	private String yhmc;

	private String telephone;

	private String code;

	private String pic;

	private String name;

	private String email;

	private String company;

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

	public String getYhmc() {
		return yhmc;
	}

	public void setYhmc(String yhmc) {
		this.yhmc = yhmc;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}
}