package kr.co.ldcc.stat.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.ldcc.stat.dao.MainStore;

@Service
public class MainService {
	//
	@Autowired
	MainStore mainStore;

	/**
	 * 일별 평균 체류 시간 조회 (is_connect=N)
	 * 
	 * @param yearMonthDay
	 *            => '2018-05-01'형식
	 * @return
	 */
	public long getAvgResidenceTimeOfDay(String yearMonthDay) {
		//
		long startTime = System.currentTimeMillis();
		System.out.println("====일별 평균 체류 시간 시작[is_connect=N]====");

		List<String> macAddrList = mainStore.getMacAddrOfDay(yearMonthDay); // 한번 이상 AP에 접속한 MAC_ADDR 조회 (일별)
		int macAddrCount = 0;
		int sumResidenceTime = 0;
		long avgResidenceTimeOfDay = 0;
		for (int i = 0; i < macAddrList.size(); i++) {
			HashMap param = new HashMap();
			param.put("macAddr", macAddrList.get(i).trim());
			param.put("yearMonthDay", yearMonthDay);
			int residenceTime = mainStore.getResidenceDayTimeByMacAddr(param); // MAC_ADDR를 기준으로 체류 시간 조회
			if (residenceTime > 5) { // 체류 시간이 5분 초과일 경우만 체류 시간으로 인정
				sumResidenceTime = sumResidenceTime + residenceTime; // 평균 체류 시간을 계산 하기 위한 체류 시간 합산.
				macAddrCount = macAddrCount + 1; // 평균 체류 시간을 계산하기 위한 카운트
			}
		}
		if(macAddrCount != 0) {
			avgResidenceTimeOfDay = (sumResidenceTime) / (macAddrCount);
		}
		System.out.println("[" + yearMonthDay + "] 평균 체류 시간은 : " + avgResidenceTimeOfDay + "분입니다.");

		long endTime = System.currentTimeMillis();
		long resultTime = (endTime - startTime) / 1000;
		System.out.println("====수행 시간 : " + resultTime + "초");

		return avgResidenceTimeOfDay;
	}

	/**
	 * 일별 평균 체류 시간 조회 (is_connect=Y)
	 * 
	 * @param yearMonthDay
	 *            => '2018-05-01'형식
	 * @return
	 */
	public long getIsConnectAvgResidenceTimeOfDay(String yearMonthDay) {
		//
		long startTime = System.currentTimeMillis();
		System.out.println("====일별 평균 체류 시간 시작[is_connect=Y]====");

		List<String> macAddrList = mainStore.getIsConnectMacAddrOfDay(yearMonthDay); // 한번 이상 AP에 접속한 is_connect=Y인
																						// MAC_ADDR 조회
		int macAddrCount = 0;
		int sumResidenceTime = 0;
		long avgResidenceTimeOfDay = 0;
		for (int i = 0; i < macAddrList.size(); i++) {
			HashMap param = new HashMap();
			param.put("macAddr", macAddrList.get(i).trim());
			param.put("yearMonthDay", yearMonthDay);
			int residenceTime = mainStore.getResidenceDayTimeByMacAddr(param); // MAC_ADDR를 기준으로 체류 시간 조회
			if (residenceTime > 5) { // 체류 시간이 5분 초과일 경우만 체류 시간으로 인정
				sumResidenceTime = sumResidenceTime + residenceTime; // 평균 체류 시간을 계산 하기 위한 체류 시간 합산.
				macAddrCount = macAddrCount + 1; // 평균 체류 시간을 계산하기 위한 카운트
			}
		}
		if(macAddrCount != 0) {
			avgResidenceTimeOfDay = (sumResidenceTime) / (macAddrCount);
		}
		System.out.println("[" + yearMonthDay + "] 평균 체류 시간은 : " + avgResidenceTimeOfDay + "분입니다.");

		long endTime = System.currentTimeMillis();
		long resultTime = (endTime - startTime) / 1000;
		System.out.println("====수행 시간 : " + resultTime + "초");

		return avgResidenceTimeOfDay;
	}


