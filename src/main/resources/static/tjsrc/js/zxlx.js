$(function (param) { 
  $(".selectpicker").selectpicker()
  $(".selectpicker2").selectpicker()
  $(".selectpicker3").selectpicker()
  $(".widget-sider").sider({
    min:0,
    max:100 //拖动滑块的最大及最小值
  })
  var intervalHandler;
  var sjData;
  var count=0;
  // 生成试卷
  $(".scsj-btn").on("click",function (param) { 
    var type = $(".selectpicker").val() //业务分类value值
    var ny = $(".form-control").val() //难易程度value值
    var num = $(".nums").val()  //  题目数量vaule值
    console.log(num)
    $.ajax({
      type: "get",
      url: "../../demoJson/json142.json",
      data: "",
      dataType: "json",
      success: function (res) {
        console.log(res)
        if(res.returnCode == 00) {
          $(".empty").css("display","none")
          $(".lxt").css("display","block")
          $(".times").css("display","block")
          $(".sub-btn").css("display","block")
          var html = ""
          var zfs = 0
          sjData = res.data
          for(var i=0;i<res.data.tmList.length;i++) {
            zfs+=res.data.tmList[i].tmScore
            if(res.data.tmList[i].tmType == "多选题") {
              html += '<li>'
                +'<h3 class="title"><span>'+(i*1+1)+'、</span>【多选题】'+res.data.tmList[i].tmDesc+'【'+res.data.tmList[i].tmScore+'分】</h3>'
                +tplCheckbox(res.data.tmList[i].tmItems,res.data.tmList[i].tmId)
              +'</li>'
            }else if(res.data.tmList[i].tmType == "单选题") {
              html += '<li>'
              +'<h3 class="title"><span>'+(i*1+1)+'、</span>【单选题】'+res.data.tmList[i].tmDesc+'【'+res.data.tmList[i].tmScore+'分】</h3>'
              +'</li>'
              +tplRadio(res.data.tmList[i].tmItems,res.data.tmList[i].tmId)
            }
            
          }
          $(".zfs").html(zfs)
          $(".zts").html(res.data.tmList.length)
          $(".topic-list").html(html)
          // 计时开始       
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
      }
    });
   })
   function tplCheckbox(res,tmid) {
     var html = ""
     for(var i = 0;i<res.length;i++) {
      html += '<p><span><input type="checkbox" name="'+tmid+'"></span>'+res[i].itemSign+'、'+res[i].itemDesc+'</p>'
     }   
     return html
   }
   function tplRadio(res,tmid) {
    var html = ""
     for(var i = 0;i<res.length;i++) {
      html += '<p><span><input type="radio" name="'+tmid+'"></span>'+res[i].itemSign+'、'+res[i].itemDesc+'</p>'
     }   
     return html
   }
   // 提交试卷
   $(".sub-btn").on("click",function (param) { 
     if($(this).hasClass("disable")) return;
      clearInterval(intervalHandler)
      count = 0
      $(this).addClass('disable')
      console.log(sjData)
      var tmList = sjData.tmList
      for(var i = 0;i<tmList.length;i++) {
        
      }
    })
 })