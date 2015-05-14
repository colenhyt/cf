
function initShareSDK()
{
    $sharesdk.open("iosv1101", true);
    var sinaConf = {};
    sinaConf["app_key"] = "568898243";
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
                "text" : "测试的文字",
                "imageUrl" : "http://img0.bdstatic.com/img/image/shouye/tangwei.jpg",
                "title" : "测试的标题",
                "titleUrl" : "http://sharesdk.cn",
                "description" : "测试的描述",
                "site" : "ShareSDK",
                "siteUrl" : "http://sharesdk.cn",
                "type" : $sharesdk.contentType.Text
            };

            $sharesdk.shareContent($sharesdk.platformID.SinaWeibo, params, function (platform, state, shareInfo, error) {

                alert("state = " + state + "\nshareInfo = " + shareInfo + "\nerror = " + error);

            });
        }

        function oneKeyShareContentClickHandler()
        {
            var params = {
                "text" : "测试的文字",
                "imageUrl" : "http://img0.bdstatic.com/img/image/shouye/tangwei.jpg",
                "title" : "测试的标题",
                "titleUrl" : "http://sharesdk.cn",
                "description" : "测试的描述",
                "site" : "ShareSDK",
                "siteUrl" : "http://sharesdk.cn",
                "type" : 1
            };

            $sharesdk.oneKeyShareContent([$sharesdk.platformID.SinaWeibo, $sharesdk.platformID.TencentWeibo], params, function (platform, state, shareInfo, error) {

                alert("state = " + state + "\nshareInfo = " + shareInfo + "\nerror = " + error);

            });
        }

        function showShareMenuClickHandler()
        {
            var params = {
                "text" : "测试的文字",
                "imageUrl" : "http://img0.bdstatic.com/img/image/shouye/tangwei.jpg",
                "title" : "测试的标题",
                "titleUrl" : "http://sharesdk.cn",
                "description" : "测试的描述",
                "site" : "ShareSDK",
                "siteUrl" : "http://sharesdk.cn",
                "type" : 1
            };

            $sharesdk.showShareMenu(null, params, 100, 100, $sharesdk.shareMenuArrowDirection.Any, function (platform, state, shareInfo, error) {

                alert("state = " + state + "\nshareInfo = " + shareInfo + "\nerror = " + error);

            });
        }

        function showShareViewClickHandler()
        {
            var params = {
                "text" : "测试的文字",
                "imageUrl" : "http://img0.bdstatic.com/img/image/shouye/tangwei.jpg",
                "title" : "测试的标题",
                "titleUrl" : "http://sharesdk.cn",
                "description" : "测试的描述",
                "site" : "ShareSDK",
                "siteUrl" : "http://sharesdk.cn",
                "type" : 1
            };

            $sharesdk.showShareView($sharesdk.platformID.SinaWeibo, params, function (platform, state, shareInfo, error) {

                alert("state = " + state + "\nshareInfo = " + shareInfo + "\nerror = " + error);

            });
        }
