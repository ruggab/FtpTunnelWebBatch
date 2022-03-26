package net.smart.rfid.tunnel.db.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * @author Gabriele
 *
 */
@Entity
@Table(name = "reader_stream")
public class ReaderStream {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	private Long idTunnel;

	private Long packId;
	
	private String packageData;

	private String epc;

	private String userData;

	private String tid;
	
	private String sku;

	private String ipAdress;
	
	private String antennaPortNumber;

	private String firstSeenTime;

	private String lastSeenTime;

	private String tagSeenCount;

	private String rfDopplerFrequency;

	private String peakRssiInDbm;

	private String channelInMhz;

	private String phaseAngleInRadians;

	private String modelName;

	@Temporal(TemporalType.TIMESTAMP)
	private Date timeStamp;
	
	@Transient
	private String dataForm;
	

	public ReaderStream() {
	}

	

	public String getAntennaPortNumber() {
		return antennaPortNumber;
	}

	public void setAntennaPortNumber(String antennaPortNumber) {
		this.antennaPortNumber = antennaPortNumber;
	}

	public String getFirstSeenTime() {
		return firstSeenTime;
	}

	public void setFirstSeenTime(String firstSeenTime) {
		this.firstSeenTime = firstSeenTime;
	}

	public String getLastSeenTime() {
		return lastSeenTime;
	}

	public void setLastSeenTime(String lastSeenTime) {
		this.lastSeenTime = lastSeenTime;
	}

	public String getTagSeenCount() {
		return tagSeenCount;
	}

	public void setTagSeenCount(String tagSeenCount) {
		this.tagSeenCount = tagSeenCount;
	}

	public String getRfDopplerFrequency() {
		return rfDopplerFrequency;
	}

	public void setRfDopplerFrequency(String rfDopplerFrequency) {
		this.rfDopplerFrequency = rfDopplerFrequency;
	}

	public String getPeakRssiInDbm() {
		return peakRssiInDbm;
	}

	public void setPeakRssiInDbm(String peakRssiInDbm) {
		this.peakRssiInDbm = peakRssiInDbm;
	}

	public String getChannelInMhz() {
		return channelInMhz;
	}

	public void setChannelInMhz(String channelInMhz) {
		this.channelInMhz = channelInMhz;
	}

	public String getPhaseAngleInRadians() {
		return phaseAngleInRadians;
	}

	public void setPhaseAngleInRadians(String phaseAngleInRadians) {
		this.phaseAngleInRadians = phaseAngleInRadians;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getIpAdress() {
		return ipAdress;
	}

	public void setIpAdress(String ipAdress) {
		this.ipAdress = ipAdress;
	}


	public String getUserData() {
		return userData;
	}

	public void setUserData(String userData) {
		this.userData = userData;
	}

	public Long getIdTunnel() {
		return idTunnel;
	}

	public void setIdTunnel(Long idTunnel) {
		this.idTunnel = idTunnel;
	}

	public String getDataForm() {
		Date date = new Date();
		date.setTime(this.timeStamp.getTime());
		String formattedDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(date);
		return formattedDate;
	}

	public void setDataForm(String dataForm) {
		this.dataForm = dataForm;
	}



	public Long getPackId() {
		return packId;
	}



	public void setPackId(Long packId) {
		this.packId = packId;
	}



	public String getPackageData() {
		return packageData;
	}



	public void setPackageData(String packageData) {
		this.packageData = packageData;
	}



	public String getSku() {
		return sku;
	}



	public void setSku(String sku) {
		this.sku = sku;
	}

	
	
	

}
