package kr.co.ldcc.stat.dao;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.ldcc.stat.dao.mapper.MainMapper;

@Repository
public class MainStore {
	@Autowired
	MainMapper mainMapper;
	
	public List<String> getMacAddrOfMonth(String yearMonthDay) {
		return mainMapper.getMacAddrOfMonth(yearMonthDay);
	}
	public int getResidenceTimeByMacAddr(String macAddr) {
		return mainMapper.getResidenceTimeByMacAddr(macAddr);
	}
	public List<String> getMacAddrOfDay(String yearMonthDay) {
		return mainMapper.getMacAddrOfDay(yearMonthDay);
	}
	public List<String> getMacAddrOfDayByDeviceIdx(HashMap map) {
		// TODO Auto-generated method stub
		return mainMapper.getMacAddrOfDayByDeviceIdx(map);
	}
	public int getResidenceDayTimeByMacAddr(HashMap param) {
		// TODO Auto-generated method stub
		return mainMapper.getResidenceDayTimeByMacAddr(param);
	}
	public List<String> getIsConnectMacAddrOfDay(String yearMonthDay) {
		// TODO Auto-generated method stub
		return mainMapper.getIsConnectMacAddrOfDay(yearMonthDay);
	}
	public List<String> getIsConnectMacAddrOfDayInFloor(HashMap param) {
		// TODO Auto-generated method stub
		return mainMapper.getIsConnectMacAddrOfDayInFloor(param);
	}
	public List<String> getMacAddrOfDayInFloor(HashMap param) {
		// TODO Auto-generated method stub
		return mainMapper.getIsConnectMacAddrOfDayInFloor(param);
	}
	
}
