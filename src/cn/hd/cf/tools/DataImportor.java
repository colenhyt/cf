package cn.hd.cf.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cn.freeteam.util.StringUtil;
import cn.hd.cf.model.Signin;
import cn.hd.cf.model.Signindata;
import cn.hd.cf.service.SignindataService;

public class DataImportor {
	private String xlsPath = "cfdata.xlsx";
	private int ROW_INDEX_RECORD = 0;
	private int ROW_INDEX_NAME = 2;
	private int ROW_INDEX_DATA = 3;

	public void importSignindata()
	{
		JSONArray data = getJsondata("signin");
		System.out.println(data.toString());
		List<Signin> list = JSONArray.toList(data, Signin.class);
		Signin a = list.get(2);
		double version = getDataversion("signin");
		int i = data.size();
		Signindata dd = new Signindata();
		dd.setStatus(Byte.valueOf((byte)1));
		dd.setType(Byte.valueOf((byte)2));
		dd.setData(data.toString().getBytes());
		i =10;
	}
	
	private JSONArray getJsondata(String strSheetName)
	{
		JSONArray jsondata = new JSONArray();
		XSSFSheet st = getSheet(strSheetName);
        int rows=st.getLastRowNum()+1;//总行数  
        if (rows<3){
        	return jsondata;
        }
        
        int cols;//总列数  
        //schema
        XSSFRow row1=st.getRow(ROW_INDEX_NAME);//:row 0 
      
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
	
	
	private double getDataversion(String strSheetName)
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
   		return version;
	}
	
	private XSSFSheet getSheet(String strSheetName)
	{
        File fileDes = new File(xlsPath);  
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
		importor.importSignindata();
	}

}
