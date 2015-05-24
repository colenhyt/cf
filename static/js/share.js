
function initShareSDK()
{
    $sharesdk.open("iosv1101", true);
    var sinaConf = {};
    sinaConf["app_key"] = "76300569a724";
    sinaConf["app_secret"] = "38a4f8204cc784f81f9f0daaf31e02e3";
    sinaConf["redirect_uri"] = "http://www.sharesdk.cn";
    $sharesdk.setPlatformConfig($sharesdk.platformID.SinaWeibo, sinaConf);

}

        function authBtnClickHandler()
        {
            $sharesdk.authorize($sharesdk.platformID.SinaWeibo, function (platform, state, error){

                alert("state = " + state + "\nerror = " + error);

            });
        }

        function hasAuthBtnClickHandler()
        {
            $sharesdk.hasAuthorized($sharesdk.platformID.SinaWeibo, function (platform, hasAuth) {

                alert("hasAuth = " + hasAuth);

            });
        }

        function userInfoBtnClickHandler()
        {
            $sharesdk.getUserInfo($sharesdk.platformID.SinaWeibo, function (platform, state, user, error) {

                alert("state = " + state + "\nuser = " + user + "\nerror = " + error);

            });
        }

        function shareContentClickHandler()
        {
            var params = {
                "text" : "微信财富人生内容",
                "imageUrl" : "http://img0.bdstatic.com/img/image/shouye/tangwei.jpg",
                "title" : "财富人生内容测试的标题",
                "titleUrl" : "http://sharesdk.cn",
                "description" : "测试的描述",
                "site" : "ShareSDK",
                "siteUrl" : "http://sharesdk.cn",
                "type" : $sharesdk.contentType.Text
            };

            var isSSO = false;
            $sharesdk.shareContent($sharesdk.platformID.WeChatTimeline, params, isSSO,function (platform, state, shareInfo, error) {
				var aa = JSON.stringify(shareInfo);
				var bb = JSON.stringify(error);
 
                alert("state = " + state + "\nshareInfo = " + aa + "\nerror = " + bb);

            });
        }

        function oneKeyShareContentClickHandler()
        {
            var params = {
                "text" : "财富人生内容新浪",
                "imageUrl" : "http://img0.bdstatic.com/img/image/shouye/tangwei.jpg",
                "title" : "财富人生标题",
                "titleUrl" : "http://sharesdk.cn",
                "description" : "测试的描述",
                "site" : "ShareSDK",
                "siteUrl" : "http://sharesdk.cn",
                "type" : 1
            };
           var isSSO = true;

            $sharesdk.oneKeyShareContent([$sharesdk.platformID.SinaWeibo], params, isSSO, function (platform, state, shareInfo, error) {
				
				var aa = JSON.stringify(shareInfo);
				var bb = JSON.stringify(error);
                alert("state = " + state + "\nshareInfo = " + aa + "\nerror = " + bb);

            });
        }

        function showShareMenuClickHandler()
        {
            var params = {
                "text" : "财富人生帅爆了，你也来吧...",
                "imageUrl" : "http://img0.bdstatic.com/img/image/shouye/tangwei.jpg",
                "title" : "测试的标题",
                "titleUrl" : "http://sharesdk.cn",
                "description" : "测试的描述333",
                "site" : "ShareSDK",
                "siteUrl" : "http://sharesdk.cn",
                "type" : 1
            };
           var isSSO = false;

            $sharesdk.showShareMenu(null, params, 100, 100, $sharesdk.shareMenuArrowDirection.Any,isSSO,  function (platform, state, shareInfo, error) {
				var aa = JSON.stringify(shareInfo);
				var bb = JSON.stringify(error);
				if (bb==null){
				 g_msg.tip('分享成功,返回应用');
				}else
                alert("state = " + state + "\n111shareInfo = " + aa + "\nerror = " + bb);

            });
        }

        function showShareViewClickHandler()
        {
            var params = {
                "text" : "测试分享内容",
                "imageUrl" : "http://img0.bdstatic.com/img/image/shouye/tangwei.jpg",
                "title" : "测试的标题",
                "titleUrl" : "http://sharesdk.cn",
                "description" : "测试的描述",
                "site" : "ShareSDK",
                "siteUrl" : "http://sharesdk.cn",
                "type" : 1
            };

            $sharesdk.showShareView($sharesdk.platformID.SinaWeibo, params, function (platform, state, shareInfo, error) {
				var aa = JSON.stringify(shareInfo);
				var bb = JSON.stringify(error);

                alert("state = " + state + "\nshareInfo = " + aa + "\nerror = " + bb);

            });
        }
