package net.smart.rfid.tunnel.services;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import net.smart.rfid.tunnel.db.entity.DataClient;
import net.smart.rfid.tunnel.db.entity.ShipTable;
import net.smart.rfid.tunnel.db.repository.DataClientRepository;
import net.smart.rfid.tunnel.db.repository.ReaderStreamRepository;
import net.smart.rfid.tunnel.db.repository.ReaderStreamRepository.ReaderStreamOnly;
import net.smart.rfid.tunnel.db.repository.ShipTableRepository;
import net.smart.rfid.util.WebSocketToClient;

@Service
public class DataService {

	private static final Logger logger = LogManager.getLogger(DataService.class);

	@Autowired
	private ReaderStreamRepository readerStreamRepository;

	@Autowired
	private ShipTableRepository shipTableRepository;

	@Autowired
	private DataClientRepository dataClientRepository;

	@Transactional
	public List<ReaderStreamOnly> findStreamAndSaveDataClientBy(Long packId) throws Exception {
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
				dataClientRepository.save(dataClient);
			}
			WebSocketToClient.sendMessageOnPackageReadEvent("Package Count: " + currentSeq);
		} else {
			WebSocketToClient.sendMessageOnPackageReadEvent("No Schipment Code PackageId: " + packId);
		}
		//

		//
		logger.info("End Insert");
		return listReaderStream;

	}

	@Transactional
	public List<DataClient> findByShipCodeOrderByShipSeq(String shipCode) throws Exception {
		//
		List<DataClient> listClientData = dataClientRepository.findByShipCodeOrderByShipSeq(shipCode);

		return listClientData;
	}

	@Transactional
	public ShipTable save(String shipCode, Long seq) throws Exception {
		//
		ShipTable shipTable = new ShipTable();
		shipTable.setShipCode(shipCode);
		shipTable.setSeq(seq);
		shipTable = shipTableRepository.save(shipTable);
		//
		return shipTable;
	}

	@Transactional
	public String getLastShip() throws Exception {
		//
		String ret = "";
		ShipTable last = shipTableRepository.getLastShip();
		if (last != null) {
			ret = last.getShipCode();
		}

		return ret;
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
	public void deleteAllShip() throws Exception {
		shipTableRepository.deleteAll();
		dataClientRepository.deleteAll();
	}

}