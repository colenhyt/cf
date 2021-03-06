package cn.hd.cf.action;

public interface RetMsg {
	public static int MSG_OK = 0;
	public static int MSG_ExecError = 1;
	public static int MSG_MoneyNotEnough = 2;
	public static int MSG_NoThisStock = 3;
	public static int MSG_NoSavingData = 4;
	public static int MSG_PlayerNotExist = 5;
	public static int MSG_WrongPlayerNameOrPwd = 6;
	public static int MSG_StockIsClosed= 7;
	public static int MSG_PlayerNameIsExist = 8;
	public static int MSG_SavingNotExist = 9;
	public static int MSG_SavingIsExist = 10;
	public static int MSG_InsureNotExist = 11;
	public static int MSG_InsureIsExist = 12;
	public static int MSG_StockNotExist = 13;
	public static int MSG_StockQtyIsZero= 14;
	public static int MSG_IllegalAccess= 15;
	public static int MSG_WrongOpenID= 16;
	public static int MSG_WrongStockPrice= 17;
	public static int MSG_ExceedMaxOpenCount= 18;
	public static int MSG_ExceedTodayMaxBuyAmount= 19;
	
	public static long MSG_WrongSession = -1;
	public static long MSG_WrongSessionSubmit = -2;
}
