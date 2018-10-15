var intervalHandler;
var sjData;
var count=0;

$(function (param) {
  $(".selectpicker").selectpicker()
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
  // 生成试卷
  $(".scsj-btn").on("click",buildPaper);
  // 提交试卷
  $(".sub-btn").on("click",commitSj);
});

function tplCheckbox(res,tmid) {
  var html = ""
  for(var i = 0;i<res.length;i++) {
   html += '<p><span><input type="checkbox" disabled=disabled name="'+tmid+'" value="'+res[i].itemSign+'"></span>'+res[i].itemSign+'、'+res[i].itemDesc+'</p>'
  }   
  return html
}
function tplRadio(res,tmid) {
 var html = ""
  for(var i = 0;i<res.length;i++) {
   html += '<p><span><input type="radio" disabled=disabled name="'+tmid+'" value="'+res[i].itemSign+'"></span>'+res[i].itemSign+'、'+res[i].itemDesc+'</p>'
  }   
  return html
}

function buildPaper() {
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
//    url: "../../demoJson/json143.json",./train/getSj",
    url: "./train/getSj",
    data: _data,
    dataType: "json",
    success: function (res) {
      if(res.returnCode == 00) {
        $(".lxt").html(res.data.name);
        $(".empty").css("display","none")
        $(".lxt").css("display","block")
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
            html += '<li>'
              +'<h3 class="title"><span>'+(i*1+1)+'、</span>【多选题】'+res.data.tmList[i].tmDesc+/*'【'+res.data.tmList[i].tmScore+'分】*/'</h3>'
              +tplCheckbox(res.data.tmList[i].tmItems,res.data.tmList[i].tmId)
              +'<div class="tm-result" id="tm-result-'+res.data.tmList[i].tmId+'"><span class="zqda"></span><span class="ndda"></span></div>'
            +'</li>'
          }else if(res.data.tmList[i].tmType == "单选题") {
            html += '<li>'
            +'<h3 class="title"><span>'+(i*1+1)+'、</span>【单选题】'+res.data.tmList[i].tmDesc+/*'【'+res.data.tmList[i].tmScore+'分】*/'</h3>'
            +'</li>'
            +tplRadio(res.data.tmList[i].tmItems,res.data.tmList[i].tmId)
            +'<div class="tm-result" id="tm-result-'+res.data.tmList[i].tmId+'"><span class="zqda"></span><span class="ndda"></span></div>'
          }else if(res.data.tmList[i].tmType == "判断题") {
            html += '<li>'
            +'<h3 class="title"><span>'+(i*1+1)+'、</span>【判断题】'+res.data.tmList[i].tmDesc+/*'【'+res.data.tmList[i].tmScore+'分】*/'</h3>'
            +'</li>'
            +tplRadio(res.data.tmList[i].tmItems,res.data.tmList[i].tmId)
            +'<div class="tm-result" id="tm-result-'+res.data.tmList[i].tmId+'"><span class="zqda"></span><span class="ndda"></span></div>'
          }
        }
        $(".zfs").html(zfs)
        $(".zts").html(res.data.tmList.length)
        $(".dd").html("0")
        $(".topic-list").html(html)
        // 计时开始
//        intervalHandler=window.setInterval(function() {
//          count++;
//          var s=count%60;
//          var tmp=Math.floor(count/60);
//          var m=tmp%60;
//          tmp=Math.floor(tmp/60);
//          var h=tmp%60;
//          tmp=((100+h)+"").substring(1)+":"+((100+m)+"").substring(1)+":"+((100+s)+"").substring(1);
//          $(".times").html(tmp);
//        }, 1000);
      } else {
        $("#emptyMsg").html("未生成练习，请重新定义条件");
        layer.msg("未生成练习，请重新定义条件");
      }
    }
  });
}

function start() {
  $("#commitSj").removeClass("disable-btn");
  $("input[type='checkbox']").removeAttr("disabled");
  $("input[type='radio']").removeAttr("disabled");
  $(".times").css("cursor", "hand").off("click",start);
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
  _data.beginTime=sjData.beginTime;
  _data.endTime=new Date();
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
  console.log(_data);
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