	/**
	 * 월별 평균 체류 시간 조회 (is_connect=N)
	 * 
	 * @param yearMonth
	 *            => '2018-05'형식
	 * @return
	 * @throws ParseException
	 */
	public long getAvgResidenceTimeOfMonth(String yearMonth) throws ParseException {
		//
		long startTime = System.currentTimeMillis();
		System.out.println("====월별 평균 체류 시간 시작====");
	
		SimpleDateFormat transDate = new SimpleDateFormat("yyyy-MM");
		Date tDate = transDate.parse(yearMonth);
		Calendar cal = Calendar.getInstance();
		cal.setTime(tDate);
		int endDayOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH); //해당 월의 일 수 가져옴.
		long sumAvgResidenceTimeOfDay = 0;//해당일 평균 체류 시간 합. 
		long avgResidenceTimeOfMonth = 0;//월 평균 체류 시간
		long avgResidenceTimeOfDay = 0;//일 평균 체류 시간 
		long dayCount = 0;
		for (int d = 1; d <= endDayOfMonth; d++) {
			String yearMonthDay = yearMonth;
			if (String.valueOf(d).length() == 1) {	//2018-MM-dd를 만들기.
				yearMonthDay = yearMonthDay + "-0" + d;

			} else {
				yearMonthDay = yearMonthDay + "-" + d;
			}
			avgResidenceTimeOfDay = this.getAvgResidenceTimeOfDay(yearMonthDay);
			if(avgResidenceTimeOfDay != 0) { //해당 일에 값이 존재하지 않는 경우 예외처리.
				sumAvgResidenceTimeOfDay = sumAvgResidenceTimeOfDay + avgResidenceTimeOfDay;
				dayCount = dayCount +1;  
			}
		}
		avgResidenceTimeOfMonth = (sumAvgResidenceTimeOfDay) / (dayCount);// 해당 일에 값이 존재 하지 않는 경우를 대상에 포함 하지 않을 경우.
		//avgResidenceTimeOfMonth = (sumAvgResidenceTimeOfDay) / (endDayOfMonth); 해당 일에 값이 존재 하지 않는 경우를 대상에 포함할 경우.
		System.out.println("[" + yearMonth + "] 평균 체류 시간은 : " + avgResidenceTimeOfMonth + "분입니다.");

		long endTime = System.currentTimeMillis();
		long resultTime = (endTime - startTime) / 1000;
		System.out.println("==== 월별 평균 체류 시간 수행 시간 : " + resultTime + "초");

