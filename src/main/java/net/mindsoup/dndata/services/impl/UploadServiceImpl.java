package net.mindsoup.dndata.services.impl;

import net.mindsoup.dndata.configuration.FtpConfiguration;
import net.mindsoup.dndata.exceptions.DnDataException;
import net.mindsoup.dndata.services.UploadService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * UploadServiceImpl
 */
@Service
public class UploadServiceImpl implements UploadService {

	private static Log logger = LogFactory.getLog(UploadServiceImpl.class);

	private FtpConfiguration ftpConfiguration;

	@Autowired
	public UploadServiceImpl(FtpConfiguration ftpConfiguration) {
		this.ftpConfiguration = ftpConfiguration;
	}

	@Override
	public void upload(File file, String filePath) {
		if(!file.exists()) {
			logger.warn("Attempting to upload non-existent file " + file.getAbsolutePath());
			return;
		}
		String remotePath = String.format("%s/%s", ftpConfiguration.getPath(), filePath);
		logger.info(String.format("Uploading %s to %s on %s", file.getAbsolutePath(), remotePath, ftpConfiguration.getServer()));
		try {
			FTPClient client = new FTPClient();
			client.connect(ftpConfiguration.getServer());
			client.login(ftpConfiguration.getUsername(), ftpConfiguration.getPassword());
			logger.info(String.format("Connected to %s", ftpConfiguration.getServer()));
			InputStream inputStream = new FileInputStream(file);
			client.storeFile(remotePath, inputStream);
			client.logout();
			logger.info(String.format("Copied file %s", file.getAbsolutePath()));
			inputStream.close();
		} catch (IOException e) {
			throw new DnDataException(e);
		}
	}
}
