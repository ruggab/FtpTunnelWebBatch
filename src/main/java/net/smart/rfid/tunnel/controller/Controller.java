package net.smart.rfid.tunnel.controller;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
		String message = "";
		try {
			if (StringUtils.isEmpty(shipCode)) {
				message = "ShipCode Mandatory";
				return message;
			}
			//
			dataService.save(shipCode, 0l);
			message = "OK";
			return message;
		} catch (Exception e) {
			throw e;
		}
	}

	@GetMapping("/stopOfShipment")
	public String stopOfShipment() throws Exception {
		try {
			//
			String message = "";
			String shipCodeDB = dataService.getLastShip();
			List<DataClient> listDataClient = dataService.findByShipCodeOrderByShipSeq(shipCodeDB);
			if (listDataClient.size() > 0) {

				// first create file object for file placed at location
				// specified by filepath
				long yourmilliseconds = System.currentTimeMillis();
				SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy_HH.mm.ss");
				Date resultdate = new Date(yourmilliseconds);
				String dateFormat = sdf.format(resultdate);
				File file = new File(PropertiesUtil.getPathLocal() + "/" + shipCodeDB + "_" + dateFormat + ".csv");
				message = file.getName();
				// create FileWriter object with file as parameter
				FileWriter outputfile = new FileWriter(file);

				// create CSVWriter with ';' as separator
				CSVWriter writer = new CSVWriter(outputfile, ';', CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
				// create a List which contains String array
				List<String[]> data = new ArrayList<String[]>();

				data.add(new String[] { "tunnel", "packageData", "tid", "epc", "sku", "scandate" });

				for (DataClient c : listDataClient) {
					data.add(new String[] { c.getIdTunnel() + "", c.getPackageData(), c.getTid() ,c.getEpc(), c.getSku(), c.getDataForm() });
				}
				writer.writeAll(data);

				// closing writer connection
				writer.close();
				message = message + " generated";

			}
			dataService.deleteAllShip();
			return "OK " + message;
		} catch (Exception e) {
			throw e;
		}
	}

	

	@PostMapping("/saveDataClient")
	public String saveDataClient(@RequestParam Long packId) throws Exception {
		try {
			dataService.findStreamAndSaveDataClientBy(packId);
			return "ok";
		} catch (Exception e) {
			throw e;
		}
	}

	@GetMapping("/getLastShip")
	public String getLastShip() throws Exception {
		try {
			String last = dataService.getLastShip();
			return last;
		} catch (Exception e) {
			throw e;
		}
	}

}
