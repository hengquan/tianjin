function closeWin() {
  var browserName=navigator.appName;
  if (browserName=="Microsoft Internet Explorer") { 
    window.parent.opener = "whocares"; 
    window.parent.close(); 
  } else {
    window.open('', '_self', '');
    window.close();
  }
}

var intervalHandler;
var sjData;
var count=0;
var startTime;

$(function (param) {
  $("body").hide();
  //获得分类
  $.ajax({
    type: "get",
    url: "./train/getCateData",
    data: "",
    dataType: "json",
    success: function (res) {
      var arr = [];
      var html = "";
      for(var i = 0;i<res.data.list.length;i++) {
        if(res.data.list[i].parentId == 0) {
          arr.push(res.data.list[i])
        }
      }
      for(var j=0;j<arr.length;j++) {
        if (j==0) html+= '<option value="'+arr[j].id+'">'+arr[j].name+'</option>';
        else html+= '<option value="'+arr[j].id+'">'+arr[j].name+'</option>';
      }
      $(".selectpicker").html(html);
      $('.selectpicker').selectpicker('refresh');
    }
  });

  $( "#slider-range" ).slider({
    range: true,
    min: 0,
    max: 9,
    values: [ 0, 9 ],
    slide: function( event, ui ) {
      $( "#amount" ).val( ui.values[ 0 ] + " - " + ui.values[ 1 ] );
    }
  });
  $( "#amount" ).val( $( "#slider-range" ).slider( "values", 0 ) +" - " + $( "#slider-range" ).slider( "values", 1 ) );
  //获得参数
  var id=getUrlParam("id");
  var type=getUrlParam("type");
  if (id) {
    if (type==1) dt(id);//答题
    else
    if (type==2) showSj(id);//查看试卷
  } else {
    // 生成试卷
    $(".scsj-btn").on("click",buildPaper);
    // 提交试卷
    $(".sub-btn").on("click",commitSj);
    $("body").show();
  }
});

//回显试卷
function showSj(id) {
  var _data={"id": id};
  $.ajax({
    type: "get",
    url: "./train/showSj",
    data: _data,
    dataType: "json",
    success: function (res) {
      if(res.returnCode == 00) {
        //设置时间
        var begin = new Date(Date.parse(res.data.beginTime.replace(/-/g, "/")));
        var end = new Date(Date.parse(res.data.endTime.replace(/-/g, "/")));
        var sCount=(end.getTime()-begin.getTime())/1000;
        var s=sCount%60;
        var tmp=Math.floor(sCount/60);
        var m=tmp%60;
        tmp=Math.floor(tmp/60);
        var h=tmp%60;
        tmp=((100+h)+"").substring(1)+":"+((100+m)+"").substring(1)+":"+((100+s)+"").substring(1);
        $(".times").css("display","block").css("cursor", "default").html(tmp);

        $(".empty").css("display","none");
        $(".lxt").css("display","block");
        $(".lxt").html(res.data.name);
        $(".sub-btn").css("display","block").addClass('disable-btn');
        $("#table").show();
        $("#zts").html(res.data.tmList.length);
        var html = ""
        var zfs = 0;
        var okCount=0;
        sjData = res.data;
        for(var i=0;i<res.data.tmList.length;i++) {
          zfs+=res.data.tmList[i].tmScore
          if(res.data.tmList[i].tmType == "多选题") {
            html += '<li><h3 class="title"><span>'+(i*1+1)+'、</span>【多选题】'+res.data.tmList[i].tmDesc+/*'【'+res.data.tmList[i].tmScore+'分】*/'</h3>'
              +tplCheckbox(res.data.tmList[i].tmItems,res.data.tmList[i].tmId);
          }else if(res.data.tmList[i].tmType == "单选题") {
            html += '<li><h3 class="title"><span>'+(i*1+1)+'、</span>【单选题】'+res.data.tmList[i].tmDesc+/*'【'+res.data.tmList[i].tmScore+'分】*/'</h3>'
            +tplRadio(res.data.tmList[i].tmItems,res.data.tmList[i].tmId)
          }else if(res.data.tmList[i].tmType == "判断题") {
            html += '<li><h3 class="title"><span>'+(i*1+1)+'、</span>【判断题】'+res.data.tmList[i].tmDesc+/*'【'+res.data.tmList[i].tmScore+'分】*/'</h3>'
            +tplRadio(res.data.tmList[i].tmItems,res.data.tmList[i].tmId)
          }
          html+='<div class="tm-result" id="tm-result-'+res.data.tmList[i].tmId+'"><span class="zqda">正确答案：'+(res.data.tmList[i].tmAnswer?res.data.tmList[i].tmAnswer:"未知")+'</span>';
          if (res.data.tmList[i].answerType==0) html+='<span class="ndda">您的答案：未答';//未答题
          else
          if (res.data.tmList[i].answerType==1) html+='<span class="ndda" style="color:green">您的答案：'+res.data.tmList[i].youAnswer;//答对
          else
          if (res.data.tmList[i].answerType==2) html+='<span class="ndda">您的答案：'+res.data.tmList[i].youAnswer;//答错
          html+='</span></div></li>';
          if (res.data.tmList[i].answerType==1) okCount++;
        }
        $(".topic-list").html(html);
        $(".zfs").html(zfs);
        $(".zts").html(res.data.tmList.length);
        $(".dd").html(okCount);
        $(".inp-box").hide();
        $(".times").css({"position":"block", "margin-top":"45px", "bottom":"30px"});
        $(".topic-box").css("padding-top", "10px");
        $(".sample").css("margin-top", "60px");
        $("body").show();
      }
    }
  });
}

