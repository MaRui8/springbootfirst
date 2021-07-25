package com.mr;

import com.ggstar.util.ip.IpHelper;

/**
 * @author Mr
 * @date 2020/5/2
 */
public class SimpleTest {

	public static void main(String[] args) {
		String ip = "83.97.20.21";
		String ip1 = "183.208.25.81";
		String region = IpHelper.findRegionByIp(ip);
		String city = IpHelper.findCityByIp(ip);
		System.out.println(region);
		System.out.println(city);
	}
}
