package net.smart.rfid.tunnel.db.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Gabriele
 *
 */
@Entity
@Table(name = "data_client_ftp_conf")
public class DataClientFtpConf {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	private String ftpHost;

	private Long ftpPort;

	private String ftpUser;

	private String ftpPsw;
	
	private String timeSendFile;
	
	private String timeDeleteFile;
	
	private String ipWirama;
	private String portCommand;
	private String startCommand;
	private String stopCommand;

	public DataClientFtpConf() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFtpHost() {
		return ftpHost;
	}

	public void setFtpHost(String ftpHost) {
		this.ftpHost = ftpHost;
	}

	public Long getFtpPort() {
		return ftpPort;
	}

	public void setFtpPort(Long ftpPort) {
		this.ftpPort = ftpPort;
	}

	public String getFtpUser() {
		return ftpUser;
	}

	public void setFtpUser(String ftpUser) {
		this.ftpUser = ftpUser;
	}

	public String getFtpPsw() {
		return ftpPsw;
	}

	public void setFtpPsw(String ftpPsw) {
		this.ftpPsw = ftpPsw;
	}

	public String getTimeSendFile() {
		return timeSendFile;
	}

	public void setTimeSendFile(String timeSendFile) {
		this.timeSendFile = timeSendFile;
	}

	public String getTimeDeleteFile() {
		return timeDeleteFile;
	}

	public void setTimeDeleteFile(String timeDeleteFile) {
		this.timeDeleteFile = timeDeleteFile;
	}

	public String getIpWirama() {
		return ipWirama;
	}

	public void setIpWirama(String ipWirama) {
		this.ipWirama = ipWirama;
	}

	public String getPortCommand() {
		return portCommand;
	}

	public void setPortCommand(String portCommand) {
		this.portCommand = portCommand;
	}

	public String getStartCommand() {
		return startCommand;
	}

	public void setStartCommand(String startCommand) {
		this.startCommand = startCommand;
	}

	public String getStopCommand() {
		return stopCommand;
	}

	public void setStopCommand(String stopCommand) {
		this.stopCommand = stopCommand;
	}
	
	

}
