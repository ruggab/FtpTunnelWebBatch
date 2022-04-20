package net.smart.rfid.tunnel.services;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import net.smart.rfid.tunnel.db.entity.DataClient;
import net.smart.rfid.tunnel.db.entity.DataClientFtpConf;
import net.smart.rfid.tunnel.db.entity.DataClientSendFile;
import net.smart.rfid.tunnel.db.entity.ShipTable;
import net.smart.rfid.tunnel.db.repository.DataClientFtpConfRepository;
import net.smart.rfid.tunnel.db.repository.DataClientRepository;
import net.smart.rfid.tunnel.db.repository.DataClientSendFileRepository;
import net.smart.rfid.tunnel.db.repository.ReaderStreamRepository;
import net.smart.rfid.tunnel.db.repository.ReaderStreamRepository.ReaderStreamOnly;
import net.smart.rfid.tunnel.db.repository.ShipTableRepository;
import net.smart.rfid.util.SocketWiramaClient;
import net.smart.rfid.util.WebSocketToClient;

@Service
public class DataService {

	private static final Logger logger = LogManager.getLogger(DataService.class);

	@Autowired
	private DataClientFtpConfRepository dataClientFtpConfRepository;

	@Autowired
	private ReaderStreamRepository readerStreamRepository;

	@Autowired
	private ShipTableRepository shipTableRepository;

	@Autowired
	private DataClientRepository dataClientRepository;
	
	@Autowired
	private DataClientSendFileRepository dataClientSendFileRepository;

	@Transactional
	public List<ReaderStreamOnly> findStreamAndSaveDataClientBy(Long packId, String packageData) throws Exception {
		logger.info("Start Insert");
		//
		List<ReaderStreamOnly> listReaderStream = readerStreamRepository.getReaderStreamListByPackId(packId);
		ShipTable schipTable = shipTableRepository.getLastShip();

		// schipTable
		// shipTableRepository.
		if (schipTable != null && StringUtils.hasText(schipTable.getShipCode())) {
			Long currentSeq = schipTable.getSeq();
			currentSeq = currentSeq + 1;
			schipTable.setSeq(currentSeq);
			shipTableRepository.save(schipTable);
			for (ReaderStreamOnly readerStreamOnly : listReaderStream) {
				DataClient dataClient = new DataClient();
				dataClient.setEpc(readerStreamOnly.getEpc());
				dataClient.setIdTunnel(readerStreamOnly.getIdTunnel());
				dataClient.setPackageData(readerStreamOnly.getPackageData());
				dataClient.setNameTunnel(readerStreamOnly.getNameTunnel());
				dataClient.setSku(readerStreamOnly.getSku());
				dataClient.setTid(readerStreamOnly.getTid());
				dataClient.setPackId(readerStreamOnly.getPackId());
				dataClient.setTimeStamp(readerStreamOnly.getTimeStamp());
				dataClient.setShipCode(schipTable.getShipCode());
				dataClient.setShipSeq(currentSeq);
				dataClient.setIdShipTable(schipTable.getId());
				dataClientRepository.save(dataClient);
			}
			WebSocketToClient.sendMessageOnPackageReadEvent("Package Count: " + currentSeq);
		} else {
			WebSocketToClient.sendMessageOnPackageReadEvent("No Schipment Code Package: " + packageData);
		}
		//

		//
		logger.info("End Insert");
		return listReaderStream;

	}

	// @Transactional
	// public List<DataClient> findByShipCodeOrderByShipSeq(String shipCode) throws Exception {
	// //
	// List<DataClient> listClientData = dataClientRepository.findByShipCodeOrderByShipSeq(shipCode);
	//
	// return listClientData;
	// }

	@Transactional
	public String createFileCsvToSend(Long shipTabId) throws Exception {
		//
		String nomeFile = dataClientRepository.createFileCsvToSend(shipTabId);

		return nomeFile;
	}

	@Transactional
	public ShipTable save(String shipCode, Long seq) throws Exception {
		//
		ShipTable shipTable = new ShipTable();
		shipTable.setShipCode(shipCode);
		shipTable.setSeq(seq);
		shipTable = shipTableRepository.save(shipTable);
		//
		//
		return shipTable;
	}

	@Transactional
	public void startStopCommand(boolean type) throws Exception {
		// GET DataClientFtpConf
		try {
			DataClientFtpConf dataClientFtpConf = null;
			List<DataClientFtpConf> listConf = dataClientFtpConfRepository.findAll();
			if (listConf.size() > 0) {
				dataClientFtpConf = listConf.get(0);
			}
			if (dataClientFtpConf != null && StringUtils.hasText(dataClientFtpConf.getIpWirama()) && StringUtils.hasText(dataClientFtpConf.getPortCommand())) {
				SocketWiramaClient cwc = new SocketWiramaClient();
				cwc.startConnection(dataClientFtpConf.getIpWirama(), new Long(dataClientFtpConf.getPortCommand()));
				if (type) {
					cwc.sendMessage(dataClientFtpConf.getStartCommand());
				} else {
					cwc.sendMessage(dataClientFtpConf.getStopCommand());
				}

				cwc.stopConnection();
			}
		} catch (Exception e) {
			logger.error("Error on command Start Stop: " + e.toString() + "-" + e.getMessage());
		}
	}

	@Transactional
	public ShipTable getLastShip() throws Exception {
		ShipTable last = shipTableRepository.getLastShip();
		return last;
	}

	@Transactional
	public Long getMaxSeq() throws Exception {
		//
		Long ret = 0l;
		ShipTable last = shipTableRepository.getLastShip();
		if (last != null) {
			ret = last.getSeq();
		}
		return ret;
	}

	@Transactional
	public void deleteAllShipTable() throws Exception {
		shipTableRepository.deleteAll();

	}

	@Transactional
	public void deleteAllDataClientByIdShipCode(Long idShip) throws Exception {
		dataClientRepository.deleteByIdShipTable(idShip);
	}

	@Transactional
	public DataClientFtpConf saveFtpConf(DataClientFtpConf ftpConf) throws Exception {
		//
		if (ftpConf.getId() == null) {
			DataClientFtpConf lastConf = dataClientFtpConfRepository.getConfFtp();
			if (lastConf !=null && lastConf.getId() != null) {
				ftpConf.setId(lastConf.getId());
			}
		}
		dataClientFtpConfRepository.save(ftpConf);
		//
		return ftpConf;
	}

	@Transactional
	public DataClientFtpConf getConfFtp() throws Exception {
		
		DataClientFtpConf conf = dataClientFtpConfRepository.getConfFtp();
		return conf;
	}
	
	@Transactional
	public List<DataClientSendFile> getListFile() throws Exception {
		
		List<DataClientSendFile> listFile = dataClientSendFileRepository.findAllByOrderByIdDesc();
		
		//
		return listFile;
	}

}