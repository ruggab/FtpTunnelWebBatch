package net.smart.rfid.tunnel.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import net.smart.rfid.util.PropertiesUtil;

@Service
public class LoadFileService {

	private static final Logger log = LoggerFactory.getLogger(LoadFileService.class);

	private static Integer cont = 0;

	//@Scheduled(fixedRateString = "${cronExpression}")
	public void creaImmagineAssociaPdf() {
		log.info("*********Start BATCH *************");
		try {
			ChannelSftp channelSftp = setupJsch();
			channelSftp.connect();

			String localFile = PropertiesUtil.getPathLocal() + "/esameScritto.pdf";
			Path p1 = Paths.get(localFile);
			File file = encryptFile(p1);
			InputStream inputStream = new FileInputStream(file);
			String remoteDir = PropertiesUtil.getPathDestination();
			channelSftp.put(inputStream, remoteDir);

			channelSftp.exit();
		} catch (Exception e) {
			e.printStackTrace();
		}

		log.info("*********FINE*************");
	}

	private ChannelSftp setupJsch() throws JSchException {
		JSch jsch = new JSch();
		jsch.setKnownHosts("C:\\Users\\Gabriele\\.ssh\\known_hosts");
		Session jschSession = jsch.getSession(PropertiesUtil.getUser(), PropertiesUtil.getHostIp(), new Integer(PropertiesUtil.getHostPort()));
		jschSession.setPassword(PropertiesUtil.getPassword());
		//jschSession.setConfig(null, null);
		jschSession.connect();
		return (ChannelSftp) jschSession.openChannel("sftp");
	}

	
	private File encryptFile(Path tempFile) {
		File file = null;
		try {
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
			generator.initialize(2048);
			KeyPair pair = generator.generateKeyPair();
			//PrivateKey privateKey = pair.getPrivate();
			PublicKey publicKey = pair.getPublic();
			//Publik key
//			File publicKeyFile = new File("public.key");
//			byte[] publicKeyBytes = Files.readAllBytes(publicKeyFile.toPath());
//			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//			EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
//			PublicKey  publicKey = keyFactory.generatePublic(publicKeySpec);
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
	
}