package net.smart.rfid.tunnel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

			dataService.findStreamAndSaveDataClientBy(new Long(packageModel.getPackId()));

			return "ok";
		} catch (Exception e) {
			throw e;
		}
	}

}