//作答之前的试卷
function dt(id) {
  var _data={"id": id};
  $.ajax({
    type: "get",
    url: "./train/showSj",
    data: _data,
    dataType: "json",
    success: function (res) {
      if(res.returnCode == 00) {
        $(".times").css("display","block").css("cursor", "pointer").html("开始练习");
        $(".empty").css("display","none");
        $(".lxt").css("display","block");
        $(".lxt").html(res.data.name);
        $(".sub-btn").css("display","block").addClass('disable-btn');
        $(".times").css("display","block").on("click",start);
        $("#table").show();
        $("#zts").html(res.data.tmList.length);
        $("#dd").html(0);
        var html = ""
        var zfs = 0;
        var okCount=0;
        sjData = res.data;
        for(var i=0;i<res.data.tmList.length;i++) {
          zfs+=res.data.tmList[i].tmScore
          if(res.data.tmList[i].tmType == "多选题") {
            html += '<li><h3 class="title"><span>'+(i*1+1)+'、</span>【多选题】'+res.data.tmList[i].tmDesc+/*'【'+res.data.tmList[i].tmScore+'分】*/'</h3>'
              +tplCheckbox(res.data.tmList[i].tmItems,res.data.tmList[i].tmId);
          }else if(res.data.tmList[i].tmType == "单选题") {
            html += '<li><h3 class="title"><span>'+(i*1+1)+'、</span>【单选题】'+res.data.tmList[i].tmDesc+/*'【'+res.data.tmList[i].tmScore+'分】*/'</h3>'
            +tplRadio(res.data.tmList[i].tmItems,res.data.tmList[i].tmId)
          }else if(res.data.tmList[i].tmType == "判断题") {
            html += '<li><h3 class="title"><span>'+(i*1+1)+'、</span>【判断题】'+res.data.tmList[i].tmDesc+/*'【'+res.data.tmList[i].tmScore+'分】*/'</h3>'
            +tplRadio(res.data.tmList[i].tmItems,res.data.tmList[i].tmId)
          }
          html+='<div class="tm-result" id="tm-result-'+res.data.tmList[i].tmId+'"><span class="zqda"></span><span class="ndda"></span></div></li>';
          if (res.data.tmList[i].answerType==1) okCount++;
        }
        $(".topic-list").html(html);
        $(".zfs").html(zfs);
        $(".zts").html(res.data.tmList.length);
        $(".dd").html(okCount);
        // 提交试卷
        $(".sub-btn").on("click",commitSj);
        $("body").show();
      }
    }
  });
}

function tplCheckbox(res,tmid) {
  var html = ""
  for(var i = 0;i<res.length;i++) {
   html += '<p><span><input type="checkbox" disabled=disabled name="'+tmid+'" value="'+res[i].itemSign+'"></span>'+res[i].itemSign+'、'+res[i].itemDesc+'</p>';
  }   
  return html
}
function tplRadio(res,tmid) {
 var html = "";
  for(var i = 0;i<res.length;i++) {
   html += '<p><span><input type="radio" disabled=disabled name="'+tmid+'" value="'+res[i].itemSign+'"></span>'+res[i].itemSign+'、'+res[i].itemDesc+'</p>';
  }
  return html;
}

