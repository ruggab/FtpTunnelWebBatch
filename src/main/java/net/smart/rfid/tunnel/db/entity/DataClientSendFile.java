package net.smart.rfid.tunnel.db.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author Gabriele
 *
 */
@Entity
@Table(name = "data_client_send_file")
public class DataClientSendFile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	private String shipCode;

	private String nameFile;

	private boolean status;

	private Date dataCreate;
	
	@Transient
	private String dataCreateFormat;

	public DataClientSendFile() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDataCreateFormat() {
		Date date = new Date();
		String formattedDate = "";
		if (this.dataCreate != null) {
			date.setTime(this.dataCreate.getTime());
			formattedDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(date);
		} else {
			formattedDate = "";
		}
		return formattedDate;
	}

	public String getNameFile() {
		return nameFile;
	}

	public void setNameFile(String nameFile) {
		this.nameFile = nameFile;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Date getDataCreate() {
		return dataCreate;
	}

	public void setDataCreate(Date dataCreate) {
		this.dataCreate = dataCreate;
	}

	public String getShipCode() {
		return shipCode;
	}

	public void setShipCode(String shipCode) {
		this.shipCode = shipCode;
	}

}
