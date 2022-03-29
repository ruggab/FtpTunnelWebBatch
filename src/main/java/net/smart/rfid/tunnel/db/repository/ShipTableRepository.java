package net.smart.rfid.tunnel.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.smart.rfid.tunnel.db.entity.ShipTable;

@Repository
public interface ShipTableRepository extends JpaSpecificationExecutor<ShipTable>, JpaRepository<ShipTable, Long> {

	
	
	@Query(value = " SELECT ship_code FROM ship_table order by id desc limit 1 ", nativeQuery = true)
	String getLastShip();

	
}