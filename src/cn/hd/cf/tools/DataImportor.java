package cn.hd.cf.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cn.hd.base.Base;
import cn.hd.base.BaseService;
import cn.hd.cf.model.*;
import cn.hd.cf.service.SignindataService;

public class DataImportor extends Base{
	private static String CONFIG_PATH_JS = "static/data/";
	private static String CONFIG_FILE_LOCATION = "cfdata.xlsx";
	
	public static String DATA_NAME_SIGNIN = "signindata";
	
	private int ROW_INDEX_RECORD = 0;
	private int ROW_INDEX_NAME = 2;
	private int ROW_INDEX_DATA = 3;
	
	private SignindataService signindataService;

	public DataImportor()
	{
		signindataService = new SignindataService();
	}
	
	public void importData(String dataName)
	{
		try {
			String strClassName = "cn.hd.cf.service."+dataName.substring(0,1).toUpperCase()+dataName.substring(1,dataName.length());
			strClassName += "Service";
			Object serviceOjb = Class.forName(strClassName).newInstance();
			Method serviceMethod=serviceOjb.getClass().getMethod("findActive",null) ;
			Object currObj = serviceMethod.invoke(serviceOjb, null);		

			Float newVersion = getDataversion(dataName);
			
		if (currObj!=null){
			Method method=currObj.getClass().getMethod("getVersion",null) ;
			Float currVer = (Float)method.invoke(currObj, null);		
			if (currVer.floatValue()>=newVersion.floatValue())
			{
				System.out.println(dataName+" current data version("+currVer.floatValue()+") is not less than xlsx version("+newVersion+"),could not import");
				return;
			}
		}
		
		strClassName = "cn.hd.cf.model."+dataName.substring(0,1).toUpperCase()+dataName.substring(1,dataName.length());
		 Object dataOjb = Class.forName(strClassName).newInstance();
		 
		 Class[] argsClass = new Class[1];     

		Byte param = Byte.valueOf(BaseService.DATA_STATUS_ACTIVE);
	    argsClass[0] = param.getClass();			
		Method method=dataOjb.getClass().getMethod("setStatus",argsClass) ;
		method.invoke(dataOjb, param);		

		param = Byte.valueOf((byte)2);
	    argsClass[0] = param.getClass();			
		method=dataOjb.getClass().getMethod("setType",argsClass) ;
		method.invoke(dataOjb, param);		

		Date time = new Date(); 
	    argsClass[0] = time.getClass();			
		method=dataOjb.getClass().getMethod("setCreatetime",argsClass) ;
		method.invoke(dataOjb, time);	
		
	    argsClass[0] = Float.class;
		method=dataOjb.getClass().getMethod("setVersion",argsClass) ;
		method.invoke(dataOjb, newVersion);		

		JSONArray data = getJsondata(dataName);
		System.out.println("find "+dataName+" xls data:"+data.toString());
	    argsClass[0] = byte[].class;			
		method=dataOjb.getClass().getMethod("setData",argsClass) ;
		method.invoke(dataOjb, data.toString().getBytes());		
			
		boolean ret = false;
		argsClass[0] = dataOjb.getClass();
		serviceMethod=serviceOjb.getClass().getMethod("add",argsClass) ;
		ret = (boolean)serviceMethod.invoke(serviceOjb, dataOjb);		
		
		if (ret&&currObj!=null)
		{
			argsClass[0] = currObj.getClass();
			serviceMethod=serviceOjb.getClass().getMethod("resetInacvtive",argsClass) ;
			serviceMethod.invoke(serviceOjb, currObj);		
			System.out.println(dataName+"old data status reset success!!");
		}
		System.out.println(dataName+"(version:"+newVersion+") import success!!");
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void outputAllJsData()
	{
		outputJsData(DATA_NAME_SIGNIN);
	}
	
	public void outputJsData(String dataName)
	{
        File fileDes = new File(CONFIG_PATH_JS+"/"+dataName+".js");  
        FileOutputStream out = null;
		try {
			if (!fileDes.exists())
			{
				fileDes.createNewFile();
			}
	        out = new FileOutputStream(fileDes);
	        out.write(("var data_"+dataName+"=[\n").getBytes());
			JSONArray data = getJsondata(dataName);
			for (int i=0;i<data.size();i++){
			out.write(data.get(i).toString().getBytes());
			out.write(",\n".getBytes());
			}
			out.write("]\n".getBytes());
			System.out.println("output ("+dataName+")js data success");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 	}
	
	private JSONArray getJsondata(String strSheetName)
	{
		JSONArray jsondata = new JSONArray();
		XSSFSheet st = getSheet(strSheetName);
        int rows=st.getLastRowNum()+1;//总行数  
        if (rows<2){
        	return jsondata;
        }
        int cols;//总列数  
        //schema
        XSSFRow row1=st.getRow(ROW_INDEX_NAME);//:row name
      
        for(int i=ROW_INDEX_DATA;i<rows;i++){  
            XSSFRow row=st.getRow(i);//读取某一行数据  
            if(row!=null){  
                //获取行中所有列数据  
                cols=row.getLastCellNum();  
                String record = "{";
            for(int j=0;j<cols;j++){  
                XSSFCell cell=row.getCell(j);  
                if(cell==null){  
                    System.out.print("   ");    
                }else{  
                //判断单元格的数据类型  
                	record += "\""+row1.getCell(j).getStringCellValue()+"\":";
                switch (cell.getCellType()) {    
                    case XSSFCell.CELL_TYPE_STRING: // 字符串    
                        record += "\""+cell.getStringCellValue()+"\",";
                        break;    
                    case XSSFCell.CELL_TYPE_NUMERIC: // 数字,转为整形
                    	double value = cell.getNumericCellValue();
                        record += Integer.valueOf((int)value)+",";
                        break;    
                    case XSSFCell.CELL_TYPE_BOOLEAN: // bool 
                        record += cell.getBooleanCellValue()+",";
                        break;    
                    default:    
                        record += cell.getNumericCellValue()+",";
                        break;    
                    }    
            }  
            }  
                record += "}";
                jsondata.add(record);
            }  
        }  
        		
		return jsondata;
	}
	
	
	private Float getDataversion(String strSheetName)
	{
		XSSFSheet st = getSheet(strSheetName);
        int rows=st.getLastRowNum()+1;//总行数  
        //version
        XSSFRow row0=st.getRow(ROW_INDEX_RECORD);//:row 0 
        XSSFCell cell0=row0.getCell(0); 
        double version = 0;
   		if (cell0.getCellType()==XSSFCell.CELL_TYPE_STRING)
   		{
   	        XSSFCell cell1=row0.getCell(1); 
   	        if (cell1.getCellType()==XSSFCell.CELL_TYPE_NUMERIC)
   	        	version = Double.valueOf(cell1.getNumericCellValue());  			
   		}
   		float ver = (float)version;
   		return Float.valueOf(ver);
	}
	
	private XSSFSheet getSheet(String strSheetName)
	{
        File fileDes = new File(CONFIG_FILE_LOCATION);  
        InputStream str;
		try {
			str = new FileInputStream(fileDes);
	        XSSFWorkbook xwb = new XSSFWorkbook(str);  //利用poi读取excel文件流  
	        return xwb.getSheet(strSheetName);  //读取sheet的第一个工作表  		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		return null;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DataImportor importor = new DataImportor();
		importor.importData("stockdata");
		importor.outputJsData("stockdata");
//		importor.outputAllJsData();
	}

}
