//function pushData(data) {
//  var param="";
//  for(var p in data) {
//    if (typeof(p)!="function") {
//      if ((data[p]+"").indexOf("[")!==0) {
//        param+=p+"="+encodeURIComponent(data[p])+"&";
//      }
//    }
//  }
//  if (param) {
//    var src='./train/gatherData?'+param;
//    var img=new Image(1, 1);
//    alert("请求到的后端脚本为" + src);
//    img.src = src;
//  }
//}

(function () {
  var params = {};
    //Document对象数据
    if (document) {
        params.domain = document.domain || ''; //获取域名
        params.url = document.URL || '';       //当前Url地址
        params.title = document.title || '';
        params.referrer = document.referrer || '';  //上一跳路径
    }
    //Window对象数据
    if (window && window.screen) {
        params.sh = window.screen.height || 0;    //获取显示屏信息
        params.sw = window.screen.width || 0;
        params.cd = window.screen.colorDepth || 0;
    }
    //navigator对象数据
    if (navigator) {
        params.lang = navigator.language || '';    //获取所用语言种类
    }
    //解析_maq配置
    var paramUrl="";
    if (_gatherData) {
        for (var p in _gatherData) {                      //获取埋点阶段，传递过来的用户行为
          if (typeof(p)!="function") {
            if ((_gatherData[p]+"").indexOf("[")!==0) {
              paramUrl+=p+"="+encodeURIComponent(_gatherData[p])+"&";
            }
          }
        }
    }
    //拼接参数串
    var args = '';
    for (var i in params) {
        // alert(i);
        if (args != '') {
            args += '&';
        }
        args += i + '=' + params[i];           //将所有获取到的信息进行拼接
    }
    //通过伪装成Image对象，请求后端脚本
    var img = new Image(1, 1);
    var src = './train/gatherData?args=' + encodeURIComponent(args);
    if (paramUrl) src+="&"+paramUrl;
    alert("请求到的后端脚本为" + src);
    img.src = src;
})();