		return avgResidenceTimeOfMonth;
	}
	/**
	 * 월별 평균 체류 시간 조회 (is_connect=Y)
	 * 
	 * @param yearMonth
	 *            => '2018-05'형식
	 * @return
	 * @throws ParseException
	 */
	public long getIsConnectAvgResidenceTimeOfMonth(String yearMonth) throws ParseException {
		//
		long startTime = System.currentTimeMillis();
		System.out.println("====월별 평균 체류 시간 시작====");
	
		SimpleDateFormat transDate = new SimpleDateFormat("yyyy-MM");
		Date tDate = transDate.parse(yearMonth);
		Calendar cal = Calendar.getInstance();
		cal.setTime(tDate);
		int endDayOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH); //해당 월의 일 수 가져옴.
		long sumAvgResidenceTimeOfDay = 0;//해당일 평균 체류 시간 합. 
		long avgResidenceTimeOfMonth = 0;//월 평균 체류 시간
		long avgResidenceTimeOfDay = 0;//일 평균 체류 시간 
		long dayCount = 0;
		for (int d = 1; d <= endDayOfMonth; d++) {
			String yearMonthDay = yearMonth;
			if (String.valueOf(d).length() == 1) {	//2018-MM-dd를 만들기.
				yearMonthDay = yearMonthDay + "-0" + d;

			} else {
				yearMonthDay = yearMonthDay + "-" + d;
			}
			avgResidenceTimeOfDay = this.getIsConnectAvgResidenceTimeOfDay(yearMonthDay);
			if(avgResidenceTimeOfDay != 0) { //해당 일에 값이 존재하지 않는 경우 예외처리.
				sumAvgResidenceTimeOfDay = sumAvgResidenceTimeOfDay + avgResidenceTimeOfDay;
				dayCount = dayCount +1;  
			}
		}
		avgResidenceTimeOfMonth = (sumAvgResidenceTimeOfDay) / (dayCount);// 해당 일에 값이 존재 하지 않는 경우를 대상에 포함 하지 않을 경우.
		//avgResidenceTimeOfMonth = (sumAvgResidenceTimeOfDay) / (endDayOfMonth); 해당 일에 값이 존재 하지 않는 경우를 대상에 포함할 경우.
		System.out.println("[" + yearMonth + "] 평균 체류 시간은 : " + avgResidenceTimeOfMonth + "분입니다.");

		long endTime = System.currentTimeMillis();
		long resultTime = (endTime - startTime) / 1000;
		System.out.println("==== 월별 평균 체류 시간 수행 시간 : " + resultTime + "초");

		return avgResidenceTimeOfMonth;
	}
	

	/**
	 * 체류시간대별 방문자수 (30분 이하, 1시간 이하, 2시간이하 , 3시간미만, 3시간 이상) - (일별, isConnect=Y)
	 * 
	 * @param yearMonthDay
	 *            => '2018-05-01' 포맷 준수
	 * @return
	 */
	public HashMap getIsConnectNumberOfVisitorsByTimeOfDay(String yearMonthDay) {
		long startTime = System.currentTimeMillis();
		System.out.println("====체류시간대별 방문자수 시작====");

		List<String> macAddrList = mainStore.getIsConnectMacAddrOfDay(yearMonthDay);
		int macAddrCount = macAddrList.size();
		int thirtyMinutesCount = 0; // 30분이하
		int hourCount = 0; // 한시간 이하
		int twoHoursCount = 0; // 두시간 이하
		int threeHoursCount = 0; // 세시간 미만
		int othersCount = 0; // 세시간 이상

		for (int i = 0; i < macAddrCount; i++) {
			HashMap param = new HashMap();
			param.put("macAddr", macAddrList.get(i).trim());
			param.put("yearMonthDay", yearMonthDay);
			int residenceTime = mainStore.getResidenceDayTimeByMacAddr(param); // MAC_ADDR를 기준으로 체류 시간 조회
			if (residenceTime > 5 && residenceTime <= 30) { // 30분 이하
				//
				thirtyMinutesCount = thirtyMinutesCount + 1;
			} else if (residenceTime > 5 && residenceTime <= 3600) { // 한 시간 이하
				//
				hourCount = hourCount + 1;
			} else if (residenceTime > 5 && residenceTime <= 7200) { // 두시간 이하
				//
				twoHoursCount = twoHoursCount + 1;
			} else if (residenceTime > 5 && residenceTime < 10800) { // 세시간 미만
				//
				threeHoursCount = threeHoursCount + 1;
			} else if(residenceTime >= 10800){ //세시간 이상
				//
				othersCount = othersCount + 1;
			}

		}
		HashMap map = new HashMap();
		map.put("thirtyMinutesCount", thirtyMinutesCount);
		map.put("hourCount", hourCount);
		map.put("twoHoursCount", twoHoursCount);
		map.put("threeHoursCount", threeHoursCount);
		map.put("othersCount", othersCount);
		HashMap result = new HashMap();
		result.put(yearMonthDay, map);

		long endTime = System.currentTimeMillis();
		long resultTime = (endTime - startTime) / 1000;
		System.out.println("==== 체류시간대별 방문자수 수행 시간 : " + resultTime + "초");

		return result;
	}
	
	/**
	 * 체류시간대별 방문자수 (30분 이하, 1시간 이하, 2시간이하 , 3시간미만, 3시간 이상) - (일별, isConnect=N)
	 * 
	 * @param yearMonthDay
	 *            => '2018-05-01' 포맷 준수
	 * @return
	 */
	public HashMap getNumberOfVisitorsByTimeOfDay(String yearMonthDay) {
		long startTime = System.currentTimeMillis();
		System.out.println("====체류시간대별 방문자수 시작====");

		List<String> macAddrList = mainStore.getMacAddrOfDay(yearMonthDay);
		int macAddrCount = macAddrList.size();
		int thirtyMinutesCount = 0; // 30분이하
		int hourCount = 0; // 한시간 이하
		int twoHoursCount = 0; // 두시간 이하
		int threeHoursCount = 0; // 세시간 미만
		int othersCount = 0; // 세시간 이상

		for (int i = 0; i < macAddrCount; i++) {
			HashMap param = new HashMap();
			param.put("macAddr", macAddrList.get(i).trim());
			param.put("yearMonthDay", yearMonthDay);
			int residenceTime = mainStore.getResidenceDayTimeByMacAddr(param); // MAC_ADDR를 기준으로 체류 시간 조회
			if (residenceTime > 5 && residenceTime <= 30) { // 30분 이하
				//
				thirtyMinutesCount = thirtyMinutesCount + 1;
			} else if (residenceTime > 5 && residenceTime <= 3600) { // 한 시간 이하
				//
				hourCount = hourCount + 1;
			} else if (residenceTime > 5 && residenceTime <= 7200) { // 두시간 이하
				//
				twoHoursCount = twoHoursCount + 1;
			} else if (residenceTime > 5 && residenceTime < 10800) { // 세시간 미만
				//
				threeHoursCount = threeHoursCount + 1;
			} else if(residenceTime >= 10800){ //세시간 이상
				//
				othersCount = othersCount + 1;
			}

		}
		HashMap map = new HashMap();
		map.put("thirtyMinutesCount", thirtyMinutesCount);
		map.put("hourCount", hourCount);
		map.put("twoHoursCount", twoHoursCount);
		map.put("threeHoursCount", threeHoursCount);
		map.put("othersCount", othersCount);
		HashMap result = new HashMap();
		result.put("day", map);

		long endTime = System.currentTimeMillis();
		long resultTime = (endTime - startTime) / 1000;
		System.out.println("==== 체류시간대별 방문자수 수행 시간 : " + resultTime + "초");

		return result;
	}

	
	

	/**
	 * 체류시간대별 방문자수 (30분 이하, 1시간 이하, 2시간이하 , 3시간미만, 3시간 이상) - (월별, isConnect=Y)
	 * 
	 * @param yearMonthDay
	 *            => '2018-05-01' 포맷 준수
	 * @return
	 * @throws ParseException 
	 */
	public List<HashMap> getIsConnectNumberOfVisitorsByTimeOfMonth(String yearMonth) throws ParseException {
		//
		long startTime = System.currentTimeMillis();
		System.out.println("====월 단위 체류시간대별 방문자수 시작====");

		SimpleDateFormat transDate = new SimpleDateFormat("yyyy-MM");
		Date tDate = transDate.parse(yearMonth);
		Calendar cal = Calendar.getInstance();
		cal.setTime(tDate);
		int endDayOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH); //해당 월의 일 수 가져옴.
		List<HashMap> resultList  = new ArrayList<HashMap>();
		for (int d = 1; d <= endDayOfMonth; d++) {
			String yearMonthDay = yearMonth;
			if (String.valueOf(d).length() == 1) {	//2018-MM-dd를 만들기.
				yearMonthDay = yearMonthDay + "-0" + d;

			} else {
				yearMonthDay = yearMonthDay + "-" + d;
			}
			resultList.add(this.getIsConnectNumberOfVisitorsByTimeOfDay(yearMonthDay));
		}

		long endTime = System.currentTimeMillis();
		long resultTime = (endTime - startTime) / 1000;
		System.out.println("==== 체류시간대별 방문자수 수행 시간 : " + resultTime + "초");
		return resultList;
	}
	
	/**
	 * 체류시간대별 방문자수 (30분 이하, 1시간 이하, 2시간이하 , 3시간미만, 3시간 이상) - (월별, isConnect=Y)
	 * 
	 * @param yearMonthDay
	 *            => '2018-05-01' 포맷 준수
	 * @return
	 * @throws ParseException 
	 */
	public List<HashMap> getNumberOfVisitorsByTimeOfMonth(String yearMonth) throws ParseException {
		//
		long startTime = System.currentTimeMillis();
		System.out.println("====월 단위 체류시간대별 방문자수 시작====");

		SimpleDateFormat transDate = new SimpleDateFormat("yyyy-MM");
		Date tDate = transDate.parse(yearMonth);
		Calendar cal = Calendar.getInstance();
		cal.setTime(tDate);
		int endDayOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH); //해당 월의 일 수 가져옴.
		List<HashMap> resultList  = new ArrayList<HashMap>();
		for (int d = 1; d <= endDayOfMonth; d++) {
			String yearMonthDay = yearMonth;
			if (String.valueOf(d).length() == 1) {	//2018-MM-dd를 만들기.
				yearMonthDay = yearMonthDay + "-0" + d;

			} else {
				yearMonthDay = yearMonthDay + "-" + d;
			}
			resultList.add(this.getIsConnectNumberOfVisitorsByTimeOfDay(yearMonthDay));
		}

		long endTime = System.currentTimeMillis();
		long resultTime = (endTime - startTime) / 1000;
		System.out.println("==== 체류시간대별 방문자수 수행 시간 : " + resultTime + "초");
		return resultList;
	}
	
	
	

	/**
	 * 층 별 평균 체류 시간
	 * @param yearMonthDay   (2018-05-01) 포맷 준수
	 * @param locationIdx    
	 * @return
	 */
	public long getIsConnectAvgResidenceTimeOfDayInFloor(String yearMonthDay, String locationIdx) {
		// TODO Auto-generated method stub
		long startTime = System.currentTimeMillis();
		System.out.println("====층별 평균 체류 시간 시작[location_idx="+locationIdx+"  [is_connect=Y]====");
		
		HashMap param = new HashMap();
		param.put("yearMonthDay", yearMonthDay);
		param.put("locationIdx", locationIdx);
		List<String> macAddrList = mainStore.getIsConnectMacAddrOfDayInFloor(param);

		int macAddrCount = 0;
		int sumResidenceTime = 0;
		long avgResidenceTimeOfDay = 0;
		for (int i = 0; i < macAddrList.size(); i++) {
			param.put("macAddr", macAddrList.get(i).trim());
			param.put("yearMonthDay", yearMonthDay);
			int residenceTime = mainStore.getResidenceDayTimeByMacAddr(param); // MAC_ADDR를 기준으로 체류 시간 조회
			if (residenceTime > 5) { // 체류 시간이 5분 초과일 경우만 체류 시간으로 인정
				sumResidenceTime = sumResidenceTime + residenceTime; // 평균 체류 시간을 계산 하기 위한 체류 시간 합산.
				macAddrCount = macAddrCount + 1; // 평균 체류 시간을 계산하기 위한 카운트
			}
		}
		if(macAddrCount != 0) {
			avgResidenceTimeOfDay = (sumResidenceTime) / (macAddrCount);
		}
		System.out.println("[" + yearMonthDay + "] 평균 체류 시간은 : " + avgResidenceTimeOfDay + "분입니다.");

		long endTime = System.currentTimeMillis();
		long resultTime = (endTime - startTime) / 1000;
		System.out.println("====수행 시간 : " + resultTime + "초");

		
		return avgResidenceTimeOfDay;
	}
	
	/**
	 * 층 별 평균 체류 시간
	 * @param yearMonthDay   (2018-05-01) 포맷 준수
	 * @param locationIdx    
	 * @return
	 */
	public long getAvgResidenceTimeOfDayInFloor(String yearMonthDay, String locationIdx) {
		// TODO Auto-generated method stub
		long startTime = System.currentTimeMillis();
		System.out.println("====층별 평균 체류 시간 시작[location_idx="+locationIdx+"  [is_connect=N]====");
		
		HashMap param = new HashMap();
		param.put("yearMonthDay", yearMonthDay);
		param.put("locationIdx", locationIdx);
		List<String> macAddrList = mainStore.getMacAddrOfDayInFloor(param);

		int macAddrCount = 0;
		int sumResidenceTime = 0;
		long avgResidenceTimeOfDay = 0;
		for (int i = 0; i < macAddrList.size(); i++) {
			param.put("macAddr", macAddrList.get(i).trim());
			param.put("yearMonthDay", yearMonthDay);
			int residenceTime = mainStore.getResidenceDayTimeByMacAddr(param); // MAC_ADDR를 기준으로 체류 시간 조회
			if (residenceTime > 5) { // 체류 시간이 5분 초과일 경우만 체류 시간으로 인정
				sumResidenceTime = sumResidenceTime + residenceTime; // 평균 체류 시간을 계산 하기 위한 체류 시간 합산.
				macAddrCount = macAddrCount + 1; // 평균 체류 시간을 계산하기 위한 카운트
			}
		}
		if(macAddrCount != 0) {
			avgResidenceTimeOfDay = (sumResidenceTime) / (macAddrCount);
		}
		System.out.println("[" + yearMonthDay + "] 평균 체류 시간은 : " + avgResidenceTimeOfDay + "분입니다.");

		long endTime = System.currentTimeMillis();
		long resultTime = (endTime - startTime) / 1000;
		System.out.println("====수행 시간 : " + resultTime + "초");

		
		return avgResidenceTimeOfDay;
	}

}
