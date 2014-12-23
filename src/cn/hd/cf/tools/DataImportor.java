package cn.hd.cf.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Iterator;

import net.sf.json.JSONArray;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cn.hd.base.Base;
import cn.hd.base.BaseService;
import cn.hd.cf.model.Quotedata;
import cn.hd.cf.model.Stock;
import cn.hd.cf.service.QuotedataService;
import cn.hd.cf.service.StockdataService;

public class DataImportor extends Base{
	private String cfg_file;
	private static String CONFIG_PATH_JS = "static/data/";
	private QuotedataService quotedataService;
	private StockdataService stockdataService;
	
	public static int ROW_INDEX_RECORD = 0;
	public static int ROW_INDEX_NAME = 2;
	public static int ROW_INDEX_DATA = 3;
	
	public DataImportor(String _cfg_file)
	{
		cfg_file = _cfg_file;
		quotedataService = new QuotedataService();
		stockdataService = new StockdataService();
		
	}
	
	public void importData(String dataName)
	{
		try {
			String strClassName = "cn.hd.cf.service."+dataName.substring(0,1).toUpperCase()+dataName.substring(1,dataName.length());
			strClassName += "Service";
			Object serviceOjb = Class.forName(strClassName).newInstance();
			Method serviceMethod=serviceOjb.getClass().getMethod("findActive",null) ;
			Object currObj = serviceMethod.invoke(serviceOjb, null);		

			Float newVersion = getRowData(dataName,0);
			
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

		if (dataName=="stockdata"||dataName=="eventdata"){
			Float freq = getRowData(dataName,2);
			Integer ifreq = Integer.valueOf(freq.intValue());
		    argsClass[0] = Integer.class;
			method=dataOjb.getClass().getMethod("setFreq",argsClass) ;
			method.invoke(dataOjb, ifreq);		
		}
		
		JSONArray data = getJsondata(dataName,ROW_INDEX_NAME,ROW_INDEX_DATA);
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
	
	public void outputXls2Js(){
        File fileDes = new File(cfg_file);  
        InputStream str;
		String dName = "quotedata";
		try {
			str = new FileInputStream(fileDes);
	        XSSFWorkbook xwb = new XSSFWorkbook(str);  //利用poi读取excel文件流  
	        Iterator<XSSFSheet> iterator = xwb.iterator();
			RandomAccessFile  dataFile = new RandomAccessFile(CONFIG_PATH_JS+"/"+dName+".js","rw");
			dataFile.setLength(0);
			dataFile.write(("\n").getBytes());
			dataFile.write(("var data_quotedata=[\n").getBytes());
	        while (iterator.hasNext())
	        {
	        	XSSFSheet i = (XSSFSheet) iterator.next();
	        	outputJsData2(dataFile,i.getSheetName(),0,1);
	        	System.out.println(i.getSheetName());
	        	
	        }	
			dataFile.write(("]\n").getBytes());
	        dataFile.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  		
	}
	
	public void outputAllJsData()
	{
		outputJsData("signindata",ROW_INDEX_NAME,ROW_INDEX_DATA);
	}
	
	public void outputJsData2(RandomAccessFile fileDes,String dataName,int nameIndex,int dataIndex)
	{
		try {
	        long fileLength = fileDes.length();
            //将写文件指针移到文件尾。
	        fileDes.seek(fileLength);	     
	        fileDes.write(("{name:'"+dataName+"',quote:[\n").getBytes());
			JSONArray data = getArraydata(dataName,nameIndex,dataIndex,30);
			for (int i=0;i<data.size();i++){
				fileDes.write(data.get(i).toString().getBytes());
				fileDes.write(",\n".getBytes());
			}
			fileDes.write("]},\n".getBytes());
			System.out.println("output ("+dataName+")js data success");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 	}
	
	private JSONArray getArraydata(String strSheetName,int nameIndex,int dataIndex,int maxRow)
	{
		JSONArray jsondata = new JSONArray();
		XSSFSheet st = getSheet(strSheetName);
        int rows = maxRow+1;//总行数  
        if (rows<2){
        	return jsondata;
        }
        int cols;//总列数  
        //schema
        XSSFRow row1=st.getRow(nameIndex);//:row name
      
        for(int i=dataIndex;i<rows;i++){  
            XSSFRow row=st.getRow(i);//读取某一行数据  
            if(row!=null){  
                //获取行中所有列数据  
                cols=row.getLastCellNum();  
                String record = "[";
            for(int j=0;j<cols;j++){  
                XSSFCell cell=row.getCell(j);  
                if(cell==null){  
                    System.out.print("   ");    
                }else{  
                //判断单元格的数据类型  
                switch (cell.getCellType()) {    
                    case XSSFCell.CELL_TYPE_STRING: // 字符串    
                        record += "\""+cell.getStringCellValue()+"\",";
                        break;    
                    case XSSFCell.CELL_TYPE_NUMERIC: // 数字,转为float
                    	float value = (float)cell.getNumericCellValue();
                        record += value+",";
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
                record += "]";
                jsondata.add(record);
            }  
        }  
        		
		return jsondata;
	}
	
	
	private Float getRowData(String strSheetName,int column)
	{
		XSSFSheet st = getSheet(strSheetName);
        int rows=st.getLastRowNum()+1;//总行数  
        //version
        XSSFRow row0=st.getRow(ROW_INDEX_RECORD);//:row 0 
        XSSFCell cell0=row0.getCell(column); 
        double version = 0;
   		if (cell0.getCellType()==XSSFCell.CELL_TYPE_STRING)
   		{
   	        XSSFCell cell1=row0.getCell(column+1); 
   	        if (cell1.getCellType()==XSSFCell.CELL_TYPE_NUMERIC)
   	        	version = Double.valueOf(cell1.getNumericCellValue());  			
   		}
   		float ver = (float)version;
   		return Float.valueOf(ver);
	}
	
	private XSSFSheet getSheet(String strSheetName)
	{
        File fileDes = new File(cfg_file);  
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
	
	private JSONArray getJsondata(String strSheetName,int nameIndex,int dataIndex)
	{
		JSONArray jsondata = new JSONArray();
		XSSFSheet st = getSheet(strSheetName);
	    int rows=st.getLastRowNum()+1;//总行数  
	    if (rows<2){
	    	return jsondata;
	    }
	    int cols;//总列数  
	    //schema
	    XSSFRow row1=st.getRow(nameIndex);//:row name
	
	    for(int i=dataIndex;i<rows;i++){  
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
	                case XSSFCell.CELL_TYPE_NUMERIC: // 数字,转为float
	                	float value = (float)cell.getNumericCellValue();
	                    record += value+",";
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

	public void outputJsData(String dataName,int nameIndex,int dataIndex)
	{
	    File fileDes = new File(CONFIG_PATH_JS+"/"+dataName+".js");  
	    FileOutputStream out = null;
		try {
			if (!fileDes.exists())
			{
				fileDes.createNewFile();
			}
	        out = new FileOutputStream(fileDes);
	        if (dataName=="eventdata"){
				Float freq = getRowData(dataName,2);
				Integer ifreq = Integer.valueOf(freq.intValue());
		        out.write(("var data_"+dataName+"_feq = "+ifreq+";\n\n").getBytes());
	        }
	        out.write(("var data_"+dataName+"=[\n").getBytes());
			JSONArray data = getJsondata(dataName,nameIndex,dataIndex);
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

	public void importXls2Quote(){
	    File fileDes = new File(cfg_file);  
	    InputStream str;
		try {
			str = new FileInputStream(fileDes);
	        XSSFWorkbook xwb = new XSSFWorkbook(str);  //利用poi读取excel文件流  
	        Iterator<XSSFSheet> iterator = xwb.iterator();
	        quotedataService.clear();
	        while (iterator.hasNext())
	        {
	        	XSSFSheet i = (XSSFSheet) iterator.next();
	        	importQuoteData(i.getSheetName(),0,1);
	        	
	        }	
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  		
	}

	public void importQuoteData(String dataName,int nameIndex,int dataIndex)
	{
		Quotedata record = new Quotedata();
		System.out.println("import quote for :"+dataName+" ....");
		JSONArray data = getArraydata(dataName,nameIndex,dataIndex,50);
		Stock stock = stockdataService.findStock(dataName);
		record.setName(dataName);
		record.setStockid(stock.getId());
		record.setData(data.toString().getBytes());
		quotedataService.add(record);
		System.out.println("import ("+dataName+") quote success");

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DataImportor importor = new DataImportor("cfdata.xlsx");
		String name = "questdata";
		importor.importData(name);
		importor.outputJsData(name,ROW_INDEX_NAME,ROW_INDEX_DATA);
//		importor.outputAllJsData();
		
		DataImportor importor2 = new DataImportor("stockdata.xlsx");
		//importor2.outputXls2Js();
		//importor2.importXls2Quote();
		
	}

}
