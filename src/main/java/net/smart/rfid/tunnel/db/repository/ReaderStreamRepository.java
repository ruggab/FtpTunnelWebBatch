package net.smart.rfid.tunnel.db.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.smart.rfid.tunnel.db.entity.ReaderStream;

@Repository
public interface ReaderStreamRepository extends JpaRepository<ReaderStream, Long> {

	
	@Query(value = " select idtunnel as idTunnel, nametunnel as nameTunnel, packid as packId, packagedata packageData, epc, tid, sku, time_stamp as timeStamp from data_client_list where packid = ?1", nativeQuery = true)
	List<ReaderStreamOnly> getReaderStreamListByPackId(Long packId);
	
	
	
	@Query(value = " SELECT ship_seq FROM public.data_client where ship_code = ?1 order by ship_seq desc limit 1 ", nativeQuery = true)
	Long getMaxShipSeqByShipCode(String shipCode);
	
	public static interface ReaderStreamOnly {
		Long getIdTunnel();

		Long getPackId();
		
		String getNameTunnel();

		String getPackageData();

		String getEpc();

		String getTid();

		String getSku();

		Date getTimeStamp();

	}

	

}
