package net.smart.rfid.tunnel.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.smart.rfid.tunnel.db.entity.DataClientFtpConf;
import net.smart.rfid.tunnel.db.entity.ShipTable;
import net.smart.rfid.tunnel.model.PackageModel;
import net.smart.rfid.tunnel.services.DataService;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:4200")
public class Controller {

	@Autowired
	private DataService dataService;

	@PostMapping("/sendDataToClient")
	public String sendDataToClient(@RequestBody PackageModel packageModel) throws Exception {
		try {
			
			dataService.findStreamAndSaveDataClientBy(new Long(packageModel.getPackId()), packageModel.getPackageData());

			return "OK";
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
			dataService.startStopCommand(true);
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
			dataService.startStopCommand(false);
			ShipTable shipTable = dataService.getLastShip();
			if (shipTable == null) {
				throw new Exception("ShipCode Mandatory");
			}
			dataService.deleteAllShipTable();
			String nomeFile = dataService.createFileCsvToSend(shipTable.getId());
			String message = "OK " + nomeFile +  "Created";
			return message;
		} catch (Exception e) {
			throw e;
		}
	}


	@GetMapping("/getLastShip")
	public String getLastShip() throws Exception {
		try {
			String ret = "";
			ShipTable last = dataService.getLastShip();
			if (last != null) {
				ret = last.getShipCode();
			}
			
			return ret;
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	@PostMapping("/saveFtpConf")
	public String saveFtpConf(@RequestBody DataClientFtpConf ftpConf) throws Exception {
		try {
			
			dataService.saveFtpConf(ftpConf);

			return "OK";
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	
	@GetMapping("/getConfFtp")
	public DataClientFtpConf getConfFtp() throws Exception {
		try {
			DataClientFtpConf last = dataService.getConfFtp();
			return last;
		} catch (Exception e) {
			throw e;
		}
	}
}
