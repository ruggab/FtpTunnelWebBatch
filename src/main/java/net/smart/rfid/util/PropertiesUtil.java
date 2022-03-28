package net.smart.rfid.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "batch.ftp")
@Configuration
public class PropertiesUtil {

	private static String cronExpression;
	private static String user;
	private static String password;
	private static String pathDestination;
	private static String pathLocal;
	private static String trashPath;
	private static String hostIp;
	private static String hostPort;
	private static String sshknownHosts;
	

	public static String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public static String getUser() {
		return user;
	}

	public  void setUser(String user) {
		this.user = user;
	}

	public static String getPathDestination() {
		return pathDestination;
	}

	public  void setPathDestination(String pathDestination) {
		this.pathDestination = pathDestination;
	}

	public static String getHostIp() {
		return hostIp;
	}

	public  void setHostIp(String hostIp) {
		this.hostIp = hostIp;
	}

	public static String getHostPort() {
		return hostPort;
	}

	public  void setHostPort(String hostPort) {
		this.hostPort = hostPort;
	}

	public static String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public static String getPathLocal() {
		return pathLocal;
	}

	public void setPathLocal(String pathLocal) {
		this.pathLocal = pathLocal;
	}

	public static String getTrashPath() {
		return trashPath;
	}

	public  void setTrashPath(String trashPath) {
		this.trashPath = trashPath;
	}

	public static String getSshknownHosts() {
		return sshknownHosts;
	}

	public  void setSshknownHosts(String sshknownHosts) {
		this.sshknownHosts = sshknownHosts;
	}
	
	


}
