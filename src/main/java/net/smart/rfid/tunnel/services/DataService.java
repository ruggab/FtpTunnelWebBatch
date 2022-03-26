package net.smart.rfid.tunnel.services;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.smart.rfid.tunnel.db.entity.DataClient;
import net.smart.rfid.tunnel.db.repository.DataClientRepository;
import net.smart.rfid.tunnel.db.repository.ReaderStreamRepository;
import net.smart.rfid.tunnel.db.repository.ReaderStreamRepository.ReaderStreamOnly;

@Service
public class DataService {

	private static final Logger logger = LoggerFactory.getLogger(DataService.class);

	@Autowired
	private ReaderStreamRepository readerStreamRepository;
	
	@Autowired
	private DataClientRepository dataClientRepository;
	
	@Transactional
	public List<ReaderStreamOnly> findStreamAndSaveDataClientBy(Long packId) throws Exception {
		//
		List<ReaderStreamOnly> listReaderStream = readerStreamRepository.getReaderStreamListByPackId(packId);
		
		for (ReaderStreamOnly readerStreamOnly : listReaderStream) {
			DataClient dataClient = new DataClient();
			dataClient.setEpc(readerStreamOnly.getEpc());
			dataClient.setIdTunnel(readerStreamOnly.getIdTunnel());
			dataClient.setPackageData(readerStreamOnly.getPackageData());
			dataClient.setSku(readerStreamOnly.getSku());
			dataClient.setTid(readerStreamOnly.getTid());
			dataClient.setPackId(readerStreamOnly.getPackId());
			dataClient.setTimeStamp(readerStreamOnly.getTimeStamp());
			dataClientRepository.save(dataClient);
		}
		
		return listReaderStream;
	}

}