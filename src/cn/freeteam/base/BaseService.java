package cn.freeteam.base;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import cn.freeteam.util.MybatisSessionFactory;


/**
 * Service的父类，提供通用方法
 * @author freeteam
 * 2011-6-14
 */
public class BaseService extends Base{
	public static byte DATA_STATUS_INACTIVE = 0;
	public static byte DATA_STATUS_ACTIVE = 1;
	
	/**
	 * 初始化指定变量
	 * @param objs 需要初始化变量列表
	 */
	protected  void initMapper(String...objs){
		try {
			for(String obj:objs){
				if(obj!=null && obj.trim().length()>0){
					//获取变量的get方法
					Method method=getClass().getMethod("get"+varMethodName(obj), null);
					//调用get方法，判断对象是否已初始化
					if (method.invoke(this, null)==null) {
						//获取get方法返回类型，即变量类型,然后动态创建对象
						initMapperObj(method.getReturnType().toString(), obj);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 初始化指定变量initMapperObj
	 * @param className 要创建对象的类名
	 * @param varName 变量名
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 */
	protected  void initMapperObj(String className,String varName) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException{
		//获取名全名
		Class clazz=Class.forName(className.replaceFirst("class ", "").replaceFirst("interface ", ""));
		//创建对象
		Object obj=MybatisSessionFactory.getSession().getMapper(clazz);
		//获取变量的set方法
		Method method=getClass().getMethod("set"+varMethodName(varName), clazz);
		//调用set方法
		method.invoke(this, obj);
	}
	
}
