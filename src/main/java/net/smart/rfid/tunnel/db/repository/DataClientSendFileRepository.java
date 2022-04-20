package net.smart.rfid.tunnel.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.smart.rfid.tunnel.db.entity.DataClientSendFile;

@Repository
public interface DataClientSendFileRepository extends JpaRepository<DataClientSendFile, Long> {

	public List<DataClientSendFile> findByStatus(boolean status);
	
	
	
	public List<DataClientSendFile> findAllByOrderByIdDesc();

}
