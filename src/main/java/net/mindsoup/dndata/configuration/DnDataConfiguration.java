package net.mindsoup.dndata.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Valentijn on 16-8-2019
 */
@Configuration
public class DnDataConfiguration {

	@Value("${app.version}")
	private String version;

	@Value("${archives.download.baseurl}")
	private String downloadBaseUrl;

	public String getDownloadBaseUrl() {
		return downloadBaseUrl;
	}

	public String getVersion() {
		return version;
	}
}
