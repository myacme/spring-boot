package com.example.boot.bean;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author ljx
 * @version 1.0.0
 * @create 2022/8/5 15:39
 */
@Configuration
public class SftpConfig {

	@Value("${sftp.hostname}")
	private String hostname;

	@Value("${sftp.host}")
	private String host;

	@Value("${sftp.port}")
	private Integer port;

	@Value("${sftp.password}")
	private String password;

	@Value("${sftp.timeout}")
	private Integer timeout;

	@Value("${sftp.path}")
	private String path;

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getTimeout() {
		return timeout;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}