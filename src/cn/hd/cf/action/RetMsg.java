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
}
