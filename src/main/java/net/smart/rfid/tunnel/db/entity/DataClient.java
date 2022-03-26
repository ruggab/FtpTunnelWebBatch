package net.smart.rfid.tunnel.db.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

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
@Table(name = "data_client")
public class DataClient {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	private Long idTunnel;

	private Long packId;

	private String packageData;

	private Date timeStamp;

	private String epc;

	private String tid;

	private String sku;

	private String shipCode;

	private Long shipSeq;

	public DataClient() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getPackageData() {
		return packageData;
	}

	public void setPackageData(String packageData) {
		this.packageData = packageData;
	}

	public String getDataForm() {
		Date date = new Date();
		String formattedDate = "";
		if (this.timeStamp != null) {
			date.setTime(this.timeStamp.getTime());
			formattedDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(date);
		} else {
			formattedDate = "";
		}
		return formattedDate;
	}

	public String getEpc() {
		return epc;
	}

	public void setEpc(String epc) {
		this.epc = epc;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Long getIdTunnel() {
		return idTunnel;
	}

	public void setIdTunnel(Long idTunnel) {
		this.idTunnel = idTunnel;
	}

	public Long getPackId() {
		return packId;
	}

	public void setPackId(Long packId) {
		this.packId = packId;
	}

	public String getShipCode() {
		return shipCode;
	}

	public void setShipCode(String shipCode) {
		this.shipCode = shipCode;
	}

	public Long getShipSeq() {
		return shipSeq;
	}

	public void setShipSeq(Long shipSeq) {
		this.shipSeq = shipSeq;
	}

}
