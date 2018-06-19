package kr.co.ldcc.stat.dao.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MainMapper {
	//
	//월별, 일별 평균 체류 시간
	List<String> getMacAddrOfMonth(String yearMonthDay);
	int getResidenceTimeByMacAddr(String macAddr);
	List<String> getMacAddrOfDay(String yearMonthDay);
	
	int getResidenceDayTimeByMacAddr(HashMap param);
	
	List<String> getMacAddrOfDayByDeviceIdx(HashMap map);
	List<String> getIsConnectMacAddrOfDay(String yearMonthDay);
	
	List<String> getIsConnectMacAddrOfDayInFloor(HashMap param);
	List<String> getMacAddrOfDayInFloor(HashMap param);

	
	
	
	//체류 시간대별 방문자수
	
	
}
