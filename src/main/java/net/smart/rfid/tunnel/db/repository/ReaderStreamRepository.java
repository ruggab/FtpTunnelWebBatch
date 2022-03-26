package net.smart.rfid.tunnel.db.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.smart.rfid.tunnel.db.entity.ReaderStream;

@Repository
public interface ReaderStreamRepository extends JpaRepository<ReaderStream, Long> {

	
	@Query(value = " select c.id_tunnel as idTunnel, c.pack_id as packId, c.package_data as packageData,"
			+ "    c.epc, c.tid, c.sku, c.time_stamp as timeStamp "
			+ " from (SELECT DISTINCT  a.id_tunnel,a.pack_id,a.package_data,a.epc, a.tid,a.sku,a.time_stamp FROM reader_stream a"
			+ " UNION ALL SELECT DISTINCT  b.id_tunnel, b.pack_id, b.package_data, b.epc, b.tid,  b.sku,  b.time_stamp FROM reader_stream_history b) c"
			+ " where c.pack_id = ?1 order by pack_id ", nativeQuery = true)
	List<ReaderStreamOnly> getReaderStreamListByPackId(Long packId);
	
	
	public static interface ReaderStreamOnly {
		Long getIdTunnel();

		Long getPackId();

		String getPackageData();

		String getEpc();

		String getTid();

		String getSku();

		Date getTimeStamp();

	}

	

}
