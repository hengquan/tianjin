$(function() {
  $(".tag-box").on("click","li",function() {
    $(this).addClass("on").siblings("li").removeClass("on")
  })
  $.ajax({
    type: "get",
    url: "../../demoJson/json111.json",
    data: "",
    dataType: "json",
    success: function (res) {
      console.log(res)
      var arr = []
      var html = ""
      for(var i = 0;i<res.data.list.length;i++) {
        if(res.data.list[i].parentId == 0) {
          arr.push(res.data.list[i])
        }
      }
      for(var j=0;j<arr.length;j++) {
        html+= '<li>'+arr[j].name+'</li>'
      }
      $(".tag-box").append(html);
    }
  });
  $.ajax({
    type: "get",
    url: "../../demoJson/json121.json",
    data: "",
    dataType: "json",
    success: function (res) {
        console.log(res)
        if(res.returnCode == 00) {
            var html = ""
            for(var i = 0;i < res.data.length; i++) {
                html += '<li class="col-sm-3">'
                    +'<div class="con">'
                    +'<div class="pic"><img src="'+(res.data[i].imgUrl || "../../tjsrc/images/mnsc_default.jpg")+'"></div>'
                    +'<div class="botoom">'
                        +'<h2>'+res.data[i].name+'</h2>'
                        +'<div class="row-box">'
                          +'<div class="left">'
                            +'<span class="eay"><img src="../../tjsrc/images/rs.jpg"></span>'
                            +'<span class="num">'+res.data[i].visitCount+'</span>'
                          +'</div>'
                          +'<div class="right"><span>'+res.data[i].createDate+'</span>发布</div>'
                        +'</div>'
                      +'</div>'
                    +'</div>'
                +'</li>'
            }
            $(".listbox").append(html)
        }
    }
});
})