function buildPaper() {
  if (!$(".selectpicker").val()) {
    layer.alert("至少选择一个分类");
    return;
  }
  layer.msg('试题生成中......', {
    icon: 16 ,shade: 0.01
  });
  clearInterval(intervalHandler);
  count = 0;
  var _data={
    cateIds:($(".selectpicker").val()).join(","),
    diffRange:$( "#slider-range" ).slider( "values", 0 )+","+$( "#slider-range" ).slider( "values", 1 ),
    tmCount:$(".nums").val()
  };
  $.ajax({
    type: "get",
    url: "./train/getSj",
    data: _data,
    dataType: "json",
    success: function (res) {
      if(res.returnCode == 00) {
        $(".lxt").html(res.data.name);
        $(".empty").css("display","none");
        $(".lxt").css("display","block");
        $(".times").html("开始练习");
        $(".times").css("display","block").on("click",start);
        $(".sub-btn").css("display","block").addClass('disable-btn');
        $("#table").show();
        $("#zts").html(res.data.tmList.length);
        $("#dd").html(0);
        var html = ""
        var zfs = 0;
        sjData = res.data;
        for(var i=0;i<res.data.tmList.length;i++) {
          zfs+=res.data.tmList[i].tmScore
          if(res.data.tmList[i].tmType == "多选题") {
            html += '<li><h3 class="title"><span>'+(i*1+1)+'、</span>【多选题】'+res.data.tmList[i].tmDesc+/*'【'+res.data.tmList[i].tmScore+'分】*/'</h3>'
              +tplCheckbox(res.data.tmList[i].tmItems,res.data.tmList[i].tmId);
          }else if(res.data.tmList[i].tmType == "单选题") {
            html += '<li><h3 class="title"><span>'+(i*1+1)+'、</span>【单选题】'+res.data.tmList[i].tmDesc+/*'【'+res.data.tmList[i].tmScore+'分】*/'</h3>'
              +tplRadio(res.data.tmList[i].tmItems,res.data.tmList[i].tmId);
          }else if(res.data.tmList[i].tmType == "判断题") {
            html += '<li><h3 class="title"><span>'+(i*1+1)+'、</span>【判断题】'+res.data.tmList[i].tmDesc+/*'【'+res.data.tmList[i].tmScore+'分】*/'</h3>'
            +tplRadio(res.data.tmList[i].tmItems,res.data.tmList[i].tmId)
          }
          html+='<div class="tm-result" id="tm-result-'+res.data.tmList[i].tmId+'"><span class="zqda"></span><span class="ndda"></span></div></li>';
      }
        $(".zfs").html(zfs)
        $(".zts").html(res.data.tmList.length)
        $(".dd").html("0")
        $(".topic-list").html(html)
      } else {
        $("#emptyMsg").html("未生成练习，请重新定义条件");
        layer.msg("未生成练习，请重新定义条件");
      }
    }
  });
}

function start() {
  $(".times").css("cursor", "default").off("click",start);
  startTime=new Date();
  $("#commitSj").removeClass("disable-btn");
  $("input[type='checkbox']").removeAttr("disabled");
  $("input[type='radio']").removeAttr("disabled");
  intervalHandler=window.setInterval(function() {
    count++;
    var s=count%60;
    var tmp=Math.floor(count/60);
    var m=tmp%60;
    tmp=Math.floor(tmp/60);
    var h=tmp%60;
    tmp=((100+h)+"").substring(1)+":"+((100+m)+"").substring(1)+":"+((100+s)+"").substring(1);
    $(".times").html(tmp);
  }, 1000);
}

function commitSj() {
  if($(this).hasClass("disable-btn")) return;
  clearInterval(intervalHandler)
  count = 0
  $(this).addClass('disable-btn');
  $("input[type='checkbox']").attr("disabled", "disabled");
  $("input[type='radio']").attr("disabled", "disabled");

  layer.msg('练习提交中......', {
    icon: 16 ,shade: 0.01
  });
  //获取参数
  var _data={};
  _data.id=sjData.id;
  _data.beginTime=startTime.Format("yyyy-MM-dd hh:mm:ss");
  _data.endTime=(new Date()).Format("yyyy-MM-dd hh:mm:ss");
  _data.resultType=1;

  var allAnswer='';
  var tmList = sjData.tmList
  for(var i = 0;i<tmList.length;i++) {
    var answer="";
    if (tmList[i].tmType=="判断题" || tmList[i].tmType=="单选题") {
      answer=$('input[name="'+tmList[i].tmId+'"]:checked').val();
    }else {
      $('input[name="'+tmList[i].tmId+'"]:checked').each(function(){
        answer+="#"+$(this).val();
      });
      answer=answer.substring(1);
    }
    if (answer) allAnswer+=","+tmList[i].tmId+":"+answer;
    // var oneTm=tmList[i]
    // $("#tm-result-"+oneTm.tmId).find(".zqda").html("正确答案："+oneTm.tmAnswer)
    // $("#tm-result-"+oneTm.tmId).find(".ndda").html("您的答案："+(!allAnswer[oneTm.tmId] ? "未答" : allAnswer[oneTm.tmId]))
  }
  if (allAnswer) allAnswer=allAnswer.substring(1);
  _data.answers=allAnswer;
  $.ajax({
    type: "get",
    url: "./train/commitSj",
    data: _data,
    dataType: "json",
    success:function(res) {
      var oktm = []
      var total = 0
      for(var i = 0;i<res.data.tmList.length;i++) {
        var oneTm=res.data.tmList[i]
        $("#tm-result-"+oneTm.tmId).find(".zqda").html("正确答案："+oneTm.tmAnswer)
        $("#tm-result-"+oneTm.tmId).find(".ndda").html("您的答案："+(oneTm.answer?oneTm.answer:"未答"));
        if(oneTm.tmAnswer==oneTm.answer) {
          $("#tm-result-"+oneTm.tmId).find(".ndda").css("color", "green");
          oktm.push(oneTm)
        }
      }
      for(var i = 0;i<oktm.length;i++) {
        total += oktm[i].tmScore
      }
      $(".dd").html(oktm.length)  //答对题数
//      $(".df").html(total) // 得分
    }
  })
}