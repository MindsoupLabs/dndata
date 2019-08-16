package net.mindsoup.dndata.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * FtpConfiguration
 */
@Configuration
public class FtpConfiguration {

	@Value("${ftp.username}")
	private String username;
	@Value("${ftp.password}")
	private String password;
	@Value("${ftp.server}")
	private String server;
	@Value("${ftp.path}")
	private String path;

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getServer() {
		return server;
	}

	public String getPath() {
		return path;
	}
}
