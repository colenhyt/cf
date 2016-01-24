package cn.hd.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.Hierarchy;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.spi.DefaultRepositorySelector;
import org.apache.log4j.spi.LoggerFactory;
import org.apache.log4j.spi.RepositorySelector;
import org.apache.log4j.spi.RootCategory;

public class SigninLog extends Logger {
 public static Properties properties;
 static private RepositorySelector myRepositorySelector;
 
 protected SigninLog(String name) {
  super(name);

 }
 
 static {
	  if (properties == null) {
		   Hierarchy h = new Hierarchy(new RootCategory((Level) Level.DEBUG));
		   myRepositorySelector = new DefaultRepositorySelector(h);
		   properties = new Properties();
		   Properties props = new Properties();
		   try {
			String path = "./WEB-INF/classes/";
			URL  res = Thread.currentThread().getContextClassLoader().getResource("/");
			if (res!=null)
				path = res.getPath();	    
		    String filePath = path + "signin.properties" ;
		    InputStream in = new BufferedInputStream(new FileInputStream(new File( filePath )));
		    props.load(in);
		   } catch (Exception ex) {
		    ex.printStackTrace();
		   }
		   properties.setProperty("log4j.rootCategory", "info, R");
		   properties.setProperty("log4j.appender.R","org.apache.log4j.DailyRollingFileAppender");
		   properties.setProperty("log4j.appender.R.File", props.getProperty("log4j.appender.R.File"));
		   properties.setProperty("log4j.appender.R.layout",props.getProperty("log4j.appender.R.layout"));
		   properties.setProperty("log4j.appender.R.layout.ConversionPattern",props.getProperty("log4j.appender.R.layout.ConversionPattern"));
		   new PropertyConfigurator().doConfigure(properties,myRepositorySelector.getLoggerRepository());
	  }	 
 }
   public static Logger getLogger(String name)
    {
    return myRepositorySelector.getLoggerRepository().getLogger(name);
    }

    public static Logger getLogger(Class clazz)
    {
      return myRepositorySelector.getLoggerRepository().getLogger(clazz.getName());
    }

    public static Logger getRootLogger()
    {
        return myRepositorySelector.getLoggerRepository().getRootLogger();
    }

    public static Logger getLogger(String name, LoggerFactory factory)
    {
     return myRepositorySelector.getLoggerRepository().getLogger(name, factory);
    }
    
    public static void main(String[] args){
    	SigninLog.getRootLogger().info("aaa");
    }
} 