package net.smart.rfid.tunnel.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;

import javax.crypto.Cipher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import net.smart.rfid.util.PropertiesUtil;

@Service
public class FileService {

	private static final Logger logger = LogManager.getLogger(FileService.class);

	@Scheduled(fixedRateString = "${cronExpression}")
	public void sendFileWithSftp() {
		logger.info("*********Start send file *************");
		try {
			String remoteDir = PropertiesUtil.getPathDestination();
			ChannelSftp channelSftp = setupJsch();
			channelSftp.connect();

			File[] listFile = getListFileOfDir(PropertiesUtil.getPathLocal());
			for (File file : listFile) {
				InputStream inputStream = new FileInputStream(file);
				channelSftp.put(inputStream, remoteDir + file.getName());
				inputStream.close();
				//
				// Sposto file pdf
				logger.info("Move sent files from local path to a trash path ");
				Path sourceDir = Paths.get(file.getPath());
				Path destDir = Paths.get(PropertiesUtil.getTrashPath()+file.getName());
				
				Files.move(sourceDir, destDir, StandardCopyOption.REPLACE_EXISTING);

				logger.info("File Moved");
			}
			channelSftp.exit();
		} catch (FileNotFoundException e) {
			logger.info("No file prenset");
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("*********FINE*************");
	}

	private ChannelSftp setupJsch() throws JSchException {
		JSch jsch = new JSch();
		jsch.setKnownHosts(PropertiesUtil.getSshknownHosts()+"/known_hosts");
		Session jschSession = jsch.getSession(PropertiesUtil.getUser(), PropertiesUtil.getHostIp(), new Integer(PropertiesUtil.getHostPort()));
		jschSession.setPassword(PropertiesUtil.getPassword());
		// jschSession.setConfig(null, null);
		jschSession.connect();
		return (ChannelSftp) jschSession.openChannel("sftp");
	}

	private File encryptFile(Path tempFile) {
		File file = null;
		try {
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
			generator.initialize(2048);
			KeyPair pair = generator.generateKeyPair();
			// PrivateKey privateKey = pair.getPrivate();
			PublicKey publicKey = pair.getPublic();
			// Publik key
			// File publicKeyFile = new File("public.key");
			// byte[] publicKeyBytes = Files.readAllBytes(publicKeyFile.toPath());
			// KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			// EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
			// PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
			//
			byte[] fileBytes = Files.readAllBytes(tempFile);
			Cipher encryptCipher = Cipher.getInstance("RSA");
			encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] encryptedFileBytes = encryptCipher.doFinal(fileBytes);
			file = tempFile.toFile();
			FileOutputStream stream = new FileOutputStream(file);
			stream.write(encryptedFileBytes);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file;
	}

	private File[] getListFileOfDir(String dir) throws IOException {
		File folder = new File(dir);
		File[] listOfFiles = folder.listFiles();

		return listOfFiles;
	}

}