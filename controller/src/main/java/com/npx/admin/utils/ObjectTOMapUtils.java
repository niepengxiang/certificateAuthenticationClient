package com.npx.admin.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.npx.admin.pojo.CaCredential;

public class ObjectTOMapUtils {

public static Map<String, Object> obj2Map(Object obj) {
		Map<String, Object> map = new HashMap<String, Object>();
		// System.out.println(obj.getClass());
		// 获取f对象对应类中的所有属性域
		Field[] fields = obj.getClass().getDeclaredFields();
		for (int i = 0, len = fields.length; i < len; i++) {
			String varName = fields[i].getName();
			try {
				// 获取原来的访问控制权限
				boolean accessFlag = fields[i].isAccessible();
				// 修改访问控制权限
				fields[i].setAccessible(true);
				// 获取在对象f中属性fields[i]对应的对象中的变量
				Object o = fields[i].get(obj);
				if (o != null)
					map.put(varName, o);
				// System.out.println("传入的对象中包含一个如下的变量：" + varName + " = " + o);
				// 恢复访问控制权限
				else 
					map.put(varName, null);
				fields[i].setAccessible(accessFlag);
			} catch (IllegalArgumentException ex) {
				ex.printStackTrace();
			} catch (IllegalAccessException ex) {
				ex.printStackTrace();
			}
		}
		return map;
	}


	public static void main(String[] args) {
		CaCredential caCredential = new CaCredential();
		
		
		caCredential.setAuthSrcId("11111");
		
		
		Map<String, Object> obj2Map = obj2Map(caCredential);
		
		System.out.println(obj2Map);
	}
}
