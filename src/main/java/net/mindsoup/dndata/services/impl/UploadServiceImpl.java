package net.mindsoup.dndata.services.impl;

import net.mindsoup.dndata.configuration.FtpConfiguration;
import net.mindsoup.dndata.exceptions.DnDataException;
import net.mindsoup.dndata.services.UploadService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTP;
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

			boolean successfulLogin = client.login(ftpConfiguration.getUsername(), ftpConfiguration.getPassword());
			String loginMessage = client.getReplyString();
			if(!successfulLogin) {
				client.disconnect();
				logger.warn("Unable to log in to FTP server: " + loginMessage);
				throw new DnDataException("Unable to log in to FTP server: " + loginMessage);
			}

			logger.info(String.format("Connected to %s", ftpConfiguration.getServer()));
			client.enterLocalPassiveMode();

			InputStream inputStream = new FileInputStream(file);
			client.setFileType(FTP.BINARY_FILE_TYPE);
			boolean successCopy = client.storeFile(remotePath, inputStream);
			String messageCopy = client.getReplyString();
			client.logout();
			client.disconnect();
			if(!successCopy) {
				logger.warn(String.format("Unable to copy file to FTP server: %s", messageCopy));
				throw new DnDataException(String.format("Unable to copy file to FTP server: %s", messageCopy));
			}
			logger.info("Copied file successfully");
			inputStream.close();
		} catch (IOException e) {
			throw new DnDataException(e);
		}
	}
}
