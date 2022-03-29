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
	public static String currentShipCode = "";
	public static Long shipSeq = new Long(0);

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
			if (StringUtils.hasText(currentShipCode)) {
				dataClient.setShipCode(currentShipCode);
				dataClient.setShipSeq(shipSeq);
				dataClientRepository.save(dataClient);
				WebSocketToClient.sendMessageOnPackageReadEvent("Package Shipped: " + readerStreamOnly.getPackageData());
			} else {
				WebSocketToClient.sendMessageOnPackageReadEvent("Package NOT Shipped: " + readerStreamOnly.getPackageData());
			}

		}
		// La seq è incrementata ad ogni nuovo package
		shipSeq = shipSeq + 1;
		logger.info("End Insert");
		return listReaderStream;

	}

	@Transactional
	public Long getMaxShipSeqByShipCode(String shipCode) throws Exception {
		//
		Long maxShip = readerStreamRepository.getMaxShipSeqByShipCode(shipCode);

		return maxShip;
	}

	@Transactional
	public List<DataClient> findByShipCodeOrderByShipSeq(String shipCode) throws Exception {
		//
		List<DataClient> listClinetData = dataClientRepository.findByShipCodeOrderByShipSeq(shipCode);

		return listClinetData;
	}

	@Transactional
	public ShipTable save(String shipCode) throws Exception {
		//
		ShipTable shipTable = new ShipTable();
		shipTable.setShipCode(shipCode);
		shipTable = shipTableRepository.save(shipTable);

		return shipTable;
	}

	@Transactional
	public String getLastShip() throws Exception {
		//
		String last = shipTableRepository.getLastShip();

		return last;
	}

	@Transactional
	public void deleteAllShip() throws Exception {
		shipTableRepository.deleteAll();
	}

}