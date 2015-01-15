package cn.hd.cf.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cn.hd.base.Base;
import cn.hd.cf.model.Quote;
import cn.hd.cf.model.Stockdata;
import cn.hd.cf.service.QuotedataService;
import cn.hd.cf.service.StockdataService;

public class StockDataImportor extends Base{
	private String cfg_file;
	private static String CONFIG_PATH_JS = "static/data/";
	private QuotedataService quotedataService;
	private StockdataService stockdataService;
	
	public static int ROW_INDEX_RECORD = 0;
	public static int ROW_INDEX_NAME = 2;
	public static int ROW_INDEX_DATA = 3;
	
	public StockDataImportor(String _cfg_file)
	{
		cfg_file = _cfg_file;
		quotedataService = new QuotedataService();
		stockdataService = new StockdataService();
		
	}
	
	private JSONArray getArraydata(int stockid,String strSheetName,int nameIndex,int dataIndex,int maxRow)
	{
		JSONArray jsondata = new JSONArray();
		XSSFSheet st = getSheet(strSheetName);
        int rows = maxRow+1;//总行数  
        if (rows<2||st==null){
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
                Quote record = new Quote();
                record.setStockid(stockid);
            for(int j=0;j<cols;j++){  
                XSSFCell cell=row.getCell(j);  
                if(cell==null){  
                    System.out.print("   ");    
                }else{  
                //判断单元格的数据类型  
                String strV = "";
                float floatV = 0;
                switch (cell.getCellType()) {    
                    case XSSFCell.CELL_TYPE_STRING: // 字符串    
                    	strV = "\""+cell.getStringCellValue()+"\",";
                        break;    
                    case XSSFCell.CELL_TYPE_NUMERIC: // 数字,转为float
                    	floatV = (float)cell.getNumericCellValue();
                        break;    
                    case XSSFCell.CELL_TYPE_BOOLEAN: // bool 
                        break;    
                    default:    
                    	floatV = (float)cell.getNumericCellValue();
                        break;    
                    }
                if (j==0){
                	record.setDate(strV);
                }
                else if (j==1)
                	record.setUpprice(floatV);
                else if (j==2)
                	record.setOpenprice(floatV);
                else if (j==3)
                	record.setPrice(floatV);
                else if (j==4)
                	record.setLowprice(floatV);
                else if (j==5)
                	record.setQty((int)floatV);
            }  
            }  
            //System.out.println("增加一个行情价格:"+record.getPrice());
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

	public List<Stockdata>  findStocks(){
		String listName = "stockdata";
		JSONArray data = getJsondata(listName,ROW_INDEX_NAME,ROW_INDEX_DATA);
		List<Stockdata> stocks  = JSONArray.toList(data, Stockdata.class);
		return stocks;
	}
	public void importStockdata(){
		stockdataService.clear();
		List<Stockdata> stocks = findStocks();
		Float freq = getRowData("stockdata",2);
		Integer ifreq = Integer.valueOf(freq.intValue());
		for (int i=0;i<stocks.size();i++){
			Stockdata stock = stocks.get(i);
			JSONArray data = getArraydata(stock.getId(),stock.getName(),0,1,50);
			stock.setCreatetime(new Date());
			stock.setQuotes(data.toString().getBytes());	
			stock.setFreq(ifreq);
			stock.setStatus((byte)1);
			Stockdata existData = stockdataService.findStockdata(stock.getName());
			if (existData==null) {
				stockdataService.add(stock);
				System.out.println("导入股票数据: "+stock.getName());
			}
		}
	}

	public static void main(String[] args) {	
		StockDataImportor importor2 = new StockDataImportor("stockdata.xlsx");
		importor2.importStockdata();
		
		DataImportor importor3 = new DataImportor("stockdata.xlsx");
		String name = "stockdata";
		//importor3.outputMapJsData(name,ROW_INDEX_NAME,ROW_INDEX_DATA);	
		
	}

}
