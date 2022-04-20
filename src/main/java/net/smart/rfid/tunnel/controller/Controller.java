package net.smart.rfid.tunnel.controller;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.smart.rfid.tunnel.db.entity.DataClientFtpConf;
import net.smart.rfid.tunnel.db.entity.DataClientSendFile;
import net.smart.rfid.tunnel.db.entity.ShipTable;
import net.smart.rfid.tunnel.model.PackageModel;
import net.smart.rfid.tunnel.services.DataService;
import net.smart.rfid.util.PropertiesUtil;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:4200")
public class Controller {

	private static final Logger logger = LogManager.getLogger(Controller.class);

	@Autowired
	private DataService dataService;

	@PostMapping("/sendDataToClient")
	public ResponseEntity<String> sendDataToClient(@RequestBody PackageModel packageModel) throws Exception {
		try {
			dataService.findStreamAndSaveDataClientBy(new Long(packageModel.getPackId()), packageModel.getPackageData());
			return ResponseEntity.ok("OK");
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/startOfShipment")
	public ResponseEntity<String> startOfShipment(@RequestParam String shipCode) throws Exception {
		String message = "";
		try {
			if (StringUtils.isEmpty(shipCode)) {
				message = "ShipCode Mandatory";
				return ResponseEntity.ok(message);
			}
			//
			dataService.save(shipCode, 0l);
			dataService.startStopCommand(true);
			message = "OK";
			return ResponseEntity.ok(message);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/stopOfShipment")
	public ResponseEntity<String> stopOfShipment() throws Exception {
		try {
			//Gestione GPO on start/stop command
			dataService.startStopCommand(false);
			ShipTable shipTable = dataService.getLastShip();
			if (shipTable == null) {
				throw new Exception("ShipCode Mandatory");
			}
			dataService.deleteAllShipTable();
			String nomeFile = dataService.createFileCsvToSend(shipTable.getId());
			if (nomeFile.equalsIgnoreCase("KO")) {
				throw new Exception("No tags read");
			}
			String message = "OK " + nomeFile + "Created";
			return ResponseEntity.ok(message);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
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

	@GetMapping("/getListFile")
	public List<DataClientSendFile> getListFile() throws Exception {
		try {

			List<DataClientSendFile> last = dataService.getListFile();

			return last;
		} catch (Exception e) {
			throw e;
		}
	}

	@GetMapping("/file")
	public ResponseEntity<byte[]> getFile(@RequestParam String fileName) throws Exception {
		byte[] arr = null;
		try {
			File file = null;
			boolean errorFile = false;
			try {
				file = new File(PropertiesUtil.getPathLocal() + File.separator + fileName);
			} catch (Exception e) {
				logger.error(e.toString() + "-" + e.getMessage());
				errorFile = true;
			}
			if (!errorFile) {
				try {
					file = new File(PropertiesUtil.getTrashPath() + File.separator + fileName);
				} catch (Exception e) {
					logger.error(e.toString() + "-" + e.getMessage());
					errorFile = true;
				}
			}

			if (!errorFile) {
				FileInputStream fl = new FileInputStream(file);
				// Now creating byte array of same length as file
				arr = new byte[(int) file.length()];

				// Reading file content to byte array
				// using standard read() method
				fl.read(arr);

				// lastly closing an instance of file input stream
				// to avoid memory leakage
				fl.close();
			} else {
				throw new Exception("File non present");
			}

		} catch (Exception e) {
			throw e;
		}
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"").body(arr);
	}
}
