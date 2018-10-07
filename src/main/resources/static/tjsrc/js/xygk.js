$(function (param) { 
  // 上部分数据
  $.ajax({
    type: "get",
    url: "../../demoJson/json161.json",
    data: "",
    dataType: "json",
    success: function (res) {
      if(res.returnType == 00) {
        $("#mnsc").text(res.data.ts_mnsc+"次")
        $("#xxcs").text(res.data.ts_kj+"次")
        $("#lxcs").text(res.data.q_sj+"次")
      }
    }
  });
  //中部左数据
  $.ajax({
    type: "get",
    url: "../../demoJson/json162_mnsc.json",
    data: "",
    dataType: "json",
    success: function (res) {
      if(res.returnCode == 00) {
        var html = ""
        for(var j=0;j<res.data.length;j++) {
          html+= '<li class="col-sm-6" style="padding-left:0;">'
            +'<div class="con">'
              +'<div class="pic"><img src="'+(res.data[j].mainUrl || "../../tjsrc/images/mnsc_default.jpg")+'"></div>'
              +'<div class="botoom">'
                +'<h2>'+res.data[j].mnscName+'</h2>'
                +'<div class="row-box">'
                  +'<div class="left">'
                    +'<span class="eay"><img src="../../tjsrc/images/rs.jpg"></span>'
                    +'<span class="num">'+res.data[j].logVisitCount+'</span>'
                  +'</div>'
                  +'<div class="right">'
                    +' <span>'+res.data[j].createDate+'</span>发布'
                  +'</div>'
                +'</div>'
              +'</div>'
            +'</div>'
          +'</li>'
        }
        $("#mnscList").html(html)
      }
    }
  });
  //中部右数据
  $.ajax({
    type: "get",
    url: "../../demoJson/json163_kj.json",
    data: "",
    dataType: "json",
    success: function (res) {
      if(res.returnCode == 00) {
        var html = ""
        for(var j=0;j<res.data.length;j++) {
          html+= '<li class="col-sm-6" style="padding-left:0;">'
            +'<div class="con">'
              +'<div class="pic"><img src="'+(res.data[j].imgUrl || "../../tjsrc/images/jiantou.png")+'"></div>'
              +'<div class="botoom">'
                +'<h2>'+res.data[j].catNames+'</h2>'
                +'<div class="row-box">'
                  +'<div class="left">'
                    +'<span class="eay"><img src="../../tjsrc/images/rs.jpg"></span>'
                    +'<span class="num">'+res.data[j].visitCount+'</span>'
                  +'</div>'
                  +'<div class="right">'
                    +' <span>'+res.data[j].createDate+'</span>发布'
                  +'</div>'
                +'</div>'
              +'</div>'
            +'</div>'
          +'</li>'
        }
        $("#kjList").html(html)
      }
    }
  });

  //下部数据
  $.ajax({
    type: "get",
    url: "../../demoJson/json164.json",
    data: "",
    dataType: "json",
    success: function (res) {
      console.log(res)
      if(res.returnCode == 00) {
        var html = ""
        for(var j=0;j<res.data.length;j++) {
          html+= '<li class="row"><div class="right col-xs-6">我观看了：<span>'+res.data[j].groupName+'的'+res.data[j].objType+'</span></div><div class="col-xs-6 date">'+res.data[j].visitTime+'</div></li>'
        }
        $(".logList").html(html)
      }
    }
  });
 })