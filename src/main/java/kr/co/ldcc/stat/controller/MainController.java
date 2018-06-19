package kr.co.ldcc.stat.controller;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.co.ldcc.stat.service.MainService;

@RestController
public class MainController {
	//
	@Autowired
	MainService mainService;
	
	@GetMapping("/avg/residencetime/month/{yearMonth}")
	public long retrieveAvgResidenceTimeOfMonth(@PathVariable String yearMonth, @RequestParam String isConnect) throws ParseException {
		if(isConnect.equals("Y")) {
			return mainService.getIsConnectAvgResidenceTimeOfMonth(yearMonth);
		}else if(isConnect.equals("N")) {
			return mainService.getAvgResidenceTimeOfMonth(yearMonth);	
		}else {
			return 0;
		}
	}
	@GetMapping("/avg/residencetime/day/{yearMonthDay}")
	public long retrieveAvgResidenceTimeOfDay(@PathVariable String yearMonthDay, @RequestParam String isConnect) {
		if(isConnect.equals("Y")) {
			return mainService.getIsConnectAvgResidenceTimeOfDay(yearMonthDay);
		}else if(isConnect.equals("N")) {
			return mainService.getAvgResidenceTimeOfDay(yearMonthDay);	
		}else {
			return 0;
		}
		
	}
	@GetMapping("/visitors/residencetime/hours/day/{yearMonthDay}")
	public HashMap retrieveNumberOfVisitorsByTimeOfDay(@PathVariable String yearMonthDay, @RequestParam String isConnect) {
		if(isConnect.equals("Y")) {
			return mainService.getIsConnectNumberOfVisitorsByTimeOfDay(yearMonthDay);
		}else if(isConnect.equals("N")) {
			return mainService.getNumberOfVisitorsByTimeOfDay(yearMonthDay);		
		}else {
			HashMap map = new HashMap();
			map.put("error", "Y 또는 N 입력");
			return map;
		}
	}
	@GetMapping("/visitors/residencetime/hours/month/{yearMonth}")
	public List<HashMap> retrieveNumberOfVisitorsByTimeOfMonth(@PathVariable String yearMonth, @RequestParam String isConnect) throws ParseException {
		if(isConnect.equals("Y")) {
			return mainService.getIsConnectNumberOfVisitorsByTimeOfMonth(yearMonth);
		}else if(isConnect.equals("N")) {
			return mainService.getNumberOfVisitorsByTimeOfMonth(yearMonth);		
		}else {
			return null;
		}
	}
	@GetMapping("/avg/floors/{locationIdx}/residencetime/day/{yearMonthDay}")
	public long retrieveAvgResidenceTimeOfDayByFloor(@RequestParam String isConnect, @PathVariable String yearMonthDay, @PathVariable String locationIdx) {
		if(isConnect.equals("Y")) {
			return mainService.getIsConnectAvgResidenceTimeOfDayInFloor(yearMonthDay, locationIdx);
		}else if(isConnect.equals("N")) {
			return mainService.getAvgResidenceTimeOfDayInFloor(yearMonthDay, locationIdx);		
		}else {
			return 0;
		}
	}
}
