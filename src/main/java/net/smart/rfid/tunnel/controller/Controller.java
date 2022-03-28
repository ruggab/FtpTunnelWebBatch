package net.smart.rfid.tunnel.controller;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.opencsv.CSVWriter;

import net.smart.rfid.tunnel.db.entity.DataClient;
import net.smart.rfid.tunnel.model.PackageModel;
import net.smart.rfid.tunnel.services.DataService;
import net.smart.rfid.util.PropertiesUtil;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:4200")
public class Controller {

	@Autowired
	private DataService dataService;

	@PostMapping("/sendDataToClient")
	public String sendDataToClient(@RequestBody PackageModel packageModel) throws Exception {
		try {

			dataService.findStreamAndSaveDataClientBy(new Long(packageModel.getPackId()));

			return "ok";
		} catch (Exception e) {
			throw e;
		}
	}

	@GetMapping("/startOfShipment")
	public String startOfShipment(@RequestParam String shipCode) throws Exception {
		try {
			DataService.currentShipCode = shipCode;
			Long maxShip = dataService.getMaxShipSeqByShipCode(shipCode);
			DataService.shipSeq = new Long(1);
			if (maxShip != null) {
				DataService.shipSeq = maxShip;
			}
			return "ok";
		} catch (Exception e) {
			throw e;
		}
	}

	@GetMapping("/stopOfShipment")
	public String stopOfShipment(@RequestParam String shipCode) throws Exception {
		try {
			DataService.currentShipCode = "";
			DataService.shipSeq = new Long(0);
			//
			String message = "";
			List<DataClient> listDataClient = dataService.findByShipCodeOrderByShipSeq(shipCode);
			if (listDataClient.size() > 0) {

				// first create file object for file placed at location
				// specified by filepath
				long yourmilliseconds = System.currentTimeMillis();
				SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");    
				Date resultdate = new Date(yourmilliseconds);
				File file = new File(PropertiesUtil.getPathLocal() + "/" + shipCode + "_" + resultdate + ".csv");
				message = file.getName();
				// create FileWriter object with file as parameter
				FileWriter outputfile = new FileWriter(file);

				// create CSVWriter with ';' as separator
				CSVWriter writer = new CSVWriter(outputfile, ';', CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
				// create a List which contains String array
				List<String[]> data = new ArrayList<String[]>();

				data.add(new String[] { "idTunnel", "NameTunnel", "ShipCode", "ShipSeq", "PackageData", "Tid", "Epc", "Sku", "Timestamp" });

				for (DataClient c : listDataClient) {
					data.add(new String[] { c.getIdTunnel() + "", c.getNameTunnel(), c.getShipCode(), c.getShipSeq() + "", c.getPackageData(), c.getTid(), c.getEpc(), c.getSku(), c.getDataForm() });
				}
				writer.writeAll(data);

				// closing writer connection
				writer.close();
				message = message + " generated";

			}
			return "OK " + message;
		} catch (Exception e) {
			throw e;
		}
	}

	@PostMapping("/getMaxShipSeq")
	public Long getMaxShipSeqByShipCode(@RequestParam String shipCode) throws Exception {
		try {
			Long maxShip = dataService.getMaxShipSeqByShipCode(shipCode);
			return maxShip;
		} catch (Exception e) {
			throw e;
		}
	}

}
