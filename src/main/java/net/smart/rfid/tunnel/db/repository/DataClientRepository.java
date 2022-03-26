package net.smart.rfid.tunnel.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.smart.rfid.tunnel.db.entity.DataClient;

@Repository
public interface DataClientRepository extends JpaRepository<DataClient, Long> {

	

}
