package net.smart.rfid.tunnel.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.smart.rfid.tunnel.db.entity.DataClient;
import net.smart.rfid.tunnel.db.entity.DataClientFtpConf;

@Repository
public interface DataClientFtpConfRepository extends JpaRepository<DataClientFtpConf, Long> {

	//public List<DataClient> findByShipCodeOrderByShipSeq(String shipCode);
	
	
//	@Query(value = "select filecreated from export_data_client(:shipCodeId)" , nativeQuery = true)
//	public String createFileCsvToSend(@Param ("shipCodeId") Long shipCodeId);
//
//	
//	public void deleteByIdShipTable(Long idShipTable);
	
//	@Query(value = "select * from data_client_ftp_conf limit 1" , nativeQuery = true)
//	public DataClientFtpConf getConfFtp()
	

}
