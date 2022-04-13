package net.smart.rfid.tunnel.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import net.schmizz.sshj.userauth.UserAuthException;
import net.schmizz.sshj.userauth.keyprovider.KeyProvider;
import net.schmizz.sshj.xfer.FileSystemFile;
import net.smart.rfid.tunnel.db.entity.DataClientFtpConf;
import net.smart.rfid.tunnel.db.entity.DataClientSendFile;
import net.smart.rfid.tunnel.db.repository.DataClientFtpConfRepository;
import net.smart.rfid.tunnel.db.repository.DataClientSendFileRepository;
import net.smart.rfid.util.PropertiesUtil;
import net.smart.rfid.util.WebSocketToClient;

@Service
public class FileService {

	private static final Logger logger = LogManager.getLogger(FileService.class);

	@Autowired
	private DataClientSendFileRepository dataClientSendFileRepository;

	@Autowired
	private DataClientFtpConfRepository dataClientFtpConfRepository;

	@Scheduled(fixedRateString = "${cronExpression}")
	@Transactional
	public void sendFileWithSftp() {
		logger.info("*********Start send file *************");
		try {

			String remoteDir = PropertiesUtil.getPathDestination();
			SSHClient ssh = setupSshj();
			//
			List<DataClientSendFile> listDataSend = dataClientSendFileRepository.findByStatus(false);
			//
			for (DataClientSendFile dataClientSendFile : listDataSend) {
				try {
					File file = getFileByName(PropertiesUtil.getPathLocal(), dataClientSendFile.getNameFile());
					//
					SFTPClient sftp = ssh.newSFTPClient();
					sftp.put(new FileSystemFile(file), remoteDir + file.getName());
					sftp.close();
					//
					// Sposto file pdf
					logger.info("Move sent files from local path to a trash path ");
					Path sourceDir = Paths.get(file.getPath());
					Path destDir = Paths.get(PropertiesUtil.getTrashPath() + file.getName());
					//
					Files.move(sourceDir, destDir, StandardCopyOption.REPLACE_EXISTING);
					//
					dataClientSendFile.setStatus(true);
					dataClientSendFileRepository.save(dataClientSendFile);
					//
					logger.info("File Moved");
				} catch (FileNotFoundException e) {
					logger.info("No file prenset");
				}
			}
			ssh.disconnect();
			//
			logger.info("SendMessageOnFileEvent: Files sent");
			WebSocketToClient.sendMessageOnFileEvent("Files sent");
			//
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}

		logger.info("*********FINE*************");
	}

	// private ChannelSftp setupJsch() throws Exception {
	// JSch jsch = new JSch();
	// Session jschSession = null;
	// DataClientFtpConf confFtp = null;
	// // Se esiste conf DB
	// List<DataClientFtpConf> listConf = dataClientFtpConfRepository.findAll();
	// if (listConf.size() > 0) {
	// confFtp = listConf.get(0);
	// jschSession = jsch.getSession(confFtp.getFtpUser(), confFtp.getFtpHost(), confFtp.getFtpPort().intValue());
	// jschSession.setPassword(confFtp.getFtpPsw());
	// } else {
	// // else config from properties file
	// jschSession = jsch.getSession(PropertiesUtil.getUser(), PropertiesUtil.getHostIp(), new
	// Integer(PropertiesUtil.getHostPort()));
	// jschSession.setPassword(PropertiesUtil.getPassword());
	// }
	// jsch.setKnownHosts(PropertiesUtil.getSshknownHosts() + "/known_hosts");
	// //
	// jschSession.connect();
	// return (ChannelSftp) jschSession.openChannel("sftp");
	// }

	private SSHClient setupSshj() throws Exception {
		SSHClient client = new SSHClient();
		try {

			DataClientFtpConf confFtp = null;
			// Se esiste conf DB
			List<DataClientFtpConf> listConf = dataClientFtpConfRepository.findAll();
			if (listConf.size() > 0) {
				confFtp = listConf.get(0);
				client.addHostKeyVerifier(new PromiscuousVerifier());
				client.connect(confFtp.getFtpHost(), confFtp.getFtpPort().intValue());
				logger.info("Host" + confFtp.getFtpHost());
				//client.authPassword(confFtp.getFtpUser(), confFtp.getFtpPsw());
			} else {
				// else config from properties file
				client.addHostKeyVerifier(new PromiscuousVerifier());
				client.connect(PropertiesUtil.getHostIp(), new Integer(PropertiesUtil.getHostPort()));
				//client.authPassword(PropertiesUtil.getUser(), PropertiesUtil.getPassword());
			}
			try {
				
				File privateKey = new File(PropertiesUtil.getSshknownHosts() + "/id_rsa");
				KeyProvider keys = client.loadKeys(privateKey.getPath());
				client.authPublickey(PropertiesUtil.getCertUser(), keys);
				logger.info("User" + PropertiesUtil.getCertUser());
				logger.info("Key" + keys);
				
				//client.authPublickey(PropertiesUtil.getUser(), PropertiesUtil.getSshknownHosts()+"/");
				
			  } catch (UserAuthException e) {
				  //e.printStackTrace();
				  logger.error(e.getMessage());
			  }
		} catch (Exception e) {
			logger.error(e.toString() + "-" + e.getMessage());
			throw e;
		}
		return client;
	}

	private File[] getListFileOfDir(String dir) throws IOException {
		// AgeFileFilter filter = new AgeFileFilter(threshold);

		File folder = new File(dir);
		File[] listOfFiles = folder.listFiles();

		return listOfFiles;
	}

	private File getFileByName(String dir, String filename) throws IOException {
		File f = new File(dir + "/" + filename);
		return f;
	}

}