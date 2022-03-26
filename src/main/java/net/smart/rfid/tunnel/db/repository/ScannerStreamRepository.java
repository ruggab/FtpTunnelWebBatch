package net.smart.rfid.tunnel.db.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.smart.rfid.tunnel.db.entity.ScannerStream;

@Repository
public interface ScannerStreamRepository extends JpaRepository<ScannerStream, Long> {

	

	@Query(value = "select b.nome as nometunnel, a.* from scanner_stream a inner join tunnel b on b.id = a.id_tunnel order by a.time_stamp desc", nativeQuery = true)
	List<ScannerStream> findAllStream();

	@Query(value = "SELECT  ss.id, ss.dettaglio,ss.esito,ss.id_tunnel as idTunnel,ss.package_data as packageData," + "ss.quantita,to_char(ss.time_stamp, 'dd/MM/yyyy HH24:MI:SS') as dataForm,ss.time_invio as timeInvio ,ss.elaborated,ss.match_type as matchTipe," + "ss.type_expected as tipeExpected ,ss.status "
			+ "FROM scanner_stream ss left join Tunnel t on t.id = ss.id_tunnel  order by  time_stamp desc", nativeQuery = true)
	List<PackageStream> findAllByOrderByTimeStampDesc();

	@Query(value = "SELECT id, id_tunnel as idTunnel, esito, package_data as packageData, quantita, dataform as dataForm, type_expected as tipeExpected FROM public.package_list", nativeQuery = true)
	List<PackageStream> findAllScannerStreamView();
	

	// and DATE(time_stamp) = current_date
	@Query(value = "SELECT t.nome as nometunnel, ss.id, ss.dettaglio,ss.esito,ss.id_tunnel as idTunnel,ss.package_data as packageData," + "ss.quantita,to_char(ss.time_stamp, 'dd/MM/yyyy HH24:MI:SS') as dataForm,ss.time_invio as timeInvio ,ss.elaborated,ss.match_type as matchTipe," + "ss.type_expected as tipeExpected ,ss.status "
			+ "FROM scanner_stream ss left join Tunnel t on t.id = ss.id_tunnel  order by  time_stamp desc LIMIT 10", nativeQuery = true)
	List<PackageStream> findLast10StreamOrderByTimeStampDesc();

	@Query(value = "SELECT id, id_tunnel as idTunnel, esito, package_data as packageData, quantita, dataform as dataForm, type_expected as tipeExpected FROM public.package_list", countQuery = "SELECT count(*) FROM public.package_list ", nativeQuery = true)
	Page<PackageStream> findScannerStreamPag(Pageable pageable);

	public ScannerStream findByPackageData(String packData);

	public static interface PackageStream {

		String getId();

		String getIdTunnel();

		String getPackageData();

		String getEsito();

		String getDettaglio();

		String getDataForm();

		String getQuantita();

		String getTypeExpected();
		
		String getNometunnel();
		

	}

}
