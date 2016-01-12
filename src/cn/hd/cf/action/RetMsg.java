package cn.hd.cf.action;

public interface RetMsg {
	public static int MSG_OK = 0;
	public static int MSG_SQLExecuteError = 1;
	public static int MSG_MoneyNotEnough = 2;
	public static int MSG_NoThisStock = 3;
	public static int MSG_NoSavingData = 4;
	public static int MSG_PlayerTelIsExist = 5;
	public static int MSG_WrongPlayerNameOrPwd = 6;
	public static int MSG_StockIsClosed= 7;
	public static int MSG_PlayerNameIsExist = 8;
	public static int MSG_SavingNotExist = 9;
	public static int MSG_SavingIsExist = 10;
	public static int MSG_InsureNotExist = 11;
	public static int MSG_InsureIsExist = 12;
	public static int MSG_StockNotExist = 13;
}
